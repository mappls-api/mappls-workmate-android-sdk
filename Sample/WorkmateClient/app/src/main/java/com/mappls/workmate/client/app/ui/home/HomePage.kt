package com.mappls.workmate.sample.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.mappls.workmate.client.app.data.listeners.OnAttendanceListeners
import com.mappls.workmate.client.app.data.listeners.OnWidgetClickListener
import com.mappls.workmate.client.app.data.model.Widgets
import com.mappls.workmate.client.app.util.AppConstant
import com.mappls.workmate.client.app.util.AppDialog
import com.mappls.workmate.sample.R
import com.mappls.workmate.sample.data.workmate.WorkmateManager
import com.mappls.workmate.sample.databinding.ActivityHomePageBinding
import com.mappls.workmate.sample.storage.AppSharedPreferences
import com.mappls.workmate.sample.ui.checkinout.ClientCheckInOut
import com.mappls.workmate.sample.ui.checkinout.TaskCheckInOut
import com.mappls.workmate.sample.ui.client.ClientList
import com.mappls.workmate.sample.ui.device.DeviceDetails
import com.mappls.workmate.sample.ui.distance.TotalDistanceActivity
import com.mappls.workmate.sample.ui.task.TaskList
import com.mappls.workmate.sample.ui.trail.MovementTrailsActivity
import com.mappls.workmate.sample.ui.user.UserActivity
import com.mappls.workmate.sample.ui.user.UserConfig

class HomePage : AppCompatActivity(), OnWidgetClickListener {

    private lateinit var binding: ActivityHomePageBinding
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var appSharedPreferences: AppSharedPreferences
    private lateinit var adapter: HomePageWidgetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page)
        setupUI()
    }

    private fun setupUI() {
        appSharedPreferences = AppSharedPreferences(this)
        adapter = HomePageWidgetAdapter(initWidgetList(), this)
        binding.homeWidgets.layoutManager = GridLayoutManager(this, 3)
        binding.homeWidgets.adapter = adapter
        binding.toolbar.title = "${getString(R.string.workmate)} ${getString(R.string.client)}"
        binding.userName.text = appSharedPreferences.userEmail
        setSupportActionBar(binding.toolbar)
    }

    override fun onWidgetClick(position: Int, widgetName: String) {
        when (position) {
            1 -> performWorkdayAction(isStarting = true, position)
            2 -> performWorkdayAction(isStarting = false, position)
            3 -> navigateTo(MovementTrailsActivity::class.java)
            4 -> navigateTo(TotalDistanceActivity::class.java)
            5 -> navigateTo(DeviceDetails::class.java)
            6 -> navigateTo(UserActivity::class.java)
            7 -> navigateTo(ClientCheckInOut::class.java)
            8 -> navigateTo(ClientList::class.java)
            9 -> navigateTo(TaskCheckInOut::class.java)
            10 -> navigateTo(TaskList::class.java)
        }
    }

    private var pendingAction: (() -> Unit)? = null

    private fun performWorkdayAction(isStarting: Boolean, position: Int) {
        if (onPermissionGranted()) {
            executeWorkdayAction(isStarting, position)
        } else {
            // Save the action to execute later
            pendingAction = { performWorkdayAction(isStarting, position) }
            ActivityCompat.requestPermissions(this, AppConstant.APP_PERMISSIONS, 1001)
        }
    }

    private fun executeWorkdayAction(isStarting: Boolean, position: Int) {
        showLoading()
        val action = if (isStarting) {
            { listener: OnAttendanceListeners ->
                WorkmateManager(this).startWorkDay(appSharedPreferences.accessToken, listener)
            }
        } else {
            { listener: OnAttendanceListeners ->
                WorkmateManager(this).stopWorkDay(
                    appSharedPreferences.accessToken,
                    appSharedPreferences.attendanceId,
                    activityId = 123,
                    formData = null,
                    listener
                )
            }
        }

        action(object : OnAttendanceListeners {
            override fun onAttendancePerform(attendanceId: Int?, message: String?) {
                hideLoading()
                attendanceId?.let { appSharedPreferences.attendanceId = it }
                handleAttendanceMessage(message, position, isStarting)
            }

            override fun onAttendanceError(error: String?) {
                hideLoading()
                AppDialog.showToast(
                    this@HomePage,
                    error ?: getString(R.string.error_please_try_again_later)
                )
            }
        })
    }

    private fun handleAttendanceMessage(message: String?, position: Int, isStarting: Boolean) {
        message?.let {
            adapter.updateUi(position)
            val successMessage = if (isStarting) {
                if (it.contains("Pending")) getString(R.string.workday_already_started)
                else getString(R.string.workday_started_successfully)
            } else {
                it
            }
            AppDialog.showSnackBar(binding.main, successMessage)
        } ?: run {
            AppDialog.showSnackBar(binding.main, getString(R.string.error_please_try_again_later))
        }
    }

    private fun showLoading() {
        binding.homeProgress.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.homeProgress.visibility = View.GONE
    }

    private fun navigateTo(destination: Class<*>) {
        startActivity(Intent(this, destination))
    }

    private fun initWidgetList(): MutableList<Widgets> {
        return mutableListOf(
            Widgets(1, "Start Workday", R.drawable.baseline_start_24),
            Widgets(2, "Stop Workday", R.drawable.baseline_stop_circle_24),
            Widgets(3, "Movement Trail", R.drawable.baseline_movement_trail_24),
            Widgets(4, "Calculate Distance", R.drawable.baseline_distance_24),
            Widgets(5, "Device Location", R.drawable.baseline_mobile_24),
            Widgets(6, "My Activity", R.drawable.baseline_local_activity_24),
            Widgets(7, "Client check-in-out", R.drawable.baseline_switch_account_24),
            Widgets(8, "Client list", R.drawable.sharp_accessible_menu_24),
            Widgets(9, "Task check-in-out", R.drawable.baseline_task_24),
            Widgets(10, "Task List", R.drawable.baseline_checklist_24)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.open_profile) {
            navigateTo(UserConfig::class.java)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun onPermissionGranted(): Boolean {
        return AppConstant.APP_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            val allPermissionsGranted =
                grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allPermissionsGranted) {
                pendingAction?.invoke()
                pendingAction = null
            } else {
                AppDialog.showToast(this, "Please allow all the permission.")
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        super.onBackPressed()
    }

}
