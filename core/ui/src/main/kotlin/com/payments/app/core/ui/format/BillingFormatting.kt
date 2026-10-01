package com.payments.app.core.ui.format

/** The billing number indicator, e.g. `3/6` for entry 3 of 6. */
fun formatBillingNumber(entryNumber: Int, totalEntryCount: Int): String = "$entryNumber/$totalEntryCount"

/**
 * Masks a card number so only the last [visibleDigits] remain: `7359327724873` → `****4873`.
 * Numbers with no more than [visibleDigits] digits are returned unchanged.
 */
fun maskCardNumber(cardNumber: String, visibleDigits: Int = 4): String {
    val digits = cardNumber.filter(Char::isDigit)
    return if (digits.length <= visibleDigits) cardNumber else CARD_MASK + digits.takeLast(visibleDigits)
}

private const val CARD_MASK = "****"
