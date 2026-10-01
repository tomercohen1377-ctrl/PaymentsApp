import com.payments.app.buildlogic.library
import com.payments.app.buildlogic.libs
import com.payments.app.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Hilt with KSP, for Android modules. Apply after (or alongside) an Android convention plugin. */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("ksp"))
        pluginManager.withPlugin("com.android.base") {
            pluginManager.apply(libs.pluginId("hilt"))
            dependencies {
                add("implementation", libs.library("hilt-android"))
                add("ksp", libs.library("hilt-compiler"))
            }
        }
    }
}
