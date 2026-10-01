package com.payments.app.core.ui.component

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.payments.app.core.designsystem.component.MessageState
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.ui.R

/** A screen's `Error` state: the message with a Retry button. */
@Composable
fun RetryMessageState(
    @StringRes message: Int,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MessageState(
        message = stringResource(message),
        modifier = modifier,
        actionLabel = stringResource(R.string.action_retry),
        onAction = onRetry,
    )
}

@PreviewThemes
@Composable
private fun RetryMessageStatePreview() {
    PaymentsPreview {
        RetryMessageState(message = R.string.error_network, onRetry = {})
    }
}
