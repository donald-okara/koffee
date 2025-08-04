package ke.don.koffee.model

import androidx.compose.runtime.Composable

data class KoffeeConfig(
    val layout: @Composable (ToastData) -> Unit,
    val dismissible: Boolean
)
