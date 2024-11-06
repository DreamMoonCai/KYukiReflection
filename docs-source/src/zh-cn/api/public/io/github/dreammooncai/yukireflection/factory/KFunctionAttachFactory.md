---
pageClass: code-page
---

# KFunctionAttachFactory <span class="symbol">- kt</span>

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 用于`KFunction`的查找附加拓展的封装类。

## KFunctionFinder.attach <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KFunctionFinder.attach(function: KFunction, loader: ClassLoader?, isUseMember:Boolean): Unit
```

```kotlin:no-line-numbers
fun KFunctionFinder.attachStatic(function: KFunction, loader: ClassLoader?, isUseMember:Boolean): Unit
```

```kotlin:no-line-numbers
fun KFunctionFinder.attachEmptyParam(function: KFunction, loader: ClassLoader?, isUseMember:Boolean): Unit
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将函数`KFunction`相关内容附加到`KFunctionFinder`中。
> `attach` 重载筛选不同参数的函数，最后一个泛型永远是返回类型。