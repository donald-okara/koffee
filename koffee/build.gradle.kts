plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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
