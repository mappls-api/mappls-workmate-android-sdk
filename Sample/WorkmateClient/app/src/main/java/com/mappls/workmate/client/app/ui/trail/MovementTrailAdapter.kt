package com.mappls.workmate.client.app.ui.trail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mappls.sdk.workmate.data.model.trails.Data
import com.mappls.workmate.client.app.data.listeners.MovementTrailClickListeners
import com.mappls.workmate.sample.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovementTrailAdapter(
    private var response: List<Data>,
    private val listeners: MovementTrailClickListeners
) : RecyclerView.Adapter<MovementTrailAdapter.MovementTrailViewHolder>() {
    class MovementTrailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addressText: TextView = itemView.findViewById(R.id.address)
        val trailLatLng: TextView = itemView.findViewById(R.id.lat_lng)
        val trailTime: TextView = itemView.findViewById(R.id.trail_time)
        val trailSpeed: TextView = itemView.findViewById(R.id.speed)
        val count: TextView = itemView.findViewById(R.id.count)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovementTrailViewHolder {
        val trailView = LayoutInflater.from(parent.context).inflate(
            R.layout.movement_trail,
            parent,
            false
        )
        return MovementTrailViewHolder(trailView)
    }

    override fun onBindViewHolder(
        holder: MovementTrailViewHolder,
        position: Int
    ) {
        val trailData = response[position]
        holder.count.text = "${position + 1}"
        holder.trailTime.text = convertEpochStringToTimestamp(trailData.timestamp.toString())
        holder.trailLatLng.text = "${trailData.latitude} - ${trailData.longitude}"
        holder.addressText.text = trailData.address.toString()
        holder.trailSpeed.text = trailData.speed.toString()
        holder.itemView.setOnClickListener {
            listeners.onMovementTrailClicked(trailData)
        }
    }

    override fun getItemCount(): Int {
        return response.size
    }

    fun updateMovementTrailResponse(data: List<Data>) {
        response = data
        notifyDataSetChanged()
    }

    private fun convertEpochStringToTimestamp(epochString: String): String {
        val epoch = epochString.toLong() * 1000 // Convert seconds to milliseconds
        val date = Date(epoch)
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(date)
    }

}