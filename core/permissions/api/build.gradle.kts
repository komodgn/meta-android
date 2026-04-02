plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.library.compose)
}

android {
    namespace = "com.metasearch.android.core.permissions.api"
}

dependencies {
    implementation(projects.core.ui)

    implementation(libs.kotlinx.collections.immutable)
    api(libs.accompanist.permissions)
}
