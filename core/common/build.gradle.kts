plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.library.compose)
    alias(libs.plugins.metasearch.android.retrofit)
}

android {
    namespace = "com.metasearch.android.core.common"
}

dependencies {
    implementation(projects.core.network)
}
