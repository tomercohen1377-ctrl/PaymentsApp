package com.payments.app.core.model

/** Where a billing entry was created. API values: `Terminal`, `Pos`, `Manual`. */
enum class BillingSource {
    TERMINAL,
    POS,
    MANUAL,
    UNKNOWN,
    ;

    companion object {
        fun fromApi(value: String?): BillingSource = parseApiEnum(value, UNKNOWN)
    }
}
