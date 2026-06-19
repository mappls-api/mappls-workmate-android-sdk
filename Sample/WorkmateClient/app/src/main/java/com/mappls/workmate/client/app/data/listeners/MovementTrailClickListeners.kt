package com.mappls.workmate.client.app.data.listeners

import com.mappls.sdk.workmate.user.Data

interface MovementTrailClickListeners {
    fun onMovementTrailClicked(trailData: com.mappls.sdk.workmate.data.model.trails.Data)
}