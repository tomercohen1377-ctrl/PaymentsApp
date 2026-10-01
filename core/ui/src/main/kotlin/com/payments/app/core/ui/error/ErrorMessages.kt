package com.payments.app.core.ui.error

import androidx.annotation.StringRes
import com.payments.app.core.common.error.AppException
import com.payments.app.core.ui.R

/**
 * Maps a failure to a user-facing string resource. Raw exception messages are never shown: they are
 * not localized and can leak implementation details (host names, stack traces).
 */
@StringRes
fun Throwable.toMessageRes(): Int = when (this) {
    is AppException.Network -> R.string.error_network
    is AppException.Server -> R.string.error_server
    is AppException.InvalidResponse -> R.string.error_invalid_response
    else -> R.string.error_unknown
}
