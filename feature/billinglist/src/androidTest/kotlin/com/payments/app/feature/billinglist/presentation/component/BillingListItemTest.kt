package com.payments.app.feature.billinglist.presentation.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.payments.app.core.designsystem.theme.PaymentsTheme
import com.payments.app.core.model.BillingEntryHeader
import com.payments.app.core.ui.preview.PreviewBillingData
import com.payments.app.feature.billinglist.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.payments.app.core.ui.R as CoreUiR

@RunWith(AndroidJUnit4::class)
class BillingListItemTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var clicks = 0
    private var uploadClicks = 0

    private fun setItem(header: BillingEntryHeader) {
        composeRule.setContent {
            PaymentsTheme {
                BillingListItem(header = header, onClick = { clicks++ }, onUploadClick = { uploadClicks++ })
            }
        }
    }

    @Test
    fun showsPriceWithoutSymbolBillingNumberSourceAndArrow() {
        setItem(PreviewBillingData.terminalIls)

        composeRule.onNodeWithText("14.40").assertIsDisplayed()
        composeRule.onNodeWithText("3/6 · ", substring = true).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(context.getString(CoreUiR.string.source_terminal)).assertIsDisplayed()
        composeRule.onNodeWithContentDescription("ILS").assertIsDisplayed()
        composeRule.onNodeWithContentDescription(context.getString(R.string.open_details)).assertIsDisplayed()
    }

    @Test
    fun masterCardHasAnUploadButton() {
        setItem(PreviewBillingData.manualMasterCard)

        composeRule.onNodeWithContentDescription(context.getString(R.string.upload)).performClick()

        assertThat(uploadClicks).isEqualTo(1)
        assertThat(clicks).isEqualTo(0)
    }

    @Test
    fun otherCardsHaveNoUploadButton() {
        setItem(PreviewBillingData.terminalIls)

        composeRule.onNodeWithContentDescription(context.getString(R.string.upload)).assertDoesNotExist()
    }

    @Test
    fun clickingTheRowOpensDetails() {
        setItem(PreviewBillingData.posUsd)

        composeRule.onNodeWithText("25,357.53").performClick()

        assertThat(clicks).isEqualTo(1)
    }
}
