package com.payments.app.feature.billinglist.presentation

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
 * Stateful entry point: gets the ViewModel from Hilt, collects state and actions, and renders the
 * stateless [BillingListScreen]. No preview here (it needs a Hilt ViewModel); the screen's previews
 * cover every state.
 */
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
