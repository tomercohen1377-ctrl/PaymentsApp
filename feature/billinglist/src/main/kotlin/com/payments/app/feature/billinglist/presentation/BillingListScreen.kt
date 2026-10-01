package com.payments.app.feature.billinglist.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.payments.app.core.designsystem.component.LoadingState
import com.payments.app.core.designsystem.component.MessageState
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.model.BillingEntryHeader
import com.payments.app.core.ui.message.showMessage
import com.payments.app.core.ui.mvi.CollectAction
import com.payments.app.core.ui.preview.PreviewBillingData
import com.payments.app.feature.billinglist.R
import com.payments.app.feature.billinglist.presentation.component.BillingListItem
import com.payments.app.core.ui.R as CoreUiR

/** Stateful entry point: collects state and actions, and renders [BillingListScreen]. */
@Composable
fun BillingListRoute(
    onNavigateToDetails: (billingId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BillingListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val resources = LocalResources.current

    CollectAction(viewModel.action) { action ->
        when (action) {
            is BillingListAction.NavigateToDetails -> onNavigateToDetails(action.billingId)
            is BillingListAction.ShowMessage -> snackbarHostState.showMessage(resources, action.message)
        }
    }

    BillingListScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

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
                is BillingListUiState.Error -> BillingListError(state, onEvent)
                is BillingListUiState.Success -> BillingList(state.items, onEvent)
            }
        }
    }
}

@Composable
private fun BillingListError(
    state: BillingListUiState.Error,
    onEvent: (BillingListEvent) -> Unit,
) {
    MessageState(
        message = stringResource(state.message),
        actionLabel = stringResource(CoreUiR.string.action_retry),
        onAction = { onEvent(BillingListEvent.Refresh) },
    )
}

@Composable
private fun BillingList(
    items: List<BillingEntryHeader>,
    onEvent: (BillingListEvent) -> Unit,
) {
    // Empty or not, the content is a LazyColumn so pull-to-refresh always works.
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (items.isEmpty()) {
            item { MessageState(message = stringResource(R.string.billing_list_empty)) }
        }
        items(items = items, key = { it.id }) { header ->
            BillingListItem(
                header = header,
                onClick = { onEvent(BillingListEvent.ItemClicked(header.id)) },
                onUploadClick = { onEvent(BillingListEvent.UploadClicked(header.id)) },
            )
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

@PreviewThemes
@Composable
private fun BillingListScreenEmptyPreview() {
    PaymentsPreview {
        BillingListScreen(BillingListUiState.Success(emptyList()), remember { SnackbarHostState() }, onEvent = {})
    }
}
