plugins {
    alias(libs.plugins.payments.jvm.library)
}

dependencies {
    api(project(":core:model"))
    implementation(project(":core:common"))
    implementation(libs.javax.inject)
    // api: Flow is part of the repository interface.
    api(libs.kotlinx.coroutines.core)
}
