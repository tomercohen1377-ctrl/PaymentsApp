package com.payments.app.feature.billinglist.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.payments.app.core.designsystem.component.CircleIconButton
import com.payments.app.core.designsystem.icon.PaymentsIcons
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.model.BillingEntryHeader
import com.payments.app.core.model.CardType
import com.payments.app.core.ui.billing.BillingSourceBadge
import com.payments.app.core.ui.billing.CurrencyIcon
import com.payments.app.core.ui.format.formatAmount
import com.payments.app.core.ui.format.formatBillingNumber
import com.payments.app.core.ui.format.formatDateTime
import com.payments.app.core.ui.format.isolateLtr
import com.payments.app.core.ui.preview.PreviewBillingData
import com.payments.app.feature.billinglist.R

private val ItemShape = RoundedCornerShape(8.dp)

/**
 * A billing list row, covering the task's list requirements:
 * (1) source icon on its color, (2) price without currency symbol, (3) date `dd-MM-yyyy HH:mm`,
 * (4) billing number, (5) upload button for MasterCard, (6) currency icon, (7) arrow.
 *
 * Laid out with start/end, so in Hebrew it mirrors to the mockup: badge on the right, arrow on the left.
 */
@Composable
fun BillingListItem(
    header: BillingEntryHeader,
    onClick: () -> Unit,
    onUploadClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = ItemShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BillingSourceBadge(
                source = header.source,
                modifier = Modifier.width(44.dp).fillMaxHeight(),
                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp),
            )
            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = formatAmount(header.price),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (header.cardType == CardType.MASTERCARD) {
                        CircleIconButton(
                            icon = PaymentsIcons.Upload,
                            contentDescription = stringResource(R.string.upload),
                            onClick = onUploadClick,
                            modifier = Modifier.size(24.dp),
                        )
                        Spacer(Modifier.width(4.dp))
                    }
                    Text(
                        text = stringResource(
                            R.string.billing_number_and_date,
                            formatBillingNumber(header.entryNumber, header.totalEntryCount),
                            // Kept left-to-right so the date and time don't swap in Hebrew.
                            formatDateTime(header.created).isolateLtr(),
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            CurrencyIcon(currency = header.price.currency)
            Icon(
                imageVector = PaymentsIcons.ArrowForward,
                contentDescription = stringResource(R.string.open_details),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@PreviewThemes
@Composable
private fun BillingListItemPreview() {
    PaymentsPreview {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PreviewBillingData.headers.forEach { header ->
                BillingListItem(header = header, onClick = {}, onUploadClick = {})
            }
        }
    }
}
