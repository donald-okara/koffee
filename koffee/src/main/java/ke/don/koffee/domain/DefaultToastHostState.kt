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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

/**
 * A default implementation of [ToastHostState] that manages toast display logic,
 * timing, visibility, and dismissal using Compose state and coroutines.
 * This class is not intended for direct instantiation by clients; use [rememberToastHostState]
 * to create and manage instances within a Composable scope.
 *
 * @property scope The [CoroutineScope] used to manage toast lifecycle jobs, especially for auto-dismiss.
 * @property durationResolver A lambda that maps [ToastDuration] values to the actual time in milliseconds
 *                            that a toast should remain visible. If `null` is returned, the toast will persist until dismissed manually.
 * @property maxVisibleToasts The maximum number of visible toasts at any given time. If the limit is reached,
 *                            the oldest toast will be removed to make room for the new one.
 */

internal class DefaultToastHostState internal constructor(
    private val scope: CoroutineScope,
    private val durationResolver: (ToastDuration) -> Long?,
    private val maxVisibleToasts: Int = 1,
) : ToastHostState {

    private val _toasts = mutableStateListOf<ToastData>()

    /**
     * The current list of active toasts being displayed.
     * Automatically recomposes when toasts are added or removed.
     */
    override val toasts: List<ToastData> get() = _toasts

    private val jobs = mutableMapOf<String, Job>()
    private val mutex = Mutex()

    /**
     * Displays a new toast message with the given parameters.
     *
     * If the number of visible toasts exceeds [maxVisibleToasts], the oldest toast is dismissed.
     * If [duration] resolves to a non-null millisecond value using [durationResolver], the toast
     * will automatically dismiss after the specified time.
     *
     * @param title Title text of the toast.
     * @param description Supporting description shown below the title.
     * @param duration The [ToastDuration] enum indicating how long the toast should be visible.
     * @param type The [ToastType] used for visual styling (e.g., Success, Error, Info).
     * @param primaryAction Optional primary action button. Can be configured to dismiss the toast after invocation.
     * @param secondaryAction Optional secondary action button. Behaves similarly to [primaryAction].
     * @sample [ke.don.koffee.sample.SampleUsage.showToastFromHostState]
     */
    override fun show(
        title: String,
        description: String,
        duration: ToastDuration,
        type: ToastType,
        primaryAction: ToastAction?,
        secondaryAction: ToastAction?,
    ) {
        val toastId = UUID.randomUUID().toString()

        val wrappedPrimary = primaryAction?.let {
            ToastAction(it.label, {
                it.onClick()
                if (it.dismissAfter) dismiss(toastId)
            })
        }

        val wrappedSecondary = secondaryAction?.let {
            ToastAction(it.label, {
                it.onClick()
                if (it.dismissAfter) dismiss(toastId)
            })
        }

        val toast = ToastData(
            title = title,
            description = description,
            type = type,
            duration = duration,
            primaryAction = wrappedPrimary,
            secondaryAction = wrappedSecondary,
            id = toastId,
        )

        scope.launch { // Ensure toast count does not exceed the max allowed
            mutex.withLock {
                if (_toasts.size >= maxVisibleToasts) {
                    _toasts.firstOrNull()?.let { dismiss(it.id) }
                }

                _toasts.add(toast)
            }

            // Auto-dismiss if duration is specified
            val millis = durationResolver(duration)
            if (millis != null) {
                jobs[toast.id] = scope.launch {
                    delay(millis)
                    dismiss(toast.id)
                }
            }
        }
    }

    /**
     * Dismisses the toast with the specified [id].
     *
     * Cancels any active timer job associated with the toast and removes it from the UI.
     *
     * @param id The unique identifier of the toast to dismiss.
     * @sample [ke.don.koffee.sample.SampleUsage.dismissToastFromHostState]
     */
    override fun dismiss(id: String) {
        scope.launch {
            mutex.withLock {
                jobs.remove(id)?.cancel()
                _toasts.removeAll { it.id == id }
            }
        }
    }

    /**
     * Dismisses all currently active toasts and cancels their timers.
     * This is typically used for force-clearing the toast state.
     * @sample [ke.don.koffee.sample.SampleUsage.dismissAllFromHostState]
     */
    override fun dismissAll() {
        scope.launch {
            mutex.withLock {
                jobs.values.forEach { it.cancel() }
                jobs.clear()
                _toasts.clear()
            }
        }
    }
}

/**
 * Remembers and provides a [ToastHostState] instance, scoped to the current composition
 * and lifecycle.
 *
 * This utility function simplifies the creation and management of a [ToastHostState]
 * within a `@Composable` scope. The returned state is remembered across recompositions
 * and its coroutine scope is tied to the lifecycle of the [LocalLifecycleOwner],
 * ensuring that any ongoing toast operations are cancelled when the composable is destroyed
 * to prevent leaks.
 *
 * It's typically used when setting up a custom toast host UI, allowing that component
 * to interact with and display toasts managed by this state.
 *
 * @param maxVisibleToasts The maximum number of toasts that can be visible simultaneously.
 *                         Defaults to `3`. If this limit is exceeded, the oldest toast
 *                         will be automatically dismissed to make space for a new one.
 * @param durationResolver A lambda function that takes a [ToastDuration] enum and
 *                         returns the corresponding display time in milliseconds.
 *                         If this function returns `null` for a given duration, the toast
 *                         will remain visible until it is manually dismissed.
 *
 * @return A [ToastHostState] instance that can be used to show, dismiss, or query
 *         the current toasts. The underlying [CoroutineScope] of this state is
 *         automatically cancelled when the composable leaves the composition or its
 *         lifecycle is destroyed.
 *
 */
@Composable
fun rememberToastHostState(
    maxVisibleToasts: Int = 3,
    durationResolver: (ToastDuration) -> Long?,
): ToastHostState {
    val lifecycleOwner = LocalLifecycleOwner.current

    // Create a CoroutineScope tied to Lifecycle
    val lifecycleScope = remember(lifecycleOwner) {
        val job = Job()
        CoroutineScope(Dispatchers.Main + job)
    }

    // Cancel scope when lifecycle is destroyed to avoid leaks
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                lifecycleScope.cancel()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            lifecycleScope.cancel()
        }
    }

    return remember(lifecycleScope, maxVisibleToasts) {
        DefaultToastHostState(lifecycleScope, durationResolver, maxVisibleToasts)
    }
}
