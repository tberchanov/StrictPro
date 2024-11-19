package com.strictpro.ui.presentation.util.snackbar

import androidx.compose.material.SnackbarDuration.Long
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult.ActionPerformed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.strictpro.ui.presentation.util.ObserveAsEvents
import kotlinx.coroutines.launch

@Composable
internal fun observeSnackbars(): SnackbarHostState {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    ObserveAsEvents(
        flow = SnackbarController.events,
        snackbarHostState
    ) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = Long
            )

            SnackbarController.finishedEvents.emit(FinishedSnackbarEvent(event))

            if (result == ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }
    return snackbarHostState
}
