@file:Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE","MISSING_DEPENDENCY_SUPERCLASS","INVISIBLE_MEMBER","INVISIBLE_REFERENCE")

package io.github.dreammooncai.yukireflection.factory

import io.github.dreammooncai.yukireflection.KYukiReflection
import io.github.dreammooncai.yukireflection.bean.KCurrentClass
import io.github.dreammooncai.yukireflection.bean.KGenericClass
import io.github.dreammooncai.yukireflection.bean.KVariousClass
import io.github.dreammooncai.yukireflection.build.KTypeBuild
import io.github.dreammooncai.yukireflection.finder.base.KBaseFinder
import io.github.dreammooncai.yukireflection.finder.base.rules.KModifierRules
import io.github.dreammooncai.yukireflection.finder.callable.KConstructorFinder
import io.github.dreammooncai.yukireflection.finder.callable.KFunctionFinder
import io.github.dreammooncai.yukireflection.finder.callable.KPropertyFinder
import io.github.dreammooncai.yukireflection.finder.classes.KClassFinder
import io.github.dreammooncai.yukireflection.finder.signature.KFunctionSignatureFinder
import io.github.dreammooncai.yukireflection.finder.signature.KPropertySignatureFinder
import io.github.dreammooncai.yukireflection.finder.signature.support.KFunctionSignatureSupport
import io.github.dreammooncai.yukireflection.finder.signature.support.KPropertySignatureSupport
import io.github.dreammooncai.yukireflection.finder.tools.KReflectionTool
import io.github.dreammooncai.yukireflection.type.factory.*
import io.github.dreammooncai.yukireflection.type.kotlin.*
import io.github.dreammooncai.yukireflection.utils.factory.ifTrue
import java.lang.ref.WeakReference
import java.lang.reflect.*
import kotlin.jvm.internal.CallableReference
import kotlin.jvm.internal.FunctionReference
import kotlin.jvm.internal.PropertyReference
import kotlin.reflect.*
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.superclasses
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.internal.calls.ThrowingCaller.returnType
import kotlin.reflect.jvm.internal.impl.descriptors.CallableMemberDescriptor
import kotlin.reflect.jvm.internal.impl.descriptors.ClassDescriptor
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf
import kotlin.reflect.jvm.internal.impl.metadata.jvm.JvmProtoBuf
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.jvmName

/**
 * 懒装载 [KClass] 实例
 * @param instance 当前实例
 * @param initialize 是否初始化
 * @param loader [ClassLoader] 装载实例
 */
open class KLazyClass<T> internal constructor(
    private val instance: Any,
    private val initialize: Boolean,
    private val loader: KClassLoaderInitializer?,
) {

    /** 当前实例 */
    private var baseInstance: KClass<T & Any>? = null

    /**
     * 获取非空实例
     * @return [KClass]<[T]>
     */
    internal val nonNull
        get(): KClass<T & Any> {
            if (baseInstance == null) baseInstance = when (instance) {
                is String -> instance.toKClass(loader?.invoke(), initialize)
                is KVariousClass -> instance.get(loader?.invoke(), initialize)
                else -> error("Unknown lazy class type \"$instance\"")
            } as KClass<T & Any>
            return baseInstance ?: error("Exception has been thrown above")
        }

    /**
     * 获取可空实例
     * @return [KClass]<[T]> or null
     */
    internal val nullable
        get(): KClass<T & Any>? {
            if (baseInstance == null) baseInstance = when (instance) {
                is String -> instance.toKClass(loader?.invoke(), initialize)
                is KVariousClass -> instance.get(loader?.invoke(), initialize)
                else -> error("Unknown lazy class type \"$instance\"")
            } as KClass<T & Any>
            return baseInstance
        }

    /**
     * 非空实例
     * @param instance 当前实例
     * @param initialize 是否初始化
     * @param loader [ClassLoader] 装载实例
     */
    class NonNull<T> internal constructor(
        instance: Any,
        initialize: Boolean,
        loader: KClassLoaderInitializer?,
    ) : KLazyClass<T>(instance, initialize, loader) {

        operator fun getValue(thisRef: Any?, property: KProperty<*>) = nonNull
    }

    /**
     * 可空实例
     * @param instance 当前实例
     * @param initialize 是否初始化
     * @param loader [ClassLoader] 装载实例
     */
    class Nullable<T> internal constructor(
        instance: Any,
        initialize: Boolean,
        loader: KClassLoaderInitializer?,
    ) : KLazyClass<T>(instance, initialize, loader) {

        operator fun getValue(thisRef: Any?, property: KProperty<*>) = nullable
    }
}

/**
 * 将 [KType] 转换为 [KClass]
 *
 * - 此行为将进行泛型擦除 如果KType是类似T:Number的类型此操作将返回Number 如果没有界限则为Any
 */
inline val KType.kotlin get() = this.jvmErasure

/**
 * 获取当前 [KClassifier]/[KClass]/[KTypeParameter] 对应的 [KClass]
 *
 * [KTypeParameter] -> 根据界限获取最合适的 [KClass]
 */
inline val KClassifier?.kotlin get() = when (this) {
    is KClass<*> -> this
    is KTypeParameter -> this.type.jvmErasure
    else -> error("Unknown classifier type \"$this\"")
}

/**
 * 获取当前 [KClass] 的顶级操作对象
 *
 * 如定义在根class外的内容 包括函数/字段等
 *
 * 顶层往往会在类名后增加Kt
 */
inline val KClass<*>.top: KDeclarationContainer get() = java.top

/**
 * 检查此[KClass]是否是顶级类
 *
 * 如 此类是xx.kt文件 -> xxKt.class
 *
 * top作为KClass没法获得有用信息需要使用KClass<*>.top转换为可使用的顶级操作对象
 */
inline val KClass<*>.isTop get() = jvmName.endsWith("Kt")

/**
 * 检查此[KClass]是否存在与之对应的顶级类
 *
 * 如 此类是xx.kt文件 -> xxKt.class
 */
inline val KClass<*>.existTop get() = isTop || "${name}Kt".toKClassOrNull() != null

/**
 * 获取顶层文件或类的 [KClass]
 */
val KDeclarationContainer.kotlin
    get() = this as? KClass<*> ?: this::class.property { name = "jClass";superClass() }.get(this).cast<Class<*>>()?.kotlin ?: error("Failed to find Class in $this.")

/**
 * 获取顶层文件或类的所有 [KProperty]
 */
@Suppress("RecursivePropertyAccessor")
val KDeclarationContainer.declaredTopPropertys: List<KProperty<*>> get() = if (this !is KClass<*>) members.filterIsInstance<KProperty<*>>() else top.declaredTopPropertys

/**
 * 获取顶层文件或类的所有 [KFunction]
 */
@Suppress("RecursivePropertyAccessor")
val KDeclarationContainer.declaredTopFunctions: List<KFunction<*>> get() = if (this !is KClass<*>) members.filterIsInstance<KFunction<*>>() else top.declaredTopFunctions

/**
 * 将 [KParameter] 转换为 [KClass]
 *
 * @see [KType.kotlin]
 */
inline val KParameter.kotlin get() = this.type.jvmErasure

/**
 * 将 [KParameter] 转换为 [KClass]
 *
 * @return Collection [KClass]
 * @see [KType.kotlin]
 */
inline val Collection<KParameter>.kotlin get() = this.map { it.type.jvmErasure }.toTypedArray()

/**
 * 将 [KParameter] 转换为 [KType]
 *
 * @return Collection [KType]
 * @see [KType.kotlin]
 */
inline val Collection<KParameter>.type get() = this.map { it.type }.toTypedArray()

/**
 * 将 [KProperty] 转换为 [KMutableProperty]
 *
 * @return [KMutableProperty]
 */
inline val KProperty<*>?.toMutable
    get() = toMutableOrNull ?: error("The conversion failed, which is not a mutable property.")

/**
 * 将 [KProperty] 转换为 [KMutableProperty]
 *
 * 如果无法转换则返回 Null
 *
 * @return [KMutableProperty] or null
 */
inline val KProperty<*>?.toMutableOrNull get() = this as? KMutableProperty

/**
 * 为当前属性设置值
 *
 * - isUseMember 开启时优先 Field > SetterMethod > [KProperty.call]
 *
 * - isUseMember 默认情况下只使用 [KProperty.call]
 *
 * @param thisRef 此属性的this实例对象
 * @param value 属性所被设置的值
 * @param extensionRef 属性如果是拓展属性你还需要传入拓展属性的this对象
 * @param isUseMember 是否优先将属性转换Java方式进行set
 */
inline fun KProperty<*>.set(thisRef: Any? = null, value: Any?, extensionRef: Any? = null, isUseMember: Boolean = false) {
    if (isUseMember || toMutableOrNull == null) {
        val field = javaFieldNoError
        if (field != null) {
            field.isAccessible = true
            field[thisRef] = value
            return
        }
        val setter = toMutableOrNull?.javaSetterNoError
        if (setter != null) {
            setter.isAccessible = true
            if (isExtension)
                setter.invoke(thisRef, extensionRef, value)
            else
                setter.invoke(thisRef, value)
            return
        }
    }
    val setter = toMutableOrNull?.setter ?: return
    if (isExtension) {
        if (thisRef == null) {
            setter.call(extensionRef, value)
        } else
            setter.call(thisRef, extensionRef, value)
    } else {
        if (thisRef == null)
            setter.call(value)
        else
            setter.call(thisRef, value)
    }
}

/**
 * 为当前属性设置值的快捷方式
 *
 * 快捷方式下拓展this为null，isUseMember为false
 *
 * @see [set]
 *
 * @param thisRef 此属性的this实例对象
 * @param value 属性所被设置的值
 */
operator fun KProperty<*>.set(thisRef: Any? = null, value: Any?) = set(thisRef, value, null, false)

/**
 * 此 属性 [KProperty] 是否为可变属性
 *
 * @return [Boolean]
 */
inline val KProperty<*>.isVar: Boolean
    get() = when (this) {
        is KMutableProperty -> true
        else -> false
    }

/**
 * 获取属性/函数 [KCallable] 的声明类
 *
 * @return [KClass] or null
 */
val KCallable<*>.declaringClass
    get() = refClass ?: when (this) {
        is KProperty -> javaFieldNoError?.declaringClass ?: javaGetterNoError?.declaringClass ?: (this as? KMutableProperty<*>?)?.javaSetterNoError?.declaringClass
        is KFunction -> javaMethodNoError?.declaringClass ?: javaConstructorNoError?.declaringClass
        else -> null
    }?.kotlin

/**
 * 获取属性/函数 [KCallable] 的Java修饰符
 *
 * @return [Int] or null
 */
val KCallable<*>.modifiers
    get() = when (this) {
        is KProperty -> javaFieldNoError?.modifiers ?: javaGetterNoError?.modifiers ?: (this as? KMutableProperty<*>?)?.javaSetterNoError?.modifiers
        is KFunction -> javaMethodNoError?.modifiers ?: javaConstructorNoError?.modifiers
        else -> null
    }

/**
 * 获取属性/函数 [KCallable] 的Java返回类型
 *
 * 即使获取不成功也会通过签名获取
 *
 * - 注意此属性获得的类型会类型擦除，保留泛型信息请使用[KCallable.returnType]
 *
 * @return JavaClass对象实例 [Class]
 */
val <V> KCallable<V>.returnJavaClass: Class<V> get() = returnClass.java as Class<V>

/**
 * 获取属性/函数 [KCallable] 的Kotlin返回类型
 *
 * 即使获取不成功也会通过签名获取
 *
 * - 注意此属性获得的类型会类型擦除，保留泛型信息请使用[KCallable.returnType]
 *
 * @return KotlinClass对象实例 [KClass]
 */
inline val <V> KCallable<V>.returnClass: KClass<V & Any>
    @JvmName("getKTypeAs") get() = try {
        returnType.jvmErasure
    } catch (e: Exception) {
        when (this) {
            is KProperty -> javaFieldNoError?.type ?: javaGetterNoError?.returnType ?: (this as? KMutableProperty<*>?)?.javaSetterNoError?.returnType
            is KFunction -> javaMethodNoError?.returnType ?: javaConstructorNoError?.declaringClass
            else -> null
        }?.kotlin ?: error("Unsupported conversions.")
    } catch (e: Error) {
        ref.signature.let {
            it.substring(it.indexOf(")L") + ")L".length, it.lastIndex).replace("/", ".")
        }.toKClass()
    } as KClass<V & Any>

/**
 * 获取属性/函数 [KCallable] 的kotlin具体引用信息
 *
 * 引用信息往往存在于自动生成的Kotlin类中
 *
 *     如: String::substring.apply { -- code -- }.ref // 其::的行为将在编译时为使用String::substring所在的类创建CallableReference的引用类 其包含类信息和方法签名信息等
 *
 *         String::class.function { name = "substring" }.give().ref // error:指定从KClass获取的函数并不会通过CallableReference创建并获取
 *
 * [CallableReference] 的存在为了让引用反射获取基础信息能加载更快，通过KClass获取会慢上几分，但一旦涉及复杂反射他们速度将持平
 *
 * @return [CallableReference]
 */
inline val KCallable<*>.ref
    get() = this as CallableReference

/**
 * 参阅 [ref] 通过其引用信息获取 [KClass]
 */
inline val KCallable<*>.refClass
    get() = runCatching { ref.declaringKotlin }.getOrNull()

/**
 * 将引用对象转换为实际对象，参阅[ref]
 *
 * 即通过对此引用相关信息在目标类中查找得到细致信息，只有细致信息的属性可以准确获取混淆信息
 *
 * - 未获取到细致信息时返回 null
 */
inline val <T,K:KCallable<T>> K.refImpl
    get() = runCatching { ref.impl as K }.getOrNull()

/**
 * 将引用对象转换为实际对象
 *
 * 即通过对此引用相关信息在目标类中查找得到细致信息，只有细致信息的属性可以准确获取混淆信息
 *
 * - 未获取到细致信息时返回 null
 */
val CallableReference.impl: KCallable<*>?
    get() {
        when (this) {
            is FunctionReference -> {
                for (callable in (owner.kotlin.declaredFunctions + if (owner.kotlin.existTop) owner.declaredTopFunctions else arrayListOf())) {
                    if (callable.name == this.name && callable.javaMethodNoError == this.javaMethodNoError) return callable
                }
                for (callable in (owner.kotlin.declaredFunctions + if (owner.kotlin.existTop) owner.declaredTopFunctions else arrayListOf())) {
                    if (callable.name != this.name && callable.javaMethodNoError == this.javaMethodNoError) return callable
                }
                return null
            }

            is PropertyReference -> {
                for (callable in (owner.kotlin.declaredPropertys + if (owner.kotlin.existTop) owner.declaredTopPropertys else arrayListOf())) {
                    if (callable.name == this.name && callable.javaFieldNoError == this.javaFieldNoError) return callable
                }
                for (callable in (owner.kotlin.declaredPropertys + if (owner.kotlin.existTop) owner.declaredTopPropertys else arrayListOf())) {
                    if (callable.name != this.name && callable.javaFieldNoError == this.javaFieldNoError) return callable
                }
                return null
            }

            else -> throw NotImplementedError("[$this] References are not implemented.")
        }
    }

/**
 * 获取引用信息 [CallableReference] 的声明类
 *
 * @return [KClass]
 */
inline val CallableReference.declaringKotlin
    get() = this.owner.kotlin

/**
 * 当前 [KClass] 的父类
 *
 * @return 父类 [KClass] 对象
 */
inline val KClass<*>.superclass get() = superclasses.find { !it.java.isInterface } ?: Any::class

/**
 * 当前 [KClass] 的密封类
 *
 * @return 密封类 [KClass] 对象
 */
inline val KClass<*>.enclosingClass get() = this.java.enclosingClass?.kotlinAs

/**
 * 当前 [KClass] 的简易类名 未找到时通过java对象获取
 *
 * @return [String]
 */
inline val KClass<*>.simpleNameOrJvm: String get() = this.simpleName ?: this.java.simpleName

/**
 * 当前 [KClass] 是否是匿名类 - 来自Java
 *
 * @return [Boolean]
 */
inline val KClass<*>.isAnonymous get() = this.java.isAnonymousClass

/**
 * 当前 [KClass] 的完整类名 未找到时通过java对象获取
 *
 * @return [String]
 */
inline val KClass<*>.name: String get() = this.qualifiedName ?: this.jvmName

/**
 * 当前 [KClass] 是否有继承关系 - 父类是 [Any] 将被认为没有继承关系
 *
 * @return [Boolean]
 */
inline val KClass<*>.hasExtends get() = superclass != Any::class

/**
 * 当前 [KClass] 的所有接口
 *
 * @return List<[KClass]>
 */
inline val KClass<*>.interfaces get() = superclasses.filter { it.java.isInterface }

/**
 * 当前 [KClass] 的泛型列表
 *
 *     class A<T?,in D,L:Int?>() -> [T?,in D,L:Int?]
 *
 * 附带继承(界限)信息
 */
inline val KClass<*>.generics get() = typeParameters

/**
 * 当前 [KClass] 的 [KType] 表示
 *
 *     class A<T,in D,L:Int>() -> A<*,*>
 *
 * 泛型参数信息由星射代替[KTypeProjection.STAR]
 */
inline val KClassifier.type get() = starProjectedType

/**
 * 根据当前 [KClass] 获取其单例实例
 *
 * 此方法是 [KClass.objectInstance] 的变种但无视固定的实例名称
 *
 * 优先对懒汉模式进行获取，获取不到时使用饿汉模式，支持被混淆的单例或对象类
 *
 * 懒汉模式示例:
 *
 *     public class A {
 *         public final static List<A> INSTANCES = new ArrayList<>(new A())
 *         public static A get(){ return INSTANCES.get(0) }
 *     } --> get:A 实例
 *
 * 在未找到时将按照以下饿汉模式获取实例
 *
 *     object A --> A 实例
 *
 *     public class A {
 *         public final static A INSTANCE = new A()
 *     } --> INSTANCE:A 实例
 *
 */
inline val <T: Any> KClass<T>.singletonInstance get() = let {
    function {
        returnType = it
        emptyParam()
        modifiers { isStatic }
    }.get().invoke<T>() ?: property {
        type = it
        modifiers { isStatic }
    }.get().cast<T>()
}

/**
 * 当前 [KClass] 的伴生对象单例实例
 *
 * 此方法是 [KClass.companionObjectInstance] 的变种但无视固定的实例名称
 *
 *     class A {
 *         companion object{
 *             fun a(){}
 *         }
 *     } A::class --> A.Companion 实例
 *
 * 支持被混淆的伴生对象
 *
 * 在未找到时将按照通过查找方法的方式获取 尽管不一定有用
 */
inline val KClass<*>.companionSingletonInstance get() = companionObject?.let {
    property {
        type = it
        modifiers { isStatic }
    }.get().any() ?: function {
        returnType = it
        emptyParam()
        modifiers { isStatic }
    }.get().call()
}

/**
 * 当前 [KType] 是否是基本类型
 *
 * 基本类型来源于Java概念
 *
 * @see [KType.javaType]
 * @return [Boolean]
 */
inline val KType.isPrimitive get() = java.isPrimitive

/**
 * 自动转换当前 [KClass] 基于类为 Java 原始类型 (Primitive Type)
 *
 * 如果当前 [KClass] 为 Java 或 Kotlin 基本类型将自动执行类型转换
 *
 * 当前能够自动转换的基本类型如下 ↓
 *
 * - [java.lang.Boolean]
 * - [java.lang.Integer]
 * - [java.lang.Float]
 * - [java.lang.Double]
 * - [java.lang.Long]
 * - [java.lang.Short]
 * - [java.lang.Character]
 * - [java.lang.Byte]
 *
 * 转换基本类型对于KClass并没有太大影响 Int::class == Int::class.toJavaPrimitiveType()
 * 这仅改变他们的构成，即 Int::class.java != Int::class.toJavaPrimitiveType().java
 * @return [KClass]
 */
fun KClass<*>.toJavaPrimitiveType() = when (this) {
    BooleanKClass, BooleanKType -> BooleanKType
    IntKClass, IntKType -> IntKType
    FloatKClass, FloatKType -> FloatKType
    DoubleKClass, DoubleKType -> DoubleKType
    LongKClass, LongKType -> LongKType
    ShortKClass, ShortKType -> ShortKType
    CharKClass, CharKType -> CharKType
    ByteKClass, ByteKType -> ByteKType
    else -> this
}

/**
 * 当前 [KType] 的 [Class] 类型
 *
 * 获取保留的来源Class 如封装Int或是基本Int
 *
 * @return [Class]
 */
inline val KType.java get() = kotlin.java

/**
 * 获取指定 参数名[name] 的 参数下标[Int] 从 0 开始
 *
 * 此方法默认不会计入this对象和拓展对象
 *
 * 未找到指定参数名的参数返回 -1
 *
 * @param name 参数名
 * @param isCountExtensionRef 是否计入拓展对象
 * @param isCountThisRef 是否计入this对象
 * @return [Int] 参数下标
 */
inline fun KCallable<*>.findParameterIndexByName(name: String,isCountExtensionRef:Boolean = false,isCountThisRef:Boolean = false): Int {
    var count = 0
    parameters.forEach {
        if (it.name == name)return count
        if (it.kind == KParameter.Kind.INSTANCE && !isCountThisRef)
            return@forEach
        if (it.kind == KParameter.Kind.EXTENSION_RECEIVER && !isCountExtensionRef)
            return@forEach
        count++
    }
    return -1
}

/**
 * 当前 [KClass] 是否继承于 [other]
 *
 * 如果当前 [KClass] 就是 [other] 也会返回 true
 *
 * 如果当前 [KClass] 为 null 或 [other] 为 null 会返回 false
 * @param other 需要判断的 [KClass]
 * @return [Boolean]
 */
infix fun KClass<*>?.extends(other: KClass<*>?): Boolean {
    if (this == null || other == null) return false
    var isMatched = false

    /**
     * 查找是否存在父类
     * @param current 当前 [KClass]
     */
    fun findSuperClass(current: KClass<*>) {
        if (current == other)
            isMatched = true
        else if (current != Any::class) findSuperClass(current.superclass)
    }
    findSuperClass(current = this)
    return isMatched
}

/**
 * 当前 [KClass] 是否不继承于 [other]
 *
 * 此方法相当于 [extends] 的反向判断
 * @param other 需要判断的 [KClass]
 * @return [Boolean]
 */
inline infix fun KClass<*>?.notExtends(other: KClass<*>?) = extends(other).not()

/**
 * [other] 是否可以转换为 [KClass]
 *
 * @param other 需要判断的实例对象
 * @return [Boolean]
 */
inline fun KClass<*>?.isCase(other: Any?) = this?.isInstance(other) ?: false

/**
 * 当前 [KClass] 的 ClassLoader
 *
 * @return [ClassLoader]
 */
inline val KClass<*>.classLoader get() = this.java.classLoader!!

/**
 * 当前 [KFunction] 是否是 Getter 函数
 *
 * Getter 函数的类型可能是 [KProperty.Getter] 或函数名为 <get-xxx>
 */
inline val KFunction<*>.isGetter get() = when {
    this is KProperty.Getter<*> -> true
    this.name.startsWith("<get-") && this.name.endsWith(">") -> true
    else -> false
}

/**
 * 当前 [KFunction] 是否是 Setter 函数
 *
 * Setter 函数的类型可能是 [KMutableProperty.Setter] 或函数名为 <set-xxx>
 */
inline val KFunction<*>.isSetter get() = when {
    this is KMutableProperty.Setter<*> -> true
    this.name.startsWith("<set-") && this.name.endsWith(">") -> true
    else -> false
}

/**
 * 通过 [KVariousClass] 获取 [KClass]
 * @param loader [KClass] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @return [KClass]
 * @throws NoClassDefFoundError 如果找不到 [KClass] 或设置了错误的 [ClassLoader]
 */
inline fun KVariousClass.toKClass(loader: ClassLoader? = null, initialize: Boolean = false) = get(loader,initialize)

/**
 * 通过 [KVariousClass] 获取 [KClass]
 *
 * 找不到 [KClass] 会返回 null - 不会抛出异常
 * @param loader [KClass] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @return [KClass] or null
 */
inline fun KVariousClass.toKClassOrNull(loader: ClassLoader? = null, initialize: Boolean = false) = getOrNull(loader,initialize)

/**
 * 通过字符串类名转换为 [loader] 中的实体类
 * @param loader [KClass] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @return [KClass]
 * @throws NoClassDefFoundError 如果找不到 [KClass] 或设置了错误的 [ClassLoader]
 */
inline fun String.toKClass(loader: ClassLoader? = null, initialize: Boolean = false) =
    KReflectionTool.findClassByName(name = this, loader, initialize)

/**
 * 通过字符串类名转换为 [loader] 中的实体类
 * @param loader [KClass] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @return [KClass]<[T]>
 * @throws NoClassDefFoundError 如果找不到 [KClass] 或设置了错误的 [ClassLoader]
 * @throws IllegalStateException 如果 [KClass] 的类型不为 [T]
 */
@JvmName("toKClass_Generics")
inline fun <reified T> String.toKClass(loader: ClassLoader? = null, initialize: Boolean = false) =
    toKClass(loader, initialize) as? KClass<T & Any>? ?: error("Target Class type cannot cast to ${T::class}")

/**
 * 通过字符串类名转换为 [loader] 中的实体类
 *
 * 找不到 [KClass] 会返回 null - 不会抛出异常
 * @param loader [KClass] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @return [KClass] or null
 */
inline fun String.toKClassOrNull(loader: ClassLoader? = null, initialize: Boolean = false) = runCatching { toKClass(loader, initialize) }.getOrNull()

/**
 * 通过字符串类名转换为 [loader] 中的实体类
 *
 * 找不到 [KClass] 会返回 null - 不会抛出异常
 * @param loader [KClass] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @return [KClass]<[T]> or null
 */
@JvmName("toKClassOrNull_Generics")
inline fun <reified T> String.toKClassOrNull(loader: ClassLoader? = null, initialize: Boolean = false) =
    runCatching { toKClass<T>(loader, initialize) }.getOrNull()

/**
 * 通过此[KClass]的字符串类名转换为 [loader] 中的实体类 - 意为重新获取
 * @param loader [KClass] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @return [KClass]
 * @throws NoClassDefFoundError 如果找不到 [KClass] 或设置了错误的 [ClassLoader]
 */
inline fun KClass<*>.toKClass(loader: ClassLoader? = null, initialize: Boolean = false) = name.toKClass(loader, initialize)

/**
 * 通过此[KClass]的字符串类名转换为 [loader] 中的实体类 - 意为重新获取
 * @param loader [KClass] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @return [KClass]<[T]>
 * @throws NoClassDefFoundError 如果找不到 [KClass] 或设置了错误的 [ClassLoader]
 * @throws IllegalStateException 如果 [KClass] 的类型不为 [T]
 */
@JvmName("toKClass_Generics")
inline fun <reified T> KClass<T & Any>.toKClass(loader: ClassLoader? = null, initialize: Boolean = false) =
    name.toKClass<T>(loader, initialize)

/**
 * 通过此[KClass]的字符串类名转换为 [loader] 中的实体类 - 意为重新获取
 *
 * 找不到 [KClass] 会返回 null - 不会抛出异常
 * @param loader [KClass] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @return [KClass] or null
 */
inline fun KClass<*>.toKClassOrNull(loader: ClassLoader? = null, initialize: Boolean = false) = runCatching { toKClass(loader, initialize) }.getOrNull()

/**
 * 通过此[KClass]的字符串类名转换为 [loader] 中的实体类 - 意为重新获取
 *
 * 找不到 [KClass] 会返回 null - 不会抛出异常
 * @param loader [KClass] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @return [KClass]<[T]> or null
 */
@JvmName("toKClassOrNull_Generics")
inline fun <reified T> KClass<T & Any>.toKClassOrNull(loader: ClassLoader? = null, initialize: Boolean = false) =
    name.toKClassOrNull<T>(loader, initialize)

/**
 * 通过 [clazz] 和 [this] 获取其中的 [KCallable]
 *
 * [loader] 也会作用于所有涉及的类型如 返回类型 参数类型等
 *
 * @param K [this] 支持重新获取可调用实例
 * @param V 可调用实例的返回值
 * @param clazz 指定在哪个类下进行 [KCallable] 获取，默认为 [declaringClass] - 涉及类必须在 [loader] 中否则会抛出 [NoClassDefFoundError]
 * @param loader [clazz] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param isUseMember 是否使用Java方式反射获取参数名和参数类型 - 默认否
 * @return [KCallable] 新获取到的可调用实例
 */
inline fun <K:KCallable<V>,V> K.toKCallable(clazz: KClass<*>? = null,loader: ClassLoader? = null,isUseMember: Boolean = false): K {
    return when (this){
        is KProperty<*> -> this.toKProperty(clazz,loader,isUseMember)
        is KFunction<*> -> this.toKFunction(clazz,loader,isUseMember)
        else -> error("Unsupported KCallable type: $this")
    }
}

/**
 * 通过 [clazz] 和 [this] 获取其中的 [KCallable]
 *
 * [loader] 也会作用于所有涉及的类型如 返回类型 参数类型等
 *
 * 获取不到 [KCallable] 会返回 null - 不会抛出异常
 * @param K [this] 支持重新获取可调用实例
 * @param V 可调用实例的返回值
 * @param clazz 指定在哪个类下进行 [KCallable] 获取，默认为 [declaringClass] - 涉及类必须在 [loader] 中否则会返回 null
 * @param loader [clazz] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param isUseMember 是否使用Java方式反射获取参数名和参数类型 - 默认否
 * @return [KCallable] 新获取到的可调用实例 or null
 */
inline fun <K:KCallable<V>,V> K.toKCallableOrNull(clazz: KClass<*>? = null,loader: ClassLoader? = null,isUseMember: Boolean = false) = runCatching { toKCallable(clazz,loader,isUseMember) }.getOrNull()

/**
 * 通过 [clazz] 和 [this] 获取其中的 [KProperty]
 *
 * [loader] 也会作用于所有涉及的类型如 返回类型 参数类型等
 *
 * @param K [this] 支持重新获取可调用实例
 * @param V 可调用实例的返回值
 * @param clazz 指定在哪个类下进行 [KProperty] 获取，默认为 [declaringClass] - 涉及类必须在 [loader] 中否则会抛出 [NoClassDefFoundError]
 * @param loader [clazz] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param isUseMember 是否使用Java方式反射获取参数名和参数类型 - 默认否
 * @return [KProperty] 新获取到的可调用实例
 */
inline fun <K:KProperty<V>,V> K.toKProperty(clazz: KClass<*>? = null,loader: ClassLoader? = null,isUseMember: Boolean = false): K {
    val declaringClass = (clazz ?: declaringClass)?.toKClass(loader) ?: throw NoClassDefFoundError("$clazz not found in $loader")
    return declaringClass.property {
        this@toKProperty.attach(loader,isUseMember)
    }.give() as K
}

/**
 * 通过 [clazz] 和 [this] 获取其中的 [KProperty]
 *
 * [loader] 也会作用于所有涉及的类型如 返回类型 参数类型等
 *
 * 获取不到 [KProperty] 会返回 null - 不会抛出异常
 * @param K [this] 支持重新获取可调用实例
 * @param V 可调用实例的返回值
 * @param clazz 指定在哪个类下进行 [KProperty] 获取，默认为 [declaringClass] - 涉及类必须在 [loader] 中否则会返回 null
 * @param loader [clazz] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param isUseMember 是否使用Java方式反射获取参数名和参数类型 - 默认否
 * @return [KProperty] 新获取到的可调用实例 or null
 */
inline fun <K:KProperty<V>,V> K.toKPropertyOrNull(clazz: KClass<*>? = null,loader: ClassLoader? = null,isUseMember: Boolean = false) = runCatching { toKProperty(clazz,loader,isUseMember) }.getOrNull()

/**
 * 通过 [clazz] 和 [this] 获取其中的 [KFunction]
 *
 * [loader] 也会作用于所有涉及的类型如 返回类型 参数类型等
 *
 * @param K [this] 支持重新获取可调用实例
 * @param V 可调用实例的返回值
 * @param clazz 指定在哪个类下进行 [KFunction] 获取，默认为 [declaringClass] - 涉及类必须在 [loader] 中否则会抛出 [NoClassDefFoundError]
 * @param loader [clazz] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param isUseMember 是否使用Java方式反射获取参数名和参数类型 - 默认否
 * @return [KFunction] 新获取到的可调用实例
 */
inline fun <K:KFunction<V>,V> K.toKFunction(clazz: KClass<*>? = null,loader: ClassLoader? = null,isUseMember: Boolean = false): K {
    val declaringClass = (clazz ?: declaringClass)?.toKClass(loader) ?: throw NoClassDefFoundError("$clazz not found in $loader")
    return declaringClass.function {
        this@toKFunction.attach(loader,isUseMember)
    }.give() as K
}

/**
 * 通过 [clazz] 和 [this] 获取其中的 [KFunction]
 *
 * [loader] 也会作用于所有涉及的类型如 返回类型 参数类型等
 *
 * 获取不到 [KFunction] 会返回 null - 不会抛出异常
 * @param K [this] 支持重新获取可调用实例
 * @param V 可调用实例的返回值
 * @param clazz 指定在哪个类下进行 [KFunction] 获取，默认为 [declaringClass] - 涉及类必须在 [loader] 中否则会返回 null
 * @param loader [clazz] 所在的 [ClassLoader] - 默认空 - 不填使用默认 [ClassLoader]
 * @param isUseMember 是否使用Java方式反射获取参数名和参数类型 - 默认否
 * @return [KFunction] 新获取到的可调用实例 or null
 */
inline fun <K:KFunction<V>,V> K.toKFunctionOrNull(clazz: KClass<*>? = null,loader: ClassLoader? = null,isUseMember: Boolean = false) = runCatching { toKFunction(clazz,loader,isUseMember) }.getOrNull()

/**
 * 通过 [T] 得到其 [KClass] 实例并转换为实体类
 * @param loader [KClass] 所在的 [ClassLoader] - 默认空 - 可不填
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 如果未设置 [loader] (为 null) 时将不会生效 - 默认否
 * @return [KClass]<[T]>
 * @throws NoClassDefFoundError 如果找不到 [KClass] 或设置了错误的 [ClassLoader]
 */
inline fun <reified T> kclassOf(loader: ClassLoader? = null, initialize: Boolean = false): KClass<T & Any> =
    (loader?.let { T::class.java.name.toKClass(loader, initialize) } ?: T::class) as KClass<T & Any>

/**
 * 懒装载 [KClass]
 * @param name 完整类名
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @param loader [ClassLoader] 装载实例 - 默认空 - 不填使用默认 [ClassLoader]
 * @return [KLazyClass.NonNull]
 */
fun lazyKClass(name: String, initialize: Boolean = false, loader: KClassLoaderInitializer? = null) =
    lazyKClass<Any>(name, initialize, loader)

/**
 * 懒装载 [KClass]<[T]>
 * @param name 完整类名
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @param loader [ClassLoader] 装载实例 - 默认空 - 不填使用默认 [ClassLoader]
 * @return [KLazyClass.NonNull]<[T]>
 */
@JvmName("lazyClass_Generics")
inline fun <reified T> lazyKClass(name: String, initialize: Boolean = false, noinline loader: KClassLoaderInitializer? = null) =
    KLazyClass.NonNull<T>(name, initialize, loader)

/**
 * 懒装载 [KClass]
 * @param variousClass [KVariousClass]
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @param loader [ClassLoader] 装载实例 - 默认空 - 不填使用默认 [ClassLoader]
 * @return [KLazyClass.NonNull]
 */
fun lazyKClass(variousClass: KVariousClass, initialize: Boolean = false, loader: KClassLoaderInitializer? = null) =
    KLazyClass.NonNull<Any>(variousClass, initialize, loader)

/**
 * 懒装载 [KClass]
 * @param name 完整类名
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @param loader [ClassLoader] 装载实例 - 默认空 - 不填使用默认 [ClassLoader]
 * @return [KLazyClass.Nullable]
 */
fun lazyKClassOrNull(name: String, initialize: Boolean = false, loader: KClassLoaderInitializer? = null) =
    lazyKClassOrNull<Any>(name, initialize, loader)

/**
 * 懒装载 [KClass]<[T]>
 * @param name 完整类名
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @param loader [ClassLoader] 装载实例 - 默认空 - 不填使用默认 [ClassLoader]
 * @return [KLazyClass.Nullable]<[T]>
 */
@JvmName("lazyClassOrNull_Generics")
inline fun <reified T> lazyKClassOrNull(name: String, initialize: Boolean = false, noinline loader: KClassLoaderInitializer? = null) =
    KLazyClass.Nullable<T>(name, initialize, loader)

/**
 * 懒装载 [KClass]
 * @param variousClass [KVariousClass]
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @param loader [ClassLoader] 装载实例 - 默认空 - 不填使用默认 [ClassLoader]
 * @return [KLazyClass.Nullable]
 */
fun lazyKClassOrNull(variousClass: KVariousClass, initialize: Boolean = false, loader: KClassLoaderInitializer? = null) =
    KLazyClass.Nullable<Any>(variousClass, initialize, loader)

/**
 * 通过字符串类名使用指定的 [ClassLoader] 查找是否存在
 * @param loader [KClass] 所在的 [ClassLoader] - 不填使用默认 [ClassLoader]
 * @return [Boolean] 是否存在
 */
inline fun String.hasKClass(loader: ClassLoader? = null) = KReflectionTool.hasClassByName(name = this, loader)

/**
 * 查找变量是否存在
 * @param initiate 方法体
 * @return [Boolean] 是否存在
 */
inline fun KClass<*>.hasProperty(initiate: KPropertyConditions) = property(initiate).ignored().isNoSuch.not()

/**
 * 查找变量签名是否存在
 * @param loader [KClass] 所在的 [ClassLoader] - 不填使用默认 [ClassLoader]
 * @param initiate 方法体
 * @return [Boolean] 是否存在
 */
inline fun KClass<*>.hasPropertySignature(loader: ClassLoader? = null,initiate: KPropertySignatureConditions) = propertySignature(loader,initiate).ignored().isNoSuch.not()

/**
 * 查找方法是否存在
 * @param initiate 方法体
 * @return [Boolean] 是否存在
 */
inline fun KClass<*>.hasFunction(initiate: KFunctionConditions) = function(initiate).ignored().isNoSuch.not()

/**
 * 查找方法签名是否存在
 * @param loader [KClass] 所在的 [ClassLoader] - 不填使用默认 [ClassLoader]
 * @param initiate 方法体
 * @return [Boolean] 是否存在
 */
inline fun KClass<*>.hasFunctionSignature(loader: ClassLoader? = null,initiate: KFunctionSignatureConditions) = functionSignature(loader,initiate).ignored().isNoSuch.not()

/**
 * 查找构造方法是否存在
 * @param initiate 方法体
 * @return [Boolean] 是否存在
 */
inline fun KClass<*>.hasConstructor(initiate: KConstructorConditions = { emptyParam() }) = constructor(initiate).ignored().isNoSuch.not()

/**
 * 查找 [KCallable] 中匹配的描述符
 * @param conditions 条件方法体
 * @return [Boolean] 是否存在
 */
inline fun KCallable<*>.hasModifiers(conditions: KModifierConditions) = conditions(KModifierRules.with(instance = this))

/**
 * 查找 [KClass] 中匹配的描述符
 * @param conditions 条件方法体
 * @return [Boolean] 是否存在
 */
inline fun KClass<*>.hasModifiers(conditions: KModifierConditions) = conditions(KModifierRules.with(instance = this))

/**
 * 查找并得到变量
 * @param initiate 查找方法体
 * @return [KPropertyFinder.Result]
 */
inline fun KClass<*>.property(initiate: KPropertyConditions = {}) = KPropertyFinder(classSet = this).apply(initiate).build() as KPropertyFinder.Result

/**
 * 对已有结果进行 结果再查询
 *
 * 自动将结果作为this
 *
 * @param args 如果获取此结果还需要参数 则使用 *args
 * @param initiate 查找方法体
 * @return [KPropertyFinder.Result]
 */
inline fun KBaseFinder.BaseInstance.property(vararg args:Any?,initiate: KPropertyConditions = {}) = callResult(*args)?.let { it::class.property(initiate).get(it) } ?: KPropertyFinder().Result().Instance(null,null)

/**
 * 查找并得到方法签名
 *
 * 获取此 [KClass] 指定 [initiate] 条件的签名
 *
 * 此方法以通过 [Metadata] 中定义的属性名获取Java层真正的签名
 *
 * [KPropertySignatureConditions] 中对属性类型进行筛选如果目标类型也有问题可能依然会出错，建议使用属性名筛选
 *
 * - 此方法不涉及转 Kotlin 的反射属性可以避免一些异常 [Metadata] 数据报错
 * @param loader [ClassLoader] 相关涉及的类型所在的 [ClassLoader]
 * @param initiate 查找方法体
 * @return [KFunctionFinder.Result]
 */
inline fun KClass<*>.propertySignature(loader: ClassLoader? = null,initiate: KPropertySignatureConditions = {}) = KPropertySignatureFinder(classSet = this,loader).apply(initiate).build()

/**
 * 查找并得到方法
 * @param initiate 查找方法体
 * @return [KFunctionFinder.Result]
 */
inline fun KClass<*>.function(initiate: KFunctionConditions = {}) = KFunctionFinder(classSet = this).apply(initiate).build() as KFunctionFinder.Result

/**
 * 对已有结果进行 结果再查询
 *
 * 自动将结果作为this
 *
 * @param args 如果获取此结果还需要参数 则使用 *args
 * @param initiate 查找方法体
 * @return [KFunctionFinder.Result]
 */
inline fun KBaseFinder.BaseInstance.function(vararg args:Any?,initiate: KFunctionConditions = {}) = callResult(*args)?.let { it::class.function(initiate).get(it) } ?: KFunctionFinder().Result().Instance(null,null)

/**
 * 查找并得到方法签名
 *
 * 获取此 [KClass] 指定 [initiate] 条件的签名
 *
 * 此方法以通过 [Metadata] 中定义的函数名获取Java层真正的签名
 *
 * [KFunctionSignatureConditions] 中对返回类型和参数类型进行筛选如果目标类型也有问题可能依然会出错，建议使用参数名筛选
 *
 * - 此方法不涉及转 Kotlin 的反射函数可以避免一些异常 [Metadata] 数据报错
 * @param loader [ClassLoader] 相关涉及的类型所在的 [ClassLoader]
 * @param initiate 查找方法体
 * @return [KFunctionFinder.Result]
 */
inline fun KClass<*>.functionSignature(loader: ClassLoader? = null,initiate: KFunctionSignatureConditions = {}) = KFunctionSignatureFinder(classSet = this,loader).apply(initiate).build()

/**
 * 查找并得到构造方法
 * @param initiate 查找方法体
 * @return [KConstructorFinder.Result]
 */
inline fun KClass<*>.constructor(initiate: KConstructorConditions = {}) = KConstructorFinder(classSet = this).apply(initiate).build()

/**
 * 对已有结果进行 结果再查询
 *
 * 自动将结果作为this
 *
 * @param args 如果获取此结果还需要参数 则使用 *args
 * @param initiate 查找方法体
 * @return [KConstructorFinder.Result]
 */
inline fun KBaseFinder.BaseInstance.constructor(vararg args:Any?,initiate: KConstructorConditions = {}) = callResult(*args)?.let { it::class.constructor(initiate).get() } ?: KConstructorFinder().Result().Instance(null)

/**
 * 获得当前 [KClass] 的父类中来自尖括号的泛型对象
 *
 * [KClassConditions]同来筛选来自哪个父类/父接口
 *
 * @param initiate 实例方法体
 * @return [KGenericClass]
 */
inline fun KClass<*>.genericSuper(initiate: KClassConditions = {}): KGenericClass = supertypes.generic(initiate)

/**
 * 为当前 [KClass] 构建细节Type 并获得其泛型操作对象
 *
 * [KTypeBuild]构建时所需要的参数
 *
 * @param params 为此类型增加泛型参数
 * @param initiate 实例方法体
 * @return [KGenericClass]
 */
inline fun KClass<*>.generic(vararg params: Any, initiate: KTypeBuildConditions = {}): KGenericClass =
    type.generic().build { params.isNotEmpty().ifTrue { param(*params) };initiate() }

/**
 * 指定并获得类型方差版本
 *
 * out/in/默认
 *
 * @param variance 类型方差 默认没有任何方差
 * @return 包含方差映射的类型
 */
inline fun KClass<*>.variance(variance: KVariance = KVariance.INVARIANT): KTypeProjection = KTypeProjection(variance, type)

/**
 * 获得当前 [KProperty] 的返回类型中来自尖括号的泛型操作对象
 *
 * @param initiate 实例方法体
 * @return [KGenericClass]
 */
inline fun KProperty<*>.generic(initiate: KGenericClass.() -> Unit = {}): KGenericClass = returnType.generic(initiate)

/**
 * 获得当前 [KType] 的泛型操作对象
 *
 * @param initiate 实例方法体
 * @receiver [KGenericClass]
 */
inline fun KType.generic(initiate: KGenericClass.() -> Unit = {}) = KGenericClass(this).apply(initiate)

/**
 * 通过对当前 [KType] 的细节重构获取完成后的泛型操作对象
 *
 * @param params 为此类型增加泛型参数
 * @param initiate 实例方法体
 * @receiver [KTypeBuild]
 */
inline fun KType.genericBuild(vararg params: Any, initiate: KTypeBuildConditions = {}) =
    generic().build { params.isNotEmpty().ifTrue { param(*params) };initiate() }

/**
 * 指定并获得类型方差版本
 *
 * out/in/默认
 *
 * @param variance 类型方差 默认没有任何方差
 * @return 包含方差映射的类型
 */
inline fun KType.variance(variance: KVariance = KVariance.INVARIANT): KTypeProjection = KTypeProjection(variance, this)

/**
 * 获得当前 [KType] 数组的泛型操作对象
 *
 * [KClassConditions]同来筛选泛型来自哪个类/接口
 *
 * @param initiate 筛选获得泛型所在的Class
 * @return [KGenericClass]
 */
inline fun Collection<KType>.generic(initiate: KClassConditions = {}): KGenericClass = this.findType(this.findClass(initiate).get()).generic()

/**
 * 获得当前 [KType] 数组的泛型操作对象
 *
 * [KClassConditions]同来筛选泛型来自哪个类/接口
 *
 * @param initiate 筛选获得泛型所在的Class
 * @return [KGenericClass]
 */
inline fun Array<out KType>.generic(initiate: KClassConditions = {}): KGenericClass = this.findType(this.findClass(initiate).get()).generic()

/**
 * 获得当前实例的类操作对象 Kotlin版本
 * @param ignored 是否开启忽略错误警告功能 - 默认否
 * @return [KCurrentClass]
 */
inline fun <reified T : Any> T.currentKotlin(ignored: Boolean = false) =
    KCurrentClass(this.javaClass.kotlin, instance = this).apply { isIgnoreErrorLogs = ignored }

/**
 * 获得当前实例的类操作对象
 * @param ignored 是否开启忽略错误警告功能 - 默认否
 * @param initiate 方法体
 * @return [T]
 */
inline fun <reified T : Any> T.currentKotlin(ignored: Boolean = false, initiate: KCurrentClass.() -> Unit): T {
    currentKotlin(ignored).apply(initiate)
    return this
}

/**
 * 通过构造方法创建新实例 - 任意类型 [Any]
 * @param args 方法参数
 * @param initiate 查找方法体
 * @return [Any] or null
 */
inline fun KClass<*>.buildOf(vararg args: Any?, initiate: KConstructorConditions = {}) =
    constructor(initiate).get().call(*args)

/**
 * 通过构造方法创建新实例 - 指定类型 [T]
 * @param args 方法参数
 * @param initiate 查找方法体
 * @return [T] or null
 */
@JvmName(name = "buildOf_Generics")
inline fun <T> KClass<*>.buildOf(vararg args: Any?, initiate: KConstructorConditions = {}) =
    constructor(initiate).get().newInstance<T>(*args)

/**
 * 获取所有声明属性不包括父类
 */
inline val KClass<*>.declaredPropertys: Collection<KProperty<*>>
    get() = declaredMembers.filterIsInstance<KProperty<*>>()

/**
 * 获取当前 [KCallable] 是否为扩展方法
 */
inline val KCallable<*>.isExtension
    get() = descriptor?.extensionReceiverParameter != null

/**
 * 获取当前 [KCallable] 的描述信息
 */
inline val KCallable<*>.descriptor
    get() =
        KCallableImplKClass.java.getDeclaredMethod("getDescriptor").also { it.isAccessible = true }
            .invoke(if (KCallableImplKClass.isCase(this)) this else runCatching { ref }.getOrNull()?.impl ?: error("No descriptive implementation found!!!")
            ) as? CallableMemberDescriptor?


/**
 * 遍历当前类中的所有方法
 * @param isAccessible 是否强制设置成员为可访问类型 - 默认是
 * @param result 回调 - ([Int] 下标,[KFunction] 实例)
 */
@Suppress("DuplicatedCode")
inline fun KClass<*>.allFunctions(isAccessible: Boolean = true, result: (index: Int, function: KFunction<*>) -> Unit) =
    (if (KYukiReflection.Configs.isUseJvmObtainCallables) ((if (existTop) top.kotlin.java.declaredMethods else arrayOf()) + java.declaredMethods).asSequence()
        .mapNotNull { it.kotlin } else ((if (existTop) top.declaredTopFunctions else arrayListOf()) + declaredFunctions).asSequence()).forEachIndexed { p, it ->
        result(
            p,
            it.also { e -> e.isAccessible = isAccessible })
    }

/**
 * 遍历当前类中的所有方法签名
 * @param loader [ClassLoader] 方法参数 [KFunctionSignatureSupport.paramClasss] 所在的 [ClassLoader]
 * @param result 回调 - ([Int] 下标,Constructor [KFunction] 实例)
 */
inline fun KClass<*>.allFunctionSignatures(loader: ClassLoader? = null,result: (index: Int, function: KFunctionSignatureSupport) -> Unit){
    val nameResolver = memberScope?.deserializationContext?.nameResolver ?: return
    val protos = (memberScope?.impl ?: return).functionProtos
    mutableListOf<ProtoBuf.Function>().also { buf -> protos.values.forEach { buf += it } }.forEachIndexed { index, function ->
        result(index,KFunctionSignatureSupport(this, loader, nameResolver, function))
    }
}

/**
 * 遍历当前类中的所有构造方法
 * @param isAccessible 是否强制设置成员为可访问类型 - 默认是
 * @param result 回调 - ([Int] 下标,Constructor [KFunction] 实例)
 */
inline fun KClass<*>.allConstructors(isAccessible: Boolean = true, result: (index: Int, constructor: KFunction<*>) -> Unit) =
    (if (KYukiReflection.Configs.isUseJvmObtainCallables) java.declaredConstructors.asSequence()
        .mapNotNull { it.kotlin } else constructors.asSequence()).forEachIndexed { p, it ->
        result(
            p,
            it.also { e -> e.isAccessible = isAccessible })
    }

/**
 * 遍历当前类中的所有变量
 * @param isAccessible 是否强制设置成员为可访问类型 - 默认是
 * @param result 回调 - ([Int] 下标,[KProperty] 实例)
 */
@Suppress("DuplicatedCode")
inline fun KClass<*>.allPropertys(isAccessible: Boolean = true, result: (index: Int, property: KProperty<*>) -> Unit) =
    (if (KYukiReflection.Configs.isUseJvmObtainCallables) ((if (existTop) top.kotlin.java.declaredFields else arrayOf()) + java.declaredFields).asSequence()
        .mapNotNull { it.kotlin } else ((if (existTop) top.declaredTopPropertys else arrayListOf()) + declaredPropertys).asSequence()).forEachIndexed { p, it ->
        result(
            p,
            it.also { e -> e.isAccessible = isAccessible })
    }

/**
 * 遍历当前类中的所有变量签名
 * @param loader loader [ClassLoader] - 属性类型所使用的 [ClassLoader]
 * @param result 回调 - ([Int] 下标,[KProperty] 实例)
 */
inline fun KClass<*>.allPropertySignatures(loader: ClassLoader? = null,result: (index: Int, property: KPropertySignatureSupport) -> Unit){
    val nameResolver = memberScope?.deserializationContext?.nameResolver ?: return
    val protos = (memberScope?.impl ?: return).propertyProtos
    mutableListOf<ProtoBuf.Property>().also { buf -> protos.values.forEach { buf += it } }.forEachIndexed { index, property ->
        result(index, KPropertySignatureSupport(this, loader, nameResolver, property.getExtension(JvmProtoBuf.propertySignature)))
    }
}

/**
 * 查找并得到类
 * @param initiate 查找方法体
 * @return [KClassFinder.Result]
 */
@JvmName("findClass_class")
inline fun Collection<KClass<*>>.findClass(initiate: KClassConditions) =
    KClassFinder(classSet = this).apply(initiate).build()

/**
 * 查找并得到类
 * @param initiate 查找方法体
 * @return [KClassFinder.Result]
 */
@JvmName("findClass_class")
inline fun Array<out KClass<*>>.findClass(initiate: KClassConditions) =
    KClassFinder(classSet = this.toList()).apply(initiate).build()

/**
 * 查找并得到类
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @param loader [ClassLoader] 装载实例 - 默认空 - 不填使用默认 [ClassLoader]
 * @param initiate 查找方法体
 * @return [KClassFinder.Result]
 */
@JvmName("findClass_string")
inline fun Collection<String>.findClass(loader: ClassLoader? = null, initialize: Boolean = false, initiate: KClassConditions) =
    this.map { it.toKClass(loader, initialize) }.findClass(initiate)

/**
 * 查找并得到类
 * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
 * @param loader [ClassLoader] 装载实例 - 默认空 - 不填使用默认 [ClassLoader]
 * @param initiate 查找方法体
 * @return [KClassFinder.Result]
 */
@JvmName("findClass_string")
inline fun Array<String>.findClass(loader: ClassLoader? = null, initialize: Boolean = false, initiate: KClassConditions) =
    this.map { it.toKClass(loader, initialize) }.findClass(initiate)

/**
 * 查找并得到类
 * @param initiate 查找方法体
 * @return [KClassFinder.Result]
 */
@JvmName("findClass_type")
inline fun Collection<KType>.findClass(initiate: KClassConditions) =
    this.map { it.kotlin }.findClass(initiate)

/**
 * 查找并得到类
 * @param initiate 查找方法体
 * @return [KClassFinder.Result]
 */
@JvmName("findClass_type")
inline fun Array<out KType>.findClass(initiate: KClassConditions) =
    this.map { it.kotlin }.findClass(initiate)

/**
 * 查找一组[KType] 中符合 [classSet]的[KClass] 类型的[KType]并返回
 * @param classSet 与[KType]一致的[KClass]
 * @return [KClassFinder.Result]
 */
inline fun Collection<KType>.findType(classSet: KClass<*>?) =
    this.find { it.kotlin == classSet } ?: error("Didn't find with in [$this] [$classSet] The type of match.")

/**
 * 查找一组[KType] 中符合 [classSet]的[KClass] 类型的[KType]并返回
 * @param classSet 与[KType]一致的[KClass]
 * @return [KClassFinder.Result]
 */
inline fun Array<out KType>.findType(classSet: KClass<*>?) =
    this.find { it.kotlin == classSet } ?: error("Didn't find with in [$this] [$classSet] The type of match.")

/**
 * 查找一组[KType] 中符合 [T]的[KClass] 类型的[KType]并返回
 * @param T 与[KType]一致的[KClass]
 * @return [KClassFinder.Result]
 */
@JvmName(name = "type_Generics")
inline fun <reified T> Collection<KType>.findType() =
    this.find { it.kotlin == T::class } ?: error("Didn't find with in [$this] [${T::class}] The type of match.")

/**
 * 查找一组[KType] 中符合 [T]的[KClass] 类型的[KType]并返回
 * @param T 与[KType]一致的[KClass]
 * @return [KClassFinder.Result]
 */
@JvmName(name = "type_Generics")
inline fun <reified T> Array<out KType>.findType() =
    this.find { it.kotlin == T::class } ?: error("Didn't find with in [$this] [${T::class}] The type of match.")

/**
 * 将 [KFunction] 转换为 [KFunctionFinder.Result.Instance] 可执行类
 *
 * @param thisRef 执行所使用this对象
 * @param extensionRef 函数如果是拓展函数你还需要传入拓展函数的this对象
 * @param isUseMember 是否优先将函数转换Java方式执行
 */
inline fun KFunction<*>.instance(thisRef: Any? = null, extensionRef: Any? = null, isUseMember: Boolean = false) =
    KFunctionFinder().Result().Instance(thisRef, this).receiver(extensionRef).useMember(isUseMember)

/**
 * 将 Constructor[KFunction] 转换为 [KConstructorFinder.Result.Instance] 可执行类
 *
 * @param isUseMember 是否优先将属性转换Java方式进行get/set
 */
inline fun KFunction<*>.constructorInstance(isUseMember: Boolean = false) =
    KConstructorFinder().Result().Instance(this).useMember(isUseMember)

/**
 * 将 [KProperty] 转换为 [KPropertyFinder.Result.Instance] 可执行类
 *
 * @param thisRef 执行所使用this对象
 * @param extensionRef 属性如果是拓展属性你还需要传入拓展属性的this对象
 * @param isUseMember 是否优先将属性转换Java方式进行get/set
 */
inline fun KProperty<*>.instance(thisRef: Any? = null, extensionRef: Any? = null, isUseMember: Boolean = false) =
    KPropertyFinder().Result().Instance(thisRef, this).receiver(extensionRef).useMember(isUseMember)

/**
 * 将此构造函数相关内容附加到此查找器
 *
 * 将影响[KConstructorFinder.param]
 *
 * @param R 返回类型/构造目标类的类型
 * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
 * @param isUseMember 是否将构造函数转换为JavaMethod再进行附加 - 即使为false当构造函数附加错误时依然会尝试JavaMethod - 为true时会导致类型擦除
 */
fun <R> KConstructorFinder.attach(function: KFunction<R>,loader: ClassLoader? = null,isUseMember:Boolean = false){
    fun KClass<*>.toClass() = if (loader == null) this else toKClass(loader)

    fun attachMember(e:Throwable? = null){
        val method = function.javaConstructorNoError ?: function.refImpl?.javaConstructorNoError ?: let {
            errorMsg("This constructor cannot be attached, or the constructor is hidden by Kotlin to prohibit reflection, you can try to use [java.constructor {}] to do the reflection !!!", e)
            return
        }
        if (method.parameterTypes.isEmpty())
            emptyParam()
        else
            param(*method.parameterTypes.map { it.kotlin.toClass() }.toTypedArray())
    }
    fun attachCallable(function: KFunction<*>){
        if (function.valueParameters.isEmpty())
            emptyParam()
        else
            param(*function.valueParameters.map {
                if (loader != null)
                    it.kotlin.toClass()
                else it
            }.toTypedArray())
    }
    if (isUseMember)
        attachMember()
    else runCatching {
        attachCallable(function)
    }.getOrNull() ?: runCatching {
        attachCallable(function.refImpl!!)
    }.getOrElse {
        attachMember(it)
    }
}

/**
 * 将此属性相关内容附加到此查找器
 *
 * 将影响[KPropertyFinder.name]、[KPropertyFinder.type]
 *
 * 多个属性引用使用示例 ↓
 *
 * ```kotlin
 *
 *  class Main{
 *      var sub = ""
 *      companion object{
 *          var sub = 5
 *      }
 *  }
 *
 *  attach(Main::sub) // error:不知道附加哪个属性伴生对象情况下优先使用var sub = ""
 *  attach<Int>(Main::sub) // 将使用返回类型为Int的属性也就是伴生对象中的属性
 * ```
 *
 * @param R 属性类型
 * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
 * @param isUseMember 是否将属性转换为JavaField再进行附加 - 即使为false当属性附加错误时依然会尝试JavaField - 为true时会导致类型擦除
 */
fun <R> KPropertyFinder.attach(function: KProperty<R>,loader: ClassLoader? = null,isUseMember:Boolean = false){
    fun KClass<*>.toClass() = if (loader == null) this else toKClass(loader)

    fun attachMember(e:Throwable? = null){
        val member = function.javaMember ?: function.refImpl?.javaMember ?: let {
            errorMsg("This property cannot be attached, or it is hidden by Kotlin to prohibit reflection, you can try to use [field {}] to reflect it !!!", e)
            return
        }
        this@attach.name = member.name
        this@attach.type = when(member){
            is Field -> member.type.kotlin.toClass()
            is Method -> member.returnType.kotlin.toClass()
            else -> null
        }
    }
    fun attachCallable(property: KProperty<*>){
        this@attach.name = property.name
        this@attach.type = runCatching {
            if (loader != null)
                property.returnClass.toClass()
            else
                property.returnType
        }.getOrNull() ?: property.returnClass.toClass()
    }
    if (isUseMember)
        attachMember()
    else runCatching {
        attachCallable(function)
    }.getOrNull() ?: runCatching {
        attachCallable(function.refImpl!!)
    }.getOrElse {
        attachMember(it)
    }
}

/**
 * 将此属性相关内容附加到此查找器
 *
 * 将影响[KPropertyFinder.name]、[KPropertyFinder.type]
 *
 * @param R 属性类型
 * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
 * @param isUseMember 是否将属性转换为JavaField再进行附加 - 即使为false当属性附加错误时依然会尝试JavaField - 为true时会导致类型擦除
 */
inline fun <R> KPropertyFinder.attachStatic(function: KProperty0<R>,loader: ClassLoader? = null, isUseMember:Boolean = false){
    attach(function,loader,isUseMember)
}

/**
 * 将此属性相关内容附加到此查找器
 *
 * 将影响[KPropertyFinder.name]、[KPropertyFinder.type]
 *
 * @param ExpandThis 拓展类的类型
 * @param R 属性类型
 * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
 * @param isUseMember 是否将属性转换为JavaField再进行附加 - 即使为false当属性附加错误时依然会尝试JavaField - 为true时会导致类型擦除
 */
inline fun <ExpandThis,R> KPropertyFinder.attach(function: KProperty2<*,ExpandThis,R>,loader: ClassLoader? = null, isUseMember:Boolean = false){
    attach<R>(function,loader,isUseMember)
}

/**
 * 委托绑定到指定类的相同特征的属性
 *
 * 假设thisRefClass Test中有以下属性 ↓
 *
 * ```kotlin
 * class Test{
 *  var test:String? = null
 * }
 * ```
 *
 * 在你的自定义类中使用以下方式简单映射绑定上 ↓
 *
 * ```kotlin
 * var test:String? by BindingInstanceSupport.Nullable(Test::class,Test())
 * test = "hello"
 * ```
 *
 * 这适用于类和实例无法直接在代码中调用时的解决方案
 *
 * 通过此方法你可以快速为某个类进行特征映射，当然需要遵循一定的映射规则
 *
 * 过于复杂的映射规则，您可以尝试手动find查找而不是选择委托绑定
 *
 * @param T 映射属性的值类型
 * @property thisRefClass 映射的属性所在的类
 * @property thisRef 映射的属性调用时所需的实例 如果为null并且被委托字段的是拓展字段并且拓展的this属于[thisRefClass]则使用拓展属性的this，否则为null
 * @property extensionRef 映射属性如果是拓展属性所需的拓展实例
 * @property isUseMember 是否优先将属性转换Java方式进行get/set 默认不使用
 * @property isLazy 是否只加载一次[KPropertyFinder.Result.Instance] 默认是 否则每次get/set都将重新查找
 * @property mappingRules 属性映射规则 默认匹配名称和返回类型 [KType]
 */
open class BindingInstanceSupport<T>(
    private val thisRefClass: KClass<*>,
    private var thisRef: Any? = null,
    private val extensionRef: Any? = null,
    private val isUseMember: Boolean = false,
    private val isLazy: Boolean = true,
    private val mappingRules: KPropertyFinder.(property: KProperty<*>) -> Unit = {
        this.name = it.name
        this.type = it.returnType
    }
) {
    private var lazy: WeakReference<KPropertyFinder.Result>? = null

    /**
     * 获取非空实例-get
     * @return T
     */
    internal fun nonNullGet(property: KProperty<*>): T {
        return nullableGet(property) ?: error("The non-null attribute results in an empty error.")
    }

    /**
     * 获取可空实例-get
     * @return T or null
     */
    internal fun nullableGet(property: KProperty<*>): T? {
        return if (isLazy) {
            if (lazy == null)
                lazy = WeakReference(thisRefClass.property {
                    mappingRules(property)
                })
            lazy?.get()?.get(thisRef, extensionRef, isUseMember)?.cast<T>()
        } else thisRefClass.property {
            mappingRules(property)
        }.get(thisRef, extensionRef, isUseMember).cast()
    }

    /**
     * 获取非空实例-set
     * @return T
     */
    internal fun nonNullSet(property: KProperty<*>, value: T) {
        return nullableSet(property, value)
    }

    /**
     * 获取可空实例-set
     */
    internal fun nullableSet(property: KProperty<*>, value: T?) {
        if (isLazy) {
            if (lazy == null)
                lazy = WeakReference(thisRefClass.property {
                    mappingRules(property)
                })
            lazy?.get()?.get(thisRef, extensionRef, isUseMember)?.set(value)
        } else {
            thisRefClass.property {
                mappingRules(property)
            }.get(thisRef, extensionRef, isUseMember).set(value)
        }
    }

    protected fun verifyReferences(thisRef: Any?):BindingInstanceSupport<T>{
        if (this.thisRef != null || thisRef == null || thisRef::class notExtends thisRefClass)return this
        this.thisRef = thisRef
        return this
    }

    /**
     * 非空实例
     * @param T 映射属性的值类型
     * @property thisRefClass 映射的属性所在的类
     * @property thisRef 映射的属性调用时所需的实例 如果为null并且被委托字段的是拓展字段并且拓展的this属于[thisRefClass]则使用拓展属性的this，否则为null
     * @property extensionRef 映射属性如果是拓展属性所需的拓展实例
     * @property isUseMember 是否优先将属性转换Java方式进行get/set 默认不使用
     * @property isLazy 是否只加载一次[KPropertyFinder.Result.Instance] 默认是 否则每次get/set都将重新查找
     * @property mappingRules 属性映射规则 默认匹配名称和返回类型 [KType]
     */
    class NonNull<T> internal constructor(
        private val thisRefClass: KClass<*>,
        private val thisRef: Any? = null,
        private val extensionRef: Any? = null,
        private val isUseMember: Boolean = false,
        private val isLazy: Boolean = true,
        private val mappingRules: KPropertyFinder.(property: KProperty<*>) -> Unit = {
            this.name = it.name
            this.type = it.returnType
        }
    ) : BindingInstanceSupport<T>(thisRefClass, thisRef, extensionRef, isUseMember, isLazy, mappingRules) {

        operator fun getValue(thisRef: Any?, property: KProperty<*>) = verifyReferences(thisRef).nonNullGet(property)
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = verifyReferences(thisRef).nonNullSet(property, value)
    }

    /**
     * 可空实例
     * @param T 映射属性的值类型
     * @property thisRefClass 映射的属性所在的类
     * @property thisRef 映射的属性调用时所需的实例 如果为null并且被委托字段的是拓展字段并且拓展的this属于[thisRefClass]则使用拓展属性的this，否则为null
     * @property extensionRef 映射属性如果是拓展属性所需的拓展实例
     * @property isUseMember 是否优先将属性转换Java方式进行get/set 默认不使用
     * @property isLazy 是否只加载一次[KPropertyFinder.Result.Instance] 默认是 否则每次get/set都将重新查找
     * @property mappingRules 属性映射规则 默认匹配名称和返回类型 [KType]
     */
    class Nullable<T> internal constructor(
        private val thisRefClass: KClass<*>,
        private val thisRef: Any? = null,
        private val extensionRef: Any? = null,
        private val isUseMember: Boolean = false,
        private val isLazy: Boolean = true,
        private val mappingRules: KPropertyFinder.(property: KProperty<*>) -> Unit = {
            this.name = it.name
            this.type = it.returnType
        }
    ) : BindingInstanceSupport<T>(thisRefClass, thisRef, extensionRef, isUseMember, isLazy, mappingRules) {

        operator fun getValue(thisRef: Any?, property: KProperty<*>) = verifyReferences(thisRef).nullableGet(property)
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) = verifyReferences(thisRef).nullableSet(property, value)
    }
}

/**
 * 委托绑定到指定类的相同特征的属性 非空实例
 *
 * 假设thisRefClass Test中有以下属性 ↓
 *
 * ```kotlin
 * class Test{
 *  var test:String = "otr"
 * }
 * ```
 *
 * 在你的自定义类中使用以下方式简单映射绑定上 ↓
 *
 * ```kotlin
 * var test:String by Test::class.bindProperty(Test())
 * test = "hello"
 * ```
 *
 * 这适用于类和实例无法直接在代码中调用时的解决方案
 *
 * 通过此方法你可以快速为某个类进行特征映射，当然需要遵循一定的映射规则
 *
 * 过于复杂的映射规则，您可以尝试手动find查找而不是选择委托绑定
 *
 * @param T 映射属性的值类型
 * @property BindingInstanceSupport.thisRefClass 映射的属性所在的类
 * @param thisRef 映射的属性调用时所需的实例 如果为null并且被委托字段的是拓展字段并且拓展的this属于[KClass]则使用拓展属性的this，否则为null
 * @param extensionRef 映射属性如果是拓展属性所需的拓展实例
 * @param isUseMember 是否优先将属性转换Java方式进行get/set 默认不使用
 * @param isLazy 是否只加载一次[KPropertyFinder.Result.Instance] 默认是 否则每次get/set都将重新查找
 * @param mappingRules 属性映射规则 默认匹配名称和返回类型 [KType]
 */
inline fun <T> KClass<*>.bindProperty(
    thisRef: Any? = null,
    extensionRef: Any? = null,
    isUseMember: Boolean = false,
    isLazy: Boolean = true,
    noinline mappingRules: KPropertyFinder.(property: KProperty<*>) -> Unit = {
        this.name = it.name
        this.type = it.returnType
    }
) = BindingInstanceSupport.NonNull<T>(this, thisRef, extensionRef, isUseMember, isLazy, mappingRules)

/**
 * 委托绑定到指定类的相同特征的属性 可空实例
 *
 * 假设thisRefClass Test中有以下属性 ↓
 *
 * ```kotlin
 * class Test{
 *  var test:String? = null
 * }
 * ```
 *
 * 在你的自定义类中使用以下方式简单映射绑定上 ↓
 *
 * ```kotlin
 * var test:String? by Test::class.bindPropertyOrNull(Test())
 * test = "hello"
 * ```
 *
 * 这适用于类和实例无法直接在代码中调用时的解决方案
 *
 * 通过此方法你可以快速为某个类进行特征映射，当然需要遵循一定的映射规则
 *
 * 过于复杂的映射规则，您可以尝试手动find查找而不是选择委托绑定
 *
 * @param T 映射属性的值类型
 * @property BindingInstanceSupport.thisRefClass 映射的属性所在的类
 * @param thisRef 映射的属性调用时所需的实例 如果为null并且被委托字段的是拓展字段并且拓展的this属于[KClass]则使用拓展属性的this，否则为null
 * @param extensionRef 映射属性如果是拓展属性所需的拓展实例
 * @param isUseMember 是否优先将属性转换Java方式进行get/set 默认不使用
 * @param isLazy 是否只加载一次[KPropertyFinder.Result.Instance] 默认是 否则每次get/set都将重新查找
 * @param mappingRules 属性映射规则 默认匹配名称和返回类型 [KType]
 */
inline fun <T> KClass<*>.bindPropertyOrNull(
    thisRef: Any? = null,
    extensionRef: Any? = null,
    isUseMember: Boolean = false,
    isLazy: Boolean = true,
    noinline mappingRules: KPropertyFinder.(property: KProperty<*>) -> Unit = {
        this.name = it.name
        this.type = it.returnType
    }
) = BindingInstanceSupport.Nullable<T>(this, thisRef, extensionRef, isUseMember, isLazy, mappingRules)

/**
 * 复制自Kotlin ReflectJvmMapping文件
 *
 * 根据 [Method] 在批量 [KCallable] 中找到与之对应的Kotlin [KFunction]
 *
 * @param method Jvm [Method]
 * @return Kotlin [KFunction]
 */
inline fun Collection<KCallable<*>>.findKFunction(method: Method): KFunction<*>? {
    // As an optimization, try to search among functions with the same name first, and then among the rest of functions.
    // This is needed because a function's JVM name might be different from its Kotlin name (because of `@JvmName`, inline class mangling,
    // internal visibility, etc).
    for (callable in this) {
        if (callable is KFunction<*> && callable.name == method.name && callable.javaMethodNoError == method) return callable
    }
    for (callable in this) {
        if (callable is KFunction<*> && callable.name != method.name && callable.javaMethodNoError == method) return callable
    }
    return null
}

/**
 * 复制自Kotlin ReflectJvmMapping文件
 *
 * 根据 [Field] 在批量 [KCallable] 中找到与之对应的Kotlin [KProperty]
 *
 * @param field Jvm [Field]
 * @return Kotlin [KProperty]
 */
inline fun Collection<KCallable<*>>.findKProperty(field: Field): KProperty<*>? {
    for (callable in this) {
        if (callable is KProperty<*> && callable.name == field.name && callable.javaFieldNoError == field) return callable
    }
    for (callable in this) {
        if (callable is KProperty<*> && callable.name != field.name && callable.javaFieldNoError == field) return callable
    }
    return null
}