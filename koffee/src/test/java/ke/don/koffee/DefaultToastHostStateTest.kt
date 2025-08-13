/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.koffee

import junit.framework.TestCase.assertTrue
import ke.don.koffee.domain.DefaultToastHostState
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [DefaultToastHostState], verifying toast lifecycle behavior,
 * including appearance, dismissal, queue limits, and action callbacks.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DefaultToastHostStateTest {

    /**
     * A test fixed duration 50ms.
     */

    val durationResolver = { duration: ToastDuration -> 50L }

    /**
     * Verifies that a toast is added to the visible list after calling [ke.don.koffee.domain.ToastHostState.show].
     */
    @Test
    fun toastIsAddedToList() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)

        val state = DefaultToastHostState(testScope, durationResolver)
        state.show("Title", "Desc", ToastDuration.Short, ToastType.Neutral, null, null)


        assertEquals(1, state.toasts.size)
        assertEquals("Title", state.toasts.first().title)
    }

    /**
     * Verifies that a toast is automatically dismissed after the resolved duration.
     */
    @Test
    fun toastAutoDismissesAfterDuration() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)

        val state = DefaultToastHostState(testScope, durationResolver)
        state.show("AutoDismiss", "Desc", ToastDuration.Short, ToastType.Neutral, null, null)

        assertEquals(1, state.toasts.size)
        advanceTimeBy(51) // simulate delay
        assertEquals(0, state.toasts.size)
    }

    /**
     * Verifies that when the maximum visible toast limit is exceeded,
     * the oldest toast is dismissed to make room for the new one.
     */
    @Test
    fun oldestToastIsRemovedWhenMaxExceeded() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)

        val state = DefaultToastHostState(testScope, durationResolver, maxVisibleToasts = 1)
        state.show("Toast1", "Desc", ToastDuration.Short, ToastType.Neutral, null, null)
        state.show("Toast2", "Desc", ToastDuration.Short, ToastType.Neutral, null, null)

        assertEquals(1, state.toasts.size)
        assertEquals("Toast2", state.toasts.first().title)
    }

    /**
     * Verifies that triggering a primary action calls the callback
     * and dismisses the toast if [ToastAction.dismissAfter] is true.
     */
    @Test
    fun actionTriggersAndDismissesToast() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)

        var clicked = false

        val action = ToastAction("Click", { clicked = true }, dismissAfter = true)

        val state = DefaultToastHostState(testScope, durationResolver)
        state.show("ActionToast", "Desc", ToastDuration.Short, ToastType.Neutral, primaryAction = action, secondaryAction = null)

        val toast = state.toasts.first()
        toast.primaryAction?.onClick()

        assertTrue(clicked)
        assertTrue(state.toasts.isEmpty())
    }

    /**
     * Verifies that triggering a primary action does not dismiss the toast
     * if [ToastAction.dismissAfter] is false.
     */
    @Test
    fun actionTriggersAndDoesntDismissToastWhenDismissAfterIsFalse() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)

        var clicked = false

        val action = ToastAction("Click", { clicked = true }, dismissAfter = false)

        val state = DefaultToastHostState(testScope, durationResolver)
        state.show("ActionToast", "Desc", ToastDuration.Short, ToastType.Neutral, primaryAction = action, secondaryAction = null)

        val toast = state.toasts.first()
        toast.primaryAction?.onClick()

        assertTrue(clicked)
        assertTrue(state.toasts.isNotEmpty())
    }

    /**
     * Verifies that [ke.don.koffee.domain.ToastHostState.dismissAll] removes all active toasts and cancels any auto-dismiss jobs.
     */
    @Test
    fun dismissAllClearsEverything() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)

        val state = DefaultToastHostState(testScope, durationResolver)
        state.show("One", "Toast", ToastDuration.Short, ToastType.Neutral, null, null)
        state.show("Two", "Toast", ToastDuration.Short, ToastType.Neutral, null, null)

        state.dismissAll()

        assertEquals(0, state.toasts.size)
    }
}
