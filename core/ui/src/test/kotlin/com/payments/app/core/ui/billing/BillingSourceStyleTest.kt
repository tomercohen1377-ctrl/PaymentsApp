package com.payments.app.core.ui.billing

import com.google.common.truth.Truth.assertThat
import com.payments.app.core.designsystem.icon.PaymentsIcons
import com.payments.app.core.designsystem.theme.PaymentsColors
import com.payments.app.core.model.BillingSource
import com.payments.app.core.ui.R
import org.junit.Test

class BillingSourceStyleTest {

    @Test
    fun `terminal is the phone icon on spec green`() {
        assertThat(BillingSource.TERMINAL.style)
            .isEqualTo(BillingSourceStyle(PaymentsIcons.Phone, PaymentsColors.Green, R.string.source_terminal))
    }

    @Test
    fun `pos is the card icon on spec 80B918`() {
        assertThat(BillingSource.POS.style)
            .isEqualTo(BillingSourceStyle(PaymentsIcons.Card, PaymentsColors.Lime, R.string.source_pos))
    }

    @Test
    fun `manual is the person icon on spec 4F5860`() {
        assertThat(BillingSource.MANUAL.style)
            .isEqualTo(BillingSourceStyle(PaymentsIcons.Person, PaymentsColors.Gray, R.string.source_manual))
    }

    @Test
    fun `every source has a distinct label`() {
        assertThat(BillingSource.entries.map { it.style.label }.toSet()).hasSize(BillingSource.entries.size)
    }
}
