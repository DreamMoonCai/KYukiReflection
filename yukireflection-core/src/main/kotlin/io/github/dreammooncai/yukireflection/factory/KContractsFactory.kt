@file:Suppress(
    "UNCHECKED_CAST",
    "NOTHING_TO_INLINE",
    "INVISIBLE_MEMBER",
)
@file:OptIn(ExperimentalContracts::class)

package io.github.dreammooncai.yukireflection.factory

import io.github.dreammooncai.yukireflection.utils.factory.lazyDomain
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.jvm.internal.CallableReference
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

/**
 * 将 [KProperty] 转换为 [KMutableProperty]
 *
 * 契约版本 自动转换 [KMutableProperty]
 *
 * 如下所示 ↓
 *
 *     property.toMutable
 *     property.set(a) // 自动转换 [KMutableProperty]
 *
 * ^^^
 *
 * @return [KMutableProperty]
 */
val <T> KProperty<T>?.toMutable: KMutableProperty<T>
    get() {
        contract {
            returnsNotNull() implies (this@toMutable is KMutableProperty<T>)
        }
        return toMutableOrNull ?: error("The conversion failed, which is not a mutable property.")
    }

/**
 * 将 [KProperty] 转换为 [KMutableProperty]
 *
 * 如果无法转换则返回 Null
 *
 * 契约版本 如果不为 null 则自动转换 [KMutableProperty]
 *
 * 如下所示 ↓
 *
 *     if (property.toMutableOrNull != null) {
 *         property.set(a) // 自动转换 [KMutableProperty]
 *     }
 *
 * ^^^
 *
 * @return [KMutableProperty] or null
 */
val <T> KProperty<T>?.toMutableOrNull: KMutableProperty<T>?
    get() {
        contract {
            returnsNotNull() implies (this@toMutableOrNull is KMutableProperty<T>)
        }
        return this as? KMutableProperty
    }

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
inline val KCallable<*>.ref: CallableReference
    get() {
        contract {
            returns() implies (this@ref is CallableReference)
        }
        return this as CallableReference
    }

/**
 * 此 属性 [KProperty] 是否为可变属性
 *
 * 契约版本 如果为 True 则自动转换 [KMutableProperty]
 *
 * 如下所示 ↓
 *
 *     if (property.isVar()) {
 *         property.set(a) // 自动转换 [KMutableProperty]
 *     }
 *
 * ^^^
 *
 * @return [Boolean]
 */
val <T> KProperty<T>.isVar: Boolean
    get() {
        contract {
            returns(true) implies (this@isVar is KMutableProperty<T>)
        }
        return when (this) {
            is KMutableProperty -> true
            else -> false
        }
    }

/**
 * 当前 [KFunction] 是否是 Getter 函数
 *
 * Getter 函数的类型可能是 [KProperty.Getter] 或函数名为 <get-xxx>
 */
val <T> KFunction<T>.isGetter: Boolean
    get() {
        contract {
            returns(true) implies (this@isGetter is KProperty.Getter<T>)
        }
        return when {
            this is KProperty.Getter<*> -> true
            this.name.startsWith("<get-") && this.name.endsWith(">") -> true
            else -> false
        }
    }

/**
 * 当前 [KFunction] 是否是 Setter 函数
 *
 * Setter 函数的类型可能是 [KMutableProperty.Setter] 或函数名为 <set-xxx>
 */
val <T> KFunction<T>.isSetter: Boolean
    get() {
        contract {
            returns(true) implies (this@isSetter is KProperty.Getter<T>)
        }
        return when {
            this is KMutableProperty.Setter<*> -> true
            this.name.startsWith("<set-") && this.name.endsWith(">") -> true
            else -> false
        }
    }

/**
 * [Any] 是否可以转换为 [other]
 *
 * 契约版本 如果为 True 则自动转换 [T]
 *
 * 如下所示 ↓
 *
 *     if (any.isCase(String::class)) {
 *         any // 自动转换 [String]
 *     }
 *
 * ^^^
 *
 * @param other 需要判断的 [KClass]
 * @return [Boolean]
 */
inline infix fun <T : Any> Any?.isCase(other: KClass<T>?): Boolean {
    contract { returns(true) implies (this@isCase is T) }
    return other?.isInstance(this) ?: false
}

/**
 * [Any] 是否可以转换为 [T]
 *
 * 契约版本 如果为 True 则自动转换 [T]
 *
 * 如下所示 ↓
 *
 *     if (any.isCase<String>()) {
 *         any // 自动转换 [String]
 *     }
 *
 * ^^^
 *
 * @param T 需要判断的 [KClass]
 * @return [Boolean]
 */
inline fun <reified T : Any> Any?.isCase(): Boolean {
    contract { returns(true) implies (this@isCase is T) }
    return isCase(T::class)
}