package com.payments.app.core.model

/**
 * Parses an API string into an enum by name, ignoring case and any non-alphanumeric characters
 * ("MasterCard", "master_card" and "MASTERCARD" all match `MASTERCARD`). Unknown or missing values
 * map to [unknown] instead of throwing, so a new server value never breaks the app.
 *
 * [aliases] covers spellings that don't match a constant name, keyed by their normalized form.
 */
internal inline fun <reified E : Enum<E>> parseApiEnum(
    value: String?,
    unknown: E,
    aliases: Map<String, E> = emptyMap(),
): E {
    val key = value?.normalizedApiKey() ?: return unknown
    return aliases[key] ?: enumValues<E>().firstOrNull { it.name.normalizedApiKey() == key } ?: unknown
}

internal fun String.normalizedApiKey(): String = filter(Char::isLetterOrDigit).uppercase()
