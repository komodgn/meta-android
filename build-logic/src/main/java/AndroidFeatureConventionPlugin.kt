import com.android.build.gradle.LibraryExtension
import com.google.devtools.ksp.gradle.KspExtension
import com.metasearch.android.convention.androidTestImplementationProject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.metasearch.android.convention.implementation
import com.metasearch.android.convention.api
import com.metasearch.android.convention.implementationProject
import com.metasearch.android.convention.libs
import com.metasearch.android.convention.ksp
import com.metasearch.android.convention.testImplementationProject
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

internal class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply("metasearch.android.library")
                apply("metasearch.android.library.compose")
                apply("com.google.devtools.ksp")
                apply("dev.zacsweers.metro")
                apply("metasearch.android.roborazzi")
            }

            tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
                compilerOptions {
                    if (freeCompilerArgs.get().none { it == "-Xcontext-parameters" }) {
                        freeCompilerArgs.add("-Xcontext-parameters")
                    }

                    if (freeCompilerArgs.get().none { it.contains("ExperimentalTestApi") }) {
                        freeCompilerArgs.add("-opt-in=androidx.compose.ui.test.ExperimentalTestApi")
                    }
                }
            }

            extensions.configure<KspExtension> {
                arg("circuit.codegen.mode", "metro")
            }

            extensions.configure<LibraryExtension> {
                testOptions {
                    unitTests.isIncludeAndroidResources = true
                }
            }

            dependencies {
                implementationProject(":core:common")
                implementationProject(":core:designsystem")
                implementationProject(":core:ui")
                implementationProject(":core:model")
                implementationProject(":core:permissions:api")
                implementationProject(":feature:screens")

                implementation(libs.compose.effects)
                implementation(libs.kotlinx.collections.immutable)

                implementation(libs.bundles.circuit)
                api(libs.circuit.codegen.annotation)
                ksp(libs.circuit.codegen.ksp)

                testImplementationProject(":core:testing")
                androidTestImplementationProject(":core:testing")
            }
        }
    }
}
