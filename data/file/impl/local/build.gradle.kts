plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metro)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.data.file.impl.local"
}

dependencies {
    implementation(projects.domain.file.api)
    implementation(projects.core.common)
    implementation(projects.core.di)

    implementation(libs.androidx.core.ktx)
}
