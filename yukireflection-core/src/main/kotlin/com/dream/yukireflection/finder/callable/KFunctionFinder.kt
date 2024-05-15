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
 * This file is created by fankes on 2022/2/4.
 * This file is modified by fankes on 2023/1/21.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST", "KotlinConstantConditions", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package com.dream.yukireflection.finder.callable

import com.dream.yukireflection.factory.hasExtends
import com.dream.yukireflection.factory.superclass
import com.dream.yukireflection.finder.base.KCallableBaseFinder
import com.dream.yukireflection.finder.callable.data.KFunctionRulesData
import com.dream.yukireflection.type.factory.KModifierConditions
import com.dream.yukireflection.finder.base.KBaseFinder
import com.dream.yukireflection.finder.tools.KReflectionTool
import com.dream.yukireflection.type.factory.KFunctionConditions
import com.dream.yukireflection.type.factory.KTypeConditions
import com.dream.yukireflection.type.factory.KParameterConditions
import com.dream.yukireflection.type.defined.UndefinedKotlin
import com.dream.yukireflection.type.defined.VagueKotlin
import com.highcapable.yukireflection.finder.type.factory.CountConditions
import com.highcapable.yukireflection.finder.type.factory.NameConditions
import com.highcapable.yukireflection.log.YLog
import com.highcapable.yukireflection.utils.factory.runBlocking
import com.dream.yukireflection.bean.KVariousClass
import com.dream.yukireflection.factory.isExtension
import com.dream.yukireflection.finder.callable.KPropertyFinder.Result.Instance
import java.lang.IllegalArgumentException
import kotlin.reflect.*

/**
 * [KFunction] 查找类
 *
 * 可通过指定类型查找指定 [KFunction] 或一组 [KFunction]
 * @param classSet 当前需要查找的 [KClass] 实例
 */
class KFunctionFinder constructor(override val classSet: KClass<*>? = null) : KCallableBaseFinder(tag = TAG_FUNCTION, classSet) {

    override var rulesData = KFunctionRulesData()

    /** 当前使用的 [classSet] */
    private var usedClassSet = classSet

    /** 当前重查找结果回调 */
    private var remedyPlansCallback: (() -> Unit)? = null

    /**
     * 设置 [KFunction] 名称
     *
     * - 若不填写名称则必须存在一个其它条件
     * @return [String]
     */
    var name
        get() = rulesData.name
        set(value) {
            rulesData.name = value
        }

    /**
     * 设置 [KFunction] 参数个数
     *
     * 你可以不使用 [param] 指定参数类型而是仅使用此变量指定参数个数
     *
     * 若参数个数小于零则忽略并使用 [param]
     * @return [Int]
     */
    var paramCount
        get() = rulesData.paramCount
        set(value) {
            rulesData.paramCount = value
        }

    /**
     * 设置 [KFunction] 返回值
     *
     * - 只能是 [KClassifier]/[KClass]/[KTypeParameter]、[KType]、[String]、[KVariousClass]、[KGenericClass]
     *
     * - 可不填写返回值
     * @return [Any] or null
     */
    var returnType
        get() = rulesData.returnType
        set(value) {
            rulesData.returnType = value.compat()
        }

    /**
     * 设置 [KFunction] 标识符筛选条件
     *
     * - 可不设置筛选条件
     *
     * - 存在多个 [KBaseFinder.IndexTypeCondition] 时除了 [order] 只会生效最后一个
     * @param conditions 条件方法体
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun modifiers(conditions: KModifierConditions): IndexTypeCondition {
        rulesData.modifiers = conditions
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 设置 [KFunction] 空参数、无参数
     *
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun emptyParam() = paramCount(num = 0)

    /**
     * 设置 [KFunction] 参数
     *
     * 如果同时使用了 [paramCount] 则 [paramType] 的数量必须与 [paramCount] 完全匹配
     *
     * 如果 [KFunction] 中存在一些无意义又很长的类型 - 你可以使用 [VagueKotlin] 来替代它
     *
     * 例如下面这个参数结构 ↓
     *
     * ```java
     * void foo(String var1, boolean var2, com.demo.Test var3, int var4)
     * ```
     *
     * 此时就可以简单地写作 ↓
     *
     * ```kotlin
     * param(StringType, BooleanType, VagueType, IntType)
     * ```
     *
     * - 无参 [KFunction] 请使用 [emptyParam] 设置查找条件
     *
     * - 有参 [KFunction] 必须使用此方法设定参数或使用 [paramCount] 指定个数
     *
     * - 存在多个 [KBaseFinder.IndexTypeCondition] 时除了 [order] 只会生效最后一个
     * @param paramType 参数类型数组 - 只能是 [KClassifier]/[KClass]/[KTypeParameter]、[KGenericClass]、[KType]、[KParameter]、[KParameter.Kind]、[String]、[KVariousClass]
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun param(vararg paramType: Any): IndexTypeCondition {
        if (paramType.isEmpty()) error("paramTypes is empty, please use emptyParam() instead")
        rulesData.paramTypes = mutableListOf<Any>().apply { paramType.forEach { add(it.compat(TAG_PARAMETER) ?: UndefinedKotlin) } }.toTypedArray()
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 设置 [KFunction] 参数条件
     *
     * 使用示例如下 ↓
     *
     * ```kotlin
     * param { it[1] == StringClass || it[2].name == "java.lang.String" }
     * ```
     *
     * - 无参 [KFunction] 请使用 [emptyParam] 设置查找条件
     *
     * - 有参 [KFunction] 必须使用此方法设定参数或使用 [paramCount] 指定个数
     *
     * - 存在多个 [KBaseFinder.IndexTypeCondition] 时除了 [order] 只会生效最后一个
     * @param conditions 条件方法体
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun param(conditions: KParameterConditions): IndexTypeCondition {
        rulesData.paramTypesConditions = conditions
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 顺序筛选字节码的下标
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun order() = IndexTypeCondition(IndexConfigType.ORDER)

    /**
     * 设置 [KFunction] 名称
     *
     * - 若不填写名称则必须存在一个其它条件
     *
     * - 存在多个 [KBaseFinder.IndexTypeCondition] 时除了 [order] 只会生效最后一个
     * @param value 名称
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun name(value: String): IndexTypeCondition {
        rulesData.name = value
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 设置 [KFunction] 名称条件
     *
     * - 若不填写名称则必须存在一个其它条件
     *
     * - 存在多个 [KBaseFinder.IndexTypeCondition] 时除了 [order] 只会生效最后一个
     * @param conditions 条件方法体
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun name(conditions: NameConditions): IndexTypeCondition {
        rulesData.nameConditions = conditions
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 设置 [KFunction] 参数个数
     *
     * 你可以不使用 [param] 指定参数类型而是仅使用此方法指定参数个数
     *
     * 若参数个数小于零则忽略并使用 [param]
     *
     * - 存在多个 [KBaseFinder.IndexTypeCondition] 时除了 [order] 只会生效最后一个
     * @param num 个数
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun paramCount(num: Int): IndexTypeCondition {
        rulesData.paramCount = num
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 设置 [KFunction] 参数个数范围
     *
     * 你可以不使用 [param] 指定参数类型而是仅使用此方法指定参数个数范围
     *
     * 使用示例如下 ↓
     *
     * ```kotlin
     * paramCount(1..5)
     * ```
     *
     * - 存在多个 [KBaseFinder.IndexTypeCondition] 时除了 [order] 只会生效最后一个
     * @param numRange 个数范围
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun paramCount(numRange: IntRange): IndexTypeCondition {
        rulesData.paramCountRange = numRange
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 设置 [KFunction] 参数个数条件
     *
     * 你可以不使用 [param] 指定参数类型而是仅使用此方法指定参数个数条件
     *
     * 使用示例如下 ↓
     *
     * ```kotlin
     * paramCount { it >= 5 || it.isZero() }
     * ```
     *
     * - 存在多个 [KBaseFinder.IndexTypeCondition] 时除了 [order] 只会生效最后一个
     * @param conditions 条件方法体
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun paramCount(conditions: CountConditions): IndexTypeCondition {
        rulesData.paramCountConditions = conditions
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 设置 [KFunction] 返回值
     *
     * - 可不填写返回值
     *
     * - 存在多个 [KBaseFinder.IndexTypeCondition] 时除了 [order] 只会生效最后一个
     * @param value 个数
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun returnType(value: Any): IndexTypeCondition {
        rulesData.returnType = value.compat()
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 设置 [KFunction] 返回值条件
     *
     * - 可不填写返回值
     *
     * 使用示例如下 ↓
     *
     * ```kotlin
     * returnType { it == StringClass || it.name == "java.lang.String" }
     * ```
     *
     * - 存在多个 [KBaseFinder.IndexTypeCondition] 时除了 [order] 只会生效最后一个
     * @param conditions 条件方法体
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun returnType(conditions: KTypeConditions): IndexTypeCondition {
        rulesData.returnTypeConditions = conditions
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 设置在 [classSet] 的所有父类中查找当前 [KFunction]
     *
     * - 若当前 [classSet] 的父类较多可能会耗时 - API 会自动循环到父类继承是 [Any] 前的最后一个类
     * @param isOnlySuperClass 是否仅在当前 [classSet] 的父类中查找 - 若父类是 [Any] 则不会生效
     */
    fun superClass(isOnlySuperClass: Boolean = false) {
        rulesData.isFindInSuper = true
        if (isOnlySuperClass && classSet?.hasExtends == true) usedClassSet = classSet.superclass
    }

    /**
     * 得到 [KFunction] 或一组 [KFunction]
     * @return [MutableList]<[KFunction]>
     * @throws NoSuchMethodError 如果找不到 [KFunction]
     */
    private val result get() = KReflectionTool.findFunctions(usedClassSet, rulesData)

    /**
     * 设置实例
     * @param functions 当前找到的 [KFunction] 数组
     */
    private fun setInstance(functions: MutableList<KFunction<*>>) {
        callableInstances.clear()
        functions.takeIf { it.isNotEmpty() }?.forEach { callableInstances.add(it) }
    }

    /** 得到 [KFunction] 结果 */
    private fun internalBuild() {
        if (classSet == null) error(CLASSSET_IS_NULL)
        runBlocking {
            setInstance(result)
        }.result { ms ->
            callableInstances.takeIf { it.isNotEmpty() }?.forEach { debugMsg(msg = "Find KFunction [$it] takes ${ms}ms") }
        }
    }

    override fun build() = runCatching {
        internalBuild()
        Result()
    }.getOrElse {
        errorMsg(e = it)
        Result(isNoSuch = true, it)
    }

    override fun failure(throwable: Throwable?) = Result(isNoSuch = true, throwable)

    /**
     * [KFunction] 重查找实现类
     *
     * 可累计失败次数直到查找成功
     */
    inner class RemedyPlan internal constructor() {

        /** 失败尝试次数数组 */
        private val remedyPlans = mutableListOf<Pair<KFunctionFinder, Result>>()

        /**
         * 创建需要重新查找的 [KFunction]
         *
         * 你可以添加多个备选 [KFunction] - 直到成功为止
         *
         * 若最后依然失败 - 将停止查找并输出错误日志
         * @param initiate 方法体
         * @return [Result] 结果
         */
        inline fun function(initiate: KFunctionConditions) = Result().apply { remedyPlans.add(KFunctionFinder(classSet).apply(initiate) to this) }

        /** 开始重查找 */
        internal fun build() {
            if (classSet == null) return
            if (remedyPlans.isNotEmpty()) {
                val errors = mutableListOf<Throwable>()
                var isFindSuccess = false
                remedyPlans.forEachIndexed { index, plan ->
                    runCatching {
                        runBlocking {
                            setInstance(plan.first.result)
                        }.result { ms ->
                            callableInstances.takeIf { it.isNotEmpty() }?.forEach { debugMsg(msg = "Find KFunction [$it] takes ${ms}ms") }
                        }
                        isFindSuccess = true
                        plan.second.onFindCallback?.invoke(callableInstances.functions())
                        remedyPlansCallback?.invoke()
                        callableInstances.takeIf { it.isNotEmpty() }
                            ?.forEach { debugMsg(msg = "RemedyPlan successed after ${index + 1} attempts of KFunction [$it]") }
                        return
                    }.onFailure { errors.add(it) }
                }
                if (isFindSuccess) return
                errorMsg(msg = "RemedyPlan failed after ${remedyPlans.size} attempts", es = errors, isAlwaysMode = true)
                remedyPlans.clear()
            } else YLog.warn(msg = "RemedyPlan is empty, forgot it?")
        }

        /**
         * [RemedyPlan] 结果实现类
         *
         * 可在这里处理是否成功的回调
         */
        inner class Result internal constructor() {

            /** 找到结果时的回调 */
            internal var onFindCallback: (MutableList<KFunction<*>>.() -> Unit)? = null

            /**
             * 当找到结果时
             * @param initiate 回调
             */
            fun onFind(initiate: MutableList<KFunction<*>>.() -> Unit) {
                onFindCallback = initiate
            }
        }
    }

    /**
     * [KFunction] 查找结果实现类
     * @param isNoSuch 是否没有找到 [KFunction] - 默认否
     * @param throwable 错误信息
     */
    inner class Result internal constructor(
        val isNoSuch: Boolean = false,
        internal val throwable: Throwable? = null
    ) : BaseResult {

        /**
         * 创建监听结果事件方法体
         * @param initiate 方法体
         * @return [Result] 可继续向下监听
         */
        inline fun result(initiate: Result.() -> Unit) = apply(initiate)

        /**
         * 获得 [KFunction] 实例处理类
         *
         * - 若有多个 [KFunction] 结果只会返回第一个
         *
         * - 在 [memberInstances] 结果为空时使用此方法将无法获得对象
         *
         * - 若你设置了 [remedys] 请使用 [wait] 回调结果方法
         * @param instance 所在实例
         * @return [Instance]
         */
        fun get(instance: Any? = null) = Instance(instance, give())

        /**
         * 获得 [KFunction] 实例处理类数组
         *
         * - 返回全部查找条件匹配的多个 [KFunction] 实例结果
         *
         * - 在 [memberInstances] 结果为空时使用此方法将无法获得对象
         *
         * - 若你设置了 [remedys] 请使用 [waitAll] 回调结果方法
         * @param instance 所在实例
         * @return [MutableList]<[Instance]>
         */
        fun all(instance: Any? = null) =
            mutableListOf<Instance>().apply { giveAll().takeIf { it.isNotEmpty() }?.forEach { add(Instance(instance, it)) } }

        /**
         * 得到 [KFunction] 本身
         *
         * - 若有多个 [KFunction] 结果只会返回第一个
         *
         * - 在查找条件找不到任何结果的时候将返回 null
         * @return [KFunction] or null
         */
        fun give() = giveAll().takeIf { it.isNotEmpty() }?.first()

        /**
         * 得到 [KFunction] 本身数组
         *
         * - 返回全部查找条件匹配的多个 [KFunction] 实例
         *
         * - 在查找条件找不到任何结果的时候将返回空的 [MutableList]
         * @return [MutableList]<[KFunction]>
         */
        fun giveAll() = callableInstances.takeIf { it.isNotEmpty() }?.functions() ?: mutableListOf()

        /**
         * 获得 [KFunction] 实例处理类
         *
         * - 若有多个 [KFunction] 结果只会返回第一个
         *
         * - 若你设置了 [remedys] 必须使用此方法才能获得结果
         *
         * - 若你没有设置 [remedys] 此方法将不会被回调
         * @param instance 所在实例
         * @param initiate 回调 [Instance]
         */
        fun wait(instance: Any? = null, initiate: Instance.() -> Unit) {
            if (callableInstances.isNotEmpty()) initiate(get(instance))
            else remedyPlansCallback = { initiate(get(instance)) }
        }

        /**
         * 获得 [KFunction] 实例处理类数组
         *
         * - 返回全部查找条件匹配的多个 [KFunction] 实例结果
         *
         * - 若你设置了 [remedys] 必须使用此方法才能获得结果
         *
         * - 若你没有设置 [remedys] 此方法将不会被回调
         * @param instance 所在实例
         * @param initiate 回调 [MutableList]<[Instance]>
         */
        fun waitAll(instance: Any? = null, initiate: MutableList<Instance>.() -> Unit) {
            if (callableInstances.isNotEmpty()) initiate(all(instance))
            else remedyPlansCallback = { initiate(all(instance)) }
        }

        /**
         * 创建 [KFunction] 重查找功能
         *
         * 当你遇到一种 [KFunction] 可能存在不同形式的存在时
         *
         * 可以使用 [RemedyPlan] 重新查找它 - 而没有必要使用 [onNoSuchFunction] 捕获异常二次查找 [KFunction]
         *
         * 若第一次查找失败了 - 你还可以在这里继续添加此方法体直到成功为止
         * @param initiate 方法体
         * @return [Result] 可继续向下监听
         */
        inline fun remedys(initiate: RemedyPlan.() -> Unit): Result {
            isUsingRemedyPlan = true
            if (isNoSuch) RemedyPlan().apply(initiate).build()
            return this
        }

        /**
         * 监听找不到 [KFunction] 时
         *
         * - 只会返回第一次的错误信息 - 不会返回 [RemedyPlan] 的错误信息
         * @param result 回调错误
         * @return [Result] 可继续向下监听
         */
        inline fun onNoSuchFunction(result: (Throwable) -> Unit): Result {
            if (isNoSuch) result(throwable ?: Throwable("Initialization Error"))
            return this
        }

        /**
         * 忽略异常并停止打印任何错误日志
         *
         * - 此时若要监听异常结果 - 你需要手动实现 [onNoSuchFunction] 方法
         * @return [Result] 可继续向下监听
         */
        fun ignored(): Result {
            isIgnoreErrorLogs = true
            return this
        }

        /**
         * [KFunction] 实例处理类
         *
         * - 请使用 [get]、[wait]、[all]、[waitAll] 方法来获取 [Instance]
         * @param instance 当前 [KFunction] 所在类的实例对象
         * @param function 当前 [KFunction] 实例对象
         */
        inner class Instance internal constructor(private val instance: Any?, private val function: KFunction<*>?) {

            private var extension:Any? = null

            /**
             * 修改 [extension] Receiver
             *
             * 当此属性是拓展属性时，你可能需要额外的一个this属性进行设置
             *
             * @param extension 拓展属性的thisRef
             * @return [KFunction] 实例处理类
             */
            fun receiver(extension:Any?): Instance {
                this.extension = extension
                return this
            }

            /**
             * 执行 [KFunction]
             * @param args 方法参数
             * @return [Any] or null
             */
            private fun baseCall(vararg args: Any?) = runCatching {
                if (function?.isExtension == true) {
                    if (instance != null) function.call(instance,extension, *args) else function.call(extension,*args)
                }else
                if (instance != null) function?.call(instance, *args) else function?.call(*args) }.getOrElse { if (it is IllegalArgumentException) errorMsg("An error occurred in the number of parameters. Check whether the instance exists or whether the number of parameters is correct.",it) else throw it }

            /**
             * 执行 [KFunction] - 不指定返回值类型
             * @param args 方法参数
             * @return [Any] or null
             */
            fun call(vararg args: Any?) = baseCall(*args)

            /**
             * 执行 [KFunction] - 指定 [T] 返回值类型
             * @param args 方法参数
             * @return [T] or null
             */
            fun <T> invoke(vararg args: Any?) = baseCall(*args) as? T?

            /**
             * 执行 [KFunction] - 指定 [Byte] 返回值类型
             *
             * - 请确认目标变量的类型 - 发生错误会返回 null
             * @param args 方法参数
             * @return [Byte] or null
             */
            fun byte(vararg args: Any?) = invoke<Byte?>(*args)

            /**
             * 执行 [KFunction] - 指定 [Int] 返回值类型
             *
             * - 请确认目标 [KFunction] 的返回值 - 发生错误会返回默认值
             * @param args 方法参数
             * @return [Int] 取不到返回 0
             */
            fun int(vararg args: Any?) = invoke(*args) ?: 0

            /**
             * 执行 [KFunction] - 指定 [Long] 返回值类型
             *
             * - 请确认目标 [KFunction] 的返回值 - 发生错误会返回默认值
             * @param args 方法参数
             * @return [Long] 取不到返回 0L
             */
            fun long(vararg args: Any?) = invoke(*args) ?: 0L

            /**
             * 执行 [KFunction] - 指定 [Short] 返回值类型
             *
             * - 请确认目标 [KFunction] 的返回值 - 发生错误会返回默认值
             * @param args 方法参数
             * @return [Short] 取不到返回 0
             */
            fun short(vararg args: Any?) = invoke<Short?>(*args) ?: 0

            /**
             * 执行 [KFunction] - 指定 [Double] 返回值类型
             *
             * - 请确认目标 [KFunction] 的返回值 - 发生错误会返回默认值
             * @param args 方法参数
             * @return [Double] 取不到返回 0.0
             */
            fun double(vararg args: Any?) = invoke(*args) ?: 0.0

            /**
             * 执行 [KFunction] - 指定 [Float] 返回值类型
             *
             * - 请确认目标 [KFunction] 的返回值 - 发生错误会返回默认值
             * @param args 方法参数
             * @return [Float] 取不到返回 0f
             */
            fun float(vararg args: Any?) = invoke(*args) ?: 0f

            /**
             * 执行 [KFunction] - 指定 [String] 返回值类型
             * @param args 方法参数
             * @return [String] 取不到返回 ""
             */
            fun string(vararg args: Any?) = invoke(*args) ?: ""

            /**
             * 执行 [KFunction] - 指定 [Char] 返回值类型
             * @param args 方法参数
             * @return [Char] 取不到返回 ' '
             */
            fun char(vararg args: Any?) = invoke(*args) ?: ' '

            /**
             * 执行 [KFunction] - 指定 [Boolean] 返回值类型
             *
             * - 请确认目标 [KFunction] 的返回值 - 发生错误会返回默认值
             * @param args 方法参数
             * @return [Boolean] 取不到返回 false
             */
            fun boolean(vararg args: Any?) = invoke(*args) ?: false

            /**
             * 执行 [KFunction] - 指定 [Array] 返回值类型 - 每项类型 [T]
             *
             * - 请确认目标 [KFunction] 的返回值 - 发生错误会返回空数组
             * @return [Array] 取不到返回空数组
             */
            inline fun <reified T> array(vararg args: Any?) = invoke(*args) ?: arrayOf<T>()

            /**
             * 执行 [KFunction] - 指定 [List] 返回值类型 - 每项类型 [T]
             *
             * - 请确认目标 [KFunction] 的返回值 - 发生错误会返回空数组
             * @return [List] 取不到返回空数组
             */
            inline fun <reified T> list(vararg args: Any?) = invoke(*args) ?: listOf<T>()

            override fun toString() = "[${function?.name ?: "<empty>"}] in [${instance?.javaClass?.name ?: "<empty>"}]"
        }
    }
}