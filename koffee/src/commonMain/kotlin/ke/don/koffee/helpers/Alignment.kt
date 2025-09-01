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

import androidx.compose.ui.Alignment
import ke.don.koffee.model.ToastPosition

fun ToastPosition.toAlignment(): Alignment = when (this) {
    ToastPosition.TopStart -> Alignment.TopStart
    ToastPosition.TopCenter -> Alignment.TopCenter
    ToastPosition.TopEnd -> Alignment.TopEnd
    ToastPosition.CenterStart -> Alignment.CenterStart
    ToastPosition.Center -> Alignment.Center
    ToastPosition.CenterEnd -> Alignment.CenterEnd
    ToastPosition.BottomStart -> Alignment.BottomStart
    ToastPosition.BottomCenter -> Alignment.BottomCenter
    ToastPosition.BottomEnd -> Alignment.BottomEnd
}

fun Alignment.toToastPosition(): ToastPosition = when (this) {
    Alignment.TopStart -> ToastPosition.TopStart
    Alignment.TopCenter -> ToastPosition.TopCenter
    Alignment.TopEnd -> ToastPosition.TopEnd
    Alignment.CenterStart -> ToastPosition.CenterStart
    Alignment.Center -> ToastPosition.Center
    Alignment.CenterEnd -> ToastPosition.CenterEnd
    Alignment.BottomStart -> ToastPosition.BottomStart
    Alignment.BottomCenter -> ToastPosition.BottomCenter
    Alignment.BottomEnd -> ToastPosition.BottomEnd
    else -> ToastPosition.BottomCenter // fallback default
}
