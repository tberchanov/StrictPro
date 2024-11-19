package com.strictpro.ui

import android.os.strictmode.Violation
import com.strictpro.ui.domain.model.StrictProViolation
import com.strictpro.ui.domain.ViolationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

object ViolationSaver : KoinComponent {

    fun save(violation: Violation) {
        MainScope().launch(Dispatchers.IO) {
            getKoin().get<ViolationRepository>()
                .saveViolation(
                    violation = StrictProViolation(
                        dateMillis = System.currentTimeMillis(),
                        violation = violation,
                    ),
                )
        }
    }
}