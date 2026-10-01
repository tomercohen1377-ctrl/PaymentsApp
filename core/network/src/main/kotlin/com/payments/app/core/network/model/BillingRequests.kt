package com.payments.app.core.network.model

import kotlinx.serialization.Serializable

/** `BillingListRequest {}`: the headers endpoint takes an empty JSON object. */
@Serializable
data object BillingListRequest

/** `BillingEntryRequest { billingId: Long }`, used by the details and delete endpoints. */
@Serializable
data class BillingEntryRequest(val billingId: Long)
