package com.strictpro.ui.domain

import com.strictpro.ui.domain.model.StrictProViolation
import kotlinx.coroutines.flow.Flow

interface ViolationRepository {

    suspend fun saveViolation(violation: StrictProViolation)

    suspend fun observeViolations(): Flow<List<StrictProViolation>>
}