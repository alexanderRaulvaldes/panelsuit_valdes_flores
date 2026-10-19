package com.panalbit.panalsuite.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// es solo para devolver la fecja y hora actual

object DateUtils {
    fun now(): String =
        SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())
}