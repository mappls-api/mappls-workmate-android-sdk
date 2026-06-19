[<img src="https://about.mappls.com/images/mappls-b-logo.svg" height="60"/>](https://www.mapmyindia.com/api)

# Attendance & Planning

[← Back to Documentation](README.md)

A typical field agent's day has a clear shape — it starts when they check in for work and ends when they check out. Everything in between is guided by a plan: a set of stops, clients, and tasks arranged in the most efficient order possible.

This document covers the two SDK methods that define the structure of that day. Manage Workday handles attendance — the geo-tagged start and end of a working shift. Route Optimization takes the agent's assigned stops and automatically reorders them into the most efficient sequence, saving time and reducing unnecessary travel. Together these two features give both agents and managers a shared, well-organized view of how each day should unfold.

---

## [Table of Contents]()

- [Manage Workday](#manage-workday)

---

## [Manage Workday]()

### Method: `manageWorkday`

Handles attendance tracking for field agents. Use this method to start or end a workday. The same method handles both operations — passing `null` for `attendanceId` starts a new workday, while passing the attendance ID ends it.

The attendance module tracks when agents start and end their working day. Check-ins and check-outs are geo-tagged, so organisations know not just when an agent clocked in, but where they were when they did it. Attendance history can be queried by agent or date range, making it straightforward to run reports or resolve disputes. This method is typically one of the first calls made after initialization.

> If a workday is already in progress when you call start, the method returns the existing pending workday instead of creating a new one.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `accessToken` | `String` | Access token from `initialize`. |
| `attendanceId` | `Int?` | Pass `null` to start workday. Pass the ID received from the start callback to end the workday. |
| `activityId` | `Int?` | Optional. Activity ID to associate with the workday start/end. Pass `null` if workflow is disabled. |
| `formData` | `JSONObject?` | Optional. Form data corresponding to `activityId`. Pass `null` if workflow is disabled. |

### Callbacks

| Callback | Description |
|---|---|
| `onSessionStarted(AttendanceResponse)` | Triggered when the workday starts. Returns `id`, `message`, and `status`. Save the `id` — it is required to end the workday. |
| `onSessionEnded(AttendanceResponse)` | Triggered when the workday ends. Returns `id`, `message`, and `status`. |
| `onSessionError(ErrorResponse)` | Triggered on failure. Returns error code and message. |

### Kotlin

```kotlin
// Method signature
Workmate.manageWorkday(
    context,
    @Nullable attendanceId,
    activityId,     // optional
    formData,
    token,
    object : WMSessionListener {
        override fun onSessionStarted(attendanceResponse: AttendanceResponse) {
            // Do something
            // attendanceResponse.id — save this for ending workday
        }
        override fun onSessionEnded(attendanceResponse: AttendanceResponse) {
            // Do something
        }
        override fun onSessionError(sessionError: ErrorResponse) {
            // Do something
        }
    }
)

// Example: Start workday
Workmate.manageWorkday(
    this,
    null,        // null starts the workday
    null,
    null,
    accessToken,
    object : WMSessionListener {
        override fun onSessionStarted(attendanceResponse: AttendanceResponse) {
            Log.d("Workday", "ID: ${attendanceResponse.id}")       // Save this ID
            Log.d("Workday", "Message: ${attendanceResponse.message}")
            Log.d("Workday", "Status: ${attendanceResponse.status}")
        }
        override fun onSessionEnded(attendanceResponse: AttendanceResponse) {
            // Not called in this case
        }
        override fun onSessionError(sessionError: ErrorResponse) {
            Log.d("Workday Error", sessionError.toString())
        }
    }
)

// Example: End workday
Workmate.manageWorkday(
    this,
    12345678,    // ID received from onSessionStarted
    null,
    null,
    accessToken,
    object : WMSessionListener {
        override fun onSessionStarted(attendanceResponse: AttendanceResponse) {
            // Not called in this case
        }
        override fun onSessionEnded(attendanceResponse: AttendanceResponse) {
            Log.d("Workday", "ID: ${attendanceResponse.id}")
            Log.d("Workday", "Message: ${attendanceResponse.message}")
            Log.d("Workday", "Status: ${attendanceResponse.status}")
        }
        override fun onSessionError(sessionError: ErrorResponse) {
            Log.d("Workday Error", sessionError.toString())
        }
    }
)
```

### Java

```java
// Method signature
Workmate.manageWorkday(
    context,
    attendanceId,   // null to start, ID to end
    activityId,
    formData,
    accessToken,
    new WMSessionListener() {
        @Override
        public void onSessionStarted(AttendanceResponse attendanceResponse) {
            // Do something
        }
        @Override
        public void onSessionEnded(AttendanceResponse attendanceResponse) {
            // Do something
        }
        @Override
        public void onSessionError(ErrorResponse sessionError) {
            // Do something
        }
    }
);

// Example: Start workday
Workmate.manageWorkday(
    this,
    null,
    null,
    null,
    accessToken,
    new WMSessionListener() {
        @Override
        public void onSessionStarted(AttendanceResponse attendanceResponse) {
            Log.d("Workday", attendanceResponse.getId());      // Save this ID
            Log.d("Workday", attendanceResponse.getMessage());
            Log.d("Workday", attendanceResponse.getStatus());
        }
        @Override
        public void onSessionEnded(AttendanceResponse attendanceResponse) {
            // Not called in this case
        }
        @Override
        public void onSessionError(ErrorResponse sessionError) {
            Log.d("Workday Error", String.valueOf(sessionError));
        }
    }
);

// Example: End workday
Workmate.manageWorkday(
    this,
    12345678,    // ID received from onSessionStarted
    null,
    null,
    accessToken,
    new WMSessionListener() {
        @Override
        public void onSessionStarted(AttendanceResponse attendanceResponse) {
            // Not called in this case
        }
        @Override
        public void onSessionEnded(AttendanceResponse attendanceResponse) {
            Log.d("Workday", attendanceResponse.getId());
            Log.d("Workday", attendanceResponse.getMessage());
            Log.d("Workday", attendanceResponse.getStatus());
        }
        @Override
        public void onSessionError(ErrorResponse sessionError) {
            Log.d("Workday Error", String.valueOf(sessionError));
        }
    }
);
```

---

For any queries and support, please contact:

[<img src="https://about.mappls.com/images/mappls-logo.svg" height="40"/>](https://about.mappls.com/api/)

Email us at [wmsupport@mapmyindia.com](mailto:wmsupport@mapmyindia.com)

Need support? [Contact us](https://about.mappls.com/contact/)

[<p align="center"><img src="https://www.mapmyindia.com/api/img/icons/stack-overflow.png"/>](https://stackoverflow.com/questions/tagged/mappls-api) [![](https://www.mapmyindia.com/api/img/icons/blog.png)](https://about.mappls.com/blog/) [![](https://www.mapmyindia.com/api/img/icons/gethub.png)](https://github.com/mappls-api)

<div align="center">© Copyright 2026 CE Info Systems Ltd. All Rights Reserved.</div>

<div align="center">
  <a href="https://about.mappls.com/api/terms-&-conditions">Terms & Conditions</a> |
  <a href="https://about.mappls.com/about/privacy-policy">Privacy Policy</a>
</div>
