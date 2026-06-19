[<img src="https://about.mappls.com/images/mappls-b-logo.svg" height="60"/>](https://www.mapmyindia.com/api)

# Getting Started

[← Back to Documentation](README.md)

Before using any Workmate SDK methods, you need to add the Mappls repositories and the SDK dependency to your project's Gradle files. Follow the setup that matches your Android Studio and AGP version below.

---

## ⚠️ Breaking Change Notice

> **Important**
>
> Starting from this version, password-based initialization has been removed from the SDK.
>
> Please remove the password parameter from your codebase and migrate to the new initialization mechanism before upgrading.

---

## [Add Repositories]()

### Below AGP 7.0.0 — project-level `build.gradle`

```groovy
allprojects {
  repositories {
    maven { url 'https://maven.mappls.com/repository/mappls/' }
    maven { url 'https://maven.mappls.com/repository/workmate/' }
  }
}
```

### Above AGP 7.0.0 — `settings.gradle`

```groovy
dependencyResolutionManagement {
  repositories {
    mavenCentral()
    maven { url 'https://maven.mappls.com/repository/mappls/' }
    maven { url 'https://maven.mappls.com/repository/workmate/' }
  }
}
```

### Kotlin DSL — `settings.gradle.kts`

```kotlin
dependencyResolutionManagement {
  repositories {
    mavenCentral()
    maven { url = uri("https://maven.mappls.com/repository/mappls/") }
    maven { url = uri("https://maven.mappls.com/repository/workmate/") }
  }
}
```

---

## [Add the SDK Dependency]()

In your app-level `build.gradle`, add the following dependency and sync your project:

```groovy
implementation 'com.mappls.sdk:mappls-workmate:<replace with latest version>'
```

Once the sync is complete, you are ready to initialize the SDK. Head over to [Initialization](USERS.md#initialization) to get started.

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
