package com.strictpro.ui.presentation.violations.history.viewmodel

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strictpro.ui.domain.model.StrictProViolation
import com.strictpro.ui.domain.model.ViolationType
import com.strictpro.ui.domain.model.getViolationName
import com.strictpro.ui.domain.usecase.FilterStackTraceUseCase
import com.strictpro.ui.domain.usecase.GetViolationsUseCase
import com.strictpro.ui.presentation.util.StringProvider
import com.strictpro.ui.presentation.util.snackbar.snackbarCoroutineExceptionHandler
import com.strictpro.ui.presentation.violations.history.model.ViolationHistoryItemUI
import com.strictpro.ui.presentation.violations.history.util.formatViolationDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class ViolationsHistoryViewModel(
    private val getViolationsUseCase: GetViolationsUseCase,
    private val filterStackTraceUseCase: FilterStackTraceUseCase,
    private val stringProvider: StringProvider,
) : ViewModel() {

    private val _state = MutableStateFlow(ViolationsHistoryState())
    val state: StateFlow<ViolationsHistoryState> = _state

    fun loadData(
        violationType: ViolationType?,
    ) {
        viewModelScope.launch(Dispatchers.IO + snackbarCoroutineExceptionHandler) {
            getViolationsUseCase.execute(violationType)
                .map { it.toViolationHistoryItemUI() }
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

    private fun List<StrictProViolation>.toViolationHistoryItemUI(): List<ViolationHistoryItemUI> {
        return map {
            it.toViolationHistoryItemUI(
                filteredStackTraceItems = filterStackTraceUseCase.execute(it.violation.stackTrace),
            )
        }
    }
}

private fun StrictProViolation.toViolationHistoryItemUI(
    filteredStackTraceItems: List<StackTraceElement>,
): ViolationHistoryItemUI {
    val formattedDate = formatViolationDate(dateMillis)
    val formattedStackTraceItems = if (VERSION.SDK_INT >= VERSION_CODES.P) {
        filteredStackTraceItems
            .take(3)
            .map { it.toString() }
    } else {
        emptyList()
    }

    return ViolationHistoryItemUI(
        dateMillis = dateMillis,
        formattedDate = formattedDate,
        violationName = getViolationName(),
        filteredStackTraceItems = formattedStackTraceItems,
        violationId = id,
    )
}

data class ViolationsHistoryState(
    val title: String = "",
    val historyItems: List<ViolationHistoryItemUI> = emptyList(),
    val showBackButton: Boolean = false,
)
