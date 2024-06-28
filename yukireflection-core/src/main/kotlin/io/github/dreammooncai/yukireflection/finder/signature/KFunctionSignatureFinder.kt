@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST", "KotlinConstantConditions", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package io.github.dreammooncai.yukireflection.finder.signature

import io.github.dreammooncai.yukireflection.factory.name
import io.github.dreammooncai.yukireflection.finder.callable.KFunctionFinder
import io.github.dreammooncai.yukireflection.finder.signature.support.KFunctionSignatureSupport
import io.github.dreammooncai.yukireflection.finder.tools.KReflectionTool
import io.github.dreammooncai.yukireflection.helper.KYukiHookHelper
import io.github.dreammooncai.yukireflection.log.KYLog
import io.github.dreammooncai.yukireflection.type.factory.KFunctionSignatureConditions
import io.github.dreammooncai.yukireflection.utils.factory.runBlocking
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import java.lang.reflect.Method

/**
 * 通过 [KFunction] 签名查找 [KFunctionSignatureSupport.member] 类
 *
 * 可通过指定类型查找指定 [KFunctionSignatureSupport] 或一组 [KFunctionSignatureSupport]
 * @param classSet 当前需要查找的 [KClass] 实例
 */
class KFunctionSignatureFinder internal constructor(classSet: KClass<*>? = null,private val loader: ClassLoader? = null) : KFunctionFinder(classSet) {
    /** 当前找到的 [KFunctionSignatureSupport] 数组 */
    internal var callableSignatureInstances = mutableListOf<KFunctionSignatureSupport>()

    /**
     * 得到 [KFunctionSignatureSupport] 或一组 [KFunctionSignatureSupport]
     * @return [MutableList]<[KFunctionSignatureSupport]>
     * @throws NoSuchMethodError 如果找不到 [KFunctionSignatureSupport]
     */
    private val result get() = KReflectionTool.findFunctionSignatures(usedClassSet, rulesData,loader)

    /**
     * 设置实例
     * @param functions 当前找到的 [KFunctionSignatureSupport] 数组
     */
    private fun setInstance(functions: MutableList<KFunctionSignatureSupport>) {
        callableSignatureInstances.clear()
        functions.takeIf { it.isNotEmpty() }?.forEach { callableSignatureInstances.add(it) }
    }

    /** 得到 [KFunctionSignatureSupport] 结果 */
    private fun internalBuild() {
        if (classSet == null) error(CLASSSET_IS_NULL)
        runBlocking {
            setInstance(result)
        }.result { ms ->
            callableSignatureInstances.takeIf { it.isNotEmpty() }?.forEach { debugMsg(msg = "Find KFunction [$it] takes ${ms}ms") }
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
     * [KFunctionSignatureSupport] 重查找实现类
     *
     * 可累计失败次数直到查找成功
     */
    inner class RemedyPlan internal constructor() {

        /** 失败尝试次数数组 */
        private val remedyPlans = mutableListOf<Pair<KFunctionSignatureFinder, Result>>()

        /**
         * 创建需要重新查找的 [KFunctionSignatureSupport]
         *
         * 你可以添加多个备选 [KFunctionSignatureSupport] - 直到成功为止
         *
         * 若最后依然失败 - 将停止查找并输出错误日志
         * @param initiate 方法体
         * @return [Result] 结果
         */
        inline fun function(initiate: KFunctionSignatureConditions) = Result().apply { remedyPlans.add(KFunctionSignatureFinder(classSet).apply(initiate) to this) }

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
            internal var onFindCallback: (MutableList<KFunctionSignatureSupport>.() -> Unit)? = null

            /**
             * 当找到结果时
             * @param initiate 回调
             */
            fun onFind(initiate: MutableList<KFunctionSignatureSupport>.() -> Unit) {
                onFindCallback = initiate
            }
        }
    }

    /**
     * [KFunctionSignatureSupport] 签名查找结果实现类
     * @param isNoSuch 是否没有找到 [KFunctionSignatureSupport.member] - 默认否
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
         * 获得 [KFunctionSignatureSupport.member] 实例处理类
         *
         * - 若有多个 [KFunctionSignatureSupport.member] 结果只会返回第一个
         *
         * - 在 [callableSignatureInstances] 结果为空时使用此方法将无法获得对象
         *
         * - 若你设置了 [remedys] 请使用 [wait] 回调结果方法
         * @param instance [KFunctionSignatureSupport.member] 所在的实例对象 - 如果是静态可不填 - 默认 null
         * @param declaringClass [KClass] 方法所在的 [KClass]
         * @param loader [ClassLoader] 方法参数 [KFunctionSignatureSupport.paramClasss] 所在的 [ClassLoader]
         * @return [Instance]
         */
        fun get(instance: Any? = null,declaringClass:KClass<*>? = null,loader: ClassLoader? = this@KFunctionSignatureFinder.loader) = Instance(instance, give()?.getMemberOrNull(declaringClass,loader))

        /**
         * 获得 [KFunctionSignatureSupport.member] 实例处理类数组
         *
         * - 返回全部查找条件匹配的多个 [KFunctionSignatureSupport.member] 实例结果
         *
         * - 在 [callableSignatureInstances] 结果为空时使用此方法将无法获得对象
         *
         * - 若你设置了 [remedys] 请使用 [waitAll] 回调结果方法
         * @param instance [KFunctionSignatureSupport.member] 所在的实例对象 - 如果是静态可不填 - 默认 null
         * @param declaringClass [KClass] 方法所在的 [KClass]
         * @param loader [ClassLoader] 方法参数 [KFunctionSignatureSupport.paramClasss] 所在的 [ClassLoader]
         * @return [MutableList]<[Instance]>
         */
        fun all(instance: Any? = null,declaringClass:KClass<*>? = null,loader: ClassLoader? = this@KFunctionSignatureFinder.loader) =
            mutableListOf<Instance>().apply { giveAll().takeIf { it.isNotEmpty() }?.forEach { add(Instance(instance, it.getMemberOrNull(declaringClass, loader))) } }

        /**
         * 得到 [KFunctionSignatureSupport] 本身
         *
         * - 若有多个 [KFunctionSignatureSupport] 结果只会返回第一个
         *
         * - 在查找条件找不到任何结果的时候将返回 null
         * @return [KFunctionSignatureSupport] or null
         */
        fun give() = giveAll().takeIf { it.isNotEmpty() }?.first()

        /**
         * 得到 [KFunctionSignatureSupport] 本身数组
         *
         * - 返回全部查找条件匹配的多个 [KFunctionSignatureSupport] 实例
         *
         * - 在查找条件找不到任何结果的时候将返回空的 [MutableList]
         * @return [MutableList]<[KFunctionSignatureSupport]>
         */
        fun giveAll() = callableSignatureInstances.takeIf { it.isNotEmpty() } ?: mutableListOf()

        /**
         * 获得 [KFunctionSignatureSupport.member] 实例处理类
         *
         * - 若有多个 [KFunctionSignatureSupport.member] 结果只会返回第一个
         *
         * - 若你设置了 [remedys] 必须使用此方法才能获得结果
         *
         * - 若你没有设置 [remedys] 此方法将不会被回调
         * @param instance [KFunctionSignatureSupport.member] 所在的实例对象 - 如果是静态可不填 - 默认 null
         * @param declaringClass [KClass] 方法所在的 [KClass]
         * @param loader [ClassLoader] 方法参数 [KFunctionSignatureSupport.paramClasss] 所在的 [ClassLoader]
         * @param initiate 回调 [Instance]
         */
        fun wait(instance: Any? = null,declaringClass:KClass<*>? = null,loader: ClassLoader? = this@KFunctionSignatureFinder.loader, initiate: Instance.() -> Unit) {
            if (callableSignatureInstances.isNotEmpty()) initiate(get(instance,declaringClass,loader))
            else remedyPlansCallback = { initiate(get(instance,declaringClass,loader)) }
        }

        /**
         * 获得 [KFunctionSignatureSupport.member] 实例处理类数组
         *
         * - 返回全部查找条件匹配的多个 [KFunctionSignatureSupport.member] 实例结果
         *
         * - 若你设置了 [remedys] 必须使用此方法才能获得结果
         *
         * - 若你没有设置 [remedys] 此方法将不会被回调
         * @param instance [KFunctionSignatureSupport.member] 所在的实例对象 - 如果是静态可不填 - 默认 null
         * @param declaringClass [KClass] 方法所在的 [KClass]
         * @param loader [ClassLoader] 方法参数 [KFunctionSignatureSupport.paramClasss] 所在的 [ClassLoader]
         * @param initiate 回调 [MutableList]<[Instance]>
         */
        fun waitAll(instance: Any? = null,declaringClass:KClass<*>? = null,loader: ClassLoader? = this@KFunctionSignatureFinder.loader, initiate: MutableList<Instance>.() -> Unit) {
            if (callableSignatureInstances.isNotEmpty()) initiate(all(instance,declaringClass,loader))
            else remedyPlansCallback = { initiate(all(instance,declaringClass,loader)) }
        }

        /**
         * 创建 [KFunctionSignatureSupport] 重查找功能
         *
         * 当你遇到一种 [KFunctionSignatureSupport] 可能存在不同形式的存在时
         *
         * 可以使用 [RemedyPlan] 重新查找它 - 而没有必要使用 [onNoSuchFunction] 捕获异常二次查找 [KFunctionSignatureSupport]
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
         * 监听找不到 [KFunctionSignatureSupport] 时
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

        @Suppress("DuplicatedCode")
        override fun toString(): String {
            val all = giveAll()
            if (all.isEmpty()) return "${classSet?.name ?: "<empty>"} not found in."
            return "Here are the results of this search:\n" + all.joinToString(separator = "\n"){
                "[${it}] in [${classSet?.name ?: "<empty>"}]"
            } + "\n"
        }

        /**
         * [Method] 实例处理类
         *
         * - 请使用 [get]、[wait]、[all]、[waitAll] 方法来获取 [Instance]
         * @param instance 当前 [Method] 所在类的实例对象
         * @param method 当前 [Method] 实例对象
         */
        inner class Instance internal constructor(private val instance: Any?, private val method: Method?):BaseInstance {

            init {
                method.also { it?.isAccessible = true }
            }

            override fun callResult(vararg args: Any?): Any? = call(*args)

            /** 标识需要调用当前 [Method] 未经 Hook 的原始方法 */
            private var isCallOriginal = false

            /**
             * 标识需要调用当前 [Method] 未经 Hook 的原始 [Method]
             *
             * 若当前 [Method] 并未 Hook 则会使用原始的 [Method.invoke] 方法调用
             *
             * - 你只能在 (Xposed) 宿主环境中使用此功能
             * - 此方法仅在 Hook Api 下有效
             * @return [Instance] 可继续向下监听
             */
            fun original(): Instance {
                isCallOriginal = true
                return this
            }

            /**
             * 执行 [Method]
             * @param args 方法参数
             * @return [Any] or null
             */
            private fun baseCall(vararg args: Any?) = if (isCallOriginal && KYukiHookHelper.isMemberHooked(method))
                KYukiHookHelper.invokeOriginalMember(method, instance, args)
            else method?.invoke(instance, *args)

            /**
             * 执行 [Method] - 不指定返回值类型
             * @param args 方法参数
             * @return [Any] or null
             */
            fun call(vararg args: Any?) = baseCall(*args)

            /**
             * 执行 [Method] - 指定 [T] 返回值类型
             * @param args 方法参数
             * @return [T] or null
             */
            fun <T> invoke(vararg args: Any?) = baseCall(*args) as? T?

            /**
             * 执行 [Method] - 指定 [Byte] 返回值类型
             *
             * - 请确认目标变量的类型 - 发生错误会返回 null
             * @param args 方法参数
             * @return [Byte] or null
             */
            fun byte(vararg args: Any?) = invoke<Byte?>(*args)

            /**
             * 执行 [Method] - 指定 [Int] 返回值类型
             *
             * - 请确认目标 [Method] 的返回值 - 发生错误会返回默认值
             * @param args 方法参数
             * @return [Int] 取不到返回 0
             */
            fun int(vararg args: Any?) = invoke(*args) ?: 0

            /**
             * 执行 [Method] - 指定 [Long] 返回值类型
             *
             * - 请确认目标 [Method] 的返回值 - 发生错误会返回默认值
             * @param args 方法参数
             * @return [Long] 取不到返回 0L
             */
            fun long(vararg args: Any?) = invoke(*args) ?: 0L

            /**
             * 执行 [Method] - 指定 [Short] 返回值类型
             *
             * - 请确认目标 [Method] 的返回值 - 发生错误会返回默认值
             * @param args 方法参数
             * @return [Short] 取不到返回 0
             */
            fun short(vararg args: Any?) = invoke<Short?>(*args) ?: 0

            /**
             * 执行 [Method] - 指定 [Double] 返回值类型
             *
             * - 请确认目标 [Method] 的返回值 - 发生错误会返回默认值
             * @param args 方法参数
             * @return [Double] 取不到返回 0.0
             */
            fun double(vararg args: Any?) = invoke(*args) ?: 0.0

            /**
             * 执行 [Method] - 指定 [Float] 返回值类型
             *
             * - 请确认目标 [Method] 的返回值 - 发生错误会返回默认值
             * @param args 方法参数
             * @return [Float] 取不到返回 0f
             */
            fun float(vararg args: Any?) = invoke(*args) ?: 0f

            /**
             * 执行 [Method] - 指定 [String] 返回值类型
             * @param args 方法参数
             * @return [String] 取不到返回 ""
             */
            fun string(vararg args: Any?) = invoke(*args) ?: ""

            /**
             * 执行 [Method] - 指定 [Char] 返回值类型
             * @param args 方法参数
             * @return [Char] 取不到返回 ' '
             */
            fun char(vararg args: Any?) = invoke(*args) ?: ' '

            /**
             * 执行 [Method] - 指定 [Boolean] 返回值类型
             *
             * - 请确认目标 [Method] 的返回值 - 发生错误会返回默认值
             * @param args 方法参数
             * @return [Boolean] 取不到返回 false
             */
            fun boolean(vararg args: Any?) = invoke(*args) ?: false

            /**
             * 执行 [Method] - 指定 [Array] 返回值类型 - 每项类型 [T]
             *
             * - 请确认目标 [Method] 的返回值 - 发生错误会返回空数组
             * @return [Array] 取不到返回空数组
             */
            inline fun <reified T> array(vararg args: Any?) = invoke(*args) ?: arrayOf<T>()

            /**
             * 执行 [Method] - 指定 [List] 返回值类型 - 每项类型 [T]
             *
             * - 请确认目标 [Method] 的返回值 - 发生错误会返回空数组
             * @return [List] 取不到返回空数组
             */
            inline fun <reified T> list(vararg args: Any?) = invoke(*args) ?: listOf<T>()

            override fun toString() = "[${method?.name ?: "<empty>"}] in [${instance?.javaClass?.name ?: "<empty>"}]"
        }
    }
}