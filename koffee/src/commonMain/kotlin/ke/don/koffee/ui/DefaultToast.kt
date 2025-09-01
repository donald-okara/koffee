/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.koffee.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ke.don.koffee.domain.style
import ke.don.koffee.helpers.COMPACT_BREAK_POINT
import ke.don.koffee.helpers.sizeMod
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastType
import ke.don.koffee.ui.toasts_suite.ToastContent

/**
 * A composable function that displays a default toast message.
 *
 * This function takes a [ToastData] object as input and displays a toast message with an icon,
 * title, description, and optional action buttons. The appearance of the toast (icon, tint,
 * background color) is determined by the [ToastType] specified in the [ToastData].
 *
 * The layout consists of:
 * - An icon on the left.
 * - A column with the title and description to the right of the icon.
 * - An optional row of action buttons (primary and secondary) below the text.
 *
 * The toast is styled with rounded corners and appropriate padding. Text overflow is handled
 * by truncating with an ellipsis.
 *
 * @param data The [ToastData] object containing the information to display in the toast.
 *             This includes the title, description, [ToastType], and optional
 *             [ToastAction]s for primary and secondary buttons.
 */
@Composable
fun DefaultToast(
    data: ToastData,
    modifier: Modifier = Modifier,
) {
    val (icon, tint, background) = data.type.style

    BoxWithConstraints(modifier = modifier) { // no fill here; respect parent alignment
        val isCompact = maxWidth < COMPACT_BREAK_POINT

        val sizeMod = sizeMod(isCompact)

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = background,
            tonalElevation = 32.dp,
            modifier = sizeMod
                .padding(horizontal = 16.dp, vertical = 8.dp), // inner padding
        ) {
            ToastContent(
                icon = icon,
                tint = tint,
                data = data,
                textColor = Color.Black,
            )
        }
    }
}
