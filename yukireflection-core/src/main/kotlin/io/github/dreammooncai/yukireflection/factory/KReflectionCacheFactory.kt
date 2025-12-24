@file:Suppress(
    "UNCHECKED_CAST",
    "NOTHING_TO_INLINE",
    "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE",
    "MISSING_DEPENDENCY_SUPERCLASS",
    "INVISIBLE_MEMBER",
    "INVISIBLE_REFERENCE"
)

package io.github.dreammooncai.yukireflection.factory

import io.github.dreammooncai.yukireflection.finder.callable.KConstructorFinder
import io.github.dreammooncai.yukireflection.finder.callable.KFunctionFinder
import io.github.dreammooncai.yukireflection.finder.callable.KPropertyFinder
import io.github.dreammooncai.yukireflection.finder.signature.KFunctionSignatureFinder
import io.github.dreammooncai.yukireflection.finder.signature.KPropertySignatureFinder
import io.github.dreammooncai.yukireflection.type.factory.KConstructorConditions
import io.github.dreammooncai.yukireflection.type.factory.KFunctionConditions
import io.github.dreammooncai.yukireflection.type.factory.KFunctionSignatureConditions
import io.github.dreammooncai.yukireflection.type.factory.KPropertyConditions
import io.github.dreammooncai.yukireflection.type.factory.KPropertySignatureConditions
import java.util.WeakHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

/**
 * 缓存属性弱引用列表
 */
private val cacheProperty = WeakHashMap<String,KPropertyFinder.Result>()

/**
 * 查找并得到变量 - 查询的结果将按照描述符缓存使用
 * @param descriptor 缓存所使用的描述符 - 默认使用 [KFunctionFinder.rulesData]
 * @param initiate 查找方法体
 * @return [KPropertyFinder.Result]
 */
inline fun KClass<*>.cacheProperty(descriptor:String? = null,initiate: KPropertyConditions = {}):KPropertyFinder.Result{
    return if (descriptor == null) {
        val finder = KPropertyFinder(classSet = this).apply(initiate)
        cacheProperty.getOrPut("[${this.name}][$classLoader][property][${finder.rulesData}]"){ finder.build() as KPropertyFinder.Result }
    }else{
        cacheProperty.getOrPut("[${this.name}][$classLoader][property][$descriptor]"){ property(initiate) }
    }
}

/**
 * 查找并得到变量 - 查询的结果将按照描述符缓存使用
 * @param attachProperty 用于附加条件的属性
 * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
 * @param isUseMember 是否将属性转换为JavaField再进行附加 - 即使为false当属性附加错误时依然会尝试JavaField - 为true时会导致类型擦除
 * @param initiate 查找方法体 - 默认使用 [KPropertyFinder.attach] 将 [attachProperty] 进行附加
 * @return [KPropertyFinder.Result]
 */
inline fun KClass<*>.cacheProperty(attachProperty:KProperty<*>,loader: ClassLoader? = null,isUseMember:Boolean = false,initiate: KPropertyConditions = { attachProperty.attach(loader, isUseMember) }) = cacheProperty(null,initiate)


/**
 * 缓存属性签名弱引用列表
 */
private val cachePropertySignature = WeakHashMap<String,KPropertySignatureFinder.Result>()

/**
 * 查找并得到方法签名 - 查询的结果将按照描述符缓存使用
 *
 * 获取此 [KClass] 指定 [initiate] 条件的签名
 *
 * 此方法以通过 [Metadata] 中定义的属性名获取Java层真正的签名
 *
 * [KPropertySignatureConditions] 中对属性类型进行筛选如果目标类型也有问题可能依然会出错，建议使用属性名筛选
 *
 * - 此方法不涉及转 Kotlin 的反射属性可以避免一些异常 [Metadata] 数据报错
 * @param descriptor 缓存所使用的描述符 - 默认使用 [KFunctionFinder.rulesData]
 * @param loader [ClassLoader] 相关涉及的类型所在的 [ClassLoader]
 * @param initiate 查找方法体
 * @return [KFunctionFinder.Result]
 */
inline fun KClass<*>.cachePropertySignature(descriptor:String? = null,loader: ClassLoader? = null, initiate: KPropertySignatureConditions = {}): KPropertySignatureFinder.Result{
    return if (descriptor == null) {
        val finder = KPropertySignatureFinder(classSet = this, loader).apply(initiate)
        cachePropertySignature.getOrPut("[${this.name}][${loader ?: classLoader}][propertySignature][${finder.rulesData}]"){ finder.build() }
    }else{
        cachePropertySignature.getOrPut("[${this.name}][${loader ?: classLoader}][propertySignature][$descriptor]"){ propertySignature(loader,initiate) }
    }
}

/**
 * 查找并得到方法签名 - 查询的结果将按照描述符缓存使用
 *
 * 获取此 [KClass] 指定 [initiate] 条件的签名
 *
 * 此方法以通过 [Metadata] 中定义的属性名获取Java层真正的签名
 *
 * [KPropertySignatureConditions] 中对属性类型进行筛选如果目标类型也有问题可能依然会出错，建议使用属性名筛选
 *
 * - 此方法不涉及转 Kotlin 的反射属性可以避免一些异常 [Metadata] 数据报错
 * @param attachProperty 用于附加条件的属性
 * @param loader [ClassLoader] 相关涉及的类型所在的 [ClassLoader]
 * @param isUseMember 是否将属性转换为JavaField再进行附加 - 即使为false当属性附加错误时依然会尝试JavaField - 为true时会导致类型擦除
 * @param initiate 查找方法体 - 默认使用 [KPropertyFinder.attach] 将 [attachProperty] 进行附加
 * @return [KFunctionFinder.Result]
 */
inline fun KClass<*>.cachePropertySignature(attachProperty:KProperty<*>,loader: ClassLoader? = null,isUseMember:Boolean = false, initiate: KPropertySignatureConditions = { attachProperty.attach(loader,isUseMember) }) = cachePropertySignature(null,loader,initiate)

/**
 * 缓存函数弱引用列表
 */
private val cacheFunction = WeakHashMap<String,KFunctionFinder.Result>()

/**
 * 查找并得到方法 - 查询的结果将按照描述符缓存使用
 * @param descriptor 缓存所使用的描述符 - 默认使用 [KFunctionFinder.rulesData]
 * @param initiate 查找方法体
 * @return [KFunctionFinder.Result]
 */
inline fun KClass<*>.cacheFunction(descriptor:String? = null,initiate: KFunctionConditions = {}):KFunctionFinder.Result{
    return if (descriptor == null) {
        val finder = KFunctionFinder(classSet = this).apply(initiate)
        cacheFunction.getOrPut("[${this.name}][$classLoader][function][${finder.rulesData}]"){ finder.build() as KFunctionFinder.Result }
    }else{
        cacheFunction.getOrPut("[${this.name}][$classLoader][function][$descriptor]"){ function(initiate) }
    }
}

/**
 * 查找并得到方法 - 查询的结果将按照描述符缓存使用
 * @param attachFunction 用于附加条件的属性
 * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
 * @param isUseMember 是否将属性转换为JavaField再进行附加 - 即使为false当属性附加错误时依然会尝试JavaField - 为true时会导致类型擦除
 * @param initiate 查找方法体 - 默认使用 [KFunctionFinder.attach] 将 [attachFunction] 进行附加 但不包括 [KFunctionFinder.name]
 * @return [KFunctionFinder.Result]
 */
inline fun KClass<*>.cacheFunction(attachFunction:KFunction<*>,loader: ClassLoader? = null,isUseMember:Boolean = false,initiate: KFunctionConditions = {
    attachFunction.attach(loader, isUseMember)
    name = ""
}) = cacheFunction(null,initiate)


/**
 * 缓存函数签名弱引用列表
 */
private val cacheFunctionSignature = WeakHashMap<String,KFunctionSignatureFinder.Result>()

/**
 * 查找并得到方法签名 - 查询的结果将按照描述符缓存使用
 *
 * 获取此 [KClass] 指定 [initiate] 条件的签名
 *
 * 此方法以通过 [Metadata] 中定义的函数名获取Java层真正的签名
 *
 * [KFunctionSignatureConditions] 中对返回类型和参数类型进行筛选如果目标类型也有问题可能依然会出错，建议使用参数名筛选
 *
 * - 此方法不涉及转 Kotlin 的反射函数可以避免一些异常 [Metadata] 数据报错
 * @param descriptor 缓存所使用的描述符 - 默认使用 [KFunctionFinder.rulesData]
 * @param loader [ClassLoader] 相关涉及的类型所在的 [ClassLoader]
 * @param initiate 查找方法体
 * @return [KFunctionSignatureFinder.Result]
 */
inline fun KClass<*>.cacheFunctionSignature(descriptor:String? = null,loader: ClassLoader? = null, initiate: KFunctionSignatureConditions = {}): KFunctionSignatureFinder.Result{
    return if (descriptor == null) {
        val finder = KFunctionSignatureFinder(classSet = this, loader).apply(initiate)
        cacheFunctionSignature.getOrPut("[${this.name}][${loader ?: classLoader}][functionSignature][${finder.rulesData}]"){ finder.build() }
    }else{
        cacheFunctionSignature.getOrPut("[${this.name}][${loader ?: classLoader}][functionSignature][$descriptor]"){ functionSignature(loader,initiate) }
    }
}

/**
 * 查找并得到方法签名 - 查询的结果将按照描述符缓存使用
 *
 * 获取此 [KClass] 指定 [initiate] 条件的签名
 *
 * 此方法以通过 [Metadata] 中定义的函数名获取Java层真正的签名
 *
 * [KFunctionSignatureConditions] 中对返回类型和参数类型进行筛选如果目标类型也有问题可能依然会出错，建议使用参数名筛选
 *
 * - 此方法不涉及转 Kotlin 的反射函数可以避免一些异常 [Metadata] 数据报错
 * @param attachFunction 用于附加条件的属性
 * @param loader [ClassLoader] 相关涉及的类型所在的 [ClassLoader]
 * @param isUseMember 是否将属性转换为JavaField再进行附加 - 即使为false当属性附加错误时依然会尝试JavaField - 为true时会导致类型擦除
 * @param initiate 查找方法体 - 默认使用 [KFunctionFinder.attach] 将 [attachFunction] 进行附加 但不包括 [KFunctionFinder.name]
 * @return [KFunctionSignatureFinder.Result]
 */
inline fun KClass<*>.cacheFunctionSignature(attachFunction:KFunction<*>,loader: ClassLoader? = null,isUseMember:Boolean = false,initiate: KFunctionSignatureConditions = {
    attachFunction.attach(loader,isUseMember)
    name = ""
}) = cacheFunctionSignature(null,loader,initiate)


/**
 * 缓存函数签名弱引用列表
 */
private val cacheConstructor = WeakHashMap<String,KConstructorFinder.Result>()

/**
 * 查找并得到构造方法 - 查询的结果将按照描述符缓存使用
 * @param descriptor 缓存所使用的描述符 - 默认使用 [KFunctionFinder.rulesData]
 * @param initiate 查找方法体
 * @return [KConstructorFinder.Result]
 */
inline fun KClass<*>.cacheConstructor(descriptor:String? = null,initiate: KConstructorConditions = {}):KConstructorFinder.Result =
    (if (descriptor == null) {
        val finder = KConstructorFinder(classSet = this).apply(initiate)
        cacheConstructor.getOrPut("[${this.name}][$classLoader][constructor][${finder.rulesData}]"){ finder.build() }
    } else cacheConstructor.getOrPut("[${this.name}][$classLoader][constructor][$descriptor]"){ constructor(initiate) })

/**
 * 查找并得到构造方法 - 查询的结果将按照描述符缓存使用
 * @param attachFunction 用于附加条件的属性
 * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
 * @param isUseMember 是否将属性转换为JavaField再进行附加 - 即使为false当属性附加错误时依然会尝试JavaField - 为true时会导致类型擦除
 * @param initiate 查找方法体
 * @return [KConstructorFinder.Result]
 */
inline fun KClass<*>.cacheConstructor(attachFunction:KFunction<*>,loader: ClassLoader? = null,isUseMember:Boolean = false,initiate: KConstructorConditions = { attachFunction.attach(loader,isUseMember) }) = cacheConstructor(null,initiate)