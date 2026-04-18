plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.metasearch.android.core.testing"
}

dependencies {
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.tooling.preview)

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

    api(libs.roborazzi.preview.scanner.support)
    api(libs.composable.preview.scanner)

    implementation(libs.coil.test)
    implementation(libs.coil.compose)
}
