import com.android.build.api.dsl.LibraryExtension
import com.payments.app.buildlogic.configureAndroid
import com.payments.app.buildlogic.libs
import com.payments.app.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** An Android library without UI (e.g. `:core:network`, `:core:data`). */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("android-library"))
        extensions.configure<LibraryExtension> {
            configureAndroid(this)
            // ":core:network" -> "com.payments.app.core.network", so modules don't repeat it.
            namespace = "com.payments.app" + path.replace(':', '.')
        }
    }
}
