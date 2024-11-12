# API 基本配置

> 这里介绍了 `KYukiReflection` 的基本配置方法。

`KYukiReflection` 无需一些复杂的配置即可直接开始使用，且不会与 Java 原生的反射 API 冲突。

你可以在使用之前对 `KYukiReflection` 进行一些功能配置。

## 获取 API 标签 & 版本

你可以通过如下方式获取当前 `KYukiReflection` 的标签和版本。

> 示例如下

```kotlin
// 获取标签
val tag = KYukiReflection.TAG
// 获取版本
val version = KYukiReflection.VERSION
```

你可以通过获取版本进行一些不同版本差异的判断或用于显示在关于信息中。

::: tip

更多功能请参考 [KYukiReflection](../api/public/io/github/dreammooncai/yukireflection/KYukiReflection)。

:::

## 配置 API 相关功能

你可以通过 `KYukiReflection.configs { ... }` 方法或 `KYukiReflection.Configs` 来配置相关功能。

### 自定义调试日志标签

你可以使用如下方式来自定义调试日志的标签。

API 内部的日志将会使用此标签进行打印。

> 示例如下

```kotlin
// 通过 configs 方法
KYukiReflection.configs {
    debugLog {
        tag = "YourCustomTag"
    }
}
// 直接设置
KYLog.Configs.tag = "YourCustomTag"
```

### 启用或禁用 Debug 模式

你可以使用如下方式来启用或禁用 Debug 模式。

Debug 模式默认是关闭的，启用后将会打印详细日志 (例如反射查找功能的耗时) 到控制台。

> 示例如下

```kotlin
// 通过 configs 方法
KYukiReflection.configs {
    isDebug = true
}
// 直接设置
KYukiReflection.Configs.isDebug = true
```

### 启用或禁用调试日志的输出功能

你可以使用如下方式来启用或禁用调试日志的输出功能。

此功能默认启用，关闭后将会停用 `KYukiReflection` 对全部日志的输出。

> 示例如下

```kotlin
// 通过 configs 方法
KYukiReflection.configs {
    debugLog {
        isEnable = true
    }
}
// 直接设置
KYLog.Configs.isEnable = true
```

### 使用 configs 方法配置

为了一次性配置多个功能，你可以直接使用 `KYukiReflection.configs { ... }` 方法进行配置。

> 示例如下

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

更多功能请参考 [KYukiReflection.configs](../api/public/io/github/dreammooncai/yukireflection/KYukiReflection#configs-method) 方法、[KYukiReflection.Configs](../api/public/io/github/dreammooncai/yukireflection/KYukiReflection#configs-object)。

:::