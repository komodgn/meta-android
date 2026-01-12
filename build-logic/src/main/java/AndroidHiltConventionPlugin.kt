import com.metasearch.android.convention.Plugins
import com.metasearch.android.convention.implementation
import com.metasearch.android.convention.libs
import com.metasearch.android.convention.ksp
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
