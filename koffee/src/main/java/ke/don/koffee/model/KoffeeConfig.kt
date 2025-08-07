/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ke.don.koffee.model

import androidx.compose.runtime.Composable

/**
 * Configuration for the Koffee toasts.
 *
 * @property layout The composable function to be used for the toast layout. It takes a [ToastData]
 * object as a parameter, which contains the data to be displayed in the toast.
 * @property dismissible A boolean value indicating whether the toast can be dismissed by the user.
 * If true, the toast can be dismissed by swiping it away.
 * @property durationResolver A function that resolves the duration of the toast. It takes a
 * [ToastDuration] object as a parameter and returns the duration in milliseconds. If null is
 * returned, the toast will be displayed indefinitely until dismissed by the user. The default
 * duration resolver uses the [defaultDurationResolver] function.
 */
data class KoffeeConfig(
    val layout: @Composable (ToastData) -> Unit,
    val dismissible: Boolean,
    val durationResolver: (ToastDuration) -> Long? = { defaultDurationResolver(it) },
)
