package com.payments.app.core.network.retrofit

import com.payments.app.core.network.model.BillingEntryDetailsResponse
import com.payments.app.core.network.model.BillingEntryRequest
import com.payments.app.core.network.model.BillingHeaderListResponse
import com.payments.app.core.network.model.BillingListRequest
import com.payments.app.core.network.model.DeleteBillingEntryResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit definition of the billing endpoints. All of them are POST with a JSON body, and the server
 * answers HTTP 200 even for "not found" (`details: null`) or a failed delete (`status: -1`).
 */
internal interface BillingApi {
    @POST("payment/billing/entry/headers")
    suspend fun getHeaders(@Body request: BillingListRequest): BillingHeaderListResponse

    @POST("payment/billing/entry/details")
    suspend fun getDetails(@Body request: BillingEntryRequest): BillingEntryDetailsResponse

    @POST("payment/billing/entry/delete")
    suspend fun delete(@Body request: BillingEntryRequest): DeleteBillingEntryResponse
}
