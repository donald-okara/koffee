plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
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
                "proguard-rules.pro"
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            val release by creating(MavenPublication::class) {
                from(components["release"])
                groupId = "com.github.donald-okara"
                artifactId = "koffee"
                version = "0.1.5"

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
                    username = project.findProperty("ossrhUsername") as String
                    password = project.findProperty("ossrhPassword") as String
                }
            }
        }
    }

    signing {
        publishing.publications
            .withType<MavenPublication>()
            .matching { it.name == "release" }
            .all {
                sign(this)
            }
    }
}
