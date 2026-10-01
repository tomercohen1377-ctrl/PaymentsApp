package com.payments.app.core.ui.format

private const val LEFT_TO_RIGHT_ISOLATE = '⁦'
private const val POP_DIRECTIONAL_ISOLATE = '⁩'

/**
 * Keeps left-to-right text in its own order inside right-to-left text. Without it, in Hebrew a date
 * time like `16-06-2020 10:50` is laid out as `10:50 16-06-2020`, because the bidi algorithm orders
 * the two number runs right to left. The isolate marks are invisible.
 */
fun String.isolateLtr(): String = "$LEFT_TO_RIGHT_ISOLATE$this$POP_DIRECTIONAL_ISOLATE"
