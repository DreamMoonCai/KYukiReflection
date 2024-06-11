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

import com.dream.yukireflection.finder.base.KCallableBaseFinder
import com.dream.yukireflection.type.defined.UndefinedKotlin
import com.dream.yukireflection.type.defined.VagueKotlin
import com.dream.yukireflection.finder.base.KBaseFinder
import com.dream.yukireflection.bean.KVariousClass
import com.dream.yukireflection.factory.*
import com.dream.yukireflection.factory.name
import com.dream.yukireflection.finder.callable.KFunctionFinder.Result
import com.dream.yukireflection.finder.callable.KPropertyFinder.Result.Instance
import com.dream.yukireflection.finder.callable.data.KConstructorRulesData
import com.dream.yukireflection.finder.tools.KReflectionTool
import com.dream.yukireflection.log.KYLog
import com.dream.yukireflection.type.factory.*
import com.dream.yukireflection.type.factory.KConstructorConditions
import com.dream.yukireflection.type.factory.KCountConditions
import com.dream.yukireflection.type.factory.KModifierConditions
import com.dream.yukireflection.type.factory.KParameterConditions
import com.dream.yukireflection.utils.factory.runBlocking
import java.lang.IllegalArgumentException
import kotlin.reflect.*
import kotlin.reflect.KClassifier
import kotlin.reflect.KFunction
import kotlin.reflect.KTypeParameter
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaConstructor

/**
 * Constructor [KFunction] 查找类
 *
 * 可通过指定类型查找指定 Constructor [KFunction] 或一组 Constructor [KFunction]
 * @param classSet 当前需要查找的 [KClass] 实例
 */
class KConstructorFinder internal constructor(override val classSet: KClass<*>? = null) : KCallableBaseFinder(tag = TAG_CONSTRUCTOR, classSet) {

    override var rulesData = KConstructorRulesData()

    /** 当前使用的 [classSet] */
    private var usedClassSet = classSet

    /** 当前重查找结果回调 */
    private var remedyPlansCallback: (() -> Unit)? = null

    /**
     * 将此构造函数相关内容附加到此查找器
     *
     * 将影响[param]
     *
     * @param R 返回类型/构造目标类的类型
     * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
     * @param isUseMember 是否将构造函数转换为JavaMethod再进行附加 - 即使为false当构造函数附加错误时依然会尝试JavaMethod - 为true时会导致类型擦除
     */
    @JvmName("attach_exp")
    fun <R> KFunction<R>.attach(loader: ClassLoader? = null,isUseMember:Boolean = false){
        attach(this,loader,isUseMember)
    }

    /**
     * 将此构造函数相关内容附加到此查找器
     *
     * 将影响[param]
     *
     * @param R 返回类型/构造目标类的类型
     * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
     * @param isUseMember 是否将构造函数转换为JavaMethod再进行附加 - 即使为false当构造函数附加错误时依然会尝试JavaMethod - 为true时会导致类型擦除
     */
    fun <R> attach(function: KFunction<R>,loader: ClassLoader? = null,isUseMember:Boolean = false){
        fun KClass<*>.toClass() = if (loader == null) this else toKClass(loader)

        fun attachMember(e:Throwable? = null){
            val method = function.javaConstructorNoError ?: function.refImpl?.javaConstructorNoError ?: let {
                errorMsg("Converting javaMethod failed !!!", e)
                return
            }
            if (method.parameterTypes.isEmpty())
                emptyParam()
            else
                param(*method.parameterTypes.map { it.kotlin.toClass() }.toTypedArray())
        }
        fun attachCallable(function: KFunction<*>){
            if (function.valueParameters.isEmpty())
                emptyParam()
            else
                param(*function.valueParameters.map {
                    if (loader != null)
                        it.kotlin.toClass()
                    else it
                }.toTypedArray())
        }
        if (isUseMember)
            attachMember()
        else runCatching {
            attachCallable(function)
        }.getOrNull() ?: runCatching {
            attachCallable(function.refImpl!!)
        }.getOrElse {
            attachMember(it)
        }
    }

    /**
     * 设置 Constructor [KFunction] 参数个数
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
     * 设置 Constructor [KFunction] 标识符筛选条件
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
     * 设置 Constructor [KFunction] 空参数、无参数
     *
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun emptyParam() = paramCount(num = 0)

    /**
     * 设置 Constructor [KFunction] 参数
     *
     * 如果同时使用了 [paramCount] 则 [paramType] 的数量必须与 [paramCount] 完全匹配
     *
     * 如果 Constructor [KFunction] 中存在一些无意义又很长的类型 - 你可以使用 [VagueKotlin] 来替代它
     *
     * 例如下面这个参数结构 ↓
     *
     * ```java
     * Foo(String var1, boolean var2, com.demo.Test var3, int var4)
     * ```
     *
     * 此时就可以简单地写作 ↓
     *
     * ```kotlin
     * param(StringType, BooleanType, VagueType, IntType)
     * ```
     *
     * - 无参 Constructor [KFunction] 请使用 [emptyParam] 设置查找条件
     *
     * - 有参 Constructor [KFunction] 必须使用此方法设定参数或使用 [paramCount] 指定个数
     *
     * - 存在多个 [KBaseFinder.IndexTypeCondition] 时除了 [order] 只会生效最后一个
     * @param paramType 参数类型数组 - 只能是 [Class]/[KClassifier]/[KClass]/[KTypeParameter]、[KGenericClass]、[KType]、[KParameter]、[KParameter.Kind]、[String]、[KVariousClass]
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun param(vararg paramType: Any): IndexTypeCondition {
        if (paramType.isEmpty()) error("paramTypes is empty, please use emptyParam() instead")
        rulesData.paramTypes = mutableListOf<Any>().apply { paramType.forEach { add(it.compat(TAG_PARAMETER) ?: UndefinedKotlin) } }.toTypedArray()
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 设置 Constructor [KFunction] 参数条件
     *
     * 使用示例如下 ↓
     *
     * ```kotlin
     * param { it[1] == StringClass || it[2].name == "java.lang.String" }
     * ```
     *
     * - 无参 Constructor [KFunction] 请使用 [emptyParam] 设置查找条件
     *
     * - 有参 Constructor [KFunction] 必须使用此方法设定参数或使用 [paramCount] 指定个数
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
     * 设置 Constructor[KFunction] 参数名称
     *
     * 如果 Constructor[KFunction] 中存在一些不太清楚的参数名称 - 你可以使用 [VagueKotlin].name 或者 空字符串 或者 "null" 来替代它
     *
     * 例如下面这个参数结构 ↓
     *
     * ```java
     * Foo(String count, boolean un$abc, com.demo.Test ends)
     * ```
     *
     * 此时就可以简单地写作 ↓
     *
     * ```kotlin
     * paramName("count","","ends")
     * ```
     *
     * @param paramName 参数名称数组
     */
    fun paramName(vararg paramName: String) {
        if (paramName.isEmpty()) error("paramTypes is empty, please use emptyParam() instead")
        rulesData.paramNames = paramName
    }


    /**
     * 设置 Constructor[KFunction] 参数名称条件
     *
     * 使用示例如下 ↓
     *
     * ```kotlin
     * paramName { it.isNull() }
     * ```
     * @param conditions 条件方法体
     */
    fun paramName(conditions: KNamesConditions) {
        rulesData.paramNamesConditions = conditions
    }

    /**
     * 顺序筛选字节码的下标
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun order() = IndexTypeCondition(IndexConfigType.ORDER)

    /**
     * 设置 Constructor [KFunction] 参数个数
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
     * 设置 Constructor [KFunction] 参数个数范围
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
     * 设置 Constructor [KFunction] 参数个数条件
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
    fun paramCount(conditions: KCountConditions): IndexTypeCondition {
        rulesData.paramCountConditions = conditions
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 设置在 [classSet] 的所有父类中查找当前 Constructor [KFunction]
     *
     * - 若当前 [classSet] 的父类较多可能会耗时 - API 会自动循环到父类继承是 [Any] 前的最后一个类
     * @param isOnlySuperClass 是否仅在当前 [classSet] 的父类中查找 - 若父类是 [Any] 则不会生效
     */
    fun superClass(isOnlySuperClass: Boolean = false) {
        rulesData.isFindInSuper = true
        if (isOnlySuperClass && classSet?.hasExtends == true) usedClassSet = classSet.superclass
    }

    /**
     * 得到 Constructor [KFunction] 或一组 Constructor [KFunction]
     * @return [MutableList]<Constructor [KFunction]>
     * @throws NoSuchMethodError 如果找不到 Constructor [KFunction]
     */
    private val result by lazy { KReflectionTool.findConstructors(usedClassSet, rulesData) }

    /**
     * 设置实例
     * @param constructors 当前找到的 Constructor [KFunction] 数组
     */
    private fun setInstance(constructors: MutableList<out KFunction<*>>) {
        callableInstances.clear()
        constructors.takeIf { it.isNotEmpty() }?.forEach { callableInstances.add(it) }
    }

    /** 得到 Constructor [KFunction] 结果 */
    private fun internalBuild() {
        if (classSet == null) error(CLASSSET_IS_NULL)
        runBlocking {
            setInstance(result)
        }.result { ms ->
            callableInstances.takeIf { it.isNotEmpty() }?.forEach { debugMsg(msg = "Find Constructor [$it] takes ${ms}ms") }
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
     * Constructor [KFunction] 重查找实现类
     *
     * 可累计失败次数直到查找成功
     */
    inner class RemedyPlan internal constructor() {

        /** 失败尝试次数数组 */
        private val remedyPlans = mutableSetOf<Pair<KConstructorFinder, Result>>()

        /**
         * 创建需要重新查找的 Constructor [KFunction]
         *
         * 你可以添加多个备选 Constructor [KFunction] - 直到成功为止
         *
         * 若最后依然失败 - 将停止查找并输出错误日志
         * @param initiate 方法体
         */
        inline fun constructor(initiate: KConstructorConditions) =
            Result().apply { remedyPlans.add(KConstructorFinder(classSet).apply(initiate) to this) }

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
                            callableInstances.takeIf { it.isNotEmpty() }?.forEach { debugMsg(msg = "Find Constructor [$it] takes ${ms}ms") }
                        }
                        isFindSuccess = true
                        plan.second.onFindCallback?.invoke(callableInstances.constructors())
                        remedyPlansCallback?.invoke()
                        callableInstances.takeIf { it.isNotEmpty() }
                            ?.forEach { debugMsg(msg = "RemedyPlan successed after ${index + 1} attempts of Constructor [$it]") }
                        return
                    }.onFailure { errors.add(it) }
                }
                if (isFindSuccess) return
                errorMsg(msg = "RemedyPlan failed after ${remedyPlans.size} attempts", es = errors, isAlwaysMode = true)
                remedyPlans.clear()
            } else KYLog.warn(msg = "RemedyPlan is empty, forgot it?")
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
     * Constructor [KFunction] 查找结果实现类
     * @param isNoSuch 是否没有找到 Constructor [KFunction] - 默认否
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
         * 获得 Constructor [KFunction] 实例处理类
         *
         * - 若有多个 Constructor [KFunction] 结果只会返回第一个
         *
         * - 在 [callableInstances] 结果为空时使用此方法将无法获得对象
         *
         * - 若你设置了 [remedys] 请使用 [wait] 回调结果方法
         * @param isUseMember 是否优先将构造函数转换Java方式进行构造
         * @return [Instance]
         */
        fun get(isUseMember:Boolean = false) = Instance(give()).useMember(isUseMember)

        /**
         * 获得 Constructor [KFunction] 实例处理类数组
         *
         * - 返回全部查找条件匹配的多个 Constructor [KFunction] 实例结果
         *
         * - 在 [callableInstances] 结果为空时使用此方法将无法获得对象
         *
         * - 若你设置了 [remedys] 请使用 [waitAll] 回调结果方法
         * @param isUseMember 是否优先将构造函数转换Java方式进行构造
         * @return [MutableList]<[Instance]>
         */
        fun all(isUseMember:Boolean = false) = mutableListOf<Instance>().apply { giveAll().takeIf { it.isNotEmpty() }?.forEach { add(Instance(it).useMember(isUseMember)) } }

        /**
         * 得到 Constructor [KFunction] 本身
         *
         * - 若有多个 Constructor [KFunction] 结果只会返回第一个
         *
         * - 在查找条件找不到任何结果的时候将返回 null
         * @return Constructor [KFunction] or null
         */
        fun give() = giveAll().takeIf { it.isNotEmpty() }?.first()

        /**
         * 得到 Constructor [KFunction] 本身数组
         *
         * - 返回全部查找条件匹配的多个 Constructor [KFunction] 实例
         *
         * - 在查找条件找不到任何结果的时候将返回空的 [MutableList]
         * @return [MutableList]<Constructor [KFunction]>
         */
        fun giveAll() = callableInstances.takeIf { it.isNotEmpty() }?.constructors() ?: mutableListOf()

        /**
         * 获得 Constructor [KFunction] 实例处理类
         *
         * - 若有多个 Constructor [KFunction] 结果只会返回第一个
         *
         * - 若你设置了 [remedys] 必须使用此方法才能获得结果
         *
         * - 若你没有设置 [remedys] 此方法将不会被回调
         * @param initiate 回调 [Instance]
         */
        fun wait(initiate: Instance.() -> Unit) {
            if (callableInstances.isNotEmpty()) initiate(get())
            else remedyPlansCallback = { initiate(get()) }
        }

        /**
         * 获得 Constructor [KFunction] 实例处理类数组
         *
         * - 返回全部查找条件匹配的多个 Constructor [KFunction] 实例结果
         *
         * - 若你设置了 [remedys] 必须使用此方法才能获得结果
         *
         * - 若你没有设置 [remedys] 此方法将不会被回调
         * @param initiate 回调 [MutableList]<[Instance]>
         */
        fun waitAll(initiate: MutableList<Instance>.() -> Unit) {
            if (callableInstances.isNotEmpty()) initiate(all())
            else remedyPlansCallback = { initiate(all()) }
        }

        /**
         * 创建 Constructor [KFunction] 重查找功能
         *
         * 当你遇到一种 Constructor [KFunction] 可能存在不同形式的存在时
         *
         * 可以使用 [RemedyPlan] 重新查找它 - 而没有必要使用 [onNoSuchConstructor] 捕获异常二次查找 Constructor [KFunction]
         * @param initiate 方法体
         * @return [Result] 可继续向下监听
         */
        inline fun remedys(initiate: RemedyPlan.() -> Unit): Result {
            isUsingRemedyPlan = true
            if (isNoSuch) RemedyPlan().apply(initiate).build()
            return this
        }

        /**
         * 监听找不到 Constructor [KFunction] 时
         *
         * - 只会返回第一次的错误信息 - 不会返回 [RemedyPlan] 的错误信息
         * @param result 回调错误
         * @return [Result] 可继续向下监听
         */
        inline fun onNoSuchConstructor(result: (Throwable) -> Unit): Result {
            if (isNoSuch) result(throwable ?: Throwable("Initialization Error"))
            return this
        }

        /**
         * 忽略异常并停止打印任何错误日志
         *
         * - 此时若要监听异常结果 - 你需要手动实现 [onNoSuchConstructor] 方法
         * @return [Result] 可继续向下监听
         */
        fun ignored(): Result {
            isIgnoreErrorLogs = true
            return this
        }

        /**
         * Constructor [KFunction] 实例处理类
         *
         * 调用与创建目标实例类对象
         *
         * - 请使用 [get]、[wait]、[all]、[waitAll] 方法来获取 [Instance]
         * @param constructor 当前 Constructor [KFunction] 实例对象
         */
        inner class Instance internal constructor(private val constructor: KFunction<*>?) {

            /**
             * @see [useMember]
             */
            private var isUseMember = false

            /**
             * 是否将构造函数转换为Java方式构造
             *
             * 为true时实例执行将通过将 Kotlin构造函数 转换为 JavaMember 方式执行
             *
             * 如果目标属性无法用Java方式描述则此设置将会自动忽略
             *
             * - 开启时优先 Constructor > [KProperty.call]
             *
             * - 默认情况下只使用 [KProperty.call]
             *
             * @param use 是否优先将构造函数转换为Java方式进行构造
             * @return [Instance] 可继续向下监听
             */
            fun useMember(use:Boolean = true): Instance {
                this.isUseMember = use
                return this
            }

            /**
             * 执行 Constructor [KFunction] 创建目标实例
             * @param args Constructor [KFunction] 参数
             * @return [Any] or null
             */
            private fun baseCall(vararg args: Any?) = runCatching<Result,Any?> {
                if (isUseMember){
                    val constructor = constructor?.javaConstructorNoError
                    if (constructor != null){
                        constructor.isAccessible = true
                        return@runCatching constructor.newInstance(*args)
                    }
                }
                constructor?.call(*args)
            }.getOrElse { if (it is IllegalArgumentException) errorMsg("An error occurred in the number of parameters. Check whether the number of parameters is correct.",it) else throw it }

            /**
             * 执行 Constructor [KFunction] 创建目标实例 - 不指定目标实例类型
             * @param args Constructor [KFunction] 参数
             * @return [Any] or null
             */
            fun call(vararg args: Any?) = baseCall(*args)

            /**
             * 执行 Constructor [KFunction] 创建目标实例 - 指定 [T] 目标实例类型
             * @param args Constructor [KFunction] 参数
             * @return [T] or null
             */
            fun <T> newInstance(vararg args: Any?) = baseCall(*args) as? T?

            override fun toString() = "[${constructor?.name ?: "<empty>"}]"
        }
    }
}