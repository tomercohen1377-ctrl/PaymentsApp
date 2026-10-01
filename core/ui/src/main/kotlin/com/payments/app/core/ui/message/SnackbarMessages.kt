package com.payments.app.core.ui.message

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState

/** Shows a string resource in the snackbar; ViewModels send `@StringRes` messages as actions. */
suspend fun SnackbarHostState.showMessage(resources: Resources, @StringRes message: Int) {
    showSnackbar(resources.getString(message))
}
