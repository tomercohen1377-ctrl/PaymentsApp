package com.payments.app.core.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * A generic wrapper describing the three states an asynchronous data stream can be in.
 * Repositories expose `Flow<Result<T>>` so the UI can render loading / success / error uniformly.
 */
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable) : Result<Nothing>
    data object Loading : Result<Nothing>
}

/**
 * Convert any `Flow<T>` into a `Flow<Result<T>>`, emitting [Result.Loading] first and mapping
 * thrown exceptions to [Result.Error].
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> = this
    .map<T, Result<T>> { Result.Success(it) }
    .onStart { emit(Result.Loading) }
    .catch { emit(Result.Error(it)) }
