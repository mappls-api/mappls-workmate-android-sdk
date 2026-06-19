package com.mappls.workmate.client.app.ui.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mappls.workmate.sample.R

class UserActivityTitleAdapter(
    private var titleList: List<String>,
    private val listener: OnUserTitleClickListener
) : RecyclerView.Adapter<UserActivityTitleAdapter.UserActivityTitleViewHolder>() {

    interface OnUserTitleClickListener {
        fun onUserTitleClicked(position: Int) // Method to handle item clicks
    }

    class UserActivityTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.user_activity_content)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserActivityTitleAdapter.UserActivityTitleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_title_list, parent, false)
        return UserActivityTitleViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UserActivityTitleAdapter.UserActivityTitleViewHolder,
        position: Int
    ) {
        holder.title.text = titleList[position]
        holder.itemView.setOnClickListener {
            listener.onUserTitleClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    fun setUserTitleList(titleList: List<String>) {
        this.titleList = titleList
        notifyDataSetChanged()
    }

}