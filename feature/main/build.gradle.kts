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
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.core.splashscreen)
}
