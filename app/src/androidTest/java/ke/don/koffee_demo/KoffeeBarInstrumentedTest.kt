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

import androidx.activity.ComponentActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import ke.don.experimental_annotations.ExperimentalKoffeeApi
import ke.don.koffee.domain.Koffee
import ke.don.koffee.model.KoffeeDefaults
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastAnimation
import ke.don.koffee.model.ToastPosition
import ke.don.koffee.ui.DefaultToast
import ke.don.koffee.ui.KoffeeBar
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalKoffeeApi::class)
class KoffeeBarInstrumentedTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    /**
     * Tests that a toast renders when triggered and auto-dismisses after the specified duration.
     */
    @Test
    fun toastRendersAndDismisses() {
        composeTestRule.setContent {
            val testConfig = KoffeeDefaults.config.copy(
                layout = { DefaultToast(it) },
                dismissible = true,
                maxVisibleToasts = 3,
                position = ToastPosition.BottomCenter,
                animationStyle = ToastAnimation.SlideUp,
                durationResolver = { 1000L },
            )

            KoffeeBar(
                config = testConfig,
            ) {
                Button(onClick = {
                    Koffee.show(
                        title = "Hello",
                        description = "World",
                    )
                }) {
                    Text("Show Toast")
                }
            }
        }

        // Tap to trigger toast
        composeTestRule.onNodeWithText("Show Toast").performClick()

        // Assert toast appears
        composeTestRule.onNodeWithText("Hello").assertIsDisplayed()

        // Wait for auto-dismiss
        composeTestRule.waitUntil(timeoutMillis = 1001) {
            composeTestRule.onAllNodesWithText("Hello").fetchSemanticsNodes().isEmpty()
        }
    }

    /**
     * Tests that the primary action button on a toast executes its onClick callback.
     */
    @Test
    fun toastPrimaryActionFires() {
        var clicked = false

        composeTestRule.setContent {
            val testConfig = KoffeeDefaults.config.copy(
                layout = { DefaultToast(it) },
                dismissible = true,
                maxVisibleToasts = 3,
                position = ToastPosition.BottomCenter,
                animationStyle = ToastAnimation.SlideUp,
                durationResolver = { null },
            )

            KoffeeBar(
                config = testConfig,
            ) {
                Button(onClick = {
                    Koffee.show(
                        title = "Undo",
                        description = "Remove action",
                        primaryAction = ToastAction("Confirm", {
                            clicked = true
                        }),
                    )
                }) {
                    Text("Trigger Toast")
                }
            }
        }

        // Show toast
        composeTestRule.onNodeWithText("Trigger Toast").performClick()

        // Click primary action
        composeTestRule.onNodeWithText("Confirm").performClick()

        // Verify result
        Assert.assertTrue(clicked)
    }

    /**
     * Tests that the primary action button on a toast executes its onClick callback.
     */
    @Test
    fun toastSecondaryActionFires() {
        var clicked = false

        composeTestRule.setContent {
            val testConfig = KoffeeDefaults.config.copy(
                layout = { DefaultToast(it) },
                dismissible = true,
                maxVisibleToasts = 3,
                position = ToastPosition.BottomCenter,
                animationStyle = ToastAnimation.SlideUp,
                durationResolver = { null },
            )

            KoffeeBar(
                config = testConfig,
            ) {
                Button(onClick = {
                    Koffee.show(
                        title = "Undo",
                        description = "Remove action",
                        secondaryAction = ToastAction("Confirm", {
                            clicked = true
                        }),
                    )
                }) {
                    Text("Trigger Toast")
                }
            }
        }

        // Show toast
        composeTestRule.onNodeWithText("Trigger Toast").performClick()

        // Click primary action
        composeTestRule.onNodeWithText("Confirm").performClick()

        // Verify result
        Assert.assertTrue(clicked)
    }

    /**
     * Tests that the primary button or action dismisses the toast when clicked.
     */
    @Test
    fun primaryButtonDismissesToast() {
        composeTestRule.setContent {
            val testConfig = KoffeeDefaults.config.copy(
                layout = { DefaultToast(it) },
                dismissible = true,
                maxVisibleToasts = 3,
                position = ToastPosition.BottomCenter,
                animationStyle = ToastAnimation.SlideUp,
                durationResolver = { null },
            )

            KoffeeBar(
                config = testConfig,
            ) {
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
        }

        composeTestRule.onNodeWithText("Launch").performClick()
        composeTestRule.onNodeWithText("Dismiss Me").assertIsDisplayed()

        // Simulate swipe or dismiss button
        // Assuming DefaultToast uses a dismiss X button
        composeTestRule.onNodeWithText("Dismiss").performClick()

        composeTestRule.waitUntil(timeoutMillis = 300) {
            composeTestRule.onAllNodesWithText("Dismiss Me").fetchSemanticsNodes().isEmpty()
        }
    }

    /**
     * Tests that the secondary button or action dismisses the toast when clicked.
     */
    @Test
    fun secondaryButtonDismissesToast() {
        composeTestRule.setContent {
            val testConfig = KoffeeDefaults.config.copy(
                layout = { DefaultToast(it) },
                dismissible = true,
                maxVisibleToasts = 3,
                position = ToastPosition.BottomCenter,
                animationStyle = ToastAnimation.SlideUp,
                durationResolver = { null },
            )

            KoffeeBar(
                config = testConfig,
            ) {
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
        }

        composeTestRule.onNodeWithText("Launch").performClick()
        composeTestRule.onNodeWithText("Dismiss Me").assertIsDisplayed()

        // Simulate swipe or dismiss button
        // Assuming DefaultToast uses a dismiss X button
        composeTestRule.onNodeWithText("Dismiss").performClick()

        composeTestRule.waitUntil(timeoutMillis = 300) {
            composeTestRule.onAllNodesWithText("Dismiss Me").fetchSemanticsNodes().isEmpty()
        }
    }

    /**
     * Tests that the primary button or action does not dismiss the toast when [ToastAction.dismissAfter] is false.
     */
    @Test
    fun primaryButtonDoesntDismissToastWhenDismissAfterIsFalse() {
        composeTestRule.setContent {
            val testConfig = KoffeeDefaults.config.copy(
                layout = { DefaultToast(it) },
                dismissible = true,
                maxVisibleToasts = 3,
                position = ToastPosition.BottomCenter,
                animationStyle = ToastAnimation.SlideUp,
                durationResolver = { null },
            )

            KoffeeBar(
                config = testConfig,
            ) {
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
        }

        composeTestRule.onNodeWithText("Launch").performClick()
        composeTestRule.onNodeWithText("Dismiss Me").assertIsDisplayed()

        // Simulate swipe or dismiss button
        // Assuming DefaultToast uses a dismiss X button
        composeTestRule.onNodeWithText("Dismiss").performClick()

        composeTestRule.waitUntil(timeoutMillis = 300) {
            composeTestRule.onAllNodesWithText("Dismiss Me").fetchSemanticsNodes().isNotEmpty()
        }
    }

    /**
     * Tests that the secondary button or action does not dismiss the toast when [ToastAction.dismissAfter] is false.
     */
    @Test
    fun secondaryButtonDoesntDismissesToastWhenDismissAfterIsFalse() {
        composeTestRule.setContent {
            val testConfig = KoffeeDefaults.config.copy(
                layout = { DefaultToast(it) },
                dismissible = true,
                maxVisibleToasts = 3,
                position = ToastPosition.BottomCenter,
                animationStyle = ToastAnimation.SlideUp,
                durationResolver = { null },
            )

            KoffeeBar(
                config = testConfig,
            ) {
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
        }

        composeTestRule.onNodeWithText("Launch").performClick()
        composeTestRule.onNodeWithText("Dismiss Me").assertIsDisplayed()

        // Simulate swipe or dismiss button
        // Assuming DefaultToast uses a dismiss X button
        composeTestRule.onNodeWithText("Dismiss").performClick()

        composeTestRule.waitUntil(timeoutMillis = 300) {
            composeTestRule.onAllNodesWithText("Dismiss Me").fetchSemanticsNodes().isNotEmpty()
        }
    }
}
