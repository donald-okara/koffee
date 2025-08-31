/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.koffee.annotations

/**
 * Marks declarations that are part of the experimental Koffee API.
 *
 * Using these APIs requires an explicit opt-in via `@OptIn(ExperimentalKoffeeApi::class)`.
 *
 * **Warning:** These APIs are subject to change or removal in future versions without notice.
 * They are provided for early access and feedback, and should not be used in production code
 * without careful consideration of potential breaking changes.
 */
@Suppress("ExperimentalAnnotationRetention")
@RequiresOptIn(
    message = "This API is experimental and requires explicit opt-in.",
    level = RequiresOptIn.Level.ERROR,
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
)
annotation class ExperimentalKoffeeApi
