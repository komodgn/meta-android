plugins {
    alias(libs.plugins.metasearch.android.library)
}

android {
    namespace = "com.example.metasearch.core.data.api"
}

dependencies {
    implementation(projects.core.model)

    api(libs.kotlinx.coroutines.core)
    api(libs.androidx.compose.paging)
}
