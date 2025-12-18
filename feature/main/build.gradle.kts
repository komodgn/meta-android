plugins {
    alias(libs.plugins.metasearch.android.feature)
}

android {
    namespace = "com.example.metasearch.feature.main"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementation(libs.compose.system.ui.controller)
    implementation(libs.androidx.core.splashscreen)
}
