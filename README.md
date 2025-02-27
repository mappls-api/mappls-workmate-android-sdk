# Workmate SDK implementation

This document will help you implement Workmate fleet management SDK in your application. Workmate SDK provides lots of methods that can be integrated inside your application. Here is step by step approach how to get started with Workmate SDK

## Table of content

- [Getting started](#getting-started)
- [Initialization](#workmate-initialization)
- [Manage Workday](#workmate-manage-workday)
- [Client Check-In-Out](#workmate-client-check-in-out)
- [Get Movemebt Trail](#workmate-get-movement-trail)
- [Get User Activity](#workmate-get-user-activity)
- [Calculate Distance](#workmate-calculate-distance)
- [Get Device Location Details](#workmate-get-device-location)
- [Refresh token](#workmate-refresh-token)
- [Geo-Fence validation](#workmate-validate-geo-fence)

## Help

- [Get time in EPOCH with System Current Time](#sample-code-to-convert-epoch-current-time-to-epoch)
- [Get time in EPOCH with some UI](#sample-code-to-get-current-time-in-epoch-from-an-ui)

## Getting started

Before implementing the various methods provided by the SDK, you must perform the following configuration changes in both your app-level and module-level Gradle files. As different versions of Android Studio support different Gradle extensions, please select the one that is specific to your environment.

- Add below repository project level Gradle file below AGP 7.0.0

```groovy
 allprojects {
    repositories {
        maven{
            url 'https://maven.mappls.com/repository/mappls/'
        }
        maven{
            url 'https://maven.mappls.com/repository/workmate/'
        }
    }
}
```

- Add below code in settings.gradle file above AGP 7.0.0

```groovy
dependencyResolutionManagement {

  repositories {
        mavenCentral()
        maven{
            url 'https://maven.mappls.com/repository/mappls/'
        }
        maven {
            url 'https://maven.mappls.com/repository/workmate/'
        }
    }
}
```

- If you are using Gradle.build.kts for latest Abdroid version releases. Choose below format.

```groovy
dependencyResolutionManagement {

  repositories {
        mavenCentral()
        maven{
            url = uri("https://maven.mappls.com/repository/mappls/")
        }
        maven{
            url = uri("https://maven.mappls.com/repository/workmate/")
        }
    }
}
```

- After go to your app level build.gradle file and add below dependency and build your application

```groovy
implementation 'com.mappls.sdk:mappls-workmate:<replace with latest version>'
```

Now we are all set to integrate Workmate SDK inside our application.

## Workmate initialization

### Method name: `initialize`

Method will initialize all the services required by the Workmate SDK and also provide a token to access all the services provided by Workmate SDK. Workmate initialize method require following parameter:

- **Parameters:**

  - `context`: pass context here. (just pass this).
  - `clientId : <String>`: provide client ID given by the mappls team.
  - `clientSecret : <String>`: provide client Secret given by the mappls team.
  - `useremail : <String>`: provide user email.
  - `password : <String>`: provide user password. `Do NOT CONVERT TO MD5 SDK WILL HANDLE THAT`

- **Callbacks:**

  - `onAuthSuccess`: Handles successful authentication and stores the `accessToken`.
  - `onAuthFailure`: Handles authentication failure and displays the error message.

Below is the method signature along with an example.

- Kotlin implementation

```groovy

    //Method signature
    Workmate.initialize(
        context, clientSecret, clientId, username, password
        authCallback = object : WMAuthListener {
            override fun onAuthSuccess(response: AuthResponse) {
                //Do something
            }

            override fun onAuthFailure(error: String) {
                //Do something
            }
        }
    )

    //Method example
    Workmate.initialize(
        this,
        "Demo client secret", //Replace with the real one provided by mappls team.
        "Demo client id", //Replace with the real one provided by mappls team.
        "useremail@example.com", //Replace with the real user email
        "my_password", //Replace with real password
        authCallback = object : WMAuthListener {
            override fun onAuthSuccess(response: AuthResponse) {
                Log.d("Auth Response","${response.accessToken}") //Save this access token as you'll be using it for other Workmate Services.
            }

            override fun onAuthFailure(error: String) {
                Log.d("Auth Error",error) //Will show any error caused by this method
            }
        }
    )

```

- Java implementation

```groovy

    //Method signature
    Workmate.initialize(
        context, clientSecret, clientId, username, password
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

    //Method example
    Workmate.initialize(
        this,
        "Demo client secret", // Replace with the real one provided by the Mappls team.
        "Demo client Id", // Replace with the real one provided by the Mappls team.
        "useremail@example.com", // Replace with the real user email.
        "my_password", // Replace with the real password.
        new WMAuthListener() {
            @Override
            public void onAuthSuccess(AuthResponse response) {
                Log.d("Auth Response", response.getAccessToken()); // Save this access token as you'll be using it for other Workmate Services.
            }

            @Override
            public void onAuthFailure(String error) {
                Log.d("Auth Error", error); // Will show any error caused by this method.
            }
        }
    );


```

## Workmate refresh token

Use below method to refresh token when needed. Method callback provide same results as of initialize method.

### Function: `refreshToken`

- **Parameters:**

  - `context`: pass context here. (just pass this).

- **Callbacks:**

  - `onAuthSuccess`: Handles successful authentication and stores the `accessToken`.
  - `onAuthFailure`: Handles authentication failure and displays the error message.

Kotlin Implementation

```groovy
    //Method Sugnature
    Workmate.refreshToken(context, object : WMAuthListener {
        override fun onAuthSuccess(response: AuthResponse) {
            // Do something
        }

        override fun onAuthFailure(error: String) {
            // Do something
        }
    })

    //Method example
    Workmate.refreshToken(this, object : WMAuthListener {
        override fun onAuthSuccess(response: AuthResponse) {
            Log.d("refresh success", "${response.accessToken}")
        }

        override fun onAuthFailure(error: String) {
            Log.d("refresh error", error)
        }
    })
```

Java Implementation

```groovy
    //method signature
    Workmate.refreshToken(context, new WMAuthListener() {
        @Override
        public void onAuthSuccess(AuthResponse response) {
            // Do something
        }

        @Override
        public void onAuthFailure(String error) {
            // Do something
        }
    });

    //Method example
    Workmate.refreshToken(this, new WMAuthListener() {
        @Override
        public void onAuthSuccess(AuthResponse response) {
            Log.d("refresh success", response.getAccessToken());
        }

        @Override
        public void onAuthFailure(String error) {
            Log.d("refresh error", error);
        }
    });

```

## Workmate Manage Workday

This function manages the attendance update process using the Workmate SDK, enabling users to perform start and end workday activities through this function.

### Method name `manageWorkday`

### Explanation

- **Parameters:**

  - `context`: Context (this)
  - `accessToken : <String>`: The access token obtained from the authentication process.
  - `attendanceId: <Int>`: Pass null to start the workday; the user will receive the ID upon completing the start workday activity. This ID is mandatory to end the workday.
  - `activityId: <Int>` Optional parameter: Provide the activity ID to access the activity while starting or ending the workday. If the workflow is disabled, pass null.
  - `formData: <JSONObject>` Optional parameter: If the workflow is enabled, provide the formData corresponding to the activityId.
  - `WMSessionListener`: A callback to handle the success or failure of the attendance update process.

- **Callbacks:**

  - `onSessionStarted`: Logs and displays the success message when the session starts. AttendanceResponse is a model class will provide you attendanceId.
  - `onSessionEnded`: Logs and displays the success message when the session ends.
  - `onSessionError`: Logs and displays an error message if there's an issue with the session.

  Below is the method signature along with an example

Kotlin implementation

```groovy

    //Method signature
    Workmate.manageWorkday(
        context,
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

    //Method example
    //Below is an example of start workday
    Workmate.manageWorkday(
        this, //Context
        null, //Pass null to start workday. If workday already started mwthod will not start new workday but gives you pending workday instead.
        null,
        null,
        accessToken, //Provided by initialize method.
        object : WMSessionListener {
            override fun onSessionStarted(attendanceResponse: AttendanceResponse) {
                //Do something
                Log.d("Attendance id","${attendanceResponse.id}") //Suppose 123456789 is the id recieved from method.
                Log.d("Attendance message","${attendanceResponse.message}")
                Log.d("Attendance status","${attendanceResponse.status}")
            }

            override fun onSessionEnded(attendanceResponse: AttendanceResponse) {
                //Do something
                //This method will not call in this case.
            }

            override fun onSessionError(sessionError: String) {
                Log.d("Attendance error", error)
            }
        }
    )

    //Method example
    //Beloww is an example of stop workday
        Workmate.manageWorkday(
        this, //Context
        12345678, //id recieved while starting workday
        null,
        null,
        accessToken, //Provided by initialize method.
        object : WMSessionListener {
            override fun onSessionStarted(attendanceResponse: AttendanceResponse) {
                //Do something
                //This method will not call in this case.
            }

            override fun onSessionEnded(attendanceResponse: AttendanceResponse) {
                //Do something
                Log.d("Attendance id","${attendanceResponse.id}")
                Log.d("Attendance message","${attendanceResponse.message}")
                Log.d("Attendance status","${attendanceResponse.status}")
            }

            override fun onSessionError(sessionError: String) {
                Log.d("Attendance error", error)
            }
        }
    )

```

Java implementation

```groovy

    //Method signture
    Workmate.manageWorkday(
        context,
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
    });


    // Method example
    // Below is an example of starting a workday
    Workmate.manageWorkday(
        this, // Context
        null, // Pass null to start workday. If workday is already started, this method will not start a new one but will return the pending workday instead.
        null,
        null,
        accessToken, // Provided by the initialize method.
        new WMSessionListener() {
            @Override
            public void onSessionStarted(AttendanceResponse attendanceResponse) {
                // Do something
                Log.d("Attendance ID", attendanceResponse.getId()); // Suppose 123456789 is the ID received from the method.
                Log.d("Attendance Message", attendanceResponse.getMessage());
                Log.d("Attendance Status", attendanceResponse.getStatus());
            }

            @Override
            public void onSessionEnded(AttendanceResponse attendanceResponse) {
                // Do something
                // This method will not be called in this case.
            }

            @Override
            public void onSessionError(String sessionError) {
                Log.d("Attendance Error", sessionError);
            }
        }
    );


    // Method example
    // Below is an example of stopping a workday
    Workmate.manageWorkday(
        this, // Context
        12345678, // ID received while starting workday
        null,
        null,
        accessToken, // Provided by the initialize method.
        new WMSessionListener() {
            @Override
            public void onSessionStarted(AttendanceResponse attendanceResponse) {
                // Do something
                // This method will not be called in this case.
            }

            @Override
            public void onSessionEnded(AttendanceResponse attendanceResponse) {
                // Do something
                Log.d("Attendance ID", attendanceResponse.getId());
                Log.d("Attendance Message", attendanceResponse.getMessage());
                Log.d("Attendance Status", attendanceResponse.getStatus());
            }

            @Override
            public void onSessionError(String sessionError) {
                Log.d("Attendance Error", sessionError);
            }
        }
    );

```

## Workmate Client Check-in-out

### method name: `clientCheckInOut`

This function handles the process of client check-in and check-out information using the Workmate SDK.

### Explanation

- **Parameters:**

  - `context`: Context (this)
  - `clientId : <Int>`: After client cheakin. You'll get an clientId pass here for <id> parameter.

  ```groovy
    In above implementation clientId is the checkinOut clientId.
  - While checkin pass null in clientCheckId
  - While checkout pass idd value in clientCheckId retrived from onClientCheckInOutSuccess method.
  ```

  - `activityId: <Int>`: Provide the activity ID to access the activity while checkin and checkout the task.If the workflow is disabled, pass null.
  - `accessToken : <String>`: The access token obtained from the authentication process.
  - `clientId : <String>`: This parameter represents the unique identifier for a client. The clientId is associated with the corresponding Workmate client.
  - `clientCheckId: <Int>`: Client check-in retrived from success callback. Pass null to new check-in
  - `formData: <JSONObject>`: The form data required when checking in or checking out of a client. If the workflow is enabled, provide the formData that corresponds to the specified ContextId. This object typically contains key-value pairs representing the data fields associated with the client . If the workflow is disabled, pass null.
  - `clientCheckInOutListener`: A callback to handle the success or failure of the check-in and check-out process.

- **Callbacks:**

  - `onClientCheckInOutSuccess`: Displays a success message.
  - `onClientCheckInOutFailed`: Displays an error message.

  Below is the method implementation along with example

#### Implementation

Kotlin implementation

```groovy

    //Method signature
    Workmate.clientCheckInOut(
        Context, clientId, clientCheckId, activityId, formData, accessToken,
        clientCheckInOutListener = object : WMClientCheckInOutListener {
            override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                //Do something
            }

            override fun onClientCheckInOutFailed(error: String) {
                //Do something
            }
        }
    )

    //Method example
    //Peroming client check-in
    Workmate.clientCheckInOut(
        this,
        1234567, //Client id obtained from Workmate
        null, //pass null for new client check-in.
        null,
        null,
        accessToken, //token recieved from workmate initialization
        clientCheckInOutListener = object : WMClientCheckInOutListener {
            override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                //Do something
                Log.d("client id:", "${clientCheckInOutResponse.id}") //Suppose 11221122 recieved from server
            }

            override fun onClientCheckInOutFailed(error: String) {
                //Do something
            }
        }
    )

    //Performing client check-out
        Workmate.clientCheckInOut(
        this,
        1234567, //Client id obtained from Workmate
        11221122, //pass null for new client check-in.
        null,
        null,
        accessToken, //token recieved from workmate initialization
        clientCheckInOutListener = object : WMClientCheckInOutListener {
            override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                //Do something
                Log.d("client id:", "${clientCheckInOutResponse.id}")
            }

            override fun onClientCheckInOutFailed(error: String) {
                //Do something
            }
        }
    )

```

Java implementation

```groovy
    //Method sugnature
    Workmate.clientCheckInOut(
        Context, clientId, activityId, formData, accessToken,
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

    //Client check in example
    Workmate.clientCheckInOut(
        this,
        1234567, // Client id obtained from Workmate
        null, // Pass null for new client check-in
        null,
        null,
        accessToken, //token recieved from workmate initialization
        new WMClientCheckInOutListener() {
            @Override
            public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
                // Do something
                Log.d("client id:", String.valueOf(clientCheckInOutResponse.getId())); // Suppose 11221122 received from server
            }

            @Override
            public void onClientCheckInOutFailed(String error) {
                // Do something
            }
        }
    );

    // Client check out example
    Workmate.clientCheckInOut(
        this,
        1234567, // Client id obtained from Workmate
        11221122,
        null,
        null,
        accessToken, //token recieved from workmate initialization
        new WMClientCheckInOutListener() {
            @Override
            public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
                // Do something
                Log.d("client id:", String.valueOf(clientCheckInOutResponse.getId()));
            }

            @Override
            public void onClientCheckInOutFailed(String error) {
                // Do something
            }
        }
    );

```

## Workmate get movement trail

### Function: `getMovementTrail`

The MovementTrails function is responsible for retrieving and managing user movement trail through the Workmate SDK. It provides a way to track and analyze the location history or movement patterns of users within a specific timeframe.

### Explanation

- **Parameters:**

  - `context`: Context (this).
  - `userEmail: <String>`: Pass the userEmail for which you want to retrieve the movement Trails. Admins have the ability to retrieve the movement Trails information for other users under their supervision.
  - `startTimeInEpoc : <Long>`: Sent start time in EPOCH.
  - `endTimeInEpoc : <Long>`: Sent stop time in EPOCH.
  - `accessToken: <String>`: Pass the access token recieved from auth.
  - `WMMovementTrailsListener`: A callback to handle the success or failure of the Movement Trails call

- **Callbacks:**

  - `onMovementTrailsSuccess`: Success call will give you a response with following values. `address`, `latitude`, `longitude`, `speed`, `timestamp`
  - `onMovementTrailsFailed`: Displays an error message while retriving movement trail from method.

#### implementation

Kotlin implementation

```groovy

    //Method sugnature
    Workmate.userActivity(
        context, userEmail, startTimeInEpoc, endTimeInEpoc, accessToken,
        userActivityListener = object : WMUserActivityListener {
            override fun onUserActivitySuccess(userActivityResponse: UserActivityResponse) {
                //Do something
            }

            override fun onUserActivityFailed(error: String) {
                //Do smething
            }
        }
    )

    //Example code
    Workmate.getMovementTrail(
        this,
        "usereamil@example.com", // Email will be same while initilialize workmate.
        1732127400,
        1732213799,
        token,
        object : WMMovementTrailsListener {
            override fun onMovementTrailsSuccess(movementTrailsResponse: MovementTrailsResponse) {
                Log.d("Trail client", movementTrailsResponse.data.toString())
                // movementTrailsResponse.data.address
                // movementTrailsResponse.data.latitude
                // movementTrailsResponse.data.longitude
                // movementTrailsResponse.data.speed
                // movementTrailsResponse.data.timestamp
            }

            override fun onMovementTrailsFailed(error: String) {
                Log.d("Trail error", error)
            }

        }
    )
```

Java implementation

```groovy
    // Method signature
    Workmate.userActivity(
        context,
        userEmail,
        startTimeInEpoc,
        endTimeInEpoc,
        accessToken,
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

    // Example code
    Workmate.getMovementTrail(
        this,
        "usereamil@example.com", // Email will be same while initializing workmate.
        1732127400,
        1732213799,
        token,
        new WMMovementTrailsListener() {
            @Override
            public void onMovementTrailsSuccess(MovementTrailsResponse movementTrailsResponse) {
                Log.d("Trail client", movementTrailsResponse.getData().toString());
                // movementTrailsResponse.getData().getAddress();
                // movementTrailsResponse.getData().getLatitude();
                // movementTrailsResponse.getData().getLongitude();
                // movementTrailsResponse.getData().getSpeed();
                // movementTrailsResponse.getData().getTimestamp();
            }

            @Override
            public void onMovementTrailsFailed(String error) {
                Log.d("Trail error", error);
            }
        }
    );

```

## Workmate get user activity

### Function name: `getUserActivity`

The function is designed to retrieve user activity the Workmate SDK. It facilitates managing activities performed by users, either linked to specific tasks or conducted independently of any task.

### Explanation

- **Parameters:**

  - `context`: Context (this).
  - `userEmail: <String>`: Pass the userEmail for which you want to retrieve the user activity .Admins have the ability to retrieve the user activity information for other users under their supervision.
  - `startTimeInEpoc : <Long>`: Sent start time in EPOCH.
  - `endTimeInEpoc : <Long>`: Sent stop time in EPOCH
  - `accessToken: <String>`: Pass teh access token recieved from auth.
  - `WMTaskListListener`: A callback to handle the success or failure of the task list call

- **Callbacks:**

  - `onUserActivitySuccess`: Displays a success message. Returns model class with all the details containing user activity
  - `onUserActivityFailed`: Displays an error message.

### implementation

Kotlin implementation

```groovy
    //Method signature
    Workmate.userActivity(
        context, userEmail, startTimeInEpoc, endTimeInEpoc, accessToken,
        userActivityListener = object : WMUserActivityListener {
            override fun onUserActivitySuccess(userActivityResponse: UserActivityResponse) {
                //Do something
            }

            override fun onUserActivityFailed(error: String) {
                //Do smething
            }
        }
    )

    //Example code
    val context = this // If inside an Activity, otherwise use applicationContext
    val userEmail = "user@example.com" //Same as workmate initialization method
    // Current time in seconds
    val startTimeInEpoch = System.currentTimeMillis() / 1000
    val endTimeInEpoch = startTimeInEpoch + 3600 // 1 hour later
    val accessToken = "your_access_token_here"

    Workmate.userActivity(
        context, userEmail, startTimeInEpoch, endTimeInEpoch, accessToken,
        object : WMUserActivityListener {
            override fun onUserActivitySuccess(userActivityResponse: UserActivityResponse) {
                Log.d("UserActivity", "Success: ${userActivityResponse.toString()}")
            }

            override fun onUserActivityFailed(error: String) {
                Log.e("UserActivity", "Failed: $error")
            }
        }
    )

```

Java implementation

```groovy
    //Method signature
    Workmate.userActivity(
        context, userEmail, startTimeInEpoc, endTimeInEpoc, accessToken,
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

    //Example code
    public static void callUserActivity(Context context) {
        String userEmail = "user@example.com"; //Same as workmate initialization method
        // Current time in seconds
        long startTimeInEpoch = System.currentTimeMillis() / 1000;
        long endTimeInEpoch = startTimeInEpoch + 3600; // 1 hour later
        String accessToken = "your_access_token_here";

        Workmate.userActivity(
            context, userEmail, startTimeInEpoch, endTimeInEpoch, accessToken,
            new WMUserActivityListener() {
                @Override
                public void onUserActivitySuccess(UserActivityResponse userActivityResponse) {
                    Log.d("UserActivity", "Success: " + userActivityResponse.toString());
                }

                @Override
                public void onUserActivityFailed(String error) {
                    Log.e("UserActivity", "Failed: " + error);
                }
            }
        );
    }
```

## Workmate calculate distance

### Function: `calculateDistance`

This function handles the calculation of distance information using the Workmate SDK. It considers various inputs, such as userEmail, type, and time, to compute the total distance between them.Admins have the ability to retrieve the distance information for other users under their supervision.This functionality is designed to allow admins to monitor the distance-related activities of users who are associated with them.

### Explanation

- **Parameters:**

  - `context`: Context (this).
  - `userEmail: <String>`: Pass the userEmail for which you want to retrieve the drive distance or odometer distance.Admins have the ability to retrieve the distance information for other users under their supervision.
  - `type: <String>`: Pass type: 1 for drive distance and type: 2 for odometer distance.
  - `startTimeInEpoc : <Long>`: Sent start time in EPOCH.
  - `endTimeInEpoc : <Long>`: Sent stop time in EPOCH
  - `accessToken: <String>`: Pass the access token recieved from auth.
  - `WMCalculateDistanceListener`: A callback to handle the success or failure of the calculate distance call.

- **Callbacks:**

  - `onCalculateDistanceSuccess`: Displays a success message.
  - `onCalculateDistanceFailed`: Displays an error message.

#### Implementation

Kotlin implementation

```groovy
    //Method signature
    Workmate.calculateDistance(
        context, userEmail, type, startTimeInEpoch, endTimeInEpoch, accessToken
        calculateDistanceListener = object : WMCalculateDistanceListener {
            override fun onCalculateDistanceSuccess(calculateDistanceResponse: CalculateDistanceResponse) {
                //Do something
            }

            override fun onCalculateDistanceFailed(error: String) {
                //Do smething
            }
        }
    )

    //Method example
    Workmate.calculateDistance(this,
        "useremail@example.com", //Email will be same as Workmate initialization call
        1, //type
        1732127400, //Start time in epoch
        1732213799, //end time in epoch
        token, //Token got from workmate initialization method
        object : WMCalculateDistanceListener {
            override fun onCalculateDistanceSuccess(calculateDistanceResponse: CalculateDistanceResponse?) {
                Log.d("Distance success", calculateDistanceResponse.toString())
            }

            override fun onCalculateDistanceFailed(error: String) {
                Log.d("Distance error", error)
            }
        })
```

Java implementation

```groovy
    //Method signature
    Workmate.calculateDistance(
        context, userEmail, type, startTimeInEpoch, endTimeInEpoch, accessToken
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

    //Method example
    Workmate.calculateDistance(
        this,
        "useremail@example.com", // Email will be the same as Workmate initialization call
        1, // type
        1732127400L, // Start time in epoch
        1732213799L, // End time in epoch
        token, // Token from Workmate initialization method
        new WMCalculateDistanceListener() {
            @Override
            public void onCalculateDistanceSuccess(CalculateDistanceResponse calculateDistanceResponse) {
                Log.d("Distance success", String.valueOf(calculateDistanceResponse));
            }

            @Override
            public void onCalculateDistanceFailed(String error) {
                Log.d("Distance error", error);
            }
        }
    );

```

### function: `getDeviceLocationData`

This functions hepls you to find device location details. See the following:

```groovy
    latitude, longitude, altitude, speed, bearing, locationProvider
```

#### Implementation

Kotlin Implementation

```groovy
    //Method signature
    Workmate.getDeviceLocationData(context, object : WMDeviceLocationListener {
        override fun onDeviceInfoSuccess(deviceLocationInfo: DeviceLocationInfo) {
            // Do something
        }

        override fun onDeviceInfoError(error: String) {
            // Do something
        }
    })

    //Meethod example
    Workmate.getDeviceLocationData(this, object : WMDeviceLocationListener {
        override fun onDeviceInfoSuccess(deviceLocationInfo: DeviceLocationInfo) {
            Log.d("location data","${deviceLocationInfo.latitude}")
            Log.d("location data","${deviceLocationInfo.longitude}")
            Log.d("location data","${deviceLocationInfo.altitude}")
            Log.d("location data","${deviceLocationInfo.speed}")
            Log.d("location data","${deviceLocationInfo.bearing}")
            Log.d("location data","${deviceLocationInfo.locationProvider}")

        }

        override fun onDeviceInfoError(error: String) {
            Log.d("location error","error")
        }
    })
```

Java implementation

```groovy
    //method Signature
    Workmate.getDeviceLocationData(context, new WMDeviceLocationListener() {
        @Override
        public void onDeviceInfoSuccess(DeviceLocationInfo deviceLocationInfo) {
            // Do something
        }

        @Override
        public void onDeviceInfoError(String error) {
            // Do something
        }
    });

    //Method example
    Workmate.getDeviceLocationData(this, new WMDeviceLocationListener() {
        @Override
        public void onDeviceInfoSuccess(DeviceLocationInfo deviceLocationInfo) {
            Log.d("location data", String.valueOf(deviceLocationInfo.getLatitude()));
            Log.d("location data", String.valueOf(deviceLocationInfo.getLongitude()));
            Log.d("location data", String.valueOf(deviceLocationInfo.getAltitude()));
            Log.d("location data", String.valueOf(deviceLocationInfo.getSpeed()));
            Log.d("location data", String.valueOf(deviceLocationInfo.getBearing()));
            Log.d("location data", String.valueOf(deviceLocationInfo.getLocationProvider()));
        }

        @Override
        public void onDeviceInfoError(String error) {
            Log.d("location error", error);
        }
    });

```

### Explanation

- **Parameters:**

  - `context`: Context (this)

- **Parameters:**
  - `onDeviceInfoSuccess`: On device location success.
  - `onDeviceIntoError`: On device location error.

## Workmate validate geo fence

### Function name: `validateGeoFence`

The validateGeoFence method checks whether a user's current location falls within a predefined geofenced area. It takes the user's latitude, longitude, a list of geofence coordinates, and a radius to determine if the user is inside or outside the defined region. The result is passed to a listener interface, which handles success or failure cases.

### Explanation

Parameters & Explanation

- `context: Type: Context`
  Description: The Android application context used for geofencing validation. It is required for accessing system resources like location services.
- `userLatitude: Type: Double (double in Java)`
  Description: The latitude of the user's current location. This is used to determine if the user is inside or outside the geofence area.
- `userLongitude: Type: Double (double in Java)`
  Description: The longitude of the user's current location. This, along with the latitude, defines the user's location for validation against the geofence.
- `geoFenceCoordinates: Type: List<WMGeoFenceCoordinates>`
  Description: A list of WMGeoFenceCoordinates that define the geofence area. Each coordinate represents a point (latitude and longitude) that helps outline the geofence boundary. CURRENTLY ONLY ACCEPTING 1 LIST ITEM
- `radius: Type: Int (int in Java)`
  Description: The geofence radius in meters. The user is validated based on whether their location falls within this radius from the geofence coordinates.
- `listener: Type: WMGeoFenceValidationListener`
  Description: A listener interface that handles success or failure results.
  It provides methods
  - `onGeoFenceValidationSuccess()` for successful validation.
  - `onGeoFenceValidationFailed(String error)` for failure scenarios.

Kotlin Implementation

```groovy

    //Method signature
    Workmate.validateGeoFence(
        context,
        userLatitude,
        userLongitude,
        geoFenceCoordinates = listOf(
            WMGeoFenceCoordinates()
        ),
        radius, // 500 meters radius
        WMGeoFenceValidationListener {
            override fun onGeoFenceValidationFailed(error: String) {
                // code here
            }

            override fun onGeoFenceValidationSuccess() {
                //code here
            }
        }
    )

    //Example implementation
    Workmate.validateGeoFence(
        context = this,
        userLatitude = 37.7749, // Dummy user latitude (San Francisco)
        userLongitude = -122.4194, // Dummy user longitude (San Francisco)
        geoFenceCoordinates = listOf(
            WMGeoFenceCoordinates(
                37.7740,  // Dummy geofence latitude (nearby location)
                -122.4190 // Dummy geofence longitude (nearby location)
            )
        ),
        radius = 500, // 500 meters radius
        listener = object : WMGeoFenceValidationListener {
            override fun onGeoFenceValidationFailed(error: String) {
                println("Geofence validation failed: $error")
            }

            override fun onGeoFenceValidationSuccess() {
                println("Geofence validation successful!")
            }
        }
    )

```

Java Implementation

```groovy
    //Java syntax
    Workmate.validateGeoFence(
        this,
        userlatitude, //Can be null
        userLongitude, //Can be null
        Arrays.asList(
            new WMGeoFenceCoordinates(
                lat,
                lng
            )
        ),
        radius, //in meters
        new WMGeoFenceValidationListener() {
            @Override
            public void onGeoFenceValidationFailed(String error) {
                //do something
            }

            @Override
            public void onGeoFenceValidationSuccess() {
                //Do something
            }
        }
    );

    //Java Sample
    Workmate.validateGeoFence(
        this,
        37.7749, // Dummy user latitude (San Francisco)
        -122.4194, // Dummy user longitude (San Francisco)
        Arrays.asList(
            new WMGeoFenceCoordinates(
                37.7740,  // Dummy geofence latitude (nearby location)
                -122.4190 // Dummy geofence longitude (nearby location)
            )
        ),
        500, // 500 meters radius
        new WMGeoFenceValidationListener() {
            @Override
            public void onGeoFenceValidationFailed(String error) {
                System.out.println("Geofence validation failed: " + error);
            }

            @Override
            public void onGeoFenceValidationSuccess() {
                System.out.println("Geofence validation successful!");
            }
        }
    );
```

# Workmate help section

## Sample code to convert epoch current time to epoch.

Kotlin sample code

```groovy

    fun convertToEpochUsingLocalDateTime(dateTime: String, pattern: String): Long {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val localDateTime = LocalDateTime.parse(dateTime, formatter)
        return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() // Returns seconds
    }

    fun convertToEpochUsingSimpleDateFormat(dateTime: String, pattern: String): Long {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.parse(dateTime)?.time?.div(1000) ?: 0 // Converts milliseconds to seconds
    }
```

Java sample code

```groovy
    public static long convertToEpochUsingLocalDateTime(String dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond(); // Returns seconds
    }

    public static long convertToEpochUsingSimpleDateFormat(String dateTime, String pattern) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        formatter.setTimeZone(TimeZone.getDefault());
        Date date = formatter.parse(dateTime);
        return date != null ? date.getTime() / 1000 : 0;
        //Converts milliseconds to second
    }
```

## Sample code to get current time in EPOCH from an UI

Getting time in epoch if you are using Data dialog or any other UI configurations. Follow the code below for help

Kotlin sample

```groovy
    //Example method
    private fun showDateTimePicker(onDateTimeSelected: (String, String, Long) -> Unit) {
        val calendar = Calendar.getInstance()

        // Date picker dialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"

                // Time picker dialog
                val timePickerDialog = TimePickerDialog(
                    this, { _, hourOfDay, minute ->
                        // Custom dialog for seconds selection
                        val secondsPickerDialog = AlertDialog.Builder(this)
                        val numberPicker = NumberPicker(this).apply {
                            minValue = 0
                            maxValue = 59
                            value = calendar.get(Calendar.SECOND)
                        }
                        secondsPickerDialog.setTitle("Select Seconds").setView(numberPicker)
                            .setPositiveButton("OK") { _, _ ->
                                val selectedSecond = numberPicker.value
                                val selectedTime = String.format(
                                    "%02d:%02d:%02d", hourOfDay, minute, selectedSecond
                                )

                                // Convert to epoch
                                val epochTime = convertToEpoch(selectedDate, selectedTime)

                                // Callback with date, time, and epoch
                                onDateTimeSelected(selectedDate, selectedTime, epochTime)
                            }.setNegativeButton("Cancel", null).show()
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }


    private fun convertToEpoch(date: String, time: String): Long {
        val dateTime = "$date $time"
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return try {
            val parsedDate = sdf.parse(dateTime)
            (parsedDate?.time ?: 0L) / 1000 // Convert milliseconds to seconds
        } catch (e: Exception) {
            AppDialog.showToast(this, "Error while converting to epoch")
            0L
        }
    }
```

Java sample

```groovy
    private void showDateTimePicker(OnDateTimeSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();

        // Date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;

                // Time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (timeView, hourOfDay, minute) -> {
                        // Custom dialog for seconds selection
                        AlertDialog.Builder secondsPickerDialog = new AlertDialog.Builder(this);
                        NumberPicker numberPicker = new NumberPicker(this);
                        numberPicker.setMinValue(0);
                        numberPicker.setMaxValue(59);
                        numberPicker.setValue(calendar.get(Calendar.SECOND));

                        secondsPickerDialog.setTitle("Select Seconds")
                            .setView(numberPicker)
                            .setPositiveButton("OK", (dialog, which) -> {
                                int selectedSecond = numberPicker.getValue();
                                String selectedTime = String.format(
                                    "%02d:%02d:%02d", hourOfDay, minute, selectedSecond
                                );

                                // Convert to epoch
                                long epochTime = convertToEpoch(selectedDate, selectedTime);

                                // Callback with date, time, and epoch
                                listener.onDateTimeSelected(selectedDate, selectedTime, epochTime);
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                );
                timePickerDialog.show();
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private long convertToEpoch(String date, String time) {
        String dateTime = date + " " + time;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        try {
            Date parsedDate = sdf.parse(dateTime);
            return (parsedDate != null ? parsedDate.getTime() : 0L) / 1000; // Convert milliseconds to seconds
        } catch (Exception e) {
            AppDialog.showToast(this, "Error while converting to epoch");
            return 0L;
        }
    }

    // Callback Interface
    public interface OnDateTimeSelectedListener {
        void onDateTimeSelected(String date, String time, long epoch);
    }

```
