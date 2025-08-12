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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.unit.dp
import ke.don.koffee.domain.ToastHostState
import ke.don.koffee.model.ToastData

/**
 * Composable function that displays toasts managed by a [ToastHostState].
 *
 * Toasts are displayed in a [Column] within a [Box] that fills the maximum size.
 * Each toast can be dismissed by swiping if [dismissible] is true.
 * Toasts have enter and exit animations.
 *
 * @param hostState The [ToastHostState] that manages the toasts to be displayed.
 * @param toast A composable lambda that defines how each [ToastData] should be rendered.
 * @param modifier Optional [Modifier] to be applied to the toast host container.
 * @param dismissible A boolean indicating whether toasts can be dismissed by swiping. Defaults to true.
 * @param alignment The [Alignment] of the toasts within the host. Defaults to [Alignment.BottomCenter].
 *                  Toasts will appear from the bottom if alignment is `BottomStart`, `BottomCenter`, or `BottomEnd`.
 *                  Otherwise, they will appear from the top.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun ToastHost(
    hostState: ToastHostState,
    toast: @Composable (ToastData) -> Unit,
    modifier: Modifier = Modifier,
    dismissible: Boolean = true,
    alignment: Alignment = Alignment.BottomCenter,
) {
    val fromBottom = when (alignment) {
        Alignment.BottomStart,
        Alignment.BottomCenter,
        Alignment.BottomEnd,
        -> true

        else -> false
    }

    val toasts = if (fromBottom) {
        hostState.toasts
    } else {
        hostState.toasts.asReversed()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 64.dp),
        contentAlignment = alignment,
    ) {
        LookaheadScope {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                toasts.forEach { data ->
                    key(data.id) {
                        var visible by remember(data.id) { mutableStateOf(false) }

                        // Trigger enter animation after composition
                        LaunchedEffect(Unit) {
                            visible = true
                        }

                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it },
                            exit = fadeOut(tween(200)) + slideOutVertically(tween(200)) { it },
                        ) {
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = {
                                    if (it == SwipeToDismissBoxValue.StartToEnd || it == SwipeToDismissBoxValue.EndToStart) {
                                        visible = false
                                        hostState.dismiss(data.id)
                                    }
                                    true
                                },
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = dismissible,
                                enableDismissFromEndToStart = dismissible,
                                backgroundContent = {},
                                modifier = Modifier.animateBounds(this@LookaheadScope), // ✅ apply here
                                content = {
                                    toast(data)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
