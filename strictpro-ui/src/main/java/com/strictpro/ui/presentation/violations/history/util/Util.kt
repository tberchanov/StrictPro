package com.strictpro.ui.presentation.violations.history.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun formatViolationDate(dateMillis: Long): String {
    return SimpleDateFormat(
        "\uD83D\uDDD3\uFE0F MM.dd  \uD83D\uDD52 HH:mm:ss",
        Locale.ROOT
    ).format(Date(dateMillis))
}
