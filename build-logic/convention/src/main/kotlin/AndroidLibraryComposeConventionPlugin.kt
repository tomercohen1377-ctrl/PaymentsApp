import com.android.build.api.dsl.LibraryExtension
import com.payments.app.buildlogic.configureCompose
import com.payments.app.buildlogic.libs
import com.payments.app.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** An Android library with Compose UI (e.g. `:core:designsystem`, `:core:ui`). */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("payments-android-library"))
        extensions.configure<LibraryExtension> {
            configureCompose(this)
        }
    }
}
