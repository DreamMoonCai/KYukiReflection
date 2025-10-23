@file:Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE","MISSING_DEPENDENCY_SUPERCLASS", "RecursivePropertyAccessor","INVISIBLE_MEMBER","INVISIBLE_REFERENCE")
package io.github.dreammooncai.yukireflection.factory

import kotlin.reflect.jvm.internal.impl.descriptors.impl.AbstractLazyTypeParameterDescriptor
import kotlin.reflect.jvm.internal.impl.descriptors.annotations.Annotations.Companion as AnnotationsCompanion
import kotlin.reflect.jvm.internal.impl.name.Name
import kotlin.reflect.jvm.internal.impl.types.Variance
import kotlin.reflect.jvm.internal.impl.descriptors.SourceElement
import kotlin.reflect.jvm.internal.impl.descriptors.SupertypeLoopChecker
import kotlin.reflect.jvm.internal.impl.descriptors.ClassifierDescriptor
import kotlin.reflect.jvm.internal.impl.descriptors.annotations.AnnotationDescriptorImpl
import io.github.dreammooncai.yukireflection.finder.callable.KConstructorFinder
import io.github.dreammooncai.yukireflection.finder.signature.KFunctionSignatureFinder
import io.github.dreammooncai.yukireflection.finder.signature.KPropertySignatureFinder
import io.github.dreammooncai.yukireflection.type.kotlin.AnyKClass
import io.github.dreammooncai.yukireflection.utils.DexSignUtil
import io.github.dreammooncai.yukireflection.utils.factory.lazyDomain
import jdk.jfr.internal.TypeLibrary.createType
import java.lang.reflect.*
import kotlin.Array
import kotlin.jvm.internal.Reflection
import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.*
import kotlin.reflect.jvm.internal.KClassifierImpl
import kotlin.reflect.jvm.internal.impl.types.KotlinType
import kotlin.reflect.jvm.internal.impl.types.*


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
val Class<*>.top: KDeclarationContainer by lazyDomain {
    Reflection.getOrCreateKotlinPackage(if (this.name.endsWith("Kt")) this else "${name}Kt".toKClass().java)
}

/**
 * 当前 [Class] 是否是kotlin类
 *
 * 为false不代表不能使用Kotlin反射，Kotlin反射优先依赖于Metadata注解，没有时依赖JavaClass中所有已知信息
 *
 * 即JavaClass中部分泛型信息Kotlin反射也能获取成功，这些已知信息将通过Kotlin反射自动转换为对应的Kotlin相关引用信息对象
 * 
 * JavaClass中使用Kotlin反射可能超出预期如获取的成员会包括父类的成员等
 *
 * @return [Boolean]
 */
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
val Class<*>.isKotlin: Boolean by lazyDomain {
    annotations.any { (it as java.lang.annotation.Annotation).annotationType().name == Metadata::class.java.name }
}

/**
 * 当前 [Class] 是否是kotlin类且反射是不会造成异常的
 *
 * 造成异常的 Kotlin 类同样支持 [KClass.functionSignature]、[KClass.propertySignature] 等使用
 *
 * 关于 Java 类参考 [Class.isKotlin]
 *
 * @return [Boolean]
 */
val Class<*>.isKotlinNoError by lazyDomain {
    isKotlin && kotlin.isSupportReflection
}

/**
 * 检查当前 [Class] 是否是数组或集合同时根据Kotlin情况进行匹配
 *
 * @return [Boolean]
 */
val Class<*>.isArrayOrCollection: Boolean by lazyDomain {
    isArray || when (this) {
        IntArray::class.java,
        ByteArray::class.java,
        ShortArray::class.java,
        LongArray::class.java,
        FloatArray::class.java,
        DoubleArray::class.java,
        CharArray::class.java,
        BooleanArray::class.java,
        Array::class.java,
        Collection::class.java -> true

        else -> {
            var isMatched = false

            /**
             * 查找是否存在父类
             * @param current 当前 [Class]
             */
            fun findSuperClass(current: Class<*>) {
                if (current == Collection::class.java)
                    isMatched = true
                else if (current != Any::class.java) findSuperClass(current.superclass)
            }
            findSuperClass(current = this)
            isMatched
        }
    }
}

/**
 * 将 [Constructor] 转换为 [KConstructorFinder.Result.Instance] 可执行类
 *
 * 这是 Constructor[KFunction] 的快捷方法
 *
 * @param isUseMember 是否优先将属性转换Java方式进行get/set
 */
inline fun Constructor<*>.instanceKotlin(isUseMember: Boolean = false) = kotlin.constructor(isUseMember)

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
val Field.kotlin: KProperty<*> by lazyDomain {
    when {
        Modifier.isStatic(modifiers) -> Reflection.mutableProperty0(KReference.mutablePropertyStatic(this))
        else -> Reflection.mutableProperty1(KReference.mutableProperty(this))
    }
}

/**
 * 通过 [Method] 分析签名构建 [KFunction]
 *
 * 签名分析或许不一定能正常转换 参阅或使用[Method.kotlinFunction]
 */
val Method.kotlin: KFunction<*> by lazyDomain { Reflection.function(KReference.function(this)) }

/**
 * 通过 [Constructor] 分析签名构建 [KFunction]
 *
 * 签名分析或许不一定能正常转换 参阅或使用[Constructor.kotlinFunction]
 */
val Constructor<*>.kotlin: KFunction<*> by lazyDomain { Reflection.function(KReference.function(this)) }

/**
 * 获取此 [Field] 在 Kotlin 常用的简单签名
 *
 *     如: int abc = * --> "getAbc()I"
 *        val abc = listOf(1, 2, 3) --> "getAbc()Ljava/util/List;"
 */
val Field.kotlinSimpleSignature by lazyDomain { name.addHump(if (type.kotlin == Boolean::class) "is" else "get") + "()" + DexSignUtil.getTypeSign(type) }

/**
 * 获取此 [Method] 在 Kotlin 常用的简单签名
 *
 *     如: int abc(int a, int b) --> "abc(II)I"
 *        fun abc(a:List<*>) --> "abc(Ljava/util/List;)V"
 */
val Method.kotlinSimpleSignature by lazyDomain { name + DexSignUtil.getMethodSign(this) }

/**
 * 获取此 [Constructor] 在 Kotlin 常用的简单签名
 *
 *     如: class abc(int a, int b) --> "<init>(II)V"
 *        class abc(a:List<*>) --> "<init>(Ljava/util/List;)V"
 */
val Constructor<*>.kotlinSimpleSignature by lazyDomain { name + DexSignUtil.getConstructorSign(this) }

/**
 * 获取 Java [Type] 的 Kotlin 类描述符
 *
 * @return [KClass]、[KTypeParameter]
 */
val Type.classifier:KClassifier by lazyDomain {
    when (this) {
        is Class<*> -> this.kotlin
        is ParameterizedType -> this.rawType.classifier
        is GenericArrayType -> this.genericComponentType.classifier
        is WildcardType -> this.upperBounds.first().classifier
        is TypeVariable<*> -> this.kotlin
        else -> error("Unsupported types can't get their Kotlin representation type.")
    }
}

/**
 * 获取 Java [Type] 的 Kotlin 类描述符
 *
 * - 此属性不会报错
 * @return [KClass]、[KTypeParameter] or null
 */
val Type.classifierOrNull by lazyDomain { runCatching { classifier }.getOrNull() }

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
val Type.kotlinType:KType by lazyDomain {
    when (this) {
        is Class<*> -> this.classifier.type
        is ParameterizedType -> {
            val rawType = this.classifier
            val arguments = this.actualTypeArguments.mapNotNull {
                if (it is WildcardType) {
                    val upperBounds = it.upperBounds.first()
                    val lowerBounds = it.lowerBounds.firstOrNull()
                    if (lowerBounds == null && !(upperBounds is Class<*> && upperBounds.kotlin == Any::class))
                        runCatching { KTypeProjection(KVariance.OUT, upperBounds.kotlinType) }.getOrElse { KTypeProjection.STAR }
                    else if (lowerBounds != null)
                        runCatching { KTypeProjection(KVariance.IN, lowerBounds.kotlinType) }.getOrElse { KTypeProjection.STAR }
                    else
                        KTypeProjection.STAR
                } else runCatching { KTypeProjection(KVariance.INVARIANT, it.kotlinType) }.getOrElse { KTypeProjection.STAR }
            }
            rawType.createType(arguments)
        }

        is GenericArrayType -> Array::class.createType(arrayListOf(if (this.genericComponentType is WildcardType) {
            val upperBounds = (this.genericComponentType as WildcardType).upperBounds.first()
            val lowerBounds = (this.genericComponentType as WildcardType).lowerBounds.firstOrNull()
            if (lowerBounds == null && !(upperBounds is Class<*> && upperBounds.kotlin == Any::class))
                runCatching { KTypeProjection(KVariance.OUT,upperBounds.kotlinType) }.getOrElse { KTypeProjection.STAR }
            else if (lowerBounds != null)
                runCatching { KTypeProjection(KVariance.IN,lowerBounds.kotlinType) }.getOrElse { KTypeProjection.STAR }
            else
                KTypeProjection.STAR
        } else runCatching { KTypeProjection(KVariance.INVARIANT,this.genericComponentType.kotlinType) }.getOrElse { KTypeProjection.STAR }))
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
val Type.kotlinTypeOrNull by lazyDomain { runCatching { kotlinType }.getOrNull() }

/**
 * 将Java [Array]<[Type]> 转换为 [Array]<[KType]>
 */
val Array<Type>.kotlinType by lazyDomain { map { it.kotlinType }.toTypedArray() }

/**
 * 将Java [Array]<[Type]> 转换为 [Array]<[KType]>
 *
 * - 此属性不会报错
 * @return [Array]<[KType]> or null
 */
val Array<Type>.kotlinTypeOrNull by lazyDomain { runCatching { kotlinType }.getOrNull() }

/**
 * 将Java [Collection]<[Type]> 转换为 [List]<[KType]>
 */
val Collection<Type>.kotlinType by lazyDomain { map { it.kotlinType } }

/**
 * 将Java [Collection]<[Type]> 转换为 [List]<[KType]>
 *
 * - 此属性不会报错
 * @return [List]<[KType]> or null
 */
val Collection<Type>.kotlinTypeOrNull by lazyDomain { runCatching { kotlinType }.getOrNull() }

/**
 * 将Java [TypeVariable] 转换为 [ClassifierDescriptor]
 */
val TypeVariable<*>.descriptor:ClassifierDescriptor by lazyDomain {
    val storageManager = runCatching { (genericDeclaration as Class<*>).kotlin.memberScope?.deserializationContext?.storageManager ?: (genericDeclaration as Class<*>).kotlin.memberJavaScope!!.resolverContext!!.storageManager }.getOrElse { error("failedToGetStorageManager.") }
    val containingDeclaration = runCatching { (genericDeclaration as Class<*>).kotlin.memberScope?.deserializationContext?.containingDeclaration ?: (genericDeclaration as Class<*>).kotlin.memberJavaScope!!.ownerDescriptor!! }.getOrElse { error("failedToGetContainingDeclaration.") }
    object : AbstractLazyTypeParameterDescriptor(storageManager,containingDeclaration,AnnotationsCompanion.create((this as AnnotatedElement).declaredAnnotations.mapNotNull {
        AnnotationDescriptorImpl(it.annotationClass.type.kotlinType ?: return@mapNotNull null,mapOf(),SourceElement.NO_SOURCE)
    }),Name.identifier(this.name),Variance.INVARIANT,false,this.genericDeclaration.typeParameters.indexOf(this),SourceElement.NO_SOURCE,SupertypeLoopChecker.EMPTY.INSTANCE) {
        override fun reportSupertypeLoopError(type: KotlinType) {
            throw IllegalStateException("There should be no cycles for deserialized type parameters, but found for: $this");
        }

        override fun resolveUpperBounds(): List<KotlinType?> {
            return this@lazyDomain.bounds.mapNotNull {
                it.kotlinType.kotlinType ?: return@mapNotNull null
            }
        }
    }
}

/**
 * 将Java [TypeVariable] 转换为 [KTypeParameter]
 */
val TypeVariable<*>.kotlin:KTypeParameter by lazyDomain {
    KTypeParameterJavaImpl(this)
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

/**
 * 通过 [Member] 分析签名构建 [KProperty] 或 [KFunction]
 *
 * 签名分析或许不一定能正常转换 参阅或使用[Member.kotlinCallable]
 */
val Member.kotlin by lazyDomain {
    when (this) {
        is Field -> kotlin
        is Method -> kotlin
        is Constructor<*> -> kotlin
        else -> error("Unsupported member type: $this")
    }
}

/**
 * 返回与给定 Java [Member] 实例相对应的[KCallable]实例，或者null如果此字段不能由 Kotlin 可执行属性表示（例如，如果它是合成属性）。
 */
val Member.kotlinCallable by lazyDomain {
    when (this) {
        is Field -> kotlinProperty
        is Method -> kotlinFunction
        is Constructor<*> -> kotlinFunction
        else -> error("Unsupported member type: $this")
    }
}

/**
 * 获取 [Member] 的返回类型
 *
 * [Field] ---> [Field.type]
 *
 * [Method] ---> [Method.returnType]
 *
 * [Constructor] ---> [Member.getDeclaringClass]
 */
val Member.returnType: Class<out Any> by lazyDomain {
    when (this) {
        is Field -> type
        is Method -> returnType
        is Constructor<*> -> declaringClass
        else -> error("Unsupported member type: $this")
    }
}