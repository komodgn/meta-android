plugins {
    alias(libs.plugins.metasearch.android.library)
}

android {
    namespace = "com.metasearch.android.core.data.api"
}

dependencies {
    implementation(projects.core.model)

    api(libs.kotlinx.coroutines.core)
    api(libs.androidx.compose.paging)
}
