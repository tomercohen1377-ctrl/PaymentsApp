package com.payments.app.core.model

import java.math.BigDecimal
import java.util.Currency

/**
 * An amount in a currency, holding exactly what the server sent. The app only presents money; it
 * never calculates with it or rounds it. Rounding to the currency's decimals happens only when
 * formatting for display (`:core:ui`, `format/`).
 *
 * [amount] is a [BigDecimal] so the value is carried without floating-point surprises.
 */
data class Money(
    val amount: BigDecimal,
    val currency: Currency,
) {
    companion object {
        /** @throws IllegalArgumentException if [currencyCode] is not an ISO 4217 code. */
        fun of(
            amount: String,
            currencyCode: String,
        ): Money = Money(BigDecimal(amount), Currency.getInstance(currencyCode))

        /**
         * For APIs that send amounts as JSON doubles (this one does). [BigDecimal.valueOf] keeps the
         * double's shortest decimal representation (`14.4` → `14.4`), unlike `BigDecimal(double)`
         * (`14.4` → `14.4000000000000003552…`).
         *
         * @throws IllegalArgumentException if [currencyCode] is not an ISO 4217 code.
         */
        fun of(amount: Double, currencyCode: String): Money =
            Money(BigDecimal.valueOf(amount), Currency.getInstance(currencyCode))
    }
}
