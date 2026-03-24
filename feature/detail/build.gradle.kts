plugins {
    alias(libs.plugins.metasearch.android.feature)
}

android {
    namespace = "com.metasearch.android.feature.detail"
}

dependencies {
    implementation(projects.core.data.api)
    implementation(projects.core.webview)
}
