plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.library.compose)
}

android {
    namespace = "com.example.metasearch.core.ui"
}

dependencies {
    implementation(projects.core.designsystem)
}
