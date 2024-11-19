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
Test::list.generic().argument(0)//直接引用获取，以下例子同理
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

通过上述方法可以获取任意位置存在的泛型信息

这在Java类上也同样适用

> 假设有以下反编译看到的类

```java:no-line-numbers
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class B<T> {
}

class Test extends B<Thread> {
    List<String> list;
    Map<String, List<Short>> map;

    <T extends Double> List<Character> test(Set<Number> one) {
        return new ArrayList<>();
    }
}
```

反编译能看到这些泛型说明这些泛型信息被保留了，我们可以使用 `KYukiReflection` 进行反射

> 示例如下

```kotlin
//获取B中的Thread
Test::class.genericSuper().argument()//默认 index = 0 获取第一个泛型结果为 Thread
//获取List<String> list;中的 String
Test::class.property { name = "list" }.give()!!.generic().argument(0)//通过give获得KProperty，generic默认获取返回类型的泛型对象
Test::list.generic().argument(0)//直接引用获取，以下例子同理
//获取Map<String, List<Short>> map;中的 Short
Test::class.property { name = "map" }.give()!!.generic().generic(1).argument(0)//通过give获得KProperty，generic默认获取返回类型的泛型对象，获取index = 1的泛型对象并获取最终对象的第一个类型
//获取test(Set<Number> one)中的Number
Test::class.function { name = "test" }.give()!!.parameters.first().generic().argument(0)//通过give获得KFunction，通过parameters选定参数，并获取泛型对象拿到第一个泛型
//获取List<Character> test(Set<Number> one)中的Char
Test::class.function { name = "test" }.give()!!.generic().argument(0)//通过give获得KFunction，generic默认获取返回类型的泛型对象
//获取<T extends Double> List<Character> test中的Double
Test::class.function { name = "test" }.give()!!.generics().argument(0)//通过give获得KFunction，generics获取定义参数泛型列表并默认获取上界的类型
```

可以看到与 `Kotlin类` 的反射一样获取泛型一样，并无区别，同样支持 `Test::list` 类似的一键引用

### 泛型匹配

在 `KFunctionFinder` 等查找器中关于类型的参数均支持泛型类型的传入匹配

与 `KCallable` 不同与 `KClass` 的 `generic`，`KClass` 是去构建一个泛型，`KCallable` 是对返回类型进行操作

`KClass.generic` 操作的是 `class B<T>` 当前类与尖括号中的内容，默认情况下构建获得的是 `B<*>` 而 `KYukiReflection` 允许我们随意完善这个类型

> 示例如下

```kotlin
class B<T>

val def = B::class.generic()// ---> 这将获得 B<*>
val thread = B::class.generic(Thread::class)// --> 这将获得B<Thread>
//或者你也可以更复杂的嵌套
val list = B::class.generic(List::class.generic(String::class))// --> 这将获得B<List<String>>
//以下能匹配是因为*允许匹配任意泛型 B<*> == B<Thread>
def == thread && def == list
//很明显，下面的泛型列表不一样，所以无法相等
thread != list

//我们可以考虑更进阶更细致的匹配
//我们为第一个泛型参数指定了方差为out
val str = B::class.generic(String::class.variance(KVariance.OUT)) // 这将获得B<out String>
val str2 = B::class.generic(String::class.variance(KVariance.IN)) // 这将获得B<in String>
val str3 = B::class.generic(String::class) // 这将获得B<String>
//两个泛型同时包括方差时一定会检查并匹配方差很显然方差不一致匹配失败
str != str2
//默认情况下以左边是否有方差检测是否需要匹配方差，当左边没有方差时则只匹配类型
str3 == str
//或者可以像下面这样任意一方明确了需要方差匹配
str3.checkVariance() != str || str3 != str.checkVariance()
//你也可以把可空性以及注解也一并考虑检查
class C {
    var test:List<@UnsafeVariance Int>? = null
}
val listType = B::class.generic(C::test.generic()) // 这将获得B<List<@UnsafeVariance Int>?>
val listType2 = B::class.generic(List::class.generic(Int::class)) // 这将获得B<List<Int>>
//默认情况下他们是等价的
listType == listType2
//但任意一方开启检查，他们就会匹配失败
listType.checkMarkedNullable() != listType2 || listType != listType2.checkMarkedNullable() || listType.checkAnnotation() != listType2 || listType != listType2.checkAnnotation()
```

有了以上泛型的生成对比匹配的基本认识后就可以考虑在一系列查找器中使用

> 示例如下

```kotlin
class Test{
    lateinit var b:List<String>
    lateinit var c:List<Int>
    lateinit var d:List<out Int>
    fun p(one:List<Double>){}
    fun s(one:List<Short>){}
}
//b是混淆的，准确通过类型获取混淆的属性可以像下面这样
Test::class.property { type = List::class.generic(String::class) } // 非常简单
//或者从c和d中准确筛选
Test::class.property { type = List::class.generic(String::class.variance(KVariance.OUT)) } // 获取d
Test::class.property { type = List::class.generic(String::class.checkVariance()) } // 获取c
Test::class.function { param(List::class.generic(Short::class)) } // 获取s
```

::: tip

更多功能请参考 [KClass.generic](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kclass-generic-ext-method)、[KClass.genericSuper](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kclass-genericSuper-ext-method)、[KCallable.generic](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kcallable-generic-ext-method) 方法。

:::

## 快速映射属性/函数

> 这里是关于 Kotlin 类的属性绑定拓展功能和介绍。

### bind进行绑定属性

使用 `KYukiReflection` 可以轻松通过委托绑定某个 `属性` 减少反复书写简单属性的书写...

假设宿主有以下类 并且在模块中可以直接引用

> 示例如下

```kotlin
class Test {
    val name = "test"
    val param:MutableList<Int> = mutableListOf(1,2,3)
    var size = 6
    var nullable:String? = null
    companion object {
        val static = 1
    }
}
```

通过 `bindProperty` 绑定属性可以快速绑定属性，支持 `var` 和 `val` 字段，只需要定义的属性名和类型即可

> 示例如下

```kotlin
// 创建 Test 实例
val instance = Test()
//通过以下方式快速绑定映射属性
val name by Test::class.bindProperty<String>(instance)
name == instance.name // 这是等价的

val param:MutableList<Int> by Test::class.bindProperty(instance)
param.add(4)
instance.param[3] == 4 // 这是等价的操作的是同一个对象

var size by Test::class.bindProperty<Int>(instance)
size = 7//支持var字段 此操作同时会修改 instance.size
// 你也可以绑定静态/对象属性，不需要实例
val static:Int by Test.Companion::class.bindProperty()

val nullable by Test::class.bindPropertyOrNull<String>(instance)
nullable?.isEmpty() // 对于可空属性可以使用bindPropertyOrNull
```

以上示例可以使用原始方式进行操作，但是通过 `bindProperty`、`bindPropertyOrNull` 可以减少重复书写

> 示例如下

```kotlin
// 创建 Test 实例
val instance = Test()

val nameRes = Test::class.property { 
    this.name = "name"
    this.type = String::class
}.get(instance)
nameRes.string() == instance.name // 这是等价的

val param = Test::class.property {
    this.name = "param"
    this.type = MutableList::class.generic(Int::class)
}.get(instance).cast<MutableList<Int>>()!!
param.add(4)
instance.param[3] == 4 // 这是等价的操作的是同一个对象

val sizeRes = Test::class.property {
    this.name = "size"
    this.type = Int::class
}.get(instance)
sizeRes.set(7)//支持var字段 此操作同时会修改 instance.size

// 你也可以绑定静态/对象属性，不需要实例
val static = Test.Companion::class.property {
    this.name = "static"
    this.type = Int::class
}.get().int()

val nullableRes = Test::class.property {
    this.name = "nullable"
    this.type = String::class
}.get(instance)
nullableRes.cast<String>()?.isEmpty() // 稍显复杂
```

对于损坏的Kotlin类同样支持 `bindPropertySignature`、`bindPropertySignatureOrNull` 方法

了解了 `bindProperty` 后，即使 `Test` 无法在模块中直接调用我们也可以很轻松的把 `Test` 的模板做出来

> 示例如下

```kotlin
import kotlin.reflect.KClass

// 将 Test 的实例作为 thisRef 传入
class TestTemplate(thisRef: Any) {
    companion object {
        lateinit var thisRefClass: KClass<*> // 通过自己的方式获取 Test 的Class
        val static by thisRefClass.bindProperty<Int>()
    }
    
    val name by thisRefClass.bindProperty<String>(thisRef)
    val param by thisRefClass.bindProperty<MutableList<Int>>(thisRef)
    var size by thisRefClass.bindProperty<Int>(thisRef)
    val nullable by thisRefClass.bindPropertyOrNull<String>(thisRef)
}
```

::: tip

更多功能请参考 [KClass.bindProperty](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kclass-bindProperty-ext-method)、[KClass.bindPropertyOrNull](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kclass-bindPropertyOrNull-ext-method)、[KClass.bindPropertySignature](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kclass-bindPropertySignature-ext-method)、[KClass.bindPropertySignatureOrNull](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kclass-bindPropertySignatureOrNull-ext-method) 方法。

:::

### 快速附加已知属性/函数到查找器

部分情况下我们已经有与需要查找的目标 `属性/函数` 大部分特征一样的另一个 `属性/函数` ，我们可以通过 `attach` 方法快速附加已知 `属性/函数` 到查找器

> 示例如下

```kotlin
//宿主中假设有以下类
class Test {
    val param:MutableList<Int> = mutableListOf(1,2,3)
    fun test(){/*...*/}
    fun test(param:List<Int>){/*...*/}
    fun run(){/*...*/}
}
//模块中创建与之类似的类
interface TestTemplate {
    val param: MutableList<Int>
    fun test()
    fun test(param:List<Int>)
    fun run()
}
Test::class.property {
    TestTemplate::param.attach()
}.give() == Test::param//这是等价的 相当于使用TestTemplate的属性名和属性类型在Test中查找
Test::class.function {
    attachEmptyParam(TestTemplate::test)
    attach(TestTemplate::test)//attach默认优先匹配有一个及以上参数的函数
}.give()//遇到重载的函数如test时可以通过指定泛型列表来确定使用哪个进行附加

//不过也有其他更轻便的方法，上述场景只是更换了 属性/函数 的所属KClass 那我们可以像下面这样
TestTemplate::param.toKProperty(Test::class)//直接把TestTemplate的param转换为Test下的param
TestTemplate::run.toKFunction(Test::class)//直接把TestTemplate的run转换为Test下的run，这样就不需要attach了但是不适用于重载
val test:KFunction2<*,List<Int>,Unit> = TestTemplate::test // 通过这样复杂的显示声明类型也可以指定获取但并不实用
test.toKFunction(Test::class)
```

::: tip

更多功能请参考 [KCallable.toKCallable](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kcallable-toKCallable-ext-method)、[KProperty.toKProperty](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kproperty-toKProperty-ext-method)、[KFunction.toKFunction](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kfunction-toKFunction-ext-method)、[KPropertyFinder.attach](../api/public/io/github/dreammooncai/yukiReflection/factory/KReflectionFactory#kpropertyfinder-attach-ext-method)、[KFunctionFinder.attach](../api/public/io/github/dreammooncai/yukiReflection/factory/KFunctionAttachFactory#kfunctionfinder-attach-ext-method) 方法。

:::
