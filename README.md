# Workmate SDK Implementation

This document outlines the implementation details for initializing the Workmate SDK, updating attendance, and check-in and check-out for the end user application.

## Table of Contents

- [SDK Integration](#sdk-integration)
- [Initialization](#initialization)
- [Manage Workday](#manage-workday)
- [Task Check-In and Check-Out](#task-check-in-and-check-out)
- [Client Check-In and Check-Out](#client-check-in-and-check-out)
- [Create Update Client ](#create-update-client )
- [Calculate Distance in km](#calculate-distance-in-km)
- [Users Movement Trails](#users-movement-trails)
- [Get User Activity](#get-user-activity)
- [Manage Workitem](#manage-workitem)
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
  implementation("com.mappls.sdk:intouch-sdk:1.4.2")
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
        activity,clientId, clientSecret, username, password
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
        activity,clientId, clientSecret, username, password
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
  - `clientId : <String>`, `clientSecret : <String>`,`username : <String>`,`password : <String>`: Credentials and user information required for SDK initialization.
  - `authCallback`: A callback to handle authentication results, including success and failure.

- **Callbacks:**

  - `onAuthSuccess`: Handles successful authentication and stores the `accessToken`.
  - `onAuthFailure`: Handles authentication failure and displays the error message.



## Manage Workday

### Function: `manageWorkday`

This function manages the attendance update process using the Workmate SDK, enabling users to perform start and end workday activities through this function.

#### Implementation

Kotlin implementation

```groovy
    Workmate.manageWorkday(
        activity,
        @Nullable attendanceId,
        activityId (optional),
        formData,
        accessToken,
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
    Workmate.manageWorkday(
        activity,        
        @Nullable attendanceId, 
        activityId (optional),
        formData,
        accessToken,
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

  ```groovy
  In above signatures attendnce id is optional. pass null to start session (Workday start) and pass attendance id to stop session (Workday End).
  ```

  - `activity`: Activity context (this)
  - `accessToken : <String>`: The access token obtained from the authentication process.
  - `attendanceId: <Int>`: Pass null to start the workday; the user will receive the ID upon completing the start workday activity.This ID is mandatory to end the workday.
  - `activityId: <Int>`: Provide the activity ID to access the activity while starting or ending the workday.If the workflow is disabled, pass null.
  - `formData: <JSONObject>`: If the workflow is enabled, provide the formData corresponding to the activityId. 
  - `WMSessionListener`: A callback to handle the success or failure of the attendance update process.

- **Callbacks:**

  - `onSessionStarted`: Logs and displays the success message when the session starts.
  - `onSessionEnded`: Logs and displays the success message when the session ends.
  - `onSessionError`: Logs and displays an error message if there's an issue with the session.






## Task Check-In and Check-Out

### Function: `taskCheckInOut`

This function facilitates the process of managing task check-in and check-out operations using the Workmate SDK.
It enables seamless integration of task features, allowing users to initiate and conclude task sessions efficiently.

#### Implementation

Kotlin implementation

```groovy
    Workmate.taskCheckInOut(
        activity, id, activityId, taskId,formData, accessToken,
        taskCheckInOutListener = object : WMTaskCheckInOutListener {
            override fun onTaskCheckInOutSuccess(taskCheckInOutResponse: TaskCheckInOutResponse) {
                //Do something
            }

            override fun onTaskCheckInOutFailed(error: String) {
                //Do smething
            }
        }
    )
```

Java implementation

```groovy

    Workmate.taskCheckInOut(
        activity, id, activityId, taskId,formData, accessToken,
        new WMTaskCheckInOutListener() {
            @Override
            public void onTaskCheckInOutSuccess(TaskCheckInOutResponse taskCheckInOutResponse) {
                // Do something
            }

            @Override
            public void onTaskCheckInOutFailed(String error) {
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

  - `activityId: <Int>`: Provide the activity ID to access the activity while checkin and checkout the task.If the workflow is disabled, pass null.
  - `accessToken : <String>`: The access token obtained from the authentication process.
  - `taskId : <String>` : This parameter represents the unique identifier for a task. The taskId is associated with the corresponding Workmate task.
  - `formData: <JSONObject>`: The form data required when checking in or checking out of a task. If the workflow is enabled, provide the formData that corresponds to the specified activityId. This object typically contains key-value pairs representing the data fields associated with the task . If the workflow is disabled, pass null.
  - `taskCheckInOutListener`: A callback to handle the success or failure of the check-in and check-out process.

- **Callbacks:**

  - `onTaskCheckInOutSuccess`: Displays a success message.
  - `onTaskCheckInOutFailed`:  Displays an error message.



## Client Check-In and Check-Out 

### Function: `clientCheckInOut`

This function handles the process of  client check-in and check-out information using the Workmate SDK.

#### Implementation

Kotlin implementation

```groovy
    Workmate.clientCheckInOut(
        activity,clientId,activityId, formData,accessToken,
        clientCheckInOutListener = object : WMClientCheckInOutListener {
            override fun onClientCheckInOutSuccess(ClientCheckInOutResponse: clientCheckInOutResponse) {
                //Do something
            }

            override fun onClientCheckInOutFailed(error: String) {
                //Do something
            }
        }
    )
```

Java implementation

```groovy

    Workmate.clientCheckInOut(
        activity,clientId,activityId, formData,accessToken,
        new WMClientCheckInOutListener() {
            @Override
            public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
                // Do something
            }

            @Override
            public void onClientCheckInOutFailed(String error) {
                // Do something
            }
        }
    );

```

### Explanation

- **Parameters:**

  - `activity`: Activity context (this)
  - `clientId : <Int>`: After client cheakin. You'll get an clientId pass here for <id> parameter.

  ```groovy
    In above implementation clientId is the checkinOut clientId.
  - While checkin pass null in clientId
  - While checkout pass clientId value retrived from onClientCheckInOutSuccess method.
  ```

  - `activityId: <Int>`: Provide the activity ID to access the activity while checkin and checkout the task.If the workflow is disabled, pass null.
  - `accessToken : <String>`: The access token obtained from the authentication process.
  - `clientId : <String>`: This parameter represents the unique identifier for a client. The clientId is associated with the corresponding Workmate client.
  - `formData: <JSONObject>`: The form data required when checking in or checking out of a client. If the workflow is enabled, provide the formData that corresponds to the specified activityId. This object typically contains key-value pairs representing the data fields associated with the client . If the workflow is disabled, pass null. 
  - `clientCheckInOutListener`: A callback to handle the success or failure of the check-in and check-out process.

- **Callbacks:**

  - `onClientCheckInOutSuccess`: Displays a success message.
  - `onClientCheckInOutFailed`: Displays an error message.


## Create Update Client 

### Function: `manageClient`

This function manages the process of adding and updating client information through the Workmate SDK. It allows users to create new client records or modify existing ones, ensuring that client data is accurately reflected in the system.

#### Implementation

Kotlin implementation

```groovy
    Workmate.manageClient(
        activity, clientId, clientData, accessToken,
        manageClientListener = object : WMManageClientListener {
            override fun onManageClientSuccess(ManageClientResponse: manageClientResponse) {
                //Do something
            }

            override fun onManageClientFailed(error: String) {
                //Do smething
            }
        }
    )
```

Java implementation

```groovy

    Workmate.manageClient(
        activity, clientId, clientData, accessToken,
        new WMManageClientListener() {
            @Override
            public void onManageClientSuccess(ManageClientResponse manageClientResponse) {
                // Do something
            }

            @Override
            public void onManageClientFailed(String error) {
                // Do something
            }
        }
    );

```

### Explanation

- **Parameters:**

  - `activity`: Activity context (this)
  - `clientId : <Int>`: After creating a client, you will receive the client ID <id>. This client ID will be used whenever the user wants to update the client's information.
  -  `clientData: <JSONObject>` :  This parameter contains the client information required for creating a new client. The data is passed as a JSONObject and typically includes details such as the client’s name, contact information, address, and any other relevant information necessary for the client creation process.
  - `accessToken : <String>`: The access token obtained from the authentication process.
  - `WMmanageClientListener`: A callback to handle the success or failure of the add client and update client process.

- **Callbacks:**

  - `onmanageClientSuccess`: Displays a success message.
  - `onmanageClientFailed`: Displays an error message.



## Calculate distance in km

### Function: `calculateDistance`

This function handles the calculation of distance information using the Workmate SDK. It considers various inputs, such as userId, type, and time, to compute the total distance between them.Admins have the ability to retrieve the distance information for other users under their supervision.This functionality is designed to allow admins to monitor the distance-related activities of users who are associated with them.

#### Implementation

Kotlin implementation

```groovy
    Workmate.calculateDistance(
        activity, userId,type,startTimeInEpoch, endTimeInEpoch,accessToken
        calculateDistanceListener = object : WMCalculateDistanceListener {
            override fun onCalculateDistanceSuccess(calculateDistanceResponse: CalculateDistanceResponse) {
                //Do something
            }

            override fun onCalculateDistanceFailed(error: String) {
                //Do smething
            }
        }
    )
```

Java implementation

```groovy

    Workmate.calculateDistance(
        activity, userId,type,startTimeInEpoch, endTimeInEpoch,accessToken
        new WMCalculateDistanceListener() {
            @Override
            public void onCalculateDistanceSuccess(CalculateDistanceResponse calculateDistanceResponse) {
                // Do something
            }

            @Override
            public void onCalculateDistanceFailed(String error) {
                // Do something
            }
        }
    );

```

### Explanation

- **Parameters:**

  - `activity`: Activity context (this).
  - `userId: <String>`: Pass the userId for which you want to retrieve the drive distance or odometer distance.Admins have the ability to retrieve the distance information for other users under their supervision.
  - `type: <String>`: Pass type: 1 for drive distance and type: 2 for odometer distance.
  - `startTimeInEpoc : <Long>`: Sent start time in EPOCH.
  - `endTimeInEpoc : <Long>`: Sent stop time in EPOCH
  - `accessToken: <String>`: Pass the access token recieved from auth.
  - `WMCalculateDistanceListener`: A callback to handle the success or failure of the calculate distance call.

- **Callbacks:**

  - `onCalculateDistanceSuccess`: Displays a success message.
  - `onCalculateDistanceFailed`: Displays an error message.




## Users Movement Trails

### Function: `MovementTrails`

The MovementTrails function is responsible for retrieving and managing user movement trail through the Workmate SDK. It provides a way to track and analyze the location history or movement patterns of users within a specific timeframe.

#### Implementation

Kotlin implementation

```groovy
    Workmate.movementTrails(
       activity, userId,startTimeInEpoc,endTimeInEpoc,accessToken,
        movementTrailsListener = object : WMMovementTrailsListener {
            override fun onMovementTrailsSuccess(movementTrailsResponse: MovementTrailsResponse) {
                //Do something
            }

            override fun onMovementTrailsFailed(error: String) {
                //Do smething
            }
        }
    )
```

Java implementation

```groovy

    Workmate.movementTrails(
       activity, userId,startTimeInEpoc,endTimeInEpoc,accessToken,
        new WMMovementTrailsListener() {
            @Override
            public void onMovementTrailsSuccess(MovementTrailsResponse movementTrailsResponse) {
                // Do something
            }

            @Override
            public void onMovementTrailsFailed(String error) {
                // Do something
            }
        }
    );

```

### Explanation

- **Parameters:**


  - `activity`: Activity context (this).
  - `userId: <String>`: Pass the userId for which you want to retrieve the movement Trails.Admins have the ability to retrieve the movement Trails information for other users under their supervision.
  - `startTimeInEpoc : <Long>`: Sent start time in EPOCH.
  - `endTimeInEpoc : <Long>`: Sent stop time in EPOCH.
  - `accessToken: <String>`: Pass teh access token recieved from auth.
  - `WMMovementTrailsListener`: A callback to handle the success or failure of the Movement Trails call

- **Callbacks:**

  - `onMovementTrailsSuccess`: Displays a success message.
  - `onMovementTrailsFailed`: Displays an error message.



## Get User Activity

### Function: `userActivity`

The function is designed to retrieve user activity the Workmate SDK. It facilitates managing activities performed by users, either linked to specific tasks or conducted independently of any task.

#### Implementation

Kotlin implementation

```groovy
    Workmate.userActivity(
        activity, userId,startTimeInEpoc,endTimeInEpoc,accessToken,
        userActivityListener = object : WMUserActivityListener {
            override fun onUserActivitySuccess(userActivityResponse: UserActivityResponse) {
                //Do something
            }

            override fun onUserActivityFailed(error: String) {
                //Do smething
            }
        }
    )
```

Java implementation

```groovy

    Workmate.userActivity(
        activity, userId,startTimeInEpoc,endTimeInEpoc,accessToken,
        new WMUserActivityListener() {
            @Override
            public void onUserActivitySuccess(UserActivityResponse userActivityResponse) {
                // Do something
            }

            @Override
            public void onUserActivityFailed(String error) {
                // Do something
            }
        }
    );

```

### Explanation

- **Parameters:**


 - `activity`: Activity context (this).
   - `userId: <String>`: Pass the userId for which you want to retrieve the user activity .Admins have the ability to retrieve the user activity information for other users under their supervision.
  - `startTimeInEpoc : <Long>`: Sent start time in EPOCH.
  - `endTimeInEpoc : <Long>`: Sent stop time in EPOCH
  - `accessToken: <String>`: Pass teh access token recieved from auth.
  - `WMTaskListListener`: A callback to handle the success or failure of the task list call

- **Callbacks:**

  - `onUserActivitySuccess`: Displays a success message.
  - `onUserActivityFailed`: Displays an error message.



## Create Workitem

### Function: `createWorkItem`

This function facilitates the management of workitem information using the Workmate SDK. It is designed to handle tasks such as creating and updating workitems within the Workmate system.


#### Implementation

Kotlin implementation

```groovy
    Workmate.createWorkItem(
        activity, activityId,formData,actionObject,accessToken,
        createWorkItemListener = object : WMCreateWorkItemListener {
            override fun onCreateWorkItemSuccess(CreateWorkItemResponse: createWorkItemResponse) {
                //Do something
            }

            override fun onCreateWorkItemFailed(error: String) {
                //Do smething
            }
        }
    )
```

Java implementation

```groovy

    Workmate.createWorkItem(
         activity, activityId,formData,actionObject,accessToken,
        new WMCreateWorkItemListener() {
            @Override
            public void onCreateWorkItemSuccess(CreateWorkItemResponse createWorkItemResponse) {
                // Do something
            }

            @Override
            public void onCreateWorkItemFailed(String error) {
                // Do something
            }
        }
    );

```

### Explanation

- **Parameters:**

  - `activityId: <Int>`: The activity id for workflows.If the workflow is not enabled, pass null. 
  - `accessToken : <String>`: The access token obtained from the authentication process.
  - `formData: <JSONObject>`: workitem information for creating the workitem.
  - `actionObject: <JSONObject>`: Pass the workitem action and decision in JSON format
  - `WMManageActivityListener`: A callback to handle the success or failure of the check-in and check-out process.

- **Callbacks:**

  - `oncreateWorkItemSuccess`: Displays a success message.
  - `oncreateWorkItemFailed`: Displays an error message.



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

### Explanation

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

### Explanation

- **Parameters**

  - `activity`: Just pass this
  - `token <String>`: Pass access token hhere
  - `taskid <String>`: Pass task id that needs to hold or resume
  - `checkInId <Int>`: Pass ccheckId that was retrived after checkIn of that perticular task.
  - `type`: Type is a already defined constant **WMTaskConstant.HOLD_TASK or WMTaskConstant.RESUME_TASK**

- **Callbacks**

  - `onTaskHoldResumeSuccess`: Will give the success response from server
  - `onTaskHoldResumeError`: Will give error response, errors or any exception happens.


##

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

### Explanation

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

### Explanation

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

### Explanation

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

### Explanation

- **Parameters:**

  - `activity`: Activity Context (this)

- **Parameters:**
  - `onDeviceInfoSuccess`: On device location success
  - `onDeviceIntoError`: On device location error.
