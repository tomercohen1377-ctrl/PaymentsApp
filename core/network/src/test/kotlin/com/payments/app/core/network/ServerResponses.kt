package com.payments.app.core.network

import java.io.File

/**
 * Response bodies recorded verbatim from the test server (`server/`, `npm run dev`), stored in
 * `src/test/resources/responses/`. Tests replay these, so the DTOs are checked against what the
 * server really sends rather than against the PDF.
 */
internal object ServerResponses {
    /** `/headers` with `{}`: all 486 entries. */
    val headers: String by lazy { resource("headers.json") }

    /** `/details` with `{"billingId":5165}`. */
    val details5165: String by lazy { resource("details_5165.json") }

    /** `/details` with an id that doesn't exist: `{"details":null}`. */
    val detailsUnknownId: String by lazy { resource("details_unknown_id.json") }

    /** `/delete` of an existing id: `{"status":0}`. */
    val deleteSuccess: String by lazy { resource("delete_success.json") }

    /** `/delete` of an id that no longer exists: `{"status":-1}`. */
    val deleteFailure: String by lazy { resource("delete_failure.json") }

    /** The server's own data file, to check that every one of its details entries parses. */
    val serverDetailsData: String by lazy { repoFile("server/details.json").readText() }

    private fun resource(name: String): String =
        requireNotNull(javaClass.classLoader?.getResource("responses/$name")) {
            "Missing test resource $name"
        }.readText()

    // Tests run with the module as working directory; walk up to the repository root.
    private fun repoFile(path: String): File = generateSequence(File("").absoluteFile) { it.parentFile }
        .map { File(it, path) }
        .first { it.isFile }
}
