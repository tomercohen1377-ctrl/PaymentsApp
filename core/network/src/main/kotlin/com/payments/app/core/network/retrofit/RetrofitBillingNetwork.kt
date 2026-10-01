package com.payments.app.core.network.retrofit

import com.payments.app.core.network.BillingNetworkDataSource
import com.payments.app.core.network.model.BillingEntryDetailsDto
import com.payments.app.core.network.model.BillingEntryHeaderDto
import com.payments.app.core.network.model.BillingEntryRequest
import com.payments.app.core.network.model.BillingListRequest
import com.payments.app.core.network.model.DeleteBillingEntryResponse
import com.payments.app.core.network.networkCall
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/** [BillingNetworkDataSource] backed by Retrofit. Together with [BillingApi], the only code that knows about Retrofit. */
@Singleton
internal class RetrofitBillingNetwork @Inject constructor(
    retrofit: Retrofit,
) : BillingNetworkDataSource {

    private val api = retrofit.create(BillingApi::class.java)

    override suspend fun getHeaders(): List<BillingEntryHeaderDto> = networkCall {
        api.getHeaders(BillingListRequest).headers
    }

    override suspend fun getDetails(billingId: Long): BillingEntryDetailsDto? = networkCall {
        api.getDetails(BillingEntryRequest(billingId)).details
    }

    override suspend fun delete(billingId: Long): DeleteBillingEntryResponse = networkCall {
        api.delete(BillingEntryRequest(billingId))
    }
}
