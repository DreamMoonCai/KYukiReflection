---
pageClass: code-page
---

# KFunctionRules <span class="symbol">- class</span>

```kotlin:no-line-numbers
class KFunctionRules internal constructor(private val rulesData: KFunctionRulesData) : KBaseRules
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KFunction` 查找条件实现类。

## name <span class="symbol">- field</span>

```kotlin:no-line-numbers
var name: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 名称。

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

> 设置 `KFunction` 返回值。

可不填写返回值。

## modifiers <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun modifiers(conditions: KModifierConditions)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 标识符筛选条件。

可不设置筛选条件。

## emptyParam <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun emptyParam()
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 空参数、无参数。

## param <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun param(vararg paramType: Any)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 参数。

如果同时使用了 `paramCount` 则 `paramType` 的数量必须与 `paramCount` 完全匹配。

如果 `KFunction` 中存在一些无意义又很长的类型，你可以使用 `VagueType` 来替代它。

::: danger

无参 **Method** 请使用 **emptyParam** 设置查找条件。

有参 **Method** 必须使用此方法设定参数或使用 **paramCount** 指定个数。

:::

## param <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun param(conditions: KParameterConditions)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 参数条件。

::: danger

无参 **Method** 请使用 **emptyParam** 设置查找条件。

有参 **Method** 必须使用此方法设定参数或使用 **paramCount** 指定个数。

:::

## paramName <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun paramName(vararg paramName: String): Unit
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `Constructor KFunction` 参数名称。

如果 `Constructor KFunction` 中存在一些不太清楚的参数名称，你可以使用 [VagueKotlin](../../../type/defined/KDefinedTypeFactory#vaguekotlin-field).name 或者 空字符串 或者 "null" 来替代它。

## paramName <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun paramName(conditions: KNamesConditions): Unit
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `Constructor KFunction` 参数名称条件。

## name <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun name(conditions: KNameConditions)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 名称条件。

## paramCount <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun paramCount(numRange: IntRange)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 参数个数范围。

你可以不使用 `param` 指定参数类型而是仅使用此方法指定参数个数范围。

## paramCount <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun paramCount(conditions: KCountConditions)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 参数个数条件。

你可以不使用 `param` 指定参数类型而是仅使用此方法指定参数个数条件。

## returnType <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun returnType(conditions: KTypeConditions)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KFunction` 返回值条件。

可不填写返回值。