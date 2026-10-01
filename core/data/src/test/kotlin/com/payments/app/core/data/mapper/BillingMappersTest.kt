package com.payments.app.core.data.mapper

import com.google.common.truth.Truth.assertThat
import com.payments.app.core.common.error.AppException
import com.payments.app.core.data.BillingDtos
import com.payments.app.core.model.BillingEntryDetails
import com.payments.app.core.model.BillingEntryHeader
import com.payments.app.core.model.BillingSource
import com.payments.app.core.model.BillingStatus
import com.payments.app.core.model.CardType
import com.payments.app.core.model.Issuer
import com.payments.app.core.model.Money
import org.junit.Assert.assertThrows
import org.junit.Test
import java.math.BigDecimal
import java.time.Instant

class BillingMappersTest {

    @Test
    fun `header maps every field`() {
        assertThat(BillingDtos.header().toModel()).isEqualTo(
            BillingEntryHeader(
                id = 5165,
                price = Money.of(25357.52885790945, "USD"),
                created = Instant.parse("2020-06-16T07:50:45Z"),
                entryNumber = 2,
                totalEntryCount = 17,
                source = BillingSource.TERMINAL,
                cardType = CardType.AMEX,
            ),
        )
    }

    @Test
    fun `details maps every field`() {
        assertThat(BillingDtos.details().toModel()).isEqualTo(
            BillingEntryDetails(
                id = 5165,
                price = Money.of(25357.52885790945, "USD"),
                created = Instant.parse("2020-06-16T07:50:45Z"),
                entryNumber = 2,
                totalEntryCount = 17,
                amountPaid = Money.of(2992.4083006040337, "USD"),
                remainingAmount = Money.of("22365.1205573054163", "USD"),
                status = BillingStatus.REJECTED,
                cardNumber = "7359327724873",
                cardType = CardType.AMEX,
                issuer = Issuer.JCB,
                source = BillingSource.TERMINAL,
                terminalName = "Emv 0",
                approvalNumber = "9748301211203",
                voucherNumber = "77-201-69",
            ),
        )
    }

    @Test
    fun `created is read as epoch seconds`() {
        assertThat(BillingDtos.header().toModel().created.epochSecond).isEqualTo(1592293845)
    }

    @Test
    fun `amounts keep the server's exact value`() {
        val details = BillingDtos.details().toModel()

        assertThat(details.price.amount).isEqualTo(BigDecimal("25357.52885790945"))
        assertThat(details.amountPaid.amount).isEqualTo(BigDecimal("2992.4083006040337"))
    }

    @Test
    fun `remaining amount is price minus paid, exact and in the entry's currency`() {
        val details = BillingDtos.details(price = 28.8, amountPaid = 14.4, currencyCode = "ILS").toModel()

        assertThat(details.remainingAmount).isEqualTo(Money.of("14.4", "ILS"))
    }

    @Test
    fun `server enum strings are parsed, including the Meastro spelling`() {
        val header = BillingDtos.header(source = "Pos", cardType = "Meastro").toModel()

        assertThat(header.source).isEqualTo(BillingSource.POS)
        assertThat(header.cardType).isEqualTo(CardType.MAESTRO)
    }

    @Test
    fun `an invalid currency code is an invalid response`() {
        assertThrows(AppException.InvalidResponse::class.java) { BillingDtos.header(currencyCode = "??").toModel() }
    }
}
