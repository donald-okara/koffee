package ke.don.koffee

import androidx.activity.ComponentActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import ke.don.koffee.domain.Koffee
import ke.don.koffee.model.ToastAction
import ke.don.koffee.ui.DefaultToast
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class) //if not using DI
class KoffeeInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun toastRendersAndDismisses() {
        composeTestRule.setContent {
            Koffee.init {
                layout { DefaultToast(it) }
                dismissible(true)
                durationResolver { 50L }
            }

            Koffee.Setup()

            Button(onClick = {
                Koffee.show(
                    title = "Hello",
                    description = "World"
                )
            }) {
                Text("Show Toast")
            }
        }

        // Tap to trigger toast
        composeTestRule.onNodeWithText("Show Toast").performClick()

        // Assert toast appears
        composeTestRule.onNodeWithText("Hello").assertIsDisplayed()

        // Wait for auto-dismiss
        composeTestRule.waitUntil(timeoutMillis = 500) {
            composeTestRule.onAllNodesWithText("Hello").fetchSemanticsNodes().isEmpty()
        }
    }

    @Test
    fun toastPrimaryActionFires() {
        var clicked = false

        composeTestRule.setContent {
            Koffee.init {
                layout { DefaultToast(it) } // or custom
                dismissible(true)
                durationResolver { null }
            }

            Koffee.Setup()

            Button(onClick = {
                Koffee.show(
                    title = "Undo",
                    description = "Remove action",
                    primaryAction = ToastAction("Confirm", {
                        clicked = true
                    })
                )
            }) {
                Text("Trigger Toast")
            }
        }

        // Show toast
        composeTestRule.onNodeWithText("Trigger Toast").performClick()

        // Click primary action
        composeTestRule.onNodeWithText("Confirm").performClick()

        // Verify result
        assertTrue(clicked)
    }

    @Test
    fun dismissButtonDismissesToast() {
        composeTestRule.setContent {
            Koffee.init {
                layout { DefaultToast(it) } // or custom
                dismissible(true)
                durationResolver { 50L }
            }

            Koffee.Setup()

            Button(onClick = {
                Koffee.show("Dismiss Me", "This should go away")
            }) {
                Text("Launch")
            }
        }

        composeTestRule.onNodeWithText("Launch").performClick()
        composeTestRule.onNodeWithText("Dismiss Me").assertIsDisplayed()

        // Simulate swipe or dismiss button
        // Assuming DefaultToast uses a dismiss X button
        composeTestRule.onNodeWithContentDescription("Dismiss").performClick()

        composeTestRule.waitUntil(timeoutMillis = 300) {
            composeTestRule.onAllNodesWithText("Dismiss Me").fetchSemanticsNodes().isEmpty()
        }
    }
}
