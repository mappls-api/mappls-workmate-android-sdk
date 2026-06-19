package com.mappls.workmate.sample.data.workmate

import android.app.Activity
import android.widget.Toast
import com.mappls.sdk.workmate.Workmate
import com.mappls.sdk.workmate.data.interfaces.WMAuthListener
import com.mappls.sdk.workmate.data.interfaces.WMCalculateDistanceListener
import com.mappls.sdk.workmate.data.interfaces.WMMovementTrailsListener
import com.mappls.sdk.workmate.data.interfaces.WMSessionListener
import com.mappls.sdk.workmate.data.model.trails.CalculateDistanceResponse
import com.mappls.sdk.workmate.data.model.trails.MovementTrailsResponse
import com.mappls.sdk.workmate.data.model.error.ErrorResponse
import com.mappls.sdk.workmate.user.AttendanceResponse
import com.mappls.sdk.workmate.user.AuthResponse
import com.mappls.workmate.client.app.data.listeners.OnAttendanceListeners
import com.mappls.workmate.client.app.data.listeners.OnTokenListener

import org.json.JSONObject

class WorkmateManager(
    private val activity: Activity
) {

    //Generate Access token
    fun generateWorkmateToken(
        email: String,
        clientId: String,
        clientSecret: String,
        tokenListener: OnTokenListener
    ) {
        Workmate.initialize(activity,
            clientSecret,
            clientId,
            email,
            object : WMAuthListener {
                override fun onAuthSuccess(response: AuthResponse) {
                    response.accessToken?.let {
                        tokenListener.onTokenGenerated(it)
                    } ?: tokenListener.onTokenError("Null value received")

                }

                override fun onAuthFailure(errorResponse: ErrorResponse) {
                    tokenListener.onTokenError(errorResponse.message ?: "Authentication failed")
                }
            })
    }

    //Start Workday
    fun stopWorkDay(
        token: String,
        attendanceId: Int?,
        activityId: Int?,
        formData: JSONObject?,
        attendanceListeners: OnAttendanceListeners
    ) {
        Workmate.manageWorkday(activity,
            attendanceId,
            activityId,
            formData,
            token,
            object : WMSessionListener {
                override fun onSessionEnded(attendanceResponse: AttendanceResponse) {
                    attendanceListeners.onAttendancePerform(
                        attendanceResponse.id, attendanceResponse.message
                    )
                }

                override fun onSessionError(sessionError: ErrorResponse) {
                    attendanceListeners.onAttendanceError(sessionError.message)
                }

                override fun onSessionStarted(attendanceResponse: AttendanceResponse) {
                    attendanceListeners.onAttendancePerform(
                        attendanceResponse.id, attendanceResponse.message
                    )
                }

            })
    }

    //Stop work day
    fun startWorkDay(token: String, attendanceListeners: OnAttendanceListeners) {
        Workmate.manageWorkday(activity,
            attendanceId = null,
            activityId = null,
            formData = null,
            token,
            object : WMSessionListener {
                override fun onSessionEnded(attendanceResponse: AttendanceResponse) {
                    attendanceListeners.onAttendancePerform(
                        attendanceResponse.id, attendanceResponse.message
                    )
                }

                override fun onSessionError(sessionError: ErrorResponse) {
                    attendanceListeners.onAttendanceError(sessionError.message)
                }

                override fun onSessionStarted(attendanceResponse: AttendanceResponse) {
                    attendanceListeners.onAttendancePerform(
                        attendanceResponse.id, attendanceResponse.message
                    )
                }

            })
    }

    fun getMovementTrail(
        email: String, start: Long?, end: Long?, token: String?
    ) {
        Workmate.getMovementTrail(activity,
            email,
            start,
            end,
            token,
            object : WMMovementTrailsListener {
                override fun onMovementTrailsFailed(error: ErrorResponse) {
                    Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
                }

                override fun onMovementTrailsSuccess(movementTrailsResponse: MovementTrailsResponse) {
                    Toast.makeText(
                        activity, movementTrailsResponse.data.toString(), Toast.LENGTH_LONG
                    ).show()
                }

            })
    }


    fun getDistance(
        email: String, start: Long?, end: Long?, token: String?
    ) {
        Workmate.calculateDistance(activity,
            email,
            1,
            start,
            end,
            token,
            object : WMCalculateDistanceListener {
                override fun onCalculateDistanceSuccess(calculateDistanceResponse: CalculateDistanceResponse?) {
                    Toast.makeText(
                        activity, calculateDistanceResponse?.distance.toString(), Toast.LENGTH_LONG
                    ).show()
                }

                override fun onCalculateDistanceFailed(error: ErrorResponse) {
                    Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
                }

            })
    }

}