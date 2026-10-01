package com.payments.app.core.ui.format

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BidiFormattingTest {

    @Test
    fun `wraps the text in a left-to-right isolate`() {
        assertThat("16-06-2020 10:50".isolateLtr()).isEqualTo("⁦16-06-2020 10:50⁩")
    }
}
