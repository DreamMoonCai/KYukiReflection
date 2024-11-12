# Introduce

> `KYukiReflection` is a reference to a variant of the Kotlin reflection API based on [YukiReflection](https://github.com/HighCapable/YukiReflection).

## Background

This is a new API designed to use the Kotlin advanced reflection framework to complete related reflection tasks without changing the original [YukiReflection](https://github.com/HighCapable/YukiReflection).

By supporting more features of Kotlin such as keywords, functional programming, extended functions, generics and other features, `KYukiReflection` can perform reflection searches more accurately.

## Usage

`KYukiReflection` is completely built from `YukiReflection` syntax, you can use `KYukiReflection` like `YukiReflection`.

It can also support the use of `Java` related class field methods and use a more user-friendly language to implement a more complete reflection solution.

## Language Requirement

Please use Kotlin with `YukiReflection`. Part of the code structure of the API is also compatible with Java, but the implementation of basic reflection scenarios may not be used at all.

All Demo sample codes in the document will be described using Kotlin. If you don’t know how to use Kotlin at all, you may not be able to use `YukiReflection`.

## Source of Inspiration

`KYukiReflection` is a summary and integration of Kotlin's poor and incomprehensible reflection. The project concept completely refers to `YukiReflection`. The same set of reflection API can be used in any Java and Android platform projects.

Now, we only need to write a small amount of code to implement a simple Kotlin-style reflection call.

With Kotlin's elegant **lambda** writing method and `KYukiReflection`, you can make your reflection logic more beautiful and clearer. **Please note that with `KYukiReflection`, please replace or convert `Class` to `KClass` wherever possible. `Class` will be supported in the future, but if you use this API, please develop a habit of converting `KClass`.**

> The following example

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