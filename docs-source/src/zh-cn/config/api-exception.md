---
pageClass: hidden-anchor-page
---

# API 异常处理

> 异常是在开发过程经常遇到的主要问题，这里介绍了 `KYukiReflection` 在使用过程中可能遇到的常见异常以及处理方式。

这里的异常说明只会同步最新的 API 版本，较旧的 API 版本的异常将不会再进行说明，请始终保持 API 版本为最新。

## 非阻断异常

> 这些异常不会导致 APP 停止运行 (FC)，但是会在控制台打印 `E` 级别的日志，也可能会停止继续执行相关功能。

###### exception

::: danger loggerE

Method/Constructor/Field match type "**TYPE**" not allowed

:::

**异常原因**

在查找方法、构造方法以及变量时设置了不允许的参数类型。

> 示例如下

```kotlin
// 查找一个方法
method {
    // 设置了无效的类型举例
    param(false, 1, 0)
    // 设置了无效的类型举例
    returnType = false
}

// 查找一个变量
field {
    // 设置了无效的类型举例
    type = false
}
```

**解决方案**

在查找中 `param`、`returnType`、`type` 中仅接受 `Class`、`String`、`VariousClass` 类型的传值，不可传入参数实例。

> 示例如下

```kotlin
// 查找一个方法
method {
    // ✅ 正确的使用方法举例
    param(BooleanType, IntType, IntType)
    // ✅ 正确的使用方法举例
    returnType = BooleanType
    // ✅ 以下方案也是正确的
    returnType = "java.lang.Boolean"
}

// 查找一个变量
field {
    // ✅ 正确的使用方法举例
    type = BooleanType
}
```

###### exception

::: danger loggerE

NoSuchMethod/NoSuchConstructor/NoSuchField happend in \[**NAME**\]

:::

**异常原因**

在查找方法、构造方法以及变量时并未找到目标方法、构造方法以及变量。

**解决方案**

请确认你的查找条件是否能正确匹配到目标 `Class` 中的指定方法、构造方法以及变量。

###### exception

::: danger loggerE

Trying **COUNT** times and all failure by RemedyPlan

:::

**异常原因**

使用 `RemedyPlan` 重新查找方法、构造方法、变量时依然没有找到方法、构造方法、变量。

**解决方案**

请确认你设置的 `RemedyPlan` 参数以及当前 APP 内存在的 `Class`，再试一次。

###### exception

::: danger loggerE

Can't find this Class in \[**CLASSLOADER**\]: **CONTENT** Generated by KYukiReflection#ReflectionTool

:::

**异常原因**

通过 `ClassLoader.searchClass` 找不到需要查找的 `Class` 对象。

> 示例如下

```kotlin
customClassLoader?.searchClass {
    from(...)
    // ...
}.get()
```

**解决方案**

这是一个安全异常，请检查你设置的条件，使用相关工具查看所在 **Dex** 中的 `Class` 以及字节码对象特征，并再试一次。

###### exception

::: danger loggerE

Can't find this Method/Constructor/Field in \[**CLASS**\]: **CONTENT** Generated by KYukiReflection#ReflectionTool

:::

**异常原因**

通过指定条件找不到需要查找的方法、构造方法以及变量。

> 示例如下

```kotlin
TargetClass.method {
    name = "test"
    param(BooleanType)
}
```

**解决方案**

这是一个安全异常，请检查你设置的条件，使用相关工具查看所在 `Class` 中的字节码对象特征，并再试一次。

###### exception

::: danger loggerE

The number of VagueType must be at least less than the count of paramTypes

:::

**异常原因**

在 `Method`、`Constructor` 查找条件中错误地使用了 `VagueType`。

> 示例如下

```kotlin
TargetClass.method {
    name = "test"
    // <情景1>
    param(VagueType)
    // <情景2>
    param(VagueType, VagueType ...)
}
```

**解决方案**

`VagueType` 不能在方法、构造方法参数中完全填充，若存在这样的需求请使用 `paramCount`。

###### exception

::: danger loggerE

Field match type class is not found

:::

**异常原因**

在查找变量时所设置的查找条件中 `type` 的 `Class` 实例未被找到。

> 示例如下

```kotlin
field {
    name = "test"
    // 假设这里设置的 type 的 Class 并不存在
    type = "com.example.TestClass"
}
```

**解决方案**

请检查查找条件中 `type` 的 `Class` 是否存在，然后再试一次。

###### exception

::: danger loggerE

Method match returnType class is not found

:::

**异常原因**

在查找方法时所设置的查找条件中 `returnType` 的 `Class` 实例未被找到。

> 示例如下

```kotlin
method {
    name = "test"
    // 假设这里设置的 returnType 的 Class 并不存在
    returnType = "com.example.TestClass"
}
```

**解决方案**

请检查查找条件中 `returnType` 的 `Class` 是否存在，然后再试一次。

###### exception

::: danger loggerE

Method/Constructor match paramType\[**INDEX**\] class is not found

:::

**异常原因**

在查找方法、构造方法时所设置的查找条件中 `param` 的 `index` 号下标的 `Class` 实例未被找到。

```kotlin
method {
    name = "test"
    // 假设这里设置的 1 号下标的 Class 并不存在
    param(StringClass, "com.example.TestClass", BooleanType)
}
```

**解决方案**

请检查查找条件中 `param` 的 `index` 号下标的 `Class` 是否存在，然后再试一次。

## 阻断异常

> 这些异常会直接导致 APP 停止运行 (FC)，同时会在控制台打印 `E` 级别的日志。

###### exception

::: danger NoClassDefFoundError

Can't find this Class in \[**CLASSLOADER**\]: **CONTENT** Generated by KYukiReflection#ReflectionTool

:::

**异常原因**

通过 `String.toClass(...)` 或 `classOf<...>()` 找不到需要查找的 `Class` 对象。

> 示例如下

```kotlin
"com.demo.Test".toClass()
```

**解决方案**

请检查当前字符串或实体匹配到的 `Class` 是否存在于当前 `ClassLoader`，并再试一次。

###### exception

::: danger IllegalStateException 

ClassLoader \[**CLASSLOADER**\] is not a DexClassLoader

:::

**异常原因**

使用 `ClassLoader.searchClass` 查找 `Class` 但是当前 `ClassLoader` 并不继承于 `BaseDexClassLoader`。

**解决方案**

这种情况基本不存在，除非当前 APP 引用了非 ART 平台的可执行文件 (但是这种情况还是不会存在) 或当前 `ClassLoader` 为空。

###### exception

::: danger IllegalStateException 

VariousClass match failed of those **CLASSES**

:::

**异常原因**

在使用 `VariousClass` 创建不确定的 `Class` 对象时全部的 `Class` 都没有被找到。

**解决方案**

检查当前 APP 内是否存在其中能够匹配的 `Class` 后，再试一次。

###### exception

::: danger IllegalStateException 

paramTypes is empty, please use emptyParam() instead

:::

**异常原因**

在查找方法、构造方法时保留了空的 `param` 方法。

> 示例如下

```kotlin
method {
    name = "test"
    // 括号内没有填写任何参数
    param()
}
```

**解决方案**

若要标识此方法、构造方法没有参数，你可以有如下设置方法。

第一种，设置 `emptyParam` (推荐)

> 示例如下

```kotlin
method {
    name = "test"
    emptyParam()
}
```

第二种，设置 `paramCount = 0`

> 示例如下

```kotlin
method {
    name = "test"
    paramCount = 0
}
```

###### exception

::: danger IllegalStateException 

Cannot create classes cache for "android", please remove "name" param

:::

**异常原因**

在系统框架 (android) 中使用了 `DexClassFinder` 的缓存功能 `searchClass(name = ...)`。

> 示例如下

```kotlin
searchClass(name = "test") {
    from(...)
    // ...
}.get()
```

**解决方案**

由于缓存会将找到的 `Class` 名称存入 `SharedPreferences`，但是系统框架不存在 data 目录，所以请不要在系统框架中使用此功能。

###### exception

::: danger IllegalStateException 

Target Class type cannot cast to **TYPE**

:::

**异常原因**

使用 `Class.toClass`、`Class.toClassOrNull`、`GenericClass.argument` 方法将字符串类名转换为目标 `Class` 时声明了错误的类型。

以下使用 `Class.toClass` 方法来进行示例。

> 示例如下

```kotlin
// 假设目标类型是 Activity 但是被错误地转换为了 WrongClass 类型
val clazz = "android.app.Activity".toClass<WrongClass>()
```

**解决方案**

> 示例如下

```kotlin
// <解决方案 1> 填写正确的类型
val clazz1 = "android.app.Activity".toClass<Activity>()
// <解决方案 2> 不填写泛型声明
val clazz2 = "android.app.Activity".toClass()
```

请确保执行方法后声明的泛型是指定的目标 `Class` 类型，在不确定目标类型的情况下你可以不需要填写泛型声明。