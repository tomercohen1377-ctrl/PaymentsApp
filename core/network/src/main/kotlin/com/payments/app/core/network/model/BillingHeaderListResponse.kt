package com.payments.app.core.network.model

import kotlinx.serialization.Serializable

/** Response of `/payment/billing/entry/headers`. */
@Serializable
data class BillingHeaderListResponse(
    val headers: List<BillingEntryHeaderDto> = emptyList(),
)

/**
 * A header as the server actually sends it. This differs from the PDF's `BillingEntryHeader`: the
 * PDF lists `val: Int`, which the server never sends, and omits [totalEntryCount], which it does
 * send (and which the billing number indicator "3/6" needs).
 */
@Serializable
data class BillingEntryHeaderDto(
    val id: Long,
    val price: Double,
    /** Epoch seconds. */
    val created: Long,
    val entryNumber: Int,
    val totalEntryCount: Int,
    val source: String,
    val currencyCode: String,
    val cardType: String,
)
