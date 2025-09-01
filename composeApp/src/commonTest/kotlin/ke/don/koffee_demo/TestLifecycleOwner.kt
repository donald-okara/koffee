package ke.don.koffee_demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.compose.LocalLifecycleOwner

class TestLifecycleOwner : LifecycleOwner {
    private val registry = LifecycleRegistry(this).apply {
        currentState = Lifecycle.State.RESUMED
    }
    override val lifecycle: Lifecycle = registry
}

@Composable
fun WithTestLifecycle(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalLifecycleOwner provides TestLifecycleOwner()
    ) {
        content()
    }
}
