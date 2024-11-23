package com.strictpro.ui.presentation.violations.list.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strictpro.ui.domain.model.ViolationQuantity
import com.strictpro.ui.domain.usecase.GetViolationsQuantityUseCase
import com.strictpro.ui.presentation.util.snackbar.snackbarCoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ViolationsViewModel(
    private val getViolationsQuantityUseCase: GetViolationsQuantityUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ViolationsListState())
    val state: StateFlow<ViolationsListState> = _state

    fun loadData() {
        viewModelScope.launch(snackbarCoroutineExceptionHandler) {
            getViolationsQuantityUseCase.execute()
                .collect { violations ->
                    _state.value = ViolationsListState(violations = violations)
                }
        }
    }
}

@Stable
data class ViolationsListState(
    val violations: List<ViolationQuantity> = emptyList(),
)