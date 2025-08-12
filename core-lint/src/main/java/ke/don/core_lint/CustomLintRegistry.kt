/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.core_lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import ke.don.core_lint.rules.ExperimentalApiUsageDetector

class CustomLintRegistry : IssueRegistry() {
    override val issues = listOf(
        ExperimentalApiUsageDetector.ISSUE,
    )
    override val api: Int = com.android.tools.lint.detector.api.CURRENT_API

    override val vendor: Vendor = Vendor(
        vendorName = "Donald Isoe",
        identifier = "ke.don.core_lint",
        feedbackUrl = "mailto:isoedonald@gmail.com",
        contact = "https://github.com/isoedonald",
    )
}
