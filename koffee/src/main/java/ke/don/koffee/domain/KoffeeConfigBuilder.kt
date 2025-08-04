package ke.don.koffee.domain

import androidx.compose.runtime.Composable
import ke.don.koffee.model.KoffeeConfig
import ke.don.koffee.model.ToastData
import ke.don.koffee.ui.DefaultToast

class KoffeeConfigBuilder {
    private var layout: @Composable (ToastData) -> Unit = { DefaultToast(it) }
    private var dismissible: Boolean = true

    fun layout(content: @Composable (ToastData) -> Unit) {
        layout = content
    }

    fun dismissible(value: Boolean) {
        dismissible = value
    }

    fun build(): KoffeeConfig = KoffeeConfig(
        layout = layout,
        dismissible = dismissible
    )
}
