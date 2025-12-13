import com.android.build.api.dsl.ApplicationExtension
import com.example.metasearch.convention.ExtensionType
import com.example.metasearch.convention.Plugins
import com.example.metasearch.convention.configureBuildTypes
import com.example.metasearch.convention.configureKotlinAndroid
import com.example.metasearch.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.ANDROID_APPLICATION)
                apply(Plugins.KOTLIN_ANDROID)
                apply(Plugins.KSP)
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = libs.versions.applicationId.get()
                    targetSdk = libs.versions.targetSdk.get().toInt()
                    versionCode = libs.versions.versionCode.get().toInt()
                    versionName = libs.versions.versionName.get()
                }

                buildFeatures {
                    dataBinding = true
                }

                configureKotlinAndroid(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.APPLICATION,
                )
            }
        }
    }
}
