package com.payments.app.core.ui.billing

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.payments.app.core.designsystem.icon.PaymentsIcons
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import java.util.Currency

/** The currency icon: the asset for ILS and USD, the currency's symbol as text for anything else. */
@Composable
fun CurrencyIcon(
    currency: Currency,
    modifier: Modifier = Modifier,
) {
    val icon = when (currency.currencyCode) {
        "ILS" -> PaymentsIcons.Shekel
        "USD" -> PaymentsIcons.Dollar
        else -> null
    }
    if (icon != null) {
        Icon(
            painter = painterResource(icon),
            contentDescription = currency.currencyCode,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier.size(14.dp),
        )
    } else {
        Text(
            text = currency.symbol,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier,
        )
    }
}

@PreviewThemes
@Composable
private fun CurrencyIconPreview() {
    PaymentsPreview {
        Row {
            listOf("ILS", "USD", "EUR").forEach { CurrencyIcon(Currency.getInstance(it)) }
        }
    }
}
