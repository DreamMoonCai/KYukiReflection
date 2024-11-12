package io.github.dreammooncai.yukireflection.core

import io.github.dreammooncai.yukireflection.KYukiReflection
import io.github.dreammooncai.yukireflection.core.data.KTypeBuildData
import io.github.dreammooncai.yukireflection.finder.base.*
import io.github.dreammooncai.yukireflection.finder.classes.rules.result.KCallableRulesResult
import io.github.dreammooncai.yukireflection.bean.*
import io.github.dreammooncai.yukireflection.factory.kotlin
import io.github.dreammooncai.yukireflection.factory.toKClass
import io.github.dreammooncai.yukireflection.log.KYLog
import io.github.dreammooncai.yukireflection.type.defined.*
import io.github.dreammooncai.yukireflection.utils.factory.runBlocking
import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.starProjectedType

/**
 * 构建类型 操作对象
 *
 * @param classSet [KClassifier] 被构建的依据类
 */
class KTypeBuild internal constructor(private val classSet: KClassifier) : KBaseFinder() {

    /**
     * 通过已有type进行构建
     *
     * 构建时会保留已有type的泛型参数注解和空信息
     */
    internal constructor(type: KType):this(type.kotlin){
        isNullable = type.isMarkedNullable
        paramCount = type.arguments.size
        rulesData.paramTypes = type.arguments.toTypedArray()
        rulesData.annotations = type.annotations
    }

    override val rulesData = KTypeBuildData()

    /** 构建好的 [KType] 实例 */
    internal var typeInstance:KType? = null

    /**
     * 此类型是否为空
     *
     * 这与之对应的Kotlin可空性 如 String? 与 String 并不一样
     *
     * true 时为类型增加 "?"
     *
     * @return [Boolean]
     */
    var isNullable
        get() = rulesData.isNullable
        set(value) {
            rulesData.isNullable = value
        }

    /**
     * 设置 [KType] 泛型参数个数
     *
     * 你可以不使用 [param] 指定参数类型而是仅使用此变量指定参数个数
     *
     * 若参数个数小于零则忽略并使用 [param]
     *
     * - 未指定类型的泛型将使用星射代替[KTypeProjection.STAR]
     * @return [Int]
     */
    var paramCount
        get() = rulesData.paramCount
        set(value) {
            rulesData.paramCount = value
        }

    /**
     * [KType] 的方差映射
     *
     * index to Variance
     *
     * 默认没有使用方差
     *
     * in、out、默认
     */
    private var variances = mutableMapOf<Int,KVariance>()

    /** 设置 [KType] 泛型为空参数、无参数，无泛型 */
    fun emptyParam() {
        paramCount = 0
        rulesData.paramTypes = arrayOf()
    }

    /**
     * 设置 [KType] 泛型参数
     *
     * 如果同时使用了 [paramCount] 则 [paramType] 的数量必须小于或等于 [paramCount] 个数，多余未设置的类型将使用星射代替[KTypeProjection.STAR]
     *
     * 你同样可以在 [KType] 中使用[VagueKotlin] - 它与[KTypeProjection.STAR]等价
     *
     * 例如下面这个参数结构 ↓
     *
     * ```kotlin
     * 生成List<Int,String,*>尽管这不合理，因为泛型数量必须与基类能承受的数量一致
     * ```
     *
     * 此时就可以简单地写作 ↓
     *
     * ```kotlin
     * param(Int::class, String::class,VagueKotlin)
     * ```
     *
     * - 无泛型 [KType] 请使用 [emptyParam] 设置没有泛型参数
     *
     * - 有泛型 [KType] 未指定的泛型将使用星射代替[KTypeProjection.STAR]
     * @param paramType 泛型参数类型数组 - 只能是 [Class]/[KClassifier]/[KClass]/[KTypeParameter]、[KGenericClass]、[KTypeProjection]、[KType]、[KParameter]、[String]、[KVariousClass]
     */
    fun param(vararg paramType: Any) {
        if (paramType.isEmpty()) error("paramTypes is empty, please use emptyParam() instead")
        rulesData.paramTypes =
            mutableListOf<Any>().apply { paramType.forEach { add(it) } }.toTypedArray()
    }

    /**
     * 增加 [KType] 泛型参数 - 你需要谨慎使用此方法，因为常常默认泛型组中包含的是所有星射，此方法会同步增加[paramCount],你往往不需要去增加泛型参数数量他们应该在源码就固定了,因此你可能需要先[emptyParam]
     *
     * 如果同时使用了 [paramCount] 则 [paramType] 的数量必须小于或等于 [paramCount] 个数，多余未设置的类型将使用星射代替[KTypeProjection.STAR]
     *
     * 你同样可以在 [KType] 中使用[VagueKotlin] - 它与[KTypeProjection.STAR]等价
     *
     * 例如下面这个参数结构 ↓
     *
     * ```kotlin
     * 生成List<Int,String,*>尽管这不合理，因为泛型数量必须与基类能承受的数量一致
     * ```
     *
     * 此时就可以简单地写作 ↓
     *
     * ```kotlin
     * addParam(Int::class)
     * addParam(String::class)
     * addParam(VagueKotlin)
     * ```
     *
     * - 无泛型 [KType] 请使用 [emptyParam] 设置没有泛型参数
     *
     * - 有泛型 [KType] 未指定的泛型将使用星射代替[KTypeProjection.STAR]
     * @param paramType 泛型参数类型数组 - 只能是 [Class]/[KClassifier]/[KClass]/[KTypeParameter]、[KGenericClass]、[KTypeProjection]、[KType]、[KParameter]、[String]、[KVariousClass]
     */
    fun addParam(paramType: Any) {
        rulesData.paramTypes = rulesData.paramTypes?.toMutableList()?.apply {
            paramCount++
            add(paramType)
        }?.toTypedArray() ?: mutableListOf<Any>().apply {
            paramCount++
            add(paramType)
        }.toTypedArray()
    }

    /**
     * 设置 [KType] 泛型参数
     *
     * 你同样可以在 [KType] 中使用[VagueKotlin] - 它与[KTypeProjection.STAR]等价
     *
     * 例如下面这个参数结构 ↓
     *
     * ```kotlin
     * 将List<*>设置为 -> List<String,*,Int> 尽管这不合理，因为泛型数量必须与基类能承受的数量一致
     * ```
     *
     * 此时就可以简单地写作 ↓
     *
     * ```kotlin
     * setParam(2,Int::class)
     * setParam(0,String::class)
     * setParam(1,VagueKotlin)
     * ```
     *
     * - 无泛型 [KType] 请使用 [emptyParam] 设置没有泛型参数
     *
     * - 有泛型 [KType] 未指定的泛型将使用星射代替[KTypeProjection.STAR]
     * @param index 泛型参数索引
     * @param paramType 泛型参数类型数组 - 只能是 [Class]/[KClassifier]/[KClass]/[KTypeParameter]、[KGenericClass]、[KTypeProjection]、[KType]、[KParameter]、[String]、[KVariousClass]
     */
    fun setParam(index: Int,paramType: Any){
        rulesData.paramTypes = rulesData.paramTypes?.toMutableList()?.apply {
            set(index,paramType)
        }?.toTypedArray() ?: mutableListOf<Any>().apply {
            set(index,paramType)
        }.toTypedArray()
    }

    /**
     * 设置 [KType] 星射泛型参数为指定类型
     *
     * - 此方法设置从最初元素开始的首个星射[KTypeProjection.STAR]为指定泛型参数
     *
     * 你同样可以在 [KType] 中使用[VagueKotlin] - 它与[KTypeProjection.STAR]等价
     *
     * 例如下面这个参数结构 ↓
     *
     * ```kotlin
     * 为填补星射参数List<Int,String,*> -> List<Int,String,Int> 尽管这不合理，因为泛型数量必须与基类能承受的数量一致
     * ```
     *
     * 此时就可以简单地写作 ↓
     *
     * ```kotlin
     * setStarParam(Int::class)
     * ```
     *
     * - 如果没有任何星射泛型则此操作等同于[setParam] (0,paramType)
     *
     * - 有泛型 [KType] 未指定的泛型将使用星射代替[KTypeProjection.STAR]
     * @param paramType 泛型参数类型数组 - 只能是 [Class]/[KClassifier]/[KClass]/[KTypeParameter]、[KGenericClass]、[KTypeProjection]、[KType]、[KParameter]、[String]、[KVariousClass]
     */
    fun setStarParam(paramType: Any){
        setStarParam(0,paramType)
    }

    /**
     * 设置 [KType] 星射泛型参数为指定类型
     *
     * - 此方法设置从最初元素开始的第[index]个星射[KTypeProjection.STAR]为指定泛型参数
     *
     * 你同样可以在 [KType] 中使用[VagueKotlin] - 它与[KTypeProjection.STAR]等价
     *
     * 例如下面这个参数结构 ↓
     *
     * ```kotlin
     * 为填补星射参数List<*,String,*> -> List<*,String,Int> 尽管这不合理，因为泛型数量必须与基类能承受的数量一致
     * ```
     *
     * 此时就可以简单地写作 ↓
     *
     * ```kotlin
     * setStarParam(1,Int::class)
     * ```
     *
     * - 如果没有任何星射泛型则此操作等同于[setParam] (index,paramType)
     *
     * - 有泛型 [KType] 未指定的泛型将使用星射代替[KTypeProjection.STAR]
     * @param paramType 泛型参数类型数组 - 只能是 [Class]/[KClassifier]/[KClass]/[KTypeParameter]、[KGenericClass]、[KTypeProjection]、[KType]、[KParameter]、[String]、[KVariousClass]
     */
    fun setStarParam(index: Int,paramType: Any){
        rulesData.paramTypes = rulesData.paramTypes?.toMutableList()?.apply {
            set(mapIndexedNotNull { index, any -> if (any == KTypeProjection.STAR) index else null }.ifEmpty { rulesData.paramTypes!!.indices.toList() }[index],paramType)
        }?.toTypedArray() ?: mutableListOf<Any>().apply {
            set(mapIndexedNotNull { index, any -> if (any == KTypeProjection.STAR) index else null }.ifEmpty { rulesData.paramTypes!!.indices.toList() }[index],paramType)
        }.toTypedArray()
    }

    /**
     * 设置 [KType] 泛型参数的方差
     *
     * 你同样可以在 [KType] 中使用[VagueKotlin] - 它与[KVariance.INVARIANT]等价
     *
     * 例如下面这个参数结构 ↓
     *
     * ```kotlin
     * 为泛型参数设置方差List<Int,in String,Int> -> List<in Int,String,out Int> 尽管这不合理，因为泛型数量必须与基类能承受的数量一致
     * ```
     *
     * 此时就可以简单地写作 ↓
     *
     * ```kotlin
     * setVariance(0,KVariance.IN)
     * setVariance(1,VagueKotlin)
     * setVariance(2,KVariance.OUT)
     * ```
     *
     * @param variance [KVariance] 方差 - 这与in、out、默认的信息有关
     */
    fun setVariance(index:Int,variance:KVariance){
        variances[index] = variance
    }

    /**
     * 设置 [KType] 注解
     *
     * 例如下面这个参数类型结构 ↓
     *
     * ```kotlin
     * vararg abc:@UnsafeVariance Int
     * ```
     *
     * 此时就可以简单地写作 ↓
     *
     * ```kotlin
     * classSet = Int::class
     * annotations(UnsafeVariance::class)
     * ```
     *
     * @param annotations 注解数组
     */
    fun annotations(vararg annotations: Annotation){
        rulesData.annotations = annotations.toList()
    }

    /**
     * 增加 [KType] 注解
     *
     * 例如下面这个参数类型结构 ↓
     *
     * ```kotlin
     * vararg abc:@UnsafeVariance Int
     * ```
     *
     * 此时就可以简单地写作 ↓
     *
     * ```kotlin
     * classSet = Int::class
     * addAnnotation(UnsafeVariance::class)
     * ```
     *
     * @param annotation 注解
     */
    fun addAnnotation(annotation: Annotation){
        rulesData.annotations = rulesData.annotations.toMutableList().apply { add(annotation) }
    }

    /**
     * 设置实例
     * @param type 当前找到的 [KType] 构造结果
     */
    private fun setInstance(type: KType) {
        typeInstance = type
    }

    /** 得到 Constructor [KFunction] 结果 */
    private fun internalBuild() {
        runBlocking {
            if (!rulesData.isInitialize){
                setInstance(classSet.starProjectedType)
                return
            }
            if (paramCount < (rulesData.paramTypes?.size ?: -1)) error("paramCount is less than paramTypes, please use param() instead. & paramCount:$paramCount,paramTypes:${rulesData.paramTypes.contentToString()}")
            val projections = mutableListOf<KTypeProjection>()
            rulesData.paramTypes?.forEachIndexed { index, any ->
                val variance = variances.getOrElse(index) { KVariance.INVARIANT }
                projections += if (any == VagueKotlin || any == KTypeProjection.STAR)
                    KTypeProjection.STAR
                else when (any){
                    is KGenericClass->
                        KTypeProjection(variance,any.type)

                    is KClassifier ->
                        KTypeProjection(variance,any.starProjectedType)

                    is KType ->
                        KTypeProjection(variance,any)

                    is KParameter ->
                        KTypeProjection(variance,any.type)

                    is String ->
                        KTypeProjection(variance,any.toKClass().starProjectedType)

                    is KTypeProjection -> KTypeProjection(variances.getOrElse(index) { any.variance },any.type)

                    else -> error("Unsupported type $any")
                }
            }
            if (paramCount > 0){
                for (i in 0 until paramCount){
                    if (projections.size <= i) projections += KTypeProjection.STAR
                }
            }
            setInstance(classSet.createType(projections,isNullable,rulesData.annotations))
        }.result { ms ->
            if (KYukiReflection.Configs.isDebug) KYLog.debug("Build KType [ $typeInstance ] takes ${ms}ms")
        }
    }


    /**
     * 返回结果实现类
     *
     * - 此功能交由方法体自动完成 - 你不应该手动调用此方法
     * @return [KCallableRulesResult]
     */
    override fun build() = runCatching {
        internalBuild()
        Result()
    }.getOrElse {
        Result(true, it)
    }

    override fun failure(throwable: Throwable?) = Result(true,throwable)

    /**
     * Constructor [KFunction] 查找结果实现类
     * @param isNoBuild 是否没有找到 Constructor [KFunction] - 默认否
     * @param throwable 错误信息
     */
    inner class Result internal constructor(
        val isNoBuild: Boolean = false,
        internal val throwable: Throwable? = null
    ) : BaseResult {

        /**
         * 创建监听结果事件方法体
         * @param initiate 方法体
         * @return [Result] 可继续向下监听
         */
        inline fun result(initiate: Result.() -> Unit) = apply(initiate)

        /**
         * 找到结果
         */
        fun get() = typeInstance ?: throw IllegalStateException("KType An error occurred during building, please handle further.", throwable)
    }
}