import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
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
