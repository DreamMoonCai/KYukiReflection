---
pageClass: code-page
---

# KotlinFactory <span class="symbol">- kt</span>

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 关于Kotlin的额外增强封装类。

## KProperty.javaFieldNoError <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KProperty<*>.javaFieldNoError: Field?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KProperty.javaField` 忽略其可能遇到的所有错误，如解析失败并返回NULL
 
## KProperty.javaSignatureField <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KProperty<*>.javaSignatureField: Field?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KProperty.javaFieldNoError` 无法转换时，尝试通过签名方式转换为 `Field`

## KProperty.javaGetterNoError <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KProperty<*>.javaGetterNoError: Method?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KProperty.javaGetter` 忽略其可能遇到的所有错误，如解析失败并返回NULL

## KProperty.javaSignatureGetter <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KProperty<*>.javaSignatureGetter: Method?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KProperty.javaGetterNoError` 无法转换时，尝试通过签名方式转换为 `Method`

## KMutableProperty.javaSetterNoError <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KMutableProperty<*>.javaSetterNoError: Method?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KMutableProperty.javaSetter` 忽略其可能遇到的所有错误，如解析失败并返回NULL

## KMutableProperty.javaSignatureSetter <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KMutableProperty<*>.javaSignatureGetter: Method?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KMutableProperty.javaSetterNoError` 无法转换时，尝试通过签名方式转换为 `Method`

## KFunction.javaMethodNoError <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KFunction<*>.javaMethodNoError: Method?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KFunction.javaMethod` 忽略其可能遇到的所有错误，如解析失败并返回NULL

## KFunction.javaSignatureMethod <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KFunction<*>.javaSignatureMethod: Method?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KFunction.javaMethodNoError` 无法转换时，尝试通过签名方式转换为 `Method`

## KFunction.javaConstructorNoError <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KFunction<*>.javaConstructorNoError: Constructor<*>?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KFunction.javaConstructor` 忽略其可能遇到的所有错误，如解析失败并返回NULL

## KCallable.javaMember <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KCallable<*>.javaMember: Member?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KCallable` 转换为 `Member`，解析失败则返回NULL

## KCallable.javaSignatureMember <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KCallable<*>.javaSignatureMember: Member?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KCallable.javaSignatureMember` 无法转换时，尝试通过签名方式转换为 `Member`

## KProperty.signature <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
val KProperty<*>.signature(declaringClass: KClass<*>?,loader: ClassLoader?,isUseMember: Boolean,initiate: KPropertyConditions)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 与 `KClass.propertySignature` 一致的快捷方法

## KFunction.signature <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
val KFunction<*>.signature(declaringClass: KClass<*>?,loader: ClassLoader?,isUseMember: Boolean,initiate: KFunctionSignatureConditions)
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 与 `KClass.functionSignature` 一致的快捷方法