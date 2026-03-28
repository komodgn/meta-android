plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    val conventionPluginClasses = listOf(
        "android.application" to "AndroidApplicationConventionPlugin",
        "android.application.compose" to "AndroidApplicationComposeConventionPlugin",
        "android.feature" to "AndroidFeatureConventionPlugin",
        "android.library" to "AndroidLibraryConventionPlugin",
        "android.library.compose" to "AndroidLibraryComposeConventionPlugin",
        "android.retrofit" to "AndroidRetrofitConventionPlugin",
        "android.roborazzi" to "AndroidRoborazziConventionPlugin",
        "jvm.library" to "JvmLibraryConventionPlugin",
        "kotlin.library.serialization" to "KotlinLibrarySerializationConventionPlugin",
    )

    plugins {
        conventionPluginClasses.forEach { conventionPluginClass ->
            val (pluginName, className) = conventionPluginClass
            register(pluginName) {
                id = "metasearch.$pluginName"
                implementationClass = className
            }
        }
    }
}
