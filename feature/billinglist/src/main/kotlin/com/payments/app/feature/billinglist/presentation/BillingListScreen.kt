package com.payments.app.feature.billinglist.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.payments.app.core.designsystem.component.LoadingState
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.ui.component.RetryMessageState
import com.payments.app.core.ui.preview.PreviewBillingData
import com.payments.app.feature.billinglist.presentation.component.BillingList
import com.payments.app.core.ui.R as CoreUiR

/** The billing list screen for a given [state]; sends user intents through [onEvent]. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingListScreen(
    state: BillingListUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (BillingListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = state is BillingListUiState.Success && state.isRefreshing,
            onRefresh = { onEvent(BillingListEvent.Refresh) },
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            when (state) {
                BillingListUiState.Loading -> LoadingState()

                is BillingListUiState.Error -> RetryMessageState(state.message, onRetry = {
                    onEvent(BillingListEvent.Refresh)
                })

                is BillingListUiState.Success -> BillingList(state, onEvent)
            }
        }
    }
}

@PreviewThemes
@Composable
private fun BillingListScreenSuccessPreview() {
    PaymentsPreview {
        BillingListScreen(
            state = BillingListUiState.Success(PreviewBillingData.headers),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
        )
    }
}

@PreviewThemes
@Composable
private fun BillingListScreenLoadingPreview() {
    PaymentsPreview {
        BillingListScreen(BillingListUiState.Loading, remember { SnackbarHostState() }, onEvent = {})
    }
}

@PreviewThemes
@Composable
private fun BillingListScreenErrorPreview() {
    PaymentsPreview {
        BillingListScreen(
            state = BillingListUiState.Error(CoreUiR.string.error_network),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
        )
    }
}
