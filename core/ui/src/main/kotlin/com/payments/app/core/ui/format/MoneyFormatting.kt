package com.payments.app.core.ui.format

import com.payments.app.core.model.Money
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

/**
 * The amount without a currency symbol, rounded for display only to the currency's decimals
 * (half-even): `14.4` → `14.40`, `25357.52885790945` → `25,357.53`.
 */
fun formatAmount(money: Money, locale: Locale = Locale.getDefault()): String =
    NumberFormat.getNumberInstance(locale).withFractionDigitsOf(money).format(money.amount)

/**
 * Like [formatAmount], with the currency symbol: `₪14.40`, `$25,357.53`. The [locale] decides where
 * the symbol goes and the separators; the symbol itself is always the plain one, so an English (UK)
 * device shows `$28.80`, not `US$28.80`.
 */
fun formatMoney(money: Money, locale: Locale = Locale.getDefault()): String =
    NumberFormat.getCurrencyInstance(locale)
        .apply {
            currency = money.currency
            (this as? DecimalFormat)?.let { format ->
                format.decimalFormatSymbols = format.decimalFormatSymbols.apply {
                    currencySymbol = money.currency.getSymbol(Locale.US)
                }
            }
        }
        .withFractionDigitsOf(money)
        .format(money.amount)

// NumberFormat rounds half-even by default; the Money value itself is never changed.
private fun NumberFormat.withFractionDigitsOf(money: Money): NumberFormat = apply {
    val digits = money.currency.defaultFractionDigits.coerceAtLeast(0)
    minimumFractionDigits = digits
    maximumFractionDigits = digits
}
