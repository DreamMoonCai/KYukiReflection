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
 * This file is modified by fankes on 2023/1/25.
 */
package io.github.dreammooncai.yukireflection.finder.base

import io.github.dreammooncai.yukireflection.KYukiReflection
import io.github.dreammooncai.yukireflection.factory.classLoader
import io.github.dreammooncai.yukireflection.log.KYLog
import kotlin.reflect.*

/**
 * 这是 [KClass] 查找类功能的基本类实现
 * @param classSet 当前使用的 [ClassLoader] 实例
 */
abstract class KClassBaseFinder internal constructor(internal open val classSet: Collection<KClass<*>>? = null) : KBaseFinder() {

    internal companion object {

        /** [classSet] 为 null 的提示 */
        internal const val LOADERSET_IS_NULL = "classSet is null"
    }

    /** 当前找到的 [KClass] 数组 */
    internal var classInstances = mutableListOf<KClass<*>>()

    /** 是否开启忽略错误警告功能 */
    internal var isIgnoreErrorLogs = false

    /**
     * 将目标类型转换为可识别的兼容类型
     * @param any 当前需要转换的实例
     * @param tag 当前查找类的标识
     * @return [Class]/[KClassifier]/[KClass]/[KTypeParameter] or [KTypeProjection]/array([KTypeProjection]) or [KVariance]/array([KVariance]) or [KType] or [KGenericClass] or null
     */
    internal fun compatType(any: Any?, tag: String) = any?.compat(tag, classSet?.first()?.classLoader)

    /**
     * 在开启 [YukiReflection.Configs.isDebug] 的情况下输出调试信息
     * @param msg 消息内容
     */
    internal fun debugMsg(msg: String) {
        if (KYukiReflection.Configs.isDebug) KYLog.debug(msg)
    }

    /**
     * 发生错误时输出日志
     * @param e 异常堆栈 - 默认空
     */
    internal fun errorMsg(e: Throwable? = null) {
        if (isIgnoreErrorLogs) return
        /** 判断是否为 [LOADERSET_IS_NULL] */
        if (e?.message == LOADERSET_IS_NULL) return
        KYLog.error(msg = "NoClassDefFound happend in [$classSet]", e = e)
    }

    override fun failure(throwable: Throwable?) = error("KClassFinder does not contain this usage")
}