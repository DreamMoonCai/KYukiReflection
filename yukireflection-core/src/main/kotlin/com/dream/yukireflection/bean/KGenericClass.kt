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
import com.dream.yukireflection.type.defined.VagueKType
import org.jetbrains.kotlin.utils.addToStdlib.ifFalse
import kotlin.reflect.*

/**
 * 对当前 [KType] 的泛型操作对象
 *
 * @param type 拥有类型声明信息的Kotlin类型 可能包含泛型信息
 */
class KGenericClass constructor(val type: KType) :ArrayList<KTypeProjection>(type.arguments){

    /**
     * 是否检查方差
     *
     * 检查对比泛型的 in/out/默认
     *
     * 影响比较相关操作，如;[equals]
     *
     * @see [KVariance]
     */
    var isVariance :Boolean? = null

    /**
     * 是否检查方差
     *
     * 检查对比泛型的 in/out/默认
     *
     * 影响比较相关操作，如;[equals]
     *
     * @see [KVariance]
     */
    fun checkVariance(): KGenericClass {
        isVariance = true
        return this
    }

    /**
     * 为当前泛型操作对象使用指定方差构建类型映射
     */
    fun variance(variance:KVariance = KVariance.INVARIANT): KTypeProjection = type.variance(variance)

    /**
     * 获得泛型参数数组下标的 [KClass] 实例
     *
     * 此结果的 [KClass] 会泛型类型擦除
     *
     * - 在运行时局部变量的泛型会被擦除 - 获取不到时将会返回 null
     * @param index 数组下标 - 默认 0
     * @return [KClass] or null
     */
    fun argument(index: Int = 0) = this[index].type?.kotlin

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
     */
    fun generic(index: Int = 0,initiate: KGenericClass.() -> Unit = {}) = this[index].type?.generic(initiate) ?: error("The type:$type index:$index arguments:${type.arguments} no further generics")

    /**
     * 判断当前 [KGenericClass] 是否包含指定 泛型信息
     *
     * 泛型信息 --- 直接投影类 泛型方差(不变、协变、逆变) 直接泛型类型 泛型擦除类型
     *
     * @param projection [KTypeProjection] or [KType] or [KClassifier]/[KClass]/[KTypeParameter]
     * @return [Boolean]
     */
    fun contains(projection: Any) = when(projection){
        is KTypeProjection -> type.arguments.contains(projection)
        is KType -> type.arguments.any { it.type == projection }
        is KClassifier -> type.arguments.any { it.type?.classifier == projection }
        else -> false
    }

    /**
     * 通过当前 [KType] 构建一个新的 [KType]
     *
     * [KTypeBuild]是构建时可自定义的参数
     *
     * @param initiate 实例方法体
     * @return [KTypeBuild]
     */
    inline fun build(initiate: KTypeBuild.() -> Unit = {}) = KTypeBuild(type).apply(initiate).build().get().generic()

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

        var isVariance = otherGeneric.isVariance
        if (isVariance == null){
            isVariance = isVarianceAutomatic!!.ifFalse { other is KGenericClass && isNotDefaultVariance(otherGeneric.type.arguments) } ?: true
        }

        if(type.arguments.size != otherGeneric.type.arguments.size) return false
        if (type.arguments.isEmpty() && otherGeneric.type.arguments.isEmpty())return type.classifier == otherGeneric.type.classifier
        var isEq:Boolean
        type.arguments.forEachIndexed { index, kTypeProjection ->
            if (isVariance && kTypeProjection.variance != otherGeneric.type.arguments[index].variance)return false
            isEq = kTypeProjection == KTypeProjection.STAR || kTypeProjection.type?.kotlin == VagueKType
            isEq = isEq || kTypeProjection.type?.generic()?.setVariance(isVariance) == otherGeneric.type.arguments[index].type?.generic()?.setVariance(isVariance)
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

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

}