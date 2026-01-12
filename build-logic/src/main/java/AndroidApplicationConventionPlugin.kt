import com.android.build.api.dsl.ApplicationExtension
import com.metasearch.android.convention.ExtensionType
import com.metasearch.android.convention.Plugins
import com.metasearch.android.convention.configureBuildTypes
import com.metasearch.android.convention.configureKotlinAndroid
import com.metasearch.android.convention.libs
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
