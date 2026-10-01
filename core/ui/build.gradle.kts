plugins {
    alias(libs.plugins.payments.android.library.compose)
}

dependencies {
    api(project(":core:common"))
    api(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
