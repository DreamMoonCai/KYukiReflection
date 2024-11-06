---
pageClass: code-page
---

# KReflectionCacheFactory <span class="symbol">- kt</span>

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 用于`KReflection`的缓存拓展封装类。

## KClass.cacheProperty <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.cacheProperty(descriptor:String?,initiate: KPropertyConditions): KPropertyFinder.Result
```

```kotlin:no-line-numbers
fun KClass<*>.cacheProperty(attachProperty:KProperty<*>,loader: ClassLoader?,isUseMember:Boolean,initiate: KPropertyConditions): KPropertyFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass.property` 的缓存版本，用于缓存`KPropertyFinder.Result`避免反复搜索。

## KClass.cachePropertySignature <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.cachePropertySignature(descriptor:String?,loader: ClassLoader?, initiate: KPropertySignatureConditions): KPropertySignatureFinder.Result
```

```kotlin:no-line-numbers
fun KClass<*>.cachePropertySignature(attachProperty:KProperty<*>,loader: ClassLoader?,isUseMember:Boolean, initiate: KPropertySignatureConditions): KPropertySignatureFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass.propertySignature` 的缓存版本，用于缓存`KPropertySignatureFinder.Result`避免反复搜索。

## KClass.cacheFunction <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.cacheFunction(descriptor:String?,initiate: KFunctionConditions): KFunctionFinder.Result
```

```kotlin:no-line-numbers
fun KClass<*>.cacheFunction(attachFunction:KFunction<*>,loader: ClassLoader?,isUseMember:Boolean,initiate: KFunctionConditions): KFunctionFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass.function` 的缓存版本，用于缓存`KFunctionFinder.Result`避免反复搜索。

## KClass.cacheFunctionSignature <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.cacheFunctionSignature(descriptor:String?,loader: ClassLoader?, initiate: KFunctionSignatureConditions): KFunctionSignatureFinder.Result
```

```kotlin:no-line-numbers
fun KClass<*>.cacheFunctionSignature(attachFunction:KFunction<*>,loader: ClassLoader?,isUseMember:Boolean, initiate: KFunctionSignatureConditions): KFunctionSignatureFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass.functionSignature` 的缓存版本，用于缓存`KFunctionSignatureFinder.Result`避免反复搜索。

## KClass.cacheConstructor <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.cacheConstructor(descriptor:String?,initiate: KConstructorConditions): KConstructorFinder.Result
```

```kotlin:no-line-numbers
fun KClass<*>.cacheConstructor(attachFunction:KFunction<*>,loader: ClassLoader?,isUseMember:Boolean,initiate: KConstructorConditions): KConstructorFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> `KClass.constructor` 的缓存版本，用于缓存`KConstructorFinder.Result`避免反复搜索。