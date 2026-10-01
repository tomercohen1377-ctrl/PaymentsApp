package com.payments.app.core.common.dispatcher

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Hilt qualifier used to inject a specific [kotlinx.coroutines.CoroutineDispatcher].
 * Injecting dispatchers (rather than hard-coding `Dispatchers.IO`) keeps code testable.
 */
@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val dispatcher: PaymentsDispatchers)

enum class PaymentsDispatchers {
    Default,
    IO,
}
