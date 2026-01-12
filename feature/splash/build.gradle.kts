plugins {
    alias(libs.plugins.metasearch.android.feature)
}

android {
    namespace = "com.metasearch.android.feature.splash"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementation(libs.accompanist.permissions)
}
