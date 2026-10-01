package com.payments.app.core.network.retrofit

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * JSON settings for the billing API: unknown fields are ignored so the server can add fields, but a
 * missing required field fails parsing (and becomes `AppException.InvalidResponse`).
 */
internal val billingJson = Json {
    ignoreUnknownKeys = true
}

/** Builds the app's Retrofit instance. Used by the Hilt module and by tests (with a MockWebServer URL). */
internal fun createRetrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient,
    json: Json = billingJson,
): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()
