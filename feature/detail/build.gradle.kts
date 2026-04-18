plugins {
    alias(libs.plugins.metasearch.android.feature)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.feature.detail"
}

dependencies {
    implementation(projects.domain.graph.api)
    implementation(projects.domain.person.api)
    implementation(projects.domain.gallery.api)
    implementation(projects.domain.analysis.api)
    implementation(projects.core.webview)
}
