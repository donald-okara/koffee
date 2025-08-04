package ke.don.koffee.domain

import androidx.compose.runtime.Composable
import ke.don.koffee.model.KoffeeConfig
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType
import ke.don.koffee.ui.DefaultToast
import ke.don.koffee.ui.ToastHost

object Koffee {
    private lateinit var toastHostState: ToastHostState
    private var config: KoffeeConfig = KoffeeConfig(
        layout = { DefaultToast(it) },
        dismissible = true
    )

    fun init(builder: KoffeeConfigBuilder.() -> Unit) {
        config = KoffeeConfigBuilder().apply(builder).build()
    }

    @Composable
    fun Setup(
        maxVisibleToasts: Int = 1,
        hostState: ToastHostState = rememberToastHostState(maxVisibleToasts, config),
    ) {
        toastHostState = hostState
        ToastHost(
            hostState = hostState,
            toast = config.layout,
            dismissible = config.dismissible
        )
    }

    fun show(
        title: String,
        description: String,
        type: ToastType = ToastType.Neutral,
        duration: ToastDuration = ToastDuration.Short,
        primaryAction: ToastAction? = null,
        secondaryAction: ToastAction? = null,
        isAppVisible: Boolean = true
    ) {
        if (!isAppVisible) return

        toastHostState.show(title, description, duration, type, primaryAction, secondaryAction)
    }


    fun dismiss(id: String) {
        toastHostState.dismiss(id)
    }

    fun dismissAll() {
        toastHostState.dismissAll()
    }
}
