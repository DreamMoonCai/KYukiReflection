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
 * This file is created by fankes on 2022/9/8.
 * This file is modified by fankes on 2023/1/21.
 */
package com.dream.yukireflection.finder.base.data

import com.dream.yukireflection.finder.base.KBaseFinder
import com.dream.yukireflection.finder.base.KBaseFinder.Companion.checkSupportedTypes
import com.dream.yukireflection.finder.base.rules.KCountRules
import com.dream.yukireflection.finder.base.rules.KModifierRules
import com.dream.yukireflection.finder.base.rules.KNameRules
import com.dream.yukireflection.finder.base.rules.KObjectRules
import com.dream.yukireflection.type.factory.KModifierConditions
import com.dream.yukireflection.type.defined.VagueKotlin
import kotlin.reflect.*
import kotlin.reflect.full.valueParameters

/**
 * 这是 [KClass] 与 [KCallable] 规则查找数据基本类实现
 * @param modifiers 描述符条件
 * @param orderIndex 字节码、数组顺序下标
 * @param matchIndex 字节码、数组筛选下标
 */
internal abstract class KBaseRulesData internal constructor(
    var modifiers: KModifierConditions? = null,
    var orderIndex: Pair<Int, Boolean>? = null,
    var matchIndex: Pair<Int, Boolean>? = null
) {

    /** 当前类唯一标识值 */
    internal var uniqueValue = 0L

    init {
        uniqueValue = System.currentTimeMillis()
    }

    /**
     * [String] 转换为 [KNameRules]
     * @return [KNameRules]
     */
    internal fun String.cast() = KNameRules.with(this)

    /**
     * [Int] 转换为 [KCountRules]
     * @return [KCountRules]
     */
    internal fun Int.cast() = KCountRules.with(this)

    /**
     * [KClass] 转换为 [KModifierRules]
     * @return [KModifierRules]
     */
    internal fun KClass<*>.cast() = KModifierRules.with(instance = this, uniqueValue)

    /**
     * [KCallable] 转换为 [KModifierRules]
     * @return [KModifierRules]
     */
    internal fun KCallable<*>.cast() = KModifierRules.with(instance = this, uniqueValue)

    /**
     * [KProperty.returnType] 转换为 [KObjectRules]
     * @return [KObjectRules]
     */
    internal fun KProperty<*>.type() = KObjectRules.with(returnType)

    /**
     * [KFunction.valueParameters] 转换为 [KObjectRules]
     * @return [KObjectRules]
     */
    internal fun KCallable<*>.paramTypes() = KObjectRules.with(valueParameters)

    /**
     * [KFunction.valueParameters]name 转换为 [KObjectRules]
     * @return [KObjectRules]
     */
    internal fun KCallable<*>.paramNames() = KObjectRules.with(valueParameters.map { it.name })

    /**
     * [KFunction.returnType] 转换为 [KObjectRules]
     * @return [KObjectRules]
     */
    internal fun KCallable<*>.returnType() = KObjectRules.with(returnType)

    /**
     * 获取参数数组文本化内容
     *
     * 仅支持 [Class]/[KClassifier]/[KClass]/[KTypeParameter] or [KTypeProjection]/array([KTypeProjection]) or [KVariance]/array([KVariance]) or [KType] or [KGenericClass]
     *
     * @return [String]
     */
    internal fun Array<out Any>?.typeOfString() =
        StringBuilder("(").also { sb ->
            var isFirst = true
            if (this == null || isEmpty()) return "()"
            forEach {
                if (isFirst) isFirst = false else sb.append(", ")
                when(val type = it.checkSupportedTypes()!!){
                    is Class<*> -> sb.append(type.takeIf { type.canonicalName != VagueKotlin.java.canonicalName }?.canonicalName ?: "*vague*")
                    is KClass<*> -> sb.append(type.takeIf { type.java.canonicalName != VagueKotlin.java.canonicalName }?.java?.canonicalName ?: "*vague*")
                    is KTypeParameter,is KType,is KTypeProjection,is KVariance -> sb.append(type.toString())
                    is String -> sb.append(type)
                    else -> sb.append(KBaseFinder.checkArrayGenerics(type))
                }
            }
            sb.append(")")
        }.toString()

    /**
     * 获取规则对象模板字符串数组
     * @return [Array]<[String]>
     */
    internal abstract val templates: Array<String>

    /**
     * 获取规则对象名称
     * @return [String]
     */
    internal abstract val objectName: String

    /**
     * 判断规则是否已经初始化 (设置了任意一个参数)
     * @return [Boolean]
     */
    internal open val isInitialize get() = modifiers != null || orderIndex != null || matchIndex != null

    override fun toString() = "[$modifiers][$orderIndex][$matchIndex]"
}