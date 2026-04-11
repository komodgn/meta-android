plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.kotlin.library.serialization)
    alias(libs.plugins.metro)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.data.analysis.impl"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.data.remote)
    implementation(projects.domain.analysis.api)
    implementation(projects.domain.device.api)
    implementation(projects.domain.gallery.api)
    implementation(projects.domain.person.api)
    implementation(projects.domain.search.api)
    implementation(projects.domain.file.api)
    implementation(projects.core.datastore.api)
    implementation(projects.core.di)
    implementation(projects.core.room.api)

    implementation(libs.androidx.core.ktx)
}
