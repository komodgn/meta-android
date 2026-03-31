plugins {
    alias(libs.plugins.metasearch.android.library)
}

android {
    namespace = "com.metasearch.android.api"
}

dependencies {
    api(libs.androidx.work.runtime)
}
