import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.metasearch.android.library)
    alias(libs.plugins.metasearch.android.retrofit)
    alias(libs.plugins.metro)
    alias(libs.plugins.metasearch.test)
}

android {
    namespace = "com.metasearch.android.data.remote"

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "WEB_SERVER_BASE_URL", getServerBaseUrl("DEBUG_WEB_SERVER_URL"))

            buildConfigField("String", "AI_SERVER_BASE_URL", getServerBaseUrl("DEBUG_AI_SERVER_URL"))

            buildConfigField("String", "OPENAI_API_KEY", getServerBaseUrl("OPENAI_API_KEY"))
        }

        release {
            buildConfigField("String", "WEB_SERVER_BASE_URL", getServerBaseUrl("RELEASE_WEB_SERVER_URL"))

            buildConfigField("String", "AI_SERVER_BASE_URL", getServerBaseUrl("RELEASE_AI_SERVER_URL"))

            buildConfigField("String", "OPENAI_API_KEY", getServerBaseUrl("OPENAI_API_KEY"))
        }
    }
}

dependencies {
    implementation(projects.core.datastore.api)
    implementation(projects.core.di)
    implementation(projects.core.worker.api)
    implementation(projects.data.domain)

    implementation(libs.androidx.core.ktx)

    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)
}

fun getServerBaseUrl(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}
