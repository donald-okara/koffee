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

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents the data required to display a toast message.
 *
 * @property title The main title of the toast message.
 * @property description A more detailed description or body of the toast message.
 * @property type The visual type of the toast, indicating its nature (e.g., success, error, info).
 *               Defaults to [ToastType.Neutral].
 * @property duration How long the toast message should be displayed. Defaults to [ToastDuration.Short].
 * @property id A unique identifier for the toast message. Defaults to a randomly generated UUID string.
 * @property primaryAction An optional primary action that the user can take in response to the toast.
 *                         This is typically displayed as a button or link. Defaults to `null`.
 * @property secondaryAction An optional secondary action that the user can take.
 *                           This is typically displayed alongside the primary action. Defaults to `null`.
 */
data class ToastData
@OptIn(ExperimentalUuidApi::class)
constructor(
    val title: String,
    val description: String,
    val type: ToastType = ToastType.Neutral,
    val duration: ToastDuration = ToastDuration.Short,
    val id: String = Uuid.random().toString(),
    val primaryAction: ToastAction? = null,
    val secondaryAction: ToastAction? = null,
)
