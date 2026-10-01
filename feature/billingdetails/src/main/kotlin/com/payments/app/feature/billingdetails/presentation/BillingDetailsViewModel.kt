package com.payments.app.feature.billingdetails.presentation

import androidx.lifecycle.viewModelScope
import com.payments.app.core.domain.usecase.DeleteBillingEntryUseCase
import com.payments.app.core.domain.usecase.GetBillingDetailsUseCase
import com.payments.app.core.ui.error.toMessageRes
import com.payments.app.core.ui.mvi.MviViewModel
import com.payments.app.feature.billingdetails.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/**
 * The billing id comes in through assisted injection: the navigation entry receives the typed
 * `BillingDetailsKey` and passes its id to [Factory.create], so the ViewModel is a plain class in
 * unit tests and is scoped to its back stack entry.
 */
@HiltViewModel(assistedFactory = BillingDetailsViewModel.Factory::class)
class BillingDetailsViewModel @AssistedInject constructor(
    @Assisted private val billingId: Long,
    private val getDetails: GetBillingDetailsUseCase,
    private val deleteEntry: DeleteBillingEntryUseCase,
) : MviViewModel<BillingDetailsUiState, BillingDetailsEvent, BillingDetailsAction>(BillingDetailsUiState.Loading) {

    @AssistedFactory
    interface Factory {
        fun create(billingId: Long): BillingDetailsViewModel
    }

    init {
        load()
    }

    override fun handleEvent(event: BillingDetailsEvent) {
        when (event) {
            BillingDetailsEvent.Retry -> load()
            BillingDetailsEvent.DeleteClicked -> showDeleteConfirmation()
            BillingDetailsEvent.DeleteDismissed -> dismissDeleteConfirmation()
            BillingDetailsEvent.DeleteConfirmed -> delete()
        }
    }

    private fun load() {
        setState(BillingDetailsUiState.Loading)
        viewModelScope.launch {
            val newState = try {
                getDetails(billingId)?.let(BillingDetailsUiState::Success) ?: notFound()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                BillingDetailsUiState.Error(e.toMessageRes())
            }
            setState(newState)
        }
    }

    private fun notFound() = BillingDetailsUiState.Error(R.string.details_not_found)

    private fun showDeleteConfirmation() {
        updateSuccess { if (isDeleting) this else copy(showDeleteConfirmation = true) }
    }

    private fun dismissDeleteConfirmation() {
        updateSuccess { copy(showDeleteConfirmation = false) }
    }

    private fun delete() {
        val current = currentState as? BillingDetailsUiState.Success ?: return
        if (current.isDeleting) return
        setState(current.copy(showDeleteConfirmation = false, isDeleting = true))
        viewModelScope.launch {
            try {
                deleteEntry(billingId)
                onDeleted()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                onDeleteFailed(e)
            }
        }
    }

    // The list drops the entry through the shared repository; just leave the screen.
    private fun onDeleted() {
        sendAction(BillingDetailsAction.NavigateBack)
    }

    private fun onDeleteFailed(error: Exception) {
        updateSuccess { copy(isDeleting = false) }
        sendAction(BillingDetailsAction.ShowMessage(error.toMessageRes()))
    }

    /** Applies [reducer] only while the details are shown. */
    private fun updateSuccess(reducer: BillingDetailsUiState.Success.() -> BillingDetailsUiState) = setState {
        if (this is BillingDetailsUiState.Success) reducer() else this
    }
}
