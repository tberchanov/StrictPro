package com.strictpro.ui.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.strictpro.ui.data.LocalViolationDataSource
import com.strictpro.ui.data.ViolationRepositoryImpl
import com.strictpro.ui.domain.ViolationRepository
import com.strictpro.ui.domain.usecase.GetAppPackageNameUseCase
import com.strictpro.ui.domain.usecase.GetViolationUseCase
import com.strictpro.ui.domain.usecase.GetViolationsQuantityUseCase
import com.strictpro.ui.domain.usecase.GetViolationsUseCase
import com.strictpro.ui.presentation.util.StringProvider
import com.strictpro.ui.presentation.violations.details.viewmodel.ViolationDetailsViewModel
import com.strictpro.ui.presentation.violations.history.viewmodel.ViolationsHistoryViewModel
import com.strictpro.ui.presentation.violations.list.viewmodel.ViolationsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val Context.violationsDataStore: DataStore<Preferences> by preferencesDataStore(name = "violations")

internal val dataModule = module {
    single { androidContext().violationsDataStore }
    single { LocalViolationDataSource(get()) }
    single<ViolationRepository> { ViolationRepositoryImpl(get()) }
}

internal val domainModule = module {
    factory { GetViolationsQuantityUseCase(get()) }
    factory { GetViolationsUseCase(get()) }
    factory { GetAppPackageNameUseCase(androidContext()) }
    factory { GetViolationUseCase(get()) }
}

internal val viewModelModule = module {
    viewModel { ViolationsViewModel(get(), get()) }
    viewModel { ViolationsHistoryViewModel(get(), get(), get()) }
    viewModel { ViolationDetailsViewModel(get()) }
}

internal val presentationModule = module {
    single { StringProvider(androidContext()) }
}

internal val appModules = listOf(
    dataModule,
    domainModule,
    viewModelModule,
    presentationModule,
)