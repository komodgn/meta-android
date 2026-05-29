plugins {
    alias(libs.plugins.metasearch.android.feature)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.feature.photo_detail"
}

dependencies {
    implementation(projects.domain.analysis.api)
    implementation(projects.domain.gallery.api)
}
