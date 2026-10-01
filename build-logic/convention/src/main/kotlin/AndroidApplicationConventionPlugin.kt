import com.android.build.api.dsl.ApplicationExtension
import com.payments.app.buildlogic.AndroidSdk
import com.payments.app.buildlogic.configureAndroid
import com.payments.app.buildlogic.configureCompose
import com.payments.app.buildlogic.libs
import com.payments.app.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** The `:app` module: Android application with Compose. */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("android-application"))
        extensions.configure<ApplicationExtension> {
            configureAndroid(this)
            configureCompose(this)
            defaultConfig.targetSdk = AndroidSdk.TARGET
        }
    }
}
