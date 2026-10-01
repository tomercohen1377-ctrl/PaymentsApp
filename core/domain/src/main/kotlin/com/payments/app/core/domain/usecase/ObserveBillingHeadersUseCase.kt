package com.payments.app.core.domain.usecase

import com.payments.app.core.domain.repository.BillingRepository
import com.payments.app.core.model.BillingEntryHeader
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Streams the billing list; see [BillingRepository.observeHeaders]. */
class ObserveBillingHeadersUseCase @Inject constructor(
    private val repository: BillingRepository,
) {
    operator fun invoke(): Flow<List<BillingEntryHeader>> = repository.observeHeaders()
}
