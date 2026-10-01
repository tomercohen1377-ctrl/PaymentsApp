plugins {
    alias(libs.plugins.payments.jvm.library)
}

// Shared test helpers (MainDispatcherRule, fakes, test data). Exposed with `api` so a module's
// tests get JUnit and coroutines-test by depending on :core:testing alone.
dependencies {
    api(project(":core:domain"))
    api(project(":core:model"))
    api(libs.junit4)
    api(libs.kotlinx.coroutines.test)
}
