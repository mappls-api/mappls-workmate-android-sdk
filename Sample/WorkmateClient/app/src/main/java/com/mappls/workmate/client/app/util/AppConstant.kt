package com.mappls.workmate.client.app.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object AppConstant {
    const val SHARED_PREFS = "WORKMATE_Client_prefs"
    const val LOGGED_IN = "LOGGED_IN"
    const val LOGGED_OUT = "LOGGED_OUT"

    //App permissions
    val APP_PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault())
        return sdf.format(calendar.time)
    }

}