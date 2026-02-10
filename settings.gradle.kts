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

rootProject.name = "Pictionis"
include(":app")

include(":feature:lobby")
include(":feature:authentication")
include(":feature:game")

include(":core:ui")
include(":core:common")
include(":core:designsystem")

include(":domain")

include(":data:firebase")
