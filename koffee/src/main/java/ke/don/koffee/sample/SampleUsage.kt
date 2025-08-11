/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.koffee.sample

import ke.don.koffee.domain.DefaultToastHostState
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType
import ke.don.koffee.model.defaultDurationResolver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * Sample usage of the Koffee toast library.
 *
 * This object contains usage examples meant for documentation and testing.
 * Consumers can refer to this file for quick integration guidance.
 */


object SampleUsage {

    /**
     * Demonstrates usage of [DefaultToastHostState.show]. with a dismiss action.
     */
    fun showToastFromHostState() {
        val hostState = DefaultToastHostState(
            scope = CoroutineScope(Dispatchers.Default),
            durationResolver = { defaultDurationResolver(it) },
        )
        hostState.show(
            title = "Success",
            description = "You just created your account",
            duration = ToastDuration.Short,
            type = ToastType.Success,
            primaryAction = ToastAction(
                label = "Undo",
                onClick = { println("Hello friend") },
                dismissAfter = false,
            ),
            secondaryAction = ToastAction(
                label = "Got it",
                onClick = { println("Dismissed") },
                dismissAfter = false,
            ),
        )
    }

    /**
     * Demonstrates usage of [DefaultToastHostState.dismiss].
     */
    fun dismissToastFromHostState() {
        val hostState = DefaultToastHostState(
            scope = CoroutineScope(Dispatchers.Default),
            durationResolver = { defaultDurationResolver(it) },
        )
        hostState.dismiss("15yhdhide-dsdfd-wdhwdxvsv") // Id should not be empty
    }

    /**
     * Demonstrates usage of [DefaultToastHostState.dismissAll].
     */
    fun dismissAllFromHostState() {
        val hostState = DefaultToastHostState(
            scope = CoroutineScope(Dispatchers.Default),
            durationResolver = { defaultDurationResolver(it) },
        )
        hostState.dismissAll()
    }
}
