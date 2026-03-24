import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.metasearch.android.convention.implementation
import com.metasearch.android.convention.api
import com.metasearch.android.convention.implementationProject
import com.metasearch.android.convention.libs
import com.metasearch.android.convention.ksp
import org.gradle.kotlin.dsl.configure

internal class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply("metasearch.android.library")
                apply("metasearch.android.library.compose")
                apply("com.google.devtools.ksp")
                apply("dev.zacsweers.metro")
            }

            extensions.configure<KspExtension> {
                arg("circuit.codegen.mode", "metro")
            }

            dependencies {
                implementationProject(":core:common")
                implementationProject(":core:designsystem")
                implementationProject(":core:ui")
                implementationProject(":core:model")
                implementationProject(":feature:screens")

                implementation(libs.compose.effects)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.bundles.circuit)
                api(libs.circuit.codegen.annotation)
                ksp(libs.circuit.codegen.ksp)
            }
        }
    }
}
