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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ke.don.experimental_annotations.ExperimentalKoffeeApi
import ke.don.koffee.domain.Koffee
import ke.don.koffee.domain.ToastHostState
import ke.don.koffee.domain.rememberToastHostState
import ke.don.koffee.model.KoffeeConfig
import ke.don.koffee.model.KoffeeDefaults

/**
 * A composable function that displays a toast notification bar.
 *
 * @param modifier The modifier to be applied to the KoffeeBar.
 * @param config The configuration for the KoffeeBar. Defaults to [KoffeeDefaults.config].
 * @param hostState The state of the toast host. Defaults to a new [ToastHostState] instance
 *                  initialized with the `maxVisibleToasts` and `durationResolver` from the [config].
 *                  This allows for managing and controlling the displayed toasts.
 * @param content The composable content to be displayed within the main area of the screen,
 *                behind the toast notifications.
 */
@Composable
@ExperimentalKoffeeApi
fun KoffeeBar(
    modifier: Modifier = Modifier,
    config: KoffeeConfig = KoffeeDefaults.config,
    hostState: ToastHostState = rememberToastHostState(
        maxVisibleToasts = config.maxVisibleToasts,
        durationResolver = config.durationResolver,
    ),
    content: @Composable () -> Unit,
) {
    Koffee.attachHostState(hostState)
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        content() // This will render the passed composable content

        ToastHost(
            hostState = hostState,
            toast = config.layout,
            dismissible = config.dismissible,
            alignment = config.position,
            animationStyle = config.animationStyle,
        )
    }
}
