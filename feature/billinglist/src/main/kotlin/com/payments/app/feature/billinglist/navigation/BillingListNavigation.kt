package com.payments.app.feature.billinglist.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.payments.app.feature.billinglist.presentation.BillingListRoute
import kotlinx.serialization.Serializable

/** The billing list destination: the app's start screen. */
@Serializable
data object BillingListKey : NavKey

/** Registers the billing list. Where "details" leads is up to the app, which owns navigation. */
fun EntryProviderScope<NavKey>.billingListEntry(
    onNavigateToDetails: (billingId: Long) -> Unit,
) {
    entry<BillingListKey> {
        BillingListRoute(onNavigateToDetails = onNavigateToDetails)
    }
}
