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
package io.github.dreammooncai.yukireflection.core.data

import io.github.dreammooncai.yukireflection.finder.base.data.KBaseRulesData
import kotlin.reflect.*

/**
 * [KType] 构建数据类
 * @param isNullable 类型是否为可空
 * @param paramTypes 参数类型数组
 * @param paramCount 参数个数
 * @param annotations 注解个数
 */
internal class KTypeBuildData internal constructor(
    var isNullable: Boolean = false,
    var paramTypes: Array<out Any>? = null,
    var paramCount: Int = -1,
    var annotations: List<Annotation> = emptyList()
) : KBaseRulesData() {

    override val templates
        get() = arrayOf(
            isNullable.let { "isNullable:[$it]" },
            paramCount.takeIf { it >= 0 }?.let { "paramCount:[$it]" } ?: "",
            paramTypes?.typeOfString()?.let { "paramTypes:[$it]" } ?: "",
            annotations.let { "annotations:[$it]" },
        )

    override val objectName get() = "TypeBuild"

    override val isInitialize
        get() = paramTypes != null || paramCount >= 0 || annotations.isNotEmpty() || isNullable

    override fun toString() = "[$isNullable][$paramTypes][$paramCount]$[$annotations]" + super.toString()
}