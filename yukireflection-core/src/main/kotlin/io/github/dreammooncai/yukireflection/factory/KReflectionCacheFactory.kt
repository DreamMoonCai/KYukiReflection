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
import kotlin.reflect.jvm.jvmName

/**
 * 缓存弱引用列表
 */
private val cache = WeakHashMap<String,Any>()

/**
 * 查找并得到变量 - 查询的结果将按照描述符缓存使用
 * @param descriptor 缓存所使用的描述符 - 默认使用 [KPropertyFinder.name] 如果没有则报错
 * @param initiate 查找方法体
 * @return [KPropertyFinder.Result]
 */
inline fun KClass<*>.cacheProperty(descriptor:String? = null,initiate: KPropertyConditions = {}):KPropertyFinder.Result{
    return if (descriptor == null) {
        val finder = KPropertyFinder(classSet = this).apply(initiate)
        val name = finder.name.ifBlank { throw IllegalArgumentException("cacheProperty name is blank") }
        cache.getOrElse("[${this.name}][$classLoader][property][$name]"){ finder.build() }
    }else{
        cache.getOrElse("[${this.name}][$classLoader][property][$descriptor]"){ property(initiate) }
    } as KPropertyFinder.Result
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
 * @param descriptor 缓存所使用的描述符 - 默认使用 [KPropertySignatureFinder.name] 如果没有则报错
 * @param loader [ClassLoader] 相关涉及的类型所在的 [ClassLoader]
 * @param initiate 查找方法体
 * @return [KFunctionFinder.Result]
 */
inline fun KClass<*>.cachePropertySignature(descriptor:String? = null,loader: ClassLoader? = null, initiate: KPropertySignatureConditions = {}): KPropertySignatureFinder.Result{
    return if (descriptor == null) {
        val finder = KPropertySignatureFinder(classSet = this, loader).apply(initiate)
        val name = finder.name.ifBlank { throw IllegalArgumentException("cachePropertySignature name is blank") }
        cache.getOrElse("[${this.name}][${loader ?: classLoader}][propertySignature][$name]"){ finder.build() }
    }else{
        cache.getOrElse("[${this.name}][${loader ?: classLoader}][propertySignature][$descriptor]"){ propertySignature(loader,initiate) }
    } as KPropertySignatureFinder.Result
}

/**
 * 查找并得到方法 - 查询的结果将按照描述符缓存使用
 * @param descriptor 缓存所使用的描述符 - 默认使用 [KFunctionFinder.name] 如果没有则报错
 * @param initiate 查找方法体
 * @return [KFunctionFinder.Result]
 */
inline fun KClass<*>.cacheFunction(descriptor:String? = null,initiate: KFunctionConditions = {}):KFunctionFinder.Result{
    return if (descriptor == null) {
        val finder = KFunctionFinder(classSet = this).apply(initiate)
        val name = finder.name.ifBlank { throw IllegalArgumentException("cacheProperty name is blank") }
        cache.getOrElse("[${this.name}][$classLoader][function][$name]"){ finder.build() }
    }else{
        cache.getOrElse("[${this.name}][$classLoader][function][$descriptor]"){ function(initiate) }
    } as KFunctionFinder.Result
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
 * @param descriptor 缓存所使用的描述符 - 默认使用 [KFunctionSignatureFinder.name] 如果没有则报错
 * @param loader [ClassLoader] 相关涉及的类型所在的 [ClassLoader]
 * @param initiate 查找方法体
 * @return [KFunctionSignatureFinder.Result]
 */
inline fun KClass<*>.cacheFunctionSignature(descriptor:String? = null,loader: ClassLoader? = null, initiate: KFunctionSignatureConditions = {}): KFunctionSignatureFinder.Result{
    return if (descriptor == null) {
        val finder = KFunctionSignatureFinder(classSet = this, loader).apply(initiate)
        val name = finder.name.ifBlank { throw IllegalArgumentException("cachePropertySignature name is blank") }
        cache.getOrElse("[${this.name}][${loader ?: classLoader}][functionSignature][$name]"){ finder.build() }
    }else{
        cache.getOrElse("[${this.name}][${loader ?: classLoader}][functionSignature][$descriptor]"){ functionSignature(loader,initiate) }
    } as KFunctionSignatureFinder.Result
}

/**
 * 查找并得到构造方法 - 查询的结果将按照描述符缓存使用
 * @param descriptor 缓存所使用的描述符
 * @param initiate 查找方法体
 * @return [KConstructorFinder.Result]
 */
inline fun KClass<*>.cacheConstructor(descriptor:String,initiate: KConstructorConditions = {}):KConstructorFinder.Result =
    cache.getOrElse("[${this.name}][$classLoader][constructor][$descriptor]"){ constructor(initiate) } as KConstructorFinder.Result