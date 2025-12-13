import com.android.build.gradle.LibraryExtension
import com.example.metasearch.convention.ExtensionType
import com.example.metasearch.convention.Plugins
import com.example.metasearch.convention.configureBuildTypes
import com.example.metasearch.convention.libs
import com.example.metasearch.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
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
