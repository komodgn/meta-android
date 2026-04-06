import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.kotlin.dsl.configure

plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metro)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.metasearch.android.core.room.impl"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

extensions.configure<KspExtension> {
    arg("circuit.codegen.mode", "metro")
}

dependencies {
    implementation(projects.data.domain)
    implementation(projects.core.room.api)

    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
