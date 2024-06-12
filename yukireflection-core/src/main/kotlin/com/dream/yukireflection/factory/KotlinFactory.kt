@file:Suppress("UnusedImport","MISSING_DEPENDENCY_SUPERCLASS", "NOTHING_TO_INLINE")

package com.dream.yukireflection.factory

import com.dream.yukireflection.type.kotlin.DeserializedMemberScope_OptimizedImplementationKClass
import com.dream.yukireflection.type.kotlin.KClassImplKClass
import com.dream.yukireflection.utils.DexSignUtil
import java.io.ByteArrayInputStream
import java.lang.reflect.*
import kotlin.reflect.*
import kotlin.jvm.internal.Reflection
import kotlin.reflect.jvm.*
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf
import kotlin.reflect.jvm.internal.impl.metadata.deserialization.NameResolver
import kotlin.reflect.jvm.internal.impl.metadata.jvm.JvmProtoBuf
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
fun String.checkHump(prefix: String) =
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
fun String.removeHump(prefix: String) =
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
fun String.addHump(prefix: String)=
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

/**
 * 通过 [Member] 分析签名构建 [KProperty] 或 [KFunction]
 *
 * 签名分析或许不一定能正常转换 参阅或使用[Member.kotlinCallable]
 */
inline val Member.kotlin get() = when (this) {
    is Field -> kotlin
    is Method -> kotlin
    is Constructor<*> -> kotlin
    else -> error("Unsupported member type: $this")
}

/**
 * 返回与给定 Java [Member] 实例相对应的[KCallable]实例，或者null如果此字段不能由 Kotlin 可执行属性表示（例如，如果它是合成属性）。
 */
inline val Member.kotlinCallable get() = when (this) {
    is Field -> kotlinProperty
    is Method -> kotlinFunction
    is Constructor<*> -> kotlinFunction
    else -> error("Unsupported member type: $this")
}

/**
 * 获取 [Member] 的返回类型
 *
 * [Field] ---> [Field.type]
 *
 * [Method] ---> [Method.returnType]
 *
 * [Constructor] ---> [Member.getDeclaringClass]
 */
inline val Member.returnType: Class<out Any> get() = when (this) {
    is Field -> type
    is Method -> returnType
    is Constructor<*> -> declaringClass
    else -> error("Unsupported member type: $this")
}

/**
 * 通过 [Field] 分析签名构建 [KProperty]
 *
 * 签名分析或许不一定能正常转换 参阅或使用[Field.kotlinProperty]
 */
inline val Field.kotlin: KProperty<*> get() = when{
    Modifier.isStatic(modifiers) -> Reflection.mutableProperty0(KReference.mutablePropertyStatic(this))
    else -> Reflection.mutableProperty1(KReference.mutableProperty(this))
}

/**
 * 通过 [Method] 分析签名构建 [KFunction]
 *
 * 签名分析或许不一定能正常转换 参阅或使用[Method.kotlinFunction]
 */
inline val Method.kotlin: KFunction<*> get() = Reflection.function(KReference.function(this))

/**
 * 通过 [Constructor] 分析签名构建 [KFunction]
 *
 * 签名分析或许不一定能正常转换 参阅或使用[Constructor.kotlinFunction]
 */
inline val Constructor<*>.kotlin: KFunction<*> get() = Reflection.function(KReference.function(this))

/**
 * 获取此 [Field] 在 Kotlin 常用的简单签名
 *
 *     如: int abc = * --> "getAbc()I"
 *        val abc = listOf(1, 2, 3) --> "getAbc()Ljava/util/List;"
 */
val Field.kotlinSimpleSignature get() = name.addHump(if (type.kotlin == Boolean::class) "is" else "get") + "()" + DexSignUtil.getTypeSign(type)

/**
 * 获取此 [Method] 在 Kotlin 常用的简单签名
 *
 *     如: int abc(int a, int b) --> "abc(II)I"
 *        fun abc(a:List<*>) --> "abc(Ljava/util/List;)V"
 */
val Method.kotlinSimpleSignature get() = name + DexSignUtil.getMethodSign(this)

/**
 * 获取此 [Constructor] 在 Kotlin 常用的简单签名
 *
 *     如: class abc(int a, int b) --> "<init>(II)V"
 *        class abc(a:List<*>) --> "<init>(Ljava/util/List;)V"
 */
val Constructor<*>.kotlinSimpleSignature get() = name + DexSignUtil.getConstructorSign(this)

// Kotlin reflection -> Java reflection

/**
 * Returns a Java [Field] instance corresponding to the backing field of the given property,
 * or `null` if the property has no backing field.
 *
 * - 复制自 [KProperty.javaField] 忽略其可能遇到的所有错误，如签名解析失败
 */
inline val KProperty<*>.javaFieldNoError: Field?
    get() = runCatching { javaField }.getOrNull()

/**
 * Returns a Java [Method] instance corresponding to the getter of the given property,
 * or `null` if the property has no getter, for example in case of a simple private `val` in a class.
 *
 * - 复制自 [KProperty.javaGetter] 忽略其可能遇到的所有错误，如签名解析失败
 */
inline val KProperty<*>.javaGetterNoError: Method?
    get() = runCatching { javaGetter }.getOrNull()

/**
 * Returns a Java [Method] instance corresponding to the setter of the given mutable property,
 * or `null` if the property has no setter, for example in case of a simple private `var` in a class.
 *
 * - 复制自 [KMutableProperty.javaSetter] 忽略其可能遇到的所有错误，如签名解析失败
 */
inline val KMutableProperty<*>.javaSetterNoError: Method?
    get() = runCatching { javaSetter }.getOrNull()


/**
 * Returns a Java [Method] instance corresponding to the given Kotlin function,
 * or `null` if this function is a constructor or cannot be represented by a Java [Method].
 *
 * - 复制自 [KFunction.javaMethod] 忽略其可能遇到的所有错误，如签名解析失败
 */
inline val KFunction<*>.javaMethodNoError: Method?
    get() = runCatching { javaMethod }.getOrNull()

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
inline val KCallable<*>.javaMember:Member? get() = when (this) {
    is KProperty -> javaFieldNoError ?: javaGetterNoError ?: (this as? KMutableProperty<*>?)?.javaSetterNoError
    is KFunction -> javaMethodNoError ?: javaConstructorNoError
    else -> null
}


/**
 * 当前 [KClass] 的成员域
 *
 * 存放所有相关 [Metadata] 的签名域
 *
 * @return [DeserializedMemberScope]
 */
inline val KClass<*>.memberScope get() = KClassImplKClass.java.declaredMethods.find { it.name.contains("getMemberScope") }?.invoke(this) as? DeserializedMemberScope?

/**
 * 当前 [DeserializedMemberScope] 的虚拟化上下文
 *
 * 存放 [Metadata] 虚拟化相关信息
 *
 * @return [DeserializationContext]
 */
inline val DeserializedMemberScope.deserializationContext get() = DeserializedMemberScope::class.property { name = "c" }.get(this).cast<DeserializationContext>()

/**
 * 当前 [DeserializedMemberScope] 的签名数据存储实现
 *
 * 存放 [Metadata] 签名数据相关信息
 *
 * ! 其不支持 NoReorderImplementation 实现将参阅使用 [OptimizedImplementationSupport]
 *
 * @return [OptimizedImplementationSupport]
 */
inline val DeserializedMemberScope.impl get() = OptimizedImplementationSupport(this, DeserializedMemberScope::class.property { name = "impl" }.get(this).any())

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
 * 获取此 [KClass] 指定属性名的签名
 *
 * 此方法以通过 [Metadata] 中定义的属性名获取Java层真正的签名
 *
 * - 此方法不涉及转 Kotlin 的反射属性可以避免一些异常 [Metadata] 数据报错
 * @param name 是在 [Metadata] 中定义的Kotlin层属性名
 * @param index 如果有多条签名选择第几个(往往只有一个) 默认为0
 * @return [PropertySignatureSupport] or null
 */
inline fun KClass<*>?.propertySignature(name: String, index: Int = 0):PropertySignatureSupport?{
    if (this == null) return null

    val nameResolver = memberScope?.deserializationContext?.nameResolver ?: return null
    val proto = (memberScope?.impl ?: return null).propertyProtos[if (!Name.isValidIdentifier(name)) Name.special(name) else Name.identifier(name)]?.get(index)?.getExtension(JvmProtoBuf.propertySignature) ?: return null
    return PropertySignatureSupport(nameResolver,proto)
}

/**
 * 与 [KClass.propertySignature] 一致的快捷方法
 * 获取指定 [declaringClass] 属性名为 [KProperty.name] 的真实签名
 *
 * 此方法以通过 [Metadata] 中定义的属性名获取Java层真正的签名
 *
 * - 此方法不涉及转 Kotlin 的反射属性可以避免一些异常 [Metadata] 数据报错
 * @param declaringClass 属性所在类
 * @param index 如果有多条签名选择第几个(往往只有一个) 默认为0
 * @return [PropertySignatureSupport] or null
 */
inline fun KProperty<*>.signatureMetadata(declaringClass: KClass<*>? = this.declaringClass,index: Int = 0):PropertySignatureSupport? = declaringClass.propertySignature(name, index)

/**
 * 属性签名处理支持组件
 *
 * @property nameResolver 名称解析器
 * @property proto 属性签名
 */
class PropertySignatureSupport(private val nameResolver:NameResolver, private val proto:JvmProtoBuf.JvmPropertySignature){

    /**
     * 字段签名处理支持组件
     *
     * @property proto 字段签名
     */
    inner class FieldSignatureSupport(private val proto:JvmProtoBuf.JvmFieldSignature){
        /**
         * 字段名
         */
        val name by lazy { nameResolver.getString(proto.name) }

        /**
         * 字段类型 如:Ljava/lang/String;
         */
        val desc by lazy { nameResolver.getString(proto.desc) }

        /**
         * 字段名是否存在
         */
        val hasName by lazy { proto.hasName() }

        /**
         * 字段类型是否存在
         */
        val hasDesc by lazy { proto.hasDesc() }

        override fun toString(): String {
            return "FieldSignatureSupport(name='$name', desc='$desc')"
        }
    }

    /**
     * 方法签名处理支持组件
     *
     * @property proto 方法签名
     */
    inner class FunctionSignatureSupport(private val proto:JvmProtoBuf.JvmMethodSignature){
        /**
         * 方法名
         */
        val name by lazy { nameResolver.getString(proto.name) }

        /**
         * 方法签名 如:(Ljava/lang/String;)V
         */
        val desc by lazy { nameResolver.getString(proto.desc) }

        /**
         * 方法名是否存在
         */
        val hasName by lazy { proto.hasName() }

        /**
         * 方法签名是否存在
         */
        val hasDesc by lazy { proto.hasDesc() }

        override fun toString(): String {
            return "FunctionSignatureSupport(name='$name', desc='$desc')"
        }
    }
    /**
     * 获取Getter函数签名处理支持组件
     */
    val getter by lazy { proto.getter?.let { FunctionSignatureSupport(it) } }

    /**
     * 获取Setter函数签名处理支持组件
     */
    val setter by lazy { proto.setter?.let { FunctionSignatureSupport(it) } }

    /**
     * 获取字段签名处理支持组件
     */
    val field by lazy { proto.field?.let { FieldSignatureSupport(it) } }

    /**
     * 获取委托函数签名处理支持组件
     */
    val delegateFunction by lazy { proto.delegateMethod?.let { FunctionSignatureSupport(it) } }

    /**
     * 获取合成函数签名处理支持组件
     */
    val syntheticFunction by lazy { proto.syntheticMethod?.let { FunctionSignatureSupport(it) } }

    /**
     * 是否存在Getter函数签名
     */
    val hasGetter by lazy { proto.hasGetter() }

    /**
     * 是否存在Setter函数签名
     */
    val hasSetter by lazy { proto.hasSetter() }

    /**
     * 是否存在字段签名
     */
    val hasField by lazy { proto.hasField() }

    /**
     * 是否存在委托函数签名
     */
    val hasDelegateFunction by lazy { proto.hasDelegateMethod() }

    /**
     * 是否存在合成函数签名
     */
    val hasSyntheticFunction by lazy { proto.hasSyntheticMethod() }

    override fun toString(): String {
        return "PropertySignatureSupport(getter=$getter, setter=$setter, field=$field, delegateFunction=$delegateFunction, syntheticFunction=$syntheticFunction)"
    }

}