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

import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType

interface ToastHostState {
    val toasts: List<ToastData>

    fun show(
        title: String,
        description: String,
        duration: ToastDuration = ToastDuration.Short,
        type: ToastType,
        primaryAction: ToastAction? = null,
        secondaryAction: ToastAction? = null,
    )

    fun dismiss(id: String)
    fun dismissAll()
}
