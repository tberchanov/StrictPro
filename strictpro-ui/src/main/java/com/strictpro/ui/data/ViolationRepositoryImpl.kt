package com.strictpro.ui.data

import com.strictpro.ui.domain.model.StrictProViolation
import com.strictpro.ui.domain.ViolationRepository
import kotlinx.coroutines.flow.Flow

class ViolationRepositoryImpl(
    private val localViolationDataSource: LocalViolationDataSource,
) : ViolationRepository {
    override suspend fun saveViolation(violation: StrictProViolation) {
        localViolationDataSource.saveViolation(violation)
    }

    override suspend fun observeViolations(): Flow<List<StrictProViolation>> {
        return localViolationDataSource.observeViolations()
    }
}