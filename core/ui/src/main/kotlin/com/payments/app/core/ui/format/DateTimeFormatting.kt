package com.payments.app.core.ui.format

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/** The spec's date-time pattern for billing entries. */
const val BILLING_DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm"

// Locale.ROOT keeps ASCII digits regardless of the device language; the pattern has no names.
private val billingDateTimeFormatter = DateTimeFormatter.ofPattern(BILLING_DATE_TIME_PATTERN, Locale.ROOT)

/** Formats [instant] as `dd-MM-yyyy HH:mm` in [zone] (the device's time zone by default). */
fun formatDateTime(instant: Instant, zone: ZoneId = ZoneId.systemDefault()): String =
    billingDateTimeFormatter.format(instant.atZone(zone))
