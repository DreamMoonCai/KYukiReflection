@file:Suppress("UnusedImport")

package com.dream.yukireflection.factory

import com.dream.yukireflection.utils.DexSignUtil
import java.lang.reflect.*
import kotlin.reflect.*
import kotlin.jvm.internal.Reflection
import kotlin.reflect.jvm.*

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