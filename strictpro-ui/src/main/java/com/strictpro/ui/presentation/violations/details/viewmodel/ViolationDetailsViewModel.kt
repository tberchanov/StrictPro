package com.strictpro.ui.presentation.violations.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strictpro.ui.domain.model.getViolationName
import com.strictpro.ui.domain.usecase.GetViolationUseCase
import com.strictpro.ui.presentation.util.snackbar.snackbarCoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ViolationDetailsViewModel(
    private val getViolationUseCase: GetViolationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ViolationDetailsState())
    val state: StateFlow<ViolationDetailsState> = _state

    fun loadData(
        violationId: String,
    ) {
        viewModelScope.launch(Dispatchers.IO + snackbarCoroutineExceptionHandler) {
            val violation = getViolationUseCase.execute(violationId)
            _state.value = ViolationDetailsState(
                title = violation?.getViolationName() ?: ""
            )
        }
    }
}

internal data class ViolationDetailsState(
    val title: String = "",
)
