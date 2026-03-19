plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.library.compose)
}

android {
    namespace = "com.metasearch.android.core.designsystem"
}

dependencies {
    implementation(libs.coil.compose)
}
