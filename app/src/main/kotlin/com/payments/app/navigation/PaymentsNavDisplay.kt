package com.payments.app.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.payments.app.feature.billinglist.navigation.BillingListKey
import com.payments.app.feature.billinglist.navigation.billingListEntry

/**
 * The app's navigation (Navigation 3). The back stack is a list of `@Serializable` NavKeys that the
 * app owns, so it survives configuration changes and process death. Each entry gets its own saveable
 * state and ViewModelStore. Features don't know about each other; this is where they are connected.
 */
@Composable
fun PaymentsNavDisplay(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(BillingListKey)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            billingListEntry(
                // The details destination is added in stage 8.
                onNavigateToDetails = { billingId -> Log.d("PaymentsNav", "Details for $billingId") },
            )
        },
    )
}
