plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.hilt)
    alias(libs.plugins.metasearch.kotlin.library.serialization)
}

android {
    namespace = "com.example.metasearch.core.data.impl"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "VERSION_NAME", "\"${libs.versions.versionName.get()}\"")
    }

    testOptions {
        unitTests.all { test ->
            test.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.data.api)
    implementation(projects.core.datastore.api)

    implementation(libs.okhttp)

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
