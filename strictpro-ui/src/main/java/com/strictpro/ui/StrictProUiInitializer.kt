package com.strictpro.ui

import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.startup.Initializer
import com.strictpro.ui.di.appModules
import com.strictpro.ui.util.ShortcutUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
internal class StrictProUiInitializer : Initializer<Any> {
    override fun create(context: Context): Any {
        startKoin {
            androidContext(context)
            modules(appModules)
        }

        if (VERSION.SDK_INT >= VERSION_CODES.N_MR1) {
            ShortcutUtil.addDynamicShortcut(context)
        }
        return "StrictProUiInitializer" // Return a dummy object
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // No dependencies on other libraries.
        return emptyList()
    }
}