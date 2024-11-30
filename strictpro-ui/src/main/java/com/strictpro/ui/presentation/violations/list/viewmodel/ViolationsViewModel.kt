package com.strictpro.ui.presentation.violations.list.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strictpro.ui.domain.model.ViolationQuantity
import com.strictpro.ui.domain.usecase.GetViolationsQuantityUseCase
import com.strictpro.ui.presentation.util.StringProvider
import com.strictpro.ui.presentation.util.snackbar.snackbarCoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ViolationsViewModel(
    private val getViolationsQuantityUseCase: GetViolationsQuantityUseCase,
    private val stringProvider: StringProvider,
) : ViewModel() {

    private val _state = MutableStateFlow(ViolationsListState())
    val state: StateFlow<ViolationsListState> = _state

    fun loadData() {
        viewModelScope.launch(snackbarCoroutineExceptionHandler) {
            getViolationsQuantityUseCase.execute()
                .collect { violations ->
                    _state.value = ViolationsListState(
                        title = stringProvider.violationsScreenTitle(violations.size),
                        violations = violations,
                    )
                }
        }
    }
}

@Stable
data class ViolationsListState(
    val title: String = "",
    val violations: List<ViolationQuantity> = emptyList(),
)