plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.hilt)
    alias(libs.plugins.metasearch.kotlin.library.serialization)
}

android {
    namespace = "com.example.metasearch.datastore.impl"
}

dependencies {
    implementation(projects.core.datastore.api)
    implementation(projects.core.model)

    implementation(libs.androidx.datastore.preferences)
}
