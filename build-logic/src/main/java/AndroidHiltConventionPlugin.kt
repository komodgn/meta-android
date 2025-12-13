import com.example.metasearch.convention.Plugins
import com.example.metasearch.convention.implementation
import com.example.metasearch.convention.libs
import com.example.metasearch.convention.ksp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.HILT)
                apply(Plugins.KSP)
            }

            dependencies {
                implementation(libs.hilt.android)
                ksp(libs.hilt.android.compiler)
            }
        }
    }
}
