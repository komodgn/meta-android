import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.example.metasearch.convention.implementation
import com.example.metasearch.convention.api
import com.example.metasearch.convention.implementationProject
import com.example.metasearch.convention.libs
import com.example.metasearch.convention.ksp

internal class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply("metasearch.android.library")
                apply("metasearch.android.library.compose")
                apply("metasearch.android.hilt")
            }

            dependencies {
//                implementationProject(":core:common")
//                implementationProject(":core:designsystem")
//                implementationProject(":core:ui")
                implementationProject(":core:model")
//                implementationProject(":feature:screens")

                implementation(libs.bundles.circuit)
                api(libs.circuit.codegen.annotation)
                ksp(libs.circuit.codegen.ksp)
            }
        }
    }
}
