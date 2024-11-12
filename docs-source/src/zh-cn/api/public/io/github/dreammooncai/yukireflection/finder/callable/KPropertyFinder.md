---
pageClass: code-page
---

# KPropertyFinder <span class="symbol">- class</span>

```kotlin:no-line-numbers
open class KPropertyFinder internal constructor(final override val classSet: KClass<*>?) : KCallableBaseFinder
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KProperty` 查找类。

可通过指定类型查找指定 `KProperty` 或一组 `KProperty`。

## name <span class="symbol">- field</span>

```kotlin:no-line-numbers
var name: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KProperty` 名称。

::: danger

若不填写名称则必须存在一个其它条件。

:::

## type <span class="symbol">- field</span>

```kotlin:no-line-numbers
var type: Any?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KProperty` 类型。

可不填写类型。

## modifiers <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun modifiers(conditions: KModifierConditions): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KProperty` 标识符筛选条件。

可不设置筛选条件。

::: danger

存在多个 **IndexTypeCondition** 时除了 **order** 只会生效最后一个。

:::

## order <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun order(): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 顺序筛选字节码的下标。

## name <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun name(value: String): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KProperty` 名称。

::: danger

若不填写名称则必须存在一个其它条件。

存在多个 **IndexTypeCondition** 时除了 **order** 只会生效最后一个。

:::

## name <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun name(conditions: KNameConditions): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KProperty` 名称条件。

::: danger

若不填写名称则必须存在一个其它条件。

存在多个 **IndexTypeCondition** 时除了 **order** 只会生效最后一个。

:::

## type <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun type(value: Any): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KProperty` 类型。

可不填写类型。

::: danger

存在多个 **IndexTypeCondition** 时除了 **order** 只会生效最后一个。

:::

## type <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun type(conditions: KTypeConditions): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KProperty` 类型条件。

可不填写类型。

::: danger

存在多个 **IndexTypeCondition** 时除了 **order** 只会生效最后一个。

:::

## superClass <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun superClass(isOnlySuperClass: Boolean)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置在 `classSet` 的所有父类中查找当前 `KProperty`。

::: warning

若当前 **classSet** 的父类较多可能会耗时，API 会自动循环到父类继承是 **Any** 前的最后一个类。

:::

## RemedyPlan <span class="symbol">- class</span>

```kotlin:no-line-numbers
inner class RemedyPlan internal constructor()
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KProperty` 重查找实现类，可累计失败次数直到查找成功。

### property <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun property(initiate: KPropertyConditions): Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 创建需要重新查找的 `KProperty`。

你可以添加多个备选 `KProperty`，直到成功为止，若最后依然失败，将停止查找并输出错误日志。

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
fun onFind(initiate: MutableList<KProperty>.() -> Unit)
```

**变更记录**

`v1.0.0` `添加`

`initiate` 类型由 `HashSet` 修改为 `MutableList`

**功能描述**

> 当在 `RemedyPlan` 中找到结果时。

**功能示例**

你可以方便地对重查找的 `KProperty` 实现 `onFind` 方法。

> 示例如下

```kotlin
property {
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

> `KProperty` 查找结果实现类。

### getter <span class="symbol">- field</span>

```kotlin:no-line-numbers
val getter: KFunctionFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取属性的 getter 组成的 `KFunction` 查找结果实现类

### setter <span class="symbol">- field</span>

```kotlin:no-line-numbers
val setter: KFunctionFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取属性的 setter 组成的 `KFunction` 查找结果实现类

### all <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun all(instance: Any?): MutableList<Instance>
```


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
property {
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
fun get(instance: Any?,extensionRef:Any?,isUseMember:Boolean): Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得 `KProperty` 实例处理类。

若有多个 `KProperty` 结果只会返回第一个。

**功能示例**

你可以轻松地得到 `KProperty` 的实例以及使用它进行设置实例。

> 示例如下

```kotlin
property {
    // Your code here.
}.get(instance).set("something")
```

如果你取到的是静态 `KProperty`，可以不需要设置实例。

> 示例如下

```kotlin
property {
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

> 获得 `KProperty` 实例处理类数组。

返回全部查找条件匹配的多个 `KProperty` 实例结果。

**功能示例**

你可以通过此方法来获得当前条件结果中匹配的全部 `KProperty`，其 `KProperty` 所在实例用法与 `get` 相同。

> 示例如下

```kotlin
property {
    // Your code here.
}.all(instance).forEach { instance ->
    instance.self
}
```

### give <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun give(): KProperty?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到 `KProperty` 本身。

若有多个 KProperty 结果只会返回第一个。

在查找条件找不到任何结果的时候将返回 `null`。

### giveAll <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun giveAll(): MutableList<KProperty>
```

**变更记录**

`v1.0.0` `添加`

返回值类型由 `HashSet` 修改为 `MutableList`

**功能描述**

> 得到 `KProperty` 本身数组。

返回全部查找条件匹配的多个 `KProperty` 实例。

在查找条件找不到任何结果的时候将返回空的 `MutableList`。

### wait <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun wait(instance: Any?, extensionRef:Any?, isUseMember:Boolean, initiate: Instance.() -> Unit)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得 `KProperty` 实例处理类，配合 `RemedyPlan` 使用。

若有多个 `KProperty` 结果只会返回第一个。

::: danger

若你设置了 **remedys** 必须使用此方法才能获得结果。

若你没有设置 **remedys** 此方法将不会被回调。

:::

### waitAll <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun waitAll(instance: Any?, extensionRef:Any?, isUseMember:Boolean, initiate: MutableList<Instance>.() -> Unit)
```

**变更记录**

`v1.0.0` `添加`

`initiate` 类型由 `ArrayList` 修改为 `MutableList`

**功能描述**

> 获得 `KProperty` 实例处理类数组，配合 `RemedyPlan` 使用。

返回全部查找条件匹配的多个 `KProperty` 实例结果。

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

> 创建 `KProperty` 重查找功能。

**功能示例**

当你遇到一种 `KProperty` 可能存在不同形式的存在时，可以使用 `RemedyPlan` 重新查找它，而没有必要使用 `onNoSuchProperty` 捕获异常二次查找 `KProperty`。

若第一次查找失败了，你还可以在这里继续添加此方法体直到成功为止。

> 示例如下

```kotlin
property {
    // Your code here.
}.remedys {
    property {
        // Your code here.
    }
    property {
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

> 监听找不到 `KProperty` 时。

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
inner class Instance internal constructor(private val instance: Any?, private val property: KProperty<*>?): BaseInstance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KProperty` 实例变量处理类。

#### useMember <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun useMember(use:Boolean): Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 是否将构造函数转换为Java方式构造

为true时实例执行将通过将 Kotlin构造函数 转换为 JavaMember 方式执行

如果目标属性无法用Java方式描述则此设置将会自动忽略

#### receiver <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun receiver(extensionRef:Any?): Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 修改 `extensionRef` Receiver

当此属性是拓展属性时，你可能需要此方法额外的一个this属性进行设置

#### original <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun original(): Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 标识需要调用当前 `KProperty` 未经 Hook 的原始 `KProperty`。

若当前 `KProperty` 并未 Hook 则会使用原始的 `KProperty.call` 方法调用

::: danger

此方法在 Hook Api 存在时将固定 `isUseMember` 为 true

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

> 获得当前 `KProperty` 自身 `self` 实例的类操作对象 `KCurrentClass`。

#### cast <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun <T> cast(): T?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `KProperty` 实例。

#### byte <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun byte(): Byte?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `KProperty` Byte 实例。

#### int <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun int(): Int
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `KProperty` Int 实例。

#### long <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun long(): Long
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `KProperty` Long 实例。

#### short <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun short(): Short
```
**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `KProperty` Short 实例。

#### double <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun double(): Double
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `KProperty` Double 实例。

#### float <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun float(): Float
```
**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `KProperty` Float 实例。

#### string <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun string(): String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `KProperty` String 实例。

#### char <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun char(): Char
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `KProperty` Char 实例。

#### boolean <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun boolean(): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `KProperty` Boolean 实例。

#### any <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun any(): Any?
```
**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `KProperty` Any 实例。

#### array <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun <reified T> array(): Array<T>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `KProperty` Array 实例。

#### list <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun <reified T> list(): List<T>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到当前 `KProperty` List 实例。

#### set <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun set(any: Any?)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置当前 `KProperty` 实例。

#### setTrue <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun setTrue()
```
**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置当前 `KProperty` 实例为 `true`。

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

> 设置当前 `KProperty` 实例为 `false`。

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

> 设置当前 `KProperty` 实例为 `null`。
