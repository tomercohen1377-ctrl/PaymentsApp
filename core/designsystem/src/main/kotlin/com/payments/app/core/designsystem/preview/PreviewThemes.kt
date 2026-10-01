package com.payments.app.core.designsystem.preview

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.payments.app.core.designsystem.theme.PaymentsTheme

/**
 * Light, dark and Hebrew (RTL) previews in one annotation. Use it on every component and screen
 * preview, together with [PaymentsPreview].
 */
@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Hebrew RTL", showBackground = true, locale = "iw")
annotation class PreviewThemes

/** Wraps preview content in the app theme and a surface, so previews match the running app. */
@Composable
fun PaymentsPreview(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    PaymentsTheme {
        Surface(modifier = modifier, content = content)
    }
}
