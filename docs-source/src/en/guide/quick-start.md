# Quick Start

> Integrate `KYukiReflection` into your project.

## Environment Requirements

- Windows 7 and above / macOS 10.14 and above / Linux distributions (Arch/Debian)

- Android Studio 2024.1 and above

- IntelliJ IDEA 2024.2(The latest Android components are not supported at this time) and above

- Kotlin 2.0.0 and above

- Android Gradle Plugin 8.5 and above

- Gradle 8.8 and above

- Java 21 and above

## Project Requirements

The project needs to be created using `Android Studio` or `IntelliJ IDEA` and the type is an Java or Android project and the Kotlin environment dependency has been integrated.

## Integration Dependencies

We recommend using Kotlin DSL as the Gradle build script language and [SweetDependency](https://github.com/HighCapable/SweetDependency) to manage dependencies.

#### SweetDependency (Recommended)

Add the repositories and dependencies in your project's `SweetDependency` configuration file.

> The following example

```yaml
repositories:
  # MavenCentral has a 2-hour cache,
  # if the latest version cannot be integrated, please add this
  sonatype-oss-releases:

libraries:
  io.github.dreammooncai.yukireflection:
    api:
      version: +
  ...
```

After adding it, run Gradle Sync and all dependencies will be autowired.

Next, deploy dependencies in your project `build.gradle.kts`.

> The following example

```kotlin
dependencies {
    implementation(io.github.dreammooncai.yukireflection.api)
    // ...
}
```

#### Traditional Method

Add repositories in your project `build.gradle.kts` or `build.gradle`.

> Kotlin DSL

```kotlin
repositories {
    google()
    mavenCentral()
    // MavenCentral has a 2-hour cache, if the latest version cannot be integrated, please add this URL
    maven { url("https://s01.oss.sonatype.org/content/repositories/releases/") }
}
```

> Groovy DSL

```groovy
repositories {
    google()
    mavenCentral()
    // MavenCentral has a 2-hour cache, if the latest version cannot be integrated, please add this URL
    maven { url 'https://s01.oss.sonatype.org/content/repositories/releases/' }
}
```

Add dependencies in your project `build.gradle.kts` or `build.gradle`.

> Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.dreammooncai.yukireflection:api:<yuki-version>")
    // ...
}
```

> Groovy DSL

```groovy
dependencies {
    implementation 'io.github.dreammooncai.yukireflection:api:<yuki-version>'
    // ...
}
```

Please change **&lt;yuki-version&gt;** to the latest version [here](../about/changelog).

::: danger

If your project is currently using the 1.x.x version of [KYukiHookAPI](https://github.com/DreamMoonCai/KYukiHookAPI),You can still continue to integrate **KYukiReflection**, because the library that **KYukiHookAPI depends on will be implicitly packaged into your project even if it is not integrated,

and may be changed in the future, 

for more tutorials, please refer to the [Documentation] (https://dreammooncai.github.io/KYukiHookAPI/zh-cn/) of **KYukiHookAPI** Check out the tutorials.

KYukiHookAPI will be changed in YukiHookAPI 2.0.0.

**KYukiHookAPI** will be completely separated from **KYukiReflection** in version **2.0.0**, by which time you can use it with **KYukiHookAPI** at the same time.

:::

#### Configure Java Version

Modify the Java version of Kotlin in your project `build.gradle.kts` or `build.gradle` to 21 or above.

> Kotlin DSL

```kt
android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}
```

> Groovy DSL

```groovy
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_21
        targetCompatibility JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = '21'
    }
}
```

::: warning

Since API **1.0.1**, the Java version used by Kotlin defaults to 21, and versions 11 and below are no longer supported.

:::