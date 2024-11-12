---
pageClass: code-page
---

# KFunctionFinder <span class="symbol">- class</span>

```kotlin:no-line-numbers
open class KFunctionFinder internal constructor(final override val classSet: KClass<*>?) : KCallableBaseFinder
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KFunction` 查找类。

可通过指定类型查找指定 `KFunction` 或一组 `KFunction`。

## name <span class="symbol">- field</span>

```kotlin:no-line-numbers
var name: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 名称。

::: danger

若不填写名称则必须存在一个其它条件。

:::

## paramCount <span class="symbol">- field</span>

```kotlin:no-line-numbers
var paramCount: Int
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 参数个数。

你可以不使用 `param` 指定参数类型而是仅使用此变量指定参数个数。

若参数个数小于零则忽略并使用 `param`。

## returnType <span class="symbol">- field</span>

```kotlin:no-line-numbers
var returnType: Any?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 返回值，可不填写返回值。

## modifiers <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun modifiers(conditions: KModifierConditions): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 标识符筛选条件。

可不设置筛选条件。

::: danger

存在多个 **IndexTypeCondition** 时除了 **order** 只会生效最后一个。

:::

## emptyParam <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun emptyParam(): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 空参数、无参数。

## param <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun param(vararg paramType: Any): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 参数。

如果同时使用了 `paramCount` 则 `paramType` 的数量必须与 `paramCount` 完全匹配。

如果 `KFunction` 中存在一些无意义又很长的类型，你可以使用 [VagueKotlin](../../../type/defined/KDefinedTypeFactory#vaguekotlin-field) 来替代它。

::: danger

无参 **KFunction** 请使用 **emptyParam** 设置查找条件。

有参 **KFunction** 必须使用此方法设定参数或使用 **paramCount** 指定个数。

存在多个 **IndexTypeCondition** 时除了 **order** 只会生效最后一个。

:::

## param <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun param(conditions: KParameterConditions): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `Method` 参数条件。

::: danger

无参 **KFunction** 请使用 **emptyParam** 设置查找条件。

有参 **KFunction** 必须使用此方法设定参数或使用 **paramCount** 指定个数。

存在多个 **IndexTypeCondition** 时除了 **order** 只会生效最后一个。

:::

## paramName <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun paramName(vararg paramName: String): Unit
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 参数名称。

如果 `KFunction` 中存在一些不太清楚的参数名称，你可以使用 [VagueKotlin](../../../type/defined/KDefinedTypeFactory#vaguekotlin-field).name 或者 空字符串 或者 "null" 来替代它。

## paramName <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun paramName(conditions: KNamesConditions): Unit
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 参数名称条件。

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

> 设置 `KFunction` 名称。

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

> 设置 `KFunction` 名称条件。

::: danger

若不填写名称则必须存在一个其它条件。

存在多个 **IndexTypeCondition** 时除了 **order** 只会生效最后一个。

:::

## paramCount <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun paramCount(num: Int): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 参数个数。

你可以不使用 `param` 指定参数类型而是仅使用此方法指定参数个数。

若参数个数小于零则忽略并使用 `param`。

::: danger

存在多个 **IndexTypeCondition** 时除了 **order** 只会生效最后一个。

:::

## paramCount <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun paramCount(numRange: IntRange): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 参数个数范围。

你可以不使用 `param` 指定参数类型而是仅使用此方法指定参数个数范围。

::: danger

存在多个 **IndexTypeCondition** 时除了 **order** 只会生效最后一个。

:::

## paramCount <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun paramCount(conditions: KCountConditions): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 参数个数条件。

你可以不使用 `param` 指定参数类型而是仅使用此方法指定参数个数条件。

::: danger

存在多个 **IndexTypeCondition** 时除了 **order** 只会生效最后一个。

:::

## returnType <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun returnType(value: Any): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 返回值。

可不填写返回值。

::: danger

存在多个 **IndexTypeCondition** 时除了 **order** 只会生效最后一个。

:::

## returnType <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun returnType(conditions: KTypeConditions): IndexTypeCondition
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 返回值条件。

可不填写返回值。

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

> 设置在 `classSet` 的所有父类中查找当前 `KFunction`。

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

> `KFunction` 重查找实现类，可累计失败次数直到查找成功。

### function <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun function(initiate: KFunctionConditions): Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 创建需要重新查找的 `KFunction`。

你可以添加多个备选 `KFunction`，直到成功为止，若最后依然失败，将停止查找并输出错误日志。

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
fun onFind(initiate: MutableList<KFunction>.() -> Unit)
```

**变更记录**

`v1.0.0` `添加`

`initiate` 类型由 `HashSet` 修改为 `MutableList`

**功能描述**

> 当在 `RemedyPlan` 中找到结果时。

**功能示例**

你可以方便地对重查找的 `KFunction` 实现 `onFind` 方法。

> 示例如下

```kotlin
function {
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

> `KFunction` 查找结果实现类。

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
function {
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
fun get(instance: Any?, extensionRef:Any?, isUseMember:Boolean): Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得 `KFunction` 实例处理类。

若有多个 `KFunction` 结果只会返回第一个。

::: danger

若你设置了 **remedys** 请使用 **wait** 回调结果方法。

:::

**功能示例**

你可以通过获得方法所在实例来执行 `KFunction`。

> 示例如下

```kotlin
function {
    // Your code here.
}.get(instance).call()
```

若当前为静态方法/单例方法，你可以不设置实例。

> 示例如下

```kotlin
function {
    // Your code here.
}.get().call()
```

### all <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun all(instance: Any?, extensionRef:Any?, isUseMember:Boolean): MutableList<Instance>
```

**变更记录**

`v1.0.0` `添加`

返回值类型由 `ArrayList` 修改为 `MutableList`

**功能描述**

> 获得 `KFunction` 实例处理类数组。

返回全部查找条件匹配的多个 `KFunction` 实例结果。

**功能示例**

你可以通过此方法来获得当前条件结果中匹配的全部 `KFunction`，其方法所在实例用法与 `get` 相同。

> 示例如下

```kotlin
function {
    // Your code here.
}.all(instance).forEach { instance ->
    instance.call(...)
}
```

### give <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun give(): KFunction?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到 `KFunction` 本身。

若有多个 `KFunction` 结果只会返回第一个。

在查找条件找不到任何结果的时候将返回 `null`。

### giveAll <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun giveAll(): MutableList<KFunction>
```

**变更记录**

`v1.0.0` `添加`

返回值类型由 `HashSet` 修改为 `MutableList`

**功能描述**

> 得到 `KFunction` 本身数组。

返回全部查找条件匹配的多个 `KFunction` 实例。

在查找条件找不到任何结果的时候将返回空的 `MutableList`。

### wait <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun wait(instance: Any?, extensionRef:Any?, isUseMember:Boolean, initiate: Instance.() -> Unit)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得 `KFunction` 实例处理类，配合 `RemedyPlan` 使用。

若有多个 `KFunction` 结果只会返回第一个。

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

> 获得 `KFunction` 实例处理类数组，配合 `RemedyPlan` 使用。

返回全部查找条件匹配的多个 `KFunction` 实例结果。

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

> 创建 `KFunction` 重查找功能。

**功能示例**

当你遇到一种 `KFunction` 可能存在不同形式的存在时，可以使用 `RemedyPlan` 重新查找它，而没有必要使用 `onNoSuchFunction` 捕获异常二次查找 `KFunction`。

若第一次查找失败了，你还可以在这里继续添加此方法体直到成功为止。

> 示例如下

```kotlin
function {
    // Your code here.
}.remedys {
    function {
        // Your code here.
    }
    function {
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

> 监听找不到 `KFunction` 时。

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
inner class Instance internal constructor(private val instance: Any?, private val function: KFunction<*>?):BaseInstance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KFunction` 实例处理类。

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

> 标识需要调用当前 `KFunction` 未经 Hook 的原始 `KFunction`。

若当前 `KFunction` 并未 Hook 则会使用原始的 `KFunction.call` 方法调用

::: danger

此方法在 Hook Api 存在时将固定 `isUseMember` 为 true

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

> 执行 `KFunction`，不指定返回值类型。

#### invoke <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun <T> invoke(vararg args: Any?): T?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `KFunction`，指定 `T` 返回值类型。

#### byte <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun byte(vararg args: Any?): Byte?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `KFunction`，指定 Byte 返回值类型。

#### int <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun int(vararg args: Any?): Int
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `KFunction`，指定 Int 返回值类型。

#### long <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun long(vararg args: Any?): Long
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `KFunction`，指定 Long 返回值类型。

#### short <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun short(vararg args: Any?): Short
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `KFunction`，指定 Short 返回值类型。

#### double <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun double(vararg args: Any?): Double
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `KFunction`，指定 Double 返回值类型。

#### float <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun float(vararg args: Any?): Float
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `KFunction`，指定 Float 返回值类型。

#### string <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun string(vararg args: Any?): String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `KFunction`，指定 String 返回值类型。

#### char <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun char(vararg args: Any?): Char
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `KFunction`，指定 Char 返回值类型。

#### boolean <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun boolean(vararg args: Any?): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `KFunction`，指定 Boolean 返回值类型。

### array <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun <reified T> array(vararg args: Any?): Array<T>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `KFunction`，指定 Array 返回值类型。

### list <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun <reified T> list(vararg args: Any?): List<T>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行 `KFunction`，指定 List 返回值类型。