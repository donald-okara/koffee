package ke.don.koffee.model

enum class ToastDuration{ Short, Long }

fun ToastDuration.toMillis(): Long = when (this) {
    ToastDuration.Short -> 2000L
    ToastDuration.Long -> 3500L
}
