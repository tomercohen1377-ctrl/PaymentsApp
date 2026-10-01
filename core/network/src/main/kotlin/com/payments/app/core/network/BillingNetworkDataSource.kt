package com.payments.app.core.network

import com.payments.app.core.network.model.BillingEntryDetailsDto
import com.payments.app.core.network.model.BillingEntryHeaderDto
import com.payments.app.core.network.model.DeleteBillingEntryResponse

/**
 * The billing server's API. `:core:data` depends on this interface only, never on Retrofit, which
 * also makes it trivial to fake in tests.
 *
 * Every call throws [com.payments.app.core.common.error.AppException] on failure.
 */
interface BillingNetworkDataSource {
    /** `/payment/billing/entry/headers` */
    suspend fun getHeaders(): List<BillingEntryHeaderDto>

    /** `/payment/billing/entry/details`. `null` when no entry has [billingId]. */
    suspend fun getDetails(billingId: Long): BillingEntryDetailsDto?

    /** `/payment/billing/entry/delete`. The caller interprets [DeleteBillingEntryResponse.status]. */
    suspend fun delete(billingId: Long): DeleteBillingEntryResponse
}
