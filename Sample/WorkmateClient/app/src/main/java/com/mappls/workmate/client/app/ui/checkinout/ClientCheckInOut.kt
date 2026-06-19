package com.mappls.workmate.sample.ui.checkinout

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mappls.sdk.workmate.Workmate
import com.mappls.sdk.workmate.data.interfaces.WMClientCheckInOutListener
import com.mappls.sdk.workmate.data.model.error.ErrorResponse
import com.mappls.sdk.workmate.user.ClientCheckInOutResponse
import com.mappls.workmate.sample.R
import com.mappls.workmate.sample.databinding.ActivityClientCheckInOutBinding
import com.mappls.workmate.sample.storage.AppSharedPreferences

class ClientCheckInOut : AppCompatActivity() {
    private lateinit var binding: ActivityClientCheckInOutBinding
    private lateinit var sharedPreferences: AppSharedPreferences
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_check_in_out)
        sharedPreferences = AppSharedPreferences(this)

        binding.toolbar.title = "Client check-in-out"

        binding.clientCheckInBtn.setOnClickListener {
            binding.checkInOutMessage.visibility = View.GONE
            binding.clientProgress.visibility = View.VISIBLE
            performClientCheckIn()
        }

        binding.clientCheckOutBtn.setOnClickListener {
            if(binding.checkInOutIdText.text.isNullOrEmpty()){
                binding.checkInOutMessage.visibility = View.VISIBLE
                binding.checkInOutMessage.setText("Please enter checkInOut id")
            } else {
                id = binding.checkInOutIdText.text.toString().toInt()
                binding.checkInOutMessage.visibility = View.GONE
                binding.clientProgress.visibility = View.VISIBLE
                performClientCheckOut()
            }

        }

    }
    //43403863   32943381


    private fun performClientCheckIn() {
        Workmate.clientCheckInOut(this,
            binding.clientIdText.text.toString().toInt(),
            null,
            null,
            sharedPreferences.accessToken,
            object : WMClientCheckInOutListener {

                override fun onClientCheckInOutFailed(error: ErrorResponse) {
                    binding.checkInOutMessage.visibility = View.VISIBLE
                    binding.clientProgress.visibility = View.GONE
                    binding.checkInOutMessage.text = "Error messages: \n ${error.message}"
                }

                override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                    binding.checkInOutMessage.visibility = View.VISIBLE
                    binding.clientProgress.visibility = View.GONE
                    id = clientCheckInOutResponse.id
                    val successMessage = String.format(
                        "%s\n\n%s\n%s\n%s",
                        "Success Message:",
                        clientCheckInOutResponse.id,
                        clientCheckInOutResponse.message,
                        clientCheckInOutResponse.status
                    )
                    binding.checkInOutMessage.text = successMessage
                }

            })
    }

    private fun performClientCheckOut() {
        if (id == null) {
            binding.checkInOutMessage.text = getString(R.string.client_id_error_string)
        } else {
            Workmate.clientCheckInOut(this,
                binding.clientIdText.text.toString().toInt(),
                id!!,
                null,
                sharedPreferences.accessToken,
                object : WMClientCheckInOutListener {
                    override fun onClientCheckInOutFailed(error: ErrorResponse) {
                        binding.checkInOutMessage.visibility = View.VISIBLE
                        binding.clientProgress.visibility = View.GONE
                        binding.checkInOutMessage.text = "Error messages: \n ${error.message}"
                    }

                    override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                        binding.clientProgress.visibility = View.GONE
                        binding.checkInOutMessage.visibility = View.VISIBLE
                        val successMessage = String.format(
                            "%s\n\n%s\n%s\n%s",
                            "Success Message:",
                            clientCheckInOutResponse.id,
                            clientCheckInOutResponse.message,
                            clientCheckInOutResponse.status
                        )
                        binding.checkInOutMessage.text = successMessage
                    }

                })
        }
    }

}