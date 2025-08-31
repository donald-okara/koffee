package previews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastType
import ke.don.koffee.ui.toasts_suite.GlowingSurfaceBox
import ke.don.koffee.ui.toasts_suite.GlowingToast
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun GlowingSurfaceBoxPreview() {
    MaterialTheme {
        GlowingSurfaceBox {
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