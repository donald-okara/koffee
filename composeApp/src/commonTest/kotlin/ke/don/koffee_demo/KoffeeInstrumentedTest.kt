/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.koffee_demo

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import ke.don.koffee.annotations.ExperimentalKoffeeApi
import ke.don.koffee.domain.Koffee
import ke.don.koffee.model.ToastAction
import ke.don.koffee.ui.DefaultToast
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalKoffeeApi::class)
class KoffeeInstrumentedTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun toastRendersAndDismisses() = runComposeUiTest {
        setContent {
            Koffee.init {
                layout { DefaultToast(it) }
                dismissible(true)
                durationResolver { 500L }
            }
            Koffee.Setup()

            Button(onClick = {
                Koffee.show(
                    title = "Hello",
                    description = "World",
                )
            }) {
                Text("Show Toast")
            }
        }

        onNodeWithText("Show Toast").performClick()
        onNodeWithText("Hello").assertIsDisplayed()

        waitUntil(timeoutMillis = 1000) {
            onAllNodesWithText("Hello").fetchSemanticsNodes().isEmpty()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun toastPrimaryActionFires() = runComposeUiTest {
        var clicked = false

        setContent {
            Koffee.init {
                layout { DefaultToast(it) }
                dismissible(true)
                durationResolver { null }
            }
            Koffee.Setup()

            Button(onClick = {
                Koffee.show(
                    title = "Undo",
                    description = "Remove action",
                    primaryAction = ToastAction("Confirm", onClick = { clicked = true }),
                )
            }) {
                Text("Trigger Toast")
            }
        }

        onNodeWithText("Trigger Toast").performClick()
        onNodeWithText("Confirm").performClick()
        assertTrue(clicked)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun toastSecondaryActionFires() = runComposeUiTest {
        var clicked = false

        setContent {
            Koffee.init {
                layout { DefaultToast(it) }
                dismissible(true)
                durationResolver { null }
            }
            Koffee.Setup()

            Button(onClick = {
                Koffee.show(
                    title = "Undo",
                    description = "Remove action",
                    secondaryAction = ToastAction("Confirm", onClick = {clicked = true}) ,
                )
            }) {
                Text("Trigger Toast")
            }
        }

        onNodeWithText("Trigger Toast").performClick()
        onNodeWithText("Confirm").performClick()
        assertTrue(clicked)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun primaryButtonDismissesToast() = runComposeUiTest {
        setContent {
            Koffee.init {
                layout { DefaultToast(it) }
                dismissible(true)
                durationResolver { null }
            }
            Koffee.Setup()

            Button(onClick = {
                Koffee.show(
                    "Dismiss Me",
                    "This should go away",
                    primaryAction = ToastAction("Dismiss", {}, dismissAfter = true),
                )
            }) {
                Text("Launch")
            }
        }

        onNodeWithText("Launch").performClick()
        onNodeWithText("Dismiss Me").assertIsDisplayed()

        onNodeWithText("Dismiss").performClick()

        onNodeWithText("Dismiss Me").assertDoesNotExist()


    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun secondaryButtonDismissesToast() = runComposeUiTest {
        setContent {
            Koffee.init {
                layout { DefaultToast(it) }
                dismissible(true)
                durationResolver { null }
            }
            Koffee.Setup()

            Button(onClick = {
                Koffee.show(
                    "Dismiss Me",
                    "This should go away",
                    secondaryAction = ToastAction("Dismiss", {}, dismissAfter = true),
                )
            }) {
                Text("Launch")
            }
        }

        onNodeWithText("Launch").performClick()
        onNodeWithText("Dismiss Me").assertIsDisplayed()

        onNodeWithText("Dismiss").performClick()

        waitUntil(timeoutMillis = 500) {
            onAllNodesWithText("Dismiss Me").fetchSemanticsNodes().isEmpty()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun primaryButtonDoesntDismissToastWhenDismissAfterIsFalse() = runComposeUiTest {
        setContent {
            Koffee.init {
                layout { DefaultToast(it) }
                dismissible(true)
                durationResolver { null }
            }
            Koffee.Setup()

            Button(onClick = {
                Koffee.show(
                    "Dismiss Me",
                    "This should go away",
                    primaryAction = ToastAction("Dismiss", {}, dismissAfter = false),
                )
            }) {
                Text("Launch")
            }
        }

        onNodeWithText("Launch").performClick()
        onNodeWithText("Dismiss Me").assertIsDisplayed()

        onNodeWithText("Dismiss").performClick()

        waitUntil(timeoutMillis = 500) {
            onAllNodesWithText("Dismiss Me").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun secondaryButtonDoesntDismissToastWhenDismissAfterIsFalse() = runComposeUiTest {
        setContent {
            Koffee.init {
                layout { DefaultToast(it) }
                dismissible(true)
                durationResolver { null }
            }
            Koffee.Setup()

            Button(onClick = {
                Koffee.show(
                    "Dismiss Me",
                    "This should go away",
                    secondaryAction = ToastAction("Dismiss", {}, dismissAfter = false),
                )
            }) {
                Text("Launch")
            }
        }

        onNodeWithText("Launch").performClick()
        onNodeWithText("Dismiss Me").assertIsDisplayed()

        onNodeWithText("Dismiss").performClick()

        waitUntil(timeoutMillis = 500) {
            onAllNodesWithText("Dismiss Me").fetchSemanticsNodes().isNotEmpty()
        }
    }
}
