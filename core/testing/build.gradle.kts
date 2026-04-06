import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include
import org.gradle.kotlin.dsl.configure

plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.metro)
    alias(libs.plugins.ksp)
    alias(libs.plugins.roborazzi)
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
    api(projects.data.domain)

    implementation(projects.core.di)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)

    implementation(projects.domain.person.api)
    implementation(projects.domain.search.api)
    implementation(projects.domain.graph.api)
    implementation(projects.domain.analysis.api)
    implementation(projects.data.person.impl)
    implementation(projects.data.domain)
    implementation(projects.data.search.impl)
    implementation(projects.data.graph.impl)
    implementation(projects.domain.gallery.api)
    implementation(projects.data.gallery.impl)
    implementation(projects.domain.device.api)
    implementation(projects.data.device.impl)
    implementation(projects.data.analysis.impl)

    implementation(projects.feature.detail)
    implementation(projects.feature.graph)
    implementation(projects.feature.home)
    implementation(projects.feature.screens)
    implementation(projects.feature.main)
    implementation(projects.feature.person)
    implementation(projects.feature.screens)
    implementation(projects.feature.search)
    implementation(projects.feature.splash)

    implementation(libs.coil.test)
    implementation(libs.coil.compose)
    implementation(libs.androidx.compose.material3)
}
