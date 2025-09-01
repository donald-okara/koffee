import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.composeMultiplatform)
    id("com.vanniktech.maven.publish") version "0.34.0"
    id("org.jetbrains.dokka") version "2.0.0"
}

group = "io.github.donald-okara"
version = project.findProperty("version") ?: throw GradleException("Version property is required. Pass it with -Pversion=<version>")

tasks.dokkaHtml.configure {
    doFirst {
        delete(rootProject.layout.projectDirectory.dir("docs"))
    }
    moduleName.set("Koffee - $gitTagVersion")

    outputDirectory.set(rootProject.layout.projectDirectory.dir("docs"))
}

tasks.named<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml").configure {
    dokkaSourceSets.configureEach {
        // 👇 include sample usage file(s)
        samples.from(file("koffee/src/main/java/ke/don/koffee/sample/SampleUsage.kt"))

        // (Optional) Suppress deprecated or undocumented elements
        suppress.set(false)
        skipEmptyPackages.set(true)
    }
}

android {
    namespace = "ke.don.koffee"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}


kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "KoffeeLib"
            isStatic = true
        }
    }
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(compose.components.resources)
                // Add KMP dependencies here
                implementation(compose.runtime)
                implementation(compose.materialIconsExtended)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                //implementation(project(":koffee"))
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.junit)
                implementation(libs.kotlinx.coroutines.test)

            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.activity.compose)
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
            }
        }

        getByName("androidInstrumentedTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.junit)
            }
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }




}

dependencies {

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}


dependencies {
    lintChecks(project(":core-lint"))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

mavenPublishing {
    publishToMavenCentral() // or publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    coordinates(group as String, "koffee", version as String)

    pom {
        name.set("Koffee Library")
        description.set("A toast library for jetpack compose.")
        inceptionYear.set("2025")
        url.set("https://github.com/donald-okara/koffee/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("donald-okara")
                name.set("Donald Okara")
                url.set("https://github.com/donald-okara/")
            }
        }
        scm {
            url.set("https://github.com/donald-okara/deploy-exampl/")
            connection.set("scm:git:git://github.com/donald-okara/koffe.git")
            developerConnection.set("scm:git:ssh://git@github.com/donald-okara/koffee.git")
        }
    }
}

// ─── Dynamically infer tag version ─────────────────────────────────────────────

val gitTagVersion: String by lazy {
    try {
        "git describe --tags --abbrev=0".runCommand() ?: "untagged"
    } catch (e: Exception) {
        "untagged"
    }
}

fun String.runCommand(): String =
    ProcessBuilder(*split(" ").toTypedArray())
        .directory(rootDir)
        .redirectErrorStream(true)
        .start()
        .inputStream
        .bufferedReader()
        .readText()
