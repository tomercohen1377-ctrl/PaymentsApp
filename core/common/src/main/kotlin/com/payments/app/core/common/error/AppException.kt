package com.payments.app.core.common.error

/**
 * Domain-level failures. The data layer translates library exceptions (IOException, HttpException,
 * SerializationException, ...) into these, so the domain and UI never depend on Retrofit or OkHttp
 * types and the UI can pick a user-facing message per case.
 */
sealed class AppException(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {

    /** No connectivity, DNS failure, timeout. Usually fixed by retrying. */
    class Network(cause: Throwable? = null) : AppException("Network unavailable", cause)

    /**
     * The server reported a failure: a non-2xx HTTP status, or an error status in the response body
     * (e.g. delete returning `status != 0`).
     */
    class Server(val code: Int, cause: Throwable? = null) : AppException("Server error $code", cause)

    /** The response could not be parsed into the expected model. */
    class InvalidResponse(cause: Throwable? = null) : AppException("Invalid response", cause)

    /** Anything not covered above. */
    class Unknown(cause: Throwable? = null) : AppException(cause?.message, cause)
}
