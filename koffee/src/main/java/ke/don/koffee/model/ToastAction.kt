package ke.don.koffee.model

data class ToastAction(
    val label: String,
    val onClick: () -> Unit
)