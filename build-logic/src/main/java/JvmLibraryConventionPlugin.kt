import com.example.metasearch.convention.Plugins
import com.example.metasearch.convention.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.KOTLIN_JVM)
            }

            configureKotlinJvm()
        }
    }
}
