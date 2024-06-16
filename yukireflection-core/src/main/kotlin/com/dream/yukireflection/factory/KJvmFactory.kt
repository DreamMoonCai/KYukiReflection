@file:Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE", "RecursivePropertyAccessor")
package com.dream.yukireflection.factory

import com.dream.yukireflection.finder.callable.KConstructorFinder
import com.dream.yukireflection.finder.signature.KFunctionSignatureFinder
import com.dream.yukireflection.finder.signature.KPropertySignatureFinder
import com.dream.yukireflection.utils.DexSignUtil
import java.lang.reflect.*
import kotlin.jvm.internal.Reflection
import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.jvm.*


/**
 * 将 [Class] 强行转换为 [KClass]
 *
 * @return [KClass]
 */
inline val <T> Class<out T>.kotlinAs get() = this.kotlin as KClass<T & Any>

/**
 * 获取当前 [Class] 在Kotlin的顶级操作对象
 *
 * 如定义在根class外的内容 包括函数/字段等
 *
 * 顶层往往会在类名后增加Kt
 */
inline val Class<*>.top: KDeclarationContainer get() = Reflection.getOrCreateKotlinPackage(if (this.name.endsWith("Kt")) this else "${name}Kt".toKClass().java)

/**
 * 当前 [Class] 是否是kotlin类
 *
 * 为false不代表不能使用Kotlin反射，Kotlin反射优先依赖于Metadata注解，没有时依赖JavaClass中所有已知信息
 *
 * 即JavaClass中部分泛型信息Kotlin反射也能获取成功，这些已知信息将通过Kotlin反射自动转换为对应的Kotlin相关引用信息对象
 *
 * @return [Boolean]
 */
inline val Class<*>.isKotlin: Boolean get() = annotations.any { it.annotationClass.jvmName == Metadata::class.jvmName }

/**
 * 将 [Constructor] 转换为 [KConstructorFinder.Result.Instance] 可执行类
 *
 * 这是 Constructor[KFunction] 的快捷方法
 *
 * @param isUseMember 是否优先将属性转换Java方式进行get/set
 */
inline fun Constructor<*>.instanceKotlin(isUseMember: Boolean = false) = kotlin.constructorInstance(isUseMember)

/**
 * 将 [Method] 转换为 [KFunctionSignatureFinder.Result.Instance] 可执行类
 *
 * @param thisRef 执行所使用this对象
 */
inline fun Method.instance(thisRef: Any? = null) = KFunctionSignatureFinder().Result().Instance(thisRef,this)

/**
 * 将 [Field] 转换为 [KPropertySignatureFinder.Result.Instance] 可执行类
 *
 * @param thisRef 执行所使用this对象
 */
inline fun Field.instance(thisRef: Any? = null) = KPropertySignatureFinder().Result().Instance(thisRef,this)

/**
 * 通过 [Field] 分析签名构建 [KProperty]
 *
 * 签名分析或许不一定能正常转换 参阅或使用[Field.kotlinProperty]
 */
inline val Field.kotlin: KProperty<*> get() = when{
    Modifier.isStatic(modifiers) -> Reflection.mutableProperty0(KReference.mutablePropertyStatic(this))
    else -> Reflection.mutableProperty1(KReference.mutableProperty(this))
}

/**
 * 通过 [Method] 分析签名构建 [KFunction]
 *
 * 签名分析或许不一定能正常转换 参阅或使用[Method.kotlinFunction]
 */
inline val Method.kotlin: KFunction<*> get() = Reflection.function(KReference.function(this))

/**
 * 通过 [Constructor] 分析签名构建 [KFunction]
 *
 * 签名分析或许不一定能正常转换 参阅或使用[Constructor.kotlinFunction]
 */
inline val Constructor<*>.kotlin: KFunction<*> get() = Reflection.function(KReference.function(this))

/**
 * 获取此 [Field] 在 Kotlin 常用的简单签名
 *
 *     如: int abc = * --> "getAbc()I"
 *        val abc = listOf(1, 2, 3) --> "getAbc()Ljava/util/List;"
 */
val Field.kotlinSimpleSignature get() = name.addHump(if (type.kotlin == Boolean::class) "is" else "get") + "()" + DexSignUtil.getTypeSign(type)

/**
 * 获取此 [Method] 在 Kotlin 常用的简单签名
 *
 *     如: int abc(int a, int b) --> "abc(II)I"
 *        fun abc(a:List<*>) --> "abc(Ljava/util/List;)V"
 */
val Method.kotlinSimpleSignature get() = name + DexSignUtil.getMethodSign(this)

/**
 * 获取此 [Constructor] 在 Kotlin 常用的简单签名
 *
 *     如: class abc(int a, int b) --> "<init>(II)V"
 *        class abc(a:List<*>) --> "<init>(Ljava/util/List;)V"
 */
val Constructor<*>.kotlinSimpleSignature get() = name + DexSignUtil.getConstructorSign(this)

/**
 * 获取 Java [Type] 的 Kotlin 类描述符
 *
 * @return [KClass]、[KTypeParameter]
 */
val Type.classifier:KClassifier get() = when (this) {
    is Class<*> -> this.kotlin
    is ParameterizedType -> this.rawType.classifier
    is GenericArrayType -> this.genericComponentType.classifier
    is WildcardType -> this.upperBounds.first().classifier
    is TypeVariable<*> -> this.kotlin
    else -> error("Unsupported types can't get their Kotlin representation type.")
}

/**
 * 获取 Java [Type] 的 Kotlin 类描述符
 *
 * - 此属性不会报错
 * @return [KClass]、[KTypeParameter] or null
 */
val Type.classifierOrNull get() = runCatching { classifier }.getOrNull()

/**
 * 获取 Java [Type] 的 Kotlin [KType]
 *
 * ```java
 *     List<? extends List<?>> -> kotlin.collections.List<out kotlin.collections.List<*>>
 *     List<? super ArrayList>[] -> kotlin.Array<kotlin.collections.List<in java.util.ArrayList<*>>>
 *     List<String>[][] -> kotlin.Array<kotlin.Array<kotlin.collections.List<kotlin.String>>>
 * ```
 *
 * 大多数情况下能够转换成功
 */
val Type.kotlinType:KType get() {
    return when (this) {
        is Class<*> -> this.classifier.type
        is ParameterizedType -> {
            val rawType = this.classifier
            val arguments = this.actualTypeArguments.mapNotNull {
                if (it is WildcardType) {
                    val upperBounds = it.upperBounds.first()
                    val lowerBounds = it.lowerBounds.firstOrNull()
                    if (lowerBounds == null && !(upperBounds is Class<*> && upperBounds.kotlin == Any::class))
                        KTypeProjection(KVariance.OUT, upperBounds.kotlinType)
                    else if (lowerBounds != null)
                        KTypeProjection(KVariance.IN, lowerBounds.kotlinType)
                    else
                        KTypeProjection.STAR
                } else KTypeProjection(KVariance.INVARIANT, it.kotlinType)
            }
            return rawType.createType(arguments)
        }

        is GenericArrayType -> Array::class.createType(arrayListOf(if (this.genericComponentType is WildcardType) {
            val upperBounds = (this.genericComponentType as WildcardType).upperBounds.first()
            val lowerBounds = (this.genericComponentType as WildcardType).lowerBounds.firstOrNull()
            if (lowerBounds == null && !(upperBounds is Class<*> && upperBounds.kotlin == Any::class))
                KTypeProjection(KVariance.OUT,upperBounds.kotlinType)
            else if (lowerBounds != null)
                KTypeProjection(KVariance.IN,lowerBounds.kotlinType)
            else
                KTypeProjection.STAR
        } else KTypeProjection(KVariance.INVARIANT,this.genericComponentType.kotlinType)))
        is WildcardType -> this.upperBounds.first().kotlinType
        is TypeVariable<*> -> this.kotlin.type
        else -> error("An unsupported type that cannot be converted to a Kotlin representable type.")
    }
}

/**
 * 获取 Java [Type] 的 Kotlin [KType]
 *
 * - 此属性不会报错
 * @return [KType] or null
 */
val Type.kotlinTypeOrNull get() = runCatching { kotlinType }.getOrNull()

/**
 * 将Java [TypeVariable] 转换为 [KTypeParameter]
 */
val TypeVariable<*>.kotlin:KTypeParameter get() {
    return object : KTypeParameter {
        override val isReified: Boolean
            get() = false
        override val name: String
            get() = this@kotlin.name
        override val upperBounds: List<KType>
            get() = this@kotlin.bounds.mapNotNull { it.kotlinType }
        override val variance: KVariance
            get() = KVariance.INVARIANT
        override fun equals(other: Any?): Boolean =
            other is KTypeParameter && name == other.name && upperBounds == other.upperBounds

        override fun hashCode(): Int {
            return super.hashCode() + 31
        }
    }
}

/**
 * 仅支持设置 [Field.isAccessible]、[Method.isAccessible]、[Constructor.isAccessible]
 */
var Member.isAccessible:Boolean
    @Deprecated(message = "Property can only be written.", level = DeprecationLevel.ERROR)
    get() = throw NotImplementedError()
    set(value) {
        when (this) {
            is Field -> this.isAccessible = value
            is Method -> this.isAccessible = value
            is Constructor<*> -> this.isAccessible = value
            else -> error("Unsupported Member type: $this")
        }
    }