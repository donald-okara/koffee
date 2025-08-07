/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ke.don.koffee.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ke.don.koffee.model.KoffeeConfig
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType
import ke.don.koffee.ui.DefaultToast
import ke.don.koffee.ui.ToastHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Koffee {
    private lateinit var toastHostState: ToastHostState
    internal var config: KoffeeConfig = KoffeeConfig(
        layout = { DefaultToast(it) },
        dismissible = true,
    )

    fun init(builder: KoffeeConfigBuilder.() -> Unit) {
        config = KoffeeConfigBuilder().apply(builder).build()
    }

    @Composable
    fun Setup(
        modifier: Modifier = Modifier,
        maxVisibleToasts: Int = 1,
        hostState: ToastHostState = rememberToastHostState(maxVisibleToasts),
        alignment: Alignment = Alignment.BottomCenter,
    ) {
        toastHostState = hostState
        ToastHost(
            modifier = modifier,
            hostState = hostState,
            toast = config.layout,
            dismissible = config.dismissible,
            alignment = alignment,
        )
    }

    fun show(
        title: String,
        description: String,
        type: ToastType = ToastType.Neutral,
        duration: ToastDuration = ToastDuration.Short,
        primaryAction: ToastAction? = null,
        secondaryAction: ToastAction? = null,
        isAppVisible: Boolean = true,
        coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate),
    ) {
        if (!isAppVisible) return

        coroutineScope.launch {
            toastHostState.show(title, description, duration, type, primaryAction, secondaryAction)
        }
    }

    fun dismissAll() = toastHostState.dismissAll()
}
