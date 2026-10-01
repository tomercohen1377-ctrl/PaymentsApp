package com.payments.app.feature.billinglist.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.payments.app.core.common.error.AppException
import com.payments.app.core.domain.usecase.ObserveBillingHeadersUseCase
import com.payments.app.core.domain.usecase.RefreshBillingHeadersUseCase
import com.payments.app.core.testing.FakeBillingRepository
import com.payments.app.core.testing.MainDispatcherRule
import com.payments.app.core.testing.TestData
import com.payments.app.feature.billinglist.R
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import com.payments.app.core.ui.R as CoreUiR

class BillingListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeBillingRepository()

    private fun viewModel() = BillingListViewModel(
        observeHeaders = ObserveBillingHeadersUseCase(repository),
        refreshHeaders = RefreshBillingHeadersUseCase(repository),
    )

    @Test
    fun `loads the list on start`() = runTest {
        repository.serverHeaders = listOf(TestData.header(id = 1), TestData.header(id = 2))

        val vm = viewModel()

        assertThat(vm.state.value).isEqualTo(
            BillingListUiState.Success(listOf(TestData.header(id = 1), TestData.header(id = 2))),
        )
    }

    @Test
    fun `is Loading until the first load completes`() = runTest {
        val gate = CompletableDeferred<Unit>()
        repository.refreshGate = gate
        repository.serverHeaders = listOf(TestData.header())
        val vm = viewModel()

        assertThat(vm.state.value).isEqualTo(BillingListUiState.Loading)

        gate.complete(Unit)

        assertThat(vm.state.value).isEqualTo(BillingListUiState.Success(listOf(TestData.header())))
    }

    @Test
    fun `an empty list is Success, not Loading`() = runTest {
        val vm = viewModel()

        assertThat(vm.state.value).isEqualTo(BillingListUiState.Success(emptyList()))
    }

    @Test
    fun `a failed first load is Error with a mapped message`() = runTest {
        repository.refreshError = AppException.Network()

        val vm = viewModel()

        assertThat(vm.state.value).isEqualTo(BillingListUiState.Error(CoreUiR.string.error_network))
    }

    @Test
    fun `retry goes back to Loading and then shows the list`() = runTest {
        repository.refreshError = AppException.Network()
        val vm = viewModel()
        repository.refreshError = null
        repository.serverHeaders = listOf(TestData.header())
        val gate = CompletableDeferred<Unit>()
        repository.refreshGate = gate

        vm.onEvent(BillingListEvent.Retry)
        assertThat(vm.state.value).isEqualTo(BillingListUiState.Loading)

        gate.complete(Unit)
        assertThat(vm.state.value).isEqualTo(BillingListUiState.Success(listOf(TestData.header())))
    }

    @Test
    fun `pull-to-refresh keeps the list and shows the refresh indicator`() = runTest {
        repository.serverHeaders = listOf(TestData.header())
        val vm = viewModel()
        repository.refreshGate = CompletableDeferred()

        vm.onEvent(BillingListEvent.Refresh)

        assertThat(vm.state.value).isEqualTo(BillingListUiState.Success(listOf(TestData.header()), isRefreshing = true))
    }

    @Test
    fun `a failed refresh over a loaded list keeps it and shows a message`() = runTest {
        repository.serverHeaders = listOf(TestData.header())
        val vm = viewModel()
        repository.refreshError = AppException.Server(code = 500)

        vm.action.test {
            vm.onEvent(BillingListEvent.Refresh)
            assertThat(awaitItem()).isEqualTo(BillingListAction.ShowMessage(CoreUiR.string.error_server))
        }
        assertThat(vm.state.value).isEqualTo(BillingListUiState.Success(listOf(TestData.header())))
    }

    @Test
    fun `clicking an item navigates to its details`() = runTest {
        val vm = viewModel()

        vm.action.test {
            vm.onEvent(BillingListEvent.ItemClicked(5165))
            assertThat(awaitItem()).isEqualTo(BillingListAction.NavigateToDetails(5165))
        }
    }

    @Test
    fun `clicking upload shows that it is not available`() = runTest {
        val vm = viewModel()

        vm.action.test {
            vm.onEvent(BillingListEvent.UploadClicked(5165))
            assertThat(awaitItem()).isEqualTo(BillingListAction.ShowMessage(R.string.upload_not_available))
        }
    }

    @Test
    fun `a delete elsewhere removes the entry from the list`() = runTest {
        repository.serverHeaders = listOf(TestData.header(id = 1), TestData.header(id = 2))
        val vm = viewModel()

        repository.delete(1)

        assertThat(vm.state.value).isEqualTo(BillingListUiState.Success(listOf(TestData.header(id = 2))))
    }
}
