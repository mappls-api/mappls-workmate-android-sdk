package com.mappls.workmate.client.app.data.listeners

interface OnAttendanceListeners {
    fun onAttendancePerform(attendanceId: Int?, message: String?)
    fun onAttendanceError(error: String?)
}