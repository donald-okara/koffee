package ke.don.koffee.domain

import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType

interface ToastHostState {
    val toasts: List<ToastData>

    fun show(
        title: String,
        description: String,
        duration: ToastDuration = ToastDuration.Short,
        type: ToastType,
        primaryAction: ToastAction? = null,
        secondaryAction: ToastAction? = null
    )

    fun dismiss(id: String)
    fun dismissAll()
}
