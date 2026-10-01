package com.payments.app.core.domain.usecase

import com.payments.app.core.domain.repository.BillingRepository
import javax.inject.Inject

/** Reloads the billing list from the server. */
class RefreshBillingHeadersUseCase @Inject constructor(
    private val repository: BillingRepository,
) {
    suspend operator fun invoke() = repository.refreshHeaders()
}
