plugins {
    alias(libs.plugins.metasearch.android.library)
}

android {
    namespace = "com.metasearch.android.core.room.api"
}

dependencies {
    implementation(projects.data.domain)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
}
