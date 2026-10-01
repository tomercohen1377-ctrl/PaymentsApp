package com.payments.app.feature.billingdetails.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.payments.app.core.ui.message.showMessage
import com.payments.app.core.ui.mvi.CollectAction

/**
 * Stateful entry point: creates the ViewModel for [billingId], collects state and actions, and renders
 * the stateless [BillingDetailsScreen]. No preview here (it needs a Hilt ViewModel); the screen's
 * previews cover every state.
 */
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
