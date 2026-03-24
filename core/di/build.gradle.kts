plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.metasearch.android.core.di"
}

dependencies {
    implementation(libs.androidx.work.runtime)
}
