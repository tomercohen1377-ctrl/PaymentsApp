package com.payments.app.core.domain.usecase

import com.payments.app.core.domain.repository.BillingRepository
import com.payments.app.core.model.BillingEntryDetails
import javax.inject.Inject

/** Loads one entry's details; `null` when the entry doesn't exist. */
class GetBillingDetailsUseCase @Inject constructor(
    private val repository: BillingRepository,
) {
    suspend operator fun invoke(billingId: Long): BillingEntryDetails? = repository.getDetails(billingId)
}
