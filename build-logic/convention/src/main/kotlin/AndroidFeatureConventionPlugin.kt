import com.payments.app.buildlogic.library
import com.payments.app.buildlogic.libs
import com.payments.app.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * A feature module: Compose library + Hilt + the core modules and libraries every MVI screen uses.
 * Features never depend on each other; `:app` wires navigation between them.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("payments-android-library-compose"))
        pluginManager.apply(libs.pluginId("payments-hilt"))
        // Navigation 3 routes are @Serializable NavKeys.
        pluginManager.apply(libs.pluginId("kotlin-serialization"))

        dependencies {
            add("implementation", project(":core:model"))
            add("implementation", project(":core:domain"))
            add("implementation", project(":core:ui"))
            add("implementation", project(":core:designsystem"))

            add("implementation", libs.library("androidx-lifecycle-runtime-compose"))
            add("implementation", libs.library("androidx-lifecycle-viewmodel-compose"))
            add("implementation", libs.library("androidx-hilt-lifecycle-viewmodel-compose"))
            add("implementation", libs.library("androidx-navigation3-runtime"))
            add("implementation", libs.library("kotlinx-serialization-json"))

            add("testImplementation", project(":core:testing"))
        }
    }
}
