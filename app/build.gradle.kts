plugins {
    alias(libs.plugins.metasearch.android.application)
    alias(libs.plugins.metasearch.android.application.compose)
    alias(libs.plugins.metasearch.android.hilt)
    // -------------리팩토링 중
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.metasearch"

    // -------------리팩토링 중
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

ksp {
    arg("circuit.codegen.mode", "hilt")
    arg("enableDataBinding", "true")
}

// -------------리팩토링 중
dependencies {

    // Retrofit dependency
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Gson dependency
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp dependency
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation("androidx.work:work-runtime:2.9.0")

    // neo4j dependency
    implementation("org.neo4j.driver:neo4j-java-driver:4.4.0")
    // fragment dependency
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    // CircleImageView dependency
    implementation("de.hdodenhof:circleimageview:3.1.0")
    // PhotoView dependency
    implementation("com.github.chrisbanes:PhotoView:2.3.0")
    // SplashScreen dependency
    implementation("androidx.core:core-splashscreen:1.0.1")
    // SpinKit dependency
    implementation("com.github.ybq:Android-SpinKit:1.4.0")
    // Glide dependency
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // CardView dependency
    implementation("androidx.cardview:cardview:1.0.0")
    // StyleableToast dependency
    implementation("io.github.muddz:styleabletoast:2.4.0")
    // ColorPickerDialog dependency
    implementation("me.jfenn.ColorPickerDialog:base:0.2.2")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.cardview:cardview:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.work:work-runtime:2.9.0")

    implementation(projects.core.common)
    implementation(projects.core.data.api)
    implementation(projects.core.data.impl)
    implementation(projects.core.datastore.api)
    implementation(projects.core.datastore.impl)
    implementation(projects.core.room.api)
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)

    implementation(projects.feature.screens)
    implementation(projects.feature.home)
    implementation(projects.feature.detail)
    implementation(projects.feature.search)
    implementation(projects.feature.main)
    implementation(projects.feature.person)
    implementation(projects.feature.graph)
    implementation(projects.feature.webview)
}
