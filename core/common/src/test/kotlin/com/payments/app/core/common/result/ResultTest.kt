package com.payments.app.core.common.result

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ResultTest {

    @Test
    fun `asResult emits Loading, then each value as Success`() = runTest {
        flowOf(1, 2).asResult().test {
            assertThat(awaitItem()).isEqualTo(Result.Loading)
            assertThat(awaitItem()).isEqualTo(Result.Success(1))
            assertThat(awaitItem()).isEqualTo(Result.Success(2))
            awaitComplete()
        }
    }

    @Test
    fun `asResult turns a failure into Error`() = runTest {
        val failure = IllegalStateException("boom")

        flow<Int> { throw failure }.asResult().test {
            assertThat(awaitItem()).isEqualTo(Result.Loading)
            assertThat((awaitItem() as Result.Error).exception).isSameInstanceAs(failure)
            awaitComplete()
        }
    }
}
