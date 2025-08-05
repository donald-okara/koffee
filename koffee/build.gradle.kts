plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
    id("signing")
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
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

afterEvaluate {

    println("Signing Info:")
    println("keyId: " + project.findProperty("signing.keyId"))
    println("password: " + if (project.hasProperty("signing.password")) "✅" else "❌")
    println("secretKeyRingFile: " + project.findProperty("signing.secretKeyRingFile"))

    publishing {
        publications {
            val release by creating(MavenPublication::class) {
                from(components["release"])
                groupId = "com.github.donald-okara"
                artifactId = "koffee"
                version = "v0.1.12"

                pom {
                    name.set("Koffee")
                    description.set("Composable toast manager for Jetpack Compose")
                    url.set("https://github.com/donald-okara/Koffee")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    developers {
                        developer {
                            id.set("donald-okara")
                            name.set("Donald Okara")
                            email.set("donaldokara123@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/donald-okara/Koffee.git")
                        developerConnection.set("scm:git:ssh://github.com/donald-okara/Koffee.git")
                        url.set("https://github.com/donald-okara/Koffee")
                    }
                }
            }
        }

        repositories {
            maven {
                name = "OSSRH"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.findProperty("ossrhUsername")?.toString() ?: ""
                    password = project.findProperty("ossrhPassword")?.toString() ?: ""
                }
            }
        }
    }

    val shouldSign =
        project.hasProperty("koffee.signingEnabled") &&
            project.hasProperty("signing.keyId") &&
            project.hasProperty("signing.password") &&
            project.hasProperty("signing.secretKeyRingFile") &&
            project.findProperty("koffee.signingEnabled") == "true"

    if (shouldSign) {
        signing {
            useInMemoryPgpKeys(
                project.findProperty("signing.keyId")!!.toString(),
                project.findProperty("signing.password")!!.toString(),
                file(project.findProperty("signing.secretKeyRingFile")!!).readText(),
            )
            sign(publishing.publications["release"])
        }
    } else {
        println("⚠️ Skipping signing — missing signing config (likely JitPack build).")
    }
}
