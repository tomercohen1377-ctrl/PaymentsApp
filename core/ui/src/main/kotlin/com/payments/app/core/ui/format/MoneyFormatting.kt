package com.payments.app.core.ui.format

import com.payments.app.core.model.Money
import java.text.NumberFormat
import java.util.Locale

/**
 * The amount without a currency symbol, rounded for display only to the currency's decimals
 * (half-even): `14.4` → `14.40`, `25357.52885790945` → `25,357.53`.
 */
fun formatAmount(money: Money, locale: Locale = Locale.getDefault()): String =
    NumberFormat.getNumberInstance(locale).withFractionDigitsOf(money).format(money.amount)

/** Like [formatAmount], with the currency symbol placed per [locale]: `₪14.40`, `$25,357.53`. */
fun formatMoney(money: Money, locale: Locale = Locale.getDefault()): String =
    NumberFormat.getCurrencyInstance(locale).apply { currency = money.currency }
        .withFractionDigitsOf(money)
        .format(money.amount)

// NumberFormat rounds half-even by default; the Money value itself is never changed.
private fun NumberFormat.withFractionDigitsOf(money: Money): NumberFormat = apply {
    val digits = money.currency.defaultFractionDigits.coerceAtLeast(0)
    minimumFractionDigits = digits
    maximumFractionDigits = digits
}
