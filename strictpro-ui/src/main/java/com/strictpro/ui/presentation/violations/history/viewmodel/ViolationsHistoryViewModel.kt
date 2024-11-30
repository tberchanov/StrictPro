package com.strictpro.ui.presentation.violations.history.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strictpro.ui.domain.model.StrictProViolation
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.domain.usecase.GetAppPackageNameUseCase
import com.strictpro.ui.domain.usecase.GetViolationsUseCase
import com.strictpro.ui.presentation.util.StringProvider
import com.strictpro.ui.presentation.util.snackbar.snackbarCoroutineExceptionHandler
import com.strictpro.ui.presentation.violations.history.model.ViolationHistoryItemUI
import com.strictpro.ui.presentation.violations.history.model.toViolationHistoryItemUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class ViolationsHistoryViewModel(
    private val getViolationsUseCase: GetViolationsUseCase,
    private val getAppPackageNameUseCase: GetAppPackageNameUseCase,
    private val stringProvider: StringProvider,
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
                    _state.value = ViolationsHistoryState(
                        title = getTitle(historyItems.size, violationType),
                        historyItems = historyItems,
                        showBackButton = violationType != null,
                    )
                }
        }
    }

    private fun getTitle(itemsSize: Int, violationType: ViolationType?): String {
        return if (violationType == null) {
            stringProvider.totalViolations(itemsSize)
        } else {
            stringProvider.violations(itemsSize, violationType.value)
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
    val title: String = "",
    val historyItems: List<ViolationHistoryItemUI> = emptyList(),
    val showBackButton: Boolean = false,
)
