package com.mappls.workmate.client.app.data.listeners

interface OnTokenListener {
    fun onTokenGenerated(token: String)
    fun onTokenError(error: String)
}