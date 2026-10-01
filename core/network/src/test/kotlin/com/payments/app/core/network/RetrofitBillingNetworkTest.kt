package com.payments.app.core.network

import com.google.common.truth.Truth.assertThat
import com.payments.app.core.common.error.AppException
import com.payments.app.core.network.model.BillingEntryDetailsDto
import com.payments.app.core.network.model.BillingEntryHeaderDto
import com.payments.app.core.network.model.DeleteBillingEntryResponse
import com.payments.app.core.network.retrofit.RetrofitBillingNetwork
import com.payments.app.core.network.retrofit.billingJson
import com.payments.app.core.network.retrofit.createRetrofit
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.jsonArray
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Test

class RetrofitBillingNetworkTest {

    private val server = MockWebServer()
    private lateinit var network: RetrofitBillingNetwork

    @Before
    fun setUp() {
        server.start()
        network = RetrofitBillingNetwork(createRetrofit(server.url("/").toString(), OkHttpClient()))
    }

    @After
    fun tearDown() {
        server.close()
    }

    // --- Requests match the contract ---

    @Test
    fun `headers is a POST with an empty JSON object`() = runTest {
        server.enqueue(json("""{"headers":[]}"""))

        network.getHeaders()

        val request = server.takeRequest()
        assertThat(request.method).isEqualTo("POST")
        assertThat(request.url.encodedPath).isEqualTo("/payment/billing/entry/headers")
        assertThat(request.body?.utf8()).isEqualTo("{}")
        assertThat(request.headers["Content-Type"]).startsWith("application/json")
    }

    @Test
    fun `details is a POST with the billing id`() = runTest {
        server.enqueue(json("""{"details":null}"""))

        network.getDetails(5200)

        val request = server.takeRequest()
        assertThat(request.method).isEqualTo("POST")
        assertThat(request.url.encodedPath).isEqualTo("/payment/billing/entry/details")
        assertThat(request.body?.utf8()).isEqualTo("""{"billingId":5200}""")
    }

    @Test
    fun `delete is a POST with the billing id`() = runTest {
        server.enqueue(json("""{"status":0}"""))

        network.delete(5200)

        val request = server.takeRequest()
        assertThat(request.method).isEqualTo("POST")
        assertThat(request.url.encodedPath).isEqualTo("/payment/billing/entry/delete")
        assertThat(request.body?.utf8()).isEqualTo("""{"billingId":5200}""")
    }

    // --- Responses: recorded from the real server ---

    @Test
    fun `parses the server's full headers response`() = runTest {
        server.enqueue(json(ServerResponses.headers))

        val headers = network.getHeaders()

        assertThat(headers).hasSize(486)
        assertThat(headers.first()).isEqualTo(
            BillingEntryHeaderDto(
                id = 5165,
                price = 25357.52885790945,
                created = 1592293845,
                entryNumber = 2,
                totalEntryCount = 17,
                source = "Terminal",
                currencyCode = "USD",
                cardType = "Amex",
            ),
        )
    }

    @Test
    fun `parses the server's details response`() = runTest {
        server.enqueue(json(ServerResponses.details5165))

        val details = network.getDetails(5165)

        assertThat(details).isEqualTo(
            BillingEntryDetailsDto(
                id = 5165,
                price = 25357.52885790945,
                created = 1592293845,
                entryNumber = 2,
                totalEntryCount = 17,
                currencyCode = "USD",
                amountPaid = 2992.4083006040337,
                status = "Rejected",
                cardNumber = "7359327724873",
                cardType = "Amex",
                issuer = "Jcb",
                source = "Terminal",
                terminalName = "Emv 0",
                approvalNumber = "9748301211203",
                voucherNumber = "77-201-69",
            ),
        )
    }

    @Test
    fun `every details entry the server can return parses`() {
        val entries = billingJson.parseToJsonElement(ServerResponses.serverDetailsData).jsonArray

        val parsed = entries.map { billingJson.decodeFromJsonElement(BillingEntryDetailsDto.serializer(), it) }

        assertThat(parsed).hasSize(486)
    }

    @Test
    fun `unknown id returns null details`() = runTest {
        server.enqueue(json(ServerResponses.detailsUnknownId))

        assertThat(network.getDetails(999)).isNull()
    }

    @Test
    fun `delete returns the server's status`() = runTest {
        server.enqueue(json(ServerResponses.deleteSuccess))
        server.enqueue(json(ServerResponses.deleteFailure))

        assertThat(network.delete(5165).status).isEqualTo(DeleteBillingEntryResponse.STATUS_SUCCESS)
        assertThat(network.delete(5165).status).isEqualTo(-1)
    }

    // --- Failures become AppException ---

    @Test
    fun `HTTP error becomes Server with the status code`() = runTest {
        server.enqueue(MockResponse.Builder().code(500).body("oops").build())

        val error = runCatching { network.getHeaders() }.exceptionOrNull()

        assertThat(error).isInstanceOf(AppException.Server::class.java)
        assertThat((error as AppException.Server).code).isEqualTo(500)
    }

    @Test
    fun `malformed or incomplete JSON becomes InvalidResponse`() = runTest {
        server.enqueue(json("""{"headers":[{"id":1}]}"""))
        server.enqueue(json("not json"))

        assertThat(runCatching { network.getHeaders() }.exceptionOrNull())
            .isInstanceOf(AppException.InvalidResponse::class.java)
        assertThat(runCatching { network.getHeaders() }.exceptionOrNull())
            .isInstanceOf(AppException.InvalidResponse::class.java)
    }

    @Test
    fun `unreachable server becomes Network`() = runTest {
        server.close()

        assertThat(runCatching { network.getHeaders() }.exceptionOrNull())
            .isInstanceOf(AppException.Network::class.java)
    }

    private fun json(body: String) = MockResponse.Builder()
        .code(200)
        .addHeader("Content-Type", "application/json")
        .body(body)
        .build()
}
