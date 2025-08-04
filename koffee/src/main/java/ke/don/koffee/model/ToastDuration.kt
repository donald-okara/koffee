package ke.don.koffee.model

enum class ToastDuration{ Short, Medium, Long, Indefinite }


fun defaultDurationResolver(duration: ToastDuration): Long? = when (duration) {
    ToastDuration.Short -> 2000L
    ToastDuration.Medium -> 3500L
    ToastDuration.Long -> 5000L
    ToastDuration.Indefinite -> null
}
