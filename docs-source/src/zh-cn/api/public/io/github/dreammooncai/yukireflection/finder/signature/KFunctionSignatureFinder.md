---
pageClass: code-page
---

# KFunctionSignatureFinder <span class="symbol">- class</span>

```kotlin:no-line-numbers
open class KFunctionSignatureFinder internal constructor(classSet: KClass<*>?,private val loader: ClassLoader?) : KFunctionFinder
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 通过 `KFunction` 签名查找 `KFunctionSignatureSupport.member` 类

可通过指定类型查找指定 `KFunctionSignatureSupport` 或一组 `KFunctionSignatureSupport`。

此查找器拥有 [KFunctionFinder](../callable/KFunctionFinder#kfunctionfinder-class) 的所有条件筛选器

## RemedyPlan <span class="symbol">- class</span>

```kotlin:no-line-numbers
inner class RemedyPlan internal constructor()
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KFunctionSignatureSupport` 重查找实现类，可累计失败次数直到查找成功。

### functionSignature <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun functionSignature(initiate: KFunctionSignatureConditions): Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 创建需要重新查找的 `KFunctionSignatureSupport`。

你可以添加多个备选 `KFunctionSignatureSupport`，直到成功为止，若最后依然失败，将停止查找并输出错误日志。

### Result <span class="symbol">- class</span>

```kotlin:no-line-numbers
inner class Result internal constructor()
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `RemedyPlan` 结果实现类。

#### onFind <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun onFind(initiate: MutableList<KFunctionSignatureSupport>.() -> Unit)
```

**变更记录**

`v1.0.0` `添加`

`initiate` 类型由 `HashSet` 修改为 `MutableList`

**功能描述**

> 当在 `RemedyPlan` 中找到结果时。

**功能示例**

你可以方便地对重查找的 `KFunctionSignatureSupport` 实现 `onFind` 方法。

> 示例如下

```kotlin
functionSignature {
    // Your code here.
}.onFind {
    // Your code here.
}
```

## Result <span class="symbol">- class</span>

```kotlin:no-line-numbers
inner class Result internal constructor(internal val isNoSuch: Boolean, internal val throwable: Throwable?) : BaseResult
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KFunctionSignatureSupport` 查找结果实现类。

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
functionSignature {
    // Your code here.
}.result {
    get(instance).call()
    all(instance)
    remedys {}
    onNoSuchFunction {}
}
```

### get <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun get(instance: Any?, declaringClass:KClass<*>?, loader: ClassLoader?): Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得 `KFunctionSignatureSupport.member` 实例处理类。

若有多个 `KFunctionSignatureSupport.member` 结果只会返回第一个。

::: danger

若你设置了 **remedys** 请使用 **wait** 回调结果方法。

:::

**功能示例**

你可以通过获得方法所在实例来执行 `KFunctionSignatureSupport.member`。

> 示例如下

```kotlin
functionSignature {
    // Your code here.
}.get(instance).call()
```

若当前为静态方法/单例方法，你可以不设置实例。

> 示例如下

```kotlin
functionSignature {
    // Your code here.
}.get().call()
```

### all <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun all(instance: Any?, declaringClass:KClass<*>?, loader: ClassLoader?): MutableList<Instance>
```

**变更记录**

`v1.0.0` `添加`

返回值类型由 `ArrayList` 修改为 `MutableList`

**功能描述**

> 获得 `KFunctionSignatureSupport.member` 实例处理类数组。

返回全部查找条件匹配的多个 `KFunctionSignatureSupport.member` 实例结果。

**功能示例**

你可以通过此方法来获得当前条件结果中匹配的全部 `KFunctionSignatureSupport.member`，其方法所在实例用法与 `get` 相同。

> 示例如下

```kotlin
functionSignature {
    // Your code here.
}.all(instance).forEach { instance ->
    instance.call(...)
}
```

### give <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun give(): KFunctionSignatureSupport?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到 `KFunctionSignatureSupport` 本身。

若有多个 `KFunctionSignatureSupport` 结果只会返回第一个。

在查找条件找不到任何结果的时候将返回 `null`。

### giveAll <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun giveAll(): MutableList<KFunctionSignatureSupport>
```

**变更记录**

`v1.0.0` `添加`

返回值类型由 `HashSet` 修改为 `MutableList`

**功能描述**

> 得到 `KFunctionSignatureSupport` 本身数组。

返回全部查找条件匹配的多个 `KFunctionSignatureSupport` 实例。

在查找条件找不到任何结果的时候将返回空的 `MutableList`。

### wait <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun wait(instance: Any?, declaringClass:KClass<*>?, loader: ClassLoader?, initiate: Instance.() -> Unit)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得 `KFunctionSignatureSupport.member` 实例处理类，配合 `RemedyPlan` 使用。

若有多个 `KFunctionSignatureSupport.member` 结果只会返回第一个。

::: danger

若你设置了 **remedys** 必须使用此方法才能获得结果。

若你没有设置 **remedys** 此方法将不会被回调。

:::

### waitAll <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun waitAll(instance: Any?, declaringClass:KClass<*>?, loader: ClassLoader?, initiate: MutableList<Instance>.() -> Unit)
```

**变更记录**

`v1.0.0` `添加`

`initiate` 类型由 `ArrayList` 修改为 `MutableList`

**功能描述**

> 获得 `KFunctionSignatureSupport.member` 实例处理类数组，配合 `RemedyPlan` 使用。

返回全部查找条件匹配的多个 `KFunctionSignatureSupport.member` 实例结果。

::: danger

若你设置了 **remedys** 必须使用此方法才能获得结果。

若你没有设置 **remedys** 此方法将不会被回调。

:::

### remedys <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun remedys(initiate: RemedyPlan.() -> Unit): Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 创建 `KFunctionSignatureSupport` 重查找功能。

**功能示例**

当你遇到一种 `KFunctionSignatureSupport` 可能存在不同形式的存在时，可以使用 `RemedyPlan` 重新查找它，而没有必要使用 `onNoSuchFunction` 捕获异常二次查找 `KFunctionSignatureSupport`。

若第一次查找失败了，你还可以在这里继续添加此方法体直到成功为止。

> 示例如下

```kotlin
functionSignature {
    // Your code here.
}.remedys {
    functionSignature {
        // Your code here.
    }
    functionSignature {
        // Your code here.
    }
}
```

### onNoSuchFunction <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun onNoSuchFunction(result: (Throwable) -> Unit): Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 监听找不到 `KFunctionSignatureSupport` 时。

只会返回第一次的错误信息，不会返回 `RemedyPlan` 的错误信息。

### ignored <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun ignored(): Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 忽略异常并停止打印任何错误日志。

::: warning

此时若要监听异常结果，你需要手动实现 **onNoSuchFunction** 方法。

:::

### Instance <span class="symbol">- class</span>

```kotlin:no-line-numbers
inner class Instance internal constructor(private val instance: Any?, private val function: Method?):BaseInstance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `Method` 实例处理类。

#### original <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun original(): Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 标识需要调用当前 `Method` 未经 Hook 的原始 `Method`。

若当前 `Method` 并未 Hook 则会使用原始的 `Method.invoke` 方法调用

::: danger

你只能在 (Xposed) 宿主环境中使用此功能

此方法仅在 Hook Api 下有效

:::

#### call <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun call(vararg args: Any?): Any?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `Method`，不指定返回值类型。

#### invoke <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun <T> invoke(vararg args: Any?): T?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `Method`，指定 `T` 返回值类型。

#### byte <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun byte(vararg args: Any?): Byte?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `Method`，指定 Byte 返回值类型。

#### int <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun int(vararg args: Any?): Int
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `Method`，指定 Int 返回值类型。

#### long <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun long(vararg args: Any?): Long
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `Method`，指定 Long 返回值类型。

#### short <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun short(vararg args: Any?): Short
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `Method`，指定 Short 返回值类型。

#### double <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun double(vararg args: Any?): Double
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `Method`，指定 Double 返回值类型。

#### float <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun float(vararg args: Any?): Float
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `Method`，指定 Float 返回值类型。

#### string <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun string(vararg args: Any?): String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `Method`，指定 String 返回值类型。

#### char <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun char(vararg args: Any?): Char
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `Method`，指定 Char 返回值类型。

#### boolean <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun boolean(vararg args: Any?): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `Method`，指定 Boolean 返回值类型。

### array <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun <reified T> array(vararg args: Any?): Array<T>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `Method`，指定 Array 返回值类型。

### list <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun <reified T> list(vararg args: Any?): List<T>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `Method`，指定 List 返回值类型。