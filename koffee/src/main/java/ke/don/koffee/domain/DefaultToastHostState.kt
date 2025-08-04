package ke.don.koffee.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import ke.don.koffee.model.KoffeeConfig
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType
import ke.don.koffee.model.toMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class DefaultToastHostState internal constructor(
    private val scope: CoroutineScope,
    private val config: KoffeeConfig,
    private val maxVisibleToasts: Int = 1
):ToastHostState {
    private val _toasts = mutableStateListOf<ToastData>()
    override val toasts: List<ToastData> get() = _toasts

    private val jobs = mutableMapOf<String, Job>()

    override fun show(
        title: String,
        description: String,
        duration: ToastDuration,
        type: ToastType,
        primaryAction: ToastAction?,
        secondaryAction: ToastAction?,
    ) {
        val toastId = UUID.randomUUID().toString()

        val wrappedPrimary = primaryAction?.let {
            ToastAction(it.label) {
                it.onClick()
                dismiss(toastId)
            }
        }

        val wrappedSecondary = secondaryAction?.let {
            ToastAction(it.label) {
                it.onClick()
                dismiss(toastId)
            }
        }

        val toast = ToastData(
            title = title,
            description = description,
            type = type,
            duration = duration,
            primaryAction = wrappedPrimary,
            secondaryAction = wrappedSecondary,
            id = toastId
        )

        if (_toasts.size >= maxVisibleToasts) {
            _toasts.firstOrNull()?.let { dismiss(it.id) }
        }

        _toasts.add(toast)

        val millis = config.durationResolver(duration)
        if (millis != null) {
            jobs[toast.id] = scope.launch {
                delay(millis)
                dismiss(toast.id)
            }
        }
    }


    override fun dismiss(id: String) {
        jobs.remove(id)?.cancel()
        _toasts.removeAll { it.id == id }
    }

    override fun dismissAll() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
        _toasts.clear()
    }
}

@Composable
fun rememberToastHostState(
    maxVisibleToasts: Int = 3,
    config: KoffeeConfig
): ToastHostState {
    val scope = rememberCoroutineScope()
    return remember(scope, maxVisibleToasts) {
        DefaultToastHostState(scope, config, maxVisibleToasts)
    }
}

@Composable
fun rememberIsAppVisible(): Boolean {
    val lifecycleOwner = LocalLifecycleOwner.current
    var isVisible by remember { mutableStateOf(true) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            isVisible = event == Lifecycle.Event.ON_START || event == Lifecycle.Event.ON_RESUME
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    return isVisible
}
