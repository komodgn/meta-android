import com.android.build.gradle.LibraryExtension
import com.metasearch.android.convention.ExtensionType
import com.metasearch.android.convention.Plugins
import com.metasearch.android.convention.configureBuildTypes
import com.metasearch.android.convention.libs
import com.metasearch.android.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import kotlin.text.toInt

internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.ANDROID_LIBRARY)
                apply(Plugins.KOTLIN_ANDROID)
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                defaultConfig.apply {
                    targetSdk = libs.versions.targetSdk.get().toInt()
                }

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )
            }
        }
    }
}
