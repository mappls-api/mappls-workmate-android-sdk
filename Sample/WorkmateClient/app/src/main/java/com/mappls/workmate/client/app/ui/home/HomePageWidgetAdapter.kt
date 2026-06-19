package com.mappls.workmate.sample.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mappls.workmate.client.app.data.listeners.OnWidgetClickListener
import com.mappls.workmate.client.app.data.model.Widgets
import com.mappls.workmate.client.app.util.AppLogger
import com.mappls.workmate.sample.R
import kotlin.random.Random

class HomePageWidgetAdapter(
    private val widgetList: List<Widgets>,
    private val widgetClickListener: OnWidgetClickListener
) : RecyclerView.Adapter<HomePageWidgetAdapter.HomePageWidgetViewHolder>() {

    private var itemPosition = 0

    class HomePageWidgetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val actionPerform: RelativeLayout = view.findViewById(R.id.open_actions)
        val actionText: TextView = view.findViewById(R.id.action_text)
        val actionImage: ImageView = view.findViewById(R.id.action_image)
        val check: ImageView = view.findViewById(R.id.check_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageWidgetViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_widget_layout, parent, false)
        return HomePageWidgetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return widgetList.size
    }

    override fun onBindViewHolder(holder: HomePageWidgetViewHolder, position: Int) {
        val widget = widgetList[position]
        if (position > 1) {
            holder.check.visibility = View.GONE
        } else {
            if (position == itemPosition - 1) {
                holder.check.visibility = View.VISIBLE
            }
        }
        holder.actionImage.setImageResource(widget.image)
        holder.actionText.text = widget.title
        holder.actionPerform.setOnClickListener {
            AppLogger.showLogWithTag("Holder", "$itemPosition -- $position")
            widgetClickListener.onWidgetClick(
                widget.id,
                widget.title
            )
        }
    }

    fun updateUi(position: Int) {
        itemPosition = position
        notifyDataSetChanged()
    }

    private fun getRandomColor(): Int {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        return Color.rgb(red, green, blue)
    }

}
