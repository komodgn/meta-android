plugins {
    alias(libs.plugins.metasearch.android.feature)
}

android {
    namespace = "com.metasearch.android.feature.home"
}

dependencies {
    implementation(projects.core.data.api)
    implementation(projects.core.di)
    implementation(projects.core.notification)

    implementation(libs.androidx.work.runtime)
}
