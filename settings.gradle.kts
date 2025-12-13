rootProject.name = "MetaSearch"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven("https://jitpack.io")
    }
}

include(":app")

include(":core:data:api")
include(":core:data:impl")
include(":core:datastore:api")
include(":core:datastore:impl")
include(":core:network")
include(":core:model")
