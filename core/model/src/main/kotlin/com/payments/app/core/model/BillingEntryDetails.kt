package com.payments.app.core.model

import java.time.Instant

/**
 * A billing entry's details (`/payment/billing/entry/details`).
 *
 * [customerName] and [paymentType] are required on the details screen but are not part of the API
 * contract; they stay `null` until the server provides them.
 *
 * @property remainingAmount What is left to charge. The API doesn't send it, so the data-layer
 * mapper computes it once as `price - amountPaid`; nothing else in the app calculates with money.
 */
data class BillingEntryDetails(
    val id: Long,
    val price: Money,
    val created: Instant,
    val entryNumber: Int,
    val totalEntryCount: Int,
    val amountPaid: Money,
    val remainingAmount: Money,
    val status: BillingStatus,
    val cardNumber: String,
    val cardType: CardType,
    val issuer: Issuer,
    val source: BillingSource,
    val terminalName: String,
    val approvalNumber: String,
    val voucherNumber: String,
    val customerName: String? = null,
    val paymentType: String? = null,
)
