package com.payments.app.core.model

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import java.math.BigDecimal
import java.util.Currency

class MoneyTest {

    @Test
    fun `keeps the server's amount exactly, without rounding`() {
        // A real price from server/list.json.
        val money = Money.of(25357.52885790945, "USD")

        assertThat(money.amount).isEqualTo(BigDecimal("25357.52885790945"))
        assertThat(money.currency).isEqualTo(Currency.getInstance("USD"))
    }

    @Test
    fun `doubles are converted without binary floating-point noise`() {
        assertThat(Money.of(14.4, "ILS").amount).isEqualTo(BigDecimal("14.4"))
    }

    @Test
    fun `string amounts are kept as written`() {
        assertThat(Money.of("14.40", "ILS").amount).isEqualTo(BigDecimal("14.40"))
    }

    @Test
    fun `an unknown currency code is rejected`() {
        assertThrows(IllegalArgumentException::class.java) { Money.of(1.0, "XYZ1") }
    }
}
