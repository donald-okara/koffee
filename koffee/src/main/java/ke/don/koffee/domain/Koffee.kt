package ke.don.koffee.domain

import androidx.compose.runtime.Composable
import ke.don.koffee.model.KoffeeConfig
import ke.don.koffee.model.ToastDuration
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
    fun Setup(maxVisibleToasts: Int = 1) {
        toastHostState = rememberToastHostState(maxVisibleToasts)
        ToastHost(
            hostState = toastHostState,
            toast = config.layout,
            dismissible = config.dismissible
        )
    }

    fun show(title: String, description: String, duration: ToastDuration = ToastDuration.Short) {
        toastHostState.show(title, description, duration)
    }

    fun dismiss(id: String) {
        toastHostState.dismiss(id)
    }

    fun dismissAll() {
        toastHostState.dismissAll()
    }
}
