plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.library.compose)
    alias(libs.plugins.metasearch.android.retrofit)
}

android {
    namespace = "com.example.metasearch.core.common"
}

dependencies {
    implementation(projects.core.network)
}
