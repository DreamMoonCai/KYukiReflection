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
 * This file is created by fankes on 2023/1/21.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE", "UnusedImport")

package io.github.dreammooncai.yukireflection

import io.github.dreammooncai.yukireflection.factory.declaredPropertys
import io.github.dreammooncai.yukireflection.log.KYLog
import io.github.dreammooncai.yukireflection.generated.YukiReflectionProperties
import java.lang.reflect.Member
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions

/**
 * [KYukiReflection] 的装载调用类
 *
 * 你可以使用 [Configs] 对 [KYukiReflection] 进行配置
 */
object KYukiReflection {

    /** 标签名称 */
    const val TAG = KYukiReflectionProperties.PROJECT_NAME

    /** 当前版本 */
    const val VERSION = KYukiReflectionProperties.PROJECT_YUKIREFLECTION_CORE_VERSION

    /**
     * 配置 [KYukiReflection]
     */
    object Configs {

        /**
         * 是否使用Jvm(Java)的Callable(Member)
         *
         * Kotlin的Callable是懒加载的 使用时针对整个Class加载所有相关使用到的系列Callable 每个Class只加载同一系列一次 首次加载速度200ms以上 之后10ms以内
         * 开启后将先获得Jvm的Member再转换为Callable(Member)使用 而关闭时默认直接查找并使用Kotlin的Callable
         *
         * 此配置项将影响一下参数使用
         *
         * [KClass.constructors] --> [Class.getConstructors]
         *
         * [KClass.declaredPropertys] --> [Class.getDeclaredFields]
         *
         * [KClass.declaredFunctions] --> [Class.getDeclaredMethods]
         */
        var isUseJvmObtainCallables = false

        /**
         * 配置 [KYLog.Configs] 相关参数
         * @param initiate 方法体
         */
        inline fun debugLog(initiate: KYLog.Configs.() -> Unit) = KYLog.Configs.apply(initiate).build()

        /**
         * 是否开启调试模式 - 默认不启用
         *
         * 启用后将交由日志输出管理器打印详细日志 (例如反射查找功能的耗时) 到控制台
         *
         * 当 [KYLog.Configs.isEnable] 关闭后 [isDebug] 也将同时关闭
         */
        var isDebug = false


        /** 结束方法体 */
        internal fun build() = Unit
    }

    /**
     * 配置 [KYukiReflection] 相关参数
     * @param initiate 方法体
     */
    inline fun configs(initiate: Configs.() -> Unit) = Configs.apply(initiate).build()
}