package com.mappls.workmate.client.app.ui.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mappls.sdk.workmate.user.TaskData
import com.mappls.workmate.sample.R
import kotlin.collections.List
import kotlin.collections.get

class TaskAdapter(private val tasks: List<TaskData>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskText: TextView = itemView.findViewById(R.id.tv_task_name)
        val assigneeName: TextView =itemView.findViewById(R.id.tv_assignee_name)
        val clientName: TextView = itemView.findViewById(R.id.tv_client_name)
        val dueData: TextView = itemView.findViewById(R.id.tv_due_date)
        val department : TextView = itemView.findViewById(R.id.tv_department_name)
        val creationDate : TextView = itemView.findViewById(R.id.tv_creation_date)

        val taskStatus : TextView = itemView.findViewById(R.id.tv_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_data, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        if (tasks[position].status != 8)
        holder.taskText.text = tasks[position].name
        holder.assigneeName.text = "Assignee: ${tasks[position].asigneeName}"
        holder.taskStatus.text = "Status: ${tasks[position].status}"
        holder.clientName.text = "Client: ${tasks[position].clientName}"
        holder.dueData.text = "Due: ${tasks[position].dueDate}"
        holder.department.text = "Department: ${tasks[position].departmentName}"
        holder.creationDate.text = "Creation Date: ${tasks[position].creationDate}"
    }

    override fun getItemCount(): Int = tasks.size
}