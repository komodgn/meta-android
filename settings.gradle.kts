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
include(":core:datastore:api")
include(":core:datastore:impl")
include(":core:designsystem")
include(":core:di")
include(":core:notification")
include(":core:permissions:api")
include(":core:room:api")
include(":core:room:impl")
include(":core:testing")
include(":core:ui")
include(":core:webview")
include(":core:worker:api")
include(":core:worker:impl")

include(":feature:screens")
include(":feature:detail")
include(":feature:graph")
include(":feature:graph_detail")
include(":feature:home")
include(":feature:main")
include(":feature:person")
include(":feature:person_detail")
include(":feature:photo_detail")
include(":feature:search")
include(":feature:splash")

include(":data:domain")
include(":data:analysis:impl")
include(":data:device:impl")
include(":data:file:impl:local")
include(":data:gallery:impl")
include(":data:graph:impl")
include(":data:person:impl")
include(":data:search:impl")

include(":domain:analysis:api")
include(":domain:device:api")
include(":domain:file:api")
include(":domain:gallery:api")
include(":domain:graph:api")
include(":domain:person:api")
include(":domain:search:api")
include(":data:remote")
