package com.payments.app.core.ui.error

import com.google.common.truth.Truth.assertThat
import com.payments.app.core.common.error.AppException
import com.payments.app.core.ui.R
import org.junit.Test

class ErrorMessagesTest {

    @Test
    fun `each AppException maps to its own message`() {
        assertThat(AppException.Network().toMessageRes()).isEqualTo(R.string.error_network)
        assertThat(AppException.Server(code = 500).toMessageRes()).isEqualTo(R.string.error_server)
        assertThat(AppException.InvalidResponse().toMessageRes()).isEqualTo(R.string.error_invalid_response)
        assertThat(AppException.Unknown().toMessageRes()).isEqualTo(R.string.error_unknown)
    }

    @Test
    fun `any other exception maps to the generic message`() {
        assertThat(IllegalStateException("boom").toMessageRes()).isEqualTo(R.string.error_unknown)
    }
}
