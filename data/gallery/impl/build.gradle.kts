plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.kotlin.library.serialization)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.metasearch.android.data.gallery.impl"

    testOptions {
        unitTests.all { test ->
            test.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(projects.core.di)
    implementation(projects.domain.gallery.api)

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
