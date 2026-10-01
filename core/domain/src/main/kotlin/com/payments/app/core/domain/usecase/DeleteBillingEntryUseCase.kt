package com.payments.app.core.domain.usecase

import com.payments.app.core.domain.repository.BillingRepository
import javax.inject.Inject

/** Deletes an entry; it disappears from the billing list on success. */
class DeleteBillingEntryUseCase @Inject constructor(
    private val repository: BillingRepository,
) {
    suspend operator fun invoke(billingId: Long) = repository.delete(billingId)
}
