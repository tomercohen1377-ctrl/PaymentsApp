package com.payments.app.feature.billingdetails.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.model.BillingEntryDetails
import com.payments.app.core.ui.preview.PreviewBillingData

/** The scrollable details: header and fields, with room at the bottom for the delete button. */
@Composable
internal fun BillingDetailsContent(
    details: BillingEntryDetails,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState()).padding(24.dp)) {
        BillingDetailsHeader(details)
        BillingDetailsFields(details, Modifier.padding(top = 16.dp))
        // So the delete button never covers the last row.
        Spacer(Modifier.height(88.dp))
    }
}

@PreviewThemes
@Composable
private fun BillingDetailsContentPreview() {
    PaymentsPreview {
        BillingDetailsContent(PreviewBillingData.rejectedDetails)
    }
}
