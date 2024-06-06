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
 * This file is created by fankes on 2022/2/18.
 * This file is modified by fankes on 2023/1/21.
 */
package com.dream.yukireflection.finder.base

import com.dream.yukireflection.KYukiReflection
import com.dream.yukireflection.factory.classLoader
import com.dream.yukireflection.factory.javaConstructorNoError
import com.dream.yukireflection.log.KYLog
import com.dream.yukireflection.utils.factory.await
import kotlin.reflect.*
import kotlin.reflect.jvm.javaConstructor

/**
 * 这是 [KCallable] 查找类功能的基本类实现
 * @param tag 当前查找类的标识
 * @param classSet 当前需要查找的 [KClass] 实例
 */
abstract class KCallableBaseFinder internal constructor(private val tag: String, internal open val classSet: KClass<*>? = null) : KBaseFinder() {

    internal companion object {

        /** [classSet] 为 null 的提示 */
        internal const val CLASSSET_IS_NULL = "classSet is null"
    }

    /** 是否使用了重查找功能 */
    internal var isUsingRemedyPlan = false

    /** 是否开启忽略错误警告功能 */
    internal var isIgnoreErrorLogs = false

    /** 当前找到的 [KCallable] 数组 */
    internal var callableInstances = mutableListOf<KCallable<*>>()

    /**
     * 将 [MutableList]<[KCallable]> 转换为 [MutableList]<[KProperty]>
     * @return [MutableList]<[KProperty]>
     */
    internal fun MutableList<KCallable<*>>.propertys() =
        mutableListOf<KProperty<*>>().also { takeIf { e -> e.isNotEmpty() }?.forEach { e -> (e as? KProperty?)?.also { f -> it.add(f) } } }

    /**
     * 将 [MutableList]<[KCallable]> 转换为 [MutableList]<[KFunction]>
     * @return [MutableList]<[KFunction]>
     */
    internal fun MutableList<KCallable<*>>.functions() =
        mutableListOf<KFunction<*>>().also { takeIf { e -> e.isNotEmpty() }?.forEach { e -> (e as? KFunction?)?.also { m -> it.add(m) } } }

    /**
     * 将 [MutableList]<[KCallable]> 转换为 [MutableList]<Constructor [KFunction]>
     * @return [MutableList]<Constructor [KFunction]>
     */
    internal fun MutableList<KCallable<*>>.constructors() =
        mutableListOf<KFunction<*>>().also { takeIf { e -> e.isNotEmpty() }?.forEach { e -> (e as? KFunction<*>?)?.takeIf { it.javaConstructorNoError != null }?.also { c -> it.add(c) } } }

    /**
     * 将目标类型转换为可识别的兼容类型
     * @return [KClassifier]/[KClass]/[KTypeParameter] or arror([KTypeProjection]) or [KVariance]/array([KVariance]) or [KType] or [KGenericClass] or null
     */
    internal fun Any?.compat(tag: String = this@KCallableBaseFinder.tag) = compat(tag, classSet?.classLoader)

    /**
     * 在开启 [YukiReflection.Configs.isDebug] 的情况下输出调试信息
     * @param msg 消息内容
     */
    internal fun debugMsg(msg: String) {
        if (KYukiReflection.Configs.isDebug) KYLog.debug(msg)
    }

    /**
     * 发生错误时输出日志
     * @param msg 消息内容
     * @param e 异常堆栈 - 默认空
     * @param e 异常堆栈数组 - 默认空
     * @param isAlwaysMode 忽略条件每次都输出日志
     */
    internal fun errorMsg(msg: String = "", e: Throwable? = null, es: List<Throwable> = emptyList(), isAlwaysMode: Boolean = false) {
        /** 判断是否为 [CLASSSET_IS_NULL] */
        if (e?.message == CLASSSET_IS_NULL) return
        await {
            if (isIgnoreErrorLogs) return@await
            if (isAlwaysMode.not() && isUsingRemedyPlan) return@await
            KYLog.error(msg = "NoSuch$tag happend in [$classSet] $msg".trim(), e = e)
            es.forEachIndexed { index, e -> KYLog.error(msg = "Throwable [${index + 1}]", e = e) }
        }
    }
}