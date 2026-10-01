package com.payments.app.core.testing

import com.payments.app.core.domain.repository.BillingRepository
import com.payments.app.core.model.BillingEntryDetails
import com.payments.app.core.model.BillingEntryHeader
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update

/**
 * In-memory [BillingRepository] for ViewModel tests. Behaves like the real one (nothing emitted
 * before the first refresh; delete removes the entry from the list), with hooks to inject failures
 * and to hold a call in flight.
 */
class FakeBillingRepository : BillingRepository {

    private val headers = MutableStateFlow<List<BillingEntryHeader>?>(null)

    /** What [refreshHeaders] loads. */
    var serverHeaders: List<BillingEntryHeader> = emptyList()

    /** What [getDetails] returns, by id. */
    var serverDetails: Map<Long, BillingEntryDetails> = emptyMap()

    /** When set, the next call of the matching function throws it. */
    var refreshError: Throwable? = null
    var detailsError: Throwable? = null
    var deleteError: Throwable? = null

    /** When set, the matching call suspends until it completes, to observe in-flight states. */
    var refreshGate: CompletableDeferred<Unit>? = null
    var deleteGate: CompletableDeferred<Unit>? = null

    val deletedIds = mutableListOf<Long>()

    override fun observeHeaders(): Flow<List<BillingEntryHeader>> = headers.filterNotNull()

    override suspend fun refreshHeaders() {
        refreshGate?.await()
        refreshError?.let { throw it }
        headers.value = serverHeaders
    }

    override suspend fun getDetails(billingId: Long): BillingEntryDetails? {
        detailsError?.let { throw it }
        return serverDetails[billingId]
    }

    override suspend fun delete(billingId: Long) {
        deleteGate?.await()
        deleteError?.let { throw it }
        deletedIds += billingId
        serverHeaders = serverHeaders.filterNot { it.id == billingId }
        headers.update { current -> current?.filterNot { it.id == billingId } }
    }
}
