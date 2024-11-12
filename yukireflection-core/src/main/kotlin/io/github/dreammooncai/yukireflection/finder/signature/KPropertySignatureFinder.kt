@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST", "KotlinConstantConditions", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package io.github.dreammooncai.yukireflection.finder.signature

import io.github.dreammooncai.yukireflection.bean.KCurrentClass
import io.github.dreammooncai.yukireflection.factory.*
import io.github.dreammooncai.yukireflection.finder.callable.KFunctionFinder
import io.github.dreammooncai.yukireflection.finder.callable.KPropertyFinder
import io.github.dreammooncai.yukireflection.finder.signature.support.KFunctionSignatureSupport
import io.github.dreammooncai.yukireflection.finder.signature.support.KPropertySignatureSupport
import io.github.dreammooncai.yukireflection.finder.tools.KReflectionTool
import io.github.dreammooncai.yukireflection.helper.KYukiHookHelper
import io.github.dreammooncai.yukireflection.log.KYLog
import io.github.dreammooncai.yukireflection.type.factory.KPropertySignatureConditions
import io.github.dreammooncai.yukireflection.utils.factory.runBlocking
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import java.lang.reflect.Field
import java.lang.reflect.Member
import java.lang.reflect.Method
import kotlin.reflect.KFunction

/**
 * 通过 [KProperty] 签名查找 [KPropertySignatureSupport.member] 类
 *
 * 可通过指定类型查找指定 [KPropertySignatureSupport] 或一组 [KPropertySignatureSupport]
 * @param classSet 当前需要查找的 [KClass] 实例
 */
class KPropertySignatureFinder internal constructor(classSet: KClass<*>? = null, private val loader: ClassLoader? = null) : KPropertyFinder(classSet) {
    /** 当前找到的 [KPropertySignatureSupport] 数组 */
    internal var callableSignatureInstances = mutableListOf<KPropertySignatureSupport>()

    /**
     * 得到 [KPropertySignatureSupport] 或一组 [KPropertySignatureSupport]
     * @return [MutableList]<[KPropertySignatureSupport]>
     * @throws NoSuchMethodError 如果找不到 [KPropertySignatureSupport]
     */
    private val result get() = KReflectionTool.findPropertySignatures(usedClassSet, rulesData,loader)

    /**
     * 设置实例
     * @param functions 当前找到的 [KPropertySignatureSupport] 数组
     */
    private fun setInstance(functions: MutableList<KPropertySignatureSupport>) {
        callableSignatureInstances.clear()
        functions.takeIf { it.isNotEmpty() }?.forEach { callableSignatureInstances.add(it) }
    }

    /** 得到 [KPropertySignatureSupport] 结果 */
    private fun internalBuild() {
        if (classSet == null) error(CLASSSET_IS_NULL)
        runBlocking {
            setInstance(result)
        }.result { ms ->
            callableSignatureInstances.takeIf { it.isNotEmpty() }?.forEach { debugMsg(msg = "Find KProperty [$it] takes ${ms}ms") }
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
     * [KPropertySignatureSupport] 重查找实现类
     *
     * 可累计失败次数直到查找成功
     */
    inner class RemedyPlan internal constructor() {

        /** 失败尝试次数数组 */
        private val remedyPlans = mutableListOf<Pair<KPropertySignatureFinder, Result>>()

        /**
         * 创建需要重新查找的 [KPropertySignatureSupport]
         *
         * 你可以添加多个备选 [KPropertySignatureSupport] - 直到成功为止
         *
         * 若最后依然失败 - 将停止查找并输出错误日志
         * @param initiate 方法体
         * @return [Result] 结果
         */
        inline fun propertySignature(initiate: KPropertySignatureConditions) = Result().apply { remedyPlans.add(KPropertySignatureFinder(classSet).apply(initiate) to this) }

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
                            callableSignatureInstances.takeIf { it.isNotEmpty() }?.forEach { debugMsg(msg = "Find KFunction [$it] takes ${ms}ms") }
                        }
                        isFindSuccess = true
                        plan.second.onFindCallback?.invoke(callableSignatureInstances)
                        remedyPlansCallback?.invoke()
                        callableSignatureInstances.takeIf { it.isNotEmpty() }
                            ?.forEach { debugMsg(msg = "RemedyPlan successed after ${index + 1} attempts of KFunction [$it]") }
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
            internal var onFindCallback: (MutableList<KPropertySignatureSupport>.() -> Unit)? = null

            /**
             * 当找到结果时
             * @param initiate 回调
             */
            fun onFind(initiate: MutableList<KPropertySignatureSupport>.() -> Unit) {
                onFindCallback = initiate
            }
        }
    }

    /**
     * [KPropertySignatureSupport] 查找结果实现类
     *
     * @param isNoSuch 是否没有找到 [KPropertySignatureSupport.member] - 默认否
     * @param throwable 错误信息
     */
    inner class Result internal constructor(
        val isNoSuch: Boolean = false,
        internal val throwable: Throwable? = null
    ) : BaseResult {

        /**
         * 获取属性的 getter 组成的 [KFunctionSignatureSupport] 查找结果实现类
         */
        val getter get() = KFunctionSignatureFinder(classSet).also { finder -> finder.callableSignatureInstances += giveAll().map { it.getter } }.Result(isNoSuch,throwable)

        /**
         * 获取属性的 setter 组成的 [KFunctionSignatureSupport] 查找结果实现类
         */
        val setter get() = KFunctionSignatureFinder(classSet).also { finder -> finder.callableSignatureInstances += giveAll().map { it.setter } }.Result(isNoSuch,throwable)

        /**
         * 创建监听结果事件方法体
         * @param initiate 方法体
         * @return [Result] 可继续向下监听
         */
        inline fun result(initiate: Result.() -> Unit) = apply(initiate)

        /**
         * 获得 [KPropertySignatureSupport.member] 实例处理类
         *
         * - 若有多个 [KPropertySignatureSupport.member] 结果只会返回第一个
         *
         * - 在 [callableSignatureInstances] 结果为空时使用此方法将无法获得对象
         *
         * - 如果目标对象不是静态 - 你必须设置 [instance]
         *
         * - 若你设置了 [remedys] 请使用 [wait] 回调结果方法
         * @param instance [KPropertySignatureSupport.member] 所在的实例对象 - 如果是静态可不填 - 默认 null
         * @param declaringClass [KClass] 属性所在的 [KClass]
         * @param loader [ClassLoader] 属性成员 [KPropertySignatureSupport.member] 所在的 [ClassLoader]
         * @return [Instance]
         */
        fun get(instance: Any? = null,declaringClass:KClass<*>? = null,loader: ClassLoader? = this@KPropertySignatureFinder.loader) = Instance(instance, give()?.getMemberOrNull(declaringClass, loader),give()?.setterOrNull?.getMemberOrNull(declaringClass,loader))

        /**
         * 获得 [KPropertySignatureSupport.member] 实例处理类数组
         *
         * - 返回全部查找条件匹配的多个 [KPropertySignatureSupport.member] 实例结果
         *
         * - 在 [callableSignatureInstances] 结果为空时使用此方法将无法获得对象
         *
         * - 如果目标对象不是静态 - 你必须设置 [instance]
         *
         * - 若你设置了 [remedys] 请使用 [waitAll] 回调结果方法
         * @param instance [KPropertySignatureSupport.member] 所在的实例对象 - 如果是静态可不填 - 默认 null
         * @param declaringClass [KClass] 属性所在的 [KClass]
         * @param loader [ClassLoader] 属性成员 [KPropertySignatureSupport.member] 所在的 [ClassLoader]
         * @return [MutableList]<[Instance]>
         */
        fun all(instance: Any? = null,declaringClass:KClass<*>? = null,loader: ClassLoader? = this@KPropertySignatureFinder.loader) =
            mutableListOf<Instance>().apply { giveAll().takeIf { it.isNotEmpty() }?.forEach { add(Instance(instance, it.getMemberOrNull(declaringClass,loader),it.setterOrNull?.getMemberOrNull(declaringClass,loader))) } }

        /**
         * 得到 [KPropertySignatureSupport] 本身
         *
         * - 若有多个 [KPropertySignatureSupport] 结果只会返回第一个
         *
         * - 在查找条件找不到任何结果的时候将返回 null
         * @return [KPropertySignatureSupport] or null
         */
        fun give() = giveAll().takeIf { it.isNotEmpty() }?.first()

        /**
         * 得到 [KPropertySignatureSupport] 本身数组
         *
         * - 返回全部查找条件匹配的多个 [KPropertySignatureSupport] 实例
         *
         * - 在查找条件找不到任何结果的时候将返回空的 [MutableList]
         * @return [MutableList]<[KPropertySignatureSupport]>
         */
        fun giveAll() = callableSignatureInstances.takeIf { it.isNotEmpty() } ?: mutableListOf()

        /**
         * 获得 [KPropertySignatureSupport.member] 实例处理类
         *
         * - 若有多个 [KPropertySignatureSupport.member] 结果只会返回第一个
         *
         * - 若你设置了 [remedys] 必须使用此方法才能获得结果
         *
         * - 若你没有设置 [remedys] 此方法将不会被回调
         * @param instance [KPropertySignatureSupport.member] 所在的实例对象 - 如果是静态可不填 - 默认 null
         * @param declaringClass [KClass] 属性所在的 [KClass]
         * @param loader [ClassLoader] 属性成员 [KPropertySignatureSupport.member] 所在的 [ClassLoader]
         * @param initiate 回调 [Instance]
         */
        fun wait(instance: Any? = null,declaringClass:KClass<*>? = null,loader: ClassLoader? = this@KPropertySignatureFinder.loader, initiate: Instance.() -> Unit) {
            if (callableSignatureInstances.isNotEmpty()) initiate(get(instance,declaringClass,loader))
            else remedyPlansCallback = { initiate(get(instance,declaringClass,loader)) }
        }

        /**
         * 获得 [KPropertySignatureSupport.member] 实例处理类数组
         *
         * - 返回全部查找条件匹配的多个 [KPropertySignatureSupport.member] 实例结果
         *
         * - 若你设置了 [remedys] 必须使用此方法才能获得结果
         *
         * - 若你没有设置 [remedys] 此方法将不会被回调
         * @param instance [KPropertySignatureSupport.member] 所在的实例对象 - 如果是静态可不填 - 默认 null
         * @param declaringClass [KClass] 属性所在的 [KClass]
         * @param loader [ClassLoader] 属性成员 [KPropertySignatureSupport.member] 所在的 [ClassLoader]
         * @param initiate 回调 [MutableList]<[Instance]>
         */
        fun waitAll(instance: Any? = null,declaringClass:KClass<*>? = null,loader: ClassLoader? = this@KPropertySignatureFinder.loader, initiate: MutableList<Instance>.() -> Unit) {
            if (callableSignatureInstances.isNotEmpty()) initiate(all(instance,declaringClass,loader))
            else remedyPlansCallback = { initiate(all(instance,declaringClass,loader)) }
        }

        /**
         * 创建 [KPropertySignatureSupport] 重查找功能
         *
         * 当你遇到一种方法可能存在不同形式的存在时
         *
         * 可以使用 [RemedyPlan] 重新查找它 - 而没有必要使用 [onNoSuchProperty] 捕获异常二次查找 [KPropertySignatureSupport]
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
         * 监听找不到 [KPropertySignatureSupport] 时
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
         * [Field]、get/set [Method] 实例处理类
         *
         * - 请使用 [get]、[all] 方法来获取 [Instance]
         * @param instance 当前 [Field]、get/set [Method] 所在类的实例对象
         * @param member 当前 [Field]、get/set [Method] 实例对象
         */
        inner class Instance internal constructor(private val instance: Any?, private val member: Member?,private val setter:Method? = null):BaseInstance {

            init {
                member?.isAccessible = true
                setter.also { it?.isAccessible = true }
            }

            override fun callResult(vararg args: Any?): Any? = self

            /** 标识需要调用当前 get/set [Method] 未经 Hook 的原始方法 */
            private var isCallOriginal = false

            /**
             * 标识需要调用当前 get/set [Method] 未经 Hook 的原始 get/set [Method]
             *
             * 若当前 get/set [Method] 并未 Hook 则会使用原始的 [Field.get]、get/set [Method.invoke] 方法调用
             *
             * - 你只能在 (Xposed) 宿主环境中使用此功能
             * - 此方法仅在 Hook Api 下有效
             * @return [Instance] 可继续向下监听
             */
            fun original(): Instance {
                isCallOriginal = true
                return this
            }

            private fun baseCall(): Any? = if (isCallOriginal &&  KYukiHookHelper.isMemberHooked(member))
                KYukiHookHelper.invokeOriginalMember(member, instance, arrayOf())
            else when (member){
                is Field -> member[instance]
                is Method -> member.invoke(instance)
                else -> null
            }

            /**
             * 获取当前 [Field]、get/set [Method] 自身的实例化对象
             *
             * - 若要直接获取不确定的实例对象 - 请调用 [any] 方法
             * @return [Any] or null
             */
            private val self get() = runCatching { baseCall() }.getOrElse { if (it is IllegalArgumentException) errorMsg("Non static method but no instance is passed in.",it) else throw it }

            /**
             * 获得当前 [Field]、get/set [Method] 自身 [self] 实例的类操作对象
             * @param ignored 是否开启忽略错误警告功能 - 默认否
             * @return [KCurrentClass] or null
             */
            fun current(ignored: Boolean = false) = self?.currentKotlin(ignored)

            /**
             * 获得当前 [Field]、get/set [Method] 自身 [self] 实例的类操作对象
             * @param ignored 是否开启忽略错误警告功能 - 默认否
             * @param initiate 方法体
             * @return [Any] or null
             */
            inline fun current(ignored: Boolean = false, initiate: KCurrentClass.() -> Unit) = self?.currentKotlin(ignored, initiate)

            /**
             * 得到当前 [Field]、get/set [Method] 实例
             * @return [T] or null
             */
            fun <T> cast() = self as? T?

            /**
             * 得到当前 [Field]、get/set [Method] 的 [Byte] 实例
             *
             * - 请确认目标 [Field]、get/set [Method] 的类型 - 发生错误会返回 null
             * @return [Byte] or null
             */
            fun byte() = cast<Byte?>()

            /**
             * 得到当前 [Field]、get/set [Method] 的 [Int] 实例
             *
             * - 请确认目标 [Field]、get/set [Method] 的类型 - 发生错误会返回默认值
             * @return [Int] 取不到返回 0
             */
            fun int() = cast() ?: 0

            /**
             * 得到当前 [Field]、get/set [Method] 的 [Long] 实例
             *
             * - 请确认目标 [Field]、get/set [Method] 的类型 - 发生错误会返回默认值
             * @return [Long] 取不到返回 0L
             */
            fun long() = cast() ?: 0L

            /**
             * 得到当前 [Field]、get/set [Method] 的 [Short] 实例
             *
             * - 请确认目标 [Field]、get/set [Method] 的类型 - 发生错误会返回默认值
             * @return [Short] 取不到返回 0
             */
            fun short() = cast<Short?>() ?: 0

            /**
             * 得到当前 [Field]、get/set [Method] 的 [Double] 实例
             *
             * - 请确认目标 [Field]、get/set [Method] 的类型 - 发生错误会返回默认值
             * @return [Double] 取不到返回 0.0
             */
            fun double() = cast() ?: 0.0

            /**
             * 得到当前 [Field]、get/set [Method] 的 [Float] 实例
             *
             * - 请确认目标 [Field]、get/set [Method] 的类型 - 发生错误会返回默认值
             * @return [Float] 取不到返回 0f
             */
            fun float() = cast() ?: 0f

            /**
             * 得到当前 [Field]、get/set [Method] 的 [String] 实例
             *
             * - 请确认目标 [Field]、get/set [Method] 的类型 - 发生错误会返回默认值
             * @return [String] 取不到返回 ""
             */
            fun string() = cast() ?: ""

            /**
             * 得到当前 [Field]、get/set [Method] 的 [Char] 实例
             *
             * - 请确认目标 [Field]、get/set [Method] 的类型 - 发生错误会返回默认值
             * @return [Char] 取不到返回 ' '
             */
            fun char() = cast() ?: ' '

            /**
             * 得到当前 [Field]、get/set [Method] 的 [Boolean] 实例
             *
             * - 请确认目标 [Field]、get/set [Method] 的类型 - 发生错误会返回默认值
             * @return [Boolean] 取不到返回 false
             */
            fun boolean() = cast() ?: false

            /**
             * 得到当前 [Field]、get/set [Method] 的 [Any] 实例
             * @return [Any] or null
             */
            fun any() = self

            /**
             * 得到当前 [Field]、get/set [Method] 的 [Array] 实例 - 每项类型 [T]
             *
             * - 请确认目标 [Field]、get/set [Method] 的类型 - 发生错误会返回空数组
             * @return [Array] 取不到返回空数组
             */
            inline fun <reified T> array() = cast() ?: arrayOf<T>()

            /**
             * 得到当前 [Field]、get/set [Method] 的 [List] 实例 - 每项类型 [T]
             *
             * - 请确认目标 [Field]、get/set [Method] 的类型 - 发生错误会返回空数组
             * @return [List] 取不到返回空数组
             */
            inline fun <reified T> list() = cast() ?: listOf<T>()

            /**
             * 设置当前 [Field]、get/set [Method] 实例
             * @param any 设置的实例内容
             */
            fun set(any: Any?) = if (isCallOriginal && KYukiHookHelper.isMemberHooked(setter))
                KYukiHookHelper.invokeOriginalMember(setter, instance, arrayOf(any))
            else when (member){
                is Field -> member[instance] = any
                is Method -> setter?.invoke(instance,any)
                else -> null
            }

            /**
             * 设置当前 [Field]、get/set [Method] 实例为 true
             *
             * - 请确保示例对象类型为 [Boolean]
             */
            fun setTrue() = set(true)

            /**
             * 设置当前 [Field]、get/set [Method] 实例为 true
             *
             * - 请确保示例对象类型为 [Boolean]
             */
            fun setFalse() = set(false)

            /** 设置当前 [Field]、get/set [Method] 实例为 null */
            fun setNull() = set(null)

            override fun toString() =
                "[${self?.javaClass?.name ?: "<empty>"}] in [${instance?.javaClass?.name ?: "<empty>"}] value \"$self\""
        }
    }
}