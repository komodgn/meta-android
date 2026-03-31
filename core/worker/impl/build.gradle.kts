plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.metasearch.android.impl"
}

dependencies {
    implementation(projects.core.worker.api)
    implementation(projects.core.di)

    implementation(libs.androidx.work.runtime)
}
