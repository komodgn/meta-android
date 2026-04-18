plugins {
    alias(libs.plugins.metasearch.android.feature)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.feature.main"
}

dependencies {
    implementation(projects.core.di)

    implementation(libs.compose.system.ui.controller)
    implementation(libs.androidx.core.splashscreen)
}
