import java.util.Base64

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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
    extensions.configure<PublishingExtension>("publishing") {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "io.github.donald-okara"
                artifactId = "koffee"
                version = project.version.toString()

                pom {
                    name.set("Koffee")
                    description.set("A beautiful toast/snackbar system for Jetpack Compose")
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
        }
    }

    extensions.configure<SigningExtension>("signing") {
        println("signing.keyId: ${findProperty("signing.keyId")}")
        println("SIGNING_PRIVATE_KEY (starts with): ${System.getenv("SIGNING_PRIVATE_KEY")?.take(20)}")
        println("signing.password: ${findProperty("signing.password")}")
        val decoded = System.getenv("SIGNING_PRIVATE_KEY")?.let {
            String(Base64.getDecoder().decode(it))
        }
        println(decoded?.take(100)) // Should print start of private key block

        useInMemoryPgpKeys(
            findProperty("signing.keyId") as String?,
            decoded,
            findProperty("signing.password") as String?
        )

        sign(extensions.getByType<PublishingExtension>().publications)
    }
}
