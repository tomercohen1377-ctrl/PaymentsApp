package com.payments.app.feature.billingdetails.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.ui.preview.PreviewBillingData
import com.payments.app.feature.billingdetails.presentation.BillingDetailsEvent
import com.payments.app.feature.billingdetails.presentation.BillingDetailsUiState

/** The loaded details, plus the delete confirmation when it is open. */
@Composable
internal fun BillingDetailsSuccess(
    state: BillingDetailsUiState.Success,
    onEvent: (BillingDetailsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    BillingDetailsContent(state.details, modifier)
    if (state.showDeleteConfirmation) {
        DeleteConfirmationDialog(onEvent)
    }
}

@PreviewThemes
@Composable
private fun BillingDetailsSuccessConfirmDeletePreview() {
    PaymentsPreview {
        BillingDetailsSuccess(
            state = BillingDetailsUiState.Success(PreviewBillingData.passedDetails, showDeleteConfirmation = true),
            onEvent = {},
        )
    }
}
