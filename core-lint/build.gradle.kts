plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

tasks.jar {
    manifest {
        attributes["Lint-Registry-v2"] = "ke.don.core_lint.CustomLintRegistry"
    }
}

dependencies {
    compileOnly(libs.lint.api) // match your AGP's lint version
    compileOnly(project(":experimental-annotations"))
    testImplementation(libs.lint.tests)
    testImplementation(kotlin("test"))
}
