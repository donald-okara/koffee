/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.koffee.model

/**
 * Enum class representing the duration for which a toast message should be displayed.
 *
 * @property Short A short duration for the toast message.
 * @property Medium A medium duration for the toast message.
 * @property Long A long duration for the toast message.
 * @property Indefinite The toast message will be displayed indefinitely until dismissed.
 */
enum class ToastDuration { Short, Medium, Long, Indefinite }

/**
 * A default resolver for [ToastDuration] to a [Long] value in milliseconds.
 * - [ToastDuration.Short] is resolved to `2000L` (2 seconds).
 * - [ToastDuration.Medium] is resolved to `3500L` (3.5 seconds).
 * - [ToastDuration.Long] is resolved to `5000L` (5 seconds).
 * - [ToastDuration.Indefinite] is resolved to `null`, indicating the toast should not dismiss automatically.
 *
 * @param duration The [ToastDuration] to resolve.
 * @return The duration in milliseconds, or `null` if the duration is indefinite.
 */
fun defaultDurationResolver(duration: ToastDuration): Long? = when (duration) {
    ToastDuration.Short -> 2000L
    ToastDuration.Medium -> 3500L
    ToastDuration.Long -> 5000L
    ToastDuration.Indefinite -> null
}
