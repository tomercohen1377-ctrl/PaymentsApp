package com.payments.app.feature.billingdetails.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.payments.app.core.common.error.AppException
import com.payments.app.core.domain.usecase.DeleteBillingEntryUseCase
import com.payments.app.core.domain.usecase.GetBillingDetailsUseCase
import com.payments.app.core.testing.FakeBillingRepository
import com.payments.app.core.testing.MainDispatcherRule
import com.payments.app.core.testing.TestData
import com.payments.app.feature.billingdetails.R
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import com.payments.app.core.ui.R as CoreUiR

class BillingDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeBillingRepository().apply {
        serverHeaders = listOf(TestData.header(id = 5165), TestData.header(id = 5166))
        serverDetails = mapOf(5165L to TestData.details(id = 5165))
    }

    private fun viewModel(billingId: Long = 5165) = BillingDetailsViewModel(
        billingId = billingId,
        getDetails = GetBillingDetailsUseCase(repository),
        deleteEntry = DeleteBillingEntryUseCase(repository),
    )

    private fun success() = BillingDetailsUiState.Success(TestData.details(id = 5165))

    @Test
    fun `loads the details for the id`() = runTest {
        assertThat(viewModel().state.value).isEqualTo(success())
    }

    @Test
    fun `an unknown id is an Error with the not-found message`() = runTest {
        assertThat(
            viewModel(billingId = 999).state.value,
        ).isEqualTo(BillingDetailsUiState.Error(R.string.details_not_found))
    }

    @Test
    fun `a failed load is Error, and retry loads again`() = runTest {
        repository.detailsError = AppException.Network()
        val vm = viewModel()
        assertThat(vm.state.value).isEqualTo(BillingDetailsUiState.Error(CoreUiR.string.error_network))

        repository.detailsError = null
        vm.onEvent(BillingDetailsEvent.Retry)

        assertThat(vm.state.value).isEqualTo(success())
    }

    @Test
    fun `delete asks for confirmation first, and dismissing cancels it`() = runTest {
        val vm = viewModel()

        vm.onEvent(BillingDetailsEvent.DeleteClicked)
        assertThat(vm.state.value).isEqualTo(success().copy(showDeleteConfirmation = true))

        vm.onEvent(BillingDetailsEvent.DeleteDismissed)
        assertThat(vm.state.value).isEqualTo(success())
        assertThat(repository.deletedIds).isEmpty()
    }

    @Test
    fun `confirming deletes the entry and navigates back`() = runTest {
        val vm = viewModel()
        vm.onEvent(BillingDetailsEvent.DeleteClicked)

        vm.action.test {
            vm.onEvent(BillingDetailsEvent.DeleteConfirmed)
            assertThat(awaitItem()).isEqualTo(BillingDetailsAction.NavigateBack)
        }
        assertThat(repository.deletedIds).containsExactly(5165L)
    }

    @Test
    fun `shows progress while deleting and ignores a second delete`() = runTest {
        repository.deleteGate = CompletableDeferred()
        val vm = viewModel()
        vm.onEvent(BillingDetailsEvent.DeleteClicked)

        vm.onEvent(BillingDetailsEvent.DeleteConfirmed)
        assertThat(vm.state.value).isEqualTo(success().copy(isDeleting = true))

        vm.onEvent(BillingDetailsEvent.DeleteClicked)
        assertThat(vm.state.value).isEqualTo(success().copy(isDeleting = true))
    }

    @Test
    fun `a failed delete keeps the details and shows a message`() = runTest {
        repository.deleteError = AppException.Server(code = -1)
        val vm = viewModel()
        vm.onEvent(BillingDetailsEvent.DeleteClicked)

        vm.action.test {
            vm.onEvent(BillingDetailsEvent.DeleteConfirmed)
            assertThat(awaitItem()).isEqualTo(BillingDetailsAction.ShowMessage(CoreUiR.string.error_server))
        }
        assertThat(vm.state.value).isEqualTo(success())
        assertThat(repository.deletedIds).isEmpty()
    }

    @Test
    fun `delete is ignored before the details are loaded`() = runTest {
        repository.detailsError = AppException.Network()
        val vm = viewModel()

        vm.onEvent(BillingDetailsEvent.DeleteClicked)
        vm.onEvent(BillingDetailsEvent.DeleteConfirmed)

        assertThat(vm.state.value).isInstanceOf(BillingDetailsUiState.Error::class.java)
        assertThat(repository.deletedIds).isEmpty()
    }
}
