---
pageClass: code-page
---

# KJvmFactory <span class="symbol">- kt</span>

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 用于Jvm平台关于Kotlin的增强封装类。

## Class.kotlinAs <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val <T> Class<out T>.kotlinAs: KClass<T & Any>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `Class` 强行转换为 `KClass`

## Class.top <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Class<*>.top: KDeclarationContainer 
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `Class` 转换为 *Kt.class 顶级 Kotlin 文件类

## Class.isKotlin <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Class<*>.isKotlin: Boolean 
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查此 `Class` 是否是一个 `Kotlin` 类

## Class.isKotlinNoError <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Class<*>.isKotlinNoError: Boolean 
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查此 `Class` 是否是一个 `Kotlin` 类，并且支持反射不会出现错误

## Class.isArrayOrCollection <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Class<*>.isArrayOrCollection: Boolean 
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查此 `Class` 是否是一个 `Array` 数组或者 `Collection`集合类

## Constructor.instanceKotlin <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun Constructor<*>.instanceKotlin(isUseMember: Boolean): KConstructorFinder.Result.Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `Constructor` 转换为 `KConstructorFinder.Result.Instance` 可执行类

这将涉及到 `Constructor` 到 `KFunction` 的转换，根据 `isUseMember` 参数决定是否使用 `Member` 对象，可能触发 `Kotlin` 反射错误

## Method.instance <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun Method.instance(thisRef: Any?): KFunctionSignatureFinder.Result.Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `Method` 转换为 `KFunctionSignatureFinder.Result.Instance` 可执行类

## Field.instance <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun Field.instance(thisRef: Any?): KPropertySignatureFinder.Result.Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `Field` 转换为 `KPropertySignatureFinder.Result.Instance` 可执行类

## Field.kotlin <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Field.kotlin: KProperty<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `Field` 使用 `KClass.ref`通过签名分析方式 转换为 `KProperty`，此方式不会触发 `Kotlin` 转换错误

## Method.kotlin <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Method.kotlin: KFunction<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `Method` 使用 `KClass.ref`通过签名分析方式 转换为 `KFunction`，此方式不会触发 `Kotlin` 转换错误

## Constructor.kotlin <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Constructor.kotlin: KFunction<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `Constructor` 使用 `KClass.ref`通过签名分析方式 转换为 `KFunction`，此方式不会触发 `Kotlin` 转换错误

## Field.kotlinSimpleSignature <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Field.kotlinSimpleSignature: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取此 `Field` 在 Kotlin 常用的简单签名

**功能示例**

> 示例如下

```java
int abc = 0; // --> "getAbc()I"
```

## Method.kotlinSimpleSignature <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Method.kotlinSimpleSignature: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取此 `Method` 在 Kotlin 常用的简单签名

**功能示例**

> 示例如下

```java
int abc(int a, int b); // --> "abc(II)I"
```

## Constructor.kotlinSimpleSignature <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Constructor<*>.kotlinSimpleSignature: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取此 `Constructor` 在 Kotlin 常用的简单签名

**功能示例**

> 示例如下

```java
class abc(int a, int b){} // --> "<init>(II)V"
```

## Type.classifier <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Type.classifier:KClassifier
```

```kotlin:no-line-numbers
val Type.classifierOrNull:KClassifier?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `JavaType` 转换为 `KClassifier`

## Type.kotlinType <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Type.kotlinType:KType
```

```kotlin:no-line-numbers
val Type.kotlinTypeOrNull:KType?
```

```kotlin:no-line-numbers
val Array<Type>.kotlinType: Array<KType>
```

```kotlin:no-line-numbers
val Array<Type>.kotlinTypeOrNull: Array<KType>?
```

```kotlin:no-line-numbers
val Collection<Type>.kotlinType: List<KType>
```

```kotlin:no-line-numbers
val Collection<Type>.kotlinTypeOrNull: List<KType>?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `JavaType` 转换为 `KType`

## TypeVariable.descriptor <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val TypeVariable<*>.descriptor:ClassifierDescriptor
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `JavaTypeVariable` 转换为 `ClassifierDescriptor`

## TypeVariable.kotlin <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val TypeVariable<*>.kotlin:KTypeParameter
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `JavaTypeVariable` 转换为 `KTypeParameter`

## Member.isAccessible <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
var Member.isAccessible:Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 用于一键设置 `Field.isAccessible`、`Method.isAccessible`、`Constructor.isAccessible`

## Member.kotlin <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Member.kotlin: KCallable<Any?>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `Member` 使用 `KClass.ref`通过签名分析方式 转换为 `KCallable`，此方式不会触发 `Kotlin` 转换错误

## Member.kotlinCallable <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Member.kotlinCallable: KCallable<Any?>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `Member` 使用 `KClass.kotlinProperty`或 `KClass.kotlinFunction`方式 转换为 `KCallable`，此方式官方方式可能会触发错误

## Member.returnType <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Member.returnType: Class<out Any>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `Member` 的返回类型

`Field` ---> `Field.type`

`Method` ---> `Method.returnType`

`Constructor` ---> `Member.getDeclaringClass`