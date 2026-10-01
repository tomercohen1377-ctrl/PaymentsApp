plugins {
    alias(libs.plugins.payments.android.library)
    alias(libs.plugins.payments.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    buildFeatures {
        buildConfig = true
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
