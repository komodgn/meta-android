plugins {
    alias(libs.plugins.metasearch.android.feature)
}

android {
    namespace = "com.metasearch.android.feature.detail"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementation(projects.core.data.api)
    implementation(projects.core.webview)
}
