package com.payments.app.feature.billingdetails.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.designsystem.theme.PaymentsColors
import com.payments.app.core.model.BillingEntryDetails
import com.payments.app.core.model.BillingStatus
import com.payments.app.core.ui.billing.BillingSourceBadge
import com.payments.app.core.ui.format.formatBillingNumber
import com.payments.app.core.ui.format.formatDateTime
import com.payments.app.core.ui.format.formatMoney
import com.payments.app.core.ui.format.isolateLtr
import com.payments.app.core.ui.preview.PreviewBillingData
import com.payments.app.feature.billingdetails.R

/**
 * Top of the details screen: (1) source indicator, (2) price, green if Passed and red if Rejected,
 * (3) date time, (4) billing number, (5) customer name.
 */
@Composable
internal fun BillingDetailsHeader(
    details: BillingEntryDetails,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BillingSourceBadge(
                source = details.source,
                modifier = Modifier.size(88.dp),
                iconSize = 40.dp,
            )
            Spacer(Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = formatMoney(details.price),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = details.status.priceColor,
                )
                Text(
                    text = stringResource(
                        R.string.details_billing_number_and_date,
                        formatBillingNumber(details.entryNumber, details.totalEntryCount),
                        formatDateTime(details.created).isolateLtr(),
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        HorizontalDivider(Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
        Text(
            text = stringResource(R.string.details_customer_name) + " " +
                (details.customerName ?: stringResource(R.string.details_value_missing)),
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

/** Green if Passed, red if Rejected; otherwise the text's default color. */
private val BillingStatus.priceColor: Color
    get() = when (this) {
        BillingStatus.PASSED -> PaymentsColors.Green
        BillingStatus.REJECTED -> PaymentsColors.Red
        BillingStatus.UNKNOWN -> Color.Unspecified
    }

@PreviewThemes
@Composable
private fun BillingDetailsHeaderPassedPreview() {
    PaymentsPreview {
        BillingDetailsHeader(PreviewBillingData.passedDetails, Modifier.padding(24.dp))
    }
}

@PreviewThemes
@Composable
private fun BillingDetailsHeaderRejectedPreview() {
    PaymentsPreview {
        BillingDetailsHeader(
            PreviewBillingData.rejectedDetails.copy(customerName = "Ricky Simon - Egg seller"),
            Modifier.padding(24.dp),
        )
    }
}
