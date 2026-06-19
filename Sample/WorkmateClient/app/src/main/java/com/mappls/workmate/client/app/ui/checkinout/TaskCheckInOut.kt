package com.mappls.workmate.sample.ui.checkinout

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mappls.sdk.workmate.Workmate
import com.mappls.sdk.workmate.data.interfaces.WMClientCheckInOutListener
import com.mappls.sdk.workmate.data.interfaces.WMTaskCheckInOutListener
import com.mappls.sdk.workmate.data.model.error.ErrorResponse
//import com.mappls.sdk.workmate.data.model.error.ErrorResponse
//import com.mappls.sdk.workmate.data.interfaces.WMTaskCheckInOutListener
import com.mappls.sdk.workmate.user.ClientCheckInOutResponse
import com.mappls.workmate.sample.R
import com.mappls.workmate.sample.databinding.ActivityTaskCheckInOutBinding
import com.mappls.workmate.sample.storage.AppSharedPreferences
import timber.log.Timber

class TaskCheckInOut:AppCompatActivity() {

    private lateinit var binding: ActivityTaskCheckInOutBinding
    private lateinit var sharedPreferences: AppSharedPreferences
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_check_in_out)
        sharedPreferences = AppSharedPreferences(this)

        binding.toolbar.title = "Task check-in-out"

        binding.taskCheckInBtn.setOnClickListener {
            binding.taskCheckInOutMessage.visibility = View.GONE
            binding.taskProgress.visibility = View.VISIBLE
            performTaskCheckIn()
        }

        binding.taskCheckOutBtn.setOnClickListener {
            if(binding.clientCheckinoutIdText.text.isNullOrEmpty()){
                binding.taskCheckInOutMessage.visibility = View.VISIBLE
                binding.taskCheckInOutMessage.setText("Please enter checkInOut id")
            } else {
                id = binding.clientCheckinoutIdText.text.toString().toInt()
                binding.taskCheckInOutMessage.visibility = View.GONE
                binding.taskProgress.visibility = View.VISIBLE
                performTaskCheckOut()
            }

        }

    }


    private fun performTaskCheckIn() {
        if(binding.clientIdText.text.toString().isNullOrEmpty() ){
            binding.taskProgress.visibility = View.GONE
            binding.taskCheckInOutMessage.visibility = View.VISIBLE
            binding.taskCheckInOutMessage.text = getString(R.string.client_id_error)
        }else if(binding.taskIdText.text.toString().isNullOrEmpty()){
            binding.taskProgress.visibility = View.GONE
            binding.taskCheckInOutMessage.visibility = View.VISIBLE
            binding.taskCheckInOutMessage.text = getString(R.string.task_id_error)
        }else{
            Workmate.taskCheckIn(this,
                clientId = binding.clientIdText.text.toString().toInt(),
                taskId = binding.taskIdText.text.toString(),
                null,
                sharedPreferences.accessToken,
                object : WMTaskCheckInOutListener {

                    override fun onTaskCheckInOutFailed(error: ErrorResponse) {
                        binding.taskCheckInOutMessage.visibility = View.VISIBLE
                        binding.taskProgress.visibility = View.GONE
                        binding.taskCheckInOutMessage.text = "Error messages: \n $error"
                    }


                    override fun onTaskCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                        binding.taskCheckInOutMessage.visibility = View.VISIBLE
                        binding.taskProgress.visibility = View.GONE
                        id = clientCheckInOutResponse.id
                        val successMessage = String.format(
                            "%s\n\n%s\n%s\n%s",
                            "Success Message:",
                            clientCheckInOutResponse.id,
                            clientCheckInOutResponse.message,
                            clientCheckInOutResponse.status
                        )
                        binding.taskCheckInOutMessage.text = successMessage
                    }

                })
        }
    }

    private fun performTaskCheckOut() {
        Timber.tag("taskCheckOutBtnTest").d("taskCheckOutBtnTest2")
        if (id == null) {
            Timber.tag("taskCheckOutBtnTest").d("taskCheckOutBtnTest3")
            binding.taskCheckInOutMessage.text = getString(R.string.task_id_error_string)
        } else {
            Timber.tag("taskCheckOutBtnTest").d("taskCheckOutBtnTest4")
            Workmate.taskCheckInAndOut(this,
                binding.clientIdText.text.toString().toInt(),
                binding.taskIdText.text.toString(),
                id,
                null,
                sharedPreferences.accessToken,
                object : WMClientCheckInOutListener {
                    override fun onClientCheckInOutFailed(error: ErrorResponse) {
                        binding.taskCheckInOutMessage.visibility = View.VISIBLE
                        binding.taskProgress.visibility = View.GONE
                        binding.taskCheckInOutMessage.text = "Error messages: \n ${error.message}"
                    }

                    override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                        binding.taskProgress.visibility = View.GONE
                        binding.taskCheckInOutMessage.visibility = View.VISIBLE
                        val successMessage = String.format(
                            "%s\n\n%s\n%s\n%s",
                            "Success Message:",
                            clientCheckInOutResponse.id,
                            clientCheckInOutResponse.message,
                            clientCheckInOutResponse.status
                        )
                        binding.taskCheckInOutMessage.text = successMessage
                    }

                })
        }
    }

}