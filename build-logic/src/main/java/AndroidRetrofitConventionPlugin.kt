import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.metasearch.android.convention.libs
import com.metasearch.android.convention.implementation

internal class AndroidRetrofitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply("metasearch.kotlin.library.serialization")
            }

            dependencies {
                implementation(libs.retrofit)
                implementation(libs.retrofit.kotlinx.serialization.converter)
                implementation(libs.okhttp.logging.interceptor)
            }
        }
    }
}
