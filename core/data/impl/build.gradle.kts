plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.hilt)
    alias(libs.plugins.metasearch.kotlin.library.serialization)
}

android {
    namespace = "com.example.metasearch.core.data.impl"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "VERSION_NAME", "\"${libs.versions.versionName.get()}\"")
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.network)

    implementation(projects.core.data.api)
    implementation(projects.core.datastore.api)
}
