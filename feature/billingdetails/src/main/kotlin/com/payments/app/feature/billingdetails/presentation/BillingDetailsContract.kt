package com.payments.app.feature.billingdetails.presentation

import androidx.annotation.StringRes
import com.payments.app.core.model.BillingEntryDetails
import com.payments.app.core.ui.mvi.UiAction
import com.payments.app.core.ui.mvi.UiEvent
import com.payments.app.core.ui.mvi.UiState

sealed interface BillingDetailsUiState : UiState {
    data object Loading : BillingDetailsUiState

    /** Loading failed, or the server has no entry with this id (e.g. it was deleted). */
    data class Error(@StringRes val message: Int) : BillingDetailsUiState

    data class Success(
        val details: BillingEntryDetails,
        val showDeleteConfirmation: Boolean = false,
        val isDeleting: Boolean = false,
    ) : BillingDetailsUiState
}

sealed interface BillingDetailsEvent : UiEvent {
    data object Retry : BillingDetailsEvent
    data object DeleteClicked : BillingDetailsEvent
    data object DeleteConfirmed : BillingDetailsEvent
    data object DeleteDismissed : BillingDetailsEvent
}

sealed interface BillingDetailsAction : UiAction {
    data object NavigateBack : BillingDetailsAction
    data class ShowMessage(@StringRes val message: Int) : BillingDetailsAction
}
