package com.strictpro.ui.domain.usecase

import android.content.Context

class GetAppPackageNameUseCase(
    private val context: Context,
) {

    fun execute(): String {
        return context.applicationContext.packageName
    }
}