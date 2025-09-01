/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.koffee.helpers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val COMPACT_BREAK_POINT = 600.dp
val MAX_NON_COMPACT_WIDTH = 420.dp

fun sizeMod(isCompact: Boolean): Modifier {
    return if (isCompact) {
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // outer margin on mobile
    } else {
        Modifier
            .wrapContentWidth()
            .widthIn(max = MAX_NON_COMPACT_WIDTH) // cap on larger screens
    }
}
