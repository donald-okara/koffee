package ke.don.koffee.model

import java.util.UUID

data class ToastData(
    val title: String,
    val description: String,
    val duration: ToastDuration = ToastDuration.Short,
    val id: String = UUID.randomUUID().toString()
)
