plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.dokka") version "1.9.20"
    id("maven-publish")
    id("signing")
}

group = "io.github.donald-okara"
version = System.getenv("RELEASE_VERSION") ?: "unspecified"

android {
    namespace = "ke.don.koffee"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar() // generates empty jar
        }
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
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "io.github.donald-okara"
                artifactId = "koffee"
                version = System.getenv("RELEASE_VERSION") ?: "unspecified"

                from(components["release"])

                pom {
                    name.set("Koffee")
                    description.set("A composable toast/snackbar framework for Android")
                    url.set("https://github.com/donald-okara/Koffee")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("donald-okara")
                            name.set("Donald Okara")
                            email.set("donaldokara123@.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/donald-okara/Koffee.git")
                        developerConnection.set("scm:git:ssh://github.com:donald-okara/Koffee.git")
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
                    username = findProperty("ossrhUsername") as String?
                    password = findProperty("ossrhPassword") as String?
                }
            }

            maven {
                name = "localTest"
                url = uri(layout.buildDirectory.dir("local-repo"))
            }


        }
    }

    signing {
        useInMemoryPgpKeys(
            findProperty("signing.keyId") as String?,
            findProperty("signing.secretKey") as String?,
            findProperty("signing.password") as String?
        )
        sign(publishing.publications["release"])
    }
}
