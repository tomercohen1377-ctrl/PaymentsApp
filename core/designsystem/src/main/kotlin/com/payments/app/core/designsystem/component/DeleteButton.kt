package com.payments.app.core.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.payments.app.core.designsystem.icon.PaymentsIcons
import com.payments.app.core.designsystem.preview.PaymentsPreview
import com.payments.app.core.designsystem.preview.PreviewThemes
import com.payments.app.core.designsystem.theme.PaymentsColors

/** The round delete button of the details screen. Shows progress (and ignores taps) while [isLoading]. */
@Composable
fun DeleteButton(
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    FloatingActionButton(
        onClick = { if (!isLoading) onClick() },
        modifier = modifier,
        shape = CircleShape,
        containerColor = PaymentsColors.Pink,
        contentColor = Color.White,
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
        } else {
            Icon(imageVector = PaymentsIcons.Delete, contentDescription = contentDescription)
        }
    }
}

@PreviewThemes
@Composable
private fun DeleteButtonPreview() {
    PaymentsPreview { DeleteButton(contentDescription = "Delete", onClick = {}) }
}

@PreviewThemes
@Composable
private fun DeleteButtonLoadingPreview() {
    PaymentsPreview { DeleteButton(contentDescription = "Delete", onClick = {}, isLoading = true) }
}
