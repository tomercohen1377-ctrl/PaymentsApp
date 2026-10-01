package com.payments.app.feature.billingdetails.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.payments.app.core.designsystem.component.ConfirmDialog
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.feature.billingdetails.R
import com.payments.app.feature.billingdetails.presentation.BillingDetailsEvent
import com.payments.app.core.ui.R as CoreUiR

/** Asks before deleting, since a delete can't be undone. */
@Composable
internal fun DeleteConfirmationDialog(
    onEvent: (BillingDetailsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConfirmDialog(
        title = stringResource(R.string.delete_confirm_title),
        text = stringResource(R.string.delete_confirm_text),
        confirmLabel = stringResource(CoreUiR.string.action_delete),
        dismissLabel = stringResource(CoreUiR.string.action_cancel),
        onConfirm = { onEvent(BillingDetailsEvent.DeleteConfirmed) },
        onDismiss = { onEvent(BillingDetailsEvent.DeleteDismissed) },
        modifier = modifier,
    )
}

@PreviewThemes
@Composable
private fun DeleteConfirmationDialogPreview() {
    PaymentsPreview {
        DeleteConfirmationDialog(onEvent = {})
    }
}
