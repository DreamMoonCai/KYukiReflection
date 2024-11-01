---
pageClass: code-page
---

# KCurrentClass <span class="symbol">- class</span>

```kotlin:no-line-numbers
class KCurrentClass internal constructor(private val classSet: KClass<*>, internal val instance: Any)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 当前实例的类操作对象。

## name <span class="symbol">- field</span>

```kotlin:no-line-numbers
val name: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得当前 `classSet` 的 `KClass.name`。

## simpleName <span class="symbol">- field</span>

```kotlin:no-line-numbers
val simpleName: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得当前 `classSet` 的 `KClass.simpleNameOrJvm`。
 
## generic <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun generic(): KGenericClass
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得当前实例中的泛型操作对象。

## generic <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun generic(vararg params: Any,initiate: KTypeBuildConditions): KGenericClass
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 使用指定的泛型类型，获得当前实例中的泛型操作对象。

## genericSuper <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun genericSuper(): KGenericClass?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得当前实例中的泛型父类操作对象。

如果当前实例不存在泛型将返回 `null`。

## genericSuper <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun genericSuper(initiate: KClassConditions): KGenericClass?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得当前实例中的泛型父类并筛选出来。

如果当前实例不存在泛型将返回 `null`。

## superClass <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun superClass(): SuperClass
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 调用父类实例。

## property <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun property(initiate: KPropertyConditions): KPropertyFinder.Result.Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 调用当前实例中的变量/属性。

## function <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun function(initiate: KFunctionConditions): KFunctionFinder.Result.Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 调用当前实例中的方法/函数。

## SuperClass <span class="symbol">- class</span>

```kotlin:no-line-numbers
inner class SuperClass internal constructor(private val superClassSet: KClass<*>)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 当前类的父类实例的类操作对象。

### name <span class="symbol">- field</span>

```kotlin:no-line-numbers
val name: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得当前 `classSet` 中父类的 `KClass.name`。

### simpleName <span class="symbol">- field</span>

```kotlin:no-line-numbers
val simpleName: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得当前 `classSet` 中父类的 `KClass.simpleNameOrJvm`。

## generic <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun generic(): KGenericClass
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得当前实例中父类的泛型操作对象。

## generic <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun generic(vararg params: Any,initiate: KTypeBuildConditions): KGenericClass
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 使用指定的泛型类型，获得当前实例中父类的泛型操作对象。

## genericSuper <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun genericSuper(): KGenericClass?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得当前实例父类中的泛型父类操作对象。

如果当前实例父类不存在泛型将返回 `null`。

## genericSuper <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun genericSuper(initiate: KClassConditions): KGenericClass?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得当前实例中父类的泛型父类并筛选出来。

如果当前实例父类不存在泛型将返回 `null`。

## property <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun property(initiate: KPropertyConditions): KPropertyFinder.Result.Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 调用当前实例中父类的变量/属性。

## function <span class="symbol">- method</span>

```kotlin:no-line-numbers
inline fun function(initiate: KFunctionConditions): KFunctionFinder.Result.Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 调用当前实例中父类的方法/函数。