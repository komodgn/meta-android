plugins {
    alias(libs.plugins.metasearch.android.feature)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.feature.graph_detail"
}

dependencies {
    implementation(projects.domain.graph.api)
    implementation(projects.core.webview)
}
