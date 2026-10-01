package com.payments.app.core.ui.format

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BillingFormattingTest {

    @Test
    fun `billing number is entry over total`() {
        assertThat(formatBillingNumber(entryNumber = 3, totalEntryCount = 6)).isEqualTo("3/6")
    }

    @Test
    fun `card number shows only the last four digits`() {
        assertThat(maskCardNumber("7359327724873")).isEqualTo("****4873")
        assertThat(maskCardNumber("864498249010")).isEqualTo("****9010")
    }

    @Test
    fun `short card numbers are left as they are`() {
        assertThat(maskCardNumber("2345")).isEqualTo("2345")
        assertThat(maskCardNumber("")).isEqualTo("")
    }

    @Test
    fun `non-digit characters are ignored when masking`() {
        assertThat(maskCardNumber("4580 1234 5678 2345")).isEqualTo("****2345")
    }
}
