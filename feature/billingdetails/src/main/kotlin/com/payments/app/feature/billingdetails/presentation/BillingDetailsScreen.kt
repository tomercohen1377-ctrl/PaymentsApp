package com.payments.app.feature.billingdetails.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.payments.app.core.designsystem.component.LoadingState
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.ui.component.RetryMessageState
import com.payments.app.core.ui.preview.PreviewBillingData
import com.payments.app.feature.billingdetails.R
import com.payments.app.feature.billingdetails.presentation.component.BillingDetailsDeleteButton
import com.payments.app.feature.billingdetails.presentation.component.BillingDetailsSuccess
import com.payments.app.core.ui.R as CoreUiR

/** The details screen for a given [state]; sends user intents through [onEvent]. */
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

            is BillingDetailsUiState.Error -> RetryMessageState(
                message = state.message,
                onRetry = { onEvent(BillingDetailsEvent.Retry) },
                modifier = contentModifier,
            )

            is BillingDetailsUiState.Success -> BillingDetailsSuccess(state, onEvent, contentModifier)
        }
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
