plugins {
    alias(libs.plugins.metasearch.android.application)
    alias(libs.plugins.metasearch.android.application.compose)
    alias(libs.plugins.metasearch.android.hilt)
    // -------------리팩토링 중
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.metasearch"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

// -------------리팩토링 중
dependencies {

//    implementation("androidx.work:work-runtime:2.9.0")

//    // CircleImageView dependency
//    implementation("de.hdodenhof:circleimageview:3.1.0")
//    // PhotoView dependency
//    implementation("com.github.chrisbanes:PhotoView:2.3.0")
//    // SpinKit dependency
//    implementation("com.github.ybq:Android-SpinKit:1.4.0")
//    // CardView dependency
//    implementation("androidx.cardview:cardview:1.0.0")
//    // ColorPickerDialog dependency
//    implementation("me.jfenn.ColorPickerDialog:base:0.2.2")

    implementation(projects.core.common)
    implementation(projects.core.data.api)
    implementation(projects.core.data.impl)
    implementation(projects.core.datastore.api)
    implementation(projects.core.datastore.impl)
    implementation(projects.core.room.api)
    implementation(projects.core.room.impl)
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.notification)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)

    implementation(projects.feature.screens)
    implementation(projects.feature.splash)
    implementation(projects.feature.home)
    implementation(projects.feature.detail)
    implementation(projects.feature.search)
    implementation(projects.feature.main)
    implementation(projects.feature.person)
    implementation(projects.feature.graph)
    implementation(projects.feature.webview)

    implementation(libs.bundles.circuit)
    
    api(libs.circuit.codegen.annotation)
    ksp(libs.circuit.codegen.ksp)
}
