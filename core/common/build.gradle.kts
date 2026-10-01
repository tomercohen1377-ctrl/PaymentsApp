plugins {
    alias(libs.plugins.payments.jvm.library)
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
