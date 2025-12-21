plugins {
    alias(libs.plugins.metasearch.android.feature)
}

android {
    namespace = "com.example.metasearch.feature.detail"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementation(projects.core.data.api)

    implementation(libs.coil.compose)
}
