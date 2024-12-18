package com.strictpro.ui

import android.os.strictmode.Violation
import com.strictpro.ui.domain.ViolationRepository
import com.strictpro.ui.domain.model.StrictProViolation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

internal object ViolationSaver : KoinComponent {

    private val violationRepository by lazy { getKoin().get<ViolationRepository>() }

    fun save(violation: Violation) {
        MainScope().launch(Dispatchers.IO) {
            val dateMillis = System.currentTimeMillis()
            violationRepository.saveViolation(
                violation = StrictProViolation(
                    id = dateMillis.toString(),
                    dateMillis = dateMillis,
                    violation = violation,
                ),
            )
        }
    }
}