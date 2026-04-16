plugins {
    alias(libs.plugins.metasearch.android.feature)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.feature.search"
}

dependencies {
    implementation(projects.domain.search.api)
    implementation(projects.domain.file.api)
}
