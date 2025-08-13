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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ke.don.experimental_annotations.ExperimentalKoffeeApi
import ke.don.koffee.model.KoffeeDefaults
import ke.don.koffee.model.ToastAnimation
import ke.don.koffee.model.ToastPosition
import ke.don.koffee.ui.DefaultToast
import ke.don.koffee.ui.KoffeeBar
import ke.don.koffee_demo.ui.theme.KoffeeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalKoffeeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoffeeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Override the current default configuration
                    val mySimpleConfig = KoffeeDefaults.config.copy(
                        layout = { DefaultToast(it) },
                        dismissible = true,
                        maxVisibleToasts = 3,
                        position = ToastPosition.BottomCenter,
                        animationStyle = ToastAnimation.SlideUp,
                        durationResolver = { customDurationResolver(it) },
                    )

                    KoffeeBar(
                        modifier = Modifier.padding(innerPadding),
                        config = mySimpleConfig,
                    ) {
                        TestToasts()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KoffeeTheme {
        Greeting("Android")
    }
}
