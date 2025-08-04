package ke.don.koffee.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.toMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ToastHostState internal constructor(
    private val scope: CoroutineScope,
    private val maxVisibleToasts: Int = 1
) {
    private val _toasts = mutableStateListOf<ToastData>()
    val toasts: List<ToastData> get() = _toasts

    private val jobs = mutableMapOf<String, Job>()

    fun show(title: String, description: String, duration: ToastDuration = ToastDuration.Short) {
        val toast = ToastData(title, description, duration)

        if (_toasts.size >= maxVisibleToasts) {
            val toDismiss = _toasts.firstOrNull()
            if (toDismiss != null) dismiss(toDismiss.id)
        }

        _toasts.add(toast)

        jobs[toast.id] = scope.launch {
            delay(duration.toMillis())
            dismiss(toast.id)
        }
    }

    fun dismiss(id: String) {
        jobs.remove(id)?.cancel()
        _toasts.removeAll { it.id == id }
    }

    fun dismissAll() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
        _toasts.clear()
    }
}

@Composable
fun rememberToastHostState(
    maxVisibleToasts: Int = 3
): ToastHostState {
    val scope = rememberCoroutineScope()
    return remember(scope, maxVisibleToasts) {
        ToastHostState(scope, maxVisibleToasts)
    }
}
