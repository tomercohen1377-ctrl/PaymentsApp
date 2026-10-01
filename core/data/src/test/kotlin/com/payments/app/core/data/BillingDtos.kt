package com.payments.app.core.data

import com.payments.app.core.network.model.BillingEntryDetailsDto
import com.payments.app.core.network.model.BillingEntryHeaderDto

/** DTOs as the server sends them; defaults are entry 5165 from the recorded responses. */
internal object BillingDtos {
    fun header(
        id: Long = 5165,
        price: Double = 25357.52885790945,
        currencyCode: String = "USD",
        source: String = "Terminal",
        cardType: String = "Amex",
    ) = BillingEntryHeaderDto(
        id = id,
        price = price,
        created = 1592293845,
        entryNumber = 2,
        totalEntryCount = 17,
        source = source,
        currencyCode = currencyCode,
        cardType = cardType,
    )

    fun details(
        id: Long = 5165,
        price: Double = 25357.52885790945,
        amountPaid: Double = 2992.4083006040337,
        currencyCode: String = "USD",
    ) = BillingEntryDetailsDto(
        id = id,
        price = price,
        created = 1592293845,
        entryNumber = 2,
        totalEntryCount = 17,
        currencyCode = currencyCode,
        amountPaid = amountPaid,
        status = "Rejected",
        cardNumber = "7359327724873",
        cardType = "Amex",
        issuer = "Jcb",
        source = "Terminal",
        terminalName = "Emv 0",
        approvalNumber = "9748301211203",
        voucherNumber = "77-201-69",
    )
}
