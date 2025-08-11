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
package ke.don.koffee.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ke.don.koffee.model.KoffeeConfig
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType
import ke.don.koffee.ui.DefaultToast
import ke.don.koffee.ui.ToastHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Central entry point for interacting with the Koffee toast notification system.
 *
 * This singleton provides a simple and unified API for initializing, displaying,
 * and managing toasts throughout your Compose application.
 *
 * It handles toast host configuration, rendering customization, and lifecycle-aware
 * toast presentation using coroutines and Compose state management.
 *
 * ### Usage
 * You typically initialize `Koffee` once at the top level of your app and then
 * use its `show()` method anywhere in your business or UI logic.
 *
 * #### Example - Initialization
 * ```kotlin
 * Koffee.init {
 *     layout { toastData -> CustomToast(toastData) }
 *     dismissible = true
 *     durationResolver = { duration ->
 *         when (duration) {
 *             ToastDuration.Short -> 2000L
 *             ToastDuration.Long -> 4000L
 *             else -> null
 *         }
 *     }
 * }
 * ```
 *
 * #### Example - Setup in Compose
 * ```kotlin
 * @Composable
 * fun App() {
 *     Box{
 *        // your UI
 *        Koffee.Setup() // Call Koffee.Setup() here
 *     }
 * }
 * ```
 *
 * #### Example - Showing a toast
 * ```kotlin
 * Koffee.show(
 *     title = "Success",
 *     description = "Your item was saved.",
 *     type = ToastType.Positive,
 *     primaryAction = ToastAction(label = "Details", onClick = { println("Viewing info details") }),
 *     secondaryAction = ToastAction(label = "Got it", onClick = {  }, dismissAfter = true),
 * )
 * ```
 */
object Koffee {

    /**
     * Holds a reference to the [ToastHostState] that controls toast visibility and queueing.
     */
    private lateinit var toastHostState: ToastHostState

    /**
     * Holds the active toast configuration including layout composable, duration logic,
     * and dismiss behavior.
     */
    private var config: KoffeeConfig = KoffeeConfig(
        layout = { DefaultToast(it) },
        dismissible = true,
    )

    /**
     * Initializes Koffee with custom configuration values.
     *
     * This function should be called once in your application’s startup phase, typically
     * in the root Composable or application class.
     *
     * @param builder A builder block that provides a DSL to define custom toast behavior.
     */
    fun init(builder: KoffeeConfigBuilder.() -> Unit) {
        config = KoffeeConfigBuilder().apply(builder).build()
    }

    /**
     * Sets up the toast rendering system inside your Compose UI.
     *
     * This must be called within your app's `@Composable` hierarchy, ideally near the top
     * of the UI tree, so that toasts can overlay the full screen or any content.
     *
     * @param modifier A [Modifier] applied to the internal [ToastHost].
     * @param maxVisibleToasts Maximum number of simultaneous toasts shown at once.
     * @param hostState The toast host state object to control toast behavior.
     * @param alignment Position of the toast container on screen (e.g. bottom-center).
     */
    @Composable
    fun Setup(
        modifier: Modifier = Modifier,
        maxVisibleToasts: Int = 1,
        hostState: ToastHostState = rememberToastHostState(maxVisibleToasts, config.durationResolver),
        alignment: Alignment = Alignment.BottomCenter,
    ) {
        toastHostState = hostState
        ToastHost(
            modifier = modifier,
            hostState = hostState,
            toast = config.layout,
            dismissible = config.dismissible,
            alignment = alignment,
        )
    }

    /**
     * Displays a toast using the current configuration.
     *
     * This function is safe to call from anywhere (as long as `Koffee.Setup()` has been called),
     * and it launches the toast inside a coroutine. If the app is not visible,
     * the toast will be ignored.
     *
     * @param title The main text of the toast.
     * @param description Supporting text or detail.
     * @param type The visual type of toast (e.g. neutral, positive, warning).
     * @param duration How long the toast should remain visible.
     * @param primaryAction Optional primary button/action.
     * @param secondaryAction Optional secondary button/action.
     * @param isAppVisible Flag indicating whether the toast should be shown.
     * @param coroutineScope The coroutine scope used to dispatch the toast show call.
     */

    @Deprecated(
        message = "No longer requires CoroutineScope. Use the simplified show(...) instead.",
        replaceWith = ReplaceWith(
            "show(title, description, type, duration, primaryAction, secondaryAction, isAppVisible)",
        ),
        level = DeprecationLevel.WARNING,
    )
    fun show(
        title: String,
        description: String,
        type: ToastType = ToastType.Neutral,
        duration: ToastDuration = ToastDuration.Short,
        primaryAction: ToastAction? = null,
        secondaryAction: ToastAction? = null,
        isAppVisible: Boolean = true,
        coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate),
    ) {
        if (!isAppVisible) return

        coroutineScope.launch {
            toastHostState.show(title, description, duration, type, primaryAction, secondaryAction)
        }
    }

    /**
     * Displays a toast using the current configuration.
     *
     * This function is safe to call from anywhere (as long as `Koffee.Setup()` has been called),
     * and it launches the toast inside a coroutine. If the app is not visible,
     * the toast will be ignored.
     *
     * @param title The main text of the toast.
     * @param description Supporting text or detail.
     * @param type The visual type of toast (e.g. neutral, positive, warning).
     * @param duration How long the toast should remain visible.
     * @param primaryAction Optional primary button/action.
     * @param secondaryAction Optional secondary button/action.
     * @param isAppVisible Flag indicating whether the toast should be shown. Defaults to `true`.
     *                     If `false`, the toast will not be displayed.
     */
    fun show(
        title: String,
        description: String,
        type: ToastType = ToastType.Neutral,
        duration: ToastDuration = ToastDuration.Short,
        primaryAction: ToastAction? = null,
        secondaryAction: ToastAction? = null,
        isAppVisible: Boolean = true,
    ) {
        if (!isAppVisible) return

        toastHostState.show(
            title,
            description,
            duration,
            type,
            primaryAction,
            secondaryAction,
        )
    }

    /**
     * Dismisses all currently visible toasts.
     *
     * Use this when navigating away from a screen or resetting UI state.
     */
    fun dismissAll() = toastHostState.dismissAll()
}
