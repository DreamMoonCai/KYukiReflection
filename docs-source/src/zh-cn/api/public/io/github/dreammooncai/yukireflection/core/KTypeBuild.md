---
pageClass: code-page
---

# KTypeBuild <span class="symbol">- class</span>

```kotlin:no-line-numbers
class KTypeBuild internal constructor(private val classSet: KClassifier) : KBaseFinder()
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KType` 构建实现类。

## isNullable <span class="symbol">- field</span>

```kotlin:no-line-numbers
var isNullable: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KType` 是否可以为空。

## paramCount <span class="symbol">- field</span>

```kotlin:no-line-numbers
var paramCount: Int
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KType` 泛型参数个数。

你可以不使用 `param` 指定参数类型而是仅使用此变量指定参数个数

若参数个数小于零则忽略并使用 `param`

::: danger

未指定类型的泛型将使用星射代替 `KTypeProjection.STAR`

:::

## emptyParam <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun emptyParam()
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KType` 的泛型为空参数、无参数。

## param <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun param(vararg paramType: Any)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KType` 泛型参数。

如果同时使用了 `paramCount` 则 `paramType` 的数量必须小于或等于 `paramCount` 个数，多余未设置的类型将使用星射代替 `KTypeProjection.STAR`

你同样可以在 `KType` 中使用[VagueKotlin](../../../type/defined/KDefinedTypeFactory#vaguekotlin-field) - 它与`KTypeProjection.STAR`等价

::: danger

无泛型 `KType` 请使用 `emptyParam` 设置没有泛型参数

有泛型 `KType` 未指定的泛型将使用星射代替 `KTypeProjection.STAR`

:::

```kotlin
生成List<Int,String,*>尽管这不合理，因为泛型数量必须与基类能承受的数量一致
```

> 此时就可以简单地写作 ↓

```kotlin
param(Int::class, String::class,VagueKotlin)
```

## addParam <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun addParam(vararg paramType: Any)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 增加 `KType` 泛型参数 - 你需要谨慎使用此方法，因为常常默认泛型组中包含的是所有星射，此方法会同步增加 `paramCount`,你往往不需要去增加泛型参数数量他们应该在源码就固定了,因此你可能需要先 `emptyParam`

如果同时使用了 `paramCount` 则 `paramType` 的数量必须小于或等于 `paramCount` 个数，多余未设置的类型将使用星射代替 `KTypeProjection.STAR`

你同样可以在 `KType` 中使用[VagueKotlin](../../../type/defined/KDefinedTypeFactory#vaguekotlin-field) - 它与`KTypeProjection.STAR`等价

::: danger

无泛型 `KType` 请使用 `emptyParam` 设置没有泛型参数

有泛型 `KType` 未指定的泛型将使用星射代替 `KTypeProjection.STAR`

:::

> 例如下面这个参数类型结构 ↓

```kotlin
生成List<Int,String,*>尽管这不合理，因为泛型数量必须与基类能承受的数量一致
```

> 此时就可以简单地写作 ↓

```kotlin
addParam(Int::class)
addParam(String::class)
addParam(VagueKotlin)
```

## setParam <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun setParam(index: Int,paramType: Any)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KType` 泛型参数

你同样可以在 `KType` 中使用[VagueKotlin](../../../type/defined/KDefinedTypeFactory#vaguekotlin-field) - 它与`KTypeProjection.STAR`等价

::: danger

无泛型 `KType` 请使用 `emptyParam` 设置没有泛型参数

有泛型 `KType` 未指定的泛型将使用星射代替 `KTypeProjection.STAR`

:::

> 例如下面这个参数类型结构 ↓

```kotlin
将List<*>设置为 -> List<String,*,Int> 尽管这不合理，因为泛型数量必须与基类能承受的数量一致
```

> 此时就可以简单地写作 ↓

```kotlin
setParam(2,Int::class)
setParam(0,String::class)
setParam(1,VagueKotlin)
```

## setStarParam <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun setStarParam(paramType: Any)
```

```kotlin:no-line-numbers
fun setStarParam(index: Int,paramType: Any)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KType` 星射泛型参数为指定类型

::: danger

设置从最初元素开始的第`index`个星射`KTypeProjection.STAR`为指定泛型参数，`index`默认为0

:::

你同样可以在 `KType` 中使用[VagueKotlin](../../../type/defined/KDefinedTypeFactory#vaguekotlin-field) - 它与`KTypeProjection.STAR`等价

::: danger

如果没有任何星射泛型则此操作等同于`setParam`

有泛型 `KType` 未指定的泛型将使用星射代替`KTypeProjection.STAR`

:::

> 例如下面这个参数类型结构 ↓

```kotlin
为填补星射参数List<*,String,*> -> List<*,String,Int> 尽管这不合理，因为泛型数量必须与基类能承受的数量一致
```

> 此时就可以简单地写作 ↓

```kotlin
setStarParam(1,Int::class)
```

## setVariance <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun setVariance(index: Int,variance: KVariance)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KType` 泛型参数的方差

你同样可以在 `KType` 中使用[VagueKotlin](../../../type/defined/KDefinedTypeFactory#vaguekotlin-field) - 它与`KVariance.INVARIANT`等价

> 例如下面这个参数类型结构 ↓

```kotlin
为泛型参数设置方差List<Int,in String,Int> -> List<in Int,String,out Int> 尽管这不合理，因为泛型数量必须与基类能承受的数量一致
```

> 此时就可以简单地写作 ↓

```kotlin
setVariance(0,KVariance.IN)
setVariance(1,VagueKotlin)
setVariance(2,KVariance.OUT)
```

## annotations <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun annotations(vararg annotations: Annotation)
```

```kotlin:no-line-numbers
fun addAnnotation(annotation: Annotation)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KType` 注解

> 例如下面这个参数类型结构 ↓

```kotlin
vararg abc:@UnsafeVariance Int
```

> 此时就可以简单地写作 ↓

```kotlin
classSet = Int::class
addAnnotation(UnsafeVariance::class)
```

## Result <span class="symbol">- class</span>

```kotlin:no-line-numbers
inner class Result internal constructor(internal val isNoSuch: Boolean, internal val throwable: Throwable?) : BaseResult
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KType` 构建结果实现类。

### result <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun result(initiate: Result.() -> Unit): Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 创建监听结果事件方法体。

**功能示例**

你可以使用 **lambda** 形式创建 `Result` 类。

### result <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun result(initiate: Result.() -> Unit): Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 创建监听结果事件方法体。

**功能示例**

你可以使用 **lambda** 形式创建 `Result` 类。

> 示例如下

```kotlin
constructor {
    // Your code here.
}.result {
    get().call()
    all()
    remedys {}
    onNoSuchConstructor {}
}
```

### get <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun get(): Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得 `KType` 实例处理类。
