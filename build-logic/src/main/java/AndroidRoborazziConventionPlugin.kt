import com.android.build.gradle.BaseExtension
import com.metasearch.android.convention.Plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidRoborazziConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.ROBORAZZI)
            }

            extensions.configure<BaseExtension> {
                testOptions {
                    unitTests.all {
                        it.systemProperties["robolectric.graphicsMode"] = "NATIVE"
                        it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
                    }
                }
            }
        }
    }
}
