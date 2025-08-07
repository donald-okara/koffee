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
package ke.don.koffee_demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ke.don.koffee.domain.Koffee
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType

@Composable
fun TestToasts(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Button(
            onClick = {
                Koffee.dismissAll()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Dismiss all")
        }

        Button(
            onClick = {
                Koffee.show(
                    title = "Neutral toast",
                    description = "This is a default notification",
                    type = ToastType.Neutral,
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Show Neutral")
        }

        Button(
            onClick = {
                Koffee.show(
                    title = "Info toast",
                    description = "This is a secondary notification",
                    type = ToastType.Info,
                    primaryAction = ToastAction("Details", { println("Viewing info details") }),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Show Info")
        }

        Button(
            onClick = {
                Koffee.show(
                    title = "Success toast",
                    description = "This is a green notification",
                    type = ToastType.Success,
                    primaryAction = ToastAction("Share", { println("Viewing info details") }),
                    secondaryAction = ToastAction("Copy", { println("Copied!") }),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Show Success")
        }

        Button(
            onClick = {
                Koffee.show(
                    title = "Warning toast",
                    description = "This is an orange notification",
                    type = ToastType.Warning,
                    secondaryAction = ToastAction("Ignore", { println("Warning ignored") }),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Show Warning")
        }

        Button(
            onClick = {
                Koffee.show(
                    title = "Error toast",
                    description = "This is a red notification",
                    type = ToastType.Error,
                    duration = ToastDuration.Indefinite,
                    primaryAction = ToastAction("Retry", { println("Retrying operation...") }),
                    secondaryAction = ToastAction("Report", { println("Reported error") }),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Show Error")
        }
    }
}
