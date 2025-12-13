plugins {
    alias(libs.plugins.metasearch.android.library)
}
android {
    namespace = "com.example.metasearch.core.datastore.api"
}

dependencies {
    implementation(projects.core.model)
}
