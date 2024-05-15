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
 * This file is created by fankes on 2022/9/12.
 * This file is modified by fankes on 2023/1/25.
 */
package com.dream.yukireflection.finder.classes.rules

import com.dream.yukireflection.finder.base.KBaseFinder
import com.dream.yukireflection.finder.classes.rules.base.KBaseRules
import com.dream.yukireflection.finder.classes.rules.result.KCallableRulesResult
import com.dream.yukireflection.finder.callable.data.KPropertyRulesData
import com.dream.yukireflection.type.factory.KModifierConditions
import com.dream.yukireflection.type.factory.KTypeConditions
import com.highcapable.yukireflection.bean.VariousClass
import com.highcapable.yukireflection.finder.type.factory.NameConditions
import kotlin.reflect.*
import kotlin.reflect.KClassifier
import kotlin.reflect.KProperty
import kotlin.reflect.KTypeParameter

/**
 * [KProperty] 查找条件实现类
 * @param rulesData 当前查找条件规则数据
 */
class KPropertyRules internal constructor(private val rulesData: KPropertyRulesData) : KBaseRules() {

    /**
     * 设置 [KProperty] 名称
     * @return [String]
     */
    var name
        get() = rulesData.name
        set(value) {
            rulesData.name = value
        }

    /**
     * 设置 [KProperty] 类型
     *
     * - 只能是 [KClassifier]/[KClass]/[KTypeParameter]、[KGenericClass]、[KType]、[String]、[VariousClass]
     *
     * - 可不填写类型
     * @return [Any] or null
     */
    var type
        get() = rulesData.type
        set(value) {
            rulesData.type = value?.compat(tag = KBaseFinder.TAG_PROPERTY)
        }

    /**
     * 设置 [KProperty] 标识符筛选条件
     *
     * - 可不设置筛选条件
     * @param conditions 条件方法体
     */
    fun modifiers(conditions: KModifierConditions) {
        rulesData.modifiers = conditions
    }

    /**
     * 设置 [KProperty] 名称条件
     * @param conditions 条件方法体
     */
    fun name(conditions: NameConditions) {
        rulesData.nameConditions = conditions
    }

    /**
     * 设置 [KProperty] 类型条件
     *
     * - 可不填写类型
     *
     * 使用示例如下 ↓
     *
     * ```kotlin
     * type { it == StringClass || it.name == "java.lang.String" }
     * ```
     * @param conditions 条件方法体
     */
    fun type(conditions: KTypeConditions) {
        rulesData.typeConditions = conditions
    }

    /**
     * 返回结果实现类
     * @return [KCallableRulesResult]
     */
    internal fun build() = KCallableRulesResult(rulesData)
}