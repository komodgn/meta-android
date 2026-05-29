plugins {
    alias(libs.plugins.metasearch.android.feature)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.feature.person_detail"
}

dependencies {
    implementation(projects.domain.person.api)
}
