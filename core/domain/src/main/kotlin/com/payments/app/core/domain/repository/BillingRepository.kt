package com.payments.app.core.domain.repository

import com.payments.app.core.model.BillingEntryDetails
import com.payments.app.core.model.BillingEntryHeader
import kotlinx.coroutines.flow.Flow

/**
 * Billing entries. The headers list is kept in memory and shared by every screen, so a delete on the
 * details screen is reflected in the list without fetching it again.
 *
 * Every suspend function throws [com.payments.app.core.common.error.AppException] on failure.
 */
interface BillingRepository {
    /**
     * The billing headers, in server order. Emits after the first successful [refreshHeaders] and
     * again after every refresh or delete; nothing is emitted before the first load.
     */
    fun observeHeaders(): Flow<List<BillingEntryHeader>>

    /** Fetches the headers from the server and publishes them to [observeHeaders]. */
    suspend fun refreshHeaders()

    /** The entry's details, or `null` if the server has no entry with [billingId]. */
    suspend fun getDetails(billingId: Long): BillingEntryDetails?

    /** Deletes the entry on the server, then removes it from [observeHeaders]. */
    suspend fun delete(billingId: Long)
}
