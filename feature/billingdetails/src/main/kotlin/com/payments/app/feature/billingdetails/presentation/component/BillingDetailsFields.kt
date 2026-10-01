package com.payments.app.feature.billingdetails.presentation.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.payments.app.core.designsystem.component.LabeledValueRow
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.model.BillingEntryDetails
import com.payments.app.core.ui.billing.style
import com.payments.app.core.ui.format.formatMoney
import com.payments.app.core.ui.format.isolateLtr
import com.payments.app.core.ui.format.maskCardNumber
import com.payments.app.core.ui.preview.PreviewBillingData
import com.payments.app.feature.billingdetails.R
import com.payments.app.feature.billingdetails.presentation.label

/**
 * The labeled rows of the details screen, grouped as in the mockup: (6) status, (7) payment type,
 * (8) card number, (9) card type, (10) issuer, (11) payment source, (12) terminal name; then
 * (13) total paid, (14) remaining, (15) approval number and the voucher number.
 */
@Composable
fun BillingDetailsFields(
    details: BillingEntryDetails,
    modifier: Modifier = Modifier,
) {
    val missing = stringResource(R.string.details_value_missing)
    val paymentInfo = listOf(
        R.string.details_status to stringResource(details.status.label),
        R.string.details_payment_type to (details.paymentType ?: missing),
        // Isolated so the mask stays in front of the digits in Hebrew ("****2345", not "2345****").
        R.string.details_card_number to maskCardNumber(details.cardNumber).isolateLtr(),
        R.string.details_card_type to stringResource(details.cardType.label),
        R.string.details_issuer to stringResource(details.issuer.label),
        R.string.details_source to stringResource(details.source.style.label),
        R.string.details_terminal_name to details.terminalName,
    )
    val amounts = listOf(
        R.string.details_amount_paid to formatMoney(details.amountPaid),
        R.string.details_remaining to formatMoney(details.remainingAmount),
        R.string.details_approval_number to details.approvalNumber,
        R.string.details_voucher_number to details.voucherNumber,
    )

    Column(modifier = modifier) {
        FieldRows(paymentInfo)
        HorizontalDivider(Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
        FieldRows(amounts)
    }
}

@Composable
private fun FieldRows(fields: List<Pair<Int, String>>) {
    fields.forEach { (@StringRes label, value) ->
        LabeledValueRow(label = stringResource(label), value = value)
    }
}

@PreviewThemes
@Composable
private fun BillingDetailsFieldsPreview() {
    PaymentsPreview {
        BillingDetailsFields(PreviewBillingData.passedDetails, Modifier.padding(24.dp))
    }
}
