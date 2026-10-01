package com.payments.app.core.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Marker interfaces for the three MVI channels of a feature. Implementing them per-feature keeps
 * the contract explicit and self-documenting.
 */
interface UiState
interface UiEvent
interface UiAction

/**
 * Base ViewModel implementing the MVI (Model-View-Intent) unidirectional data flow:
 *
 *  - [state]  : the single immutable [UiState] the View renders (StateFlow).
 *  - [action] : one-shot actions (navigation, snackbars) the View consumes exactly once.
 *  - [onEvent]: the only entry point through which the View sends user intents into the ViewModel.
 *
 * Each screen defines its state as a sealed [UiState] (typically `Loading` / `Error` / `Success`
 * variants). Concrete ViewModels implement [handleEvent] and update state via [setState].
 */
abstract class MviViewModel<S : UiState, E : UiEvent, A : UiAction>(
    initialState: S,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    // Channel (not StateFlow) so actions are delivered once and never re-emitted on config change.
    private val _action = Channel<A>(Channel.BUFFERED)
    val action: Flow<A> = _action.receiveAsFlow()

    val currentState: S get() = _state.value

    /** The single public entry point for user intents. */
    fun onEvent(event: E) = handleEvent(event)

    protected abstract fun handleEvent(event: E)

    /** Replace the state. Typical with sealed states: `setState(MyUiState.Loading)`. */
    protected fun setState(state: S) {
        _state.value = state
    }

    /** Reduce the current state to a new one, e.g. `setState { if (this is Success) copy(...) else this }`. */
    protected fun setState(reducer: S.() -> S) {
        _state.update(reducer)
    }

    /** Emit a one-shot action to the View. */
    protected fun sendAction(action: A) {
        viewModelScope.launch { _action.send(action) }
    }
}
