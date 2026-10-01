package com.payments.app.feature.billingdetails.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.payments.app.core.designsystem.theme.PaymentsTheme
import com.payments.app.core.ui.preview.PreviewBillingData
import com.payments.app.feature.billingdetails.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.payments.app.core.ui.R as CoreUiR

@RunWith(AndroidJUnit4::class)
class BillingDetailsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val events = mutableListOf<BillingDetailsEvent>()

    private fun setScreen(state: BillingDetailsUiState) {
        composeRule.setContent {
            PaymentsTheme {
                BillingDetailsScreen(state = state, snackbarHostState = SnackbarHostState(), onEvent = { events += it })
            }
        }
    }

    @Test
    fun showsEveryRequiredField() {
        setScreen(BillingDetailsUiState.Success(PreviewBillingData.passedDetails))

        listOf(
            R.string.details_customer_name, R.string.details_status, R.string.details_payment_type,
            R.string.details_card_number, R.string.details_card_type, R.string.details_issuer,
            R.string.details_source, R.string.details_terminal_name, R.string.details_amount_paid,
            R.string.details_remaining, R.string.details_approval_number, R.string.details_voucher_number,
        ).forEach { label ->
            composeRule.onNodeWithText(context.getString(label), substring = true).performScrollTo().assertIsDisplayed()
        }
        composeRule.onNodeWithText("****2345", substring = true).assertExists()
        composeRule.onNodeWithText("34576934").assertExists()
        composeRule.onNodeWithText("3/6 · ", substring = true).assertIsDisplayed()
    }

    @Test
    fun deleteButtonSendsDeleteClicked() {
        setScreen(BillingDetailsUiState.Success(PreviewBillingData.passedDetails))

        composeRule.onNodeWithContentDescription(context.getString(CoreUiR.string.action_delete)).performClick()

        assertThat(events).containsExactly(BillingDetailsEvent.DeleteClicked)
    }

    @Test
    fun confirmationDialogConfirmsAndDismisses() {
        setScreen(BillingDetailsUiState.Success(PreviewBillingData.passedDetails, showDeleteConfirmation = true))

        composeRule.onNodeWithText(context.getString(R.string.delete_confirm_title)).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(CoreUiR.string.action_cancel)).performClick()

        assertThat(events).containsExactly(BillingDetailsEvent.DeleteDismissed)
    }

    @Test
    fun errorShowsMessageAndRetryButNoDeleteButton() {
        setScreen(BillingDetailsUiState.Error(R.string.details_not_found))

        composeRule.onNodeWithText(context.getString(R.string.details_not_found)).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(context.getString(CoreUiR.string.action_delete)).assertDoesNotExist()
        composeRule.onNodeWithText(context.getString(CoreUiR.string.action_retry)).performClick()

        assertThat(events).containsExactly(BillingDetailsEvent.Retry)
    }
}
