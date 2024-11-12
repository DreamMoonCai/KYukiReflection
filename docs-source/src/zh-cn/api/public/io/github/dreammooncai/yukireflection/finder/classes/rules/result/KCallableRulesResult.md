---
pageClass: code-page
---

# KCallableRulesResult <span class="symbol">- class</span>

```kotlin:no-line-numbers
class KCallableRulesResult internal constructor(private val rulesData: KCallableRulesData)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 当前 `KCallable` 查找条件结果实现类。

## none <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun none(): KCallableRulesResult
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置当前 `KCallable` 在查找条件中个数为 `0`。

## count <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun count(num: Int): KCallableRulesResult
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置当前 `KCallable` 在查找条件中需要全部匹配的个数。

## count <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun count(numRange: IntRange): KCallableRulesResult
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置当前 `KCallable` 在查找条件中需要全部匹配的个数范围。

## count <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun count(conditions: KCountConditions): KCallableRulesResult
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 设置当前 `KCallable` 在查找条件中需要全部匹配的个数条件。