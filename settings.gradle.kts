pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal() // 👈 Must be listed *before* JitPack or MavenCentral
        google()
        mavenCentral()
        maven(url = "https://jitpack.io") // 👈 Add this
    }
}

rootProject.name = "Koffee"
include(":app")
include(":koffee")
