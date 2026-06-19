package com.mappls.workmate.sample.ui.device

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mappls.sdk.workmate.Workmate
import com.mappls.sdk.workmate.data.interfaces.WMDeviceLocationListener
import com.mappls.sdk.workmate.data.model.error.ErrorResponse
import com.mappls.sdk.workmate.user.DeviceLocationInfo
import com.mappls.workmate.client.app.util.AppDialog
import com.mappls.workmate.sample.R
import com.mappls.workmate.sample.databinding.ActivityDeviceDetailsBinding

class DeviceDetails : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_details)
        binding.getData.setOnClickListener {
            getDeviceDetails()
        }
    }

    override fun onResume() {
        super.onResume()
        getDeviceDetails()
    }

    private fun getDeviceDetails() {
        binding.progress.visibility = View.VISIBLE
        Workmate.getDeviceLocationData(this, object : WMDeviceLocationListener {
            override fun onDeviceInfoError(error: ErrorResponse) {
                binding.progress.visibility = View.GONE
                AppDialog.showToast(this@DeviceDetails, error.message ?: "Location error")
            }

            override fun onDeviceInfoSuccess(deviceLocationInfo: DeviceLocationInfo) {
                binding.progress.visibility = View.GONE
                binding.lat.text = deviceLocationInfo.latitude.toString()
                binding.lng.text = deviceLocationInfo.longitude.toString()
                binding.speed.text = deviceLocationInfo.speed.toString()
                binding.altitude.text = deviceLocationInfo.altitude.toString()
                binding.type.text = deviceLocationInfo.locationProvider
                binding.bearing.text = deviceLocationInfo.bearing.toString()
            }

        })
    }

}