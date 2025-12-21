plugins {
    alias(libs.plugins.metasearch.android.feature)
}

android {
    namespace = "com.example.metasearch.feature.webview"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}
