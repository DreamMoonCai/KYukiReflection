@file:Suppress("UnusedImport", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE","MISSING_DEPENDENCY_SUPERCLASS", "NOTHING_TO_INLINE")

package io.github.dreammooncai.yukireflection.factory

import io.github.dreammooncai.yukireflection.finder.callable.KFunctionFinder
import io.github.dreammooncai.yukireflection.finder.signature.support.KPropertySignatureSupport
import io.github.dreammooncai.yukireflection.type.factory.KFunctionConditions
import io.github.dreammooncai.yukireflection.type.factory.KFunctionSignatureConditions
import io.github.dreammooncai.yukireflection.type.factory.KPropertyConditions
import io.github.dreammooncai.yukireflection.type.kotlin.DeserializedMemberScope_OptimizedImplementationKClass
import io.github.dreammooncai.yukireflection.type.kotlin.KClassImplKClass
import io.github.dreammooncai.yukireflection.utils.factory.lazyDomain
import java.io.ByteArrayInputStream
import java.lang.reflect.*
import kotlin.reflect.*
import kotlin.reflect.jvm.*
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf
import kotlin.reflect.jvm.internal.impl.name.Name
import kotlin.reflect.jvm.internal.impl.serialization.deserialization.DeserializationContext
import kotlin.reflect.jvm.internal.impl.serialization.deserialization.descriptors.DeserializedMemberScope

/**
 * 检查前缀是否匹配 并且前缀后的第一个首字母大写
 *
 * 意味检测驼峰命名
 *
 *     如: "setAbc".checkPrefix("set") --- true
 *        "setabc".checkPrefix("set") --- false
 *
 * @param prefix 需要检测的前缀
 */
internal fun String.checkHump(prefix: String) =
    startsWith(prefix) && this[prefix.length].isUpperCase()

/**
 * 删除前缀/删除驼峰前缀
 *
 * 被删除后首字母转换小写
 *
 *     如: "setAbc".removeHump("set") --- "abc"
 *        "setabc".removeHump("set") --- "setabc"
 *
 * @param prefix 需要删除的前缀
 */
internal fun String.removeHump(prefix: String) =
    if (checkHump(prefix)) substring(prefix.length).lowercaseOf() else this

/**
 * 增加前缀/增加驼峰前缀
 *
 * 被增加前的首字母转换大写
 *
 *     如: "Abc".addHump("set") --- "setAbc"
 *        "abc".addHump("set") --- "setAbc"
 *
 * @param prefix 需要增加的前缀
 */
internal fun String.addHump(prefix: String)=
    if (!checkHump(prefix)) prefix + this.uppercaseOf() else this

/**
 * 指定下标字母转换小写
 *
 *     如: "abc".lowercaseOf(0) --- "abc"
 *        "Abc".lowercaseOf(0) --- "abc"
 *        "Abc".lowercaseOf(1) --- "Abc"
 */
fun String.lowercaseOf(index: Int = 0) =
    StringBuffer(this).also { it.setCharAt(index, this[index].lowercaseChar()) }.toString()

/**
 * 指定下标字母转换大写
 *
 *     如: "abc".uppercaseOf(0) --- "Abc"
 *        "Abc".uppercaseOf(0) --- "Abc"
 *        "Abc".uppercaseOf(1) --- "ABc"
 */
fun String.uppercaseOf(index: Int = 0) =
    StringBuffer(this).also { it.setCharAt(index, this[index].uppercaseChar()) }.toString()

// Kotlin reflection -> Java reflection

/**
 * Returns a Java [Field] instance corresponding to the backing field of the given property,
 * or `null` if the property has no backing field.
 *
 * - 复制自 [KProperty.javaField] 忽略其可能遇到的所有错误，如签名解析失败
 */
val KProperty<*>.javaFieldNoError: Field? by lazyDomain {
    runCatching { javaField }.getOrNull()
}

/**
 * 获取属性的Java字段表示方式
 *
 * 没有获取到时使用签名方式获取
 */
val KProperty<*>.javaSignatureField: Field? by lazyDomain {
    javaFieldNoError ?: refClass?.let { signature(it).give()?.fieldOrNull?.memberOrNull }
}

/**
 * Returns a Java [Method] instance corresponding to the getter of the given property,
 * or `null` if the property has no getter, for example in isCase of a simple private `val` in a class.
 *
 * - 复制自 [KProperty.javaGetter] 忽略其可能遇到的所有错误，如签名解析失败
 */
val KProperty<*>.javaGetterNoError: Method? by lazyDomain {
    runCatching { javaGetter }.getOrNull()
}

/**
 * 获取属性的Java getter表示方式
 *
 * 没有获取到时使用签名方式获取
 */
val KProperty<*>.javaSignatureGetter: Method? by lazyDomain {
    javaGetterNoError ?: refClass?.let { signature(it).give()?.getterOrNull?.memberOrNull }
}

/**
 * Returns a Java [Method] instance corresponding to the setter of the given mutable property,
 * or `null` if the property has no setter, for example in isCase of a simple private `var` in a class.
 *
 * - 复制自 [KMutableProperty.javaSetter] 忽略其可能遇到的所有错误，如签名解析失败
 */
val KMutableProperty<*>.javaSetterNoError: Method? by lazyDomain {
    runCatching { javaSetter }.getOrNull()
}

/**
 * 获取属性的Java setter表示方式
 *
 * 没有获取到时使用签名方式获取
 */
val KMutableProperty<*>.javaSignatureSetter: Method? by lazyDomain { javaSetterNoError ?: refClass?.let { signature(it).give()?.setterOrNull?.memberOrNull } }

/**
 * Returns a Java [Method] instance corresponding to the given Kotlin function,
 * or `null` if this function is a constructor or cannot be represented by a Java [Method].
 *
 * - 复制自 [KFunction.javaMethod] 忽略其可能遇到的所有错误，如签名解析失败
 */
val KFunction<*>.javaMethodNoError: Method? by lazyDomain { runCatching { javaMethod }.getOrNull() }

/**
 * 获取函数的Java 方法表示方式
 *
 * 没有获取到时使用签名方式获取
 */
val KFunction<*>.javaSignatureMethod: Method? by lazyDomain { javaMethodNoError ?: refClass?.let { signature(it).give()?.memberOrNull } }

/**
 * Returns a Java [Constructor] instance corresponding to the given Kotlin function,
 * or `null` if this function is not a constructor or cannot be represented by a Java [Constructor].
 *
 * - 复制自 [KFunction.javaConstructor] 忽略其可能遇到的所有错误，如签名解析失败
 */
inline val <T> KFunction<T>.javaConstructorNoError: Constructor<T>?
    get() = runCatching { javaConstructor }.getOrNull()

/**
 * 获取属性/函数 [KCallable] 的Java成员
 *
 * @return [Member] or null
 */
val KCallable<*>.javaMember:Member? by lazyDomain {
    when (this) {
        is KProperty -> javaFieldNoError ?: javaGetterNoError ?: (this as? KMutableProperty<*>?)?.javaSetterNoError
        is KFunction -> javaMethodNoError ?: javaConstructorNoError
        else -> null
    }
}

/**
 * 获取字段/函数的Java 字段/getter/setter/方法表示方式
 *
 * 没有获取到时使用签名方式获取
 */
val KCallable<*>.javaSignatureMember: Member? by lazyDomain {
    when (this) {
        is KProperty -> javaSignatureField ?: javaSignatureGetter ?: (this as? KMutableProperty<*>?)?.javaSignatureSetter
        is KFunction -> javaSignatureMethod ?: javaConstructorNoError
        else -> null
    }
}


/**
 * 当前 [KClass] 的成员域
 *
 * 存放所有相关 [Metadata] 的签名域
 *
 * @return [DeserializedMemberScope]
 */
val KClass<*>.memberScope by lazyDomain { KClassImplKClass.java.declaredMethods.find { it.name.contains("getMemberScope") }?.invoke(this) as? DeserializedMemberScope? }

/**
 * 当前 [DeserializedMemberScope] 的序列化上下文
 *
 * 存放 [Metadata] 序列化相关信息
 *
 * @return [DeserializationContext]
 */
val DeserializedMemberScope.deserializationContext by lazyDomain { DeserializedMemberScope::class.property { name = "c" }.get(this).cast<DeserializationContext>() }

/**
 * 当前 [DeserializedMemberScope] 的签名数据存储实现
 *
 * 存放 [Metadata] 签名数据相关信息
 *
 * ! 其不支持 NoReorderImplementation 实现将参阅使用 [OptimizedImplementationSupport]
 *
 * @return [OptimizedImplementationSupport]
 */
val DeserializedMemberScope.impl by lazyDomain { OptimizedImplementationSupport(this, DeserializedMemberScope::class.property { name = "impl" }.get(this).any()) }

/**
 * 经过Kotlin 编译器优化生成的 [Metadata] 获取签名数据存储实现
 *
 * @param impl [DeserializedMemberScope.OptimizedImplementation] 内对象因私有原因使用KYuki进行映射
 */
class OptimizedImplementationSupport(private val scope: DeserializedMemberScope, impl: Any?){
    val functionProtosBytes:Map<Name, ByteArray> by DeserializedMemberScope_OptimizedImplementationKClass.bindProperty(impl)

    val propertyProtosBytes:Map<Name, ByteArray> by DeserializedMemberScope_OptimizedImplementationKClass.bindProperty(impl)

    val typeAliasBytes:Map<Name, ByteArray> by DeserializedMemberScope_OptimizedImplementationKClass.bindProperty(impl)

    val functionProtos by lazy {
        functionProtosBytes.mapValues {
            val inputStream = ByteArrayInputStream(it.value)
            generateSequence {
                ProtoBuf.Function.PARSER.parseDelimitedFrom(inputStream, scope.deserializationContext?.components?.extensionRegistryLite)
            }.toList()
        }
    }

    val propertyProtos by lazy {
        propertyProtosBytes.mapValues {
            val inputStream = ByteArrayInputStream(it.value)
            generateSequence {
                ProtoBuf.Property.PARSER.parseDelimitedFrom(inputStream, scope.deserializationContext?.components?.extensionRegistryLite)
            }.toList()
        }
    }

    val typeAliasProtos by lazy {
        typeAliasBytes.mapValues {
            val inputStream = ByteArrayInputStream(it.value)
            generateSequence {
                ProtoBuf.TypeAlias.PARSER.parseDelimitedFrom(inputStream, scope.deserializationContext?.components?.extensionRegistryLite)
            }.toList()
        }
    }
}

/**
 * 与 [KClass.propertySignature] 一致的快捷方法
 *
 * 获取指定 [declaringClass] 指定 [initiate] 条件的签名
 *
 * 此方法以通过 [Metadata] 中定义的属性名获取Java层真正的签名
 *
 * [KPropertyConditions] 中对属性类型进行筛选如果目标类型也有问题可能依然会出错，建议使用属性名筛选
 *
 * - 此方法不涉及转 Kotlin 的反射属性可以避免一些异常 [Metadata] 数据报错
 * @param declaringClass 属性所在类
 * @param loader [ClassLoader] 相关涉及的类型所在的 [ClassLoader] - 不填使用 [declaringClass] [KClass.classLoader]
 * @param isUseMember 是否将属性转换为JavaField再进行附加 - 即使为false当属性附加错误时依然会尝试JavaField - 为true时会导致类型擦除
 * @param initiate 条件方法体
 * @return [KPropertySignatureSupport] or null - 找不到返回null
 */
inline fun KProperty<*>.signature(declaringClass: KClass<*>? = this.declaringClass,loader: ClassLoader? = declaringClass?.classLoader,isUseMember: Boolean = false, noinline initiate: KPropertyConditions = { this@signature.attach(loader,isUseMember) }) = declaringClass?.propertySignature(loader,initiate) ?: error("This property does not get the declaring class or the declaring class you specified is empty.")

/**
 * 与 [KClass.functionSignature] 一致的快捷方法
 *
 * 获取指定 [declaringClass] 指定 [initiate] 条件的签名
 *
 * 此方法以通过 [Metadata] 中定义的函数名获取Java层真正的签名
 *
 * [KFunctionConditions] 中对返回类型和参数类型进行筛选如果目标类型也有问题可能依然会出错，建议使用参数名筛选
 *
 * - 此方法不涉及转 Kotlin 的反射函数可以避免一些异常 [Metadata] 数据报错
 * @param declaringClass 属性所在类
 * @param loader [ClassLoader] 相关涉及的类型所在的 [ClassLoader] - 不填使用 [declaringClass] [KClass.classLoader]
 * @param isUseMember 是否将属性转换为JavaField再进行附加 - 即使为false当属性附加错误时依然会尝试JavaField - 为true时会导致类型擦除
 * @param initiate 条件方法体
 * @return [KFunctionFinder.Result] or null - 找不到返回null
 */
inline fun KFunction<*>.signature(declaringClass: KClass<*>? = this.declaringClass,loader: ClassLoader? = declaringClass?.classLoader,isUseMember: Boolean = false, noinline initiate: KFunctionSignatureConditions = { this@signature.attach(loader,isUseMember) }) = declaringClass?.functionSignature(loader,initiate) ?: error("This function does not get the declaring class or the declaring class you specified is empty.")

