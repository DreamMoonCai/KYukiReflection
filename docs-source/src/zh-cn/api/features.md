# 功能介绍

> 这里包含了 `KYukiReflection` 全部核心功能的用法示例。

## KClass 扩展

> 这里是 **KClass** 对象自身相关的扩展功能。

### 对象转换

假设我们要得到一个不能直接调用的 `KClass`，通常情况下，我们可以使用标准的反射 API 去查找这个 `KClass`。

> 示例如下

```kotlin
// 默认 ClassLoader 环境下的 Class
var instance = Class.forName("com.demo.Test")
// 指定 ClassLoader 环境下的 Class
val customClassLoader: ClassLoader? = ... // 假设这个就是你的 ClassLoader
var instance = customClassLoader?.loadClass("com.demo.Test")?.kotlin
```

这种写法大概不是很友好，此时 `KYukiReflection` 就为你提供了一个可在任意地方使用的语法糖。

以上写法换做 `KYukiReflection` 可写作如下形式。

> 示例如下

```kotlin
// 直接得到这个 KClass
var instance = "com.demo.Test".toKClass()
// 自定义 KClass 所在的 ClassLoader
val customClassLoader: ClassLoader? = ... // 假设这个就是你的 ClassLoader
var instance = "com.demo.Test".toKClass(customClassLoader)
```

如果当前 `KClass` 并不存在，使用上述方法会抛出异常，如果你不确定 `KClass` 是否存在，可以参考下面的解决方案。

> 示例如下

```kotlin
// 直接得到这个 KClass
// 得不到时结果会为 null 但不会抛出异常
var instance = "com.demo.Test".toKClassOrNull()
// 自定义 KClass 所在的 ClassLoader
val customClassLoader: ClassLoader? = ... // 假设这个就是你的 ClassLoader
// 得不到时结果会为 null 但不会抛出异常
var instance = "com.demo.Test".toKClassOrNull(customClassLoader)
```

我们还可以通过映射来得到一个存在的 `KClass` 对象。

> 示例如下

```kotlin
// 假设这个 KClass 是能够被直接得到的
var instance = kclassOf<Test>()// 或者更直接的方法 Test::class
// 我们同样可以自定义 KClass 所在的 ClassLoader，这对于 stub 来说非常有效
val customClassLoader: ClassLoader? = ... // 假设这个就是你的 ClassLoader
var instance = kclassOf<Test>(customClassLoader)
```

::: tip

更多功能请参考 [kclassOf](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kclassof-method)、[String.toKClass](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#string-tokclass-ext-method)、[String.toKClassOrNull](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#string-tokclassornull-ext-method) 方法。

:::

### 延迟装载

假设我们要得到一个不能直接调用的 `KClass`，但是我们也不是立刻就需要这个 `KClass`。

这个时候，你可以使用 `lazyKClass` 来完成这个功能。

> 示例如下

```kotlin
// 延迟装载这个 KClass
val instance by lazyKClass("com.demo.Test")
// 自定义 KClass 所在的 ClassLoader
val customClassLoader: ClassLoader? = ... // 假设这个就是你的 ClassLoader
val instance by lazyKClass("com.demo.Test") { customClassLoader }
// 在适当的时候调用这个 KClass
instance.function { 
    // ...   
}
```

如果当前 `KClass` 并不存在，使用上述方法会抛出异常，如果你不确定 `KClass` 是否存在，可以参考下面的解决方案。

> 示例如下

```kotlin
// 延迟装载这个 KClass
// 得不到时结果会为 null 但不会抛出异常
val instance by lazyKClassOrNull("com.demo.Test")
// 自定义 Class 所在的 ClassLoader
val customClassLoader: ClassLoader? = ... // 假设这个就是你的 ClassLoader
// 得不到时结果会为 null 但不会抛出异常
val instance by lazyKClassOrNull("com.demo.Test") { customClassLoader }
// 在适当的时候调用这个 KClass
instance?.function { 
    // ...   
}
```

::: tip

更多功能请参考 [lazyKClass](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#lazykclass-method)、[lazyKClassOrNull](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#lazykclassornull-method) 方法。

:::

### 存在判断

假设我们要判断一个 `KClass` 是否存在，通常情况下，我们可以使用标准的反射 API 去查找这个 `KClass` 通过异常来判断是否存在。

> 示例如下

```kotlin
// 默认 ClassLoader 环境下的 KClass
var isExist = try {
    Class.forName("com.demo.Test")
    true
} catch (_: Throwable) {
    false
}
// 指定 ClassLoader 环境下的 KClass
val customClassLoader: ClassLoader? = ... // 假设这个就是你的 ClassLoader
var isExist = try {
    customClassLoader?.loadClass("com.demo.Test")
    true
} catch (_: Throwable) {
    false
}
```

这种写法大概不是很友好，此时 `KYukiReflection` 就为你提供了一个可在任意地方使用的语法糖。

以上写法换做 `KYukiReflection` 可写作如下形式。

> 示例如下

```kotlin
// 判断这个 KClass 是否存在
var isExist = "com.demo.Test".hasKClass()
// 自定义 KClass 所在的 ClassLoader
val customClassLoader: ClassLoader? = ... // 假设这个就是你的 ClassLoader
var isExist = "com.demo.Test".hasKClass(customClassLoader)
```

::: tip

更多功能请参考 [String.hasClass](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#string-haskclass-ext-method) 方法。

:::

### KClass 的常用大量拓展

让我们使用 `KClass` 与 Java `Class` 体验类似，支持非常多快捷强而有力的属性获取。

> 示例如下

```kotlin
// 默认 KClass 获取 isInterface 等
var isInterface = Test::class.java.isInterface
// 默认 KClass 获取 interfaces
Test::class.superclasses.filter { it.java.isInterface }
```

这种写法大概不是很友好尽管这在`Class`中易如反掌但我们置身于处处`KClass`的位置中稍显冗余，此时 `KYukiReflection` 就为你提供了一个可在任意地方使用的语法糖。

以上写法换做 `KYukiReflection` 可写作如下形式。

> 示例如下

```kotlin
// 获取 isInterface
var isInterface = Test::class.isInterface
// 获取 interfaces
Test::class.interfaces
// 获取唯一父类
Test::class.superclass
// 获取不为空的简易类名
Test::class.simpleNameOrJvm
// 获取不为空的完整类名
Test::class.name
// 是否是对象类
Test::class.isObject
// 获取其单例实例
Test::class.singletonInstance
// 获取类加载器
Test::class.classLoader
// 获取类所在的 kt 文件类
Test::class.top // ---> KClass<TestKt>
// 等几十余项常用属性拓展
```

::: tip

更多功能请参考 [KReflectionFactory](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory) 。

:::

## Jvm 扩展

> 这里是 **Jvm** 对象自身相关的扩展功能。

::: tip

**Member** 是 **Field**、**Method**、**Constructor** 的接口描述对象，它在 Java 反射中为 **Class** 中字节码成员的总称。

**Jvm** 是一种通用虚拟机平台 如支持 Java 等

为了更好的使用 `KYukiReflection` 我们为其增加了各种方便的转换

:::

我们可以轻松的将一个 `Class` 快速转换为 `KClass`

> 示例如下

```kotlin
val clazz:Class<*> = *
val kClass = clazz.kotlin
val kClassAs = clazz.kotlinAs // 部分Class不显示 kotlin 属性可以使用强行转换 kotlinAs
```

同时我们可以检查一个 `Class` 是否是 Kotlin 类，并且检查是否支持常规的 `KYukiReflection` 反射

> 示例如下

```kotlin
val clazz:Class<*> = *
val isKotlin = clazz.isKotlin
val isSupportReflection = clazz.kotlin.isSupportReflection // 是否支持常规Kotlin反射，不支持的只能使用签名查找或者 Java 原生的反射
val isKotlinNoError = clazz.isKotlinNoError // 检查是否是 Kotlin 类 并且支持常规Kotlin反射
```

当然了我们也可以轻松将一个 `Member` 快速转换为 `KCallable`

> 示例如下

```kotlin
// 假设这个method 是 fun Test.one()
val method:Method = *
// 等价与 Test::one
val function = method.kotlin
// 等价与 Test::class 中依次匹配相同的Method找到 KFunction
val kotlinFunction = method.kotlinFunction

// 假设这个method 是 val Test.user
val field:Field = *
// 等价与 Test::user
val property = field.kotlin
// 等价与 Test::class 中依次匹配相同的Field找到 KFunction
val kotlinProperty = property.kotlinProperty
```

将保留泛型信息的 `Java Type` 转换为 `KClass` 或 `KType`

> 示例如下

```kotlin
import java.lang.reflect.Type
import kotlin.reflect.KType

// Java 通过反射获取的父类泛型或者字段的返回类型，均包括泛型信息
val type: Type = *
val kClazz = type.classifier // ---> 除了单字母的泛型参数结果为KTypeParameter，其余均为KClass
val kType: KType = type.kotlinType // 如复杂的类型Java为 List<String>[][] -> 转换后 kotlin.Array<kotlin.Array<kotlin.collections.List<kotlin.String>>>
```

### Member 操作实例转换

拥有 `Member` 但不想用原生的 Java 方式反射调用，我们可以将其转换为封装好的实例，跟原 `YukiReflection` 的操作一样

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()

val method:Method = *
method.instance(instance).call("args1","args2")
method.instance(instance).string("args1","args2")//返回 String 类型

val field:Field = *
field.instance(instance).any()
field.instance(instance).set("args1")//返回 String 类型
```

::: tip

更多功能请参考 [KJvmFactory](../api/public/io/github/dreammooncai/yukiReflection/factory/KJvmFactory) 。

:::

## KClass 筛选

> 这里是 **KClass** 一组中筛选出满足条件的 KClass 相关的拓展功能

下面是一个简单的用法示例。

假设下面这个 `KClass` 是我们想要得到的，其中的名称经过了混淆，在每个版本可能都不一样。

> 示例如下

```java:no-line-numbers
package com.demo;

public class a extends Activity implements Serializable {

    public a(String var1) {
        // ...
    }

    private String a;

    private String b;

    private boolean a;

    protected void onCreate(Bundle var1) {
        // ...
    }

    private static void a(String var1) {
        // ...
    }

    private String a(boolean var1, String var2) {
        // ...
    }

    private void a() {
        // ...
    }

    public void a(boolean var1, a var2, b var3, String var4) {
        // ...
    }
}
```

此时，我们想得到这个 `KClass`，假设他在一个List中可以使用 `Collection<KClass<*>>.findClass` 方法。

下方演示的条件中每一个都是可选的，条件越复杂定位越精确，同时性能也会越差。

> 示例如下

```kotlin
listOf<KClass<*>>(/* 多个 KClass */).findClass {
    // 从指定的包名范围开始查找，实际使用时，你可以同时指定多个包名范围
    from("com.demo")
    // 指定当前 KClass 的 getSimpleName 的结果，你可以直接对这个字符串进行逻辑判断
    // 这里我们不确定它的名称是不是 a，可以只判断字符串长度
    simpleName { it.length == 1 }
    // 指定继承的父类对象，如果是存在的 stub，可以直接用泛型表示
    extends<Activity>()
    // 指定继承的父类对象，可以直接写为完整类名，你还可以同时指定多个
    extends("android.app.Activity")
    // 指定实现的接口，如果是存在的 stub，可以直接用泛型表示
    implements<Serializable>()
    // 指定实现的接口，可以直接写为完整类名，你还可以同时指定多个
    implements("java.io.Serializable")
    // 指定构造方法的类型与样式，以及在当前类中存在的个数 count
    constructor { param(StringKClass) }.count(num = 1)
    // 指定变量的类型与样式，以及在当前类中存在的个数 count
    property { type = StringKClass }.count(num = 2)
    // 指定变量的类型与样式，以及在当前类中存在的个数 count
    property { type = BooleanKClass }.count(num = 1)
    // 直接指定所有变量在当前类中存在的个数 count
    property().count(num = 3)
    // 如果你认为变量的个数是不确定的，还可以使用如下自定义条件
    property().count(1..3)
    property().count { it >= 3 }
    // 指定方法的类型与样式，以及在当前类中存在的个数 count
    function {
        name = "onCreate"
        param(BundleClass)
    }.count(num = 1)
    // 指定方法的类型与样式，同时指定修饰符，以及在当前类中存在的个数 count
    function {
        modifiers { isStatic && isPrivate }
        param(StringKClass)
        returnType = UnitType
    }.count(num = 1)
    // 指定方法的类型与样式，同时指定修饰符，以及在当前类中存在的个数 count
    function {
        modifiers { isPrivate && isStatic.not() }
        param(BooleanKClass, StringKClass)
        returnType = StringKClass
    }.count(num = 1)
    // 指定方法的类型与样式，同时指定修饰符，以及在当前类中存在的个数 count
    function {
        modifiers { isPrivate && isStatic.not() }
        emptyParam()
        returnType = UnitType
    }.count(num = 1)
    // 指定方法的类型与样式，同时指定修饰符和模糊类型 VagueKotlin，以及在当前类中存在的个数 count
    function {
        modifiers { isPrivate && isStatic.not() }
        param(BooleanKClass, VagueKotlin, VagueKotlin, StringKClass)
        returnType = UnitType
    }.count(num = 1)
    // 直接指定所有方法在当前类中存在的个数 count
    function().count(num = 5)
    // 如果你认为方法的个数是不确定的，还可以使用如下自定义条件
    function().count(1..5)
    function().count { it >= 5 }
    // 直接指定所有成员 (KCallable) 在当前类中存在的个数 count
    // 成员包括：KProperty (属性/变量)、KFunction (函数/方法)、KConstructor (构造方法)
    callable().count(num = 9)
    // 所有成员中一定存在一个 static 修饰符，可以这样加入此条件
    callable {
        modifiers { isStatic }
    }
}.get() // 得到这个 KClass 本身的实例，找不到会返回 null
```

::: tip

上述用法中对于 **KProperty**、**KFunction**、**KFunction Constructor** 的条件用法与 [KCallable 扩展](#kcallable-扩展) 中的相关用法是一致的，仅有小部分区别。

更多功能请参考 [KCallableRules](../api/public/io/github/dreammooncai/yukiReflection/finder/classes/rules/KCallableRules)、[KPropertyRules](../api/public/io/github/dreammooncai/yukiReflection/finder/classes/rules/KPropertyRules)、[KFunctionRules](../api/public/io/github/dreammooncai/yukiReflection/finder/classes/rules/KFunctionRules)、[KConstructorRules](../api/public/io/github/dreammooncai/yukiReflection/finder/classes/rules/KConstructorRules)。

:::

#### 多重查找

如果你需要使用固定的条件同时查找一组 `KClass`，那么你只需要使用 `all` 或 `waitAll` 方法来得到结果。

```kotlin
// 同步查找，使用 all 得到条件全部查找到的结果
listOf<KClass<*>>(/* 多个 KClass */).findClass {
    // ...
}.all().forEach { clazz ->
    // 得到每个结果
}
// 同步查找，使用 all { ... } 遍历每个结果
listOf<KClass<*>>(/* 多个 KClass */).findClass {
    // ...
}.all { clazz ->
    // 得到每个结果
}
```

## KCallable 拓展

> ~~这里是 **Class** 字节码成员变量 **Field**、**Method**、**Constructor** 相关的扩展功能。~~
> 这里是 **KClass** 字节码成员变量 **KProperty**、**KFunction**、**Constructor KFunction** 相关的扩展功能。

::: tip

**KCallable** 是 **KProperty**(Kotlin 属性/字段)、**KFunction**(Kotlin 函数/方法) 的接口描述对象，它在 Kotlin 反射中为 **KClass** 中可调用内容的总称。

不同于 Java，Kotlin 中并不含与之对应的 **Constructor** ，**Constructor**在 Kotlin 中也是 **KFunction** 表示。

:::

假设有一个这样的 `Class`。

> 示例如下

```java:no-line-numbers
package com.demo;

public class BaseTest {

    public BaseTest() {
        // ...
    }

    public BaseTest(boolean isInit) {
        // ...
    }

    private void doBaseTask(String taskName) {
        // ...
    }
}
```

```java:no-line-numbers
package com.demo;

public class Test extends BaseTest {

    public Test() {
        // ...
    }

    public Test(boolean isInit) {
        // ...
    }

    private static TAG = "Test";

    private BaseTest baseInstance;

    private String a;

    private boolean a;

    private boolean isTaskRunning = false;

    private static void init() {
        // ...
    }

    private void doTask(String taskName) {
        // ...
    }

    private void release(String taskName, Function<boolean, String> task, boolean isFinish) {
        // ...
    }

    private void stop() {
        // ...
    }

    private String getName() {
        // ...
    }

    private void b() {
        // ...
    }

    private void b(String a) {
        // ...
    }
}
```

### 查找与反射调用

假设我们要得到 `Test`(以下统称“当前 `KClass`”)的 `doTask` 方法并执行，通常情况下，我们可以使用标准的反射 API 去查找这个方法。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用反射 API 调用并执行
Test::class.declaredFunctions
    .find { it.name == "doTask" && it.parameters.first().type.jvmErasure == String::class }!!
    .apply { isAccessible = true }
    .call(instance, "task_name")
```

这种写法大概不是很友好，此时 `KYukiReflection` 就为你提供了一个可在任意地方使用的语法糖。

以上写法换做 `KYukiReflection` 可写作如下形式。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name = "doTask"
    param(StringKClass)
}.get(instance).call("task_name")
```

::: tip

更多功能请参考 [KFunctionFinder](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KFunctionFinder)。

:::

同样地，我们需要得到 `isTaskRunning` 变量也可以写作如下形式。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.property {
    name = "isTaskRunning"
    type = BooleanKClass
}.get(instance).any() // any 为 Field 的任意类型实例化对象
```

::: tip

更多功能请参考 [KPropertyFinder](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KPropertyFinder)。

:::

也许你还想得到当前 `KClass` 的构造方法，同样可以实现。

> 示例如下

```kotlin
Test::class.constructor {
    param(BooleanKClass)
}.get().call(true) // 可创建一个新的实例
```

若想得到的是 `KClass` 的无参构造方法，可写作如下形式。

> 示例如下

```kotlin
Test::class.constructor().get().call() // 可创建一个新的实例
```

::: tip

更多功能请参考 [KConstructorFinder](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KConstructorFinder)。

:::

### 可选的查找条件

假设我们要得到 `KClass` 中的 `getName` 方法，可以使用如下实现。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name = "getName"
    emptyParam()
    returnType = StringKClass
}.get(instance).string() // 得到方法的结果
```

通过观察发现，这个 `KClass` 中只有一个名为 `getName` 的方法，那我们可不可以再简单一点呢？

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name = "getName"
    emptyParam()
}.get(instance).string() // 得到方法的结果
```

是的，对于确切不会变化的方法，你可以精简查找条件。

在只使用 `get` 或 `wait` 方法得到结果时 `KYukiReflection` **会默认按照字节码顺序匹配第一个查找到的结果**。

问题又来了，这个 `KClass` 中有一个 `release` 方法，但是它的方法参数很长，而且部分类型可能无法直接得到。

通常情况下我们会使用 `param(...)` 来查找这个方法，但是有没有更简单的方法呢。

此时，在确定方法唯一性后，你可以使用 `paramCount` 来查找到这个方法。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name = "release"
    // 此时我们不必确定方法参数具体类型，写个数就好
    paramCount = 3
}.get(instance) // 得到这个方法
```

上述示例虽然能够匹配成功，但是不精确，此时你还可以使用 `VagueKotlin` 来填充你不想填写的方法参数类型。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name = "release"
    // 使用 VagueKotlin 来填充不想填写的类型，同时保证其它类型能够匹配
    param(StringKClass, VagueKotlin, BooleanKClass)
}.get(instance) // 得到这个方法
```

如果你并不确定每一个参数的类型，你可以通过 `param { ... }` 方法来创建一个条件方法体。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name = "release"
    // 得到 it (KParameter) 方法参数类型数组实例来仅判断已知的类型和它的位置
    param { it[0].kotlin == StringKClass && it[2].kotlin == BooleanKClass }
}.get(instance) // 得到这个方法
```

::: tip

使用 **param { ... }** 创建一个条件方法体，其中的变量 **it** 即当前方法参数的 **KParameter** 类型数组实例，此时你就可以自由使用 **KParameter** 中的所有对象及其方法。

方法体末尾条件需要返回一个 **Boolean**，即最终的条件判断结果。

更多功能请参考 [KPropertyFinder.type](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KPropertyFinder#type-method-1)、[KFunctionFinder.param](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KFunctionFinder#param-method-1)、[KFunctionFinder.returnType](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KFunctionFinder#returntype-method-1)、[KConstructorFinder.param](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KFunctionFinder#param-method-1) 方法。

:::

### 在父类查找

你会注意到 `Test` 继承于 `BaseTest`，现在我们想得到 `BaseTest` 的 `doBaseTask` 方法，在不知道父类名称的情况下，要怎么做呢？

参照上面的查找条件，我们只需要在查找条件中加入一个 `superClass` 即可实现这个功能。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name = "doBaseTask"
    param(StringKClass)
    // 只需要添加这个条件
    superClass()
}.get(instance).call("task_name")
```

这个时候我们就可以在父类中取到这个方法了。

`superClass` 有一个参数为 `isOnlySuperClass`，设置为 `true` 后，可以跳过当前 `KClass` 仅查找当前 `KClass` 的父类。

由于我们现在已知 `doBaseTask` 方法只存在于父类，可以加上这个条件节省查找时间。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name = "doBaseTask"
    param(StringKClass)
    // 加入一个查找条件
    superClass(isOnlySuperClass = true)
}.get(instance).call("task_name")
```

这个时候我们同样可以得到父类中的这个方法。

`superClass` 一旦设置就会自动循环向后查找全部继承的父类中是否有这个方法，直到查找到目标没有父类(继承关系为 `java.lang.Object / kotlin.Any`)为止。

::: tip

更多功能请参考 [KFunctionFinder.superClass](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KFunctionFinder#superclass-method)、[KConstructorFinder.superClass](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KConstructorFinder#superclass-method)、[KPropertyFinder.superClass](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KPropertyFinder#superclass-method) 方法。

:::

::: danger

当前查找的 **KFunction** 除非指定 **superClass** 条件，否则只能查找到当前 **KClass** 的 **KFunction**，这是 KYukiHookAPI 的默认行为。

:::

### 模糊查找

如果我们想查找一个方法名称，但是又不确定它在每个版本中是否发生变化，此时我们就可以使用模糊查找功能。

假设我们要得到 `Class` 中的 `doTask` 方法，可以使用如下实现。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name {
        // 设置名称不区分大小写
        it.equals("dotask", isIgnoreCase = true)
    }
    param(StringKClass)
}.get(instance).call("task_name")
```

已知当前 `KClass` 中仅有一个 `doTask` 方法，我们还可以判断方法名称仅包含其中指定的字符。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name {
        // 仅包含 oTas
        it.contains("oTas")
    }
    param(StringKClass)
}.get(instance).call("task_name")
```

我们还可以根据首尾字符串进行判断。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name {
        // 开头包含 do，结尾包含 Task
        it.startsWith("do") && it.endsWith("Task")
    }
    param(StringKClass)
}.get(instance).call("task_name")
```

通过观察发现这个方法名称中只包含字母，我们还可以再增加一个精确的查找条件。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name {
        // 开头包含 do，结尾包含 Task，仅包含字母
        it.startsWith("do") && it.endsWith("Task") && it.isOnlyLetters()
    }
    param(StringKClass)
}.get(instance).call("task_name")
```

::: tip

使用 **name { ... }** 创建一个条件方法体，其中的变量 **it** 即当前名称的字符串，此时你就可以在 **KNameRules** 的扩展方法中自由使用其中的功能。

方法体末尾条件需要返回一个 **Boolean**，即最终的条件判断结果。

更多功能请参考 [KPropertyFinder.name](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KPropertyFinder#name-method-1)、[KFunctionFinder.name](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KFunctionFinder#name-method-1) 方法以及 [KNameRules](../api/public/io/github/dreammooncai/yukiReflection/finder/base/rules/KNameRules)。

:::

### 多重查找

有些时候，我们可能需要查找一个 `KClass` 中具有相同特征的一组方法、构造方法、变量，此时，我们就可以利用相对条件匹配来完成。

在查找条件结果的基础上，我们只需要把 `get` 换为 `all` 即可得到匹配条件的全部字节码。

假设这次我们要得到 `KClass` 中方法参数个数范围在 `1..3` 的全部方法，可以使用如下实现。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    paramCount(1..3)
}.all(instance).forEach { instance ->
    // 调用执行每个方法
    instance.call(...)
}
```

上述示例可完美匹配到如下 3 个方法。

`private void doTask(String taskName)`

`private void release(String taskName, Function<boolean, String> task, boolean isFinish)`

`private void b(String a)`

如果你想更加自由地定义参数个数范围的条件，可以使用如下实现。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    paramCount { it < 3 }
}.all(instance).forEach { instance ->
    // 调用执行每个方法
    instance.call(...)
}
```

上述示例可完美匹配到如下 6 个方法。

`private static void init()`

`private void doTask(String taskName)`

`private void stop(String a)`

`private void getName(String a)`

`private void b()`

`private void b(String a)`

通过观察 `KClass` 中有两个名称为 `b` 的方法，可以使用如下实现。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name = "b"
}.all(instance).forEach { instance ->
    // 调用执行每个方法
    instance.call(...)
}
```

上述示例可完美匹配到如下 2 个方法。

`private void b()`

`private void b(String a)`

::: tip

使用 **paramCount { ... }** 创建一个条件方法体，其中的变量 **it** 即当前参数个数的整数，此时你就可以在 **KCountRules** 的扩展方法中自由使用其中的功能。

方法体末尾条件需要返回一个 **Boolean**，即最终的条件判断结果。

更多功能请参考 [KFunctionFinder.paramCount](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KFunctionFinder#paramcount-method-2)、[KConstructorFinder.paramCount](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KConstructorFinder#paramcount-method-2) 方法以及 [KCountRules](../api/public/io/github/dreammooncai/yukiReflection/finder/base/rules/KCountRules)。

:::

### 静态字节码

有些方法和变量在 `Class` 中是静态的实现，这个时候，我们不需要传入实例就可以调用它们。

假设我们这次要得到静态变量 `TAG` 的内容。

> 示例如下

```kotlin
Test::class.property {
    name = "TAG"
    type = StringKClass
}.get().string() // Field 的类型是字符串，可直接进行 cast
```

假设 `Class` 中存在同名的非静态 `TAG` 变量，这个时候怎么办呢？

加入一个筛选条件即可。

> 示例如下

```kotlin
Test::class.property {
    name = "TAG"
    type = StringKClass
    // 标识查找的这个变量需要是静态
    modifiers { isStatic }
}.get().string() // Field 的类型是字符串，可直接进行 cast
```

我们还可以调用名为 `init` 的静态方法。

> 示例如下

```kotlin
Test::class.function {
    name = "init"
    emptyParam()
}.get().call()
```

同样地，你可以标识它是一个静态。

> 示例如下

```kotlin
Test::class.function {
    name = "init"
    emptyParam()
    // 标识查找的这个方法需要是静态
    modifiers { isStatic }
}.get().call()
```

::: tip

使用 **modifiers { ... }** 创建一个条件方法体，此时你就可以在 **ModifierRules** 中自由使用其中的功能。

方法体末尾条件需要返回一个 **Boolean**，即最终的条件判断结果。

更多功能请参考 [KPropertyFinder.modifiers](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KPropertyFinder#modifiers-method)、[KFunctionFinder.modifiers](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KFunctionFinder#modifiers-method)、[KConstructorFinder.modifiers](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KConstructorFinder#modifiers-method) 方法以及 [KModifierRules](../api/public/io/github/dreammooncai/yukiReflection/finder/base/rules/KModifierRules)。

:::

### 混淆的字节码

你可能已经注意到了，这里给出的示例 `KClass` 中有两个混淆的变量名称，它们都是 `a`，这个时候我们要怎么得到它们呢？

有两种方案。

第一种方案，确定变量的名称和类型。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.property {
    name = "a"
    type = BooleanKClass
}.get(instance).any() // 得到名称为 a 类型为 Boolean 的变量
```

第二种方案，确定变量的类型所在的位置。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.property {
    type(BooleanKClass).index().first()
}.get(instance).any() // 得到第一个类型为 Boolean 的变量
```

以上两种情况均可得到对应的变量 `private boolean a`。

同样地，这个 `KClass` 中也有两个混淆的方法名称，它们都是 `b`。

你也可以有两种方案来得到它们。

第一种方案，确定方法的名称和方法参数。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name = "b"
    param(StringKClass)
}.get(instance).call("test_string") // 得到名称为 b 方法参数为 [String] 的方法
```

第二种方案，确定方法的参数所在的位置。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    param(StringKClass).index().first()
}.get(instance).call("test_string") // 得到第一个方法参数为 [String] 的方法
```

由于观察到这个方法在 `KClass` 的最后一个，那我们还有一个备选方案。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    order().index().last()
}.get(instance).call("test_string") // 得到当前 Class 的最后一个方法
```

::: warning

请尽量避免使用 **order** 来筛选字节码的下标，它们可能是不确定的，除非你确定它在这个 **KClass** 中的位置一定不会变。

:::

### 直接调用

上面介绍的调用字节码的方法都需要使用 `get(instance)` 才能调用对应的方法，有没有简单一点的办法呢？

此时，你可以在任意实例上使用 `current` 方法来创建一个调用空间。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 假设这个 Class 是不能被直接得到的
instance.currentKotlin {
    // 执行 doTask 方法
    function {
        name = "doTask"
        param(StringKClass)
    }.call("task_name")
    // 执行 stop 方法
    function {
        name = "stop"
        emptyParam()
    }.call()
    // 得到 name
    val name = function { name = "getName" }.string()
}
```

我们还可以用 `superClass` 调用当前 `KClass` 父类的方法。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 假设这个 KClass 是不能被直接得到的
instance.currentKotlin {
    // 执行父类的 doBaseTask 方法
    superClass().function {
        name = "doBaseTask"
        param(StringKClass)
    }.call("task_name")
}
```

如果你不喜欢使用一个大括号的调用域来创建当前实例的命名空间，你可以直接使用 `current()` 方法。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例，这个 KClass 是不能被直接得到的
val instance = Test()
// 执行 doTask 方法
instance
    .currentKotlin()
    .function {
        name = "doTask"
        param(StringKClass)
    }.call("task_name")
// 执行 stop 方法
instance
    .currentKotlin()
    .function {
        name = "stop"
        emptyParam()
    }.call()
// 得到 name
val name = instance.currentKotlin().function { name = "getName" }.string()
```

同样地，它们之间可以连续调用，但<u>**不允许内联调用**</u>。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 假设这个 KClass 是不能被直接得到的
instance.currentKotlin {
    function {
        name = "doTask"
        param(StringKClass)
    }.call("task_name")
}.currentKotlin()
    .function {
        name = "stop"
        emptyParam()
    }.call()
// 注意，因为 currentKotlin() 返回的是 CurrentClass 自身对象，所以不能像下面这样调用
instance.currentKotlin().currentKotlin()
```

针对 `KProperty` 实例，还有一个便捷的方法，可以直接获取 `KProperty` 所在实例的对象。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 假设这个 KClass 是不能被直接得到的
instance.currentKotlin {
    // <方案1>
    property {
        name = "baseInstance"
    }.currentKotlin {
        function {
            name = "doBaseTask"
            param(StringKClass)
        }.call("task_name")
    }
    // <方案2>
    property {
        name = "baseInstance"
    }.currentKotlin()
        ?.function {
            name = "doBaseTask"
            param(StringKClass)
        }?.call("task_name")
}
```

::: warning

上述 **currentKotlin** 方法相当于帮你调用了 **KCurrentClass** 中的 **property { ... }.any()?.currentKotlin()** 方法。

若不存在 **KCurrentClass** 调用域，你需要使用 **property { ... }.get(instance).currentKotlin()** 来进行调用。

:::

问题又来了，我想使用反射的方式创建如下的实例并调用其中的方法，该怎么做呢？

> 示例如下

```kotlin
Test(true).doTask("task_name")
```

通常情况下，我们可以使用标准的反射 API 来调用。

> 示例如下

```kotlin
"com.demo.Test".toKClass()
    .constructors
    .find { it.parameters.first().type.jvmErasure == Boolean::class }!!
    .apply { isAccessible = true }
    .call(true).apply {
        this::class.declaredFunctions.find {
            name == "doTask" && it.parameters.first().type.jvmErasure == String::class
        }!!.apply { isAccessible = true }.call(this,"task_name")
    }
```

但是感觉这种做法好麻烦，有没有更简洁的调用方法呢？

这个时候，我们还可以借助 `buildOf` 方法来创建一个实例。

> 示例如下

```kotlin
"com.demo.Test".toKClass().buildOf(true) { param(BooleanKClass) }?.currentKotlin {
    function {
        name = "doTask"
        param(StringKClass)
    }.call("task_name")
}
```

若你希望 `buildOf` 方法返回当前实例的类型，你可以在其中加入类型泛型声明，而无需使用 `as` 来 `cast` 目标类型。

这种情况多用于实例本身的构造方法是私有的，但是里面的方法是公有的，这样我们只需要对其构造方法进行反射创建即可。

> 示例如下

```kotlin
// 假设这个 Class 是能够直接被得到的
val test = Test::class.buildOf<Test>(true) { param(BooleanKClass) }
test.doTask("task_name")
```

::: tip

更多功能请参考 [KCurrentClass](../api/public/io/github/dreammooncai/yukiReflection/bean/KCurrentClass) 以及 [KClass.buildOf](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kclass-buildof-ext-method) 方法。

:::

### 再次查找

假设有三个不同版本的 `KClass`，它们都是这个 APP 不同版本相同的 `KClass`。

这里面同样都有一个方法 `doTask`，假设它们的功能是一样的。

> 版本 A 示例如下

```java:no-line-numbers
public class Test {

    public void doTask() {
        // ...
    }
}
```

> 版本 B 示例如下

```java:no-line-numbers
public class Test {

    public void doTask(String taskName) {
        // ...
    }
}
```

> 版本 C 示例如下

```java:no-line-numbers
public class Test {

    public void doTask(String taskName, int type) {
        // ...
    }
}
```

我们需要在不同的版本中得到这个相同功能的 `doTask` 方法，要怎么做呢？

此时，你可以使用 `RemedyPlan` 完成你的需求。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name = "doTask"
    emptyParam()
}.remedys {
    function {
        name = "doTask"
        param(StringKClass)
    }.onFind {
        // 可在这里实现找到的逻辑
    }
    function {
        name = "doTask"
        param(StringKClass, IntKClass)
    }.onFind {
        // 可在这里实现找到的逻辑
    }
}.wait(instance) {
    // 得到方法的结果
}
```

::: danger

使用了 **RemedyPlan** 的方法查找结果不能再使用 **get** 的方式得到方法实例，应当使用 **wait** 方法。

:::

另外，你还可以在使用 [多重查找](#多重查找) 的情况下继续使用 `RemedyPlan`。

> 示例如下

```kotlin
// 假设这就是这个 KClass 的实例
val instance = Test()
// 使用 KYukiReflection 调用并执行
Test::class.function {
    name = "doTask"
    emptyParam()
}.remedys {
    function {
        name = "doTask"
        paramCount(0..1)
    }.onFind {
        // 可在这里实现找到的逻辑
    }
    function {
        name = "doTask"
        paramCount(1..2)
    }.onFind {
        // 可在这里实现找到的逻辑
    }
}.waitAll(instance) {
    // 得到方法的结果
}
```

::: tip

更多功能请参考 [KFunctionFinder.RemedyPlan](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KFunctionFinder#remedyplan-class)、[KConstructorFinder.RemedyPlan](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KConstructorFinder#remedyplan-class)、[KPropertyFinder.RemedyPlan](../api/public/io/github/dreammooncai/yukiReflection/finder/callable/KPropertyFinder#remedyplan-class)。

:::

### 相对匹配

假设当前 APP 中不同版本中存在功能相同的 `KClass` 但仅有 `KClass` 的名称不一样。

> 版本 A 示例如下

```java:no-line-numbers
public class ATest {

    public static void doTask() {
        // ...
    }
}
```

> 版本 B 示例如下

```java:no-line-numbers
public class BTest {

    public static void doTask() {
        // ...
    }
}
```

这个时候我们想在每个版本都调用这个 `KClass` 里的 `doTask` 方法该怎么做呢？

通常做法是判断 `KClass` 是否存在。

> 示例如下

```kotlin
// 首先查找到这个 KClass
val currentClass = 
    if("com.demo.ATest".hasKClass()) "com.demo.ATest".toKClass() else "com.demo.BTest".toKClass()
// 然后再查找这个方法并调用
currentClass.function {
    name = "doTask"
    emptyParam()
}.get().call()
```

感觉这种方案非常的不优雅且繁琐，那么此时 `KYukiReflection` 就为你提供了一个非常方便的 `KVariousClass` 专门来解决这个问题。

现在，你可以直接使用以下方式获取到这个 `KClass`。

> 示例如下

```kotlin
KVariousClass("com.demo.ATest", "com.demo.BTest").get().function {
    name = "doTask"
    emptyParam()
}.get().call()
```

若当前 `KClass` 在指定的 `ClassLoader` 中存在，你可以在 `get` 中填入你的 `ClassLoader`。

> 示例如下

```kotlin
val customClassLoader: ClassLoader? = ... // 假设这个就是你的 ClassLoader
KVariousClass("com.demo.ATest", "com.demo.BTest").get(customClassLoader).function {
    name = "doTask"
    emptyParam()
}.get().call()
```

若你不确定所有的 `KClass` 一定会被匹配到，你可以使用 `getOrNull` 方法。

> 示例如下

```kotlin
val customClassLoader: ClassLoader? = ... // 假设这个就是你的 ClassLoader
KVariousClass("com.demo.ATest", "com.demo.BTest").getOrNull(customClassLoader)?.function {
    name = "doTask"
    emptyParam()
}?.get()?.call()
```

::: tip

更多功能请参考 [KVariousClass](../api/public/io/github/dreammooncai/yukiReflection/bean/KVariousClass)。

:::

### 调用泛型

在反射过程中，我们可能会遇到泛型问题，在泛型的反射处理上，`KYukiReflection` 同样提供了一个可在任意地方使用的语法糖。

例如我们有如下的泛型类。

> 示例如下

```kotlin
class TestGeneric<T, R> (t: T, r: R) {

    fun foo() {
        // ...
    }
}
```

当我们想在当前 `KClass` 中获得泛型 `T` 或 `R` 的 `KClass` 实例，只需要如下实现。

> 示例如下

```kotlin
class TestGeneric<T, R> (t: T, r: R) {

    fun foo() {
        // 获得当前实例上继承父类书写的泛型操作对象
        // 获得 T 的 KClass 实例，在参数第 0 位，默认值可以不写
        val tClass = currentKotlin().genericSuper()?.argument()
        // 获得 R 的 KClass 实例，在参数第 1 位
        val rClass = currentKotlin().genericSuper()?.argument(index = 1)
        // 你还可以使用如下写法
        currentKotlin().genericSuper {
             // 获得 T 的 KClass 实例，在参数第 0 位，默认值可以不写
            val tClass = argument()
            // 获得 R 的 KClass 实例，在参数第 1 位
            val rClass = argument(index = 1)
        }
    }
}
```

当我们想在外部调用这个 `KClass` 时，就可以有如下实现。

> 示例如下

```kotlin
// 假设这个就是 T 的 KClass
class TI {

    fun foo() {
        // ...
    }
}
// 假设这个就是 KT 的实例
val tInstance: TI? = ...
// 获得 KT 的 KClass 实例，在参数第 0 位，默认值可以不写，并获得其中的方法 foo 并调用
TestGeneric::class.java.genericSuper()?.argument()?.function {
    name = "foo"
    emptyParam()
}?.get(tInstance)?.invoke<TI>()
```

::: tip

更多功能请参考 [KCurrentClass.generic](../api/public/io/github/dreammooncai/yukiReflection/bean/KCurrentClass#generic-method)、[KClass.genericSuper](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kclass-genericSuper-ext-method) 方法以及 [KGenericClass](../api/public/io/github/dreammooncai/yukiReflection/bean/KGenericClass)。

:::

### 注意误区

> 这里列举了使用时可能会遇到的误区部分，可供参考。

#### 限制性查找条件

在查找条件中，除了 `order` 你<u>**只能**</u>使用一次 `index` 功能。

> 示例如下

```kotlin
function {
    name = "test"
    param(BooleanKClass).index(num = 2)
    // 错误的使用方法，请仅保留一个 index 方法
    returnType(StringKClass).index(num = 1)
}
```

以下查找条件的使用是没有任何问题的。

> 示例如下

```kotlin
function {
    name = "test"
    param(BooleanKClass).index(num = 2)
    order().index(num = 1)
}
```

#### 必要的查找条件

在普通方法查找条件中，<u>**即使是无参的方法也需要设置查找条件**</u>。

假设我们有如下的 `KClass`。

> 示例如下

```java:no-line-numbers
public class TestFoo {

    public void foo(String string) {
        // ...
    }

    public void foo() {
        // ...
    }
}
```

我们要得到其中的 `public void foo()` 方法，可以写作如下形式。

> 示例如下

```kotlin
TestFoo::class.function {
    name = "foo"
}
```

但是，上面的例子<u>**是错误的**</u>。

你会发现这个 `KClass` 中有两个 `foo` 方法，其中一个带有方法参数。

由于上述例子没有设置 `param` 的查找条件，得到的结果将会是匹配名称且匹配字节码顺序的第一个方法 `public void foo(String string)`，而不是我们需要的最后一个方法。

这是一个**经常会出现的错误**，**没有方法参数就会丢失方法参数查找条件**的使用问题。

正确的使用方法如下。

> 示例如下

```kotlin
TestFoo::class.function {
    name = "foo"
    // ✅ 正确的使用方法，添加详细的筛选条件
    emptyParam()
}
```

至此，上述的示例将可以完美地匹配到 `public void foo()` 方法。

::: tip 兼容性说明

在过往历史版本的 API 中是允许匹配不写默认匹配无参方法的做法的，但是最新版本更正了这一问题，请确保你使用的是最新的 API 版本。

:::

在构造方法查找条件中，<u>**即使是无参的构造方法也需要设置查找条件**</u>。

假设我们有如下的 `KClass`。

> 示例如下

```java:no-line-numbers
public class TestFoo {

    public TestFoo() {
        // ...
    }
}
```

我们要得到其中的 `public TestFoo()` 构造方法，必须写作如下形式。

> 示例如下

```kotlin
TestFoo::class.constructor { emptyParam() }
```

上面的例子可以成功获取到 `public TestFoo()` 构造方法。

如果你写作 `constructor()` 而丢失了 `emptyParam()`，此时查找到的结果会是按照字节码顺序排列的的第一位，<u>**可能并不是无参的**</u>。

#### 不设置查找条件

在不设置查找条件的情况下，使用 `property()`、`constructor()`、`function()` 将返回当前 `KClass` 下的所有成员对象。

使用 `get(...)` 或 `give()` 的方式获取将只能得到按照字节码顺序排列的的第一位。

> 示例如下

```kotlin
Test::class.property().get(...)
Test::class.function().give()
```

如果你想得到全部成员对象，你可以使用 `all(...)` 或 `giveAll()`

> 示例如下

```kotlin
Test::class.property().all(...)
Test::class.function().giveAll()
```

#### 字节码类型

在字节码调用结果中，**cast** 方法<u>**只能**</u>指定字节码对应的类型。

例如我们想得到一个 `Boolean` 类型的变量，把他转换为 `String`。

以下是错误的使用方法。

> 示例如下

```kotlin
property {
    name = "test"
    type = BooleanKClass
}.get().string() // 错误的使用方法，必须 cast 为字节码目标类型
```

以下是正确的使用方法。

> 示例如下

```kotlin
property {
    name = "test"
    type = BooleanKClass
}.get().boolean().toString() // ✅ 正确的使用方法，得到类型后再进行转换
```

## 常用类型扩展

在查找方法、变量的时候我们通常需要指定所查找的类型。

> 示例如下

```kotlin
property {
    name = "test"
    type = Boolean::class.javaPrimitiveType?.kotlin
}
```

在 Kotlin 中表达出 `Boolean::class.javaPrimitiveType?.kotlin` 这个类型的写法很长，感觉并不方便，而且并没有意义。

在 Kotlin 中不区分封装类型和基本类型，`Boolean::class == Boolean::class.javaPrimitiveType?.kotlin` 这是等价的

除此区别之外，`KYukiReflection` 为开发者封装了常见的类型调用，其中包含了 Android 的相关类型和 Java 的常见类型与**原始类型关键字**。

这个时候上面的类型就可以写作如下形式了。

> 示例如下

```kotlin
field {
    name = "test"
    type = BooleanKClass
}
```

在 Java 常见类型中的**原始类型 (或基本类型) 关键字**都已被封装为 **类型 + KType** 的方式，例如 `IntKClass`、`FloatKType` (它们的字节码类型为 `int`、`float`)。

这可能在极端场景下使用，`BooleanKClass == BooleanKType` 但 `BooleanKClass.java != BooleanKType.java` 他们的 Java 组成不同，按需使用

相应地，数组类型也有方便的使用方法，假设我们要获得 `String[]` 类型的数组。

可以写做 `Array<String>::class` 才能得到这个类型。

如果你觉得不保险，这个时候我们可以使用方法 `ArrayKClass(StringKClass)` 来得到这个类型。

同时由于 `String` 是常见类型，所以还可以直接使用 `StringArrayKClass` 来得到这个类型。

一些常见需求中查找的方法，都有其对应的封装类型以供使用，格式为 **类型 + KClass**。

以下是 Java 中一些特例类型在 `YukiReflection` 中的封装名称。

- `void` → `UnitKType`

- `java.lang.Void` → `UnitKClass`

- `java.lang.Object` → `AnyKClass`

- `java.lang.Integer` → `IntKClass`

- `java.lang.Character` → `CharKClass`

::: warning

以 **类型 + KType** 封装类型会且仅会表示为 Java **原始类型关键字**，由于 Kotlin 中不存在**原始类型**这个概念，所以它们都会被定义为 **KClass**。

Java 中共有 9 个**原始类型关键字**，其中 8 个为**原始类型**，分别为 **boolean**、**char**、**byte**、**short**、**int**、**float**、**long**、**double**，其中 **void** 类型是一个特例。

同时它们都有 Java 自身对应的封装类型，例如 **java.lang.Boolean**、**java.lang.Integer**，这些类型是<u>**不相等的**</u>，请注意区分。

同样地，数组也有对应的封装类型，它们也需要与 Java **原始类型关键字** 进行区分。

例如 **byte[]** 的封装类型为 **ByteArrayKType** 或 **ArrayClass(ByteType)**，而 **Byte[]** 的封装类型为 **ByteArrayKClass** 或 **ArrayClass(ByteClass)**，这些类型在Kotlin中<u>**是相等的**</u>。

:::

::: tip

更多类型可查看 [KComponentTypeFactory](../api/public/io/github/dreammooncai/yukiReflection/type/android/KComponentTypeFactory)、[KGraphicsTypeFactory](../api/public/io/github/dreammooncai/yukiReflection/type/android/KGraphicsTypeFactory)、[KViewTypeFactory](../api/public/io/github/dreammooncai/yukiReflection/type/android/KViewTypeFactory)、[KVariableTypeFactory](../api/public/io/github/dreammooncai/yukiReflection/type/java/KVariableTypeFactory)。 

:::

同时，欢迎你能贡献更多的常用类型。