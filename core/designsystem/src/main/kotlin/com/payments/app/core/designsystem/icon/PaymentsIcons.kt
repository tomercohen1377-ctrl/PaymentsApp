package com.payments.app.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.ui.graphics.vector.ImageVector
import com.payments.app.core.designsystem.R

/**
 * Every icon the app uses, so features never depend on an icon library directly.
 *
 * Drawable resources are the interview assets (copied from `InterviewAssets/`), named by what they
 * show. Their file names don't match their billing meaning: `ic_pos` is a phone (used for the
 * Terminal source) and `ic_card` is a card (used for Pos). The others are Material icons.
 */
object PaymentsIcons {
    val Phone = R.drawable.ic_pos
    val Card = R.drawable.ic_card
    val Person = R.drawable.ic_attendant
    val Upload = R.drawable.ic_upload
    val Dollar = R.drawable.ic_dollar
    val Shekel = R.drawable.ic_ils

    /** Points to the end of the row (mirrors to `<` in RTL, as in the mockup). */
    val ArrowForward: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight
    val Delete: ImageVector = Icons.Outlined.Delete
}
