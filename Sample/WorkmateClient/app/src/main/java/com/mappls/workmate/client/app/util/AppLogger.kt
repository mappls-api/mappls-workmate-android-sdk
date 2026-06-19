package com.mappls.workmate.client.app.util

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log

@SuppressLint("LogNotTimber")
object AppLogger {

    fun showLog(log: String) {
        Log.d("Workmate Client", log)
    }

    fun showLogWithTag(tag: String, log: String) {
        Log.d(tag, log)
    }

}