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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ke.don.koffee.domain.style
import ke.don.koffee.helpers.COMPACT_BREAK_POINT
import ke.don.koffee.helpers.sizeMod
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastType

@Composable
fun GlowingToast(
    data: ToastData,
) {
    val (icon, tint) = data.type.style

    GlowingSurfaceBox(
        glowColor = tint,
        cornerRadius = 12.dp,
        glowRadius = 16.dp,
        modifier = Modifier
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
fun GlowingSurfaceBox(
    modifier: Modifier = Modifier,
    glowColor: Color = MaterialTheme.colorScheme.primary,
    cornerRadius: Dp = 12.dp,
    glowRadius: Dp = 16.dp,
    content: @Composable () -> Unit = {},
) {
    BoxWithConstraints(modifier = modifier) { // no fill here; respect parent alignment
        val isCompact = maxWidth < COMPACT_BREAK_POINT

        val sizeMod = sizeMod(isCompact)
        Box(
            modifier = sizeMod
                .shadow(
                    elevation = glowRadius,
                    shape = RoundedCornerShape(cornerRadius),
                    spotColor = glowColor.copy(alpha = 0.5f),
                )
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(cornerRadius),
                )
                .border(
                    width = 2.dp,
                    color = glowColor.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(cornerRadius),
                ),
        ) {
            content()
        }
    }

}


