package ke.don.koffee_demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ke.don.koffee.annotations.ExperimentalKoffeeApi
import ke.don.koffee.model.KoffeeDefaults
import ke.don.koffee.model.ToastAnimation
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastPosition
import ke.don.koffee.ui.DefaultToast
import ke.don.koffee.ui.KoffeeBar
import ke.don.koffee.ui.toasts_suite.GlowingToast
import ke.don.koffee_demo.ui.theme.KoffeeTheme

@OptIn(ExperimentalKoffeeApi::class)
@Composable
fun App(){
    KoffeeTheme(
        darkTheme = true
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

            // Override the current default configuration (stable across recompositions)
            val mySimpleConfig = remember {
                KoffeeDefaults.config.copy(
                    layout = { GlowingToast(it) },
                    dismissible = true,
                    maxVisibleToasts = 3,
                    position = ToastPosition.BottomEnd,
                    animationStyle = ToastAnimation.SlideUp,
                    durationResolver = ::customDurationResolver,
                )
            }

            KoffeeBar(
                modifier = Modifier.padding(innerPadding),
                config = mySimpleConfig,
            ) {
                TestToasts()
            }
        }
    }
}

fun customDurationResolver(duration: ToastDuration): Long? = when (duration) {
    ToastDuration.Short -> 5000L
    ToastDuration.Medium -> 7000L
    ToastDuration.Long -> 10000L
    ToastDuration.Indefinite -> null
}