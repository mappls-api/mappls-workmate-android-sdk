package com.mappls.workmate.sample.ui.distance

//import com.mappls.sdk.workmate.data.model.error.ErrorResponse
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mappls.sdk.workmate.Workmate
import com.mappls.sdk.workmate.data.interfaces.WMCalculateDistanceListener
import com.mappls.sdk.workmate.data.model.trails.CalculateDistanceResponse
import com.mappls.workmate.client.app.util.AppDialog
import com.mappls.workmate.sample.R
import com.mappls.workmate.sample.databinding.ActivityTotalDistanceBinding
import com.mappls.workmate.sample.storage.AppSharedPreferences
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TotalDistanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTotalDistanceBinding
    private lateinit var appSharedPreferences: AppSharedPreferences
    private var startTimeInEpoch: Long? = null
    private var endTimeInEpoch: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_total_distance)
        appSharedPreferences = AppSharedPreferences(this)

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

        binding.fetchTotalDistance.setOnClickListener {
            getTotalDistance()
        }

    }

    private fun getTotalDistance() {
        Workmate.calculateDistance(this,
            appSharedPreferences.userEmail,
            1,
            startTimeInEpoch?.div(1000),
            endTimeInEpoch?.div(1000),
            appSharedPreferences.accessToken,
            object : WMCalculateDistanceListener {
                override fun onCalculateDistanceSuccess(calculateDistanceResponse: CalculateDistanceResponse?) {
                    binding.distance.text = calculateDistanceResponse?.distance.toString()
                }

                override fun onCalculateDistanceFailed(error: String) {
                    AppDialog.showToast(this@TotalDistanceActivity, error)
                }

                /*override fun onCalculateDistanceFailed(error: ErrorResponse) {
                    TODO("Not yet implemented")
                }*/

            })
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

}