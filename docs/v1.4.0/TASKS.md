[<img src="https://about.mappls.com/images/mappls-b-logo.svg" height="60"/>](https://www.mapmyindia.com/api)

# Tasks

[← Back to Documentation](README.md)

Tasks are the unit of work in Workmate SDK. Managers create tasks, assign them to specific agents, and track them through a clear lifecycle — from assignment, to in-progress, to completion. Agents can update task status, add notes, and attach relevant information as they work. The tasks module gives both managers and agents a shared view of what needs to be done, what's in progress, and what's been completed.

This document covers the methods for retrieving an agent's task list, logging the start of a task via check-in, and recording its completion via check-out. Each check-in and check-out captures the agent's location and timestamp, creating a precise record that feeds into reporting and client history.

---

## [Table of Contents]()

- [Get Task List](#get-task-list)
- [Task Check-In](#task-check-in)
- [Task Check-Out](#task-check-out)
- [Task Check-In/Out (Deprecated)](#task-check-inout-deprecated)

---

## [Get Task List]()

### Method: `getTaskList`

Retrieves all tasks assigned to the authenticated user.

Call this method to fetch the agent's current task list. This is typically the first step in any task workflow — the agent sees what's been assigned to them for the day, selects a task to work on, and then proceeds to check in. The returned `TaskData` objects contain the task details your app needs to build a task queue UI or to look up the correct `taskId` for check-in operations.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `accessId` | `String` | Access token from `initialize`. |

### Callbacks

| Callback | Description |
|---|---|
| `onTaskListSuccess(List<TaskData>)` | Returns a list of `TaskData` objects representing available tasks. |
| `onTaskListError(ErrorResponse)` | Returns error code and message on failure. |

### Kotlin

```kotlin
// Method signature
Workmate.getTaskList(
    context, token,
    object : WMTaskListListener {
        override fun onTaskListSuccess(taskList: List<TaskData>) {
            // Do something
        }
        override fun onTaskListError(errorResponse: ErrorResponse) {
            // Do something
        }
    }
)

// Example
Workmate.getTaskList(
    this, token,
    object : WMTaskListListener {
        override fun onTaskListSuccess(taskList: List<TaskData>) {
            Log.d("Tasks", taskList.toString())
        }
        override fun onTaskListError(error: ErrorResponse) {
            Log.d("Task List Error", error.toString())
        }
    }
)
```

### Java

```java
// Method signature
Workmate.getTaskList(
    context, token,
    new WMTaskListListener() {
        @Override
        public void onTaskListSuccess(List<TaskData> taskList) {
            // Do something
        }
        @Override
        public void onTaskListError(ErrorResponse error) {
            // Do something
        }
    }
);

// Example
Workmate.getTaskList(
    this, token,
    new WMTaskListListener() {
        @Override
        public void onTaskListSuccess(List<TaskData> taskList) {
            Log.d("Tasks", taskList.toString());
        }
        @Override
        public void onTaskListError(ErrorResponse error) {
            Log.d("Task List Error", error.toString());
        }
    }
);
```

---

## [Task Check-In]()

### Method: `taskCheckIn`

Logs the start of a task by submitting the task ID, location, and timestamp to the server. Returns a `taskCheckId` in the success callback — save this value as it is required for `taskCheckOut`.

When an agent begins working on a task, this method records that the task is now in progress. The check-in is geo-tagged so managers can confirm the agent was at the right location when they started. If your organisation uses task workflows (for example, a pre-task form or required activity), you can attach form data to the check-in. The `taskCheckId` returned on success links this check-in to its check-out — keep it in memory until the task is complete.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `clientId` | `Int` | Unique identifier for the client associated with this task. |
| `taskId` | `String` | Unique identifier for the task. |
| `formData` | `JSONObject?` | Form data for the check-in workflow. Pass `null` if workflow is disabled. |
| `accessToken` | `String` | Access token from `initialize`. |

### Callbacks

| Callback | Description |
|---|---|
| `onTaskCheckInOutSuccess(ClientCheckInOutResponse)` | Check-in succeeded. Returns `id` (the `taskCheckId`) — save this for check-out. |
| `onTaskCheckInOutFailed(ErrorResponse)` | Check-in failed. Returns error code and message. |

### Kotlin

```kotlin
// Method signature
Workmate.taskCheckIn(
    context,
    clientId,
    taskId,
    formData,
    accessToken,
    object : WMTaskCheckInOutListener {
        override fun onTaskCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
            // Do something
        }
        override fun onTaskCheckInOutFailed(error: String) {
            // Do something
        }
        override fun onTaskCheckInOutFailed(error: ErrorResponse) {
            // Do something
        }
    }
)

// Example
Workmate.taskCheckIn(
    this,
    1234567,  // Client ID from Workmate
    13,       // Task ID from Workmate
    null,     // formData — null if workflow disabled
    null,
    accessToken,
    object : WMTaskCheckInOutListener {
        override fun onTaskCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
            Log.d("Task Check-In", "${clientCheckInOutResponse.id}") // Save this taskCheckId
        }
        override fun onTaskCheckInOutFailed(error: String) {
            Log.d("Task Error", error)
        }
        override fun onTaskCheckInOutFailed(error: ErrorResponse) {
            Log.d("Task Error", String.valueOf(error.getId()))
        }
    }
)
```

### Java

```java
// Method signature
Workmate.taskCheckIn(
    context, clientId, taskId, formData, token,
    new WMTaskCheckInOutListener() {
        @Override
        public void onTaskCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
            // Do something
        }
        @Override
        public void onTaskCheckInOutFailed(ErrorResponse error) {
            // Do something
        }
    }
);

// Example
Workmate.taskCheckIn(
    this,
    1234567,  // Client ID from Workmate
    13,       // Task ID from Workmate
    null,
    accessToken,
    new WMTaskCheckInOutListener() {
        @Override
        public void onTaskCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
            Log.d("Task Check-In", String.valueOf(clientCheckInOutResponse.getId())); // Save this taskCheckId
        }
        @Override
        public void onTaskCheckInOutFailed(ErrorResponse error) {
            Log.d("Task Error", String.valueOf(error));
        }
    }
);
```

---

## [Task Check-Out]()

### Method: `taskCheckOut`

Logs the completion of a task. Requires the `taskCheckId` returned by `taskCheckIn`.

When the agent finishes a task, this method marks it complete and records the departure location and timestamp. Together with `taskCheckIn`, it produces a full task execution record — duration, start location, end location — visible to both the agent and their manager. Pass the `taskCheckId` from the corresponding check-in to correctly link the completion to the task.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `clientId` | `Int` | Unique identifier for the client associated with this task. |
| `taskId` | `String` | Unique identifier for the task. |
| `taskCheckId` | `Int` | The check-in ID returned by `onTaskCheckInOutSuccess` during check-in. |
| `formData` | `JSONObject?` | Form data for the check-out workflow. Pass `null` if workflow is disabled. |
| `accessToken` | `String` | Access token from `initialize`. |

### Callbacks

| Callback | Description |
|---|---|
| `onTaskCheckInOutSuccess(ClientCheckInOutResponse)` | Check-out succeeded. Returns operation status and details. |
| `onTaskCheckInOutFailed(ErrorResponse)` | Check-out failed. Returns error code and message. |

### Kotlin

```kotlin
// Method signature
Workmate.taskCheckOut(
    context,
    clientId,
    taskId,
    taskCheckId,
    formData,
    accessToken,
    object : WMTaskCheckInOutListener {
        override fun onTaskCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
            // Do something
        }
        override fun onTaskCheckInOutFailed(error: ErrorResponse) {
            // Do something
        }
    }
)

// Example
Workmate.taskCheckOut(
    this,
    1234567,   // Client ID from Workmate
    13,        // Task ID from Workmate
    11221122,  // taskCheckId from check-in success callback
    null,
    accessToken,
    taskCheckInOutListener = object : WMTaskCheckInOutListener {
        override fun onTaskCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
            Log.d("Task Check-Out", "${clientCheckInOutResponse.id}")
        }
        override fun onTaskCheckInOutFailed(error: ErrorResponse) {
            Log.d("Task Error", error.toString())
        }
    }
)
```

### Java

```java
// Method signature
Workmate.taskCheckOut(
    context, clientId, taskId, taskCheckId, formData, token,
    new WMTaskCheckInOutListener() {
        @Override
        public void onTaskCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
            // Do something
        }
        @Override
        public void onTaskCheckInOutFailed(ErrorResponse error) {
            // Do something
        }
    }
);

// Example
Workmate.taskCheckOut(
    this,
    1234567,   // Client ID from Workmate
    13,        // Task ID from Workmate
    11221122,  // taskCheckId from check-in success callback
    null,
    accessToken,
    new WMTaskCheckInOutListener() {
        @Override
        public void onTaskCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
            Log.d("Task Check-Out", String.valueOf(clientCheckInOutResponse.getId()));
        }
        @Override
        public void onTaskCheckInOutFailed(String error) {
            Log.d("Task Error", error);
        }
        @Override
        public void onTaskCheckInOutFailed(ErrorResponse error) {
            Log.d("Task Error", String.valueOf(error));
        }
    }
);
```

---

## [Task Check-In/Out (Deprecated)]()

> **Deprecated:** This method is no longer recommended. Use [`taskCheckIn`](#task-check-in) and [`taskCheckOut`](#task-check-out) instead.

### Method: `taskCheckInAndOut`

A combined method that previously handled both task check-in and check-out in a single call. Pass `null` for `taskCheckId` to check in, or pass the ID received from a previous check-in to check out.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `clientId` | `Int` | Unique identifier for the client associated with this task. |
| `taskId` | `String` | Unique identifier for the task. |
| `taskCheckId` | `Int?` | Pass `null` for check-in. Pass the ID from a previous check-in for check-out. |
| `formData` | `JSONObject?` | Form data for the workflow. Pass `null` if workflow is disabled. |
| `accessToken` | `String` | Access token from `initialize`. |

### Callbacks

| Callback | Description |
|---|---|
| `onClientCheckInOutSuccess(ClientCheckInOutResponse)` | Operation succeeded. Returns the `taskCheckId`. |
| `onClientCheckInOutFailed(ErrorResponse)` | Operation failed. Returns error code and message. |

### Kotlin

```kotlin
// Check-in example
Workmate.taskCheckInAndOut(
    this,
    1234567,  // Client ID
    13,       // Task ID
    null,     // null for check-in
    null,
    accessToken,
    object : WMClientCheckInOutListener {
        override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
            Log.d("Task", "${clientCheckInOutResponse.id}") // Save this for check-out
        }
        override fun onClientCheckInOutFailed(error: ErrorResponse) {
            Log.d("Task Error", error.toString())
        }
    }
)

// Check-out example
Workmate.taskCheckInAndOut(
    this,
    1234567,
    13,
    11221122,  // taskCheckId from check-in
    null,
    accessToken,
    clientCheckInOutListener = object : WMClientCheckInOutListener {
        override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
            Log.d("Task", "${clientCheckInOutResponse.id}")
        }
        override fun onClientCheckInOutFailed(error: ErrorResponse) {
            Log.d("Task Error", error.toString())
        }
    }
)
```

### Java

```java
// Check-in example
Workmate.taskCheckInAndOut(
    this,
    1234567,
    13,
    null,
    null,
    accessToken,
    new WMClientCheckInOutListener() {
        @Override
        public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
            Log.d("Task", String.valueOf(clientCheckInOutResponse.getId()));
        }
        @Override
        public void onClientCheckInOutFailed(ErrorResponse error) {
            Log.d("Task Error", String.valueOf(error));
        }
    }
);

// Check-out example
Workmate.taskCheckInAndOut(
    this,
    1234567,
    13,
    11221122,
    null,
    accessToken,
    new WMClientCheckInOutListener() {
        @Override
        public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
            Log.d("Task", String.valueOf(clientCheckInOutResponse.getId()));
        }
        @Override
        public void onClientCheckInOutFailed(ErrorResponse error) {
            Log.d("Task Error", String.valueOf(error));
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
