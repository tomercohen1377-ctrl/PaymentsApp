package com.payments.app.core.ui.preview

import com.payments.app.core.model.BillingEntryDetails
import com.payments.app.core.model.BillingEntryHeader
import com.payments.app.core.model.BillingSource
import com.payments.app.core.model.BillingStatus
import com.payments.app.core.model.CardType
import com.payments.app.core.model.Issuer
import com.payments.app.core.model.Money
import java.time.Instant

/** Sample billing data for `@Preview`s, covering every source, both currencies and MasterCard. */
object PreviewBillingData {
    private val created: Instant = Instant.parse("2022-08-21T16:02:00Z")

    val terminalIls = BillingEntryHeader(
        id = 1,
        price = Money.of("14.40", "ILS"),
        created = created,
        entryNumber = 3,
        totalEntryCount = 6,
        source = BillingSource.TERMINAL,
        cardType = CardType.VISA,
    )

    val posUsd = terminalIls.copy(id = 2, price = Money.of("25357.52885790945", "USD"), source = BillingSource.POS)

    val manualMasterCard = terminalIls.copy(
        id = 3,
        price = Money.of("82575.5", "USD"),
        source = BillingSource.MANUAL,
        cardType = CardType.MASTERCARD,
    )

    val headers: List<BillingEntryHeader> = listOf(terminalIls, posUsd, manualMasterCard)

    val passedDetails = BillingEntryDetails(
        id = 1,
        price = Money.of("14.40", "ILS"),
        created = created,
        entryNumber = 3,
        totalEntryCount = 6,
        amountPaid = Money.of("28.80", "ILS"),
        remainingAmount = Money.of("28.80", "ILS"),
        status = BillingStatus.PASSED,
        cardNumber = "4580123456782345",
        cardType = CardType.VISA,
        issuer = Issuer.MAX,
        source = BillingSource.TERMINAL,
        terminalName = "EMV",
        approvalNumber = "34576934",
        voucherNumber = "23-333-343",
    )

    val rejectedDetails = passedDetails.copy(status = BillingStatus.REJECTED, source = BillingSource.POS)
}
