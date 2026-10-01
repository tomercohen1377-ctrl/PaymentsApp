package com.payments.app.core.network

import com.google.common.truth.Truth.assertThat
import com.payments.app.core.common.error.AppException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class NetworkErrorMappingTest {

    @Test
    fun `IO failures map to Network`() {
        assertThat(IOException("no route").toAppException()).isInstanceOf(AppException.Network::class.java)
        assertThat(SocketTimeoutException().toAppException()).isInstanceOf(AppException.Network::class.java)
    }

    @Test
    fun `HTTP errors map to Server with the status code`() {
        val error = HttpException(Response.error<Any>(503, "".toResponseBody()))

        val mapped = error.toAppException()

        assertThat(mapped).isInstanceOf(AppException.Server::class.java)
        assertThat((mapped as AppException.Server).code).isEqualTo(503)
    }

    @Test
    fun `parsing failures map to InvalidResponse`() {
        assertThat(SerializationException("bad json").toAppException())
            .isInstanceOf(AppException.InvalidResponse::class.java)
    }

    @Test
    fun `anything else maps to Unknown and keeps the cause`() {
        val cause = IllegalStateException("boom")

        val mapped = cause.toAppException()

        assertThat(mapped).isInstanceOf(AppException.Unknown::class.java)
        assertThat(mapped.cause).isSameInstanceAs(cause)
    }

    @Test
    fun `networkCall translates failures`() = runTest {
        val result = runCatching { networkCall { throw IOException("offline") } }

        assertThat(result.exceptionOrNull()).isInstanceOf(AppException.Network::class.java)
    }

    @Test
    fun `networkCall rethrows cancellation untouched`() = runTest {
        val cancellation = CancellationException("cancelled")

        val result = runCatching { networkCall { throw cancellation } }

        assertThat(result.exceptionOrNull()).isSameInstanceAs(cancellation)
    }
}
