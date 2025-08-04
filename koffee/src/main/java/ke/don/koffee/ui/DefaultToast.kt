package ke.don.koffee.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastType

@Composable
fun DefaultToast(
    data: ToastData,
) {
    val (icon, tint, background) = when (data.type) {
        ToastType.Neutral -> Triple(Icons.Default.Info, Color(0xFF6E6E6E), Color(0xFFF0F0F0))
        ToastType.Info -> Triple(Icons.Default.Notifications, Color(0xFF2962FF), Color(0xFFE3F2FD))
        ToastType.Success -> Triple(Icons.Default.CheckCircle, Color(0xFF2E7D32), Color(0xFFE6F4EA))
        ToastType.Warning -> Triple(Icons.Default.Warning, Color(0xFFFFA000), Color(0xFFFFF3E0))
        ToastType.Error -> Triple(Icons.Default.Error, Color(0xFFC62828), Color(0xFFFFEBEE))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = background, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    )
                    Text(
                        text = data.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.DarkGray
                        )
                    )
                }

            }

            if (data.primaryAction != null || data.secondaryAction != null) {
                Spacer(modifier = Modifier.height(8.dp))

                ToastActionRow(
                    secondaryAction = data.secondaryAction,
                    primaryAction = data.primaryAction,
                    tint = tint
                )
            }
        }

    }
}

@Composable
fun ToastActionRow(
    secondaryAction: ToastAction?,
    primaryAction: ToastAction?,
    tint: Color
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val buttonHeight = 36.dp
        val buttonTextStyle = MaterialTheme.typography.labelSmall

        secondaryAction?.let { action ->
            OutlinedButton(
                onClick = action.onClick,
                border = BorderStroke(1.dp, tint.copy(alpha = 0.5f)), // outline color
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = tint            // text/icon color
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(buttonHeight),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(action.label, style = buttonTextStyle)
            }
        }

        primaryAction?.let { action ->
            Button(
                onClick = action.onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = tint.copy(alpha = 0.3f),
                    contentColor = tint
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(buttonHeight),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(action.label, style = buttonTextStyle)
            }
        }
    }
}


@Preview
@Composable
fun DefaultToastPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        DefaultToast(
            ToastData(
                title = "Info",
                description = "This is an info toast",
                type = ToastType.Info,
                primaryAction = ToastAction("Details") { println("Info > Details") },
                secondaryAction = ToastAction("Dismiss") { println("Info > Dismiss") }
            )
        )

        DefaultToast(
            ToastData(
                title = "Neutral",
                description = "This is a neutral toast",
                type = ToastType.Neutral,
                primaryAction = ToastAction("Open") { println("Neutral > Open") },
                secondaryAction = ToastAction("Dismiss") { println("Neutral > Dismiss") }
            )
        )

        DefaultToast(
            ToastData(
                title = "Success",
                description = "This is a success toast",
                type = ToastType.Success,
                primaryAction = ToastAction("Celebrate") { println("Success > Celebrate") },
                secondaryAction = ToastAction("Dismiss") { println("Success > Dismiss") }
            )
        )

        DefaultToast(
            ToastData(
                title = "Warning",
                description = "This is a warning toast",
                type = ToastType.Warning,
                primaryAction = ToastAction("Fix") { println("Warning > Fix") },
                secondaryAction = ToastAction("Ignore") { println("Warning > Ignore") }
            )
        )

        DefaultToast(
            ToastData(
                title = "Error",
                description = "This is an error toast",
                type = ToastType.Error,
                primaryAction = ToastAction("Retry") { println("Error > Retry") },
                secondaryAction = ToastAction("Report") { println("Error > Report") }
            )
        )
    }

}