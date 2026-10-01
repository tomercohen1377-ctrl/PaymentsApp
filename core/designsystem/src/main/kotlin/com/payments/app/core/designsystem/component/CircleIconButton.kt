package com.payments.app.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.payments.app.core.designsystem.icon.PaymentsIcons
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.designsystem.theme.PaymentsColors

/**
 * A small filled circle with a white icon, e.g. the MasterCard upload button. The visible circle is
 * [size]; the touch target is still the 48dp accessibility minimum.
 */
@Composable
fun CircleIconButton(
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = PaymentsColors.Green,
    size: Dp = 18.dp,
    iconSize: Dp = 8.dp,
) {
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clickable(onClick = onClick, role = Role.Button, onClickLabel = contentDescription),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.size(size).clip(CircleShape).background(containerColor),
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
}

@PreviewThemes
@Composable
private fun CircleIconButtonPreview() {
    PaymentsPreview {
        CircleIconButton(icon = PaymentsIcons.Upload, contentDescription = "Upload", onClick = {})
    }
}
