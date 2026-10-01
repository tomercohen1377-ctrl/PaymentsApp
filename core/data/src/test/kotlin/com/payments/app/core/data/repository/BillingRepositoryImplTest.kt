package com.payments.app.core.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.payments.app.core.common.error.AppException
import com.payments.app.core.data.BillingDtos
import com.payments.app.core.network.BillingNetworkDataSource
import com.payments.app.core.network.model.BillingEntryDetailsDto
import com.payments.app.core.network.model.BillingEntryHeaderDto
import com.payments.app.core.network.model.DeleteBillingEntryResponse
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BillingRepositoryImplTest {

    private val network = FakeBillingNetworkDataSource()

    private fun TestScope.repository() = BillingRepositoryImpl(network, StandardTestDispatcher(testScheduler))

    @Test
    fun `nothing is emitted before the first refresh`() = runTest {
        repository().observeHeaders().test {
            expectNoEvents()
        }
    }

    @Test
    fun `refresh publishes the mapped headers`() = runTest {
        network.headers = listOf(BillingDtos.header(id = 1), BillingDtos.header(id = 2))
        val repository = repository()

        repository.refreshHeaders()

        repository.observeHeaders().test {
            assertThat(awaitItem().map { it.id }).containsExactly(1L, 2L).inOrder()
        }
    }

    @Test
    fun `an empty server list is emitted as an empty list`() = runTest {
        val repository = repository()

        repository.refreshHeaders()

        repository.observeHeaders().test {
            assertThat(awaitItem()).isEmpty()
        }
    }

    @Test
    fun `a failed refresh keeps the previous list and propagates the error`() = runTest {
        network.headers = listOf(BillingDtos.header(id = 1))
        val repository = repository()
        repository.refreshHeaders()
        network.error = AppException.Network()

        val error = runCatching { repository.refreshHeaders() }.exceptionOrNull()

        assertThat(error).isInstanceOf(AppException.Network::class.java)
        repository.observeHeaders().test {
            assertThat(awaitItem().map { it.id }).containsExactly(1L)
        }
    }

    @Test
    fun `details are mapped, and an unknown id is null`() = runTest {
        network.details = mapOf(5165L to BillingDtos.details())
        val repository = repository()

        assertThat(repository.getDetails(5165)?.id).isEqualTo(5165)
        assertThat(repository.getDetails(999)).isNull()
    }

    @Test
    fun `successful delete removes the entry from the list`() = runTest {
        network.headers = listOf(BillingDtos.header(id = 1), BillingDtos.header(id = 2))
        val repository = repository()
        repository.refreshHeaders()

        repository.observeHeaders().test {
            assertThat(awaitItem().map { it.id }).containsExactly(1L, 2L)

            repository.delete(1)

            assertThat(awaitItem().map { it.id }).containsExactly(2L)
        }
        assertThat(network.deleteRequests).containsExactly(1L)
    }

    @Test
    fun `a non-zero delete status is a server error and keeps the entry`() = runTest {
        network.headers = listOf(BillingDtos.header(id = 1))
        network.deleteStatus = -1
        val repository = repository()
        repository.refreshHeaders()

        val error = runCatching { repository.delete(1) }.exceptionOrNull()

        assertThat(error).isInstanceOf(AppException.Server::class.java)
        assertThat((error as AppException.Server).code).isEqualTo(-1)
        repository.observeHeaders().test {
            assertThat(awaitItem().map { it.id }).containsExactly(1L)
        }
    }
}

private class FakeBillingNetworkDataSource : BillingNetworkDataSource {
    var headers: List<BillingEntryHeaderDto> = emptyList()
    var details: Map<Long, BillingEntryDetailsDto> = emptyMap()
    var deleteStatus: Int = DeleteBillingEntryResponse.STATUS_SUCCESS
    var error: Throwable? = null
    val deleteRequests = mutableListOf<Long>()

    override suspend fun getHeaders(): List<BillingEntryHeaderDto> {
        error?.let { throw it }
        return headers
    }

    override suspend fun getDetails(billingId: Long): BillingEntryDetailsDto? {
        error?.let { throw it }
        return details[billingId]
    }

    override suspend fun delete(billingId: Long): DeleteBillingEntryResponse {
        error?.let { throw it }
        deleteRequests += billingId
        return DeleteBillingEntryResponse(deleteStatus)
    }
}
