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
package com.dream.yukireflection.finder.classes.rules.base

import com.dream.yukireflection.finder.classes.KClassFinder
import com.dream.yukireflection.finder.classes.rules.KCallableRules
import com.dream.yukireflection.finder.classes.rules.KConstructorRules
import com.dream.yukireflection.finder.classes.rules.KFunctionRules
import com.dream.yukireflection.finder.classes.rules.KPropertyRules
import com.dream.yukireflection.finder.callable.data.KCallableRulesData
import com.dream.yukireflection.finder.callable.data.KConstructorRulesData
import com.dream.yukireflection.finder.callable.data.KFunctionRulesData
import com.dream.yukireflection.finder.callable.data.KPropertyRulesData
import kotlin.reflect.*

/**
 * [KCallable] 查找条件实现父类
 * @param instance 当前查找类实例
 */
open class KBaseRules internal constructor(internal var instance: KClassFinder? = null) {

    internal companion object {

        /**
         * 创建查找条件规则数据
         * @param instance 当前查找类实例
         * @return [KCallableRulesData]
         */
        internal fun createCallableRules(instance: KClassFinder) =
            KCallableRules(KCallableRulesData().apply { instance.rulesData.callableRules.add(this) }).apply { this.instance = instance }

        /**
         * 创建查找条件规则数据
         * @return [KPropertyRulesData]
         */
        internal fun createPropertyRules(instance: KClassFinder) =
            KPropertyRules(KPropertyRulesData().apply { instance.rulesData.propertyRules.add(this) }).apply { this.instance = instance }

        /**
         * 创建查找条件规则数据
         * @return [KFunctionRulesData]
         */
        internal fun createFunctionRules(instance: KClassFinder) =
            KFunctionRules(KFunctionRulesData().apply { instance.rulesData.functionRules.add(this) }).apply { this.instance = instance }

        /**
         * 创建查找条件规则数据
         * @return [KConstructorRulesData]
         */
        internal fun createConstructorRules(instance: KClassFinder) =
            KConstructorRules(KConstructorRulesData().apply { instance.rulesData.constroctorRules.add(this) }).apply { this.instance = instance }
    }

    /**
     * 将目标类型转换为可识别的兼容类型
     * @param tag 当前查找类的标识
     * @return [KClassifier]/[KClass]/[KTypeParameter] or [KTypeProjection]/array([KTypeProjection]) or [KVariance]/array([KVariance]) or [KType] or [KGenericClass] or null
     */
    internal fun Any?.compat(tag: String) = instance?.compatType(any = this, tag)
}