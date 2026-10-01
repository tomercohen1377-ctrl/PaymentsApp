package com.payments.app.core.model

/**
 * Card brand. API values: `Visa`, `MasterCard`, `Diners`, `Amex`, `Isracard`, `Discover`,
 * `Meastro` (the server's and the spec's spelling of Maestro), `Max`.
 */
enum class CardType {
    VISA,
    MASTERCARD,
    DINERS,
    AMEX,
    ISRACARD,
    DISCOVER,
    MAESTRO,
    MAX,
    UNKNOWN,
    ;

    companion object {
        private val aliases = mapOf("MEASTRO" to MAESTRO)

        fun fromApi(value: String?): CardType = parseApiEnum(value, UNKNOWN, aliases)
    }
}
