package com.payments.app.core.model

/** Outcome of a billing entry. API values: `Passed`, `Rejected`. */
enum class BillingStatus {
    PASSED,
    REJECTED,
    UNKNOWN,
    ;

    companion object {
        fun fromApi(value: String?): BillingStatus = parseApiEnum(value, UNKNOWN)
    }
}
