package com.payments.app.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Brand colors from the task spec. Features use these tokens (or the Material scheme built from
 * them), never inline hex values.
 */
object PaymentsColors {
    /** Spec "Green". Also the color of the Terminal source and of a Passed price. */
    val Green = Color(0xFF34AA53)

    /** Spec "Source pos". */
    val Lime = Color(0xFF80B918)

    /**
     * `#4F5860`. The spec calls it "Light gray" and also uses it for "Source manual"; it is the
     * darker of the two grays, used for secondary text and icons.
     */
    val Gray = Color(0xFF4F5860)

    /**
     * `#E3E6E9`. The spec calls it "Dark gray", but it is the lighter of the two grays, used for
     * dividers and subtle surfaces.
     */
    val GrayLight = Color(0xFFE3E6E9)

    /** Rejected price. Not in the spec; a standard Material red. */
    val Red = Color(0xFFE53935)

    /** Delete button, taken from the details mockup. Not in the spec. */
    val Pink = Color(0xFFF0386B)

    val SourceTerminal = Green
    val SourcePos = Lime
    val SourceManual = Gray
}
