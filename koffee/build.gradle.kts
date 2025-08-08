plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.dokka") version "2.0.0"
}

group = "io.github.donald-okara"
version = gitTagVersion()

tasks.dokkaHtml.configure {
    doFirst {
        delete(rootProject.layout.projectDirectory.dir("docs"))
    }

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
        testOptions.targetSdk = 36

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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// ─── Dynamically infer tag version ─────────────────────────────────────────────

fun gitTagVersion(): String {
    return try {
        val tag = "git describe --tags --abbrev=0".runCommand().trim()
        tag.removePrefix("v") // strip 'v' from v1.0.0 → 1.0.0
    } catch (e: Exception) {
        println("Warning: Git tag not found. Using fallback version.")
        "unspecified"
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
