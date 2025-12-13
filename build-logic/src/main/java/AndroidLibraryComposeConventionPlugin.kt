import com.android.build.api.dsl.LibraryExtension
import com.example.metasearch.convention.Plugins
import com.example.metasearch.convention.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.ANDROID_LIBRARY)
                apply(Plugins.KOTLIN_COMPOSE)
            }

            extensions.configure<LibraryExtension> {
                configureCompose(this)
            }
        }
    }
}
