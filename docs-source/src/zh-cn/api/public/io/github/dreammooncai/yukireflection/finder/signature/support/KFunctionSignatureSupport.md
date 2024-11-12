---
pageClass: code-page
---

# KFunctionSignatureSupport <span class="symbol">- class</span>

```kotlin:no-line-numbers
open class KFunctionSignatureSupport internal constructor(private val declaringClass: KClass<*>?, private val loader: ClassLoader?, private val proto: KSignatureData)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 依据签名信息对 `KFunction` 获取相关信息的支持类

## name <span class="symbol">- field</span>

```kotlin:no-line-numbers
val name: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KFunction` 方法名。

## descriptor <span class="symbol">- field</span>

```kotlin:no-line-numbers
val descriptor: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KFunction` 方法签名。

## paramTypesDescriptors <span class="symbol">- field</span>

```kotlin:no-line-numbers
val paramTypesDescriptors: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KFunction` 参数签名。

## returnTypeDescriptor <span class="symbol">- field</span>

```kotlin:no-line-numbers
val returnTypeDescriptor: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KFunction` 返回类型签名。

## paramTypes <span class="symbol">- field</span>

```kotlin:no-line-numbers
val paramTypes: List<KType>
```

```kotlin:no-line-numbers
val paramTypesOrNull: List<KType>?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 依据参数签名，获取 `KFunction` 参数列表 `KType`。

## paramClass <span class="symbol">- field</span>

```kotlin:no-line-numbers
val paramClass: List<KClass<*>>
```

```kotlin:no-line-numbers
val paramClassOrNull: List<KClass<*>>?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 依据参数签名，获取 `KFunction` 泛型擦除的参数列表 `KClass`。

## returnType <span class="symbol">- field</span>

```kotlin:no-line-numbers
val returnType: KType
```

```kotlin:no-line-numbers
val returnTypeOrNull: KType?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 依据返回类型签名，获取 `KFunction` 返回类型 `KType`。

## returnClass <span class="symbol">- field</span>

```kotlin:no-line-numbers
val returnClass: KClass<*>
```

```kotlin:no-line-numbers
val returnClassOrNull: KClass<*>?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 依据返回类型签名，获取 `KFunction` 泛型擦除的返回类型 `KClass`。

## member <span class="symbol">- field</span>

```kotlin:no-line-numbers
val member: Method
```

```kotlin:no-line-numbers
val memberOrNull: Method?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 依据签名，获取 `KFunction` 以 `Method` 的表述对象。

## hasSignature <span class="symbol">- field</span>

```kotlin:no-line-numbers
val hasSignature: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查签名是否有效。
