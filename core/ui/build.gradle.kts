plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.library.compose)
}

android {
    namespace = "com.metasearch.android.core.ui"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.designsystem)

    implementation(libs.coil.compose)
}
