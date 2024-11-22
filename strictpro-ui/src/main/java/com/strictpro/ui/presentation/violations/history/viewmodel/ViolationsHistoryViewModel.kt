package com.strictpro.ui.presentation.violations.history.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strictpro.ui.domain.model.StrictProViolation
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.domain.usecase.GetAppPackageNameUseCase
import com.strictpro.ui.domain.usecase.GetViolationsUseCase
import com.strictpro.ui.presentation.util.snackbar.snackbarCoroutineExceptionHandler
import com.strictpro.ui.presentation.violations.history.model.ViolationHistoryItemUI
import com.strictpro.ui.presentation.violations.history.model.toViolationHistoryItemUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ViolationsHistoryViewModel(
    private val getViolationsUseCase: GetViolationsUseCase,
    private val getAppPackageNameUseCase: GetAppPackageNameUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ViolationsHistoryState())
    val state: StateFlow<ViolationsHistoryState> = _state

    private val packageName by lazy { getAppPackageNameUseCase.execute() }

    fun loadData(
        violationType: ViolationType?,
    ) {
        viewModelScope.launch(snackbarCoroutineExceptionHandler) {
            getViolationsUseCase.execute(violationType)
                .map { it.toViolationHistoryItemUI(packageName) }
                .collect { historyItems ->
                    _state.value = ViolationsHistoryState(historyItems = historyItems)
                }
        }
    }

    private fun List<StrictProViolation>.toViolationHistoryItemUI(
        packageName: String,
    ): List<ViolationHistoryItemUI> {
        return map { it.toViolationHistoryItemUI(packageName) }
    }
}

@Stable
data class ViolationsHistoryState(
    val historyItems: List<ViolationHistoryItemUI> = emptyList(),
)
