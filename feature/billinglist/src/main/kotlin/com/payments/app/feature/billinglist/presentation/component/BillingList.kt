package com.payments.app.feature.billinglist.presentation.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.payments.app.core.designsystem.component.MessageState
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.ui.preview.PreviewBillingData
import com.payments.app.feature.billinglist.R
import com.payments.app.feature.billinglist.presentation.BillingListEvent
import com.payments.app.feature.billinglist.presentation.BillingListUiState
import kotlinx.coroutines.delay

/** Lets the screen (and the back navigation from details) settle, so the user sees the row leave. */
private const val REMOVAL_DELAY_MILLIS = 600L
private const val REMOVAL_ANIMATION_MILLIS = 400

/**
 * The loaded list. Entries in [BillingListUiState.Success.pendingRemovalIds] (just deleted) are shown
 * briefly, then dropped: `animateItem()` fades them out and slides the rows below into place.
 * Always a LazyColumn, even when empty, so pull-to-refresh keeps working.
 */
@Composable
internal fun BillingList(
    state: BillingListUiState.Success,
    onEvent: (BillingListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnEvent by rememberUpdatedState(onEvent)
    LaunchedEffect(state.pendingRemovalIds) {
        if (state.pendingRemovalIds.isNotEmpty()) {
            delay(REMOVAL_DELAY_MILLIS)
            currentOnEvent(BillingListEvent.RemovalsShown)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (state.items.isEmpty()) {
            item { MessageState(message = stringResource(R.string.billing_list_empty)) }
        }
        items(items = state.items, key = { it.id }) { header ->
            BillingListItem(
                header = header,
                onClick = { onEvent(BillingListEvent.ItemClicked(header.id)) },
                onUploadClick = { onEvent(BillingListEvent.UploadClicked(header.id)) },
                modifier = Modifier.animateItem(fadeOutSpec = tween(REMOVAL_ANIMATION_MILLIS)),
            )
        }
    }
}

@PreviewThemes
@Composable
private fun BillingListPreview() {
    PaymentsPreview {
        BillingList(BillingListUiState.Success(PreviewBillingData.headers), onEvent = {})
    }
}

@PreviewThemes
@Composable
private fun BillingListEmptyPreview() {
    PaymentsPreview {
        BillingList(BillingListUiState.Success(emptyList()), onEvent = {})
    }
}
