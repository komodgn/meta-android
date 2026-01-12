plugins {
    alias(libs.plugins.metasearch.android.feature)
}

android {
    namespace = "com.metasearch.android.feature.webview"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}
