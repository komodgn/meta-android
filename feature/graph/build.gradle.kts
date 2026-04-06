plugins {
    alias(libs.plugins.metasearch.android.feature)
}

android {
    namespace = "com.metasearch.android.feature.graph"
}

dependencies {
    implementation(projects.domain.graph.api)
    implementation(projects.core.webview)
}
