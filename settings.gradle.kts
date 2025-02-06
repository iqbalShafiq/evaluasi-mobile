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
        google()
        mavenCentral()
    }
}

rootProject.name = "Evaluasi"
include(":app")
include(":core:presentation:ui")
include(":core:presentation:designsystem")
include(":core:domain")
include(":core:data")
include(":core:database")
include(":assessment:presentation")
include(":assessment:data")
include(":assessment:domain")
include(":authentication:presentation")
include(":authentication:domain")
include(":authentication:data")
