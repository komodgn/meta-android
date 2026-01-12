plugins {
    alias(libs.plugins.metasearch.android.library)
}
android {
    namespace = "com.metasearch.android.core.datastore.api"
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.kotlinx.coroutines.core)
}
