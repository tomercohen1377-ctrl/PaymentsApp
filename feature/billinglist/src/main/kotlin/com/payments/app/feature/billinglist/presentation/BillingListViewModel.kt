package com.payments.app.feature.billinglist.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.payments.app.core.domain.usecase.ObserveBillingHeadersUseCase
import com.payments.app.core.domain.usecase.RefreshBillingHeadersUseCase
import com.payments.app.core.model.BillingEntryHeader
import com.payments.app.core.ui.error.toMessageRes
import com.payments.app.core.ui.mvi.MviViewModel
import com.payments.app.feature.billinglist.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class BillingListViewModel @Inject constructor(
    observeHeaders: ObserveBillingHeadersUseCase,
    private val refreshHeaders: RefreshBillingHeadersUseCase,
) : MviViewModel<BillingListUiState, BillingListEvent, BillingListAction>(BillingListUiState.Loading) {

    private val isRefreshing = MutableStateFlow(false)

    /** A refresh failure while nothing is loaded yet. */
    private val blockingError = MutableStateFlow<Int?>(null)

    private var refreshJob: Job? = null

    init {
        // The repository emits nothing until the first load; `null` stands for "not loaded yet".
        val headers = observeHeaders().map<List<BillingEntryHeader>, List<BillingEntryHeader>?> {
            it
        }.onStart { emit(null) }
        combine(headers, isRefreshing, blockingError, ::toUiState)
            .onEach(::setState)
            .launchIn(viewModelScope)
        refresh()
    }

    override fun handleEvent(event: BillingListEvent) {
        when (event) {
            BillingListEvent.Refresh -> refresh()
            is BillingListEvent.ItemClicked -> navigateToDetails(event.billingId)
            is BillingListEvent.UploadClicked -> showUploadNotAvailable()
        }
    }

    private fun navigateToDetails(billingId: Long) {
        sendAction(BillingListAction.NavigateToDetails(billingId))
    }

    // The task asks for the button but not for what it does.
    private fun showUploadNotAvailable() {
        sendAction(BillingListAction.ShowMessage(R.string.upload_not_available))
    }

    /** The single place that decides which [BillingListUiState] the screen is in. Loaded content wins. */
    private fun toUiState(
        headers: List<BillingEntryHeader>?,
        isRefreshing: Boolean,
        @StringRes blockingError: Int?,
    ): BillingListUiState = when {
        headers != null -> BillingListUiState.Success(headers, isRefreshing)
        blockingError != null -> BillingListUiState.Error(blockingError)
        else -> BillingListUiState.Loading
    }

    private fun refresh() {
        // Ignore repeated pulls while a refresh is already running.
        if (refreshJob?.isActive == true) return
        refreshJob = viewModelScope.launch {
            blockingError.value = null
            isRefreshing.value = true
            try {
                refreshHeaders()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                onRefreshFailed(e)
            } finally {
                isRefreshing.value = false
            }
        }
    }

    /** With a list on screen, keep it and show a message; with nothing loaded, show the error screen. */
    private fun onRefreshFailed(error: Exception) {
        val message = error.toMessageRes()
        if (currentState is BillingListUiState.Success) {
            sendAction(BillingListAction.ShowMessage(message))
        } else {
            blockingError.value = message
        }
    }
}
