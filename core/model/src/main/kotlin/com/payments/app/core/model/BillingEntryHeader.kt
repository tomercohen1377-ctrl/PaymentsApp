package com.payments.app.core.model

import java.time.Instant

/**
 * A row of the billing list (`/payment/billing/entry/headers`).
 *
 * @property price The amount, in [Money.currency] (the API's `currencyCode`).
 * @property entryNumber With [totalEntryCount], the billing number indicator ("3/6").
 */
data class BillingEntryHeader(
    val id: Long,
    val price: Money,
    val created: Instant,
    val entryNumber: Int,
    val totalEntryCount: Int,
    val source: BillingSource,
    val cardType: CardType,
)
