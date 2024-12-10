package com.strictpro.ui.domain.usecase

import com.strictpro.ui.domain.ViolationRepository
import com.strictpro.ui.domain.model.StrictProViolation
import kotlinx.coroutines.flow.firstOrNull

internal class GetViolationUseCase(
    private val violationRepository: ViolationRepository,
) {

    suspend fun execute(violationId: String): StrictProViolation? {
        val violations = violationRepository.observeViolations().firstOrNull()
        return violations?.firstOrNull { it.id == violationId }
    }
}