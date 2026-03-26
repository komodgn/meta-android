import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.kotlin.dsl.configure

plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.metro)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.metasearch.android.core.testing"

    kotlin {
        compilerOptions {
            freeCompilerArgs.add("-Xcontext-parameters")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

extensions.configure<KspExtension> {
    arg("circuit.codegen.mode", "metro")
}

dependencies {
    // Robolectric & JUnit
    api(libs.junit)
    api(libs.robolectric)
    api(libs.androidx.junit)
    api(libs.androidx.test.core)

    // Compose UI Test
    api(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // ScreenShot Test
    api(libs.roborazzi)
    api(libs.roborazzi.compose)

    api(libs.kotlinx.coroutines.test)

    api(projects.core.common)
    api(projects.core.data.api)
    api(projects.core.model)
    implementation(projects.core.di)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)

    implementation(libs.androidx.compose.material3)
}
