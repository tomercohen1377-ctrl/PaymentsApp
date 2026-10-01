package com.payments.app.feature.billingdetails.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.payments.app.core.designsystem.component.DeleteButton
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.ui.preview.PreviewBillingData
import com.payments.app.feature.billingdetails.presentation.BillingDetailsEvent
import com.payments.app.feature.billingdetails.presentation.BillingDetailsUiState
import com.payments.app.core.ui.R as CoreUiR

/** The delete button: shown only with the details on screen, spinning while the delete runs. */
@Composable
internal fun BillingDetailsDeleteButton(
    state: BillingDetailsUiState,
    onEvent: (BillingDetailsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state is BillingDetailsUiState.Success) {
        DeleteButton(
            contentDescription = stringResource(CoreUiR.string.action_delete),
            onClick = { onEvent(BillingDetailsEvent.DeleteClicked) },
            modifier = modifier,
            isLoading = state.isDeleting,
        )
    }
}

@PreviewThemes
@Composable
private fun BillingDetailsDeleteButtonPreview() {
    PaymentsPreview {
        BillingDetailsDeleteButton(BillingDetailsUiState.Success(PreviewBillingData.passedDetails), onEvent = {})
    }
}

@PreviewThemes
@Composable
private fun BillingDetailsDeleteButtonDeletingPreview() {
    PaymentsPreview {
        BillingDetailsDeleteButton(
            BillingDetailsUiState.Success(PreviewBillingData.passedDetails, isDeleting = true),
            onEvent = {},
        )
    }
}
