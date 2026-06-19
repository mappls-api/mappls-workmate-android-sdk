package com.mappls.workmate.client.app.ui.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mappls.sdk.workmate.data.model.client.ClientDetails
import com.mappls.workmate.sample.R

class ClientAdapter(private val tasks: List<ClientDetails>):RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {
    inner class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val clientText: TextView = itemView.findViewById(R.id.tv_client_name)
        val userId: TextView =itemView.findViewById(R.id.tv_user_id)
        val address: TextView = itemView.findViewById(R.id.tv_address)
        val email: TextView = itemView.findViewById(R.id.tv_email)
        val latLng : TextView = itemView.findViewById(R.id.tv_lat_lng)
        val creationDate : TextView = itemView.findViewById(R.id.tv_creation_date)

        val clientId : TextView = itemView.findViewById(R.id.tv_client_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_client_data, parent, false)
        return ClientViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.clientText.text = tasks[position].name
        holder.clientId.text = tasks[position].id.toString()
        holder.latLng.text = "LatLng: ${tasks[position].lat}, ${tasks[position].lng}"
        holder.userId.text = "UserId: ${tasks[position].userId}"
        holder.email.text = "Email: ${tasks[position].emailId}"
        holder.address.text = "Address: ${tasks[position].address}"
        holder.creationDate.text = "CreationDate: ${tasks[position].creationTime}"
    }
}