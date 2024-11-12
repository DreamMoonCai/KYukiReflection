# 介绍

> `KYukiReflection` 是一个参考基于 [YukiReflection](https://github.com/HighCapable/YukiReflection) 的变种Kotlin反射 API。

## 背景

这是一个旨在不改动原版 [YukiReflection](https://github.com/HighCapable/YukiReflection) 去使用 Kotlin高级反射框架完成相关反射任务的全新API。

通过对Kotlin更多特性支持如关键字，函数式编程，扩展函数、泛型等特性，使得 `KYukiReflection` 做到更精确的进行反射查找。

## 用途

`KYukiReflection` 完全沿袭 `YukiReflection` 语法构建，你可以像 `YukiReflection` 一样使用 `KYukiReflection`。

同样他能支持 `Java` 相关类字段方法使用，使用更加人性化的语言实现一套更加完善的反射方案。

## 语言要求

同 `YukiReflection` 请使用 Kotlin，API 部分代码构成同样兼容 Java 但基础反射场景的实现**可能完全无法使用**。

文档全部的 Demo 示例代码都将使用 Kotlin 进行描述，如果你完全不会使用 Kotlin 那你将有可能无法使用 `YukiReflection`。

## 灵感来源

`KYukiReflection` 是对Kotlin蹩脚难以理解的反射进行汇总整合 项目理念完全参考 `YukiReflection`，同样这套反射 API 可以在任何 Java 和 Android 平台的项目中使用。

现在，我们只需要编写少量的代码，就能实现一个简单的Kotlin式的反射调用。

借助 Kotlin 优雅的 **lambda** 写法以及 `KYukiReflection`，可以让你的反射逻辑更加美观清晰，**烦请注意 `KYukiReflection` 在任何地方都请将`Class`替换或转换为`KClass`尽管可能支持`Class`但如果使用此API请对转换`KClass`养成习惯。**

> 示例如下

:::: code-group
::: code-group-item KYuki Reflection

```kotlin
"android.os.SystemProperties".toKClass()
    .function {
        name = "get"
        param(StringKClass, StringKClass)
    }.get().call("ro.system.build.fingerprint", "none")
```

:::
::: code-group-item Kotlin Reflection

```kotlin
Class.forName("android.os.SystemProperties")
    .kotlin
    .staticFunctions
    .first {
        it.name == "get" &&
                it.valueParameters[0].type.jvmErasure == String::class &&
                it.valueParameters[1].type.jvmErasure == String::class
    }.call("ro.system.build.fingerprint", "none")
```

:::
::::