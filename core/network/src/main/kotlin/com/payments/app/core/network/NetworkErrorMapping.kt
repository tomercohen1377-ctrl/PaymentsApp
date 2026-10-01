package com.payments.app.core.network

import com.payments.app.core.common.error.AppException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Runs a network call and translates library exceptions into [AppException]s. Cancellation is
 * rethrown untouched so structured concurrency keeps working.
 */
internal suspend fun <T> networkCall(block: suspend () -> T): T = try {
    block()
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    throw e.toAppException()
}

internal fun Throwable.toAppException(): AppException = when (this) {
    is AppException -> this
    is HttpException -> AppException.Server(code(), this)
    is SerializationException -> AppException.InvalidResponse(this)
    is IOException -> AppException.Network(this)
    else -> AppException.Unknown(this)
}
