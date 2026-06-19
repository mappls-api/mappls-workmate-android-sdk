package com.mappls.workmate.sample.ui.task

//import com.mappls.sdk.workmate.data.model.error.ErrorResponse
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mappls.sdk.workmate.Workmate
import com.mappls.sdk.workmate.data.interfaces.task.WMTaskListListener
import com.mappls.sdk.workmate.data.model.error.ErrorResponse
import com.mappls.sdk.workmate.user.TaskData
import com.mappls.workmate.client.app.ui.task.TaskAdapter
import com.mappls.workmate.client.app.util.AppDialog
import com.mappls.workmate.sample.R
import com.mappls.workmate.sample.databinding.ActivityTaskListBinding
import com.mappls.workmate.sample.storage.AppSharedPreferences

class TaskList:AppCompatActivity() {
    private lateinit var binding: ActivityTaskListBinding
    private lateinit var appSharedPreferences: AppSharedPreferences
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_list)
        appSharedPreferences = AppSharedPreferences(this)
        binding.progress.visibility = View.VISIBLE

        Workmate.getTaskList(this
            ,appSharedPreferences.accessToken
            ,object :WMTaskListListener{

            override fun onTaskListError(error: ErrorResponse) {
                binding.progress.visibility = View.GONE
                AppDialog.showToast(this@TaskList, error.toString())
            }

            override fun onTaskListSuccess(taskList: List<TaskData>) {
                val updatedList = taskList.filter { it.status != 8 && it.status != 7 && it.status != 6 }
                adapter=TaskAdapter(updatedList)
                binding.toolbar.title = "Tasks (${updatedList.size})"
                binding.progress.visibility = View.GONE
                binding.recyclerView.layoutManager = LinearLayoutManager(this@TaskList)
                binding.recyclerView.adapter = adapter
            }
        })
    }
}