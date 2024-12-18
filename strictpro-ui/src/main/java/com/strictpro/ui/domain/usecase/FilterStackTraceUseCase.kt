package com.strictpro.ui.domain.usecase

internal class FilterStackTraceUseCase(
    private val getAppPackageNameUseCase: GetAppPackageNameUseCase,
) {

    private val packageName by lazy { getAppPackageNameUseCase.execute() }

    fun execute(stackTrace: Array<StackTraceElement>): List<StackTraceElement> {
        return stackTrace.filter {
            it.className.startsWith(packageName)
        }
    }
}