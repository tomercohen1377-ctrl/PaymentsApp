// Top-level build file. Every plugin is declared here (not applied) so its version resolves once
// and the build-logic convention plugins can apply it by id.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    // AGP 9 has built-in Kotlin support; declaring KGP here pins the Kotlin compiler version it uses.
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.spotless)
}

// Formatting + static checks: `./gradlew spotlessCheck` (CI) / `./gradlew spotlessApply` (fix).
// Mirrors the [*.{kt,kts}] section of .editorconfig, which the IDE reads.
val ktlintRules = mapOf(
    "ktlint_code_style" to "intellij_idea",
    // Leave signature layout to the author (one parameter per line is the house style).
    "ktlint_standard_class-signature" to "disabled",
    "ktlint_standard_function-signature" to "disabled",
    "max_line_length" to "120",
    "ij_kotlin_allow_trailing_comma" to "true",
    "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
    "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
    "ktlint_ignore_back_ticked_identifier" to "true",
)

spotless {
    kotlin {
        // Fixed-depth patterns (:app, :core:x / :feature:x, build-logic) so Spotless only walks src/
        // folders and never build/ output that a parallel `clean` may be deleting.
        target("*/src/**/*.kt", "*/*/src/**/*.kt")
        ktlint(libs.versions.ktlint.get())
            .customRuleSets(listOf(libs.compose.rules.ktlint.get().toString()))
            .editorConfigOverride(ktlintRules)
    }
    kotlinGradle {
        target("*.gradle.kts", "*/*.gradle.kts", "*/*/*.gradle.kts")
        ktlint(libs.versions.ktlint.get())
            .editorConfigOverride(ktlintRules)
    }
}
