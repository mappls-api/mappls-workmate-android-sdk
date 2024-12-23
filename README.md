# Workmate SDK Implementation

This document outlines the implementation details for initializing the Workmate SDK, updating attendance, and check-in and check-out for the end user application.

## Table of Contents

- [SDK Integration](#sdk-integration)
- [Initialization](#initialization)
- [Manage Workday](#manage-workday)
- [Calculate Distance in km](#calculate-distance-in-km)
- [Users Movement Trails](#users-movement-trails)
- [Get User Activity](#get-user-activity)
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
