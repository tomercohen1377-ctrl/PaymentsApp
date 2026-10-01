package com.payments.app.core.data.di

import com.payments.app.core.data.repository.BillingRepositoryImpl
import com.payments.app.core.domain.repository.BillingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    @Binds
    fun bindsBillingRepository(impl: BillingRepositoryImpl): BillingRepository
}
