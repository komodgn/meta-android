plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.kotlin.library.serialization)
    alias(libs.plugins.metro)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.data.device.impl"
}

dependencies {
    implementation(projects.core.datastore.api)
    implementation(projects.core.di)
    implementation(projects.domain.device.api)

    implementation(libs.androidx.core.ktx)
}
