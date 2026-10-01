package com.payments.app.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/** The `libs` version catalog, for use inside convention plugins. */
internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.library(alias: String) = findLibrary(alias).get()

internal fun VersionCatalog.bundle(alias: String) = findBundle(alias).get()

internal fun VersionCatalog.pluginId(alias: String): String = findPlugin(alias).get().get().pluginId
