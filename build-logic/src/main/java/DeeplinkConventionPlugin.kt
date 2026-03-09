import com.metasearch.android.convention.Plugins
import com.metasearch.android.convention.implementation
import com.metasearch.android.convention.ksp
import com.metasearch.android.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class DeeplinkConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.KSP)
            }

            dependencies {
                implementation(libs.deeplink.dispatch)
                ksp(libs.deeplink.dispatch.processor)
            }
        }
    }
}
