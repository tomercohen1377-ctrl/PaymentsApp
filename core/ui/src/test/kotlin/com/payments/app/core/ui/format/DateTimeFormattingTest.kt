package com.payments.app.core.ui.format

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

class DateTimeFormattingTest {

    @Test
    fun `formats with the spec pattern dd-MM-yyyy HH mm`() {
        // `created` from server/list.json (epoch seconds) for id 5165.
        val created = Instant.ofEpochSecond(1_592_293_845)

        assertThat(formatDateTime(created, ZoneOffset.UTC)).isEqualTo("16-06-2020 07:50")
    }

    @Test
    fun `uses the given time zone`() {
        val created = Instant.ofEpochSecond(1_661_097_720) // 2022-08-21 16:02 UTC

        assertThat(formatDateTime(created, ZoneId.of("Asia/Jerusalem"))).isEqualTo("21-08-2022 19:02")
    }

    @Test
    fun `uses a 24-hour clock and zero-padded fields`() {
        val created = Instant.parse("2023-01-05T03:04:00Z")

        assertThat(formatDateTime(created, ZoneOffset.UTC)).isEqualTo("05-01-2023 03:04")
    }
}
