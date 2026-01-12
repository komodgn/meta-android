import com.android.build.api.dsl.ApplicationExtension
import com.metasearch.android.convention.Plugins
import com.metasearch.android.convention.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.ANDROID_APPLICATION)
                apply(Plugins.KOTLIN_COMPOSE)
            }

            extensions.configure<ApplicationExtension> {
                configureCompose(this)
            }
        }
    }
}
