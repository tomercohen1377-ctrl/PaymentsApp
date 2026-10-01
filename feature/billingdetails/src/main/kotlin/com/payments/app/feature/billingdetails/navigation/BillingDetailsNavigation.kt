package com.payments.app.feature.billingdetails.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.payments.app.feature.billingdetails.presentation.BillingDetailsRoute
import kotlinx.serialization.Serializable

/** The details destination; [billingId] is the argument, passed to the ViewModel's assisted factory. */
@Serializable
data class BillingDetailsKey(val billingId: Long) : NavKey

fun EntryProviderScope<NavKey>.billingDetailsEntry(
    onBack: () -> Unit,
) {
    entry<BillingDetailsKey> { key ->
        BillingDetailsRoute(billingId = key.billingId, onBack = onBack)
    }
}
