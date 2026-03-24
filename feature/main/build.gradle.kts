plugins {
    alias(libs.plugins.metasearch.android.feature)
}

android {
    namespace = "com.metasearch.android.feature.main"
}

dependencies {
    implementation(libs.compose.system.ui.controller)
    implementation(libs.androidx.core.splashscreen)
}
