package com.mappls.workmate.sample.ui.user

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mappls.sdk.workmate.Workmate
import com.mappls.sdk.workmate.data.interfaces.WMUserActivityListener
import com.mappls.sdk.workmate.data.model.activity.UserActivityResponse
import com.mappls.sdk.workmate.data.model.error.ErrorResponse
import com.mappls.workmate.client.app.ui.user.UserActivityTitleAdapter
import com.mappls.workmate.client.app.util.AppConstant
import com.mappls.workmate.client.app.util.AppDialog
import com.mappls.workmate.sample.R
import com.mappls.workmate.sample.databinding.ActivityUserBinding
import com.mappls.workmate.sample.storage.AppSharedPreferences
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UserActivity : AppCompatActivity(), UserActivityTitleAdapter.OnUserTitleClickListener {
    private lateinit var binding: ActivityUserBinding
    private lateinit var appSharedPreferences: AppSharedPreferences
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var userActivityTitleAdapter: UserActivityTitleAdapter
    private lateinit var userActivityResponse: UserActivityResponse
    private var startTimeInEpoch: Long? = null
    private var endTimeInEpoch: Long? = null
    private var formattedData = ""

    //Adding title ad menu for user activity.
    private val userTitleList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user)
        appSharedPreferences = AppSharedPreferences(this)

        //user title recyclerview
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.activityView.layoutManager = linearLayoutManager
        userActivityTitleAdapter = UserActivityTitleAdapter(userTitleList, this)
        binding.activityView.adapter = userActivityTitleAdapter

        binding.startDateSelect.setOnClickListener {
            showDateTimePicker { date, time, sec ->
                binding.startDatetime.setText("$date - $time")
                startTimeInEpoch = convertToEpoch(date, time)
            }
        }

        binding.endDateSelect.setOnClickListener {
            showDateTimePicker { date, time, sec ->
                binding.endDatetime.setText("$date - $time")
                endTimeInEpoch = convertToEpoch(date, time)
            }
        }

        binding.fetchUserActivity.setOnClickListener {
            binding.userProgress.visibility = View.VISIBLE
            binding.userDetailsView.visibility = View.GONE
            binding.saveToFile.visibility = View.GONE
            getUserActivityData()
        }

        binding.saveToFile.setOnClickListener {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) { // Android 9 (API level 28) or lower
                if (ContextCompat.checkSelfPermission(
                        this, Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                    // Save the file in the public storage if permissions are granted
                    saveResponseToFile(
                        "UserActivity Data\n\n" + "Start_time: ${binding.startDatetime.text} - $startTimeInEpoch, \n" + "End_time: ${binding.endDatetime.text} - $endTimeInEpoch, \n\n" + "$userActivityResponse",
                        "UserActivity-${AppConstant.getCurrentTime()}.txt"
                    )
                } else {
                    // Request permissions if not granted
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ), 10001
                    )
                }
            } else {
                // For Android 10 and above, use app-specific storage directory
                saveResponseToFile(
                    formattedData, "UserActivityResponse_${AppConstant.getCurrentTime()}.txt"
                )
            }
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10001) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissions granted, save the file
                saveResponseToFile(
                    formattedData, "UserActivityResponse_${AppConstant.getCurrentTime()}.txt"
                )
            } else {
                // Permissions denied
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun getUserActivityData() {
        Workmate.getUserActivity(this,
            appSharedPreferences.userEmail,
            startTimeInEpoch,
            endTimeInEpoch,
            appSharedPreferences.accessToken,
            object : WMUserActivityListener {
                override fun onUserActivityFailed(error: ErrorResponse) {
                    binding.userProgress.visibility = View.GONE
                    AppDialog.showToast(this@UserActivity, error.message ?: "Failed to fetch activity")
                }

                override fun onUserActivitySuccess(response: UserActivityResponse) {
                    binding.userProgress.visibility = View.GONE
                    binding.userDetailsView.visibility = View.VISIBLE
                    binding.saveToFile.visibility = View.VISIBLE
                    userActivityResponse = response
                    bindUserTitleList()
                }

            })
    }

    private fun showDateTimePicker(onDateTimeSelected: (String, String, Long) -> Unit) {
        val calendar = Calendar.getInstance()

        // Date picker dialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"

                // Time picker dialog
                val timePickerDialog = TimePickerDialog(
                    this, { _, hourOfDay, minute ->
                        // Custom dialog for seconds selection
                        val secondsPickerDialog = AlertDialog.Builder(this)
                        val numberPicker = NumberPicker(this).apply {
                            minValue = 0
                            maxValue = 59
                            value = calendar.get(Calendar.SECOND)
                        }
                        secondsPickerDialog.setTitle("Select Seconds").setView(numberPicker)
                            .setPositiveButton("OK") { _, _ ->
                                val selectedSecond = numberPicker.value
                                val selectedTime = String.format(
                                    "%02d:%02d:%02d", hourOfDay, minute, selectedSecond
                                )

                                // Convert to epoch
                                val epochTime = convertToEpoch(selectedDate, selectedTime)

                                // Callback with date, time, and epoch
                                onDateTimeSelected(selectedDate, selectedTime, epochTime)
                            }.setNegativeButton("Cancel", null).show()
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
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
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return try {
            val parsedDate = sdf.parse(dateTime)
            (parsedDate?.time ?: 0L) / 1000 // Convert milliseconds to seconds
        } catch (e: Exception) {
            AppDialog.showToast(this, "Error while converting to epoch")
            0L
        }
    }


    @SuppressLint("LogNotTimber")
    fun saveResponseToFile(response: String, fileName: String) {
        try {
            val logDir: File = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                // For Android 9 and below, use public external storage
                File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    "Logs"
                )
            } else {
                // For Android 10 and above, use app-specific external storage
                File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Logs")
            }

            if (!logDir.exists()) {
                logDir.mkdirs()
            }

            val logFile = File(logDir, fileName)
            val fileOutputStream = FileOutputStream(logFile, true)
            val writer = OutputStreamWriter(fileOutputStream)
            writer.write("Response saved at: ${System.currentTimeMillis()}\n")
            writer.write(response)
            writer.write("\n\n-------------------------------\n")
            writer.close()

        } catch (e: IOException) {
            Log.e("FileSaveError", "Error saving response to file: ${e.message}")
        }
    }


    private fun convertEpochStringToTimestamp(epochString: String): String {
        val epoch = epochString.toLong() * 1000 // Convert seconds to milliseconds
        val date = Date(epoch)
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(date)
    }


    override fun onUserTitleClicked(position: Int) {
        performOperation(position)
    }


    private fun bindUserTitleList() {
        if (userTitleList.size > 1) {
            userTitleList.clear()
        }
        //adding menu items to title list
        userTitleList.add(0, "Partners")
        userTitleList.add(1, "Check-in")
        userTitleList.add(2, "Check-out")
        userTitleList.add(3, "Attendance-in")
        userTitleList.add(4, "Attendance-out")
        userTitleList.add(5, "Movement")
        userTitleList.add(6, "Travel_in")
        userTitleList.add(7, "Travel-out")
        userTitleList.add(8, "Attendance-inout")
        //Updating title list
        userActivityTitleAdapter.setUserTitleList(userTitleList)
        //initial loading
        onUserTitleClicked(0)
    }

    private fun performOperation(position: Int) {
        try {
            formattedData = when (position) {
                0 -> {
                    StringBuilder().apply {
                        append("Total partners: ${userActivityResponse.partnercreationdetails.size}\n")
                        append("Partner Details\n\n")
                        userActivityResponse.partnercreationdetails.forEachIndexed { index, partner ->
                            append("${index + 1}: ${partner.p_name}\n")
                            append("${index + 1}: ${partner.address}\n")
                            append("${index + 1}: ${partner.lat} - ${partner.lng}\n")
                            append("${index + 1}: ${convertEpochStringToTimestamp(partner.timestamp.toString())}\n\n")
                        }
                    }.toString()
                } // partners
                1 -> {
                    StringBuilder().apply {
                        append("Total Check-in: ${userActivityResponse.checkindetails.size}\n")
                        append("Check-in Details\n\n")
                        userActivityResponse.checkindetails.forEachIndexed { index, checkIn ->
                            append("${index + 1}: ${checkIn.p_name}\n")
                            append("${index + 1}: ${checkIn.address}\n")
                            append("${index + 1}: ${checkIn.lat} - ${checkIn.lng}\n")
                            append("${index + 1}: ${convertEpochStringToTimestamp(checkIn.timestamp.toString())}\n\n")
                        }
                    }.toString()
                } // Check-in
                2 -> {
                    StringBuilder().apply {
                        append("Total Check-out: ${userActivityResponse.checkoutdetails.size}\n")
                        append("Check-out Details\n\n")
                        userActivityResponse.checkoutdetails.forEachIndexed { index, checkOut ->
                            append("${index + 1}: ${checkOut.p_name}\n")
                            append("${index + 1}: ${checkOut.address}\n")
                            append("${index + 1}: ${checkOut.lat} - ${checkOut.lng}\n")
                            append("${index + 1}: ${convertEpochStringToTimestamp(checkOut.timestamp.toString())}\n\n")
                        }
                    }.toString()
                } // Check-out
                3 -> {
                    StringBuilder().apply {
                        append("Total Attendance-in: ${userActivityResponse.attendanceindetails.size}\n")
                        append("Attendance-in Details\n\n")
                        userActivityResponse.attendanceindetails.forEachIndexed { index, attendanceIn ->
                            append("${index + 1}: ${attendanceIn.address}\n")
                            append("${index + 1}: ${attendanceIn.lat} - ${attendanceIn.lng}\n")
                            append("${index + 1}: ${convertEpochStringToTimestamp(attendanceIn.timestamp.toString())}\n\n")
                        }
                    }.toString()
                } // Attendance-in
                4 -> {
                    StringBuilder().apply {
                        append("Total Attendance-out: ${userActivityResponse.attendanceoutdetails.size}\n")
                        append("Attendance-out Details\n\n")
                        userActivityResponse.attendanceoutdetails.forEachIndexed { index, attendanceOut ->
                            append("${index + 1}: ${attendanceOut.address}\n")
                            append("${index + 1}: ${attendanceOut.lat} - ${attendanceOut.lng}\n")
                            append("${index + 1}: ${convertEpochStringToTimestamp(attendanceOut.timestamp.toString())}\n\n")
                        }
                    }.toString()
                } // Attendance-out
                5 -> {
                    StringBuilder().apply {
                        append("Total Movement: ${userActivityResponse.movementData.size}\n")
                        append(
                            "Start_time: ${binding.startDatetime.text} - $startTimeInEpoch, \n" + "End_time: ${binding.endDatetime.text} - $endTimeInEpoch, \n\n"
                        )
                        userActivityResponse.movementData.forEachIndexed { index, movementDetails ->
                            append("${index + 1}: ${movementDetails.address}\n")
                            append("${index + 1}: ${movementDetails.speed}\n")
                            append("${index + 1}: ${movementDetails.latitude} - ${movementDetails.longitude}\n")
                            append("${index + 1}: ${convertEpochStringToTimestamp(movementDetails.timestamp.toString())}\n\n")
                        }
                    }.toString()
                } // Movement Data
                6 -> {
                    StringBuilder().apply {
                        append("Total Travel-in: ${userActivityResponse.travelindetailsList.size}\n")
                        append("travel-in Details\n\n")
                        userActivityResponse.travelindetailsList.forEachIndexed { index, travelInDetails ->
                            append("${index + 1}: ${travelInDetails.address}\n")
                            append("${index + 1}: ${travelInDetails.lat} - ${travelInDetails.lng}\n")
                            append("${index + 1}: ${convertEpochStringToTimestamp(travelInDetails.timestamp.toString())}\n\n")
                        }
                    }.toString()
                } // Travel-in
                7 -> {
                    StringBuilder().apply {
                        append("Total Travel-out: ${userActivityResponse.traveloutdetailsList.size}\n")
                        append("Travel-out Details\n\n")
                        userActivityResponse.traveloutdetailsList.forEachIndexed { index, travelOutDetails ->
                            append("${index + 1}: ${travelOutDetails.address}\n")
                            append("${index + 1}: ${travelOutDetails.lat} - ${travelOutDetails.lng}\n")
                            append("${index + 1}: ${convertEpochStringToTimestamp(travelOutDetails.timestamp.toString())}\n\n")
                        }
                    }.toString()
                } // Travel-out
                8 -> {
                    StringBuilder().apply {
                        append("Total Attendance-in-out: ${userActivityResponse.attendanceInOutList.size}\n")
                        append("Attendance-in-out Details\n\n")
                        userActivityResponse.attendanceoutdetails.forEachIndexed { index, attendanceInOut ->
                            append("${index + 1}: ${attendanceInOut.address}\n")
                            append("${index + 1}: ${attendanceInOut.lat} - ${attendanceInOut.lng}\n")
                            append("${index + 1}: ${convertEpochStringToTimestamp(attendanceInOut.timestamp.toString())}\n\n")
                        }
                    }.toString()
                } // Attendance-in-out
                else -> {
                    "No data found."
                }
            }
            binding.userActivityDetailsText.text = formattedData
        } catch (ex: Exception) {
            Toast.makeText(this, ex.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }


}