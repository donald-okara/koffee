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

import androidx.compose.runtime.Composable
import ke.don.koffee.model.KoffeeConfig
import ke.don.koffee.model.KoffeeDefaults
import ke.don.koffee.model.ToastAnimation
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastPosition
import ke.don.koffee.model.defaultDurationResolver
import ke.don.koffee.ui.DefaultToast

/**
 * Builder for creating a [KoffeeConfig] instance with custom configurations for Koffee toasts.
 *
 * This class provides a fluent API to customize various aspects of Koffee toasts,
 * such as their appearance, dismiss behavior, and duration.
 *
 * The following properties can be configured:
 * - **layout**: Defines a custom composable function to render the toast UI. This allows for complete
 *   control over the visual presentation of toasts. By default, [DefaultToast] is used.
 * - **dismissible**: A boolean flag indicating whether toasts can be dismissed by user interaction
 *   (e.g., tapping on the toast). If `false`, toasts will only disappear after their specified duration.
 *   Defaults to `true`.
 * - **durationResolver**: A lambda function that determines the duration (in milliseconds) for
 *   each [ToastDuration] level (e.g., `Short`, `Long`). This allows for customizing how long
 *   different types of toasts remain visible. By default, [defaultDurationResolver] is used.
 *
 * **Example Usage:**
 *
 * ```kotlin
 * val customKoffeeConfig = KoffeeConfigBuilder().apply {
 *     // Set a custom composable for the toast layout
 *     layout { toastData ->
 *         MyCustomToastUI(message = toastData.message, type = toastData.type)
 *     }
 *
 *     // Make toasts non-dismissible by user interaction
 *     dismissible(false)
 *
 *     // Define custom durations for Short and Long toasts
 *     durationResolver { duration ->
 *         when (duration) {
 *             ToastDuration.Short -> 2000L // 2 seconds for short toasts
 *             ToastDuration.Long -> 5000L  // 5 seconds for long toasts
 *         }
 *     }
 * }.build()
 *
 * // Initialize Koffee with the custom configuration
 * Koffee.initialize(context, customKoffeeConfig)
 * ```
 *
 * After building the [KoffeeConfig] instance, it can be passed to `Koffee.initialize()`
 * to apply the custom settings globally for all subsequent Koffee toasts.
 */
class KoffeeConfigBuilder {
    private var config: KoffeeConfig = KoffeeDefaults.config

    private fun updateConfig(transform: (KoffeeConfig) -> KoffeeConfig){
        config = transform(config)
    }
    /**
     * Sets the custom toast layout to be used by Koffee.
     *
     * This allows you to fully customize the appearance and behavior of the toast.
     * You receive the [ToastData] and are expected to render a composable.
     *
     * Example:
     * ```
     * layout { toastData ->
     *     MyCustomToast(toastData)
     * }
     * ```
     *
     * @param content The composable lambda representing the toast UI.
     */
    fun layout(content: @Composable (ToastData) -> Unit) {
        updateConfig {
            it.copy(
                layout = content
            )
        }
    }

    /**
     * Sets whether the toast can be dismissed by user interaction (e.g. tap).
     *
     * If set to `false`, the toast will only disappear after the configured duration.
     *
     * @param value `true` if the toast can be dismissed, `false` otherwise.
     */
    fun dismissible(value: Boolean) {
        updateConfig {
            it.copy(dismissible = value)
        }
    }

    /**
     * Sets the duration resolver for the toast.
     *
     * This function defines how long a toast with a given [ToastDuration] should stay visible.
     * You can override the default duration values with your own custom logic.
     *
     * Example:
     * ```
     * durationResolver {
     *     when (it) {
     *         ToastDuration.Short -> 1500L
     *         ToastDuration.Long -> 3500L
     *     }
     * }
     * ```
     *
     * @param resolver Lambda that receives a [ToastDuration] and returns a duration in milliseconds.
     */
    fun durationResolver(resolver: (ToastDuration) -> Long?) {
        updateConfig {
            it.copy(durationResolver = resolver)
        }
    }


    /**
     * Sets the maximum number of toasts that can be visible simultaneously.
     *
     * If more toasts are added beyond this number, the oldest toast will be automatically dismissed
     * to make room for the new one.
     *
     * @param number Maximum visible toasts. Must be greater than 0.
     */
    fun maxVisibleToasts(number: Int){
        updateConfig { it.copy(maxVisibleToasts = number) }
    }

    /**
     * Sets the screen position of the KoffeeBar where toasts will appear.
     *
     * Options include top, bottom, center, or corners depending on [ToastPosition].
     *
     * @param position Desired toast position on the screen.
     */
    fun position(position: ToastPosition) {
        updateConfig { it.copy(position = position) }
    }

    /**
     * Sets the animation style for toast entry and exit.
     *
     * Options include fade, slide up/down, defined in [ToastAnimation].
     *
     * @param animation The animation style to use for toasts.
     */
    fun animationStyle(animation: ToastAnimation) {
        updateConfig { it.copy(animationStyle = animation) }
    }


    /**
     * Builds the [KoffeeConfig] instance with the current configuration settings.
     *
     * @return The configured [KoffeeConfig] instance.
     */
    fun build(): KoffeeConfig = config
}
