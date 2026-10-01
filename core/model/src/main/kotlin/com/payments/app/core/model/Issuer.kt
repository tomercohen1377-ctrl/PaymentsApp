package com.payments.app.core.model

/** Card issuer. API values: `Isracard`, `Visacal`, `Diners`, `Amex`, `Jcb`, `Max`. */
enum class Issuer {
    ISRACARD,
    VISACAL,
    DINERS,
    AMEX,
    JCB,
    MAX,
    UNKNOWN,
    ;

    companion object {
        fun fromApi(value: String?): Issuer = parseApiEnum(value, UNKNOWN)
    }
}
