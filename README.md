# Workmate SDK implementation

This document will help you implement Workmate fleet management SDK in your application. Workmate SDK provides lots of methods that can be integrated inside your application. Here is step by step approach how to get started with Workmate SDK.


## ⚠️ Breaking Change Notice

> **Important**
>
> Starting from this version, password-based initialization has been removed from the SDK.
> 
> Please update your codebase to remove the password parameter and migrate to the new initialization mechanism before upgrading.

## Table of content

- [Getting started](#getting-started)
- [Initialization](#workmate-initialization)
- [Manage Workday](#workmate-manage-workday)
- [Client Check-In-Out](#workmate-client-check-in-out)
- [Client Check-In](#workmate-client-check-in)
- [Client Check-Out](#workmate-client-check-out)
- [Get Movement Trail](#workmate-get-movement-trail)
- [Get User Activity](#workmate-get-user-activity)
- [Calculate Distance](#workmate-calculate-distance)
- [Get Device Location Details](#workmate-get-device-location)
- [Refresh token](#workmate-refresh-token)
- [Geo-Fence validation](#workmate-validate-geo-fence)
- [Get Task List](#workmate-get-task-list)
- [Get Client List](#workmate-get-client-list)
- [Task Check-In-out](#workmate-task-check-in-out)
- [Task Check-In](#workmate-task-check-in)
- [Task Check-Out](#workmate-task-check-out)
- [Route Optimization](#workmate-route-optimization)
- [Error Code](#workmate-error-code)
- [Error Response](#workmate-error-response)

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

- If you are using Gradle.build.kts for latest Android version releases. Choose below format.

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

- After that, go to your app level build.gradle file and add below dependency and build your application

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
  - `useremail : <String>`: provide user email. Email will be used for implicit login automatically.

- **Callbacks:**

  - `onAuthSuccess`: Handles successful authentication and stores the `accessToken`.
  - `onAuthFailure`: Handles authentication failure and displays the error message with status code.

Below is the method signature along with an example.

- Kotlin implementation

```kotlin

    //Method signature
    Workmate.initialize(
        context, clientSecret , clientId , email,
        listener = object : WMAuthListener {
            override fun onAuthSuccess(response: AuthResponse) {
                //Do something
            }
          
            override fun onAuthFailure(errorResponse: ErrorResponse) {
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
        listener = object : WMAuthListener {
            override fun onAuthSuccess(response: AuthResponse) {
                Log.d("Auth Response","${response.accessToken}") //Save this access token as you'll be using it for other Workmate Services.
            }
          
          
            override fun onAuthFailure(errorResponse: ErrorResponse) {
              Log.d("Auth Error", errorResponse.toString());
            }
        }
    )

```

- Java implementation

```java
    //Method signature
        Workmate.initialize(
            context, clientSecret, clientId, email,
            new WMAuthListener() {
                @Override
                public void onAuthSuccess(AuthResponse response) {
                    // Do something
                }
    
                @Override
                public void onAuthFailure(ErrorResponse errorResponse) {
                    // Do something
                }
            }
        );

        //Method example
        Workmate.initialize(
                this,
                "Demo client Id", // Replace with the real one provided by the Mappls team.
                "Demo client secret", // Replace with the real one provided by the Mappls team.
                "useremail@example.com", // Replace with the real user email.
                new WMAuthListener() {
                    @Override
                    public void onAuthSuccess(AuthResponse response) {
                        Log.d("Auth Response", response.getAccessToken()); // Save this access token as you'll be using it for other Workmate Services.
                    }

                    @Override
                    public void onAuthFailure(ErrorResponse errorResponse) {
                        Log.d("Auth Error", errorResponse.toString()); // Will show any error caused by this method.
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
  - `onAuthFailure`: Handles authentication failure and displays the error message with status code.

Kotlin Implementation

```kotlin
    //Method Signature
    Workmate.refreshToken(context, object : WMAuthListener {
        override fun onAuthSuccess(response: AuthResponse) {
            // Do something
        }

        override fun onAuthFailure(errorResponse: ErrorResponse) {
            // Do something
        }
    })

    //Method example
    Workmate.refreshToken(this, object : WMAuthListener {
        override fun onAuthSuccess(response: AuthResponse) {
            Log.d("refresh success", "${response.accessToken}")
        }

        override fun onAuthFailure(errorResponse: ErrorResponse) {
            Log.d("refresh error", errorResponse.toString())
        }
    })
```

Java Implementation

```java
    //method signature
      Workmate.refreshToken(context, new WMAuthListener() {
          @Override
          public void onAuthSuccess(AuthResponse response) {
              // Do something
          }
  
          @Override
          public void onAuthFailure(ErrorResponse errorResponse) {
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
          public void onAuthFailure(ErrorResponse errorResponse) {
              Log.d("refresh error", String.valueOf(errorResponse));
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
  - `onSessionError`: Invoked when a session-related error occurs, logs the error and displays the corresponding status code and message.

  Below is the method signature along with an example

Kotlin implementation

```kotlin

    //Method signature
    Workmate.manageWorkday(
        context,
        @Nullable attendanceId,
        activityId (optional),
        formData,
        token,
        object : WMSessionListener {
            override fun onSessionStarted(attendanceResponse: AttendanceResponse) {
                //Do something
                //attendanceResponse (id, message, status) here id is your attendance id.
            }

            override fun onSessionEnded(attendanceResponse: AttendanceResponse) {
                //Do something
            }

            override fun onSessionError(sessionError: ErrorResponse) {
                //Do something
            }
        }
    )

    //Method example
    //Below is an example of start workday
    Workmate.manageWorkday(
        this, //Context
        null, //Pass null to start workday. If workday already started method will not start new workday but gives you pending workday instead.
        null,
        null,
        accessToken, //Provided by initialize method.
        object : WMSessionListener {
            override fun onSessionStarted(attendanceResponse: AttendanceResponse) {
                //Do something
                Log.d("Attendance id","${attendanceResponse.id}") //Suppose 123456789 is the id received from method.
                Log.d("Attendance message","${attendanceResponse.message}")
                Log.d("Attendance status","${attendanceResponse.status}")
            }

            override fun onSessionEnded(attendanceResponse: AttendanceResponse) {
                //Do something
                //This method will not call in this case.
            }

            override fun onSessionError(sessionError: ErrorResponse) {
                Log.d("Attendance error", sessionError.toString())
            }
        }
    )

    //Method example
    //Below is an example of stop workday
        Workmate.manageWorkday(
        this, //Context
        12345678, //id received while starting workday
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

            override fun onSessionError(sessionError: ErrorResponse) {
                Log.d("Attendance error", sessionError.toString())
            }
        }
    )

```

Java implementation

```java

    //Method signature
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
        public void onSessionError(ErrorResponse sessionError) {
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
            public void onSessionError(ErrorResponse sessionError) {
                Log.d("Attendance Error", String.valueOf(sessionError));
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
            public void onSessionError(ErrorResponse sessionError) {
                Log.d("Attendance Error", String.valueOf(sessionError));
            }
        }
    );

```


## Workmate Client Check-in-out

**Deprecated:** This method is no longer recommended. Please use `clientCheckIn() and clientCheckOut()` instead.

### method name: `clientCheckInOut`

This function handles the process of client check-in and check-out information using the Workmate SDK.

### Explanation

- **Parameters:**

  - `context`: Context (this)
  - `clientId : <Int>`: After client check-in. You'll get an clientId pass here for <id> parameter.

  ```Text
    In above implementation clientId is the checkinOut clientId.
  - While checkin pass null in clientCheckId
  - While checkout pass id value in clientCheckId retrieved from onClientCheckInOutSuccess method.
  ```

  - `accessToken : <String>`: The access token obtained from the authentication process.
  - `clientId : <String>`: This parameter represents the unique identifier for a client. The clientId is associated with the corresponding Workmate client.
  - `clientCheckId: <Int>`: Client check-in retrieved from success callback. Pass null to new check-in
  - `formData: <JSONObject>`: The form data required when checking in or checking out of a client. If the workflow is enabled, provide the formData that corresponds to the specified ContextId. This object typically contains key-value pairs representing the data fields associated with the client . If the workflow is disabled, pass null.
  - `clientCheckInOutListener`: A callback to handle the success or failure of the check-in and check-out process.

- **Callbacks:**

  - `onClientCheckInOutSuccess`: Displays a success message.
  - `onClientCheckInOutFailed`: Invoked when a client check-in or check-out error occurs. Logs the error and displays the corresponding status code and message.

  Below is the method implementation along with example

#### Implementation

Kotlin implementation

```kotlin

    //Method signature
    Workmate.clientCheckInOut(
        Context, clientId, clientCheckId, formData, accessToken,
        clientCheckInOutListener = object : WMClientCheckInOutListener {
            override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                //Do something
            }

            override fun onClientCheckInOutFailed(errorResponse: ErrorResponse) {
                //Do something
            }
        }
    )

    //Method example
    //Performing client check-in
    Workmate.clientCheckInOut(
        this,
        1234567, //Client id obtained from Workmate
        null, //pass null for new client check-in.
        null,
        accessToken, //token received from workmate initialization
        clientCheckInOutListener = object : WMClientCheckInOutListener {
            override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                //Do something
                Log.d("client id:", "${clientCheckInOutResponse.id}") //Suppose 11221122 received from server
            }

            override fun onClientCheckInOutFailed(errorResponse: ErrorResponse) {
                //Do something
                Log.d("Client Error:",errorResponse.toString())
            }
        }
    )

    //Performing client check-out
        Workmate.clientCheckInOut(
        this,
        1234567, //Client id obtained from Workmate
        11221122, // Pass the clientCheckId received from check-in
        null,
        accessToken, //token received from workmate initialization
        clientCheckInOutListener = object : WMClientCheckInOutListener {
            override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                Log.d("client id:", "${clientCheckInOutResponse.id}")
            }

            override fun onClientCheckInOutFailed(errorResponse: ErrorResponse) {
                Log.d("Client Error:",errorResponse.toString())
            }
        }
    )

```

Java implementation

```java
    //Method signature
    Workmate.clientCheckInOut(
        Context, clientId, clientCheckId, formData, accessToken,
        new WMClientCheckInOutListener() {
            @Override
            public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
                // Do something
            }

            @Override
            public void onClientCheckInOutFailed(ErrorResponse error) {
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
        accessToken, //token received from workmate initialization
        new WMClientCheckInOutListener() {
            @Override
            public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
                // Do something
                Log.d("client id:", String.valueOf(clientCheckInOutResponse.getId())); // Suppose 11221122 received from server
            }

            @Override
            public void onClientCheckInOutFailed(ErrorResponse error) {
                // Do something
                Log.d("Client Error:",String.valueOf(error));
            }
        }
    );

    // Client check out example
    Workmate.clientCheckInOut(
        this,
        1234567, // Client id obtained from Workmate
        11221122, // Pass the clientCheckId received from check-in
        null,
        accessToken, //token received from workmate initialization
        new WMClientCheckInOutListener() {
            @Override
            public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
                // Do something
                Log.d("client id:", String.valueOf(clientCheckInOutResponse.getId()));
            }

            @Override
            public void onClientCheckInOutFailed(ErrorResponse error) {
                // Do something
                Log.d("Client Error:",String.valueOf(error));
            }
        }
    );

```

## Workmate Client Check-in

### method name: `clientCheckIn`

This function handles the process of client check-in by submitting relevant details such as location and timestamp to the server.

### Explanation

- **Parameters:**

  - `context`: Context (this)
  - `clientId : <Int>`: Pass Client id here.
  - `formData: <JSONObject>`: The form data required when checking in or checking out of a client. If the workflow is enabled, provide the formData that corresponds to the specified ContextId. This object typically contains key-value pairs representing the data fields associated with the client . If the workflow is disabled, pass null.
  - `accessId : <String>`: The access token obtained from the authentication process.
  - `clientCheckInOutListener`: A callback to handle the success or failure of the check-in and check-out process.

- **Callbacks:**

  - `onClientCheckInOutSuccess`: Invoked when the client check-in or check-out operation is successfully completed.
    Returns a ClientCheckInOutResponse object containing the status and details of the operation.
  - `onClientCheckInOutFailed`: Invoked when the client check-in or check-out operation fails.
    Returns an ErrorResponse object containing the status code and error message, which can be used for logging or notifying the user.

  Below is the method implementation along with example

#### Implementation

Kotlin implementation

```kotlin

    //Method signature
    Workmate.clientCheckIn(context, 
        clientId, 
        formData, 
        accessId,
        clientCheckInOutListener = object : WMClientCheckInOutListener {
            override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                //Do something
            }

            override fun onClientCheckInOutFailed(errorResponse: ErrorResponse) {
                //Do something
            }
        }
    )

    //Method example
    //Performing client check-in
    Workmate.clientCheckIn(
        this,
        1234567, //Client id obtained from Workmate
        null, //pass null for new client check-in.
        accessId, //token received from workmate initialization
        clientCheckInOutListener = object : WMClientCheckInOutListener {
            override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                //Do something
                Log.d("client id:", "${clientCheckInOutResponse.id}") //Suppose 11221122 received from server
            }

            override fun onClientCheckInOutFailed(errorResponse: ErrorResponse) {
                //Do something
                Log.d("Client Error:",errorResponse.toString())
            }
        }
    )

```

Java implementation

```java
    //Method signature
    Workmate.clientCheckIn(
        context, clientId, formData, accessId,
        new WMClientCheckInOutListener() {
            @Override
            public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
                // Do something
            }

            @Override
            public void onClientCheckInOutFailed(ErrorResponse error) {
                // Do something
            }
        }
    );

    //Client check in example
    Workmate.clientCheckIn(
        this,
        1234567, // Client id obtained from Workmate
        null, // Pass null for new client check-in
        accessId, //token received from workmate initialization
        new WMClientCheckInOutListener() {
            @Override
            public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
                // Do something
                Log.d("client id:", String.valueOf(clientCheckInOutResponse.getId())); // Suppose 11221122 received from server
            }

            @Override
            public void onClientCheckInOutFailed(ErrorResponse error) {
                // Do something
                Log.d("Client Error:",String.valueOf(error));
            }
        }
    );

```



## Workmate Client Check-out

### method name: `clientCheckOut`

This function handles the process of client check-out by submitting relevant details such as location and timestamp to the server.

### Explanation

- **Parameters:**

  - `context`: Context (this)
  - `clientId : <Int>`: After client check-in. You'll get an clientId pass here for <id> parameter.
  - `clientCheckId: <Int>`: Client check-in retrieved from success callback. Pass null to new check-in.
  
  ```Text
    In above implementation clientId is the checkinOut clientId.
  - While checkin pass null in clientCheckId
  - While checkout pass id value in clientCheckId retrieved from onClientCheckInOutSuccess method.
  ```
  
  - `formData: <JSONObject>`: The form data required when checking in or checking out of a client. If the workflow is enabled, provide the formData that corresponds to the specified ContextId. This object typically contains key-value pairs representing the data fields associated with the client . If the workflow is disabled, pass null.
  - `accessId : <String>`: The access token obtained from the authentication process.
  - `clientCheckInOutListener`: A callback to handle the success or failure of the check-in and check-out process.

- **Callbacks:**

  - `onClientCheckInOutSuccess`: Invoked when the client check-in or check-out operation is successfully completed.
    Returns a ClientCheckInOutResponse object containing the status and details of the operation.
  - `onClientCheckInOutFailed`: Invoked when the client check-in or check-out operation fails.
    Returns an ErrorResponse object containing the status code and error message, which can be used for logging or notifying the user.

  Below is the method implementation along with example

#### Implementation

Kotlin implementation

```kotlin

    //Method signature
    Workmate.clientCheckOut(
        context, clientId, clientCheckId, formData, accessId,
        clientCheckInOutListener = object : WMClientCheckInOutListener {
            override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                //Do something
            }

            override fun onClientCheckInOutFailed(errorResponse: ErrorResponse) {
                //Do something
            }
        }
    )

    //Method example
    //Performing client check-out
        Workmate.clientCheckOut(
        this,
        1234567, //Client id obtained from Workmate
        11221122, // Check-in ID obtained from a successful check-in operation.
        null,
        accessId, //token received from workmate initialization
        clientCheckInOutListener = object : WMClientCheckInOutListener {
            override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                Log.d("client id:", "${clientCheckInOutResponse.id}")
            }

            override fun onClientCheckInOutFailed(errorResponse: ErrorResponse) {
                Log.d("Client Error:",errorResponse.toString())
            }
        }
    )

```

Java implementation

```java
    //Method signature
    Workmate.clientCheckOut(
        context, clientId, clientCheckId, formData, accessId,
        new WMClientCheckInOutListener() {
            @Override
            public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
                // Do something
            }

            @Override
            public void onClientCheckInOutFailed(ErrorResponse error) {
                // Do something
            }
        }
    );


    // Client check out example
    Workmate.clientCheckOut(
        this,
        1234567, // Client id obtained from Workmate
        11221122, // Check-in ID obtained from a successful check-in operation.
        null,
        accessId, //token received from workmate initialization
        new WMClientCheckInOutListener() {
            @Override
            public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
                // Do something
                Log.d("client id:", String.valueOf(clientCheckInOutResponse.getId()));
            }

            @Override
            public void onClientCheckInOutFailed(ErrorResponse error) {
                // Do something
                Log.d("Client Error:",String.valueOf(error));
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
  - `userId: <String>`: Pass the userEmail for which you want to retrieve the movement Trails. Admins have the ability to retrieve the movement Trails information for other users under their supervision.
  - `startTimeInEpoc : <Long>`: Sent start time in EPOCH.
  - `stopTimeInEpoch : <Long>`: Sent stop time in EPOCH.
  - `token: <String>`: Pass the access token received from auth.
  - `WMMovementTrailsListener`: A callback to handle the success or failure of the Movement Trails call

- **Callbacks:**

  - `onMovementTrailsSuccess`: Success call will give you a response with following values. `address`, `latitude`, `longitude`, `speed`, `timestamp`
  - `onMovementTrailsFailed`: Invoked when retrieving the movement trail fails. Logs the error and displays the corresponding status code and message.

#### implementation

Kotlin implementation

```kotlin

    //Method signature
    Workmate.getMovementTrail(
        context, userId, startTimeInEpoc, stopTimeInEpoch, token,
        userActivityListener = object : WMMovementTrailsListener {
            override fun onMovementTrailsSuccess(userActivityResponse: UserActivityResponse) {
                //Do something
            }

            override fun onMovementTrailsFailed(error: ErrorResponse) {
                //Do something
            }
        }
    )

    //Example code
    Workmate.getMovementTrail(
        this,
        "useremail@example.com", // Email will be same while initilialize workmate.
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

            override fun onMovementTrailsFailed(error: ErrorResponse) {
                Log.d("Trail error", error.toString())
            }

        }
    )
```

Java implementation

```java
    // Method signature
      Workmate.getMovementTrail(
          context,
          userEmail,
          startTimeInEpoc,
          endTimeInEpoc,
          token,
          new WMMovementTrailsListener() {
              @Override
              public void onMovementTrailsSuccess(UserActivityResponse userActivityResponse) {
                  // Do something
              }
  
              @Override
              public void onMovementTrailsFailed(ErrorResponse error) {
                  // Do something
              }
          }
      );
      
      // Example code
        Workmate.getMovementTrail(
            this,
            "useremail@example.com", // Email will be same while initializing workmate.
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
                public void onMovementTrailsFailed(ErrorResponse error) {
                    Log.d("Trail error", String.valueOf(error));
                }
            });
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
  - `accessToken: <String>`: Pass the access token received from auth.
  - `WMUserActivityListener`: A callback to handle the success or failure of the task list call

- **Callbacks:**

  - `onUserActivitySuccess`: Displays a success message. Returns model class with all the details containing user activity
  - `onUserActivityFailed`: Displays an error message with the status code.

### implementation

Kotlin implementation

```kotlin
    //Method signature
    val context = this // If inside an Activity, otherwise use applicationContext
    val userEmail = "user@example.com" //Same as workmate initialization method
    // Current time in seconds
    val startTimeInEpoch = System.currentTimeMillis() / 1000
    val endTimeInEpoch = startTimeInEpoch + 3600 // 1 hour later
    val accessToken = "your_access_token_here"
    Workmate.getUserActivity(
        context, userEmail, startTimeInEpoc, endTimeInEpoc, accessToken,
        userActivityListener = object : WMUserActivityListener {
            override fun onUserActivitySuccess(userActivityResponse: UserActivityResponse) {
                //Do something
            }

            override fun onUserActivityFailed(error: ErrorResponse) {
                //Do something
            }
        }
    )

    //Example code


    Workmate.getUserActivity(
        context, userEmail, startTimeInEpoch, endTimeInEpoch, accessToken,
        object : WMUserActivityListener {
            override fun onUserActivitySuccess(userActivityResponse: UserActivityResponse) {
                Log.d("UserActivity", "Success: ${userActivityResponse.toString()}")
            }

            override fun onUserActivityFailed(error: ErrorResponse) {
                Log.e("UserActivity", "Failed: ${error.toString()}")
            }
        }
    )

```

Java implementation

```java
      //Method signature
      Workmate.getUserActivity(
              context, userEmail, startTimeInEpoc, endTimeInEpoc, accessToken,
              new WMUserActivityListener() {
                  @Override
                  public void onUserActivitySuccess(UserActivityResponse userActivityResponse) {
                      // Do something
                  }
  
                  @Override
                  public void onUserActivityFailed(ErrorResponse error) {
                      // Do something
                  }
              }
      );
  
      //Example code
          String userEmail = "user@example.com"; //Same as workmate initialization method
          // Current time in seconds
          long startTimeInEpoch = System.currentTimeMillis() / 1000;
          long endTimeInEpoch = startTimeInEpoch + 3600; // 1 hour later
          String accessToken = "your_access_token_here";
  
          Workmate.getUserActivity(
                  context, userEmail, startTimeInEpoch, endTimeInEpoch, accessToken,
                  new WMUserActivityListener() {
                      @Override
                      public void onUserActivitySuccess(UserActivityResponse userActivityResponse) {
                          Log.d("UserActivity", "Success: " + userActivityResponse.toString());
                      }
  
                      @Override
                      public void onUserActivityFailed(ErrorResponse error) {
                          Log.e("UserActivity", "Failed: " + error.toString());
                      }
                  }
          );
```

## Workmate calculate distance

### Function: `calculateDistance`

This function handles the calculation of distance information using the Workmate SDK. It considers various inputs, such as userEmail, type, and time, to compute the total distance between them.Admins have the ability to retrieve the distance information for other users under their supervision.This functionality is designed to allow admins to monitor the distance-related activities of users who are associated with them.

### Explanation

- **Parameters:**

  - `context`: Context (this).
  - `userId: <String>`: Pass the userEmail for which you want to retrieve the drive distance or odometer distance.Admins have the ability to retrieve the distance information for other users under their supervision.
  - `type: <String>`: Pass type: 1 for drive distance and type: 2 for odometer distance.
  - `startTimeInEpoc : <Long>`: Sent start time in EPOCH.
  - `endTimeInEpoc : <Long>`: Sent stop time in EPOCH
  - `token: <String>`: Pass the access token received from auth.
  - `WMCalculateDistanceListener`: A callback to handle the success or failure of the calculate distance call.

- **Callbacks:**

  - `onCalculateDistanceSuccess`: Displays a success message.
  - `onCalculateDistanceFailed`: Invoked when distance calculation fails. Logs the error and displays the corresponding status code and message.

#### Implementation

Kotlin implementation

```kotlin
    //Method signature
    Workmate.calculateDistance(
        context, userId, type, startTimeInEpoch, endTimeInEpoch, accessToken,
        listener = object : WMCalculateDistanceListener {
            override fun onCalculateDistanceSuccess(calculateDistanceResponse: CalculateDistanceResponse) {
                //Do something
            }

            override fun onCalculateDistanceFailed(error: ErrorResponse) {
                //Do something
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

            override fun onCalculateDistanceFailed(error: ErrorResponse) {
                Log.d("Distance error", error.toString())
            }
        })
```

Java implementation

```java
      //Method signature
      Workmate.calculateDistance(
              context, userId, type, startTimeInEpoch, endTimeInEpoch, token,
      new WMCalculateDistanceListener() {
          @Override
          public void onCalculateDistanceSuccess(CalculateDistanceResponse calculateDistanceResponse) {
              // Do something
          }
  
          @Override
          public void onCalculateDistanceFailed(ErrorResponse error) {
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
                  public void onCalculateDistanceFailed(ErrorResponse error) {
                      Log.d("Distance error", String.valueOf(error));
                  }
              }
      );

```

### function: `getDeviceLocationData`

This functions helps you to find device location details. See the following:

- **Parameters**

  - `context` :Context (this)

- **Explanation**

  - `onDeviceInfoSuccess` :Invoked when device location data is successfully retrieved.
  - `onDeviceInfoError` :Invoked when an error occurs while retrieving device location data. Logs the error and displays the corresponding status code and message.

```text
    latitude, longitude, altitude, speed, bearing, locationProvider
```

#### Implementation

Kotlin Implementation

```kotlin
    //Method signature
    Workmate.getDeviceLocationData(context, object : WMDeviceLocationListener {
        override fun onDeviceInfoSuccess(deviceLocationInfo: DeviceLocationInfo) {
            // Do something
        }

        override fun onDeviceInfoError(error: ErrorResponse) {
            // Do something
        }
    })

    //Method example
    Workmate.getDeviceLocationData(this, object : WMDeviceLocationListener {
        override fun onDeviceInfoSuccess(deviceLocationInfo: DeviceLocationInfo) {
            Log.d("location data","${deviceLocationInfo.latitude}")
            Log.d("location data","${deviceLocationInfo.longitude}")
            Log.d("location data","${deviceLocationInfo.altitude}")
            Log.d("location data","${deviceLocationInfo.speed}")
            Log.d("location data","${deviceLocationInfo.bearing}")
            Log.d("location data","${deviceLocationInfo.locationProvider}")

        }

        override fun onDeviceInfoError(error: ErrorResponse) {
            Log.d("location error",error.toString())
        }
    })
```

Java implementation

```java
      //method Signature
      Workmate.getDeviceLocationData(context, new WMDeviceLocationListener() {
          @Override
          public void onDeviceInfoSuccess(DeviceLocationInfo deviceLocationInfo) {
              // Do something
          }
  
          @Override
          public void onDeviceInfoError(ErrorResponse error) {
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
          public void onDeviceInfoError(ErrorResponse error) {
              Log.d("location error", error.toString());
          }
      });

```

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
  - `onGeoFenceValidationFailed(ErrorResponse error)` for failure scenarios.

Kotlin Implementation

```kotlin

    //Method signature
    Workmate.validateGeoFence(
        context,
        userLatitude,
        userLongitude,
        geoFenceCoordinates = listOf(
            WMGeoFenceCoordinates()
        ),
        radius, // 500 meters radius
        object : WMGeoFenceValidationListener {
            override fun onGeoFenceValidationFailed(error: ErrorResponse) {
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
            override fun onGeoFenceValidationFailed(error: ErrorResponse) {
                println("Geofence validation failed: $error")
            }

            override fun onGeoFenceValidationSuccess() {
                println("Geofence validation successful!")
            }
        }
    )

```

Java Implementation

```java
    //Java syntax
    Workmate.validateGeoFence(
            this,
            userLatitude, 
            userLongitude,
            Arrays.asList(
                    new WMGeoFenceCoordinates(
                            lat,
                            lng
                    )
            ),
            radius, //in meters
            new WMGeoFenceValidationListener() {
                @Override
                public void onGeoFenceValidationFailed(ErrorResponse error) {
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
                public void onGeoFenceValidationFailed(ErrorResponse error) {
                    System.out.println("Geofence validation failed: " + error);
                }

                @Override
                public void onGeoFenceValidationSuccess() {
                    System.out.println("Geofence validation successful!");
                }
            }
    );
```

## Workmate get task list

### Function: `getTaskList`

To get all tasks that are created.  Follow the code below for help

- **Parameters:**

  - `context`: Context (this)
  - `accessId <String>` : Pass the access token received from auth.
  - `WMTaskListListener`: A callback to handle the success or failure of get task list call.

- **Callbacks:**

  - `onTaskListSuccess`: Invoked when the task list is successfully retrieved.
    Returns a List of TaskData objects representing the available tasks.
  - `onTaskListError`: Invoked when the task list retrieval fails.
    Returns an ErrorResponse object containing the status code and error message, which can be used for logging or informing the user.

### Explanation

Kotlin sample

```kotlin
     Workmate.getTaskList(context,
        token,
        object : WMTaskListListener{
          override fun onTaskListSuccess(taskList: List<TaskData>) {
            //Do something
          }
      
          override fun onTaskListError(errorResponse: ErrorResponse) {
            //Do something
          }
      
        }
     )
      //Method Example
      Workmate.getTaskList(this,
        token,
        object :WMTaskListListener{
          override fun onTaskListSuccess(taskList: List<TaskData>) {
            Log.d("Task List:", taskList.toString())
          }
      
          override fun onTaskListError(error: ErrorResponse){
            Log.d("Task List Error:", error.toString())
            
          }
      
        }
      )
```

Java sample

```java

    Workmate.getTaskList(context, 
      token,
      new WMTaskListListener() {
      @Override
      public void onTaskListSuccess(List<TaskData> taskList) {
        //Do Something
      }
      
      @Override
      public void onTaskListError(ErrorResponse error){
        //Do Something
      }
    });

    Workmate.getTaskList(this,
        token,
        new WMTaskListListener() {
          @Override
          public void onTaskListSuccess(List<TaskData> taskList) {
            Log.d("Task List:", taskList.toString());
          }
          
          @Override
          public void onTaskListError(ErrorResponse error){
            Log.d("Task List Error:", error.toString());
          }
    });
```



## Workmate get client list

### Function: `getClientList`

To get all clients that are created.  Follow the code below for help.

- **Parameters:**

  - `context`: Context (this)
  - `token <String>` : Pass the access token received from auth.
  - `WMClientListListener`: A callback to handle the success or failure of get client list call.

- **Callbacks:**

  - `onClientListSuccess`: Invoked when the client list is successfully retrieved.
    Returns a List of ClientDetails objects representing the retrieved clients, along with the status information.
  - `onClientListError`: Invoked when the client list retrieval fails.
    Returns an ErrorResponse object containing the status code and error message, which can be used for logging or notifying the user.

### Explanation

Kotlin sample

```kotlin

    // Method Example
    Workmate.getClientList(context,
      token,
      object :WMClientListListener{
        override fun onClientListSuccess(clientList: List<ClientDetails>) {
          //Do Something
        }
    
        override fun onClientListError(errorResponse: ErrorResponse) {
          //Do Something
        }
    
        override fun onClientListError(error: String) {
          //Do something
        }
    
      }
    )

    // Get Client List example
    Workmate.getClientList(this,
        token,
        object :WMClientListListener{
            override fun onClientListSuccess(clientList: List<ClientDetails>) {
              Log.d("Client List:", clientList.toString());
            }

            override fun onClientListError(errorResponse: ErrorResponse) {
              Log.d("Client List Error", errorResponse.toString());
            }

            override fun onClientListError(error: String) {
              //Do Something
            }

        }
     )
```

Java sample

```java

     // Method Example
     Workmate.getClientList(
        context,
        token,
        new WMClientListListener() {
        @Override
        public void onClientListSuccess(List<ClientDetails> clientList) {
          // Do something
        }
      
        @Override
        public void onClientListError(ErrorResponse errorResponse) {
          // Do something
        }
      
        @Override
        public void onClientListError(String error) {
          // Do something
        }
      }
      );
    
     // Get Client List example
     Workmate.getClientList(
      this,
      token,
      new WMClientListListener() {
      @Override
      public void onClientListSuccess(List<ClientDetails> clientList) {
        Log.d("Client List:", clientList.toString());
      }
    
      @Override
      public void onClientListError(ErrorResponse errorResponse) {
        Log.d("Client List Error", errorResponse.toString());
      }
    
      @Override
      public void onClientListError(String error) {
        // Do something
      }
    }
  );

```


## Workmate Task Check-in-out

**Deprecated:** This method is no longer recommended. Please use `taskCheckIn() and taskCheckOut()` instead.

### method name: `taskCheckInAndOut`

This function handles the process of task check-in and check-out information using the Workmate SDK.


### Explanation

- **Parameters:**

  - `context`: Context (this)
  - `clientId : <Int>`: This parameter represents the unique identifier for a client. The clientId is associated with the corresponding Workmate client.
  - `taskId <String>` : This parameter represents the unique identifier for a task. The taskId is used to reference, track, or perform operations on a specific task within the system or database.
  - `taskCheckId : <Int>`: After task check-in, you will receive a taskCheckId. Pass this value as the <id> parameter during task check-out.

  ```groovy
    In above implementation taskCheckId represents the check-in/check-out identifier for a task.
  - While checkin pass null in taskCheckId
  - While checkout pass id value in taskCheckId retrieved from onClientCheckInOutSuccess method.
  ```

  - `formData <JSONObject>`: The form data required when checking in or checking out of a client. If the workflow is enabled, provide the formData that corresponds to the specified ContextId. This object typically contains key-value pairs representing the data fields associated with the client . If the workflow is disabled, pass null.
  - `accessToken  <String>`: The access token obtained from the authentication process.
  - `clientCheckInOutListener`: A callback interface used to handle the success or failure of the task check-in and check-out process.

- **Callbacks:**

  - `onClientCheckInOutSuccess`: Displays a success message.
  - `onClientCheckInOutFailed`: Invoked when the task check-in or check-out process fails. Logs the error and displays an appropriate message to inform the user or aid in debugging.

  Below is the method implementation along with example

#### Implementation

Kotlin implementation

```kotlin

    //Method signature
    Workmate.taskCheckInAndOut(
      context,
      clientId,
      taskId,
      taskCheckId,
      formData,
      accessToken,
      object : WMClientCheckInOutListener {
        override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
          //Do something
        }

        override fun onClientCheckInOutFailed(error: String) {
          //Do something
        }

        override fun onClientCheckInOutFailed(error: ErrorResponse) {
          //Do something
        }


      }
    )

    //Method example
    //Performing task check-in
    Workmate.taskCheckInAndOut(this,
      1234567, // Client id obtained from Workmate
      13, //task id obtained from Workmate
      null, // Pass null for new task check-in
      null,
      accessToken,
      object : WMClientCheckInOutListener {
            override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
              //Do something
              Log.d("task id:", " ${clientCheckInOutResponse.id}"); // Suppose 11221122 received from server
            }
    
            override fun onClientCheckInOutFailed(error: ErrorResponse) {
                // Do Something
            }
      }
    )

    //Performing task check-out
    Workmate.taskCheckInAndOut(
        this,
        1234567, //Client id obtained from Workmate
        13,  //task id obtained from Workmate
        11221122, // Pass id obtained from task check in
        null,
        accessToken, //token received from workmate initialization
        clientCheckInOutListener = object : WMClientCheckInOutListener {
            override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                //Do something
                Log.d("task id:", "${clientCheckInOutResponse.id}")
            }
  
            override fun onClientCheckInOutFailed(error: ErrorResponse) {
                //Do something 
            }
        }
    )

```

Java implementation

```java
    //Method signature
    Workmate.taskCheckInAndOut(
    context,
    clientId,
    taskId,
    taskCheckId,
    formData,
    token,
    new WMClientCheckInOutListener() {
         @Override
         public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
             // Do something
         }
    
         @Override
         public void onClientCheckInOutFailed(ErrorResponse error) {
                // Do something
         }
    }
    );


    //Task check in example
     Workmate.taskCheckInAndOut(
     this,
     1234567, // Client id obtained from Workmate
     13,      // Task id obtained from Workmate
     null,    // Pass null for new task check-in
     null,
     accessToken,
     new WMClientCheckInOutListener() {
          @Override
          public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
              // Do something
              Log.d("task id:", String.valueOf(clientCheckInOutResponse.getId())); // Suppose 11221122 received from server
          }
                
          @Override
          public void onClientCheckInOutFailed(ErrorResponse error) {
              //Do something 
          }
     });


     // Task check out example
    Workmate.taskCheckInAndOut(
        this,
        1234567, // Client id obtained from Workmate
        13,      // Task id obtained from Workmate
        11221122,// Pass id obtained from task check in
        null,
        accessToken, //token received from workmate initialization
        new WMClientCheckInOutListener() {
            @Override
            public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
                // Do something
                Log.d("task id:", String.valueOf(clientCheckInOutResponse.getId()));
            }
            
            @Override
            public void onClientCheckInOutFailed(ErrorResponse error) {
                // Do Something
            }
        }
    );

```



## Workmate Task Check-in

### method name: `taskCheckIn`

This function handles the process of task check-in. It is used to log the start of a task or activity by submitting check-in information such as task ID, location, and timestamp to the server.

### Explanation

- **Parameters:**

  - `context`: Context (this)
  - `clientId : <Int>`: This parameter represents the unique identifier for a client. The clientId is associated with the corresponding Workmate client.
  - `taskId <String>` : This parameter represents the unique identifier for a task. The taskId is used to reference, track, or perform operations on a specific task within the system or database.
  - `formData <JSONObject>`: The form data required when checking in or checking out of a client. If the workflow is enabled, provide the formData that corresponds to the specified ContextId. This object typically contains key-value pairs representing the data fields associated with the client . If the workflow is disabled, pass null.
  - `accessToken  <String>`: The access token obtained from the authentication process.
  - `taskCheckInOutListener`: A callback interface used to handle the success or failure of the task check-in and check-out process.

- **Callbacks:**

  - `onTaskCheckInOutSuccess`: Invoked when the task check-in or check-out operation is successfully completed.
    Returns a ClientCheckInOutResponse object containing the status and details of the operation.
  - `onTaskCheckInOutFailed`: Invoked when the task check-in or check-out operation fails.
    Returns an ErrorResponse object containing the status code and error message, which can be used for logging or displaying an appropriate message to the user.

  Below is the method implementation along with example

#### Implementation

Kotlin implementation

```kotlin

    //Method signature
    Workmate.taskCheckIn(
      context,
      clientId,
      taskId,
      formData,
      accessToken,
      object : WMTaskCheckInOutListener {
        override fun onTaskCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
          //Do something
        }

        override fun onTaskCheckInOutFailed(error: String) {
          //Do something
        }

        override fun onTaskCheckInOutFailed(error: ErrorResponse) {
          //Do something
        }
      }
    )

    //Method example
    //Performing task check-in
    Workmate.taskCheckIn(this,
      1234567, // Client id obtained from Workmate
      13, //task id obtained from Workmate
      null, // Pass null for new task check-in
      null,
      accessToken,
      object : WMTaskCheckInOutListener {
            override fun onTaskCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) { 
              Log.d("task id:", " ${clientCheckInOutResponse.id}"); // Suppose 11221122 received from server
            }
    
            override fun onTaskCheckInOutFailed(error: String) {
              //Do something  
            }
    
            override fun onTaskCheckInOutFailed(error: ErrorResponse) {
              Log.d("task check in error:", String.valueOf(error.getId())); // Suppose 11221122 received from server
            }
      }
    )

```

Java implementation

```java
    //Method signature
    Workmate.taskCheckIn(
    context,
    clientId,
    taskId,
    formData,
    token,
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


    //Task check in example
     Workmate.taskCheckIn(
     this,
     1234567, // Client id obtained from Workmate
     13,      // Task id obtained from Workmate
     null,
     accessToken,
     new WMTaskCheckInOutListener() {
          @Override
          public void onTaskCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
              Log.d("task id:", String.valueOf(clientCheckInOutResponse.getId())); // Suppose 11221122 received from server
          }
                
          @Override
          public void onTaskCheckInOutFailed(ErrorResponse error) {
              // Do Something
          }
     });

```


## Workmate Task Check-Out

### method name: `taskCheckOut`

This function handles the process of task check-out. It is used to log the completion of a task or activity by submitting check-out information such as task ID, location, and timestamp to the server.

### Explanation

- **Parameters:**

  - `context`: Context (this)
  - `clientId : <Int>`: This parameter represents the unique identifier for a client. The clientId is associated with the corresponding Workmate client.
  - `taskId <String>` : This parameter represents the unique identifier for a task. The taskId is used to reference, track, or perform operations on a specific task within the system or database.
  - `taskCheckId : <Int>`: After task check-in, you will receive a taskCheckId. Pass this value as the <id> parameter during task check-out.
  - `formData <JSONObject>`: The form data required when checking in or checking out of a client. If the workflow is enabled, provide the formData that corresponds to the specified ContextId. This object typically contains key-value pairs representing the data fields associated with the client . If the workflow is disabled, pass null.
  - `accessToken  <String>`: The access token obtained from the authentication process.
  - `taskCheckInOutListener`: A callback interface used to handle the success or failure of the task check-in and check-out process.

    ```groovy
      In above implementation taskCheckId represents the check-in/check-out identifier for a task.
    - While checkin pass null in taskCheckId
    - While checkout pass id value in taskCheckId retrieved from onClientCheckInOutSuccess method.
    ```



- **Callbacks:**

  - `onTaskCheckInOutSuccess`: Invoked when the task check-in or check-out operation is successfully completed.
    Returns a ClientCheckInOutResponse object containing the status and details of the operation.
  - `onTaskCheckInOutFailed`: Invoked when the task check-in or check-out operation fails.
    Returns an ErrorResponse object containing the status code and error message, which can be used for logging or displaying an appropriate message to the user.

  Below is the method implementation along with example

#### Implementation

Kotlin implementation

```kotlin

    //Method signature
    Workmate.taskCheckOut(
      context,
      clientId,
      taskId,
      taskCheckId,
      formData,
      accessToken,
      object : WMTaskCheckInOutListener {
        override fun onTaskCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
          //Do something
        }

        override fun onTaskCheckInOutFailed(error: ErrorResponse) {
          //Do something
        }
      }
    )

    //Method example
    //Performing task check-out
    Workmate.taskCheckOut(
        this,
        1234567, //Client id obtained from Workmate
        13,  //task id obtained from Workmate
        11221122, // Pass id obtained from task check in
        null,
        accessToken, //token received from workmate initialization
        taskCheckInOutListener = object : WMTaskCheckInOutListener {
            override fun onTaskCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
                //Do something
                Log.d("task id:", "${clientCheckInOutResponse.id}")
            }
  
            override fun onTaskCheckInOutFailed(error: ErrorResponse) {
                //Do something  
            }
        }
    )

```

Java implementation

```java
    //Method signature
    Workmate.taskCheckOut(
    context,
    clientId,
    taskId,
    taskCheckId,
    formData,
    token,
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


     // Task check out example
    Workmate.taskCheckOut(
        this,
        1234567, // Client id obtained from Workmate
        13,      // Task id obtained from Workmate
        11221122,// Pass id obtained from task check in
        null,
        accessToken, //token received from workmate initialization
        new WMTaskCheckInOutListener() {
            @Override
            public void onTaskCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
                // Do something
                Log.d("task id:", String.valueOf(clientCheckInOutResponse.getId()));
            }

            @Override
            public void onTaskCheckInOutFailed(String error) {
                // Do something
            }
            @Override
            public void onTaskCheckInOutFailed(ErrorResponse error) {
                Log.d("task check in error:", String.valueOf(error));
            }
        }
    );

```




## Workmate Route Optimization

### method name: `getRouteOptimize`

This method calculates the most efficient route by optimizing the order of stops or waypoints based on factors such as distance or travel time conditions. It is typically used to improve routing performance for delivery, travel, or logistics applications.

### Explanation

- **Parameters:**

  - `context`: Context (this)
  - `token <String>` : Pass the access token received from auth.
  - `WMRouteListener`: A callback interface to handle the success or failure of route optimization operation.

- **Callbacks:**

  - `onRouteOptimizeSuccess`: Invoked when the route optimization operation is successfully completed.
    Returns a PlanResponse object containing the optimized route details and relevant plan information.
  - `onRouteOptimizeFailed`: Invoked when the route optimization process fails.
    Returns an ErrorResponse object containing the status code and error message, which can be used for logging or displaying an appropriate message to the user.

  Below is the method implementation along with example

#### Implementation

Kotlin implementation

```kotlin

    //Method signature
    Workmate.getRouteOptimize(
      context, 
      token, //token received from workmate initialization
      wmRouteListener = object :WMRouteListener{
      override fun onRouteOptimizeSuccess(planResponse: PlanResponse) {
         // Do Something
      }
    
      override fun onRouteOptimizeFailure(errorResponse: ErrorResponse) {
         // Do Something 
      }
    })
    
    //Route Optimization Example
    Workmate.getRouteOptimize(
      this,
      token,
      wmRouteListener = object :WMRouteListener{
        override fun onRouteOptimizeSuccess(planResponse: PlanResponse) {
          Log.d("Route Optimized Response:", String.valueOf(planResponse))
        }

        override fun onRouteOptimizeFailure(errorResponse: ErrorResponse) {
          Log.d("Route Optimized Failure:", String.valueOf(errorResponse))
        }
    })

```
Java implementation

```java

    //Method signature
    Workmate.getRouteOptimize(
        context,
        token, //token received from workmate initialization
        new WMRouteListener() {
            @Override
            public void onRouteOptimizeSuccess(PlanResponse planResponse) {
                // Do Something
            }
    
            @Override
            public void onRouteOptimizeFailure(ErrorResponse errorResponse) {
                // Do Something
            }
        }
    );

    //Route Optimization Example
    Workmate.getRouteOptimize(
        this,
        token,
        new WMRouteListener() {
            @Override
            public void onRouteOptimizeSuccess(PlanResponse planResponse) {
                Log.d("Route Optimized Response:", String.valueOf(planResponse));
            }
    
            @Override
            public void onRouteOptimizeFailure(ErrorResponse errorResponse) {
                Log.d("Route Optimized Failure:", String.valueOf(errorResponse));
            }
        }
    );
    
```




# Workmate help section

## Sample code to convert epoch current time to epoch.

Kotlin sample code

```kotlin

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

```java
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

```kotlin
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

```java
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
## Workmate Error code

### Enum Constants & Explanation

| Code | Message                                                               |
|:----:|:----------------------------------------------------------------------|
| 1000 | Location fetched successfully.                                        |
| 1001 | Location permission failed or not granted.                            |
| 1002 | Location permission permanently denied. Please enable it in settings. |
| 1003 | Location error. Please check your location service.                   |
| 1004 | Failed to retrieve location: Timeout.                                 |
| 1005 | Location accuracy is insufficient.                                    |
| 1006 | Location unavailable.                                                 |
| 1007 | Device settings do not meet the location requirements.                |
| 1008 | Mock location detected.                                               |
| 1009 | Location provider returned an error.                                  |
| 1010 | An unknown location error occurred.                                   |
| 1011 | GPS provider is not enabled.                                          |
| 1020 | No internet connection. Please check your network.                    |
| 1021 | Network request timed out.                                            |
| 1022 | Cannot reach the server. Please try again later.                      |
| 1023 | An unexpected network error occurred.                                 |
| 1030 | You are not authorised. Please try again later.                       |
| 1040 | Bad request. Please try again later.                                  |
| 1041 | Access id is null.                                                    |
| 1042 | Access id is required.                                                |
| 1043 | Plan id is required.                                                  |
| 1050 | Task cannot be hold.                                                  |
| 1051 | Task cannot be resumed.                                               |
| 1052 | Task is not checked in.                                               |
| 1053 | Task already resumed.                                                 |
| 1054 | Task already hold.                                                    |
| 1055 | Invalid task id.                                                      |
| 1056 | Task already checked in.                                              |
| 1060 | API error. Please try again later.                                    |
| 1061 | Workmate is not initialized.                                          |
| 1062 | NullPointerException.                                                 |
| 1063 | ClassNotFoundException.                                               |
| 1064 | NoClassDefFoundError.                                                 |
| 1065 | Please provide valid client id.                                       |
| 1066 | Server error. Please try again later.                                 |
| 1070 | Permission grant error.                                               |
| 1071 | No value received from server.                                        |
| 1072 | An error occurred. Please try again later.                            |
| 1073 | Timestamp error.                                                      |
| 1074 | Token is required and cannot be null.                                 |
| 1075 | User is required and cannot be null.                                  |
| 1076 | API error.                                                            |
| 1077 | Server error occurred.                                                |
| 1078 | Location fetching error.                                              |
| 1080 | Exception error.                                                      |
| 1090 | Client is not checked in.                                             |
| 1091 | No client is checked in.                                              |
| 1092 | No data found on server.                                              |
| 1093 | Client already checked in at other client.                            |
| 1094 | Client is already checked in.                                         |
| 1095 | Client is on hold cannot check-in.                                    |
| 1096 | Client is resumed cannot check-in.                                    |
| 1097 | Client is on hold and cannot check out.                               |
| 1081 | Workday already started.                                              |
| 1082 | Workday already ended.                                                |
| 1083 | User location data is invalid or missing.                             |
| 1084 | Only one geofence coordinate is allowed.                              |
| 1085 | Device is outside the defined geofence boundaries.                    |


## Workmate Error Response

### Parameters & Explanation

| Field   | Type    | Description                                                                 |
|---------|---------|-----------------------------------------------------------------------------|
| `message` | `String?` | A descriptive error message. May be `null`.                                  |
| `status`  | `Int?`    | A numeric error code. May be `null`. Maps to a value from the `ErrorCode` enum. |
