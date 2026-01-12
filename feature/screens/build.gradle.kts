plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.library.compose)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.metasearch.android.feature.screens"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.ui)

    implementation(libs.circuit.foundation)
}
