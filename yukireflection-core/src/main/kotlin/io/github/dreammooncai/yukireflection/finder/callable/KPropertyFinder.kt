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

package io.github.dreammooncai.yukireflection.finder.callable

import io.github.dreammooncai.yukireflection.bean.KCurrentClass
import io.github.dreammooncai.yukireflection.finder.base.KCallableBaseFinder
import io.github.dreammooncai.yukireflection.finder.base.KBaseFinder
import io.github.dreammooncai.yukireflection.finder.callable.data.KPropertyRulesData
import io.github.dreammooncai.yukireflection.finder.tools.KReflectionTool
import io.github.dreammooncai.yukireflection.type.factory.KModifierConditions
import io.github.dreammooncai.yukireflection.type.factory.KTypeConditions
import io.github.dreammooncai.yukireflection.type.factory.KPropertyConditions
import io.github.dreammooncai.yukireflection.bean.KVariousClass
import io.github.dreammooncai.yukireflection.factory.*
import io.github.dreammooncai.yukireflection.helper.KYukiHookHelper
import io.github.dreammooncai.yukireflection.log.KYLog
import io.github.dreammooncai.yukireflection.type.factory.KNameConditions
import io.github.dreammooncai.yukireflection.utils.factory.runBlocking
import java.lang.IllegalArgumentException
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.*
import kotlin.reflect.full.instanceParameter

/**
 * [KProperty] 查找类
 *
 * 可通过指定类型查找指定 [KProperty] 或一组 [KProperty]
 * @param classSet 当前需要查找的 [KClass] 实例
 */
open class KPropertyFinder internal constructor(final override val classSet: KClass<*>? = null) : KCallableBaseFinder(tag = TAG_PROPERTY, classSet) {

    override var rulesData = KPropertyRulesData()

    /** 当前使用的 [classSet] */
    internal var usedClassSet = classSet

    /** 当前重查找结果回调 */
    internal var remedyPlansCallback: (() -> Unit)? = null

    /**
     * 将此属性相关内容附加到此查找器
     *
     * 将影响[name]、[type]
     *
     * @param R 属性类型
     * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
     * @param isUseMember 是否将属性转换为JavaField再进行附加 - 即使为false当属性附加错误时依然会尝试JavaField - 为true时会导致类型擦除
     */
    @JvmName("attach_exp")
    fun <R> KProperty<R>.attach(loader: ClassLoader? = null,isUseMember:Boolean = false){
        attach(this, loader,isUseMember)
    }

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
    fun name(conditions: KNameConditions): IndexTypeCondition {
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

    override fun build():BaseResult = runCatching {
        internalBuild()
        Result()
    }.getOrElse {
        errorMsg(e = it)
        Result(isNoSuch = true, it)
    }

    override fun failure(throwable: Throwable?):BaseResult = Result(isNoSuch = true, throwable)

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
            } else KYLog.warn(msg = "RemedyPlan is empty, forgot it?")
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
         * 获取属性的 getter 组成的 [KFunction] 查找结果实现类
         */
        val getter get() = KFunctionFinder(classSet).also { finder -> finder.callableInstances += giveAll().map { it.getter } }.Result(isNoSuch,throwable)

        /**
         * 获取属性的 setter 组成的 [KFunction] 查找结果实现类
         */
        val setter get() = KFunctionFinder(classSet).also { finder -> finder.callableInstances += giveAll().mapNotNull { it.toMutableOrNull?.setter } }.Result(isNoSuch,throwable)

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
         * @param extensionRef 属性如果是拓展属性你还需要传入拓展属性的this对象
         * @param isUseMember 是否优先将属性转换Java方式进行get/set
         * @return [Instance]
         */
        fun get(instance: Any? = null,extensionRef:Any? = null,isUseMember:Boolean = false) = Instance(instance, give()).receiver(extensionRef).useMember(isUseMember)

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
         * @param extensionRef 属性如果是拓展属性你还需要传入拓展属性的this对象
         * @param isUseMember 是否优先将属性转换Java方式进行get/set
         * @return [MutableList]<[Instance]>
         */
        fun all(instance: Any? = null,extensionRef:Any? = null,isUseMember:Boolean = false) =
            mutableListOf<Instance>().apply { giveAll().takeIf { it.isNotEmpty() }?.forEach { add(Instance(instance, it).receiver(extensionRef).useMember(isUseMember)) } }

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
         * @param extensionRef 属性如果是拓展属性你还需要传入拓展属性的this对象
         * @param isUseMember 是否优先将属性转换Java方式进行get/set
         * @param initiate 回调 [Instance]
         */
        fun wait(instance: Any? = null,extensionRef:Any? = null,isUseMember:Boolean = false, initiate: Instance.() -> Unit) {
            if (callableInstances.isNotEmpty()) initiate(get(instance,extensionRef,isUseMember))
            else remedyPlansCallback = { initiate(get(instance,extensionRef,isUseMember)) }
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
         * @param extensionRef 属性如果是拓展属性你还需要传入拓展属性的this对象
         * @param isUseMember 是否优先将属性转换Java方式进行get/set
         * @param initiate 回调 [MutableList]<[Instance]>
         */
        fun waitAll(instance: Any? = null,extensionRef:Any? = null,isUseMember:Boolean = false, initiate: MutableList<Instance>.() -> Unit) {
            if (callableInstances.isNotEmpty()) initiate(all(instance,extensionRef,isUseMember))
            else remedyPlansCallback = { initiate(all(instance,extensionRef,isUseMember)) }
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

        @Suppress("DuplicatedCode")
        override fun toString(): String {
            val all = giveAll()
            if (all.isEmpty()) return "${classSet?.name ?: "<empty>"} not found in."
            return "Here are the results of this search:\n" + all.joinToString(separator = "\n"){
                "[${it}] in [${classSet?.name ?: "<empty>"}]"
            } + "\n"
        }


        /**
         * [KProperty] 实例处理类
         *
         * - 请使用 [get]、[all] 方法来获取 [Instance]
         * @param instance 当前 [KProperty] 所在类的实例对象
         * @param property 当前 [KProperty] 实例对象
         */
        inner class Instance internal constructor(private var instance: Any?, private val property: KProperty<*>?):BaseInstance {

            init {
                if (instance == null){
                    instance = runCatching { property?.instanceParameter?.kotlin?.singletonInstance }.getOrNull()
                }
            }

            override fun callResult(vararg args: Any?): Any? = self

            /**
             * @see [useMember]
             */
            private var isUseMember = false

            /**
             * 是否将get/set使用Java方式获取或设置
             *
             * 为true时实例执行将通过将 Kotlin属性 转换为 JavaMember 方式执行
             *
             * 如果目标属性无法用Java方式描述则此设置将会自动忽略
             *
             * - 开启时优先 Field > GetterMethod > [KProperty.call]
             *
             * - 默认情况下只使用 [KProperty.call]
             *
             * @param use 是否优先将属性转换为Java方式进行get/set
             * @return [Instance] 可继续向下监听
             */
            fun useMember(use:Boolean = true): Instance {
                this.isUseMember = use
                return this
            }

            /**
             * @see [receiver]
             */
            private var extensionRef:Any? = null

            /**
             * 修改 [extensionRef] Receiver
             *
             * 当此属性是拓展属性时，你可能需要额外的一个this属性进行设置
             *
             * @param extensionRef 拓展属性的thisRef
             * @return [KProperty] 实例处理类
             */
            fun receiver(extensionRef:Any?):Instance{
                this.extensionRef = extensionRef
                return this
            }

            /** 标识需要调用当前 get/set [KProperty] 未经 Hook 的原始方法 */
            private var isCallOriginal = false

            /**
             * 标识需要调用当前 get/set [KProperty] 未经 Hook 的原始 get/set [KProperty]
             *
             * 若当前 get/set [KProperty] 并未 Hook 则会使用原始的 [KProperty]、get/set [KProperty] 方法调用
             *
             * - 此方法在 Hook Api 存在时将固定 [isUseMember] 为 true 无视 [extensionRef] 参数
             * - 你只能在 (Xposed) 宿主环境中使用此功能
             * - 此方法仅在 Hook Api 下有效
             * @return [Instance] 可继续向下监听
             */
            fun original(): Instance {
                isCallOriginal = true
                return this
            }

            private fun baseCall(): Any? {
                if (isCallOriginal && KYukiHookHelper.isMemberHooked(property?.javaGetterNoError.also { it?.isAccessible = true }))
                    return KYukiHookHelper.invokeOriginalMember(property?.javaGetterNoError.also { it?.isAccessible = true }, instance, arrayOf())
                if (isUseMember){
                    val field = property?.javaFieldNoError
                    if (field != null){
                        field.isAccessible = true
                        return field[instance]
                    }
                    val getter = property?.javaGetterNoError
                    if (getter != null){
                        getter.isAccessible = true
                        return if (property?.isExtension == true)
                            getter.invoke(instance,extensionRef)
                        else
                            getter.invoke(instance)
                    }
                }
                return when {
                    property?.isExtension == true -> {
                        if (instance != null) property.call(instance,extensionRef)
                        else property.call(extensionRef)
                    }
                    instance != null -> property?.call(instance)
                    else -> property?.call()
                }
            }

            /**
             * 获取当前 [KProperty] 自身的实例化对象
             *
             * - 若要直接获取不确定的实例对象 - 请调用 [any] 方法
             * @return [Any] or null
             */
            private val self get() = runCatching { baseCall() }.getOrElse { if (it is IllegalArgumentException) errorMsg("Non static method but no instance is passed in.",it) else throw it }

            /**
             * 获得当前 [KProperty] 自身 [self] 实例的类操作对象
             * @param ignored 是否开启忽略错误警告功能 - 默认否
             * @return [KCurrentClass] or null
             */
            fun current(ignored: Boolean = false) = self?.currentKotlin(ignored)

            /**
             * 获得当前 [KProperty] 自身 [self] 实例的类操作对象
             * @param ignored 是否开启忽略错误警告功能 - 默认否
             * @param initiate 方法体
             * @return [Any] or null
             */
            inline fun current(ignored: Boolean = false, initiate: KCurrentClass.() -> Unit) = self?.currentKotlin(ignored, initiate)

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
            fun set(any: Any?) = if (isCallOriginal && property is KMutableProperty && KYukiHookHelper.isMemberHooked(property.javaSetterNoError))
                KYukiHookHelper.invokeOriginalMember(property.javaSetterNoError, instance, arrayOf(any))
            else property?.set(instance,any,extensionRef,isUseMember)

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