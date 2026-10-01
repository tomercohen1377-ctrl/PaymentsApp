package com.payments.app.feature.billinglist.presentation

import androidx.annotation.StringRes
import com.payments.app.core.model.BillingEntryHeader
import com.payments.app.core.ui.mvi.UiAction
import com.payments.app.core.ui.mvi.UiEvent
import com.payments.app.core.ui.mvi.UiState

sealed interface BillingListUiState : UiState {
    /** The first load is in flight and there is nothing to show yet. */
    data object Loading : BillingListUiState

    /** The first load failed. */
    data class Error(@StringRes val message: Int) : BillingListUiState

    /**
     * The list is loaded (possibly empty). Later failures (e.g. a failed pull-to-refresh) are sent as
     * [BillingListAction.ShowMessage] and the list stays on screen.
     */
    data class Success(
        val items: List<BillingEntryHeader>,
        val isRefreshing: Boolean = false,
    ) : BillingListUiState
}

sealed interface BillingListEvent : UiEvent {
    /** Reload the list: pull-to-refresh, or Retry after a failed first load. */
    data object Refresh : BillingListEvent
    data class ItemClicked(val billingId: Long) : BillingListEvent
    data class UploadClicked(val billingId: Long) : BillingListEvent
}

sealed interface BillingListAction : UiAction {
    data class NavigateToDetails(val billingId: Long) : BillingListAction
    data class ShowMessage(@StringRes val message: Int) : BillingListAction
}
