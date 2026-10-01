package com.payments.app.core.testing

import com.payments.app.core.model.BillingEntryDetails
import com.payments.app.core.model.BillingEntryHeader
import com.payments.app.core.model.BillingSource
import com.payments.app.core.model.BillingStatus
import com.payments.app.core.model.CardType
import com.payments.app.core.model.Issuer
import com.payments.app.core.model.Money
import java.time.Instant

/**
 * Builders for test data with sensible defaults, so each test only spells out the fields it is
 * about. Values mirror real rows from `server/list.json` / `details.json`.
 */
object TestData {
    /** 2022-08-21 16:02:00 UTC */
    val CREATED: Instant = Instant.ofEpochSecond(1_661_097_720)

    fun header(
        id: Long = 5165,
        price: Money = Money.of("14.40", "ILS"),
        created: Instant = CREATED,
        entryNumber: Int = 3,
        totalEntryCount: Int = 6,
        source: BillingSource = BillingSource.TERMINAL,
        cardType: CardType = CardType.VISA,
    ) = BillingEntryHeader(
        id = id,
        price = price,
        created = created,
        entryNumber = entryNumber,
        totalEntryCount = totalEntryCount,
        source = source,
        cardType = cardType,
    )

    fun details(
        id: Long = 5165,
        price: Money = Money.of("28.80", "ILS"),
        created: Instant = CREATED,
        entryNumber: Int = 3,
        totalEntryCount: Int = 6,
        amountPaid: Money = Money.of("14.40", "ILS"),
        remainingAmount: Money = Money.of("14.40", "ILS"),
        status: BillingStatus = BillingStatus.PASSED,
        cardNumber: String = "7359327724873",
        cardType: CardType = CardType.VISA,
        issuer: Issuer = Issuer.MAX,
        source: BillingSource = BillingSource.TERMINAL,
        terminalName: String = "Emv 0",
        approvalNumber: String = "9748301211203",
        voucherNumber: String = "77-201-69",
        customerName: String? = null,
        paymentType: String? = null,
    ) = BillingEntryDetails(
        id = id,
        price = price,
        created = created,
        entryNumber = entryNumber,
        totalEntryCount = totalEntryCount,
        amountPaid = amountPaid,
        remainingAmount = remainingAmount,
        status = status,
        cardNumber = cardNumber,
        cardType = cardType,
        issuer = issuer,
        source = source,
        terminalName = terminalName,
        approvalNumber = approvalNumber,
        voucherNumber = voucherNumber,
        customerName = customerName,
        paymentType = paymentType,
    )
}
