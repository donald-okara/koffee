package ke.don.koffee

import junit.framework.TestCase.assertTrue
import ke.don.koffee.domain.DefaultToastHostState
import ke.don.koffee.model.KoffeeConfig
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType
import ke.don.koffee.ui.DefaultToast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
/**
 * Unit tests for [DefaultToastHostState], verifying toast lifecycle behavior,
 * including appearance, dismissal, queue limits, and action callbacks.
 */
class DefaultToastHostStateTest {

    /**
     * A test config with a fake layout and fixed 50ms toast duration.
     */
    val testConfig = KoffeeConfig(
        layout = { DefaultToast(it) }, // can be fake
        dismissible = true,
        durationResolver = { 50L }
    )

    /**
     * Verifies that a toast is added to the visible list after calling [ke.don.koffee.domain.ToastHostState.show].
     */
    @Test
    fun toastIsAddedToList() = runTest {
        val state = DefaultToastHostState(this, config = testConfig)
        state.show("Title", "Desc", ToastDuration.Short, ToastType.Neutral, null, null)

        assertEquals(1, state.toasts.size)
        assertEquals("Title", state.toasts.first().title)
    }

    /**
     * Verifies that a toast is automatically dismissed after the resolved duration.
     */
    @Test
    fun toastAutoDismissesAfterDuration() = runTest {
        val state = DefaultToastHostState(this, config = testConfig)
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
        val state = DefaultToastHostState(this, config = testConfig, maxVisibleToasts = 1)
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
        var clicked = false

        val action = ToastAction("Click", { clicked = true }, dismissAfter = true)

        val state = DefaultToastHostState(this, config = testConfig)
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
        var clicked = false

        val action = ToastAction("Click", { clicked = true }, dismissAfter = false)

        val state = DefaultToastHostState(this, config = testConfig)
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
        val state = DefaultToastHostState(this, config = testConfig)
        state.show("One", "Toast", ToastDuration.Short, ToastType.Neutral, null, null)
        state.show("Two", "Toast", ToastDuration.Short, ToastType.Neutral, null, null)

        state.dismissAll()

        assertEquals(0, state.toasts.size)
    }
}
