package ke.don.koffee_demo

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.test.*
import ke.don.koffee.annotations.ExperimentalKoffeeApi
import ke.don.koffee.domain.Koffee
import ke.don.koffee.model.*
import ke.don.koffee.ui.DefaultToast
import ke.don.koffee.ui.KoffeeBar
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalKoffeeApi::class)
class KoffeeBarTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun toastRendersAndDismisses() = runComposeUiTest {
        setContent {
            WithTestLifecycle{
                val testConfig = KoffeeDefaults.config.copy(
                    layout = { DefaultToast(it) },
                    dismissible = true,
                    maxVisibleToasts = 3,
                    position = ToastPosition.BottomCenter,
                    animationStyle = ToastAnimation.SlideUp,
                    durationResolver = { 500L },
                )

                KoffeeBar(config = testConfig) {
                    Button(onClick = {
                        Koffee.show("Hello", "World")
                    }) {
                        Text("Show Toast")
                    }
                }
            }
        }

        // Trigger toast
        onNodeWithText("Show Toast").performClick()
        onNodeWithText("Hello").assertIsDisplayed()

        // Wait for auto-dismiss
        waitUntil(timeoutMillis = 1000) {
            onAllNodesWithText("Hello").fetchSemanticsNodes().isEmpty()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun toastPrimaryActionFires() = runComposeUiTest {
        var clicked = false

        setContent {
            WithTestLifecycle{
                val testConfig = KoffeeDefaults.config.copy(
                    layout = { DefaultToast(it) },
                    dismissible = true,
                    durationResolver = { null },
                )

                KoffeeBar(config = testConfig) {
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
            WithTestLifecycle{
                val testConfig = KoffeeDefaults.config.copy(
                    layout = { DefaultToast(it) },
                    dismissible = true,
                    durationResolver = { null },
                )

                KoffeeBar(config = testConfig) {
                    Button(onClick = {
                        Koffee.show(
                            title = "Undo",
                            description = "Remove action",
                            secondaryAction = ToastAction("Confirm", onClick = { clicked = true }),
                        )
                    }) {
                        Text("Trigger Toast")
                    }
                }
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
            WithTestLifecycle{
                val testConfig = KoffeeDefaults.config.copy(
                    layout = { DefaultToast(it) },
                    dismissible = true,
                    durationResolver = { null },
                )

                KoffeeBar(config = testConfig) {
                    Button(
                        onClick = {
                            Koffee.show(
                                "Dismiss Me",
                                "This should go away",
                                primaryAction = ToastAction("Dismiss", {}, dismissAfter = true),
                            )
                        }
                    )
                    {
                        Text("Launch")
                    }
                }


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
            WithTestLifecycle{
                val testConfig = KoffeeDefaults.config.copy(
                    layout = { DefaultToast(it) },
                    dismissible = true,
                    durationResolver = { null },
                )

                KoffeeBar(config = testConfig) {
                    Button(
                        onClick = {
                            Koffee.show(
                                "Dismiss Me",
                                "This should go away",
                                secondaryAction = ToastAction("Dismiss", {}, dismissAfter = true),
                            )
                        }
                    )
                    {
                        Text("Launch")
                    }
                }


            }
        }

        onNodeWithText("Launch").performClick()
        onNodeWithText("Dismiss Me").assertIsDisplayed()

        onNodeWithText("Dismiss").performClick()
        onNodeWithText("Dismiss Me").assertDoesNotExist()

    }


    @OptIn(ExperimentalTestApi::class)
    @Test
    fun primaryButtonDoesntDismissToastWhenDismissAfterIsFalse() = runComposeUiTest {
        setContent {
            WithTestLifecycle{
                val testConfig = KoffeeDefaults.config.copy(
                    layout = { DefaultToast(it) },
                    dismissible = true,
                    durationResolver = { null },
                )

                KoffeeBar(config = testConfig) {
                    Button(
                        onClick = {
                            Koffee.show(
                                "Dismiss Me",
                                "This should go away",
                                primaryAction = ToastAction("Dismiss", {}, dismissAfter = false),
                            )
                        }
                    )
                    {
                        Text("Launch")
                    }
                }


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
            WithTestLifecycle{
                val testConfig = KoffeeDefaults.config.copy(
                    layout = { DefaultToast(it) },
                    dismissible = true,
                    durationResolver = { null },
                )

                KoffeeBar(config = testConfig) {
                    Button(
                        onClick = {
                            Koffee.show(
                                "Dismiss Me",
                                "This should go away",
                                secondaryAction = ToastAction("Dismiss", {}, dismissAfter = false),
                            )
                        }
                    )
                    {
                        Text("Launch")
                    }
                }


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
