package com.payments.app.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal object AndroidSdk {
    const val COMPILE = 37
    const val TARGET = 37
    const val MIN = 24
}

/** Settings shared by the application and every Android library. */
internal fun Project.configureAndroid(extension: CommonExtension) {
    extension.apply {
        compileSdk = AndroidSdk.COMPILE
        defaultConfig.minSdk = AndroidSdk.MIN
        defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        compileOptions.sourceCompatibility = JavaVersion.VERSION_17
        compileOptions.targetCompatibility = JavaVersion.VERSION_17
    }
    configureUnitTests()
}

/** Compose compiler + the Compose libraries every Compose module needs, including previews. */
internal fun Project.configureCompose(extension: CommonExtension) {
    pluginManager.apply(libs.pluginId("compose-compiler"))
    extension.buildFeatures.compose = true

    dependencies {
        val bom = platform(libs.library("androidx-compose-bom"))
        add("implementation", bom)
        add("implementation", libs.library("androidx-compose-ui"))
        add("implementation", libs.library("androidx-compose-ui-tooling-preview"))
        add("implementation", libs.library("androidx-compose-material3"))
        add("debugImplementation", libs.library("androidx-compose-ui-tooling"))
        add("debugImplementation", libs.library("androidx-compose-ui-test-manifest"))

        add("androidTestImplementation", bom)
        add("androidTestImplementation", libs.library("androidx-compose-ui-test-junit4"))
        add("androidTestImplementation", libs.library("androidx-test-ext-junit"))
        add("androidTestImplementation", libs.library("androidx-test-runner"))
        add("androidTestImplementation", libs.library("androidx-espresso-core"))
        add("androidTestImplementation", libs.library("truth"))
    }
}

/** JUnit4, Truth, Turbine and coroutines-test for every module's unit tests. */
internal fun Project.configureUnitTests() {
    dependencies {
        add("testImplementation", libs.bundle("unit-test"))
    }
}
