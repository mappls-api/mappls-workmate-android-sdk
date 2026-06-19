[<img src="https://about.mappls.com/images/mappls-b-logo.svg" height="60"/>](https://www.mapmyindia.com/api)

# Users & Authentication

[← Back to Documentation](README.md)

Workmate SDK is built for organisations that operate a distributed field workforce — sales teams, service technicians, delivery agents, healthcare field workers, or any role where people work outside a fixed office. This document covers the foundational SDK methods that every integration depends on: authenticating agents, managing tokens, tracking location and movement, and validating geo-boundaries.

Every field agent has a profile in the system linked to their device. The authentication module manages who can log in, what they can access, and ensures every action is tied to a verified identity. All subsequent SDK features — attendance, tasks, client visits — require a valid access token obtained through initialization.

The movement and location methods are built directly on the Mappls location infrastructure, capturing real-time location data that managers can use to understand how agents are moving across their territory, how much ground they're covering, and where they spent their time.

---

## [Table of Contents]()

- [Initialization](#initialization)
- [Refresh Token](#refresh-token)
- [Get Movement Trail](#get-movement-trail)
- [Get User Activity](#get-user-activity)
- [Calculate Distance](#calculate-distance)
- [Get Device Location](#get-device-location)
- [Validate Geo-Fence](#validate-geo-fence)
- [Error Codes](#error-codes)
- [Error Response](#error-response)
- [Help: Convert String to Epoch](#convert-string-to-epoch)
- [Help: Get Epoch from Date/Time UI](#get-epoch-from-datetime-ui)

---

## [Initialization]()

### Method: `initialize`

Initializes all services required by the Workmate SDK and returns an access token used for all subsequent SDK calls. Authentication is handled implicitly using the provided user email.

This is always the first call you make. Workmate SDK handles identity and access for field agents and administrators — this method is the entry point that authenticates the agent, links them to their device, and unlocks all other SDK functionality. Without a successful initialization, no other method will work.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `clientId` | `String` | Client ID provided by the Mappls team. |
| `clientSecret` | `String` | Client secret provided by the Mappls team. |
| `userEmail` | `String` | The user's email address. Used for implicit login. |

### Callbacks

| Callback | Description |
|---|---|
| `onAuthSuccess(AuthResponse)` | Triggered on successful authentication. Provides the `accessToken`. |
| `onAuthFailure(ErrorResponse)` | Triggered on failure. Provides the error message and status code. |

> **Save the `accessToken`** returned in `onAuthSuccess` — it is required for all other Workmate SDK methods.

### Kotlin

```kotlin
// Method signature
Workmate.initialize(
    context, clientSecret, clientId, email,
    listener = object : WMAuthListener {
        override fun onAuthSuccess(response: AuthResponse) {
            // Do something
        }
        override fun onAuthFailure(errorResponse: ErrorResponse) {
            // Do something
        }
    }
)

// Example
Workmate.initialize(
    this,
    "your_client_secret",   // Replace with real value from Mappls team
    "your_client_id",       // Replace with real value from Mappls team
    "useremail@example.com",
    listener = object : WMAuthListener {
        override fun onAuthSuccess(response: AuthResponse) {
            Log.d("Auth", "${response.accessToken}") // Save this token
        }
        override fun onAuthFailure(errorResponse: ErrorResponse) {
            Log.d("Auth Error", errorResponse.toString())
        }
    }
)
```

### Java

```java
// Method signature
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

// Example
Workmate.initialize(
    this,
    "your_client_secret",   // Replace with real value from Mappls team
    "your_client_id",       // Replace with real value from Mappls team
    "useremail@example.com",
    new WMAuthListener() {
        @Override
        public void onAuthSuccess(AuthResponse response) {
            Log.d("Auth", response.getAccessToken()); // Save this token
        }
        @Override
        public void onAuthFailure(ErrorResponse errorResponse) {
            Log.d("Auth Error", errorResponse.toString());
        }
    }
);
```

---

## [Refresh Token]()

### Method: `refreshToken`

Refreshes the Workmate access token. Returns the same callbacks as `initialize`.

Access tokens have a limited lifespan. Use this method to silently renew the token in the background without requiring the agent to log in again. It is good practice to call this proactively before the token expires to avoid disruption to running SDK operations.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |

### Callbacks

| Callback | Description |
|---|---|
| `onAuthSuccess(AuthResponse)` | Triggered on successful token refresh. Provides the new `accessToken`. |
| `onAuthFailure(ErrorResponse)` | Triggered on failure. Provides the error message and status code. |

### Kotlin

```kotlin
// Method signature
Workmate.refreshToken(context, object : WMAuthListener {
    override fun onAuthSuccess(response: AuthResponse) {
        // Do something
    }
    override fun onAuthFailure(errorResponse: ErrorResponse) {
        // Do something
    }
})

// Example
Workmate.refreshToken(this, object : WMAuthListener {
    override fun onAuthSuccess(response: AuthResponse) {
        Log.d("Refresh", "${response.accessToken}")
    }
    override fun onAuthFailure(errorResponse: ErrorResponse) {
        Log.d("Refresh Error", errorResponse.toString())
    }
})
```

### Java

```java
// Method signature
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

// Example
Workmate.refreshToken(this, new WMAuthListener() {
    @Override
    public void onAuthSuccess(AuthResponse response) {
        Log.d("Refresh", response.getAccessToken());
    }
    @Override
    public void onAuthFailure(ErrorResponse errorResponse) {
        Log.d("Refresh Error", String.valueOf(errorResponse));
    }
});
```

---

## [Get Movement Trail]()

### Method: `getMovementTrail`

Retrieves the movement trail (location history) of a user within a specified time range. Admins can query movement trails for any user under their supervision.

Built directly on the Mappls location infrastructure, this method gives managers a continuous picture of how an agent moved through their territory during a given period. It can be used to review daily coverage, verify client visits, resolve disputes, or identify inefficiencies in agent routing. The trail includes precise coordinates, speed, and timestamps for every recorded location ping.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `userId` | `String` | Email of the user whose trail you want to retrieve. |
| `startTimeInEpoch` | `Long` | Start time in UNIX epoch (seconds). |
| `stopTimeInEpoch` | `Long` | End time in UNIX epoch (seconds). |
| `token` | `String` | Access token from `initialize`. |

### Callbacks

| Callback | Description |
|---|---|
| `onMovementTrailsSuccess(MovementTrailsResponse)` | Returns trail data: `address`, `latitude`, `longitude`, `speed`, `timestamp`. |
| `onMovementTrailsFailed(ErrorResponse)` | Returns error details on failure. |

### Kotlin

```kotlin
// Method signature
Workmate.getMovementTrail(
    context, userId, startTimeInEpoch, stopTimeInEpoch, token,
    userActivityListener = object : WMMovementTrailsListener {
        override fun onMovementTrailsSuccess(movementTrailsResponse: MovementTrailsResponse) {
            // Do something
        }
        override fun onMovementTrailsFailed(error: ErrorResponse) {
            // Do something
        }
    }
)

// Example
Workmate.getMovementTrail(
    this,
    "useremail@example.com",
    1732127400,
    1732213799,
    token,
    object : WMMovementTrailsListener {
        override fun onMovementTrailsSuccess(movementTrailsResponse: MovementTrailsResponse) {
            Log.d("Trail", movementTrailsResponse.data.toString())
            // movementTrailsResponse.data.address
            // movementTrailsResponse.data.latitude
            // movementTrailsResponse.data.longitude
            // movementTrailsResponse.data.speed
            // movementTrailsResponse.data.timestamp
        }
        override fun onMovementTrailsFailed(error: ErrorResponse) {
            Log.d("Trail Error", error.toString())
        }
    }
)
```

### Java

```java
// Method signature
Workmate.getMovementTrail(
    context, userEmail, startTimeInEpoch, endTimeInEpoch, token,
    new WMMovementTrailsListener() {
        @Override
        public void onMovementTrailsSuccess(MovementTrailsResponse movementTrailsResponse) {
            // Do something
        }
        @Override
        public void onMovementTrailsFailed(ErrorResponse error) {
            // Do something
        }
    }
);

// Example
Workmate.getMovementTrail(
    this,
    "useremail@example.com",
    1732127400,
    1732213799,
    token,
    new WMMovementTrailsListener() {
        @Override
        public void onMovementTrailsSuccess(MovementTrailsResponse movementTrailsResponse) {
            Log.d("Trail", movementTrailsResponse.getData().toString());
            // movementTrailsResponse.getData().getAddress();
            // movementTrailsResponse.getData().getLatitude();
            // movementTrailsResponse.getData().getLongitude();
            // movementTrailsResponse.getData().getSpeed();
            // movementTrailsResponse.getData().getTimestamp();
        }
        @Override
        public void onMovementTrailsFailed(ErrorResponse error) {
            Log.d("Trail Error", String.valueOf(error));
        }
    }
);
```

---

## [Get User Activity]()

### Method: `getUserActivity`

Retrieves all activities performed by a user within a given time range. Activities may be linked to specific tasks or independent. Admins can query activity for any user under their supervision.

Field agents perform many types of activities throughout the day — client meetings, product demos, service calls, site inspections. This method retrieves a feed of those logged events, each with a type, location stamp, and timestamp. Organisations can use this data to monitor agent productivity, filter by activity type, and maintain a complete audit trail of field operations.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `userEmail` | `String` | Email of the user whose activity you want to retrieve. Same email used during `initialize`. |
| `startTimeInEpoch` | `Long` | Start time in UNIX epoch (seconds). |
| `endTimeInEpoch` | `Long` | End time in UNIX epoch (seconds). |
| `accessToken` | `String` | Access token from `initialize`. |

### Callbacks

| Callback | Description |
|---|---|
| `onUserActivitySuccess(UserActivityResponse)` | Returns a model with full user activity details. |
| `onUserActivityFailed(ErrorResponse)` | Returns error details on failure. |

### Kotlin

```kotlin
// Method signature
Workmate.getUserActivity(
    context, userEmail, startTimeInEpoch, endTimeInEpoch, accessToken,
    userActivityListener = object : WMUserActivityListener {
        override fun onUserActivitySuccess(userActivityResponse: UserActivityResponse) {
            // Do something
        }
        override fun onUserActivityFailed(error: ErrorResponse) {
            // Do something
        }
    }
)

// Example
val userEmail = "user@example.com"
val startTimeInEpoch = System.currentTimeMillis() / 1000
val endTimeInEpoch = startTimeInEpoch + 3600 // 1 hour later

Workmate.getUserActivity(
    this, userEmail, startTimeInEpoch, endTimeInEpoch, accessToken,
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

### Java

```java
// Method signature
Workmate.getUserActivity(
    context, userEmail, startTimeInEpoch, endTimeInEpoch, accessToken,
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

// Example
String userEmail = "user@example.com";
long startTimeInEpoch = System.currentTimeMillis() / 1000;
long endTimeInEpoch = startTimeInEpoch + 3600; // 1 hour later

Workmate.getUserActivity(
    this, userEmail, startTimeInEpoch, endTimeInEpoch, accessToken,
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

---

## [Calculate Distance]()

### Method: `calculateDistance`

Calculates the total distance travelled by a user within a time range. Supports drive distance (GPS-based) and odometer distance. Admins can retrieve distance data for any user under their supervision.

This method is useful for mileage reporting, reimbursement calculations, and monitoring field coverage. Drive distance reflects the actual GPS-tracked path taken, while odometer distance provides a cumulative measure. Admins can pull this data for any agent under their supervision, making it practical for fleet-level reporting as well.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `userId` | `String` | Email of the user. Same as used during `initialize`. |
| `type` | `String` | `1` for drive distance, `2` for odometer distance. |
| `startTimeInEpoch` | `Long` | Start time in UNIX epoch (seconds). |
| `endTimeInEpoch` | `Long` | End time in UNIX epoch (seconds). |
| `token` | `String` | Access token from `initialize`. |

### Callbacks

| Callback | Description |
|---|---|
| `onCalculateDistanceSuccess(CalculateDistanceResponse)` | Returns the calculated distance. |
| `onCalculateDistanceFailed(ErrorResponse)` | Returns error details on failure. |

### Kotlin

```kotlin
// Method signature
Workmate.calculateDistance(
    context, userId, type, startTimeInEpoch, endTimeInEpoch, accessToken,
    listener = object : WMCalculateDistanceListener {
        override fun onCalculateDistanceSuccess(calculateDistanceResponse: CalculateDistanceResponse) {
            // Do something
        }
        override fun onCalculateDistanceFailed(error: ErrorResponse) {
            // Do something
        }
    }
)

// Example
Workmate.calculateDistance(
    this,
    "useremail@example.com",
    1,           // type: 1 = drive distance
    1732127400,  // start time in epoch
    1732213799,  // end time in epoch
    token,
    object : WMCalculateDistanceListener {
        override fun onCalculateDistanceSuccess(calculateDistanceResponse: CalculateDistanceResponse?) {
            Log.d("Distance", calculateDistanceResponse.toString())
        }
        override fun onCalculateDistanceFailed(error: ErrorResponse) {
            Log.d("Distance Error", error.toString())
        }
    }
)
```

### Java

```java
// Method signature
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

// Example
Workmate.calculateDistance(
    this,
    "useremail@example.com",
    1,             // type: 1 = drive distance
    1732127400L,   // start time in epoch
    1732213799L,   // end time in epoch
    token,
    new WMCalculateDistanceListener() {
        @Override
        public void onCalculateDistanceSuccess(CalculateDistanceResponse calculateDistanceResponse) {
            Log.d("Distance", String.valueOf(calculateDistanceResponse));
        }
        @Override
        public void onCalculateDistanceFailed(ErrorResponse error) {
            Log.d("Distance Error", String.valueOf(error));
        }
    }
);
```

---

## [Get Device Location]()

### Method: `getDeviceLocationData`

Retrieves the current device location details including coordinates, altitude, speed, bearing, and location provider.

Use this method when you need an instant snapshot of where the agent's device is right now — without querying historical data. It is particularly useful for real-time features like displaying an agent's current position on a map, triggering location-based logic, or verifying that location services are active and accurate before performing a check-in or geo-fence validation.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |

### Callbacks

| Callback | Description |
|---|---|
| `onDeviceInfoSuccess(DeviceLocationInfo)` | Returns `latitude`, `longitude`, `altitude`, `speed`, `bearing`, `locationProvider`. |
| `onDeviceInfoError(ErrorResponse)` | Returns error details on failure. |

### Kotlin

```kotlin
// Method signature
Workmate.getDeviceLocationData(context, object : WMDeviceLocationListener {
    override fun onDeviceInfoSuccess(deviceLocationInfo: DeviceLocationInfo) {
        // Do something
    }
    override fun onDeviceInfoError(error: ErrorResponse) {
        // Do something
    }
})

// Example
Workmate.getDeviceLocationData(this, object : WMDeviceLocationListener {
    override fun onDeviceInfoSuccess(deviceLocationInfo: DeviceLocationInfo) {
        Log.d("Location", "${deviceLocationInfo.latitude}")
        Log.d("Location", "${deviceLocationInfo.longitude}")
        Log.d("Location", "${deviceLocationInfo.altitude}")
        Log.d("Location", "${deviceLocationInfo.speed}")
        Log.d("Location", "${deviceLocationInfo.bearing}")
        Log.d("Location", "${deviceLocationInfo.locationProvider}")
    }
    override fun onDeviceInfoError(error: ErrorResponse) {
        Log.d("Location Error", error.toString())
    }
})
```

### Java

```java
// Method signature
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

// Example
Workmate.getDeviceLocationData(this, new WMDeviceLocationListener() {
    @Override
    public void onDeviceInfoSuccess(DeviceLocationInfo deviceLocationInfo) {
        Log.d("Location", String.valueOf(deviceLocationInfo.getLatitude()));
        Log.d("Location", String.valueOf(deviceLocationInfo.getLongitude()));
        Log.d("Location", String.valueOf(deviceLocationInfo.getAltitude()));
        Log.d("Location", String.valueOf(deviceLocationInfo.getSpeed()));
        Log.d("Location", String.valueOf(deviceLocationInfo.getBearing()));
        Log.d("Location", String.valueOf(deviceLocationInfo.getLocationProvider()));
    }
    @Override
    public void onDeviceInfoError(ErrorResponse error) {
        Log.d("Location Error", error.toString());
    }
});
```

---

## [Validate Geo-Fence]()

### Method: `validateGeoFence`

Checks whether the user's current location falls within a predefined geofenced area, based on a center coordinate and radius.

Geo-fence validation is commonly used to enforce location-based rules — for example, ensuring an agent is physically present at a client site before allowing a check-in, or confirming a delivery agent is within the designated drop-off zone. The method compares the agent's live coordinates against a defined boundary and returns a clear pass or fail result, which your application can use to gate further actions.

> **Note:** Currently only one geofence coordinate is accepted per call (`geoFenceCoordinates` list must contain exactly 1 item).

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `userLatitude` | `Double` | The user's current latitude. |
| `userLongitude` | `Double` | The user's current longitude. |
| `geoFenceCoordinates` | `List<WMGeoFenceCoordinates>` | List of geofence center coordinates. Must contain exactly 1 item. |
| `radius` | `Int` | Geofence radius in meters. |
| `listener` | `WMGeoFenceValidationListener` | Callback for success or failure. |

### Callbacks

| Callback | Description |
|---|---|
| `onGeoFenceValidationSuccess()` | User is inside the geofenced area. |
| `onGeoFenceValidationFailed(ErrorResponse)` | User is outside the geofence, or an error occurred. |

### Kotlin

```kotlin
// Method signature
Workmate.validateGeoFence(
    context,
    userLatitude,
    userLongitude,
    geoFenceCoordinates = listOf(WMGeoFenceCoordinates()),
    radius,
    object : WMGeoFenceValidationListener {
        override fun onGeoFenceValidationFailed(error: ErrorResponse) {
            // Do something
        }
        override fun onGeoFenceValidationSuccess() {
            // Do something
        }
    }
)

// Example
Workmate.validateGeoFence(
    context = this,
    userLatitude = 37.7749,
    userLongitude = -122.4194,
    geoFenceCoordinates = listOf(
        WMGeoFenceCoordinates(37.7740, -122.4190)
    ),
    radius = 500,
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

### Java

```java
// Method signature
Workmate.validateGeoFence(
    this,
    userLatitude,
    userLongitude,
    Arrays.asList(new WMGeoFenceCoordinates(lat, lng)),
    radius,
    new WMGeoFenceValidationListener() {
        @Override
        public void onGeoFenceValidationFailed(ErrorResponse error) {
            // Do something
        }
        @Override
        public void onGeoFenceValidationSuccess() {
            // Do something
        }
    }
);

// Example
Workmate.validateGeoFence(
    this,
    37.7749,
    -122.4194,
    Arrays.asList(new WMGeoFenceCoordinates(37.7740, -122.4190)),
    500,
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

---

## [Error Codes]()

| Code | Message |
|:----:|:--------|
| 1000 | Location fetched successfully. |
| 1001 | Location permission failed or not granted. |
| 1002 | Location permission permanently denied. Please enable it in settings. |
| 1003 | Location error. Please check your location service. |
| 1004 | Failed to retrieve location: Timeout. |
| 1005 | Location accuracy is insufficient. |
| 1006 | Location unavailable. |
| 1007 | Device settings do not meet the location requirements. |
| 1008 | Mock location detected. |
| 1009 | Location provider returned an error. |
| 1010 | An unknown location error occurred. |
| 1011 | GPS provider is not enabled. |
| 1020 | No internet connection. Please check your network. |
| 1021 | Network request timed out. |
| 1022 | Cannot reach the server. Please try again later. |
| 1023 | An unexpected network error occurred. |
| 1030 | You are not authorised. Please try again later. |
| 1040 | Bad request. Please try again later. |
| 1041 | Access id is null. |
| 1042 | Access id is required. |
| 1043 | Plan id is required. |
| 1050 | Task cannot be held. |
| 1051 | Task cannot be resumed. |
| 1052 | Task is not checked in. |
| 1053 | Task already resumed. |
| 1054 | Task already on hold. |
| 1055 | Invalid task id. |
| 1056 | Task already checked in. |
| 1060 | API error. Please try again later. |
| 1061 | Workmate is not initialized. |
| 1062 | NullPointerException. |
| 1063 | ClassNotFoundException. |
| 1064 | NoClassDefFoundError. |
| 1065 | Please provide a valid client id. |
| 1066 | Server error. Please try again later. |
| 1070 | Permission grant error. |
| 1071 | No value received from server. |
| 1072 | An error occurred. Please try again later. |
| 1073 | Timestamp error. |
| 1074 | Token is required and cannot be null. |
| 1075 | User is required and cannot be null. |
| 1076 | API error. |
| 1077 | Server error occurred. |
| 1078 | Location fetching error. |
| 1080 | Exception error. |
| 1081 | Workday already started. |
| 1082 | Workday already ended. |
| 1083 | User location data is invalid or missing. |
| 1084 | Only one geofence coordinate is allowed. |
| 1085 | Device is outside the defined geofence boundaries. |
| 1090 | Client is not checked in. |
| 1091 | No client is checked in. |
| 1092 | No data found on server. |
| 1093 | Client already checked in at another client. |
| 1094 | Client is already checked in. |
| 1095 | Client is on hold — cannot check in. |
| 1096 | Client is resumed — cannot check in. |
| 1097 | Client is on hold — cannot check out. |

---

## [Error Response]()

All error callbacks return an `ErrorResponse` object with the following fields:

| Field | Type | Description |
|---|---|---|
| `message` | `String?` | A descriptive error message. May be `null`. |
| `status` | `Int?` | Numeric error code. Maps to a value from the error code table above. May be `null`. |

---

## [Convert String to Epoch]()

Use these helpers to convert a date/time string to a UNIX epoch value (in seconds) for use with `getMovementTrail`, `getUserActivity`, and `calculateDistance`.

### Kotlin

```kotlin
fun convertToEpochUsingLocalDateTime(dateTime: String, pattern: String): Long {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val localDateTime = LocalDateTime.parse(dateTime, formatter)
    return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond()
}

fun convertToEpochUsingSimpleDateFormat(dateTime: String, pattern: String): Long {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.parse(dateTime)?.time?.div(1000) ?: 0
}
```

### Java

```java
public static long convertToEpochUsingLocalDateTime(String dateTime, String pattern) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
    return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
}

public static long convertToEpochUsingSimpleDateFormat(String dateTime, String pattern) throws Exception {
    SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
    formatter.setTimeZone(TimeZone.getDefault());
    Date date = formatter.parse(dateTime);
    return date != null ? date.getTime() / 1000 : 0;
}
```

---

## [Get Epoch from Date/Time UI]()

If you are collecting dates from a UI picker (date dialog, time dialog, etc.), use the following helpers to convert the selected values to a UNIX epoch.

### Kotlin

```kotlin
private fun showDateTimePicker(onDateTimeSelected: (String, String, Long) -> Unit) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        this,
        { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            val timePickerDialog = TimePickerDialog(
                this, { _, hourOfDay, minute ->
                    val secondsPickerDialog = AlertDialog.Builder(this)
                    val numberPicker = NumberPicker(this).apply {
                        minValue = 0
                        maxValue = 59
                        value = calendar.get(Calendar.SECOND)
                    }
                    secondsPickerDialog.setTitle("Select Seconds").setView(numberPicker)
                        .setPositiveButton("OK") { _, _ ->
                            val selectedSecond = numberPicker.value
                            val selectedTime = String.format("%02d:%02d:%02d", hourOfDay, minute, selectedSecond)
                            val epochTime = convertToEpoch(selectedDate, selectedTime)
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
        (parsedDate?.time ?: 0L) / 1000
    } catch (e: Exception) {
        0L
    }
}
```

### Java

```java
private void showDateTimePicker(OnDateTimeSelectedListener listener) {
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog datePickerDialog = new DatePickerDialog(
        this,
        (view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (timeView, hourOfDay, minute) -> {
                    AlertDialog.Builder secondsPickerDialog = new AlertDialog.Builder(this);
                    NumberPicker numberPicker = new NumberPicker(this);
                    numberPicker.setMinValue(0);
                    numberPicker.setMaxValue(59);
                    numberPicker.setValue(calendar.get(Calendar.SECOND));
                    secondsPickerDialog.setTitle("Select Seconds")
                        .setView(numberPicker)
                        .setPositiveButton("OK", (dialog, which) -> {
                            int selectedSecond = numberPicker.getValue();
                            String selectedTime = String.format("%02d:%02d:%02d", hourOfDay, minute, selectedSecond);
                            long epochTime = convertToEpoch(selectedDate, selectedTime);
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
        return (parsedDate != null ? parsedDate.getTime() : 0L) / 1000;
    } catch (Exception e) {
        return 0L;
    }
}

public interface OnDateTimeSelectedListener {
    void onDateTimeSelected(String date, String time, long epoch);
}
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
