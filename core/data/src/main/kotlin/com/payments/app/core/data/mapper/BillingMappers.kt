package com.payments.app.core.data.mapper

import com.payments.app.core.common.error.AppException
import com.payments.app.core.model.BillingEntryDetails
import com.payments.app.core.model.BillingEntryHeader
import com.payments.app.core.model.BillingSource
import com.payments.app.core.model.BillingStatus
import com.payments.app.core.model.CardType
import com.payments.app.core.model.Issuer
import com.payments.app.core.model.Money
import com.payments.app.core.network.model.BillingEntryDetailsDto
import com.payments.app.core.network.model.BillingEntryHeaderDto
import java.time.Instant

/*
 * DTO -> domain model. The only place that knows the server's representations: `created` in epoch
 * seconds, amounts as doubles with a separate currency code, enums as strings.
 */

internal fun BillingEntryHeaderDto.toModel(): BillingEntryHeader = BillingEntryHeader(
    id = id,
    price = money(price, currencyCode),
    created = Instant.ofEpochSecond(created),
    entryNumber = entryNumber,
    totalEntryCount = totalEntryCount,
    source = BillingSource.fromApi(source),
    cardType = CardType.fromApi(cardType),
)

internal fun BillingEntryDetailsDto.toModel(): BillingEntryDetails {
    val price = money(price, currencyCode)
    val amountPaid = money(amountPaid, currencyCode)
    return BillingEntryDetails(
        id = id,
        price = price,
        created = Instant.ofEpochSecond(created),
        entryNumber = entryNumber,
        totalEntryCount = totalEntryCount,
        amountPaid = amountPaid,
        // The API doesn't send it, so this is the app's one money calculation: exact, unrounded.
        remainingAmount = Money(price.amount - amountPaid.amount, price.currency),
        status = BillingStatus.fromApi(status),
        cardNumber = cardNumber,
        cardType = CardType.fromApi(cardType),
        issuer = Issuer.fromApi(issuer),
        source = BillingSource.fromApi(source),
        terminalName = terminalName,
        approvalNumber = approvalNumber,
        voucherNumber = voucherNumber,
        customerName = customerName,
        paymentType = paymentType,
    )
}

/** A currency code that isn't ISO 4217 means the response is unusable, like any other parse error. */
private fun money(amount: Double, currencyCode: String): Money = try {
    Money.of(amount, currencyCode)
} catch (e: IllegalArgumentException) {
    throw AppException.InvalidResponse(e)
}
