package com.payments.app.core.ui.billing

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.payments.app.core.designsystem.icon.PaymentsIcons
import com.payments.app.core.designsystem.theme.PaymentsColors
import com.payments.app.core.model.BillingSource
import com.payments.app.core.ui.R

/** How a [BillingSource] is shown everywhere: icon, background color (from the spec) and name. */
data class BillingSourceStyle(
    @DrawableRes val icon: Int,
    val color: Color,
    @StringRes val label: Int,
)

val BillingSource.style: BillingSourceStyle
    get() = when (this) {
        BillingSource.TERMINAL -> BillingSourceStyle(
            PaymentsIcons.Phone,
            PaymentsColors.SourceTerminal,
            R.string.source_terminal,
        )

        BillingSource.POS -> BillingSourceStyle(PaymentsIcons.Card, PaymentsColors.SourcePos, R.string.source_pos)

        BillingSource.MANUAL -> BillingSourceStyle(
            PaymentsIcons.Person,
            PaymentsColors.SourceManual,
            R.string.source_manual,
        )

        BillingSource.UNKNOWN -> BillingSourceStyle(PaymentsIcons.Card, PaymentsColors.Gray, R.string.source_unknown)
    }
