---
pageClass: code-page
---

# KPropertySignatureSupport <span class="symbol">- class</span>

```kotlin:no-line-numbers
open class KPropertySignatureSupport internal constructor(private val declaringClass: KClass<*>?, private val loader: ClassLoader?, private val nameResolver: NameResolver, private val proto: JvmProtoBuf.JvmPropertySignature)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 依据签名信息对 `KProperty` 获取相关信息的支持类

## FieldSignatureSupport <span class="symbol">- class</span>

```kotlin:no-line-numbers
inner class FieldSignatureSupport(private val proto: JvmProtoBuf.JvmFieldSignature)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 依据签名信息对 `Field` 获取相关信息的支持类

### name <span class="symbol">- field</span>

```kotlin:no-line-numbers
val name: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `Field` 字段名。

### typeDescriptor <span class="symbol">- field</span>

```kotlin:no-line-numbers
val typeDescriptor: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `Field` 字段类型签名。

### returnType <span class="symbol">- field</span>

```kotlin:no-line-numbers
val returnType: KType
```

```kotlin:no-line-numbers
val returnTypeOrNull: KType?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 依据返回类型签名，获取 `Field` 参数列表 `KType`。

### member <span class="symbol">- field</span>

```kotlin:no-line-numbers
val member: Field
```

```kotlin:no-line-numbers
val memberOrNull: Field?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 依据签名，获取 `Field` 的实例表述对象。

### hasName <span class="symbol">- field</span>

```kotlin:no-line-numbers
val hasName: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查字段名是否有效。

### hasType <span class="symbol">- field</span>

```kotlin:no-line-numbers
val hasType: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查字段类型是否有效。

### hasSignature <span class="symbol">- field</span>

```kotlin:no-line-numbers
val hasSignature: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查字段签名是否有效。

## getter <span class="symbol">- field</span>

```kotlin:no-line-numbers
val getter: KFunctionSignatureSupport
```

```kotlin:no-line-numbers
val getterOrNull: KFunctionSignatureSupport?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取Getter函数签名处理支持组件。

## setter <span class="symbol">- field</span>

```kotlin:no-line-numbers
val setter: KFunctionSignatureSupport
```

```kotlin:no-line-numbers
val setterOrNull: KFunctionSignatureSupport?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取Setter函数签名处理支持组件。

## field <span class="symbol">- field</span>

```kotlin:no-line-numbers
val field: FieldSignatureSupport
```

```kotlin:no-line-numbers
val fieldOrNull: FieldSignatureSupport?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取字段签名处理支持组件。

## delegateFunction <span class="symbol">- field</span>

```kotlin:no-line-numbers
val delegateFunction: KFunctionSignatureSupport
```

```kotlin:no-line-numbers
val delegateFunctionOrNull: KFunctionSignatureSupport?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取Delegate函数签名处理支持组件。

## syntheticFunction <span class="symbol">- field</span>

```kotlin:no-line-numbers
val syntheticFunction: KFunctionSignatureSupport
```

```kotlin:no-line-numbers
val syntheticFunctionOrNull: KFunctionSignatureSupport?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取Synthetic函数签名处理支持组件。

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

> 依据返回类型签名，获取 `KProperty` 返回类型 `KType`。

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

> 依据返回类型签名，获取 `KProperty` 泛型擦除的返回类型 `KClass`。

## member <span class="symbol">- field</span>

```kotlin:no-line-numbers
val member: Member
```

```kotlin:no-line-numbers
val memberOrNull: Member?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 依据签名，获取 `KProperty` 以 `Member` 的表述对象。

## hasGetter <span class="symbol">- field</span>

```kotlin:no-line-numbers
val hasGetter: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查Getter是否有效。

## hasSetter <span class="symbol">- field</span>

```kotlin:no-line-numbers
val hasSetter: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查Setter是否有效。

## hasField <span class="symbol">- field</span>

```kotlin:no-line-numbers
val hasField: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查Field是否有效。

## hasDelegateFunction <span class="symbol">- field</span>

```kotlin:no-line-numbers
val hasDelegateFunction: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查Delegate是否有效。

## hasSyntheticFunction <span class="symbol">- field</span>

```kotlin:no-line-numbers
val hasSyntheticFunction: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查Synthetic是否有效。

## hasSignature <span class="symbol">- field</span>

```kotlin:no-line-numbers
val hasSignature: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查签名是否有效。