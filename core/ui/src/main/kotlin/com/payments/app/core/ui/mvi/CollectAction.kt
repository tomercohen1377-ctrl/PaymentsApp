package com.payments.app.core.ui.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * Lifecycle-aware collector for one-shot MVI actions. Collection is paused when the screen is not
 * at least STARTED, so navigation/snackbar actions never fire while the UI is in the background.
 */
@Composable
fun <A : UiAction> CollectAction(
    action: Flow<A>,
    onAction: suspend (A) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(action, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            action.collect(onAction)
        }
    }
}
