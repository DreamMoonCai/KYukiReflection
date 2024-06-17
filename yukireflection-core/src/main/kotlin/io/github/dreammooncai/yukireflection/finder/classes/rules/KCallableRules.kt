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
package io.github.dreammooncai.yukireflection.finder.classes.rules

import io.github.dreammooncai.yukireflection.finder.classes.rules.base.KBaseRules
import io.github.dreammooncai.yukireflection.finder.classes.rules.result.KCallableRulesResult
import io.github.dreammooncai.yukireflection.finder.callable.data.KCallableRulesData
import io.github.dreammooncai.yukireflection.type.factory.KModifierConditions
import kotlin.reflect.KCallable

/**
 * [KCallable] 查找条件实现类
 * @param rulesData 当前查找条件规则数据
 */
class KCallableRules internal constructor(private val rulesData: KCallableRulesData) : KBaseRules() {

    /**
     * 设置 [KCallable] 标识符筛选条件
     *
     * - 可不设置筛选条件
     * @param conditions 条件方法体
     */
    fun modifiers(conditions: KModifierConditions) {
        rulesData.modifiers = conditions
    }

    /**
     * 返回结果实现类
     * @return [KCallableRulesResult]
     */
    internal fun build() = KCallableRulesResult(rulesData)
}