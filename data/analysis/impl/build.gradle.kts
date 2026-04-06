plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.kotlin.library.serialization)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.metasearch.android.data.analysis.impl"

    testOptions {
        unitTests.all { test ->
            test.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.network)
    implementation(projects.domain.analysis.api)
    implementation(projects.domain.device.api)
    implementation(projects.domain.gallery.api)
    implementation(projects.domain.person.api)
    implementation(projects.domain.search.api)
    implementation(projects.core.datastore.api)
    implementation(projects.core.di)
    implementation(projects.core.room.api)

    implementation(libs.okhttp)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.core.ktx)

    // 코루틴 테스트 유틸리티
    testImplementation(libs.kotlinx.coroutines.test)

    // JUnit 5 (JUnit Jupiter)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)

    // Mocking 프레임워크 (Mockito + Kotlin extension)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
}
