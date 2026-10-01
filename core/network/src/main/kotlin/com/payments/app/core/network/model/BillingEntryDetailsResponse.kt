package com.payments.app.core.network.model

import kotlinx.serialization.Serializable

/** Response of `/payment/billing/entry/details`. [details] is `null` when the id doesn't exist. */
@Serializable
data class BillingEntryDetailsResponse(
    val details: BillingEntryDetailsDto? = null,
)

/**
 * `BillingEntryDetails` from the contract.
 *
 * [customerName] and [paymentType] are shown on the details screen but are not part of the contract
 * and are never sent; they are optional so they are picked up if the server adds them.
 */
@Serializable
data class BillingEntryDetailsDto(
    val id: Long,
    val price: Double,
    /** Epoch seconds. */
    val created: Long,
    val entryNumber: Int,
    val totalEntryCount: Int,
    val currencyCode: String,
    val amountPaid: Double,
    val status: String,
    val cardNumber: String,
    val cardType: String,
    val issuer: String,
    val source: String,
    val terminalName: String,
    val approvalNumber: String,
    val voucherNumber: String,
    val customerName: String? = null,
    val paymentType: String? = null,
)
