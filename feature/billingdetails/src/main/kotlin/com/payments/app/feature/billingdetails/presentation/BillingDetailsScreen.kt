package com.payments.app.feature.billingdetails.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.payments.app.core.designsystem.component.ConfirmDialog
import com.payments.app.core.designsystem.component.DeleteButton
import com.payments.app.core.designsystem.component.LoadingState
import com.payments.app.core.designsystem.component.MessageState
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.model.BillingEntryDetails
import com.payments.app.core.ui.message.showMessage
import com.payments.app.core.ui.mvi.CollectAction
import com.payments.app.core.ui.preview.PreviewBillingData
import com.payments.app.feature.billingdetails.R
import com.payments.app.feature.billingdetails.presentation.component.BillingDetailsFields
import com.payments.app.feature.billingdetails.presentation.component.BillingDetailsHeader
import com.payments.app.core.ui.R as CoreUiR

/** Stateful entry point: creates the ViewModel for [billingId], collects state and actions. */
@Composable
fun BillingDetailsRoute(
    billingId: Long,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BillingDetailsViewModel = hiltViewModel<BillingDetailsViewModel, BillingDetailsViewModel.Factory>(
        creationCallback = { factory -> factory.create(billingId) },
    ),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val resources = LocalResources.current

    CollectAction(viewModel.action) { action ->
        when (action) {
            BillingDetailsAction.NavigateBack -> onBack()
            is BillingDetailsAction.ShowMessage -> snackbarHostState.showMessage(resources, action.message)
        }
    }

    BillingDetailsScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

@Composable
fun BillingDetailsScreen(
    state: BillingDetailsUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (BillingDetailsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = { BillingDetailsDeleteButton(state, onEvent) },
        floatingActionButtonPosition = FabPosition.Center,
    ) { padding ->
        val contentModifier = Modifier.fillMaxSize().padding(padding)
        when (state) {
            BillingDetailsUiState.Loading -> LoadingState(contentModifier)
            is BillingDetailsUiState.Error -> BillingDetailsError(state, onEvent, contentModifier)
            is BillingDetailsUiState.Success -> BillingDetailsSuccess(state, onEvent, contentModifier)
        }
    }
}

/** Shown only with the details on screen; spins while the delete is running. */
@Composable
private fun BillingDetailsDeleteButton(
    state: BillingDetailsUiState,
    onEvent: (BillingDetailsEvent) -> Unit,
) {
    if (state is BillingDetailsUiState.Success) {
        DeleteButton(
            contentDescription = stringResource(CoreUiR.string.action_delete),
            onClick = { onEvent(BillingDetailsEvent.DeleteClicked) },
            isLoading = state.isDeleting,
        )
    }
}

@Composable
private fun BillingDetailsError(
    state: BillingDetailsUiState.Error,
    onEvent: (BillingDetailsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    MessageState(
        message = stringResource(state.message),
        modifier = modifier,
        actionLabel = stringResource(CoreUiR.string.action_retry),
        onAction = { onEvent(BillingDetailsEvent.Retry) },
    )
}

@Composable
private fun BillingDetailsSuccess(
    state: BillingDetailsUiState.Success,
    onEvent: (BillingDetailsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    BillingDetailsContent(state.details, modifier)
    if (state.showDeleteConfirmation) {
        DeleteConfirmationDialog(onEvent)
    }
}

@Composable
private fun DeleteConfirmationDialog(onEvent: (BillingDetailsEvent) -> Unit) {
    ConfirmDialog(
        title = stringResource(R.string.delete_confirm_title),
        text = stringResource(R.string.delete_confirm_text),
        confirmLabel = stringResource(CoreUiR.string.action_delete),
        dismissLabel = stringResource(CoreUiR.string.action_cancel),
        onConfirm = { onEvent(BillingDetailsEvent.DeleteConfirmed) },
        onDismiss = { onEvent(BillingDetailsEvent.DeleteDismissed) },
    )
}

@Composable
private fun BillingDetailsContent(
    details: BillingEntryDetails,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState()).padding(24.dp)) {
        BillingDetailsHeader(details)
        BillingDetailsFields(details, Modifier.padding(top = 16.dp))
        // Room for the delete button, so it never covers the last row.
        Spacer(Modifier.height(88.dp))
    }
}

@PreviewThemes
@Composable
private fun BillingDetailsScreenPassedPreview() {
    PaymentsPreview {
        BillingDetailsScreen(
            state = BillingDetailsUiState.Success(PreviewBillingData.passedDetails),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
        )
    }
}

@PreviewThemes
@Composable
private fun BillingDetailsScreenRejectedDeletingPreview() {
    PaymentsPreview {
        BillingDetailsScreen(
            state = BillingDetailsUiState.Success(PreviewBillingData.rejectedDetails, isDeleting = true),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
        )
    }
}

@PreviewThemes
@Composable
private fun BillingDetailsScreenConfirmDeletePreview() {
    PaymentsPreview {
        BillingDetailsScreen(
            state = BillingDetailsUiState.Success(PreviewBillingData.passedDetails, showDeleteConfirmation = true),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
        )
    }
}

@PreviewThemes
@Composable
private fun BillingDetailsScreenLoadingPreview() {
    PaymentsPreview {
        BillingDetailsScreen(BillingDetailsUiState.Loading, remember { SnackbarHostState() }, onEvent = {})
    }
}

@PreviewThemes
@Composable
private fun BillingDetailsScreenErrorPreview() {
    PaymentsPreview {
        BillingDetailsScreen(
            state = BillingDetailsUiState.Error(CoreUiR.string.error_network),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
        )
    }
}

@PreviewThemes
@Composable
private fun BillingDetailsScreenNotFoundPreview() {
    PaymentsPreview {
        BillingDetailsScreen(
            state = BillingDetailsUiState.Error(R.string.details_not_found),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
        )
    }
}
