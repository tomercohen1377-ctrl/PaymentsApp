import java.util.Properties

plugins {
    alias(libs.plugins.payments.android.library)
    alias(libs.plugins.payments.hilt)
    alias(libs.plugins.kotlin.serialization)
}

/**
 * The billing server's URL. Defaults to the local test server as seen from the Android emulator.
 * Override it with `-Ppayments.baseUrl=...` or `payments.baseUrl=...` in local.properties, e.g.
 * `http://localhost:8030/` after `adb reverse tcp:8030 tcp:8030` (a physical device, or an emulator
 * when the Mac firewall blocks incoming connections to node).
 */
val baseUrl: String = providers.gradleProperty("payments.baseUrl")
    .orElse(
        providers.fileContents(rootProject.layout.projectDirectory.file("local.properties")).asText
            .map { text -> Properties().apply { load(text.reader()) }.getProperty("payments.baseUrl").orEmpty() },
    )
    .orNull
    ?.takeIf { it.isNotBlank() }
    ?: "http://10.0.2.2:8030/"

android {
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.okhttp.mockwebserver)
}
