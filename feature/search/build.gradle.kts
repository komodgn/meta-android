plugins {
    alias(libs.plugins.metasearch.android.feature)
}

android {
    namespace = "com.metasearch.android.feature.search"
}

dependencies {
    implementation(projects.core.data.api)
}
