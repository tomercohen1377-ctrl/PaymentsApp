package com.payments.app.core.data.repository

import com.payments.app.core.common.dispatcher.Dispatcher
import com.payments.app.core.common.dispatcher.PaymentsDispatchers
import com.payments.app.core.common.error.AppException
import com.payments.app.core.data.mapper.toModel
import com.payments.app.core.domain.repository.BillingRepository
import com.payments.app.core.model.BillingEntryDetails
import com.payments.app.core.model.BillingEntryHeader
import com.payments.app.core.network.BillingNetworkDataSource
import com.payments.app.core.network.model.DeleteBillingEntryResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [BillingRepository] backed by the billing server, with the headers list cached in memory.
 * The server is the source of truth; there is no persistent cache.
 *
 * Retrofit's suspend calls are main-safe, so only the mapping of the (486-row) list is moved off the
 * caller's thread.
 */
@Singleton
internal class BillingRepositoryImpl @Inject constructor(
    private val network: BillingNetworkDataSource,
    @Dispatcher(PaymentsDispatchers.Default) private val mappingDispatcher: CoroutineDispatcher,
) : BillingRepository {

    /** `null` until the first successful refresh, so "not loaded" and "empty" stay distinguishable. */
    private val headers = MutableStateFlow<List<BillingEntryHeader>?>(null)

    override fun observeHeaders(): Flow<List<BillingEntryHeader>> = headers.filterNotNull()

    override suspend fun refreshHeaders() {
        val dtos = network.getHeaders()
        headers.value = withContext(mappingDispatcher) { dtos.map { it.toModel() } }
    }

    override suspend fun getDetails(billingId: Long): BillingEntryDetails? = network.getDetails(billingId)?.toModel()

    override suspend fun delete(billingId: Long) {
        val response = network.delete(billingId)
        if (response.status != DeleteBillingEntryResponse.STATUS_SUCCESS) {
            throw AppException.Server(code = response.status)
        }
        headers.update { current -> current?.filterNot { it.id == billingId } }
    }
}
