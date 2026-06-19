package com.mappls.workmate.sample.ui.user

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mappls.workmate.client.app.data.listeners.OnTokenListener
import com.mappls.workmate.client.app.util.AppDialog
import com.mappls.workmate.client.app.util.AppLogger
import com.mappls.workmate.sample.R
import com.mappls.workmate.sample.data.workmate.WorkmateManager
import com.mappls.workmate.sample.databinding.ActivityUserConfigBinding
import com.mappls.workmate.sample.service.AppSessionService
import com.mappls.workmate.sample.storage.AppSharedPreferences

class UserConfig : AppCompatActivity() {
    private lateinit var binding: ActivityUserConfigBinding
    private lateinit var appSharedPreferences: AppSharedPreferences
    private lateinit var appSessionService: AppSessionService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_config)
        appSharedPreferences = AppSharedPreferences(this)
        appSessionService = AppSessionService(appSharedPreferences)
        binding.apply {
            clientId.setText(appSharedPreferences.clientId)
            clientSecret.setText(appSharedPreferences.clientSecret)
            userEmail.setText(appSharedPreferences.userEmail)
        }
        binding.updateBtn.setOnClickListener {
            performLoginOperation()
        }
    }

    private fun performLoginOperation() {
        performLoadingAnimation()
        WorkmateManager(this).generateWorkmateToken(
            email = binding.userEmail.text.toString(),
            clientId = binding.clientId.text.toString(),
            clientSecret = binding.clientSecret.text.toString(),
            tokenListener = object : OnTokenListener {
                override fun onTokenGenerated(token: String) {
                    performLoadingAnimation()
                    //Adding value to shared preferences
                    appSessionService.performLoginOperation(
                        token = token,
                        email = binding.userEmail.text.toString(),
                        clientId = binding.clientId.text.toString(),
                        clientSecret = binding.clientSecret.text.toString(),
                    )
                    //Logging the output
                    AppLogger.showLog(token)

                    //start home activity
                    finish()
                }

                override fun onTokenError(error: String) {
                    performLoadingAnimation()
                    AppDialog.showToast(this@UserConfig, error)
                }

            }
        )
    }

    fun performLoadingAnimation() {
        binding.submitBtnIcon.apply {
            visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        binding.submitBtnProgress.apply {
            visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

}