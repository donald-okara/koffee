package ke.don.core_lint

import com.android.tools.lint.client.api.IssueRegistry
import ke.don.core_lint.rules.ExperimentalApiUsageDetector

class CustomLintRegistry : IssueRegistry() {
    override val issues = listOf(ExperimentalApiUsageDetector.ISSUE)
    override val api: Int = com.android.tools.lint.detector.api.CURRENT_API
}