/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.koffee.model

/**
 * Represents an action that can be performed on a toast notification.
 *
 * @property label The text to display for the action button.
 * @property onClick A lambda function to be executed when the action button is clicked.
 * @property dismissAfter If `true`, the toast will be dismissed after the action is clicked. Defaults to `false`.
 */
data class ToastAction(
    val label: String,
    val onClick: () -> Unit,
    val dismissAfter: Boolean = false,
)
