package com.mappls.workmate.sample.storage

import android.content.Context
import com.mappls.workmate.client.app.util.AppConstant

class AppSharedPreferences(
    private val context: Context
) {

    //Immutable values
    private val appPrefs =
        context.getSharedPreferences(AppConstant.SHARED_PREFS, Context.MODE_PRIVATE)

    private val editor = appPrefs.edit()


    //mutable values
    var accessToken: String
        get() = appPrefs.getString("accessToken", "")!!
        set(value) {
            editor.putString("accessToken", value)
            editor.apply()
        }

    var attendanceId: Int
        get() = appPrefs.getInt("attendanceId", 0)
        set(value) {
            editor.putInt("attendanceId", value)
            editor.apply()
        }

    var userEmail: String
        get() = appPrefs.getString("userEmail", "")!!
        set(value) {
            editor.putString("userEmail", value)
            editor.apply()
        }

    var clientId: String
        get() = appPrefs.getString("clientId", "")!!
        set(value) {
            editor.putString("clientId", value)
            editor.apply()
        }

    var clientSecret: String
        get() = appPrefs.getString("clientSecret", "")!!
        set(value) {
            editor.putString("clientSecret", value)
            editor.apply()
        }

    var password: String
        get() = appPrefs.getString("password", "")!!
        set(value) {
            editor.putString("password", value)
            editor.apply()
        }

    var loggedIn: String
        get() = appPrefs.getString("loggedIn", AppConstant.LOGGED_OUT)!!
        set(value) {
            editor.putString("loggedIn", value)
            editor.apply()
        }

    override fun toString(): String {
        return "AppSharedPreferences(" +
                "accessToken='$accessToken', " +
                "attendanceId=$attendanceId, " +
                "userEmail='$userEmail', " +
                "clientId='$clientId', " +
                "clientSecret='$clientSecret', " +
                "password='$password', " +
                "loggedIn='$loggedIn'" +
                ")"
    }


    //Clearing shared preferences for session destroy
    fun destroySession() {
        editor.clear()
        editor.apply()
    }

}