package com.payments.app.feature.billingdetails.presentation

import androidx.annotation.StringRes
import com.payments.app.core.model.BillingStatus
import com.payments.app.core.model.CardType
import com.payments.app.core.model.Issuer
import com.payments.app.feature.billingdetails.R

/** Display names for the details screen's enum values. */

@get:StringRes
internal val BillingStatus.label: Int
    get() = when (this) {
        BillingStatus.PASSED -> R.string.status_passed
        BillingStatus.REJECTED -> R.string.status_rejected
        BillingStatus.UNKNOWN -> R.string.status_unknown
    }

@get:StringRes
internal val CardType.label: Int
    get() = when (this) {
        CardType.VISA -> R.string.card_visa
        CardType.MASTERCARD -> R.string.card_mastercard
        CardType.DINERS -> R.string.card_diners
        CardType.AMEX -> R.string.card_amex
        CardType.ISRACARD -> R.string.card_isracard
        CardType.DISCOVER -> R.string.card_discover
        CardType.MAESTRO -> R.string.card_maestro
        CardType.MAX -> R.string.card_max
        CardType.UNKNOWN -> R.string.details_value_missing
    }

@get:StringRes
internal val Issuer.label: Int
    get() = when (this) {
        Issuer.ISRACARD -> R.string.card_isracard
        Issuer.VISACAL -> R.string.issuer_visacal
        Issuer.DINERS -> R.string.card_diners
        Issuer.AMEX -> R.string.card_amex
        Issuer.JCB -> R.string.issuer_jcb
        Issuer.MAX -> R.string.card_max
        Issuer.UNKNOWN -> R.string.details_value_missing
    }
