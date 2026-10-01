package com.payments.app.core.network.model

import kotlinx.serialization.Serializable

/** Response of `/payment/billing/entry/delete`: [status] is [STATUS_SUCCESS] on success. */
@Serializable
data class DeleteBillingEntryResponse(val status: Int) {
    companion object {
        const val STATUS_SUCCESS = 0
    }
}
