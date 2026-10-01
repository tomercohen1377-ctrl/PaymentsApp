plugins {
    alias(libs.plugins.payments.android.library)
    alias(libs.plugins.payments.hilt)
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(project(":core:testing"))
}
