package com.mappls.workmate.sample.service

import com.mappls.workmate.client.app.util.AppConstant
import com.mappls.workmate.sample.storage.AppSharedPreferences

class AppSessionService(
    private val appSharedPreferences: AppSharedPreferences
) {

    fun performLoginOperation(
        token: String,
        email: String,
        clientId: String,
        clientSecret: String,
    ) {
        appSharedPreferences.accessToken = token
        appSharedPreferences.userEmail = email
        appSharedPreferences.clientId = clientId
        appSharedPreferences.clientSecret = clientSecret
        appSharedPreferences.loggedIn = AppConstant.LOGGED_IN
    }

    fun performLogoutOperation() {
        appSharedPreferences.destroySession()
    }

}