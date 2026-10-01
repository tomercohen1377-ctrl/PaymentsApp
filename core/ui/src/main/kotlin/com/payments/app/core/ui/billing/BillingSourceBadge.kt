package com.payments.app.core.ui.billing

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.payments.app.core.designsystem.component.IconBadge
import com.payments.app.core.designsystem.component.IconBadgeDefaults
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.model.BillingSource

/** The billing source indicator: the source's icon on its spec color. Sized by the caller. */
@Composable
fun BillingSourceBadge(
    source: BillingSource,
    modifier: Modifier = Modifier,
    iconSize: Dp = 20.dp,
    shape: Shape = IconBadgeDefaults.Shape,
) {
    val style = source.style
    IconBadge(
        icon = style.icon,
        containerColor = style.color,
        contentDescription = stringResource(style.label),
        modifier = modifier,
        iconSize = iconSize,
        shape = shape,
    )
}

@PreviewThemes
@Composable
private fun BillingSourceBadgePreview() {
    PaymentsPreview {
        Row {
            BillingSource.entries.forEach { source ->
                BillingSourceBadge(source = source, modifier = Modifier.size(40.dp))
            }
        }
    }
}
