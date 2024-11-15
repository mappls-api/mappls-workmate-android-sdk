# Workmate SDK Implementation

This document outlines the implementation details for initializing the Workmate SDK, updating attendance, and check-in and check-out for the end user application.

## Table of Contents

- [SDK Integration](#sdk-integration)
- [Initialization](#initialization)
- [Updating Attendance](#updating-attendance)
- [Updating Attendance with Activity](#updating-attendance-with-activity)
- [Updating Check-In and Check-Out](#updating-check-in-and-check-out)
- [Get task List](#get-task-list)
- [Hold or resume task](#hold-and-resume-task)
- [Update Workitem Activity](#update-workitem-activity)
- [Update Workitem Activity with lat and lng](#update-workitem-activity-with-lat-and-lng)
- [Get device Location Details](#get-device-location-details)

## SDK Integration

### Setup your project

###

- Add below repository project level Gradle file below AGP 7.0.0

```groovy
 allprojects {
    repositories {
        maven{
            url 'https://maven.mappls.com/repository/workmate/'
        }
        flatDir {
		  dirs 'libs'
		}
    }
}
```

- Add below code in settings.gradle file above AGP 7.0.0

```groovy
dependencyResolutionManagement {

  repositories {
        mavenCentral()
        maven {
            url 'https://maven.mappls.com/repository/workmate/'
        }
        flatDir {
		  dirs 'libs'
		}
    }
}
```

- If you are using Gradle.build.kts

```groovy
dependencyResolutionManagement {

  repositories {
        mavenCentral()
		flatDir {
		  dirs("libs")
		}

        maven{
            url = uri("https://maven.mappls.com/repository/workmate/")
        }
    }
}
```

- Add app level Gradle dependencies

Create a folded in Android application

    application/app/libs/<release-file-name.aar>

```groovy
  //Add workmate sdk
  implementation(files("./libs/<release-file-name>.aar"))
  //Required dependencies
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
  implementation("com.mappls.sdk:intouch-sdk:1.x.x")
}

```

- Make sure java version 8 is added

```groovy
compileOptions {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}
```

## Initialization

### Function: `initWorkmate`

This function initializes the Workmate SDK with the required credentials and user information. It also handles authentication and provides callbacks for success and failure.

#### Implementation

Kotlin Implementation

```groovy
    Workmate.initialize(
        activity, appID, clientId, clientSecret, username, userID, password, email,
        authCallback = object : WMAuthListener {
            override fun onAuthSuccess(response: AuthResponse) {
                //Do something
            }

            override fun onAuthFailure(error: String) {
                //Do something
            }
        }
    )

```

Java Implementation

```groovy
    Workmate.initialize(
        activity, appID, clientId, clientSecret, username, userID, password, email,
        new WMAuthListener() {
            @Override
            public void onAuthSuccess(AuthResponse response) {
                // Do something
            }

            @Override
            public void onAuthFailure(String error) {
                // Do something
            }
        }
    );

```

### Refresh Token

Use below method to refresh token when needed.

### Function: `refreshToken`

Kotlin Implementation

```groovy
    Workmate.refreshToken(activity, object : WMAuthListener {
        override fun onAuthSuccess(response: AuthResponse) {
            // Do something
        }

        override fun onAuthFailure(error: String) {
            // Do something
        }
    })
```

Java Implementation

```groovy
    Workmate.refreshToken(activity, new WMAuthListener() {
        @Override
        public void onAuthSuccess(AuthResponse response) {
            // Do something
        }

        @Override
        public void onAuthFailure(String error) {
            // Do something
        }
    });

```

### Explanation

- **Parameters:**

  - `activity`: Activity context (just pass this).
  - `appID : <String>`, `clientId : <String>`, `clientSecret : <String>`, `email : <String>`, `password : <String>`, `username : <String>`, `userID : String>`: Credentials and user information required for SDK initialization.
  - `authCallback`: A callback to handle authentication results, including success and failure.

- **Callbacks:**

  - `onAuthSuccess`: Handles successful authentication and stores the `accessToken`.
  - `onAuthFailure`: Handles authentication failure and displays the error message.

## Updating Attendance

### Function: `updateAttendance`

This function handles the process of updating attendance using the Workmate SDK.

#### Implementation

Kotlin implementation

```groovy
    Workmate.updateAttendance(
        activity,
        accessToken,
        attendanceId (optional),
        object : WMSessionListener {
            override fun onSessionStarted(attendanceResponse: AttendanceResponse) {
                //Do something
                //attendanceResponse (id, message, status) here id is your attendance id.
            }

            override fun onSessionEnded(attendanceResponse: AttendanceResponse) {
                //Do something
            }

            override fun onSessionError(sessionError: String) {
                //Do smething
            }
        }
    )

```

Java implementation

```groovy
    Workmate.updateAttendance(
    activity,
    accessToken,
    @Nullable attendanceId, // Pass null if attendanceId is optional
    new WMSessionListener() {
        @Override
        public void onSessionStarted(AttendanceResponse attendanceResponse) {
            // Do something
            // attendanceResponse (id, message, status) - here id is your attendance id.
        }

        @Override
        public void onSessionEnded(AttendanceResponse attendanceResponse) {
            // Do something
        }

        @Override
        public void onSessionError(String sessionError) {
            // Do something
        }
    }
);

```

### Explanation

- **Parameters:**

  - Important update regarding attendance id

  ```groovy
  In above signatures attendnce id is optional. pass null to start session (Workday start) and pass attendance id to stop session (Workday End).
  ```

  - `activity`: Activity context (this)
  - `accessToken : <String>`: The access token obtained from the authentication process.
  - `attendanceId : <Int>`: The attendance id for workday end. (Required)
  - `WMSessionListener`: A callback to handle the success or failure of the attendance update process.

- **Callbacks:**

  - `onSessionStarted`: Logs and displays the success message when the session starts.
  - `onSessionEnded`: Logs and displays the success message when the session ends.
  - `onSessionError`: Logs and displays an error message if there's an issue with the session.

## Updating Attendance with Activity

### Function: `updateAttendance`

This function is the overload method of updateAttendance. Added extra paramter called `activityId`.
Rest uage and callback responses remains the same.

#### Implementation

Kotlin implementation

```groovy
    Workmate.updateAttendance(
        activity,
        accessToken,
        attendanceId (optional),
        activityId,
        object : WMSessionListener {
            override fun onSessionStarted(attendanceResponse: AttendanceResponse) {
                //Do something
                //attendanceResponse (id, message, status) here id is your attendance id.
            }

            override fun onSessionEnded(attendanceResponse: AttendanceResponse) {
                //Do something
            }

            override fun onSessionError(sessionError: String) {
                //Do smething
            }
        }
    )

```

Java implementation

```groovy
    Workmate.updateAttendance(
    activity,
    accessToken,
    @Nullable attendanceId, // Pass null if attendanceId is optional
    activityId,
    new WMSessionListener() {
        @Override
        public void onSessionStarted(AttendanceResponse attendanceResponse) {
            // Do something
            // attendanceResponse (id, message, status) - here id is your attendance id.
        }

        @Override
        public void onSessionEnded(AttendanceResponse attendanceResponse) {
            // Do something
        }

        @Override
        public void onSessionError(String sessionError) {
            // Do something
        }
    }
);

```

### Explanation

- **Parameters:**

  - Important update regarding attendance id

  ```groovy
  In above signatures attendnce id is optional. pass null to start session (Workday start) and pass attendance id to stop session (Workday End).
  ```

  - `activity`: Activity context (this)
  - `accessToken : <String>`: The access token obtained from the authentication process.
  - `attendanceId : <Int>`: The attendance id for workday end. (Required)
  - `activityId: <Int>`: Pass activity id to access activity while start or end workday.
  - `WMSessionListener`: A callback to handle the success or failure of the attendance update process.

- **Callbacks:**

  - `onSessionStarted`: Logs and displays the success message when the session starts.
  - `onSessionEnded`: Logs and displays the success message when the session ends.
  - `onSessionError`: Logs and displays an error message if there's an issue with the session.

## Updating Check-In and Check-Out

### Function: `updateCheckInOut`

This function handles the process of updating check-in and check-out information using the Workmate SDK.

#### Implementation

Kotlin implementation

```groovy
    Workmate.updateCheckInAndOut(
        activity, id, activityId, taskId, partnerId, accessToken,
        checkInOutListener = object : WMCheckInOutListener {
            override fun onCheckInAndOutSuccess(checkInAndOutResponse: CheckInAndOutResponse) {
                //Do something
            }

            override fun onCheckInAndOutFailed(error: String) {
                //Do smething
            }
        }
    )
```

Java implementation

```groovy

    Workmate.updateCheckInAndOut(
        activity, id, activityId, taskId, partnerId, accessToken,
        new WMCheckInOutListener() {
            @Override
            public void onCheckInAndOutSuccess(CheckInAndOutResponse checkInAndOutResponse) {
                // Do something
            }

            @Override
            public void onCheckInAndOutFailed(String error) {
                // Do something
            }
        }
    );

```

### Explanation

- **Parameters:**

  - `activity`: Activity context (this)
  - `id : <Int>`: After cheakin. You'll get an id pass here for <id> parameter.

  ```groovy
  In above implementation id is the checkinOut id.
  - While checkin pass null in id
  - While checkout pass id value retrived from onCheckInAndOutSuccess method.
  ```

  - `activityId: <Int>`: The activity id for workflows.
  - `accessToken : <String>`: The access token obtained from the authentication process.
  - `taskId : <String>`, `partnerId : <Int>`: Identifiers for the task and partner.
  - `checkInOutListener`: A callback to handle the success or failure of the check-in and check-out process.

- **Callbacks:**

  - `onCheckInAndOutSuccess`: Displays a success message.
  - `onCheckInAndOutFailed`: Displays an error message.

## Get task List

### Function: `getTaskList`

This function will give you the list of all the task.

#### Implementation

Kotlin implementation

```groovy
    Workmate.getTaskList(activity, startTimeInEpoch, stopTimeInEpoch, accessToken,
        object : WMTaskListListener {
            override fun onTaskListSuccess(taskList: List<TaskData>) {
                //do something
            }

            override fun onTaskListError(error: String) {
                //do something
            }
    })
```

Java implementation

```groovy
Workmate.getTaskList(activity, startTimeInEpoch, stopTimeInEpoch, accessToken,
    new WMTaskListListener() {
        @Override
        public void onTaskListSuccess(List<TaskData> taskList) {
            // do something
        }

        @Override
        public void onTaskListError(String error) {
            // do something
        }
    });
```

### Help

Get Epoch time from time stamp. Below code can help

```groovy

//Kotlin code
    val timestamp = "2024-09-12 15:30:00" // Example timestamp
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.parse(timestamp, formatter)
    val epoch: Long = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

//Java code
    String timestamp = "2024-09-12 15:30:00"; // Example timestamp
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);
    long epoch = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

```

### Explenation

- **Parameters:**

  - `activity`: Activity context (this)
  - `startTimeInEpoc : <Long>`: Sent start time in EPOCH.
  - `stopTimeInEpoc : <Long>`: Sent stop time in EPOCH
  - `accessToken: <String>`: Pass teh access token recieved from auth.
  - `WMTaskListListener`: A callback to handle the success or failure of the task list call.

- **Callbacks:**

  - `onTaskListSuccess`: In success it will give list of all the task.
  - `onTaskListError`: Displays an error message.

## Hold and resume task

### Function: `performTaskHoldResume`

This function will hep to hold and resume the task that checked in.

```groovy
Hold or Resume operations can only be performed for the task that was checked in.
```

#### Implementation

Kotlin implementation

```groovy
Workmate.performTaskHoldResume(activity, token, taskId, checkInId, type,
    object : WMTaskHRListener {
        override fun onTaskHoldResumeSuccess(response: TaskStatusResponse) {
            // TODO("Not yet implemented")
        }

        override fun onTaskHoldResumeError(error: String) {
            // TODO("Not yet implemented")
        }

    })
```

Java implementation

```groovy
Workmate.performTaskHoldResume(activity, token, taskId, checkInId, type,
    new WMTaskHRListener() {
        @Override
        public void onTaskHoldResumeSuccess(TaskStatusResponse response) {
            // TODO: Implement this method
        }

        @Override
        public void onTaskHoldResumeError(String error) {
            // TODO: Implement this method
        }
    });

```

### Explenation

- **Parameters**

  - `activity`: Just pass this
  - `token <String>`: Pass access token hhere
  - `taskid <String>`: Pass task id that needs to hold or resume
  - `checkInId <Int>`: Pass ccheckId that was retrived after checkIn of that perticular task.
  - `type`: Type is a already defined constant **WMTaskConstant.HOLD_TASK or WMTaskConstant.RESUME_TASK**

- **Callbacks**

  - `onTaskHoldResumeSuccess`: Will give the success response from server
  - `onTaskHoldResumeError`: Will give error response, errors or any exception happens.

## Update Workitem Activity

### Function: `updateWorkItemActivity`

This function will help you to update you workitem activity.

#### Implementation

Kotlin implementation

```groovy
    Workmate.updateWorkItemActivity(activity, accessToken, taskId, partnerId, activityId,
        object : WMUpdateActivityListener {
            override fun onActivityUpdateSuccess(updateActivity: UpdateActivity) {
                // do something
            }

            override fun onActivityUpdateError(error: String?) {
                // do something
            }
        }
)
```

Java Implementation

```groovy
    Workmate.updateWorkItemActivity(activity, accessToken, taskId, partnerId, activityId,
        new WMUpdateActivityListener() {
            @Override
            public void onActivityUpdateSuccess(UpdateActivity updateActivity) {
                // do something
            }

            @Override
            public void onActivityUpdateError(String error) {
                // do something
            }
        });
```

### Explenation

- **Parameters:**

  - `activity`: Activity context (this)
  - `activityId: <Int>`: The activity id for workflows.
  - `accessToken : <String>`: The access token obtained from the authentication process.
  - `taskId : <String>`, `partnerId : <Int>`: Identifiers for the task and partner.
  - `WMUpdateActivityListener`: A callback to handle the success or failure of the UpdateWorkitemActivity.

- **Callbacks:**

  - `onActivityUpdateSuccess`: In success it will give you the updateactivity model can be used accordingly.
  - `onActivityUpdateError`: Displays an error message.

## Update Workitem Activity with lat and lng

### Function: `updateWorkItemActivity`

This function will help you to update you workitem activity. Actually a overload method for updateWorkItemActivity()

#### Implementation

Kotlin implementation

```groovy
    Workmate.updateWorkItemActivity(activity, accessToken, taskId, partnerId, activityId, lat, lng,
        object : WMUpdateActivityListener {
            override fun onActivityUpdateSuccess(updateActivity: UpdateActivity) {
                // do something
            }

            override fun onActivityUpdateError(error: String?) {
                // do something
            }
        }
)
```

Java Implementation

```groovy
    Workmate.updateWorkItemActivity(activity, accessToken, taskId, partnerId, activityId, lat, lng,
        new WMUpdateActivityListener() {
            @Override
            public void onActivityUpdateSuccess(UpdateActivity updateActivity) {
                // do something
            }

            @Override
            public void onActivityUpdateError(String error) {
                // do something
            }
        });
```

### Explenation

- **Parameters:**

  - `activity`: Activity context (this)
  - `activityId: <Int>`: The activity id for workflows.
  - `accessToken : <String>`: The access token obtained from the authentication process.
  - `taskId : <String>`, `partnerId : <Int>`: Identifiers for the task and partner.
  - `lat, lng <Double>`: lat and lng in double values
  - `WMUpdateActivityListener`: A callback to handle the success or failure of the UpdateWorkitemActivity.

- **Callbacks:**

  - `onActivityUpdateSuccess`: In success it will give you the updateactivity model can be used accordingly.
  - `onActivityUpdateError`: Displays an error message.

## Get Device Location Details

### function: `getDeviceLocationData`

This functions hepls you to find device location details. See the following:

```groovy
latitude, longitude, altitude, speed, bearing, locationProvider
```

#### Implementation

Kotlin Implementation

```groovy
    Workmate.getDeviceLocationData(activity, object : WMDeviceLocationListener {
        override fun onDeviceInfoSuccess(deviceLocationInfo: DeviceLocationInfo) {
            // Do something
        }

        override fun onDeviceInfoError(error: String) {
            // Do something
        }
    })
```

Java implementation

```groovy
    Workmate.getDeviceLocationData(activity, new WMDeviceLocationListener() {
        @Override
        public void onDeviceInfoSuccess(DeviceLocationInfo deviceLocationInfo) {
            // Do something
        }

        @Override
        public void onDeviceInfoError(String error) {
            // Do something
        }
    });

```

### Explenation

- **Parameters:**

  - `activity`: Activity Context (this)

- **Parameters:**
  - `onDeviceInfoSuccess`: On device location success
  - `onDeviceIntoError`: On device location error.
