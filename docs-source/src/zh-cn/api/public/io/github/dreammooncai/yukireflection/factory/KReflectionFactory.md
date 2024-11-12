---
pageClass: code-page
---

# KReflectionFactory <span class="symbol">- kt</span>

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 这是自定义 `KClass` 和 `KCallable` 相关功能的查找匹配以及 `invoke` 的封装类。

## KLazyClass <span class="symbol">- class</span>

```kotlin:no-line-numbers
open class KLazyClass<T> internal constructor(
    private val instance: Any,
    private val initialize: Boolean,
    private val loader: KClassLoaderInitializer?
)
```

**变更记录**

`v1.0.0` `新增`

**功能描述**

> 懒装载 `KClass` 实例。

## KType.kotlin <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KType.kotlin: KClass<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KType` 类型擦除转换为 `KClass`

## KClassifier.kotlin <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClassifier.kotlin: KClass<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KClassifier` 转换为 `KClass`

## KClass.top <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.top: KDeclarationContainer
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KClass` 获取并转换为 *Kt 顶级 Kotlin 文件类

## KClass.isTop <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.isTop: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KClass` 是否是 *Kt 顶级 Kotlin 文件类

## KClass.existTop <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.existTop: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KClass` 是否存在 *Kt 顶级 Kotlin 文件类

## KDeclarationContainer.kotlin <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KDeclarationContainer.kotlin: KClass<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 *Kt 顶级 Kotlin 文件类 转换为 `KClass`

## KDeclarationContainer.declaredTopPropertys <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KDeclarationContainer.declaredTopPropertys: List<KProperty<*>>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 *Kt 顶级 Kotlin 文件类 的所有属性

## KDeclarationContainer.declaredTopFunctions <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KDeclarationContainer.declaredTopFunctions: List<KFunction<*>>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 *Kt 顶级 Kotlin 文件类 的所有函数

## KParameter.kotlin <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KParameter.kotlin: KClass<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KParameter` 转换为 `KClass`

## Collection.kotlin <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Collection<KParameter>.kotlin: Array<KClass<*>>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将一组 `KParameter` 擦除泛型转换为一组 `KClass`

> 将 `KParameter` 转换为 `KClass`

## Collection.type <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val Collection<KParameter>.type: Array<KType>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将一组 `KParameter` 保留泛型数据转换为一组 `KType`

## KProperty.toMutable <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KProperty<*>?.toMutable: KMutableProperty<*>
```

```kotlin:no-line-numbers
val KProperty<*>?.toMutableOrNull: KMutableProperty<*>?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将一组 `KProperty` 强行转换为 `KMutableProperty`

## KProperty.set <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KProperty<*>.set(thisRef: Any?, value: Any?, extensionRef: Any?, isUseMember: Boolean): Unit
```

```kotlin:no-line-numbers
operator fun KProperty<*>.set(thisRef: Any?, value: Any?): Unit
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 为 `KProperty` 修改值

## KClass.isSupportReflection <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.isSupportReflection: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KClass` 是否支持反射

## KProperty.isVar <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KProperty<*>.isVar: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KProperty` 是否支持是由var关键字修饰的

## KProperty.isVal <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KProperty<*>.isVal: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KProperty` 是否支持是由val关键字修饰的

## KClass.isArrayOrCollection <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.isArrayOrCollection: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KClass` 是否为数组或集合类型

## KCallable.declaringClass <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KCallable<*>.declaringClass: KClass<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KCallable` 的所属/声明类

## KCallable.modifiers <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KCallable<*>.modifiers: Int
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KCallable` Java 能表示的首个描述符

## KCallable.returnJavaClass <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val <V> KCallable<V>.returnJavaClass: Class<V>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KCallable` 返回类型 Java 形式

## KCallable.returnClass <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val <V> KCallable<V>.returnClass: KClass<V & Any>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KCallable` 返回类型

## KCallable.ref <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KCallable<*>.ref: CallableReference
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KCallable` 转换为 `CallableReference` 静态引用类

## KCallable.refClass <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KCallable<*>.refClass: KClass<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 通过 `KCallable.ref` 静态引用类信息获取其声明所属 `KClass`

## KCallable.refImpl <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val <T, K : KCallable<T>> K.refImpl: K?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 通过 `KCallable.ref` 静态引用类信息获取其运行时 `K : KCallable`

## CallableReference.impl <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val CallableReference.impl: KCallable<*>?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 通过 `CallableReference` 静态引用类信息获取其运行时 `KCallable`

## CallableReference.declaringKotlin <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val CallableReference.declaringKotlin: KClass<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 通过 `CallableReference` 静态引用类信息获取其声明所属 `KClass`

## KClass.isInterface <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.isInterface: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KClass` 是否为接口

## KClass.superclass <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.superclass: KClass<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 的非接口父类

## KClass.enclosingClass <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.enclosingClass: KClass<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 的密封类

## KClass.simpleNameOrJvm <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.simpleNameOrJvm: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 的简易类名

## KClass.isAnonymous <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.isAnonymous: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KClass` 是否为匿名类

## KClass.name <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.name: String
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 完整类名

## KClass.hasExtends <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.hasExtends: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KClass` 是否有继承 不考虑接口 继承为 `Any` 时算作没有继承

## KClass.interfaces <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.interfaces: List<KClass<*>>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 的所有接口

## KClass.generics <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.generics: List<KTypeParameter>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 的泛型列表

## KClassifier.type <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClassifier.type: KType
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KClassifier` 转换为 `KType`

## KClass.descriptor <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.descriptor: ClassDescriptor
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 的描述符信息对象

## KClass.isObject <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.isObject: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KClass` 是否是 object 关键字修饰的对象类

## KClass.isObject <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.isObject: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KClass` 是否是 object 关键字修饰的对象类

## KClass.singletonInstance <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val <T : Any> KClass<T>.singletonInstance: T?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 的单例实例

## KClass.companionSingletonInstance <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.companionSingletonInstance: Any?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 的伴生对象实例

## KType.isPrimitive <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KType.isPrimitive: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KType` 是否是 Java 基本数据类型

## KClass.toJavaPrimitiveType <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.toJavaPrimitiveType(): KClass<*>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KClass` 的构成转换为基本数据类型构成

转换基本类型对于KClass并没有太大影响 `Int::class == Int::class.toJavaPrimitiveType()`
这仅改变他们的构成，即 `Int::class.java != Int::class.toJavaPrimitiveType().java`

## KType.java <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KType.java: Class<out Any>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KType` 转换为 `Class`

## KCallable.findParameterIndexByName <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KCallable<*>.findParameterIndexByName(name: String, isCountExtensionRef: Boolean, isCountThisRef: Boolean): Int
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 根据参数名获取其 `KCallable` 中所在的下标

## KClass.extends <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>?.extends(other: KClass<*>?): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KClass` 是否继承于 `other`

如果当前 `KClass` 就是 `other` 也会返回 `true`。

如果当前 `KClass` 为 `null` 或 `other` 为 `null` 会返回 `false`。

**功能示例**

你可以使用此方法来判断两个 `KClass` 是否存在继承关系。

> 示例如下

```kotlin
// 假设下面这两个 KClass 就是你需要判断的 KClass
val classA: KClass<*>?
val classB: KClass<*>?
// 判断 A 是否继承于 B
if (classA extends classB) {
    // Your code here.
}
```


## KClass.notExtends <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>?.notExtends(other: KClass<*>?): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KClass` 是否不继承于 `other`

此方法相当于 `extends` 的反向判断。

**功能示例**

你可以使用此方法来判断两个 `KClass` 是否不存在继承关系。

> 示例如下

```kotlin
// 假设下面这两个 KClass 就是你需要判断的 KClass
val classA: KClass<*>?
val classB: KClass<*>?
// 判断 A 是否不继承于 B
if (classA notExtends classB) {
    // Your code here.
}
```

## KClass.implements <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>?.implements(other: KClass<*>?): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KClass` 是否实现了 `other`

如果当前 `KClass` 为 `null` 或 `other` 为 `null` 会返回 `false`。

**功能示例**

你可以使用此方法来判断两个 `KClass` 是否存在依赖关系。

> 示例如下

```kotlin
// 假设下面这两个 KClass 就是你需要判断的 KClass
val classA: KClass<*>?
val classB: KClass<*>?
// 判断 A 是否实现了 B 接口类
if (classA implements classB) {
    // Your code here.
}
```


## KClass.notImplements <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>?.notImplements(other: KClass<*>?): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KClass` 是否未实现 `other`

此方法相当于 `implements` 的反向判断。

**功能示例**

你可以使用此方法来判断两个 `KClass` 是否不存在依赖关系。

> 示例如下

```kotlin
// 假设下面这两个 KClass 就是你需要判断的 KClass
val classA: KClass<*>?
val classB: KClass<*>?
// 判断 A 是否未实现 B 接口类
if (classA notImplements classB) {
    // Your code here.
}
```

## Any.isCase <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
infix fun Any?.isCase(other: KClass<*>?): Boolean
```

```kotlin:no-line-numbers
fun <reified T> Any?.isCase(): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `Any` 是否可以转换为指定类型

## KClass.classLoader <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.classLoader: ClassLoader
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 使用的 `ClassLoader`

## KFunction.isGetter <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KFunction<*>.isGetter: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KFunction` 是否是 Getter 函数

## KFunction.isSetter <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KFunction<*>.isSetter: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KFunction` 是否是 Setter 函数

## KVariousClass.toKClass <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KVariousClass.toKClass(loader: ClassLoader?, initialize: Boolean): KClass<*>
```

```kotlin:no-line-numbers
fun KVariousClass.toKClassOrNull(loader: ClassLoader?, initialize: Boolean): KClass<*>?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 通过 `KVariousClass` 获取 `KClass`

## String.toKClass <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun String.toKClass(loader: ClassLoader?, initialize: Boolean): KClass<*>
```

```kotlin:no-line-numbers
fun <reified T> String.toKClass(loader: ClassLoader?, initialize: Boolean): KClass<T & Any>
```

```kotlin:no-line-numbers
fun String.toKClassOrNull(loader: ClassLoader?, initialize: Boolean): KClass<*>?
```

```kotlin:no-line-numbers
fun <reified T> String.toKClassOrNull(loader: ClassLoader?, initialize: Boolean): KClass<T & Any>?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 通过字符串类名转换为指定 `ClassLoader` 中的实体类

带 OrNull 的版本找不到 `KClass` 会返回 `null`，不会抛出异常。

**功能示例**

你可以直接填写你要查找的目标 `KClass`，必须在默认 `ClassLoader` 下存在。

> 示例如下

```kotlin
"com.example.demo.DemoClass".toKClass()
```

你还可以自定义 `KClass` 所在的 `ClassLoader`。

> 示例如下

```kotlin
val customClassLoader: ClassLoader? = ... // 假设这个就是你的 ClassLoader
"com.example.demo.DemoClass".toKClass(customClassLoader)
```

你还可以指定 `KClass` 的目标类型。

> 示例如下

```kotlin
// 指定的 DemoClass 必须存在或为可访问的 stub
"com.example.demo.DemoClass".toKClass<DemoClass>()
```

你还可以设置在获取到这个 `KClass` 时是否自动执行其默认的静态方法块，默认情况下不会执行。

> 示例如下

```kotlin
// 获取并执行 DemoClass 默认的静态方法块
"com.example.demo.DemoClass".toKClass(initialize = true)
```

默认的静态方法块在 Java 中使用如下方式定义。

> 示例如下

```java:no-line-numbers
public class DemoClass {

    static {
        // 这里是静态方法块的内容
    }

    public DemoClass() {
        // ...
    }
}
```

## KClass.toKClass <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.toKClass(loader: ClassLoader?, initialize: Boolean): KClass<*>
```

```kotlin:no-line-numbers
fun <reified T> KClass<T & Any>.toKClass(loader: ClassLoader?, initialize: Boolean): KClass<T & Any>
```

```kotlin:no-line-numbers
fun KClass<*>.toKClassOrNull(loader: ClassLoader?, initialize: Boolean): KClass<*>?
```

```kotlin:no-line-numbers
fun <reified T> KClass<T & Any>.toKClassOrNull(loader: ClassLoader?, initialize: Boolean): KClass<T & Any>?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 通过 `KClass` 的类名使用指定 `ClassLoader` 转换为新的 `KClass`

## KCallable.toKCallable <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun <K : KCallable<V>, V> K.toKCallable(clazz: KClass<*>?, loader: ClassLoader?, isUseMember: Boolean): K
```

```kotlin:no-line-numbers
fun <K : KCallable<V>, V> K.toKCallableOrNull(clazz: KClass<*>?, loader: ClassLoader?, isUseMember: Boolean): K?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KCallable` 使用指定类与 `ClassLoader` 转换为新的 `KCallable`

## KProperty.toKProperty <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun <K : KProperty<V>, V> K.toKProperty(clazz: KClass<*>?, loader: ClassLoader?, isUseMember: Boolean): K
```

```kotlin:no-line-numbers
fun <K : KProperty<V>, V> K.toKPropertyOrNull(clazz: KClass<*>?, loader: ClassLoader?, isUseMember: Boolean): K?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KProperty` 使用指定类与 `ClassLoader` 转换为新的 `KProperty`

## KFunction.toKFunction <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun <K : KFunction<V>, V> K.toKFunction(clazz: KClass<*>?, loader: ClassLoader?, isUseMember: Boolean): K
```

```kotlin:no-line-numbers
fun <K : KFunction<V>, V> K.toKFunctionOrNull(clazz: KClass<*>?, loader: ClassLoader?, isUseMember: Boolean): K?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KFunction` 使用指定类与 `ClassLoader` 转换为新的 `KFunction`

## kclassOf <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun <reified T> kclassOf(loader: ClassLoader?, initialize: Boolean): KClass<T & Any>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 通过 `T` 得到其 `KClass` 实例并转换为实体类。

**功能示例**

我们要获取一个 `KClass` 在 Kotlin 下不通过反射时应该这样做。

> 示例如下

```kotlin
DemoClass::class
```

现在，你可以直接 `cast` 一个实例并获取它的 `KClass` 对象，必须在当前 `ClassLoader` 下存在。

> 示例如下

```kotlin
kclassOf<DemoClass>()
```

若目标存在的 `KClass` 为 `stub`，通过这种方式，你还可以自定义 `KClass` 所在的 `ClassLoader`。

> 示例如下

```kotlin
val customClassLoader: ClassLoader? = ... // 假设这个就是你的 ClassLoader
kclassOf<DemoClass>(customClassLoader)
```

## lazyKClass <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun lazyKClass(name: String, initialize: Boolean, loader: KClassLoaderInitializer?): KLazyClass.NonNull<Any>
```

```kotlin:no-line-numbers
fun <reified T> lazyKClass(name: String, initialize: Boolean, noinline loader: KClassLoaderInitializer?): KLazyClass.NonNull<T>
```

```kotlin:no-line-numbers
fun lazyKClass(variousClass: KVariousClass, initialize: Boolean, loader: KClassLoaderInitializer?): KLazyClass.NonNull<Any>
```

```kotlin:no-line-numbers
fun lazyKClassOrNull(name: String, initialize: Boolean, loader: KClassLoaderInitializer?): KLazyClass.Nullable<Any>
```

```kotlin:no-line-numbers
fun <reified T> lazyKClassOrNull(name: String, initialize: Boolean, loader: KClassLoaderInitializer?): KLazyClass.Nullable<T>
```

```kotlin:no-line-numbers
fun lazyKClassOrNull(variousClass: KVariousClass, initialize: Boolean, loader: KClassLoaderInitializer?): KLazyClass.Nullable<Any>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 懒加载获取 `KClass`

## String.hasKClass <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun String.hasKClass(loader: ClassLoader?): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 通过字符串类名使用指定的 `ClassLoader` 查找是否存在 `KClass`

**功能示例**

你可以轻松的使用此方法判断字符串中的类是否存在。

> 示例如下

```kotlin
if("com.example.demo.DemoClass".hasKClass()) {
    // Your code here.
}
```

填入方法中的 `loader` 参数可判断指定的 `ClassLoader` 中的 `KClass` 是否存在。

> 示例如下

```kotlin
val customClassLoader: ClassLoader? = ... // 假设这个就是你的 ClassLoader
if("com.example.demo.DemoClass".hasKClass(customClassLoader)) {
    // Your code here.
}
```

## KClass.hasProperty <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.hasProperty(initiate: KPropertyConditions): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 查找在`KClass`中是否存在指定 `KProperty` 属性

## KClass.hasPropertySignature <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.hasPropertySignature(loader: ClassLoader?, initiate: KPropertySignatureConditions): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 查找在`KClass`中是否存在指定 `KProperty` 属性签名

## KClass.hasFunction <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.hasFunction(initiate: KFunctionConditions): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 查找在`KClass`中是否存在指定 `KFunction` 函数

## KClass.hasFunctionSignature <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.hasFunctionSignature(loader: ClassLoader?, initiate: KFunctionSignatureConditions): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 查找在`KClass`中是否存在指定 `KFunction` 函数签名

## KClass.hasConstructor <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.hasConstructor(initiate: KConstructorConditions): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 查找在`KClass`中是否存在指定 Constructor `KFunction` 构造函数

## KCallable.hasModifiers <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KCallable<*>.hasModifiers(conditions: KModifierConditions): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查在 `KCallable` 中是否存在匹配的描述符

## KClass.hasModifiers <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.hasModifiers(conditions: KModifierConditions): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查在 `KClass` 中是否存在匹配的描述符

## KClass.property <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.property(initiate: KPropertyConditions): KPropertyFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 中的指定属性

## KBaseFinder.BaseInstance.property <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KBaseFinder.BaseInstance.property(vararg args: Any?, initiate: KPropertyConditions): KPropertyFinder.Result.Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 对已经查询的 `KBaseFinder.BaseInstance` 实例执行获取结果，对此结果再次查找其中属性

## KClass.propertySignature <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.propertySignature(loader: ClassLoader?, initiate: KPropertySignatureConditions): KPropertySignatureFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 中的指定的属性签名实例

## KClass.function <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.function(initiate: KFunctionConditions): KFunctionFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 中的指定函数

## KBaseFinder.BaseInstance.function <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KBaseFinder.BaseInstance.function(vararg args: Any?, initiate: KFunctionConditions): KFunctionFinder.Result.Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 对已经查询的 `KBaseFinder.BaseInstance` 实例执行获取结果，对此结果再次查找其中函数

## KClass.functionSignature <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.functionSignature(loader: ClassLoader?, initiate: KFunctionSignatureConditions): KFunctionSignatureFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 中的指定的函数签名实例

## KClass.constructor <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.constructor(initiate: KConstructorConditions): KConstructorFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 中的指定构造函数

## KBaseFinder.BaseInstance.constructor <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KBaseFinder.BaseInstance.constructor(vararg args: Any?, initiate: KConstructorConditions): KConstructorFinder.Result.Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 对已经查询的 `KBaseFinder.BaseInstance` 实例执行获取结果，对此结果再次查找其中构造函数

## KClass.genericSuper <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.genericSuper(initiate: KClassConditions): KGenericClass
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 的父类泛型操作对象

## KClass.generic <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.generic(vararg params: Any, initiate: KTypeBuildConditions): KGenericClass
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 使用指定泛型参数创建包含泛型信息的 `KClass` 泛型操作对象

## KClass.variance <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.variance(variance: KVariance): KTypeProjection
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 使用指定方差创建包含方差信息的 `KTypeProjection` 方差实例对象

## KProperty.generic <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KProperty<*>.generic(initiate: KGenericClassDomain): KGenericClass
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KProperty` 返回类型的泛型操作对象

## KType.generic <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KType.generic(initiate: KGenericClassDomain): KGenericClass
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KType` 转换为泛型操作对象

## KType.genericBuild <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KType.genericBuild(vararg params: Any, initiate: KTypeBuildConditions): KGenericClass
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 为当前 `KType` 附加指定泛型参数后转换为泛型操作对象

## KType.variance <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KType.variance(variance: KVariance): KTypeProjection
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 使用指定方差创建包含方差信息的 `KTypeProjection` 方差实例对象

## Collection.generic <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun Collection<KType>.generic(initiate: KClassConditions): KGenericClass
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将一组的 `KType` 指定其中一个转换为泛型操作对象

## Array.generic <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun Array<KType>.generic(initiate: KClassConditions): KGenericClass
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将一组的 `KType` 指定其中一个转换为泛型操作对象

## Any.currentKotlin <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun <reified T : Any> T.currentKotlin(ignored: Boolean): KCurrentClass
```

```kotlin:no-line-numbers
fun <reified T : Any> T.currentKotlin(ignored: Boolean, initiate: KCurrentClass.() -> Unit): T
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获得当前实例的类操作对象 Kotlin版本

## KClass.buildOf <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.buildOf(vararg args: Any?, initiate: KConstructorConditions): Any?
```

```kotlin:no-line-numbers
fun <T> KClass<*>.buildOf(vararg args: Any?, initiate: KConstructorConditions): T?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 通过构造方法创建新实例

## KClass.declaredPropertys <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KClass<*>.declaredPropertys: Collection<KProperty<*>>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KClass` 不包括父类的所有声明属性

## KCallable.isExtension <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KCallable<*>.isExtension: Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 检查 `KCallable` 是否是拓展函数

## KCallable.descriptor <span class="symbol">- ext-field</span>

```kotlin:no-line-numbers
val KCallable<*>.descriptor: CallableMemberDescriptor?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取 `KCallable` 描述信息实例

## KClass.allFunctions <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.allFunctions(isAccessible: Boolean, result: (index: Int, function: KFunction<*>) -> Unit): Unit
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 遍历 `KClass` 的所有函数

## KClass.allFunctionSignatures <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.allFunctionSignatures(loader: ClassLoader?, result: (index: Int, function: KFunctionSignatureSupport) -> Unit): Unit
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 遍历 `KClass` 的所有函数签名

## KClass.allConstructors <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.allConstructors(isAccessible: Boolean, result: (index: Int, function: KFunction<*>) -> Unit): Unit
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 遍历 `KClass` 的所有构造函数

## KClass.allPropertys <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.allPropertys(isAccessible: Boolean, result: (index: Int, property: KProperty<*>) -> Unit): Unit
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 遍历 `KClass` 的所有属性

## KClass.allPropertySignatures <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KClass<*>.allPropertySignatures(loader: ClassLoader?, result: (index: Int, property: KPropertySignatureSupport) -> Unit): Unit
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 遍历 `KClass` 的所有属性签名

## Collection.findClass <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun Collection<KClass<*>>.findClass(initiate: KClassConditions): KClassFinder.Result
```

```kotlin:no-line-numbers
fun Collection<String>.findClass(loader: ClassLoader?, initialize: Boolean, initiate: KClassConditions): KClassFinder.Result
```

```kotlin:no-line-numbers
fun Collection<KType>.findClass(initiate: KClassConditions): KClassFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 在集合中查找或筛选 `KClass`

## Array.findClass <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun Array<KClass<*>>.findClass(initiate: KClassConditions): KClassFinder.Result
```

```kotlin:no-line-numbers
fun Array<String>.findClass(loader: ClassLoader?, initialize: Boolean, initiate: KClassConditions): KClassFinder.Result
```

```kotlin:no-line-numbers
fun Array<KType>.findClass(initiate: KClassConditions): KClassFinder.Result
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 在数组中查找或筛选 `KClass`

## Collection.findType <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun Collection<KType>.findType(classSet: KClass<*>?): KType
```

```kotlin:no-line-numbers
fun <reified T> Collection<KType>.findType(): KType
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 在集合中查找 `KType`

## Array.findType <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun Array<KType>.findType(classSet: KClass<*>?): KType
```

```kotlin:no-line-numbers
fun <reified T> Array<KType>.findType(): KType
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 在数组中查找 `KType`

## KFunction.instance <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KFunction<*>.instance(thisRef: Any?, extensionRef: Any?, isUseMember: Boolean): KFunctionFinder.Result.Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KFunction` 转换为 `KFunctionFinder.Result.Instance` 可执行类

## KFunction.constructor <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KFunction<*>.constructor(isUseMember: Boolean): KConstructorFinder.Result.Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 Constructor`KFunction` 转换为 `KConstructorFinder.Result.Instance` 可执行类

## KProperty.instance <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun KProperty<*>.instance(thisRef: Any?, extensionRef: Any?, isUseMember: Boolean): KPropertyFinder.Result.Instance
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将 `KProperty` 转换为 `KPropertyFinder.Result.Instance` 可执行类

## KConstructorFinder.attach <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun <R> KConstructorFinder.attach(function: KFunction<R>, loader: ClassLoader?, isUseMember: Boolean): Unit
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将构造函数`KFunction`相关内容附加到`KConstructorFinder`中。

## KPropertyFinder.attach <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun <R> KPropertyFinder.attach(property: KProperty<R>, loader: ClassLoader?, isUseMember: Boolean): Unit
```

```kotlin:no-line-numbers
fun <R> KPropertyFinder.attachStatic(property: KProperty0<R>, loader: ClassLoader?, isUseMember: Boolean): Unit
```

```kotlin:no-line-numbers
fun <ExpandThis, R> KPropertyFinder.attach(property: KProperty2<*, ExpandThis, R>, loader: ClassLoader?, isUseMember: Boolean): Unit
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 将属性`KProperty`相关内容附加到`KPropertyFinder`中。

## BindingInstanceSupport <span class="symbol">- class</span>

```kotlin:no-line-numbers
open class BindingInstanceSupport<T>(
    private val thisRefClass: KClass<*>,
    private var thisRef: Any?,
    private val extensionRef: Any?,
    private val isUseMember: Boolean,
    private val isLazy: Boolean,
    private val mappingRules: KPropertyFinder.(property: KProperty<*>) -> Unit
)
```

**变更记录**

`v1.0.0` `新增`

**功能描述**

> 委托绑定映射 ` KProperty` 实例。

## KClass.bindProperty <span class="symbol">- ext-method</span>

```kotlin:no-line-numbers
fun <T> KClass<*>.bindProperty(
    thisRef: Any?,
    extensionRef: Any?,
    isUseMember: Boolean,
    isLazy: Boolean,
    noinline mappingRules: KPropertyFinder.(property: KProperty<*>) -> Unit
): BindingInstanceSupport.NonNull<T>
```
```kotlin:no-line-numbers
fun <T> KClass<*>.bindPropertyOrNull(
    thisRef: Any?,
    extensionRef: Any?,
    isUseMember: Boolean,
    isLazy: Boolean,
    noinline mappingRules: KPropertyFinder.(property: KProperty<*>) -> Unit
): BindingInstanceSupport.Nullable<T>
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 与指定 `KProperty` 取得/绑定映射关系


