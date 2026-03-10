plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.hilt)
}

android {
    namespace = "com.metasearch.android.core.notification"
}

dependencies {
    implementation(projects.core.common)
}
