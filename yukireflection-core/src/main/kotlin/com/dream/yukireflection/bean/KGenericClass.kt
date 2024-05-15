/*
 * YukiReflection - An efficient Reflection API for Java and Android built in Kotlin.
 * Copyright (C) 2019-2024 HighCapable
 * https://github.com/HighCapable/YukiReflection
 *
 * Apache License Version 2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file is created by fankes on 2022/9/20.
 * This file is modified by fankes on 2023/1/21.
 */
@file:Suppress("unused", "UNCHECKED_CAST", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package com.dream.yukireflection.bean

import com.dream.yukireflection.build.KTypeBuild
import com.dream.yukireflection.factory.generic
import com.dream.yukireflection.factory.kotlin
import com.dream.yukireflection.factory.variance
import com.dream.yukireflection.type.defined.VagueKotlin
import com.dream.yukireflection.type.factory.KTypeBuildConditions
import org.jetbrains.kotlin.utils.addToStdlib.ifFalse
import kotlin.reflect.*

/**
 * 对当前 [KType] 的泛型操作对象
 *
 * @param type 拥有类型声明信息的Kotlin类型 可能包含泛型信息
 * @property ArrayList this存储当前泛型参数数组
 */
class KGenericClass constructor(val type: KType) :List<KTypeProjection> by type.arguments{

    /**
     * 是否检查方差
     *
     * 检查对比泛型的 in/out/默认
     *
     * 影响比较相关操作，如:[equals]
     *
     * @see [KVariance]
     */
    var isVariance :Boolean? = null

    /**
     * 使用检查方差 快捷方法
     *
     * 检查对比泛型的 in/out/默认
     *
     * 影响比较相关操作，如:[equals]
     *
     * @see [KVariance]
     */
    fun checkVariance(): KGenericClass {
        isVariance = true
        return this
    }

    /**
     * 是否检查可空性
     *
     * 检查往往与类型是否带问号有关如:String->false,String?->true
     *
     * 影响比较相关操作，如:[equals]
     *
     * 默认不检查可空性
     *
     * @see [KType.isMarkedNullable]
     */
    var isMarkedNullable = false

    /**
     * 使用检查可空性 快捷方法
     *
     * 检查往往与类型是否带问号有关如:String->false,String?->true
     *
     * 影响比较相关操作，如:[equals]
     *
     * 默认不检查可空性
     *
     * @see [KType.isMarkedNullable]
     */
    fun checkMarkedNullable(): KGenericClass {
        isMarkedNullable = true
        return this
    }

    /**
     * 是否检查注解一致性
     *
     * 检查往往与类型所携带的注解有关 ↓
     *
     * ```kotlin
     * vararg abc:@UnsafeVariance Int
     * ```
     *
     * 为true时检查两者type的注解是否同时包含满足条件
     *
     * 影响比较相关操作，如:[equals]
     *
     * 默认不检查注解一致性
     *
     * @see [KType.isMarkedNullable]
     */
    var isAnnotation = false

    /**
     * 使用检查注解一致性 快捷方法
     *
     * 检查往往与类型所携带的注解有关 ↓
     *
     * ```kotlin
     * vararg abc:@UnsafeVariance Int
     * ```
     *
     * 为true时检查两者type的注解是否同时包含满足条件
     *
     * 影响比较相关操作，如:[equals]
     *
     * 默认不检查注解一致性
     *
     * @see [KType.isMarkedNullable]
     */
    fun checkAnnotation(): KGenericClass {
        isAnnotation = true
        return this
    }

    /**
     * 为当前泛型操作对象使用指定方差构建类型映射
     *
     * 目标:[type]
     * @param variance 方差类型
     * @return [KTypeProjection] -> out/in/默认 [type]
     */
    fun variance(variance:KVariance = KVariance.INVARIANT): KTypeProjection = type.variance(variance)

    /**
     * 获得泛型参数数组下标的 [KClass] 实例
     *
     * 此结果的 [KClass] 会泛型类型擦除
     *
     *     当type为 List<List<String>>
     *
     *     错误示例 argument(0) -> List::class 无法获取进一步:argument(0).argument(0) KClass没有argument方法，当获得KClass后将进行擦除类型
     *
     *     应使用 generic(0).argument(0) -> String
     *
     * - 在运行时局部变量的泛型会被擦除 - 获取不到时将会返回 null
     * @param index 数组下标 - 默认 0
     * @return [KClass] or null
     */
    fun argument(index: Int = 0) = runCatching { this[index].type?.kotlin }.getOrNull()

    /**
     * 获得泛型参数数组下标的 泛型操作对象
     *
     * 此结果的 [KGenericClass] 不会对泛型类型擦除
     *
     *     如 type = List<List<Int>>
     *     generic(0) ---> List<Int>
     *     generic(0).generic(0) ---> Int
     *
     * @param index 数组下标 - 默认 0
     * @param initiate 实例方法体
     * @return [KGenericClass] 进行进行泛型使用
     */
    fun generic(index: Int = 0,initiate: KGenericClass.() -> Unit = {}) = this[index].type?.generic(initiate) ?: error("The type:$type index:$index arguments:${type.arguments} no further generics")

    /**
     * 判断当前 [KGenericClass] 是否包含指定泛型类型 根据[isVariance]测试是否检查方差
     *
     * 泛型信息 --- 直接投影类 泛型方差(不变、协变、逆变) 直接泛型类型 泛型擦除类型
     *
     * @param element [KTypeProjection] or [KType] or [KClassifier]/[KClass]/[KTypeParameter]
     * @return [Boolean]
     */
    @JvmName("contains_Any")
    fun contains(element: Any) = type.arguments.any {
        if (element is KTypeProjection && isVariance == true)
            it.variance == element.variance && it.type?.generic() == element
        else
            it.type?.generic() == element
    }

    /**
     * 判断当前 [KGenericClass] 是否包含指定泛型类型 根据[isVariance]测试是否检查方差
     *
     * @param element [KTypeProjection]
     * @return [Boolean]
     */
    override fun contains(element: KTypeProjection): Boolean = contains(element as Any)

    /**
     * 判断当前 [KGenericClass] 是否包含指定一批泛型类型 根据[isVariance]测试是否检查方差
     *
     * 包含判断数组不分顺序
     *
     * @param elements [Collection]
     * @return [Boolean]
     */
    override fun containsAll(elements: Collection<KTypeProjection>): Boolean {
        elements.forEach {
            if (!contains(it))return false
        }
        return true
    }

    /**
     * 通过当前 [KType] 构建一个新的 [KType] 并继续使用泛型操作对象
     *
     * [KTypeBuildConditions]是构建时可自定义的参数
     *
     * @param initiate 实例方法体
     * @return [KGenericClass] 新Type的泛型操作对象
     */
    inline fun build(initiate: KTypeBuildConditions = {}) = KTypeBuild(type).apply(initiate).build().get().generic()

    /**
     * 获得泛型参数数组下标的 [KClass] 实例
     *
     * - 在运行时局部变量的泛型会被擦除 - 获取不到时将会返回 null
     * @param index 数组下标 - 默认 0
     * @return [KClass]<[T]> or null
     * @throws IllegalStateException 如果 [KClass] 的类型不为 [T]
     */
    @JvmName("argument_Generics")
    inline fun <reified T> argument(index: Int = 0) =
        type.arguments[index].type?.kotlin.let { args ->
            if (args is KClass<*>) args as? KClass<T & Any>? ?: error("Target Class type cannot cast to ${T::class.java}")
            else null
        }

    /**
     * 自动获取是否检查方差
     *
     * - 如果[isVariance]没有指定 - 只要有任何一个泛型参数包含非默认方差则返回true
     *
     * @return [Boolean]
     */
    private var isVarianceAutomatic:Boolean? = null
        get(){
            if (field == null || field != isVariance){
                field = if (isVariance == null)isNotDefaultVariance(type.arguments) else isVariance!!
            }
            return field
        }

    /**
     * 此参数列表是否都是默认的方差，即没有in、out
     *
     * @param arguments 参数列表
     * @return [Boolean]
     */
    private fun isNotDefaultVariance(arguments: List<KTypeProjection>):Boolean =
        arguments.any {
            (it.variance != null && it.variance != KVariance.INVARIANT) || (it.type != null && isNotDefaultVariance(it.type!!.arguments))
        }

    override fun toString(): String {
        return "KGenericClass(isVariance=$isVarianceAutomatic,type=$type) & " + super.toString()
    }

    /**
     * 对目标类型是否一致进行判断
     *
     * 受[isVariance]影响
     *
     * @param other [KGenericClass] or [KType] or [KTypeProjection] or [KParameter] or [KProperty] or [KClassifier]/[KClass]/[KTypeParameter]
     * @return [Boolean]
     */
    override fun equals(other: Any?): Boolean {
        if (this === other || (super.equals(other) && other is KGenericClass && type == other.type)) return true

        val otherGeneric = when(other){
            is KType -> other.generic()
            is KTypeProjection -> other.type?.generic() ?: return false
            is KParameter -> other.type.generic()
            is KProperty<*> -> other.generic()
            is KGenericClass -> other
            is KClassifier -> return other == type.classifier
            else -> return false
        }

        if ((isMarkedNullable || otherGeneric.isMarkedNullable) && type.isMarkedNullable != otherGeneric.type.isMarkedNullable)return false

        if ((isAnnotation || otherGeneric.isAnnotation) && type.arguments != otherGeneric.type.arguments)return false

        var isVariance = otherGeneric.isVariance
        if (isVariance == null){
            isVariance = isVarianceAutomatic!!.ifFalse { other is KGenericClass && isNotDefaultVariance(otherGeneric.type.arguments) } ?: true
        }

        if(type.arguments.size != otherGeneric.type.arguments.size) return false
        if (type.arguments.isEmpty() && otherGeneric.type.arguments.isEmpty())return type.classifier == otherGeneric.type.classifier
        var isEq:Boolean
        type.arguments.forEachIndexed { index, kTypeProjection ->
            if (isVariance && kTypeProjection.variance != otherGeneric.type.arguments[index].variance)return false
            isEq = kTypeProjection == KTypeProjection.STAR || kTypeProjection.type?.kotlin == VagueKotlin
            isEq = isEq || kTypeProjection.type?.generic()?.setVariance(isVariance)?.setMarkedNullable(isMarkedNullable)?.setAnnotation(isAnnotation) == otherGeneric.type.arguments[index].type?.generic()?.setVariance(isVariance)?.setMarkedNullable(isMarkedNullable)?.setAnnotation(isAnnotation)
            if (!isEq)return false
        }
        return type.classifier == otherGeneric.type.classifier
    }

    /**
     * @see [checkVariance]
     */
    private fun setVariance(isVariance: Boolean): KGenericClass {
        this.isVariance = isVariance
        return this
    }

    /**
     * @see [checkMarkedNullable]
     */
    private fun setMarkedNullable(isMarkedNullable: Boolean): KGenericClass {
        this.isMarkedNullable = isMarkedNullable
        return this
    }

    /**
     * @see [checkAnnotation]
     */
    private fun setAnnotation(isAnnotation: Boolean): KGenericClass {
        this.isAnnotation = isAnnotation
        return this
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

}