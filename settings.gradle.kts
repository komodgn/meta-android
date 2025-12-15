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

include(":core:common")
include(":core:data:api")
include(":core:data:impl")
include(":core:datastore:api")
include(":core:datastore:impl")
include(":core:room:api")
include(":core:room:impl")
include(":core:network")
include(":core:model")
include(":core:ui")
include(":core:designsystem")

include(":feature:screens")
include(":feature:home")
include(":feature:detail")
include(":feature:search")
include(":feature:main")
include(":feature:person")
include(":feature:graph")
include(":feature:webview")
