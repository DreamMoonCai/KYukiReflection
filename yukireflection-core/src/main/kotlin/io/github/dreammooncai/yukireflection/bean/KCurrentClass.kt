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
 * This file is created by fankes on 2022/4/4.
 * This file is modified by fankes on 2023/1/21.
 */
@file:Suppress("unused", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package io.github.dreammooncai.yukireflection.bean

import io.github.dreammooncai.yukireflection.factory.*
import io.github.dreammooncai.yukireflection.type.factory.KFunctionConditions
import io.github.dreammooncai.yukireflection.type.factory.KPropertyConditions
import io.github.dreammooncai.yukireflection.finder.callable.KPropertyFinder
import io.github.dreammooncai.yukireflection.finder.callable.KFunctionFinder
import io.github.dreammooncai.yukireflection.type.factory.KClassConditions
import io.github.dreammooncai.yukireflection.type.factory.KTypeBuildConditions
import kotlin.reflect.KClass

/**
 * 当前实例的类操作对象
 * @param classSet 当前实例的 [KClass]
 * @param instance 当前实例本身
 */
class KCurrentClass internal constructor(private val classSet: KClass<*>, internal val instance: Any) {

    /** 是否开启忽略错误警告功能 */
    internal var isIgnoreErrorLogs = false

    /**
     * 获得当前 [classSet] 的 [KClass.name]
     * @return [String]
     */
    val name get() = classSet.name

    /**
     * 获得当前 [classSet] 的 [KClass.simpleNameOrJvm]
     * @return [String]
     */
    val simpleName get() = classSet.simpleNameOrJvm

    /**
     * 获得当前实例中的泛型操作对象
     *
     *     class A<T> --> generic().type = A<*>
     *
     * @return [KGenericClass]
     */
    fun generic() = classSet.generic()

    /**
     * 获得当前实例中的泛型操作对象
     *
     * [KTypeBuildConditions]同来筛选如何构建这个this类型
     *
     * @param params 类型所需的参数 --> A<T> generic(String::class).type -> A<String>
     * @param initiate 实例方法体
     * @return [KGenericClass]
     */
    inline fun generic(vararg params: Any,initiate: KTypeBuildConditions) = classSet.generic(*params, initiate = initiate)

    /**
     * 获得当前实例中的泛型父类
     *
     * 如果当前实例不存在泛型父类将返回 null
     * @return [KGenericClass] or null
     */
    fun genericSuper() = runCatching { classSet.genericSuper() }.getOrNull()

    /**
     * 获得当前 [KClass] 的父类中来自尖括号的泛型操作对象
     *
     * [KClassConditions]同来筛选来自哪个父类/父接口
     *
     * 如果当前实例不存在泛型父类将返回 null
     * @param initiate 实例方法体
     * @return [KGenericClass] or null
     */
    inline fun genericSuper(initiate: KClassConditions) = runCatching { classSet.genericSuper(initiate) }.getOrNull()

    /**
     * 调用父类实例 只获取非接口的父类实例
     * @return [SuperClass]
     */
    fun superClass() = SuperClass(classSet.superclass)

    /**
     * 调用当前实例中的变量
     * @param initiate 查找方法体
     * @return [KPropertyFinder.Result.Instance]
     */
    inline fun property(initiate: KPropertyConditions) = classSet.property(initiate).result { if (isIgnoreErrorLogs) ignored() }.get(instance)

    /**
     * 调用当前实例中的方法
     * @param initiate 查找方法体
     * @return [KFunctionFinder.Result.Instance]
     */
    inline fun function(initiate: KFunctionConditions) = classSet.function(initiate).result { if (isIgnoreErrorLogs) ignored() }.get(instance)

    /**
     * 当前类的父类实例的类操作对象
     *
     * - 请使用 [superClass] 方法来获取 [SuperClass]
     * @param superClassSet 父类 [KClass] 对象
     */
    inner class SuperClass internal constructor(private val superClassSet: KClass<*>) {

        /**
         * 获得当前 [classSet] 中父类的 [KClass.name]
         * @return [String]
         */
        val name get() = superClassSet.name

        /**
         * 获得当前 [classSet] 中父类的 [KClass.simpleNameOrJvm]
         * @return [String]
         */
        val simpleName get() = superClassSet.simpleNameOrJvm

        /**
         * 获得当前 [classSet] 中父类的泛型操作对象
         *
         *     class A<T> --> generic().type = A<*>
         *
         * @return [KGenericClass]
         */
        fun generic() = superClassSet.generic()

        /**
         * 获得当前 [classSet] 中父类的泛型操作对象
         *
         * [KTypeBuildConditions]同来筛选如何构建这个this类型
         *
         * @param params 类型所需的参数 --> A<T> generic(String::class).type -> A<String>
         * @param initiate 实例方法体
         * @return [KGenericClass]
         */
        inline fun generic(vararg params: Any,initiate: KTypeBuildConditions) = superClassSet.generic(*params, initiate = initiate)

        /**
         * 获得当前 [classSet] 中父类的泛型父类
         *
         * 如果当前实例不存在泛型父类将返回 null
         * @return [KGenericClass] or null
         */
        fun genericSuper() = runCatching { superClassSet.genericSuper() }.getOrNull()

        /**
         * 获得当前 [classSet] 中父类 [KClass] 的父类中来自尖括号的泛型操作对象
         *
         * [KClassConditions]同来筛选来自哪个父类/父接口
         *
         * 如果当前实例不存在泛型父类将返回 null
         * @param initiate 实例方法体
         * @return [KGenericClass] or null
         */
        inline fun genericSuper(initiate: KClassConditions) = runCatching { superClassSet.genericSuper(initiate) }.getOrNull()

        /**
         * 调用父类实例中的变量
         * @param initiate 查找方法体
         * @return [KPropertyFinder.Result.Instance]
         */
        inline fun property(initiate: KPropertyConditions) = superClassSet.property(initiate).result { if (isIgnoreErrorLogs) ignored() }.get(instance)

        /**
         * 调用父类实例中的方法
         * @param initiate 查找方法体
         * @return [KFunctionFinder.Result.Instance]
         */
        inline fun function(initiate: KFunctionConditions) = superClassSet.function(initiate).result { if (isIgnoreErrorLogs) ignored() }.get(instance)

        override fun toString() = "KCurrentClass super [$superClassSet]"
    }

    override fun toString() = "KCurrentClass [$classSet]"
}