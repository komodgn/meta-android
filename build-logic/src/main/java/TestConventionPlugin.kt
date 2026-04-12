import com.android.build.gradle.LibraryExtension
import com.metasearch.android.convention.implementation
import com.metasearch.android.convention.libs
import com.metasearch.android.convention.testImplementation
import com.metasearch.android.convention.testRuntimeOnly
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class TestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension> {
                testOptions {
                    unitTests.all { test ->
                        test.useJUnitPlatform()
                    }
                    unitTests.isReturnDefaultValues = true
                }
            }

            dependencies {
                implementation(libs.truth)

                testImplementation(libs.bundles.test.unit)

                testRuntimeOnly(libs.junit.jupiter.engine)
                testRuntimeOnly(libs.junit.platform.launcher)
            }
        }
    }
}
