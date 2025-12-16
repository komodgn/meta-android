plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.library.compose)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.example.metasearch.feature.screens"
}

dependencies {
    implementation(libs.circuit.foundation)
}
