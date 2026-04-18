import com.android.build.gradle.BaseExtension
import com.metasearch.android.convention.Plugins
import com.metasearch.android.convention.libs
import com.metasearch.android.convention.testImplementation
import com.metasearch.android.convention.testImplementationProject
import io.github.takahirom.roborazzi.RoborazziExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal class AndroidRoborazziConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.ROBORAZZI)
            }

            extensions.configure<RoborazziExtension> {
                generateComposePreviewRobolectricTests {
                    enable.set(true)
                    packages.set(listOf("com.metasearch.android"))
                    includePrivatePreviews.set(true)
                    useScanOptionParametersInTester.set(true)
                    testerQualifiedClassName.set("com.metasearch.android.core.testing.previewtester.MetaSearchPreviewTester")
                }
            }

            extensions.configure<BaseExtension> {
                testOptions {
                    unitTests.all {
                        it.systemProperties["robolectric.graphicsMode"] = "NATIVE"
                        it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
                    }
                }
            }

            dependencies {
                testImplementation(libs.robolectric)
                testImplementationProject(":core:testing")
                testImplementation(libs.junit)
                testImplementation(libs.roborazzi.preview.scanner.support)
                testImplementation(libs.composable.preview.scanner)
            }
        }
    }
}
