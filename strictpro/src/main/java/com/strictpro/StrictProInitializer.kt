package com.strictpro

import android.content.Context
import androidx.startup.Initializer

@Suppress("unused")
internal class StrictProInitializer : Initializer<StrictPro> {
    override fun create(context: Context): StrictPro {
        StrictPro.listenActivities(context)
        return StrictPro
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        // No dependencies on other libraries.
        return emptyList()
    }
}
