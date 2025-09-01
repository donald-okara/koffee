/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.koffee.domain

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ke.don.koffee.model.ToastType

data class ToastStyle(
    val icon: ImageVector,
    val tint: Color,
    val background: Color,
)

val ToastType.style: ToastStyle
    get() = when (this) {
        ToastType.Neutral -> ToastStyle(Icons.Default.Info, Color(0xFF6E6E6E), Color(0xFFF0F0F0))
        ToastType.Info -> ToastStyle(Icons.Default.Notifications, Color(0xFF2962FF), Color(0xFFE3F2FD))
        ToastType.Success -> ToastStyle(Icons.Default.CheckCircle, Color(0xFF2E7D32), Color(0xFFE6F4EA))
        ToastType.Warning -> ToastStyle(Icons.Default.Warning, Color(0xFFFFA000), Color(0xFFFFF3E0))
        ToastType.Error -> ToastStyle(Icons.Default.Error, Color(0xFFC62828), Color(0xFFFFEBEE))
    }
