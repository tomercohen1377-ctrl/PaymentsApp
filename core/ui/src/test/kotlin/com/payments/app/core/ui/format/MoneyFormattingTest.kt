package com.payments.app.core.ui.format

import com.google.common.truth.Truth.assertThat
import com.payments.app.core.model.Money
import org.junit.Test
import java.util.Locale

class MoneyFormattingTest {

    @Test
    fun `amount has two decimals and no currency symbol`() {
        assertThat(formatAmount(Money.of("14.4", "ILS"), Locale.US)).isEqualTo("14.40")
        assertThat(formatAmount(Money.of(25357.52885790945, "USD"), Locale.US)).isEqualTo("25,357.53")
    }

    @Test
    fun `rounding is half-even and for display only`() {
        val money = Money.of(0.125, "ILS")

        assertThat(formatAmount(money, Locale.US)).isEqualTo("0.12")
        assertThat(formatMoney(Money.of(0.135, "ILS"), Locale.US)).isEqualTo("₪0.14")
        assertThat(money.amount.toPlainString()).isEqualTo("0.125")
    }

    @Test
    fun `amount follows the locale's separators`() {
        assertThat(formatAmount(Money.of("25357.53", "USD"), Locale.GERMANY)).isEqualTo("25.357,53")
    }

    @Test
    fun `money shows the currency symbol`() {
        assertThat(formatMoney(Money.of("14.40", "ILS"), Locale.US)).isEqualTo("₪14.40")
        assertThat(formatMoney(Money.of("28.80", "USD"), Locale.US)).isEqualTo("$28.80")
    }

    @Test
    fun `money in Hebrew puts the symbol after the amount`() {
        val formatted = formatMoney(Money.of("14.40", "ILS"), Locale.forLanguageTag("he-IL"))

        // CLDR adds bidi marks and a no-break space; compare the visible characters only.
        assertThat(formatted.filterNot { it == '‏' || it == '‎' }.replace(' ', ' '))
            .isEqualTo("14.40 ₪")
    }
}
