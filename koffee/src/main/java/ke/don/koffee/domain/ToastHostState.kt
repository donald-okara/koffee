/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.koffee.domain

import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastData
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType

/**
 * Host state responsible for managing toast notifications in the UI.
 */
interface ToastHostState {
    /**
     * Currently visible toasts.
     */
    val toasts: List<ToastData>

    /**
     * Shows a new toast with the given properties.
     *
     * @param title The main message of the toast.
     * @param description Optional supporting text below the title.
     * @param duration How long the toast should be shown. Defaults to [ToastDuration.Short].
     * @param type The visual style/type of the toast (e.g. Success, Error).
     * @param primaryAction Optional primary action shown in the toast.
     * @param secondaryAction Optional secondary action shown in the toast.
     */
    fun show(
        title: String,
        description: String,
        duration: ToastDuration = ToastDuration.Short,
        type: ToastType,
        primaryAction: ToastAction? = null,
        secondaryAction: ToastAction? = null,
    )

    /**
     * Dismisses a toast by its unique [id].
     *
     * @param id The identifier of the toast to dismiss.
     */
    fun dismiss(id: String)

    /**
     * Dismisses all currently shown toasts.
     */
    fun dismissAll()
}
