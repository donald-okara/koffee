/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.koffee.ui.toasts_suite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ke.don.koffee.domain.style
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastType

@Composable
fun GradientToast(
    data: ToastData,
) {
    val (icon, tint) = data.type.style

    GradientSurfaceBox(
        hueColor = tint,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        ToastContent(
            icon = icon,
            tint = tint,
            data = data,
        )
    }
}

@Composable
fun GradientSurfaceBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    glowRadius: Dp = 16.dp,
    hueColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable () -> Unit = {},
) {
    val shape = RoundedCornerShape(cornerRadius)
    Surface(
        shape = shape,
        tonalElevation = 0.dp,
        color = MaterialTheme.colorScheme.surface, // important: allow gradient to show
        shadowElevation = 0.dp,
        modifier = modifier
            .shadow(
                elevation = glowRadius,
                shape = shape,
                spotColor = hueColor.copy(alpha = 0.5f),
            ),
    ) {
        // Use Box to draw the gradient inside Surface
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            hueColor.copy(alpha = 0.3f),
                            hueColor.copy(alpha = 0.2f),
                            hueColor.copy(alpha = 0.1f),
                            hueColor.copy(alpha = 0f),
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(300f, 0f), // adjust or make dynamic
                    ),
                    shape = shape,
                ),
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun GradientSurfaceBoxPreview() {
    MaterialTheme {
        GradientSurfaceBox {
            Text(text = "This is some text")
        }
    }
}

@Preview
@Composable
fun GradientToastPreview() {
    MaterialTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
        ) {
            GradientToast(
                ToastData(
                    title = "Info",
                    description = "This is an info toast",
                    type = ToastType.Info,
                    primaryAction = ToastAction("Details", { println("Info > Details") }),
                    secondaryAction = ToastAction("Dismiss", { println("Info > Dismiss") }),
                ),
            )

            GradientToast(
                ToastData(
                    title = "Neutral",
                    description = "This is a neutral toast",
                    type = ToastType.Neutral,
                    primaryAction = ToastAction("Open", { println("Neutral > Open") }),
                    secondaryAction = ToastAction("Dismiss", { println("Neutral > Dismiss") }),
                ),
            )

            GradientToast(
                ToastData(
                    title = "Success",
                    description = "This is a success toast",
                    type = ToastType.Success,
                    primaryAction = ToastAction("Celebrate", { println("Success > Celebrate") }),
                    secondaryAction = ToastAction("Dismiss", { println("Success > Dismiss") }),
                ),
            )

            GradientToast(
                ToastData(
                    title = "Warning",
                    description = "This is a warning toast",
                    type = ToastType.Warning,
                    primaryAction = ToastAction("Fix", { println("Warning > Fix") }),
                    secondaryAction = ToastAction("Ignore", { println("Warning > Ignore") }),
                ),
            )

            GradientToast(
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
