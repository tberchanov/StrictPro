package com.strictpro.ui.presentation.util.snackbar

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow

internal data class SnackbarEvent(
    val message: String,
    val action: SnackbarAction? = null,
    val backgroundColor: Color? = null,
)

internal data class FinishedSnackbarEvent(
    val event: SnackbarEvent,
)

internal data class SnackbarAction(
    val name: String,
    val action: suspend () -> Unit,
)

internal object SnackbarController {

    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()
    val finishedEvents = MutableSharedFlow<FinishedSnackbarEvent>()
    var lastEvent: SnackbarEvent? = null

    suspend fun sendEvent(event: SnackbarEvent) {
        lastEvent = event
        _events.send(event)
        finishedEvents.first { it.event === event }
    }
}
