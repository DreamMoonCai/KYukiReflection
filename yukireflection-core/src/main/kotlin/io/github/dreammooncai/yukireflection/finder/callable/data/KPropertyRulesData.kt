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
 * This file is created by fankes on 2022/9/4.
 * This file is modified by fankes on 2023/1/21.
 */
package io.github.dreammooncai.yukireflection.finder.callable.data

import io.github.dreammooncai.yukireflection.finder.base.KBaseFinder
import io.github.dreammooncai.yukireflection.type.factory.KNameConditions
import io.github.dreammooncai.yukireflection.type.factory.KTypeConditions
import kotlin.reflect.KProperty

/**
 * [KProperty] 规则查找数据类
 * @param name 名称
 * @param nameConditions 名称规则
 * @param type 类型
 * @param typeConditions 类型条件
 */
internal class KPropertyRulesData internal constructor(
    var name: String = "",
    var nameConditions: KNameConditions? = null,
    var type: Any? = null,
    var typeConditions: KTypeConditions? = null
) : KCallableRulesData() {

    override val templates
        get() = arrayOf(
            name.takeIf { it.isNotBlank() }?.let { "name:[$it]" } ?: "",
            nameConditions?.let { "nameConditions:[existed]" } ?: "",
            type?.let { "type:[$it]" } ?: "",
            typeConditions?.let { "typeConditions:[existed]" } ?: "", *super.templates
        )

    override val objectName get() = KBaseFinder.TAG_PROPERTY

    override val isInitialize
        get() = super.isInitializeOfSuper || name.isNotBlank() || nameConditions != null || type != null || typeConditions != null

    override fun toString() = "[$name][$nameConditions][$type][$typeConditions]" + super.toString()
}