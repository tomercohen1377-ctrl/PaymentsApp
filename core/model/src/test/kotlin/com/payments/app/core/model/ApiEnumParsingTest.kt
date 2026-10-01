package com.payments.app.core.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/** Every value below appears in server/list.json or server/details.json. */
class ApiEnumParsingTest {

    @Test
    fun `billing source parses every API value`() {
        assertThat(BillingSource.fromApi("Terminal")).isEqualTo(BillingSource.TERMINAL)
        assertThat(BillingSource.fromApi("Pos")).isEqualTo(BillingSource.POS)
        assertThat(BillingSource.fromApi("Manual")).isEqualTo(BillingSource.MANUAL)
    }

    @Test
    fun `billing status parses every API value`() {
        assertThat(BillingStatus.fromApi("Passed")).isEqualTo(BillingStatus.PASSED)
        assertThat(BillingStatus.fromApi("Rejected")).isEqualTo(BillingStatus.REJECTED)
    }

    @Test
    fun `card type parses every API value, including the Meastro spelling`() {
        val parsed = listOf("Visa", "MasterCard", "Diners", "Amex", "Isracard", "Discover", "Meastro", "Max")
            .map(CardType::fromApi)

        assertThat(parsed).containsExactly(
            CardType.VISA,
            CardType.MASTERCARD,
            CardType.DINERS,
            CardType.AMEX,
            CardType.ISRACARD,
            CardType.DISCOVER,
            CardType.MAESTRO,
            CardType.MAX,
        ).inOrder()
    }

    @Test
    fun `the correct Maestro spelling also parses`() {
        assertThat(CardType.fromApi("Maestro")).isEqualTo(CardType.MAESTRO)
    }

    @Test
    fun `issuer parses every API value`() {
        val parsed = listOf("Isracard", "Visacal", "Diners", "Amex", "Jcb", "Max").map(Issuer::fromApi)

        assertThat(parsed).containsExactly(
            Issuer.ISRACARD,
            Issuer.VISACAL,
            Issuer.DINERS,
            Issuer.AMEX,
            Issuer.JCB,
            Issuer.MAX,
        ).inOrder()
    }

    @Test
    fun `matching ignores case and separators`() {
        assertThat(CardType.fromApi("MASTERCARD")).isEqualTo(CardType.MASTERCARD)
        assertThat(CardType.fromApi("master card")).isEqualTo(CardType.MASTERCARD)
        assertThat(CardType.fromApi("Master_Card")).isEqualTo(CardType.MASTERCARD)
        assertThat(BillingSource.fromApi("pos")).isEqualTo(BillingSource.POS)
    }

    @Test
    fun `unknown, empty and missing values fall back to UNKNOWN`() {
        assertThat(CardType.fromApi("UnionPay")).isEqualTo(CardType.UNKNOWN)
        assertThat(BillingSource.fromApi("")).isEqualTo(BillingSource.UNKNOWN)
        assertThat(BillingStatus.fromApi(null)).isEqualTo(BillingStatus.UNKNOWN)
        assertThat(Issuer.fromApi("  ")).isEqualTo(Issuer.UNKNOWN)
    }

    @Test
    fun `the UNKNOWN constant name is not matched from the API`() {
        // "Unknown" from the server should still be UNKNOWN, which is also the fallback.
        assertThat(BillingStatus.fromApi("Unknown")).isEqualTo(BillingStatus.UNKNOWN)
    }
}
