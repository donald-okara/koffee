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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import ke.don.koffee.model.KoffeeConfig
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class DefaultToastHostState internal constructor(
    private val scope: CoroutineScope,
    private val durationResolver: (ToastDuration) -> Long?,
    private val maxVisibleToasts: Int = 1,
) : ToastHostState {
    private val _toasts = mutableStateListOf<ToastData>()
    override val toasts: List<ToastData> get() = _toasts

    private val jobs = mutableMapOf<String, Job>()

    override fun show(
        title: String,
        description: String,
        duration: ToastDuration,
        type: ToastType,
        primaryAction: ToastAction?,
        secondaryAction: ToastAction?,
    ) {
        val toastId = UUID.randomUUID().toString()

        val wrappedPrimary = primaryAction?.let {
            ToastAction(it.label, {
                it.onClick()
                if (it.dismissAfter) dismiss(toastId)
            })
        }

        val wrappedSecondary = secondaryAction?.let {
            ToastAction(it.label, {
                it.onClick()
                if (it.dismissAfter) dismiss(toastId)
            })
        }


        val toast = ToastData(
            title = title,
            description = description,
            type = type,
            duration = duration,
            primaryAction = wrappedPrimary,
            secondaryAction = wrappedSecondary,
            id = toastId,
        )

        if (_toasts.size >= maxVisibleToasts) {
            _toasts.firstOrNull()?.let { dismiss(it.id) }
        }

        _toasts.add(toast)

        val millis = durationResolver(duration)
        if (millis != null) {
            jobs[toast.id] = scope.launch {
                delay(millis)
                dismiss(toast.id)
            }
        }
    }

    override fun dismiss(id: String) {
        jobs.remove(id)?.cancel()
        _toasts.removeAll { it.id == id }
    }

    override fun dismissAll() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
        _toasts.clear()
    }
}

@Composable
fun rememberToastHostState(
    maxVisibleToasts: Int = 3,
    durationResolver: (ToastDuration) -> Long?
): ToastHostState {
    val scope = rememberCoroutineScope()

    return remember(scope, maxVisibleToasts) {
        DefaultToastHostState(scope, durationResolver, maxVisibleToasts)
    }
}
