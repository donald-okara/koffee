package ke.don.koffee.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import ke.don.koffee.ui.DefaultToast

object KoffeeDefaults {
    private val layout: @Composable (ToastData) -> Unit = { DefaultToast(it) }
    private val dismissible: Boolean = true
    private val durationResolver: (ToastDuration) -> Long? = { defaultDurationResolver(it) }
    private val maxVisibleToasts: Int = 1
    private val position: ToastPosition = ToastPosition.BottomCenter

    val config = KoffeeConfig(
        layout = layout,
        dismissible = dismissible,
        durationResolver = durationResolver,
        maxVisibleToasts = maxVisibleToasts,
        position = position,
        animationStyle = when (position) {
            ToastPosition.BottomStart,
            ToastPosition.BottomCenter,
            ToastPosition.BottomEnd,
                -> ToastAnimation.SlideUp

            else -> ToastAnimation.SlideDown
        }
    )
}