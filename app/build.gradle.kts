import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.kotlin.dsl.configure

plugins {
    alias(libs.plugins.metasearch.android.application)
    alias(libs.plugins.metasearch.android.application.compose)
    alias(libs.plugins.metro)
    alias(libs.plugins.ksp)
    // -------------리팩토링 중
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.metasearch.android"
}

composeStabilityAnalyzer {
    enabled.set(true)
}

extensions.configure<KspExtension> {
    arg("circuit.codegen.mode", "metro")
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data.api)
    implementation(projects.core.data.impl)
    implementation(projects.core.datastore.api)
    implementation(projects.core.datastore.impl)
    implementation(projects.core.di)
    implementation(projects.core.room.api)
    implementation(projects.core.room.impl)
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.notification)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.webview)
    implementation(projects.core.worker.api)
    implementation(projects.core.worker.impl)
    implementation(projects.core.permissions.api)

    implementation(projects.domain.analysis.api)
    implementation(projects.data.analysis.impl)
    implementation(projects.feature.screens)
    implementation(projects.feature.splash)
    implementation(projects.feature.home)
    implementation(projects.feature.detail)
    implementation(projects.feature.search)
    implementation(projects.feature.main)
    implementation(projects.feature.person)
    implementation(projects.feature.graph)

    implementation(libs.bundles.circuit)

    api(libs.circuit.codegen.annotation)
    ksp(libs.circuit.codegen.ksp)
}
