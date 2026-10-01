package com.payments.app.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.payments.app.core.designsystem.icon.PaymentsIcons
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.designsystem.theme.PaymentsColors

/**
 * A white icon on a colored block, e.g. the billing source indicator. The caller sizes it through
 * [modifier] (a fixed square on the details screen, full row height in the list).
 */
@Composable
fun IconBadge(
    @DrawableRes icon: Int,
    containerColor: Color,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    iconSize: Dp = 20.dp,
    shape: Shape = IconBadgeDefaults.Shape,
) {
    Box(
        modifier = modifier.clip(shape).background(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(iconSize),
        )
    }
}

object IconBadgeDefaults {
    val Shape: Shape = RoundedCornerShape(8.dp)
}

@PreviewThemes
@Composable
private fun IconBadgePreview() {
    PaymentsPreview {
        Row {
            IconBadge(PaymentsIcons.Phone, PaymentsColors.SourceTerminal, null, Modifier.size(40.dp))
            IconBadge(PaymentsIcons.Card, PaymentsColors.SourcePos, null, Modifier.size(40.dp))
            IconBadge(PaymentsIcons.Person, PaymentsColors.SourceManual, null, Modifier.size(40.dp))
            IconBadge(PaymentsIcons.Phone, PaymentsColors.SourceTerminal, null, Modifier.size(96.dp), iconSize = 40.dp)
        }
    }
}
