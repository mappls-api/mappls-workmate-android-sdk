package com.mappls.workmate.sample.ui.trail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mappls.sdk.workmate.Workmate
import com.mappls.sdk.workmate.data.interfaces.WMMovementTrailsListener
import com.mappls.sdk.workmate.data.model.error.ErrorResponse
import com.mappls.sdk.workmate.data.model.trails.Data
import com.mappls.sdk.workmate.data.model.trails.MovementTrailsResponse
import com.mappls.workmate.client.app.data.listeners.MovementTrailClickListeners
import com.mappls.workmate.client.app.ui.trail.MovementTrailAdapter
import com.mappls.workmate.client.app.util.AppDialog
import com.mappls.workmate.client.app.util.AppLogger
import com.mappls.workmate.sample.R
import com.mappls.workmate.sample.databinding.ActivityMovementTrailsBinding
import com.mappls.workmate.sample.storage.AppSharedPreferences
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MovementTrailsActivity : AppCompatActivity(), MovementTrailClickListeners {
    private lateinit var binding: ActivityMovementTrailsBinding
    private lateinit var adapter: MovementTrailAdapter
    private lateinit var appSharedPreferences: AppSharedPreferences
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var startTimeInEpoch: Long? = null
    private var endTimeInEpoch: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movement_trails)
        appSharedPreferences = AppSharedPreferences(this)

        adapter = MovementTrailAdapter(emptyList(), this)
        linearLayoutManager = LinearLayoutManager(this)
        binding.trailData.layoutManager = linearLayoutManager
        binding.trailData.adapter = adapter

        binding.startDateSelect.setOnClickListener {
            showDateTimePicker { date, time ->
                binding.startDatetime.setText("$date - $time")
                startTimeInEpoch = convertToEpoch(date, time)
            }
        }

        binding.endDateSelect.setOnClickListener {
            showDateTimePicker { date, time ->
                binding.endDatetime.setText("$date - $time")
                endTimeInEpoch = convertToEpoch(date, time)
            }
        }

        binding.fetchTrail.setOnClickListener {
            getMovementTrail()
        }

        binding.goUp.setOnClickListener {
            binding.scrollText.text = getString(R.string.top)
            binding.trailData.scrollToPosition(0)
        }

        binding.goDown.setOnClickListener {
            binding.scrollText.text = getString(R.string.bottom)
            binding.trailData.scrollToPosition(adapter.itemCount - 1)
        }

    }


    private fun showDateTimePicker(onDateTimeSelected: (String, String) -> Unit) {
        val calendar = Calendar.getInstance()

        // Date picker dialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"

                // Time picker dialog
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                        onDateTimeSelected(selectedDate, selectedTime)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }


    private fun convertToEpoch(date: String, time: String): Long {
        val dateTime = "$date $time"
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return try {
            val parsedDate = sdf.parse(dateTime)
            parsedDate?.time ?: 0L
        } catch (e: Exception) {
            AppDialog.showToast(this, "Error file converting to EPOCh")
            0L
        }
    }


    private fun getMovementTrail() {
        Workmate.getMovementTrail(
            this,
            appSharedPreferences.userEmail,
            startTimeInEpoch?.div(1000),
            endTimeInEpoch?.div(1000),
            appSharedPreferences.accessToken,
            object : WMMovementTrailsListener {
                override fun onMovementTrailsSuccess(movementTrailsResponse: MovementTrailsResponse) {
                    AppLogger.showLog(movementTrailsResponse.data.toString())
                    if (movementTrailsResponse.data.isNotEmpty()) {
                        binding.totalValue.text = "Total: ${movementTrailsResponse.data.size}"
                        adapter.updateMovementTrailResponse(movementTrailsResponse.data)
                    } else {
                        AppDialog.showToast(this@MovementTrailsActivity, "No data found")
                    }
                }

                override fun onMovementTrailsFailed(error: ErrorResponse) {
                    AppDialog.showToast(this@MovementTrailsActivity, error.message ?: "Failed to fetch trail")
                }

            }
        )
    }

    override fun onMovementTrailClicked(trailData: Data) {
        TODO("Not yet implemented")
    }

}
