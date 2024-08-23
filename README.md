# mappls-workmate-android-sdk
Native Android SDK for our Workforce Management &amp; Automation Solution - Workmate

[<img src="https://about.mappls.com/images/mappls-b-logo.svg" height="60"/> </p>](https://www.mapmyindia.com/api)

# WORKMATE SDK

**Easy To Integrate WORKMATE SDKs For Web & Mobile Applications**

Powered by India's most comprehensive and robust task performing functionalities.

1. You can get your api key to be used in this document here: [https://apis.mappls.com/console/](https://apis.mappls.com/console/)

2. The sample code is provided to help you understand the basic functionality of task methods working on **Web**, **Android** & **iOS** native development platform.

4. Explore through [200 + nations with Global Search](https://github.com/mappls-api/mappls-rest-apis/blob/main/docs/countryISO.md) with **Global Search, Routing and Mapping APIs & SDKs** by Mappls.

## [Getting Started]()

The `postEvent` function allows you to efficiently track and update the progress of tasks within your application. By using `postEvent`, you can record each step of a task, ensuring real-time updates and accurate tracking of task progress. This functionality is crucial for maintaining up-to-date task status and providing users with timely information about their tasks.

Additionally, the `manageWorkDay` function facilitates the management of workday schedules. It enables you to mark the beginning and end of a workday, effectively tracking attendance and ensuring accurate records of work hours. This feature is essential for businesses looking to streamline attendance management and monitor employee work patterns. Together, these functions enhance task management and attendance tracking in your application, offering a comprehensive solution for workday and task progress management.


## [Setup your project]()

Follow these steps to add the SDK to your project –

-   Create a new project in Android Studio

**For older Build versions (i.e, Before gradle  v7.0.0)**
-  Add Mappls repository in your project level `build.gradle`
~~~groovy  
 allprojects {  
    repositories {  
  
        maven {  
            url 'https://maven.mappls.com/repository/mappls/'  
        }  
    }  
}  
~~~  
**For Newer Build Versions (i.e, After gradle v7.0.0)**
- Add Mappls repository in your `settings.gradle`
```groovy  
dependencyResolutionManagement {  
//   repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)  
  repositories {  
        mavenCentral()  
        maven {  
            url 'https://maven.mappls.com/repository/mappls/'  
        }  
    }  
   }  
```
-   Add below dependency in your app-level `build.gradle`

```groovy
implementation 'com.mappls.sdk:mappls-workmate-sdk:1.0.0'
```
- Add these permissions in your project
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```
### [Add Java 8 Support to the project]()

*add following lines in your app module's build.gradle*

```groovy
compileOptions {
    sourceCompatibility 1.8
    targetCompatibility 1.8
}
  ```

### [Add your API keys to the SDK]()
*Add your API keys to the SDK (in your application's onCreate() or before using map)*

#### Java
```java
MapplsAccountManager.getInstance().setRestAPIKey("SET_REST_API_KEY");
MapplsAccountManager.getInstance().setSDKKey("SET_MAP_KEY");
MapplsAccountManager.getInstance().setClientId("SET_CLIENT_ID");
MapplsAccountManager.getInstance().setClientSecret("SET_CLIENT_SECRET");
Mappls.getInstance(getApplicationContext());
```

#### Kotlin
```kotlin
MapplsAccountManager.getInstance().setRestAPIKey = "SET_REST_API_KEY"
MapplsAccountManager.getInstance().setSDKKey = "SET_MAP_KEY"
MapplsAccountManager.getInstance().setClientId = "SET_CLIENT_ID"
MapplsAccountManager.getInstance().setClientSecret = "SET_CLIENT_SECRET"
Mappls.getInstance(applicationContext)
```
*You cannot use the Mappls Workmate Mobile SDK without these function calls. You will find your keys in your API Dashboard.*


## [Add a Mappls Workmate SDK to your application]()

## Initialize Workmate SDK

Initialization can be done either of the below mentioned method. Keep the same method accross your project.

Initialize the SDK with your Application Id, ClientID, ClientSecret, DeviceID ,DeviceFingerprint(optional).

#### Java

```java
// IAuthListener - returns authorization results in the forms of callbacks.
Workmate.initialize(appId, clientId, clientSecret, deviceId, deviceFingerPrint, new IAuthListener() {
	@Override
	public void onSuccess(Long id) {
			  //write your code here
	}
	@Override
	public void onError(String reason, String identifier, String description) {
	         // reason gives the error type.
            // errorIdentifier gives information about error code.
           // errorDescription gives a message for a particular error.
	}
});

```

#### Kotlin

```Kotlin
Workmate.initialize(appId, clientId, clientSecret, deviceId, deviceFingerPrint, object : IAuthListener {
    override fun onSuccess(Long entityId) {
         //write your code here
    }
	override fun onError(reason: String?, errorIdentifier: String?, errorDescription: String?) {
       // reason gives the error type.
      // errorIdentifier gives information about error code.
      // errorDescription gives a message for a particular error.
    }

})
```

On sucessful initialization you will get the Id, use this Id to invoke methods to perform the man tasks or to get the live location.

### Workmate method to add task  

This section describes how to use workmate for add task .

#### Java
~~~java
Workmate.addtask(String accessID, String appId, String userId, JsonObject taskObject)
~~~

#### Kotlin
~~~kotlin
Workmate.addtask(String accessID, String appId, String userId, JsonObject taskObject)
~~~

### Workmate method to fetch Task Stats 

This section describes how to use workmate for fetching task stats.

#### Java
~~~java
Workmate.getTaskStats(String accessID, String appId, String userId)
~~~

#### Kotlin
~~~kotlin
Workmate.getTaskStats(String accessID, String appId, String userId)
~~~

### Workmate method to Start and End Work Day

This section describes how to use workmate method for starting and ending a work day, which also be used for mark attendance.

#### Java
```java
Workmate.manageWorkDay(String accessID, String appId, String userId, String workDayAction)
```

#### Kotlin
~~~kotlin
Workmate.manageWorkDay(String accessID, String appId, String userId, String workDayAction)
~~~

### Workmate method to Start and End task

This section describes how to use workmate for starting and ending a task.
#### Java
```java
Workmate.postEvent(String accessID, String appId, String userId, String taskId, String eventCode, String eventName, String remarks)
```

#### Kotlin
~~~kotlin
Workmate.postEvent(String accessID, String appId, String userId, String taskId, String eventCode, String eventName, String remarks)
~~~

### Workmate method Responses
~~~Response
{
  "message": "Success",
  "status": 200
}
~~~
~~~Response
{
  "message": "<message>",
  "status": 202
}
~~~
~~~Response
{
  "message": "Unauthorized. Invalid Access ID",
  "status": 401
}
~~~
~~~Response
{
  "message": "Unauthorized. Invalid Access ID (Expired)",
  "status": 401
}
~~~
~~~Response
{
  "message": "Expectation Failed",
  "status": 417
}
~~~

<br>

For any queries and support, please contact: 

[<img src="https://about.mappls.com/images/mappls-logo.svg" height="40"/> </p>](https://about.mappls.com/api/)
Email us at [apisupport@mappls.com](mailto:apisupport@mappls.com)


![](https://www.mapmyindia.com/api/img/icons/support.png)
[Support](https://about.mappls.com/contact/)
Need support? contact us!

<br></br>
<br></br>

[<p align="center"> <img src="https://www.mapmyindia.com/api/img/icons/stack-overflow.png"/> ](https://stackoverflow.com/questions/tagged/mappls-api)[![](https://www.mapmyindia.com/api/img/icons/blog.png)](https://about.mappls.com/blog/)[![](https://www.mapmyindia.com/api/img/icons/gethub.png)](https://github.com/Mappls-api)[<img src="https://mmi-api-team.s3.ap-south-1.amazonaws.com/API-Team/npm-logo.one-third%5B1%5D.png" height="40"/> </p>](https://www.npmjs.com/org/mapmyindia) 



[<p align="center"> <img src="https://www.mapmyindia.com/june-newsletter/icon4.png"/> ](https://www.facebook.com/Mapplsofficial)[![](https://www.mapmyindia.com/june-newsletter/icon2.png)](https://twitter.com/mappls)[![](https://www.mapmyindia.com/newsletter/2017/aug/llinkedin.png)](https://www.linkedin.com/company/mappls/)[![](https://www.mapmyindia.com/june-newsletter/icon3.png)](https://www.youtube.com/channel/UCAWvWsh-dZLLeUU7_J9HiOA)




<div align="center">@ Copyright 2024 CE Info Systems Ltd. All Rights Reserved.</div>

<div align="center"> <a href="https://about.mappls.com/api/terms-&-conditions">Terms & Conditions</a> | <a href="https://about.mappls.com/about/privacy-policy">Privacy Policy</a> | <a href="https://about.mappls.com/pdf/mapmyIndia-sustainability-policy-healt-labour-rules-supplir-sustainability.pdf">Supplier Sustainability Policy</a> | <a href="https://about.mappls.com/pdf/Health-Safety-Management.pdf">Health & Safety Policy</a> | <a href="https://about.mappls.com/pdf/Environment-Sustainability-Policy-CSR-Report.pdf">Environmental Policy & CSR Report</a>

<div align="center">Customer Care: +91-9999333223</div>
