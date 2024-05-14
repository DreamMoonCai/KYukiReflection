@file:Suppress("UnusedImport")

package com.dream.yukireflection.factory

import com.dream.yukireflection.utils.DexSignUtil
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.jvm.internal.Reflection
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.kotlinFunction
import kotlin.reflect.jvm.kotlinProperty

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
 * 通过 [Field] 分析签名构建 [KProperty]
 *
 * 签名分析或许不一定能正常转换 参阅或使用[Field.kotlinProperty]
 */
val Field.kotlin: KProperty<*> get() = when{
    Modifier.isStatic(modifiers) -> Reflection.mutableProperty0(KReference.mutablePropertyStatic(this))
    else -> Reflection.mutableProperty1(KReference.mutableProperty(this))
}

/**
 * 通过 [Method] 分析签名构建 [KFunction]
 *
 * 签名分析或许不一定能正常转换 参阅或使用[Method.kotlinFunction]
 */
val Method.kotlin: KFunction<*> get() = Reflection.function(KReference.function(this))

/**
 * 通过 [Constructor] 分析签名构建 [KFunction]
 *
 * 签名分析或许不一定能正常转换 参阅或使用[Constructor.kotlinFunction]
 */
val Constructor<*>.kotlin: KFunction<*> get() = Reflection.function(KReference.function(this))

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