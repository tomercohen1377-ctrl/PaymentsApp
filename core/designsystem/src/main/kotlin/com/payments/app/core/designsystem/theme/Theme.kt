package com.payments.app.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PaymentsColors.Green,
    onPrimary = Color.White,
    secondary = PaymentsColors.Lime,
    onSecondary = Color.White,
    error = PaymentsColors.Red,
    // White surfaces as in the mockups (Material's defaults are tinted).
    background = Color.White,
    surface = Color.White,
    surfaceContainer = Color.White,
    onSurfaceVariant = PaymentsColors.Gray,
    outlineVariant = PaymentsColors.GrayLight,
)

private val DarkColors = darkColorScheme(
    primary = PaymentsColors.Green,
    onPrimary = Color.White,
    secondary = PaymentsColors.Lime,
    onSecondary = Color.White,
    error = PaymentsColors.Red,
    outlineVariant = PaymentsColors.Gray,
)

/** App theme built from the spec colors. No dynamic color: the brand colors carry meaning. */
@Composable
fun PaymentsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
