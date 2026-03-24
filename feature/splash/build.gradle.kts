plugins {
    alias(libs.plugins.metasearch.android.feature)
}

android {
    namespace = "com.metasearch.android.feature.splash"
}

dependencies {
    implementation(libs.accompanist.permissions)
}
