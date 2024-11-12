# 特色功能

> 这里包含了 `KYukiReflection` 区别于 `YukiReflection` 全部特色功能的用法示例。

## 强力泛型技巧

> 这里是关于 Kotlin 泛型的一些拓展功能和介绍。

### 获取泛型信息

使用 `KYukiReflection` 可以轻松获取各种泛型信息包括 `属性类型`、`参数类型`、`函数返回类型`、`拓展this泛型类型`等，非局部的各式泛型信息...

> 示例如下

```kotlin
open class B<T>
class Test :B<Thread>() {
    lateinit var list: List<String>
    lateinit var map: Map<String,List<Short>>
    fun <B:Double> List<Int>.test(one:Set<Number>):List<Char> = listOf()
}

//获取B中的Thread
Test::class.genericSuper().argument()//默认 index = 0 获取第一个泛型结果为 Thread
//获取list: List<String>中的 String
Test::class.property { name = "list" }.give()!!.generic().argument(0)//通过give获得KProperty，generic默认获取返回类型的泛型对象
//获取map: Map<String,List<Short>>中的 Short
Test::class.property { name = "map" }.give()!!.generic().generic(1).argument(0)//通过give获得KProperty，generic默认获取返回类型的泛型对象，获取index = 1的泛型对象并获取最终对象的第一个类型
//获取test(one:Set<Number>)中的Number
Test::class.function { name = "test" }.give()!!.parameters.first().generic().argument(0)//通过give获得KFunction，通过parameters获取首个参数，并获取泛型对象拿到第一个泛型
//获取test(one:Set<Number>):List<Char>中的Char
Test::class.function { name = "test" }.give()!!.generic().argument(0)//通过give获得KFunction，generic默认获取返回类型的泛型对象
//获取List<Int>.test(one:Set<Number>)中的Int
Test::class.function { name = "test" }.give()!!.instanceParameter!!.generic().argument(0)//通过give获得KFunction，通过instanceParameter获取拓展实例参数，并获取泛型对象拿到第一个泛型
//获取fun <B:Double> List<Int>.test中的Double
Test::class.function { name = "test" }.give()!!.generics().argument(0)//通过give获得KFunction，generics获取定义参数泛型列表并默认获取上界的类型

```
