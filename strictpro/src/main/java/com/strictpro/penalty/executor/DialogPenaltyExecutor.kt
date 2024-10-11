package com.strictpro.penalty.executor

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.strictmode.Violation
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.strictpro.R
import com.strictpro.penalty.ViolationPenalty
import com.strictpro.utils.Base64Util
import com.strictpro.utils.stackTraceToStringCompat

private const val TAG = "DialogPenaltyExecutor"

internal class DialogPenaltyExecutor : PenaltyExecutor {
    override fun executablePenalty() = ViolationPenalty.Dialog

    override fun executePenalty(violation: Violation, currentActivity: Activity?) {
        if (currentActivity != null) {
            Dialog(currentActivity).apply {
                setCancelable(true)
                setContentView(R.layout.dialog_violation)
                findViewById<TextView>(R.id.tv_violation).text = violation.toString()
                findViewById<Button>(R.id.btn_ok).setOnClickListener {
                    dismiss()
                }
                findViewById<Button>(R.id.btn_copy_base64).setOnClickListener {
                    copyToClipboard(
                        it.context,
                        Base64Util.encodeToString(violation.stackTraceToStringCompat()),
                    )
                }
                findViewById<Button>(R.id.btn_copy_stack).setOnClickListener {
                    copyToClipboard(
                        it.context,
                        violation.stackTraceToStringCompat(),
                    )
                }
                show()
            }
        } else {
            Log.e(TAG, "Cannot show penalty dialog, as current activity is null")
        }
    }

    private fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
    }
}