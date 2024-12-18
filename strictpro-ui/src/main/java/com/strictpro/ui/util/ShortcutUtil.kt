package com.strictpro.ui.util

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutManager
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import com.strictpro.ui.R
import com.strictpro.ui.presentation.ui.StrictProUiActivity

private const val SHORTCUT_ID = "strictpro_shortcut_id"

object ShortcutUtil {
    @RequiresApi(VERSION_CODES.N_MR1)
    fun addDynamicShortcut(context: Context): Boolean {
        val shortcutManager = context.getSystemService(ShortcutManager::class.java)

        val dynamicShortcuts = shortcutManager.dynamicShortcuts

        val shortcutInstalled =
            dynamicShortcuts.any { shortcut -> shortcut.id == SHORTCUT_ID }

        if (shortcutInstalled) {
            return false
        }

        val (shortLabel, longLabel) =
            getShortAndLongLabels(context, getFirstLauncherActivityLabel(context))

        return ShortcutManagerCompat.pushDynamicShortcut(
            context,
            buildShortcut(context, shortLabel, longLabel),
        )
    }

    private fun getFirstLauncherActivityLabel(context: Context): String? {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        mainIntent.setPackage(context.packageName)
        val activities = context.packageManager.queryIntentActivities(mainIntent, 0)
            .filter {
                it.activityInfo.name != "com.strictpro.ui.presentation.StrictProUiActivity"
            }

        if (activities.isEmpty()) {
            return null
        }

        val firstMainActivity = activities.first()
            .activityInfo

        return if (firstMainActivity.labelRes != 0) {
            context.getString(firstMainActivity.labelRes)
        } else {
            context.packageManager.getApplicationLabel(context.applicationInfo)
        }.toString()
    }

    private fun getShortAndLongLabels(
        context: Context,
        firstLauncherActivityLabel: String?,
    ): Pair<String, String> {
        val label = context.getString(R.string.strictpro)

        if (firstLauncherActivityLabel == null) {
            return label to label
        }

        // Displayed on long tap on app icon
        val longLabel: String
        // Label when dropping shortcut to launcher
        val shortLabel: String

        val fullLengthLabel = "$label $firstLauncherActivityLabel"
        // short label should be under 10 and long label under 25
        if (fullLengthLabel.length > 10) {
            if (fullLengthLabel.length <= 25) {
                longLabel = fullLengthLabel
                shortLabel = label
            } else {
                longLabel = label
                shortLabel = label
            }
        } else {
            longLabel = fullLengthLabel
            shortLabel = fullLengthLabel
        }
        return shortLabel to longLabel
    }

    private fun buildShortcut(context: Context, shortLabel: String, longLabel: String) =
        ShortcutInfoCompat.Builder(context, SHORTCUT_ID)
            .setShortLabel(shortLabel)
            .setLongLabel(longLabel)
            .setIntent(Intent(context, StrictProUiActivity::class.java).setAction("stub"))
            .build()
}