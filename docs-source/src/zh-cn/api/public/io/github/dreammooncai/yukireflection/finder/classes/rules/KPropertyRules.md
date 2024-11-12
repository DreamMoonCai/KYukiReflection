---
pageClass: code-page
---

# KPropertyRules <span class="symbol">- class</span>

```kotlin:no-line-numbers
class KPropertyRules internal constructor(private val rulesData: KPropertyRulesData) : KBaseRules
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KProperty` 查找条件实现类。

## name <span class="symbol">- field</span>

```kotlin:no-line-numbers
var name: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KProperty` 名称。

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
fun modifiers(conditions: KModifierConditions)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KProperty` 标识符筛选条件。

可不设置筛选条件。

## name <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun name(conditions: KNameConditions)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KProperty` 名称条件。

## type <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun type(conditions: KTypeConditions)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置 `KProperty` 类型条件。

可不填写类型。