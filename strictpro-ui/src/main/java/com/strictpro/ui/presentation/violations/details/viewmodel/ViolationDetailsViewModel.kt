package com.strictpro.ui.presentation.violations.details.viewmodel

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strictpro.ui.domain.model.StrictProViolation
import com.strictpro.ui.domain.model.getViolationName
import com.strictpro.ui.domain.usecase.GetViolationUseCase
import com.strictpro.ui.presentation.util.snackbar.snackbarCoroutineExceptionHandler
import com.strictpro.utils.Base64Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ViolationDetailsViewModel(
    private val getViolationUseCase: GetViolationUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ViolationDetailsState())
    val state: StateFlow<ViolationDetailsState> = _state

    fun loadData(
        violationId: String,
    ) {
        viewModelScope.launch(Dispatchers.IO + snackbarCoroutineExceptionHandler) {
            val violation = getViolationUseCase.execute(violationId)
            if (violation != null) {
                _state.value = ViolationDetailsState(
                    title = violation.getViolationName(),
                    trace = violation.getTraceString(),
                    strictProViolation = violation,
                )
            }
        }
    }

    fun printTrace() {
        if (VERSION.SDK_INT >= VERSION_CODES.P) {
            Log.e("StrictPro", "printTrace", state.value.strictProViolation?.violation)
        }
    }

    fun printBase64Trace() {
        if (VERSION.SDK_INT >= VERSION_CODES.P) {
            val base64 = state.value.strictProViolation?.violation?.stackTraceToString()?.let {
                Base64Util.encodeToString(it)
            }
            if (base64 != null) {
                Log.e("StrictPro", "printBase64: $base64")
            }
        }
    }

    private fun StrictProViolation.getTraceString(): List<String> {
        return if (VERSION.SDK_INT >= VERSION_CODES.P) {
            violation.stackTrace
                .map { it.toString() }
        } else {
            emptyList()
        }
    }
}

internal data class ViolationDetailsState(
    val title: String = "",
    val trace: List<String> = emptyList(),
    val strictProViolation: StrictProViolation? = null,
)
