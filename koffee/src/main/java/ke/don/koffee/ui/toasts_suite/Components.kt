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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastData

@Composable
fun ToastContent(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    tint: Color,
    textColor: Color? = null,
    data: ToastData,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.End,
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 12.dp),
            )

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = data.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = textColor ?: MaterialTheme.colorScheme.onSurface,
                    ),
                )
                Text(
                    text = data.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = textColor?.copy(alpha = 0.8f) ?: MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )
            }
        }

        if (data.primaryAction != null || data.secondaryAction != null) {
            Spacer(modifier = Modifier.height(8.dp))

            ToastActionRow(
                secondaryAction = data.secondaryAction,
                primaryAction = data.primaryAction,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

/**
 * Composable function that displays a row of actions for a toast message.
 * It can display a primary and/or a secondary action button.
 * If both actions are null, the composable returns early.
 *
 * @param secondaryAction The [ToastAction] for the secondary button. Can be null.
 * @param primaryAction The [ToastAction] for the primary button. Can be null.
 * @param tint The [Color] used to style the buttons.
 */
@Composable
fun ToastActionRow(
    modifier: Modifier = Modifier,
    secondaryAction: ToastAction?,
    primaryAction: ToastAction?,
    tint: Color,
) {
    if (secondaryAction == null && primaryAction == null) return

    val buttonHeight = 32.dp
    val shape = RoundedCornerShape(6.dp)
    val buttonTextStyle = MaterialTheme.typography.labelSmall

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.wrapContentWidth(),
    ) {
        secondaryAction?.let { action ->
            OutlinedButton(
                onClick = action.onClick,
                border = BorderStroke(1.dp, tint.copy(alpha = 0.4f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = tint,
                ),
                shape = shape,
                modifier = Modifier
                    .defaultMinSize(minHeight = buttonHeight)
                    .wrapContentWidth(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            ) {
                Text(
                    text = action.label,
                    style = buttonTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        primaryAction?.let { action ->
            Button(
                onClick = action.onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = tint.copy(alpha = 0.15f),
                    contentColor = tint,
                ),
                shape = shape,
                modifier = Modifier
                    .defaultMinSize(minHeight = buttonHeight)
                    .wrapContentWidth(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            ) {
                Text(
                    text = action.label,
                    style = buttonTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
