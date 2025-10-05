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

rootProject.name = "ReleaseFlow"
include(":app")

// Core modules
include(":core:design")
include(":core:data")
include(":core:domain")

// Feature modules
include(":feature:projects")
include(":feature:calendar")
include(":feature:analytics")
include(":feature:promotions")
include(":feature:assistant")
