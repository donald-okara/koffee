package ke.don.core_lint.rules

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getParentOfType


import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.*
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.*
class ExperimentalApiUsageDetector : Detector(), SourceCodeScanner {

    companion object {
        val ISSUE = Issue.create(
            id = "ExperimentalKoffeeApiUsage",
            briefDescription = "Usage of experimental Koffee API",
            explanation = "This API is marked @ExperimentalKoffeeApi and requires opt-in.",
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.ERROR,
            implementation = Implementation(
                ExperimentalApiUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        // We want to analyze annotations usage in code
        return listOf(UAnnotation::class.java)
    }


    override fun visitAnnotationUsage(
        context: JavaContext,
        element: UElement,
        annotationInfo: AnnotationInfo,
        usageInfo: AnnotationUsageInfo
    ) {
        val qualifiedName = annotationInfo.qualifiedName
        if (qualifiedName != "ke.don.experimental_annotations.ExperimentalKoffeeApi") return

        val hasOptIn = findOptInAnnotation(element, "ke.don.experimental_annotations.ExperimentalKoffeeApi")

        if (!hasOptIn) {
            val target = findEnclosingMethodOrClass(element) ?: element

            val addOptInAnnotation = LintFix.create()
                .name("Add @OptIn annotation")
                .replace()
                .range(context.getLocation(target))
                .beginning()
                .with("@OptIn(ExperimentalKoffeeApi::class)\n")
                .shortenNames()
                .autoFix()
                .build()

            val addImport = LintFix.create()
                .name("Import ExperimentalKoffeeApi")
                .replace()
                .pattern("^(package[\\s\\S]*?\\n)")
                .with("$1import ke.don.experimental_annotations.ExperimentalKoffeeApi\n")
                .autoFix()
                .build()

            val fix = LintFix.create()
                .name("Opt-in to Experimental API")
                .composite(addImport, addOptInAnnotation) // no .build() here

            context.report(
                ISSUE,
                element,
                context.getLocation(element),
                "Calling experimental API requires opt-in using @OptIn(ExperimentalKoffeeApi::class)",
                fix
            )
        }
    }


    private fun findOptInAnnotation(element: UElement?, annotationFqName: String): Boolean {
        if (element == null) return false

        val annotations = when (element) {
            is UMethod -> element.uAnnotations
            is UClass -> element.uAnnotations
            else -> emptyList()
        }

        if (annotations.any { it.qualifiedName == "kotlin.OptIn" && it.asSourceString().contains(annotationFqName) }) {
            return true
        }

        // Recursively check parent element (e.g., enclosing class)
        return findOptInAnnotation(element.uastParent, annotationFqName)
    }

    private fun findEnclosingMethodOrClass(element: UElement): UAnnotated? {
        var current: UElement? = element
        while (current != null) {
            when (current) {
                is UMethod -> return current
                is UClass -> return current
            }
            current = current.uastParent
        }
        return null
    }
}
