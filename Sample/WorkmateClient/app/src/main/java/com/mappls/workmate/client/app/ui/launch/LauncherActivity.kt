package com.mappls.workmate.sample.ui.launch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mappls.workmate.client.app.data.listeners.OnTokenListener
import com.mappls.workmate.client.app.util.AppDialog
import com.mappls.workmate.client.app.util.AppLogger
import com.mappls.workmate.sample.R
import com.mappls.workmate.sample.data.workmate.WorkmateManager
import com.mappls.workmate.sample.databinding.ActivityLauncherBinding
import com.mappls.workmate.sample.service.AppSessionService
import com.mappls.workmate.sample.storage.AppSharedPreferences
import com.mappls.workmate.sample.ui.auth.LoginPage
import com.mappls.workmate.sample.ui.home.HomePage

class LauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLauncherBinding
    private lateinit var appSharedPreferences: AppSharedPreferences
    private lateinit var appSessionService: AppSessionService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launcher)
        appSharedPreferences = AppSharedPreferences(this)
        appSessionService = AppSessionService(appSharedPreferences)
        AppLogger.showLog(appSharedPreferences.toString())
        performLaunchOperation()
    }

    private fun performLaunchOperation() {
        /*if (appSharedPreferences.loggedIn == AppConstant.LOGGED_IN) {
            openWorkmateInitProcess()
        } else {
            openLoginPage()
        }*/
        openLoginPage()
    }

    private fun openLoginPage() {
        Handler().postDelayed({
            Intent(this, LoginPage::class.java).also {
                startActivity(it)
            }
        }, 2000)
    }

    private fun openWorkmateInitProcess() {
        binding.loginProgress.visibility = View.VISIBLE
        WorkmateManager(this).generateWorkmateToken(
            email = appSharedPreferences.userEmail,
            clientId = appSharedPreferences.clientId,
            clientSecret = appSharedPreferences.clientSecret,
            tokenListener = object : OnTokenListener {
                override fun onTokenGenerated(token: String) {
                    binding.loginProgress.visibility = View.GONE
                    //Adding value to shared preferences
                    appSessionService.performLoginOperation(
                        token = token,
                        email = appSharedPreferences.userEmail,
                        clientId = appSharedPreferences.clientId,
                        clientSecret = appSharedPreferences.clientSecret,
                    )
                    //Logging the output
                    AppLogger.showLog(token)

                    //start home activity
                    Intent(this@LauncherActivity, HomePage::class.java).also { intent ->
                        startActivity(intent)
                        finishAffinity()
                    }
                }

                override fun onTokenError(error: String) {
                    binding.loginProgress.visibility = View.GONE
                    AppDialog.showToast(this@LauncherActivity, error)
                }

            }
        )
    }

}