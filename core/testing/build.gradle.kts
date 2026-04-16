plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.metasearch.android.core.testing"
}

dependencies {
    // Robolectric & JUnit
    api(libs.junit)
    api(libs.robolectric)
    api(libs.androidx.test.core)

    // Compose UI Test
    api(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // ScreenShot Test
    api(libs.roborazzi)
    api(libs.roborazzi.compose)

    implementation(libs.coil.test)
    implementation(libs.coil.compose)
}
