plugins {
    alias(libs.plugins.metasearch.android.feature)
}

android {
    namespace = "com.metasearch.android.feature.person"
}

dependencies {
    implementation(projects.core.data.api)
}
