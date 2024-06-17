# API Basic Configs

> The basic configuration method of `KYukiReflection` is introduced here.

`KYukiReflection` can be used directly without some complex configuration, and does not conflict with Java's native Reflection API.

You can configure some functions of `KYukiReflection` before using it.

## Get the API Tag & Version

You can get the current tag and version of `KYukiReflection` as follows.

> The following example

```kotlin
// Get the tag
val tag = KYukiReflection.TAG
// Get the version
val version = KYukiReflection.VERSION
```

You can judge the difference between different versions or display it in the about information by obtaining the version.

::: tip

For more functions, please refer to [KYukiReflection](../api/public/com/DreamMoonCai/KYukiReflection/KYukiReflection).

:::

## Configure API Related Functions

You can configure related functions through `KYukiReflection.configs { ... }` method or `KYukiReflection.Configs`.

### Custom Debug Log Tag

You can use the following methods to customize the tag of the debug log.

Logs inside the API will be printed using this tag.

> The following example

```kotlin
// Via the configs method
KYukiReflection.configs {
    debugLog {
        tag = "YourCustomTag"
    }
}
// Set directly
YLog.Configs.tag = "YourCustomTag"
```

### Enable or Disable Debug Mode

You can use the following methods to enable or disable Debug mode.

The Debug mode is disabled by default, and when enabled, detailed logs (such as the time spent on the reflective search function) will be printed to the console.

> The following example

```kotlin
// Via the configs method
KYukiReflection.configs {
    isDebug = true
}
// Set directly
KYukiReflection.Configs.isDebug = true
```

### Enable or Disable Debug Logs

You can use the following methods to enable or disable debug logs.

This function is enabled by default, and disable will stop `KYukiReflection` output all logs.

> The following example

```kotlin
// Via the configs method
KYukiReflection.configs {
    debugLog {
        isEnable = true
    }
}
// Set directly
YLog.Configs.isEnable = true
```

### Use the configs Method to Configure

In order to configure multiple features at once, you can directly use the `KYukiReflection.configs { ... }` method to configure.

> The following example

```kotlin
KYukiReflection.configs {
    debugLog {
        tag = "YourCustomTag"
        isEnable = true
    }
    isDebug = true
}
```

::: tip

For more functions, please refer to [KYukiReflection.configs](../api/public/com/DreamMoonCai/KYukiReflection/KYukiReflection#configs-method) method, [YukiReflection.Configs](../api/public/com/DreamMoonCai/KYukiReflection/KYukiReflection#configs-object).

:::