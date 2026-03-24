plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.metasearch.android.core.notification"
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.androidx.core.ktx)
}
