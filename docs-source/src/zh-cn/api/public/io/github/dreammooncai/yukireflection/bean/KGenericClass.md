---
pageClass: code-page
---

# KGenericClass <span class="symbol">- class</span>

```kotlin:no-line-numbers
class KGenericClass internal constructor(val type: KType):List<KTypeProjection> by type.arguments
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 当前 `KClass` 的泛型操作对象。

## isVariance <span class="symbol">- field</span>

```kotlin:no-line-numbers
var isVariance: Boolean?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 此泛型对象被对比检查时是否需要考虑协变/逆变性。

## isMarkedNullable <span class="symbol">- field</span>

```kotlin:no-line-numbers
var isMarkedNullable: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 此泛型对象被对比检查时是否需要考虑可空性。

## isAnnotation <span class="symbol">- field</span>

```kotlin:no-line-numbers
var isAnnotation: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 此泛型对象被对比检查时是否需要考虑注解一致。

> 示例如下

```kotlin
vararg abc:@UnsafeVariance Int
```

## variance <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun variance(variance:KVariance): KTypeProjection
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得当前实例中的泛型指定方差的版本。

## argument <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun argument(index: Int): KClass<*>?
```

```kotlin:no-line-numbers
inline fun <reified T> argument(index: Int): KClass<T>?
```

**变更记录**

`v1.0.0` `添加`

方法的返回值可为 `null`

**功能描述**

> 获得泛型参数数组下标的 `KClass` 实例。

::: warning

在运行时获取KClass的行为将导致泛型擦除，获取不到时将会返回 **null**。

:::

> 示例如下

```kotlin
当type为 List<List<String>>
错误示例 argument(0) -> List::class 无法获取进一步:argument(0).argument(0) KClass没有argument方法，当获得KClass后将进行擦除类型
应使用 generic(0).argument(0) -> String
```

## generic <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun generic(index: Int,initiate: KGenericClassDomain): KGenericClass
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得泛型参数数组下标的泛型实例。

> 示例如下

```kotlin
如 type = List<List<Int>>
generic(0) ---> List<Int>
generic(0).generic(0) ---> Int
```