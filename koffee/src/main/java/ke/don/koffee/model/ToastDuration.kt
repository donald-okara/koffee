package ke.don.koffee.model

enum class ToastDuration{ Short, Long, Indefinite }


fun defaultDurationResolver(duration: ToastDuration): Long? = when (duration) {
    ToastDuration.Short -> 2000L
    ToastDuration.Long -> 3500L
    ToastDuration.Indefinite -> null
}
