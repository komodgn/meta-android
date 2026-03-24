plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.kotlin.library.serialization)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.metasearch.android.datastore.impl"
}

dependencies {
    implementation(projects.core.datastore.api)
    implementation(projects.core.model)

    api(libs.androidx.datastore.preferences)
}
