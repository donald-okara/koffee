package ke.don.koffee.ui.toasts_suite

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastType

@Composable
fun GlowingToast(
    data: ToastData,
){
    val (icon, tint) = when (data.type) {
        ToastType.Neutral -> Pair(Icons.Default.Info, Color(0xFF6E6E6E))
        ToastType.Info -> Pair(Icons.Default.Notifications, Color(0xFF2962FF))
        ToastType.Success -> Pair(Icons.Default.CheckCircle, Color(0xFF2E7D32))
        ToastType.Warning -> Pair(Icons.Default.Warning, Color(0xFFFFA000))
        ToastType.Error -> Pair(Icons.Default.Error, Color(0xFFC62828))
    }

    GlowingSurfaceBox(
        glowColor = tint,
        cornerRadius = 12.dp,
        glowRadius = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        ToastContent(
            icon = icon,
            tint = tint,
            data = data
        )
    }


}

@Composable
fun GlowingSurfaceBox(
    modifier: Modifier = Modifier,
    glowColor: Color = MaterialTheme.colorScheme.primary,
    cornerRadius: Dp = 12.dp,
    glowRadius: Dp = 16.dp,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = glowRadius,
                shape = RoundedCornerShape(cornerRadius),
                spotColor = glowColor.copy(alpha = 0.5f)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(cornerRadius)
            )
            .border(
                width = 2.dp,
                color = glowColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        content()
    }
}


@Preview
@Composable
fun GlowingSurfaceBoxPreview() {
    MaterialTheme {
        GlowingSurfaceBox{
            Text(text = "This is some text")
        }
    }
}

@Preview
@Composable
fun GlowingToastPreview() {
    MaterialTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
        ) {
            GlowingToast(
                ToastData(
                    title = "Info",
                    description = "This is an info toast",
                    type = ToastType.Info,
                    primaryAction = ToastAction("Details", { println("Info > Details") }),
                    secondaryAction = ToastAction("Dismiss", { println("Info > Dismiss") }),
                ),
            )

            GlowingToast(
                ToastData(
                    title = "Neutral",
                    description = "This is a neutral toast",
                    type = ToastType.Neutral,
                    primaryAction = ToastAction("Open", { println("Neutral > Open") }),
                    secondaryAction = ToastAction("Dismiss", { println("Neutral > Dismiss") }),
                ),
            )

            GlowingToast(
                ToastData(
                    title = "Success",
                    description = "This is a success toast",
                    type = ToastType.Success,
                    primaryAction = ToastAction("Celebrate", { println("Success > Celebrate") }),
                    secondaryAction = ToastAction("Dismiss", { println("Success > Dismiss") }),
                ),
            )

            GlowingToast(
                ToastData(
                    title = "Warning",
                    description = "This is a warning toast",
                    type = ToastType.Warning,
                    primaryAction = ToastAction("Fix", { println("Warning > Fix") }),
                    secondaryAction = ToastAction("Ignore", { println("Warning > Ignore") }),
                ),
            )

            GlowingToast(
                ToastData(
                    title = "Error",
                    description = "This is an error toast",
                    type = ToastType.Error,
                    primaryAction = ToastAction("Retry", { println("Error > Retry") }),
                    secondaryAction = ToastAction("Report", { println("Error > Report") }),
                ),
            )
        }
    }

}