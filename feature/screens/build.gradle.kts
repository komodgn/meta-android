plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.library.compose)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.example.metasearch.feature.screens"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.ui)

    implementation(libs.circuit.foundation)
}
