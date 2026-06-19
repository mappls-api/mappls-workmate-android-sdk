[<img src="https://about.mappls.com/images/mappls-b-logo.svg" height="60"/>](https://www.mapmyindia.com/api)

# Clients

[← Back to Documentation](README.md)

The clients module is a record of all the customers and accounts that field agents work with. It stores client details, tracks which agents are associated with which clients, and maintains a history of visits and interactions. This gives managers visibility into account coverage and helps agents stay on top of their client relationships.

This document covers the SDK methods for logging client visits — checking in when an agent arrives at a client location and checking out when they leave — along with retrieving the full client list. Each check-in and check-out is geo-tagged and timestamped, creating a precise record of every client interaction.

---

## [Table of Contents]()

- [Client Check-In](#client-check-in)
- [Client Check-Out](#client-check-out)
- [Client Check-In/Out (Deprecated)](#client-check-inout-deprecated)
- [Get Client List](#get-client-list)

---

## [Client Check-In]()

### Method: `clientCheckIn`

Logs the start of a client visit by submitting location and timestamp data to the server. Returns a `clientCheckId` in the success callback — save this value as it is required for `clientCheckOut`.

When an agent arrives at a client location, this method records the visit start. The check-in is geo-tagged to confirm the agent's physical presence at the site. If your organisation has workflows enabled (for example, a pre-visit form or activity log), you can attach form data to the check-in. The `clientCheckId` returned on success is the key that links this check-in to its eventual check-out — keep it in memory for the duration of the visit.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `clientId` | `Int` | Unique identifier for the client from Workmate. |
| `formData` | `JSONObject?` | Form data for the check-in workflow. Pass `null` if workflow is disabled. |
| `accessId` | `String` | Access token from `initialize`. |

### Callbacks

| Callback | Description |
|---|---|
| `onClientCheckInOutSuccess(ClientCheckInOutResponse)` | Check-in succeeded. Returns `id` (the `clientCheckId`) — save this for check-out. |
| `onClientCheckInOutFailed(ErrorResponse)` | Check-in failed. Returns error code and message. |

### Kotlin

```kotlin
// Method signature
Workmate.clientCheckIn(
    context,
    clientId,
    formData,
    accessId,
    clientCheckInOutListener = object : WMClientCheckInOutListener {
        override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
            // Do something
        }
        override fun onClientCheckInOutFailed(errorResponse: ErrorResponse) {
            // Do something
        }
    }
)

// Example
Workmate.clientCheckIn(
    this,
    1234567,     // Client ID from Workmate
    null,        // formData — null if workflow disabled
    accessId,
    clientCheckInOutListener = object : WMClientCheckInOutListener {
        override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
            Log.d("Client Check-In", "${clientCheckInOutResponse.id}") // Save this clientCheckId
        }
        override fun onClientCheckInOutFailed(errorResponse: ErrorResponse) {
            Log.d("Client Error", errorResponse.toString())
        }
    }
)
```

### Java

```java
// Method signature
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

// Example
Workmate.clientCheckIn(
    this,
    1234567,     // Client ID from Workmate
    null,        // formData — null if workflow disabled
    accessId,
    new WMClientCheckInOutListener() {
        @Override
        public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
            Log.d("Client Check-In", String.valueOf(clientCheckInOutResponse.getId())); // Save this clientCheckId
        }
        @Override
        public void onClientCheckInOutFailed(ErrorResponse error) {
            Log.d("Client Error", String.valueOf(error));
        }
    }
);
```

---

## [Client Check-Out]()

### Method: `clientCheckOut`

Logs the end of a client visit. Requires the `clientCheckId` returned by `clientCheckIn`.

When the agent concludes their visit, this method closes out the check-in record by submitting the departure location and timestamp. Together with `clientCheckIn`, it creates a complete visit record — duration, start location, end location — that feeds into the client interaction history. Pass the `clientCheckId` from the corresponding check-in to correctly link the two events.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `clientId` | `Int` | Unique identifier for the client from Workmate. |
| `clientCheckId` | `Int` | The check-in ID returned by `onClientCheckInOutSuccess` during check-in. |
| `formData` | `JSONObject?` | Form data for the check-out workflow. Pass `null` if workflow is disabled. |
| `accessId` | `String` | Access token from `initialize`. |

### Callbacks

| Callback | Description |
|---|---|
| `onClientCheckInOutSuccess(ClientCheckInOutResponse)` | Check-out succeeded. Returns operation status and details. |
| `onClientCheckInOutFailed(ErrorResponse)` | Check-out failed. Returns error code and message. |

### Kotlin

```kotlin
// Method signature
Workmate.clientCheckOut(
    context, clientId, clientCheckId, formData, accessId,
    clientCheckInOutListener = object : WMClientCheckInOutListener {
        override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
            // Do something
        }
        override fun onClientCheckInOutFailed(errorResponse: ErrorResponse) {
            // Do something
        }
    }
)

// Example
Workmate.clientCheckOut(
    this,
    1234567,     // Client ID from Workmate
    11221122,    // clientCheckId from check-in success callback
    null,
    accessId,
    clientCheckInOutListener = object : WMClientCheckInOutListener {
        override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
            Log.d("Client Check-Out", "${clientCheckInOutResponse.id}")
        }
        override fun onClientCheckInOutFailed(errorResponse: ErrorResponse) {
            Log.d("Client Error", errorResponse.toString())
        }
    }
)
```

### Java

```java
// Method signature
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

// Example
Workmate.clientCheckOut(
    this,
    1234567,     // Client ID from Workmate
    11221122,    // clientCheckId from check-in success callback
    null,
    accessId,
    new WMClientCheckInOutListener() {
        @Override
        public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
            Log.d("Client Check-Out", String.valueOf(clientCheckInOutResponse.getId()));
        }
        @Override
        public void onClientCheckInOutFailed(ErrorResponse error) {
            Log.d("Client Error", String.valueOf(error));
        }
    }
);
```

---

## [Client Check-In/Out (Deprecated)]()

> **Deprecated:** This method is no longer recommended. Use [`clientCheckIn`](#client-check-in) and [`clientCheckOut`](#client-check-out) instead.

### Method: `clientCheckInOut`

A combined method that previously handled both client check-in and check-out in a single call. Pass `null` for `clientCheckId` to check in, or pass the ID received from a previous check-in to check out.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `clientId` | `Int` | Unique identifier for the client from Workmate. |
| `clientCheckId` | `Int?` | Pass `null` for check-in. Pass the ID from a previous check-in for check-out. |
| `formData` | `JSONObject?` | Form data for the workflow. Pass `null` if workflow is disabled. |
| `accessToken` | `String` | Access token from `initialize`. |

### Callbacks

| Callback | Description |
|---|---|
| `onClientCheckInOutSuccess(ClientCheckInOutResponse)` | Operation succeeded. Returns the `clientCheckId`. |
| `onClientCheckInOutFailed(ErrorResponse)` | Operation failed. Returns error code and message. |

### Kotlin

```kotlin
// Check-in example
Workmate.clientCheckInOut(
    this,
    1234567,     // Client ID
    null,        // null for check-in
    null,
    accessToken,
    clientCheckInOutListener = object : WMClientCheckInOutListener {
        override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
            Log.d("Client", "${clientCheckInOutResponse.id}") // Save this for check-out
        }
        override fun onClientCheckInOutFailed(errorResponse: ErrorResponse) {
            Log.d("Client Error", errorResponse.toString())
        }
    }
)

// Check-out example
Workmate.clientCheckInOut(
    this,
    1234567,
    11221122,    // clientCheckId from check-in
    null,
    accessToken,
    clientCheckInOutListener = object : WMClientCheckInOutListener {
        override fun onClientCheckInOutSuccess(clientCheckInOutResponse: ClientCheckInOutResponse) {
            Log.d("Client", "${clientCheckInOutResponse.id}")
        }
        override fun onClientCheckInOutFailed(errorResponse: ErrorResponse) {
            Log.d("Client Error", errorResponse.toString())
        }
    }
)
```

### Java

```java
// Check-in example
Workmate.clientCheckInOut(
    this,
    1234567,
    null,
    null,
    accessToken,
    new WMClientCheckInOutListener() {
        @Override
        public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
            Log.d("Client", String.valueOf(clientCheckInOutResponse.getId()));
        }
        @Override
        public void onClientCheckInOutFailed(ErrorResponse error) {
            Log.d("Client Error", String.valueOf(error));
        }
    }
);

// Check-out example
Workmate.clientCheckInOut(
    this,
    1234567,
    11221122,
    null,
    accessToken,
    new WMClientCheckInOutListener() {
        @Override
        public void onClientCheckInOutSuccess(ClientCheckInOutResponse clientCheckInOutResponse) {
            Log.d("Client", String.valueOf(clientCheckInOutResponse.getId()));
        }
        @Override
        public void onClientCheckInOutFailed(ErrorResponse error) {
            Log.d("Client Error", String.valueOf(error));
        }
    }
);
```

---

## [Get Client List]()

### Method: `getClientList`

Retrieves all clients associated with the authenticated user.

Use this method to fetch the full list of clients the agent is associated with. This is typically called at the start of a session or when the agent needs to select a client to check in at. The returned `ClientDetails` objects contain the information your app needs to present a client selection UI or to look up the correct `clientId` for check-in/check-out operations.

### Parameters

| Parameter | Type | Description |
|---|---|---|
| `context` | `Context` | Android context. Pass `this`. |
| `token` | `String` | Access token from `initialize`. |

### Callbacks

| Callback | Description |
|---|---|
| `onClientListSuccess(List<ClientDetails>)` | Returns a list of `ClientDetails` objects with client information. |
| `onClientListError(ErrorResponse)` | Returns error code and message on failure. |
| `onClientListError(String)` | Returns a plain string error message on failure. |

### Kotlin

```kotlin
// Method signature
Workmate.getClientList(
    context, token,
    object : WMClientListListener {
        override fun onClientListSuccess(clientList: List<ClientDetails>) {
            // Do something
        }
        override fun onClientListError(errorResponse: ErrorResponse) {
            // Do something
        }
        override fun onClientListError(error: String) {
            // Do something
        }
    }
)

// Example
Workmate.getClientList(
    this, token,
    object : WMClientListListener {
        override fun onClientListSuccess(clientList: List<ClientDetails>) {
            Log.d("Clients", clientList.toString())
        }
        override fun onClientListError(errorResponse: ErrorResponse) {
            Log.d("Client List Error", errorResponse.toString())
        }
        override fun onClientListError(error: String) {
            Log.d("Client List Error", error)
        }
    }
)
```

### Java

```java
// Method signature
Workmate.getClientList(
    context, token,
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

// Example
Workmate.getClientList(
    this, token,
    new WMClientListListener() {
        @Override
        public void onClientListSuccess(List<ClientDetails> clientList) {
            Log.d("Clients", clientList.toString());
        }
        @Override
        public void onClientListError(ErrorResponse errorResponse) {
            Log.d("Client List Error", errorResponse.toString());
        }
        @Override
        public void onClientListError(String error) {
            Log.d("Client List Error", error);
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
