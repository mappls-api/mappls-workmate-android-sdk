package com.mappls.workmate.sample.ui.client

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mappls.sdk.workmate.Workmate
import com.mappls.sdk.workmate.data.interfaces.client.WMClientListListener
import com.mappls.sdk.workmate.data.model.client.ClientDetails
import com.mappls.sdk.workmate.data.model.error.ErrorResponse
import com.mappls.workmate.client.app.ui.client.ClientAdapter
import com.mappls.workmate.client.app.util.AppDialog
import com.mappls.workmate.sample.R
import com.mappls.workmate.sample.databinding.ActivityClientListBinding
import com.mappls.workmate.sample.storage.AppSharedPreferences

class ClientList:AppCompatActivity() {
    private lateinit var binding: ActivityClientListBinding
    private lateinit var appSharedPreferences: AppSharedPreferences
    private lateinit var adapter: ClientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_list)
        appSharedPreferences = AppSharedPreferences(this)
        binding.progress.visibility = View.VISIBLE

        Workmate.getClientList(this
            ,appSharedPreferences.accessToken
            ,object : WMClientListListener {

                override fun onClientListError(errorResponse: ErrorResponse) {
                    binding.progress.visibility = View.GONE
                    AppDialog.showToast(this@ClientList, errorResponse.toString())
                }

                override fun onClientListSuccess(clientList: List<ClientDetails>) {
                    adapter= ClientAdapter(clientList)
                    binding.toolbar.title = "Clients (${clientList.size})"
                    binding.progress.visibility = View.GONE
                    binding.recyclerView.layoutManager = LinearLayoutManager(this@ClientList)
                    binding.recyclerView.adapter =  adapter
                }
            })
    }
}