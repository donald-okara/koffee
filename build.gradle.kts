import java.util.Properties
import com.diffplug.gradle.spotless.SpotlessExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.diffplug.spotless") version "6.25.0"
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("${layout.buildDirectory.get().asFile}/**/*.kt")
            ktlint("0.50.0")
                .editorConfigOverride(
                    mapOf(
                        "ktlint_standard_package-name" to "disabled",
                        "ktlint_standard_no-wildcard-imports" to "disabled"
                    )
                )
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        }

        kotlinGradle {
            target("*.gradle.kts")
            ktlint("0.50.0")
        }
    }
}


val secretsFile = rootProject.file("gradle-secrets.properties")
if (secretsFile.exists()) {
    secretsFile.reader().use {
        Properties().apply {
            load(it)
            forEach { (k, v) -> project.extensions.extraProperties[k.toString()] = v }
        }
    }
}
