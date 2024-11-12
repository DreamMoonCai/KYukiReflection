---
pageClass: code-page
---

# ModifierRules <span class="symbol">- class</span>

```kotlin:no-line-numbers
class ModifierRules private constructor()
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 这是一个 `KClass`、`KCallable` 描述符条件实现类。

可对 R8 混淆后的 `KClass`、`KCallable` 进行更加详细的定位。

## isPublic <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isPublic: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 类型是否包含 `public`。

## isPrivate <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isPrivate: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 类型是否包含 `private`。

## isProtected <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isProtected: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 类型是否包含 `protected`。

## isStatic <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isStatic: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 类型是否包含 `static`。

对于任意的静态 `KClass`、`KCallable` 可添加此描述进行确定。

::: warning

Kotlin → Jvm 后的 **object** 类中的方法并不是静态的。

:::

## isFinal <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isFinal: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 类型是否包含 `final`。

::: warning

Kotlin → Jvm 后没有 **open** 符号标识的 **Class**、**Member** 和没有任何关联的 **Class**、**Member** 都将为 **final**。

:::

## isSynchronized <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isSynchronized: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 类型是否包含 `synchronized`。

## isVolatile <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isVolatile: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `Field` 类型是否包含 `volatile`。

## isTransient <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isTransient: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `Field` 类型是否包含 `transient`。

## isNative <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isNative: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `Method` 类型是否包含 `native`。

对于任意 JNI 对接的 `Method` 可添加此描述进行确定。

## isInterface <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isInterface: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass` 类型是否包含 `interface`。

## isAbstract <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isAbstract: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 类型是否包含 `abstract`。

对于任意的抽象 `KClass`、`KCallable` 可添加此描述进行确定。

## isStrict <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isStrict: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 类型是否包含 `strictfp`。

## isKotlin <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isKotlin: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 所属类是否是 `Kotlin` 类。

## isCompanion <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isCompanion: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 所属类是否是 `companion object` 伴生对象。

## isData <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isData: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 所属类是否是 `data` 对象类。

## isInner <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isInner: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 所属类是否是 `inner class` 内部类。

## isSealed <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isSealed: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 所属类是否是 `sealed class` 密封类。

## isFun <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isFun: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 所属类是否是 `fun class` 函数类。

## isValue <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isValue: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 所属类是否是 `value class` 属性类。

## isAnonymous <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isAnonymous: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 所属类是否是 `anonymous` 匿名类。

## isConst <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isConst: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KProperty` 是否是 `const` 编译时常量属性。

## isLateinit <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isLateinit: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KProperty` 是否是 `lateinit` 延时初始化属性。

## isVar <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isVar: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KProperty` 是否是 `var` 可变属性。

## isVal <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isVal: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KProperty` 是否是 `val` 常量属性。

## isSuspend <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isSuspend: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KCallable` 是否是 `suspend` 可挂起属性/函数。

## isExternal <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isExternal: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KFunction` 是否是 `external` 外部函数。

## isExtension <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isExtension: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KCallable` 是否是 `extensionRef` 拓展属性/函数。

## isTop <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isTop: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 所属类是否是 `package` 顶级类。

## isInfix <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isInfix: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KFunction` 是否是 `infix` 中缀函数。

## isOperator <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isOperator: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KFunction` 是否是 `operator` 操作符函数。

## isInline <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isInline: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KFunction` 是否是 `inline` 内联函数。

## isOpen <span class="symbol">- i-ext-field</span>

```kotlin:no-line-numbers
val isOpen: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass`、`KCallable` 是否是 `open` 公开继承类/属性/函数。


