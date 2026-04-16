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

            extensions.configure<KspExtension> {
                arg("circuit.codegen.mode", "metro")
            }

            dependencies {
                implementationProject(":core:common")
                implementationProject(":core:designsystem")
                implementationProject(":core:ui")
                implementationProject(":data:domain")
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
