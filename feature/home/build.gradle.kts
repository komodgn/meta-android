plugins {
    alias(libs.plugins.metasearch.android.feature)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.feature.home"
}

dependencies {
    implementation(projects.domain.analysis.api)
    implementation(projects.domain.gallery.api)
    implementation(projects.domain.person.api)
    implementation(projects.core.di)
    implementation(projects.core.notification)
    implementation(projects.core.worker.api)

    implementation(libs.androidx.compose.paging)
}
