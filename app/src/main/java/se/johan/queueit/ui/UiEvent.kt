package se.johan.queueit.ui

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
}

suspend fun handleUiEvent(event: UiEvent, action: (suspend (message: String) -> Unit)? = null) {
    when (event) {
        is UiEvent.ShowSnackbar -> action?.let { action(event.message) }
    }
}