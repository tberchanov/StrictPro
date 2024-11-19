package com.strictpro.ui.presentation.util.snackbar

import com.strictpro.ui.presentation.ui.theme.AccentRed
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

internal val snackbarCoroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    MainScope().launch {
        SnackbarController.sendEvent(
            SnackbarEvent(exception.message ?: "Unknown error", backgroundColor = AccentRed)
        )
    }
}
