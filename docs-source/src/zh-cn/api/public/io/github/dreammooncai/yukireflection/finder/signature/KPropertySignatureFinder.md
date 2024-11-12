---
pageClass: code-page
---

# KPropertySignatureFinder <span class="symbol">- class</span>

```kotlin:no-line-numbers
open class KPropertySignatureFinder internal constructor(classSet: KClass<*>?,private val loader: ClassLoader?) : KPropertyFinder
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 通过 `KProperty` 签名查找 `KPropertySignatureSupport.member` 类

可通过指定类型查找指定 `KPropertySignatureSupport` 或一组 `KPropertySignatureSupport`。

此查找器拥有 [KPropertyFinder](../callable/KPropertyFinder#kpropertyfinder-class) 的所有条件筛选器

## RemedyPlan <span class="symbol">- class</span>

```kotlin:no-line-numbers
inner class RemedyPlan internal constructor()
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KPropertySignatureSupport` 重查找实现类，可累计失败次数直到查找成功。

### propertySignature <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun propertySignature(initiate: KFunctionSignatureConditions): Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 创建需要重新查找的 `KPropertySignatureSupport`。

你可以添加多个备选 `KPropertySignatureSupport`，直到成功为止，若最后依然失败，将停止查找并输出错误日志。

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
fun onFind(initiate: MutableList<KPropertySignatureSupport>.() -> Unit)
```

**变更记录**

`v1.0.0` `添加`

`initiate` 类型由 `HashSet` 修改为 `MutableList`

**功能描述**

> 当在 `RemedyPlan` 中找到结果时。

**功能示例**

你可以方便地对重查找的 `KPropertySignatureSupport` 实现 `onFind` 方法。

> 示例如下

```kotlin
propertySignature {
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

> `KPropertySignatureSupport` 查找结果实现类。

### getter <span class="symbol">- field</span>

```kotlin:no-line-numbers
val getter: KFunctionSignatureFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取属性的 getter 组成的 `KFunctionSignatureSupport` 查找结果实现类

### setter <span class="symbol">- field</span>

```kotlin:no-line-numbers
val setter: KFunctionSignatureFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取属性的 setter 组成的 `KFunctionSignatureSupport` 查找结果实现类

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
propertySignature {
    // Your code here.
}.result {
    get(instance).set("something")
    get(instance).string()
    get(instance).cast<CustomClass>()
    get().boolean()
    all(instance)
    give()
    giveAll()
    onNoSuchProperty {}
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

**功能示例**

你可以轻松地得到 `KFunctionSignatureSupport.member` 的实例以及使用它进行设置实例。

> 示例如下

```kotlin
propertySignature {
    // Your code here.
}.get(instance).set("something")
```

如果你取到的是静态 `KFunctionSignatureSupport.member`，可以不需要设置实例。

> 示例如下

```kotlin
propertySignature {
    // Your code here.
}.get().set("something")
```

### all <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun all(instance: Any?): MutableList<Instance>
```

**变更记录**

`v1.0.0` `添加`

返回值类型由 `ArrayList` 修改为 `MutableList`

**功能描述**

> 获得 `KFunctionSignatureSupport.member` 实例处理类数组。

返回全部查找条件匹配的多个 `KFunctionSignatureSupport.member` 实例结果。

**功能示例**

你可以通过此方法来获得当前条件结果中匹配的全部 `KFunctionSignatureSupport.member`，其 `KFunctionSignatureSupport.member` 所在实例用法与 `get` 相同。

> 示例如下

```kotlin
propertySignature {
    // Your code here.
}.all(instance).forEach { instance ->
    instance.self
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

若有多个 KFunctionSignatureSupport 结果只会返回第一个。

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

当你遇到一种 `KFunctionSignatureSupport` 可能存在不同形式的存在时，可以使用 `RemedyPlan` 重新查找它，而没有必要使用 `onNoSuchProperty` 捕获异常二次查找 `KFunctionSignatureSupport`。

若第一次查找失败了，你还可以在这里继续添加此方法体直到成功为止。

> 示例如下

```kotlin
propertySignature {
    // Your code here.
}.remedys {
    propertySignature {
        // Your code here.
    }
    propertySignature {
        // Your code here.
    }
}
```

### onNoSuchProperty <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun onNoSuchProperty(result: (Throwable) -> Unit): Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 监听找不到 `KFunctionSignatureSupport` 时。

### ignored <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun ignored(): Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 忽略异常并停止打印任何错误日志。

::: warning

此时若要监听异常结果，你需要手动实现 **onNoSuchProperty** 方法。

:::

### Instance <span class="symbol">- class</span>

```kotlin:no-line-numbers
inner class Instance internal constructor(private val instance: Any?, private val member: Member?,private val setter:Method?): BaseInstance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `Field`、`get/set Method` 实例变量处理类。

#### original <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun original(): Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 标识需要调用当前 `get/set Method` 未经 Hook 的原始 `get/set Method`。

若当前 `get/set Method` 并未 Hook 则会使用原始的 `get/set Method.invoke` 方法调用

::: danger

你只能在 (Xposed) 宿主环境中使用此功能

此方法仅在 Hook Api 下有效

:::

#### current <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun current(ignored: Boolean): KCurrentClass?
```

```kotlin:no-line-numbers
inline fun current(ignored: Boolean, initiate: KCurrentClass.() -> Unit): Any?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得当前 `Field`、`get Method` 自身 `self` 实例的类操作对象 `KCurrentClass`。

#### cast <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun <T> cast(): T?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `Field`、`get Method` 实例。

#### byte <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun byte(): Byte?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `Field`、`get Method` Byte 实例。

#### int <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun int(): Int
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `Field`、`get Method` Int 实例。

#### long <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun long(): Long
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `Field`、`get Method` Long 实例。

#### short <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun short(): Short
```
**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `Field`、`get Method` Short 实例。

#### double <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun double(): Double
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `Field`、`get Method` Double 实例。

#### float <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun float(): Float
```
**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `Field`、`get Method` Float 实例。

#### string <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun string(): String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `Field`、`get Method` String 实例。

#### char <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun char(): Char
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `Field`、`get Method` Char 实例。

#### boolean <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun boolean(): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `Field`、`get Method` Boolean 实例。

#### any <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun any(): Any?
```
**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `Field`、`get Method` Any 实例。

#### array <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun <reified T> array(): Array<T>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `Field`、`get Method` Array 实例。

#### list <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun <reified T> list(): List<T>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `Field`、`get Method` List 实例。

#### set <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun set(any: Any?)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置当前 `Field`、`set Method` 实例。

#### setTrue <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun setTrue()
```
**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置当前 `Field`、`set Method` 实例为 `true`。

::: danger

请确保实例对象类型为 **Boolean**。

:::

#### setFalse <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun setFalse()
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置当前 `Field`、`set Method` 实例为 `false`。

::: danger

请确保实例对象类型为 **Boolean**。

:::

#### setNull <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun setNull()
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置当前 `Field`、`set Method` 实例为 `null`。