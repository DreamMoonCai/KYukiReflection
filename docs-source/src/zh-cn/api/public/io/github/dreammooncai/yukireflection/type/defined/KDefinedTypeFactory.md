---
pageClass: code-page
---

# KDefinedTypeFactory <span class="symbol">- kt</span>

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 这是一个内部类型的定义常量类，主要用于反射 API 相关用法的延伸。

## VagueKotlin <span class="symbol">- field</span>

```kotlin:no-line-numbers
val VagueKotlin: KClass<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 得到模糊类型与`YukiReflection`通用。

## KClass.generic <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<VagueClass>.generic(vararg params:Any): ArrayList<Any>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取模糊根的泛型对象。

**功能示例**

你可以使用此方法来将未知类型的泛型进行匹配。

> 示例如下

```kotlin
class ABC{
    val temp:bbb<Int>
}
val vague = ABC::class.property { type = VagueKotlin.generic(Int::class) }
vague --> val temp:bbb<Int>
```