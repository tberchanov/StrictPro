package com.strictpro.ui.domain.usecase

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.strictpro.ui.domain.ViolationRepository
import com.strictpro.ui.domain.model.ViolationQuantity
import com.strictpro.ui.domain.model.ViolationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

internal class GetViolationsQuantityUseCase(
    private val violationRepository: ViolationRepository,
) {

    suspend fun execute(): Flow<List<ViolationQuantity>> {
        return if (VERSION.SDK_INT >= VERSION_CODES.P) {
            violationRepository.observeViolations().map { violationsList ->
                violationsList
                    .groupBy { it.violation::class.java.simpleName }
                    .map { (violationClassName, violations) ->
                        ViolationQuantity(violations.size, ViolationType(violationClassName))
                    }
                    .sortedByDescending { it.quantity }
            }
        } else {
            emptyFlow()
        }
    }
}