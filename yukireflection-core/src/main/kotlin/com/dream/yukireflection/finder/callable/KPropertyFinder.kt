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
@file:Suppress("unused", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "KotlinConstantConditions", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package com.dream.yukireflection.finder.callable

import com.dream.yukireflection.factory.hasExtends
import com.dream.yukireflection.factory.superclass
import com.dream.yukireflection.finder.base.KCallableBaseFinder
import com.dream.yukireflection.finder.base.KBaseFinder
import com.dream.yukireflection.finder.callable.data.KPropertyRulesData
import com.dream.yukireflection.finder.tools.KReflectionTool
import com.dream.yukireflection.finder.type.factory.KModifierConditions
import com.dream.yukireflection.finder.type.factory.KTypeConditions
import com.dream.yukireflection.finder.type.factory.KPropertyConditions
import com.highcapable.yukireflection.bean.CurrentClass
import com.highcapable.yukireflection.factory.current
import com.highcapable.yukireflection.finder.type.factory.NameConditions
import com.highcapable.yukireflection.log.YLog
import com.highcapable.yukireflection.utils.factory.runBlocking
import kotlin.reflect.KProperty
import kotlin.reflect.KClass
import com.dream.yukireflection.bean.KVariousClass
import com.dream.yukireflection.factory.set
import java.lang.IllegalArgumentException

/**
 * [KProperty] 查找类
 *
 * 可通过指定类型查找指定 [KProperty] 或一组 [KProperty]
 * @param classSet 当前需要查找的 [KClass] 实例
 */
class KPropertyFinder constructor(override val classSet: KClass<*>? = null) : KCallableBaseFinder(tag = TAG_PROPERTY, classSet) {

    override var rulesData = KPropertyRulesData()

    /** 当前使用的 [classSet] */
    private var usedClassSet = classSet

    /** 当前重查找结果回调 */
    private var remedyPlansCallback: (() -> Unit)? = null

    /**
     * 设置 [KProperty] 名称
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
     * 设置 [KProperty] 类型
     *
     * - 只能是 [KClass]、[String]、[KVariousClass]
     *
     * - 可不填写类型
     * @return [Any] or null
     */
    var type
        get() = rulesData.type
        set(value) {
            rulesData.type = value.compat()
        }

    /**
     * 设置 [KProperty] 标识符筛选条件
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
     * 顺序筛选字节码的下标
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun order() = IndexTypeCondition(IndexConfigType.ORDER)

    /**
     * 设置 [KProperty] 名称
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
     * 设置 [KProperty] 名称条件
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
     * 设置 [KProperty] 类型
     *
     * - 可不填写类型
     *
     * - 存在多个 [KBaseFinder.IndexTypeCondition] 时除了 [order] 只会生效最后一个
     * @param value 类型 - 只能是 [KClass]、[String]、[KVariousClass]
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun type(value: Any): IndexTypeCondition {
        rulesData.type = value.compat()
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 设置 [KProperty] 类型条件
     *
     * - 可不填写类型
     *
     * 使用示例如下 ↓
     *
     * ```kotlin
     * type { it == StringClass || it.name == "java.lang.String" }
     * ```
     *
     * - 存在多个 [KBaseFinder.IndexTypeCondition] 时除了 [order] 只会生效最后一个
     * @param conditions 条件方法体
     * @return [KBaseFinder.IndexTypeCondition]
     */
    fun type(conditions: KTypeConditions): IndexTypeCondition {
        rulesData.typeConditions = conditions
        return IndexTypeCondition(IndexConfigType.MATCH)
    }

    /**
     * 设置在 [classSet] 的所有父类中查找当前 [KProperty]
     *
     * - 若当前 [classSet] 的父类较多可能会耗时 - API 会自动循环到父类继承是 [Any] 前的最后一个类
     * @param isOnlySuperClass 是否仅在当前 [classSet] 的父类中查找 - 若父类是 [Any] 则不会生效
     */
    fun superClass(isOnlySuperClass: Boolean = false) {
        rulesData.isFindInSuper = true
        if (isOnlySuperClass && classSet?.hasExtends == true) usedClassSet = classSet.superclass
    }

    /**
     * 得到 [KProperty] 或一组 [KProperty]
     * @return [MutableList]<[KProperty]>
     * @throws NoSuchFieldError 如果找不到 [KProperty]
     */
    private val result get() = KReflectionTool.findPropertys(usedClassSet, rulesData)

    /**
     * 设置实例
     * @param propertys 当前找到的 [KProperty] 数组
     */
    private fun setInstance(propertys: MutableList<KProperty<*>>) {
        callableInstances.clear()
        propertys.takeIf { it.isNotEmpty() }?.forEach { callableInstances.add(it) }
    }

    /** 得到 [KProperty] 结果 */
    private fun internalBuild() {
        if (classSet == null) error(CLASSSET_IS_NULL)
        runBlocking {
            setInstance(result)
        }.result { ms ->
            callableInstances.takeIf { it.isNotEmpty() }?.forEach { debugMsg(msg = "Find Property [$it] takes ${ms}ms") }
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
     * [KProperty] 重查找实现类
     *
     * 可累计失败次数直到查找成功
     */
    inner class RemedyPlan internal constructor() {

        /** 失败尝试次数数组 */
        private val remedyPlans = mutableSetOf<Pair<KPropertyFinder, Result>>()

        /**
         * 创建需要重新查找的 [KProperty]
         *
         * 你可以添加多个备选 [KProperty] - 直到成功为止
         *
         * 若最后依然失败 - 将停止查找并输出错误日志
         * @param initiate 方法体
         * @return [Result] 结果
         */
        inline fun property(initiate: KPropertyConditions) = Result().apply { remedyPlans.add(KPropertyFinder(classSet).apply(initiate) to this) }

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
                            callableInstances.takeIf { it.isNotEmpty() }?.forEach { debugMsg(msg = "Find Property [$it] takes ${ms}ms") }
                        }
                        isFindSuccess = true
                        plan.second.onFindCallback?.invoke(callableInstances.propertys())
                        remedyPlansCallback?.invoke()
                        callableInstances.takeIf { it.isNotEmpty() }
                            ?.forEach { debugMsg(msg = "RemedyPlan successed after ${index + 1} attempts of Property [$it]") }
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
            internal var onFindCallback: (MutableList<KProperty<*>>.() -> Unit)? = null

            /**
             * 当找到结果时
             * @param initiate 回调
             */
            fun onFind(initiate: MutableList<KProperty<*>>.() -> Unit) {
                onFindCallback = initiate
            }
        }
    }

    /**
     * [KProperty] 查找结果实现类
     *
     * @param isNoSuch 是否没有找到 [KProperty] - 默认否
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
         * 获得 [KProperty] 实例处理类
         *
         * - 若有多个 [KProperty] 结果只会返回第一个
         *
         * - 在 [callableInstances] 结果为空时使用此方法将无法获得对象
         *
         * - 如果目标对象不是静态 - 你必须设置 [instance]
         *
         * - 若你设置了 [remedys] 请使用 [wait] 回调结果方法
         * @param instance [KProperty] 所在的实例对象 - 如果是静态可不填 - 默认 null
         * @return [Instance]
         */
        fun get(instance: Any? = null) = Instance(instance, give())

        /**
         * 获得 [KProperty] 实例处理类数组
         *
         * - 返回全部查找条件匹配的多个 [KProperty] 实例结果
         *
         * - 在 [callableInstances] 结果为空时使用此方法将无法获得对象
         *
         * - 如果目标对象不是静态 - 你必须设置 [instance]
         *
         * - 若你设置了 [remedys] 请使用 [waitAll] 回调结果方法
         * @param instance [KProperty] 所在的实例对象 - 如果是静态可不填 - 默认 null
         * @return [MutableList]<[Instance]>
         */
        fun all(instance: Any? = null) =
            mutableListOf<Instance>().apply { giveAll().takeIf { it.isNotEmpty() }?.forEach { add(Instance(instance, it)) } }

        /**
         * 得到 [KProperty] 本身
         *
         * - 若有多个 [KProperty] 结果只会返回第一个
         *
         * - 在查找条件找不到任何结果的时候将返回 null
         * @return [KProperty] or null
         */
        fun give() = giveAll().takeIf { it.isNotEmpty() }?.first()

        /**
         * 得到 [KProperty] 本身数组
         *
         * - 返回全部查找条件匹配的多个 [KProperty] 实例
         *
         * - 在查找条件找不到任何结果的时候将返回空的 [MutableList]
         * @return [MutableList]<[KProperty]>
         */
        fun giveAll() = callableInstances.takeIf { it.isNotEmpty() }?.propertys() ?: mutableListOf()

        /**
         * 获得 [KProperty] 实例处理类
         *
         * - 若有多个 [KProperty] 结果只会返回第一个
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
         * 获得 [KProperty] 实例处理类数组
         *
         * - 返回全部查找条件匹配的多个 [KProperty] 实例结果
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
         * 创建 [KProperty] 重查找功能
         *
         * 当你遇到一种方法可能存在不同形式的存在时
         *
         * 可以使用 [RemedyPlan] 重新查找它 - 而没有必要使用 [onNoSuchProperty] 捕获异常二次查找 [KProperty]
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
         * 监听找不到 [KProperty] 时
         *
         * - 只会返回第一次的错误信息 - 不会返回 [RemedyPlan] 的错误信息
         * @param result 回调错误
         * @return [Result] 可继续向下监听
         */
        fun onNoSuchProperty(result: (Throwable) -> Unit): Result {
            if (isNoSuch) result(throwable ?: Throwable("Initialization Error"))
            return this
        }

        /**
         * 忽略异常并停止打印任何错误日志
         *
         * - 此时若要监听异常结果 - 你需要手动实现 [onNoSuchProperty] 方法
         * @return [Result] 可继续向下监听
         */
        fun ignored(): Result {
            isIgnoreErrorLogs = true
            return this
        }

        /**
         * [KProperty] 实例处理类
         *
         * - 请使用 [get]、[all] 方法来获取 [Instance]
         * @param instance 当前 [KProperty] 所在类的实例对象
         * @param property 当前 [KProperty] 实例对象
         */
        inner class Instance internal constructor(private val instance: Any?, private val property: KProperty<*>?) {

            /**
             * 获取当前 [KProperty] 自身的实例化对象
             *
             * - 若要直接获取不确定的实例对象 - 请调用 [any] 方法
             * @return [Any] or null
             */
            private val self get() = runCatching { if (instance != null) property?.call(instance) else property?.call() }.getOrElse { if (it is IllegalArgumentException) errorMsg("Non static method but no instance is passed in.",it) else throw it }

            /**
             * 获得当前 [KProperty] 自身 [self] 实例的类操作对象
             * @param ignored 是否开启忽略错误警告功能 - 默认否
             * @return [CurrentClass] or null
             */
            fun current(ignored: Boolean = false) = self?.current(ignored)

            /**
             * 获得当前 [KProperty] 自身 [self] 实例的类操作对象
             * @param ignored 是否开启忽略错误警告功能 - 默认否
             * @param initiate 方法体
             * @return [Any] or null
             */
            inline fun current(ignored: Boolean = false, initiate: CurrentClass.() -> Unit) = self?.current(ignored, initiate)

            /**
             * 得到当前 [KProperty] 实例
             * @return [T] or null
             */
            fun <T> cast() = self as? T?

            /**
             * 得到当前 [KProperty] 的 [Byte] 实例
             *
             * - 请确认目标 [KProperty] 的类型 - 发生错误会返回 null
             * @return [Byte] or null
             */
            fun byte() = cast<Byte?>()

            /**
             * 得到当前 [KProperty] 的 [Int] 实例
             *
             * - 请确认目标 [KProperty] 的类型 - 发生错误会返回默认值
             * @return [Int] 取不到返回 0
             */
            fun int() = cast() ?: 0

            /**
             * 得到当前 [KProperty] 的 [Long] 实例
             *
             * - 请确认目标 [KProperty] 的类型 - 发生错误会返回默认值
             * @return [Long] 取不到返回 0L
             */
            fun long() = cast() ?: 0L

            /**
             * 得到当前 [KProperty] 的 [Short] 实例
             *
             * - 请确认目标 [KProperty] 的类型 - 发生错误会返回默认值
             * @return [Short] 取不到返回 0
             */
            fun short() = cast<Short?>() ?: 0

            /**
             * 得到当前 [KProperty] 的 [Double] 实例
             *
             * - 请确认目标 [KProperty] 的类型 - 发生错误会返回默认值
             * @return [Double] 取不到返回 0.0
             */
            fun double() = cast() ?: 0.0

            /**
             * 得到当前 [KProperty] 的 [Float] 实例
             *
             * - 请确认目标 [KProperty] 的类型 - 发生错误会返回默认值
             * @return [Float] 取不到返回 0f
             */
            fun float() = cast() ?: 0f

            /**
             * 得到当前 [KProperty] 的 [String] 实例
             *
             * - 请确认目标 [KProperty] 的类型 - 发生错误会返回默认值
             * @return [String] 取不到返回 ""
             */
            fun string() = cast() ?: ""

            /**
             * 得到当前 [KProperty] 的 [Char] 实例
             *
             * - 请确认目标 [KProperty] 的类型 - 发生错误会返回默认值
             * @return [Char] 取不到返回 ' '
             */
            fun char() = cast() ?: ' '

            /**
             * 得到当前 [KProperty] 的 [Boolean] 实例
             *
             * - 请确认目标 [KProperty] 的类型 - 发生错误会返回默认值
             * @return [Boolean] 取不到返回 false
             */
            fun boolean() = cast() ?: false

            /**
             * 得到当前 [KProperty] 的 [Any] 实例
             * @return [Any] or null
             */
            fun any() = self

            /**
             * 得到当前 [KProperty] 的 [Array] 实例 - 每项类型 [T]
             *
             * - 请确认目标 [KProperty] 的类型 - 发生错误会返回空数组
             * @return [Array] 取不到返回空数组
             */
            inline fun <reified T> array() = cast() ?: arrayOf<T>()

            /**
             * 得到当前 [KProperty] 的 [List] 实例 - 每项类型 [T]
             *
             * - 请确认目标 [KProperty] 的类型 - 发生错误会返回空数组
             * @return [List] 取不到返回空数组
             */
            inline fun <reified T> list() = cast() ?: listOf<T>()

            /**
             * 设置当前 [KProperty] 实例
             * @param any 设置的实例内容
             */
            fun set(any: Any?) = property?.set(instance,any)

            /**
             * 设置当前 [KProperty] 实例为 true
             *
             * - 请确保示例对象类型为 [Boolean]
             */
            fun setTrue() = set(true)

            /**
             * 设置当前 [KProperty] 实例为 true
             *
             * - 请确保示例对象类型为 [Boolean]
             */
            fun setFalse() = set(false)

            /** 设置当前 [KProperty] 实例为 null */
            fun setNull() = set(null)

            override fun toString() =
                "[${self?.javaClass?.name ?: "<empty>"}] in [${instance?.javaClass?.name ?: "<empty>"}] value \"$self\""
        }
    }
}