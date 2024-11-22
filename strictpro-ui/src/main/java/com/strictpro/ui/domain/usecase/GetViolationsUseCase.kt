package com.strictpro.ui.domain.usecase

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.strictpro.ui.domain.ViolationRepository
import com.strictpro.ui.domain.model.StrictProViolation
import com.strictpro.ui.domain.model.ViolationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class GetViolationsUseCase(
    private val violationRepository: ViolationRepository,
) {

    suspend fun execute(violationType: ViolationType? = null): Flow<List<StrictProViolation>> {
        return if (VERSION.SDK_INT >= VERSION_CODES.P) {
            violationRepository.observeViolations().map { violationsList ->
                violationsList
                    .asSequence()
                    .filter { violation ->
                        if (violationType == null) {
                            true
                        } else {
                            violation.violation::class.java.simpleName == violationType.value
                        }
                    }
                    .sortedBy { it.dateMillis }
                    .toList()
            }
        } else {
            emptyFlow()
        }
    }
}