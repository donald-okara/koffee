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
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import ke.don.koffee.helpers.toAlignment
import ke.don.koffee.model.ToastAnimation
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastPosition

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun ToastHost(
    hostState: ToastHostState,
    toast: @Composable (ToastData) -> Unit,
    modifier: Modifier = Modifier,
    dismissible: Boolean = true,
    animationStyle: ToastAnimation,
    alignment: ToastPosition,
) {
    val fromBottom = animationStyle == ToastAnimation.SlideUp
    val toasts = hostState.toasts

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 64.dp),
        contentAlignment = alignment.toAlignment(),
    ) {
        LookaheadScope {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                reverseLayout = !fromBottom,
            ) {
                items(
                    items = toasts,
                    key = { it.id },
                ) { data ->
                    ToastItem(
                        data = data,
                        toast = toast,
                        hostState = hostState,
                        dismissible = dismissible,
                        fromBottom = fromBottom,
                    )
                }
            }
        }
    }
}

@Composable
private fun ToastItem(
    data: ToastData,
    toast: @Composable (ToastData) -> Unit,
    hostState: ToastHostState,
    dismissible: Boolean,
    fromBottom: Boolean,
) {
    var visible by remember(data.id) { mutableStateOf(false) }

    // Trigger enter animation
    LaunchedEffect(Unit) { visible = true }

    // Slide direction
    val enterSlide = if (fromBottom) {
        slideInVertically(animationSpec = tween(300)) { it }
    } else {
        slideInVertically(animationSpec = tween(300)) { -it }
    }
    val exitSlide = if (fromBottom) {
        slideOutVertically(animationSpec = tween(200)) { it }
    } else {
        slideOutVertically(animationSpec = tween(200)) { -it }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)) + enterSlide,
        exit = fadeOut(tween(200)) + exitSlide,
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
            content = { toast(data) },
        )
    }
}
