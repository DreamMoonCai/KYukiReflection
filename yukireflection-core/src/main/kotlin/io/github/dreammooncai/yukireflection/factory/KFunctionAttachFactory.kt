@file:Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE","MISSING_DEPENDENCY_SUPERCLASS")
package io.github.dreammooncai.yukireflection.factory

import io.github.dreammooncai.yukireflection.finder.callable.KFunctionFinder
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2
import kotlin.reflect.KFunction3
import kotlin.reflect.full.valueParameters

/**
 * 将此函数相关内容附加到此查找器
 *
 * 将影响[KFunctionFinder.name]、[KFunctionFinder.returnType]、[KFunctionFinder.param] - 如果使用 attachCallable 附加 则额外影响 [KFunctionFinder.paramName]
 *
 * 重载引用使用示例 ↓
 *
 * ```kotlin
 *
 *  class Main{
 *      fun sub(a:Int):Int{}
 *
 *      fun sub(c:Double):String{}
 *  }
 *
 *  attach(Main::sub) // error:不知道附加哪个函数
 *  attach<String>(Main::sub) // 将使用返回类型为String的函数
 * ```
 *
 * @param R 返回类型
 * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
 * @param isUseMember 是否将函数转换为JavaMethod再进行附加 - 即使为false当函数附加错误时依然会尝试JavaMethod - 为true时会导致类型擦除
 */
@JvmName("attach_1")
fun <R> KFunctionFinder.attach(function: KFunction<R>, loader: ClassLoader? = null, isUseMember:Boolean = false){
    fun KClass<*>.toClass() = if (loader == null) this else toKClass(loader)

    fun attachMember(e:Throwable? = null){
        val method = function.javaMethodNoError ?: function.refImpl?.javaMethodNoError ?: let {
            errorMsg("If you can't attach this function or the function is hidden by Kotlin to prohibit reflection, you can try to use [java.method {}] to do the reflection !!!", e)
            return
        }
        this@attach.name = method.name
        this@attach.returnType = method.returnType.kotlin.toClass()
        if (method.parameterTypes.isEmpty())
            emptyParam()
        else
            param(*method.parameterTypes.map { it.kotlin.toClass() }.toTypedArray())
    }
    fun attachCallable(function: KFunction<*>){
        this@attach.name = function.name
        this@attach.returnType = runCatching {
            if (loader != null)
                function.returnClass.toClass()
            else
                function.returnType
        }.getOrNull() ?: function.returnClass.toClass()
        if (function.valueParameters.isEmpty())
            emptyParam()
        else
            param(*function.valueParameters.map {
                if (loader != null)
                    it.kotlin.toClass()
                else
                    it
            }.toTypedArray())
        paramName(*function.valueParameters.mapNotNull { it.name }.toTypedArray())
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
 * 将此函数相关内容附加到此查找器
 *
 * 将影响[KFunctionFinder.name]、[KFunctionFinder.returnType]、[KFunctionFinder.param] - 如果使用 attachCallable 附加 则额外影响 [KFunctionFinder.paramName]
 *
 * 重载引用使用示例 ↓
 *
 * ```java
 *
 *  class Main{
 *      public static void sub(){}
 *      public void sub(){}
 *  }
 *
 *  attach(Main::sub) // error:不知道附加哪个函数
 *  attachStatic(Main::sub) // 将使用静态sub
 * ```
 *
 * @param R 返回类型
 * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
 * @param isUseMember 是否将函数转换为JavaMethod再进行附加 - 即使为false当函数附加错误时依然会尝试JavaMethod - 为true时会导致类型擦除
 */
@JvmName("attachStatic_0")
inline fun <R> KFunctionFinder.attachStatic(function: KFunction0<R>, loader: ClassLoader? = null, isUseMember:Boolean = false){
    attach(function,loader,isUseMember)
}

/**
 * 将此函数相关内容附加到此查找器
 *
 * 将影响[KFunctionFinder.name]、[KFunctionFinder.returnType]、[KFunctionFinder.param] - 如果使用 attachCallable 附加 则额外影响 [KFunctionFinder.paramName]
 *
 * 重载引用使用示例 ↓
 *
 * ```java
 *
 *  class Main{
 *      public void sub(a:Int):String{}
 *      public void sub():String{}
 *      public void sub(b:Int):Int{}
 *  }
 *
 *  attach<String>(Main::sub) // error:尽管筛选了返回值但依然不知道附加哪个函数
 *  attachEmptyParam(Main::sub) // 将使用没有参数sub
 * ```
 *
 * @param R 返回类型
 * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
 * @param isUseMember 是否将函数转换为JavaMethod再进行附加 - 即使为false当函数附加错误时依然会尝试JavaMethod - 为true时会导致类型擦除
 */
inline fun <R> KFunctionFinder.attachEmptyParam(function: KFunction1<*, R>, loader: ClassLoader? = null, isUseMember:Boolean = false) {
    attach(function, loader, isUseMember)
}

/**
 * 将此函数相关内容附加到此查找器 - 指定参数的快捷方法 参阅:[attach]
 *
 * 将影响[KFunctionFinder.name]、[KFunctionFinder.returnType]、[KFunctionFinder.param] - 如果使用 attachCallable 附加 则额外影响 [KFunctionFinder.paramName]
 *
 * 重载引用使用示例 ↓
 *
 * ```kotlin
 *
 *  class Main{
 *      fun sub(a:Int):Int{}
 *
 *      fun sub(c:Double):Int{}
 *  }
 *
 *  attach(Main::sub) // error:不知道附加哪个函数
 *  attach<Double,Int>(Main::sub) // 将使用第一个参数为Double返回类型为Int的函数
 * ```
 *
 * @param P1 第一个参数的类型
 * @param R 返回类型
 * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
 * @param isUseMember 是否将函数转换为JavaMethod再进行附加 - 即使为false当函数附加错误时依然会尝试JavaMethod - 为true时会导致类型擦除
 */
@JvmName("attach_2")
inline fun <P1, R> KFunctionFinder.attach(function: KFunction2<*, P1, R>, loader: ClassLoader? = null, isUseMember:Boolean = false){
    attach<R>(function,loader, isUseMember)
}

/**
 * 将此函数相关内容附加到此查找器 - 指定参数的快捷方法 参阅:[attach]
 *
 * 将影响[KFunctionFinder.name]、[KFunctionFinder.returnType]、[KFunctionFinder.param] - 如果使用 attachCallable 附加 则额外影响 [KFunctionFinder.paramName]
 *
 * 重载引用参考[KFunctionFinder.attach]
 *
 * @param P1 第一个参数的类型
 * @param P2 第二个参数的类型
 * @param R 返回类型
 * @param loader 默认不使用 [ClassLoader] ，如果使用 [ClassLoader] 将把涉及的类型，转换为指定 [ClassLoader] 中的 [KClass] 并且会擦除泛型
 * @param isUseMember 是否将函数转换为JavaMethod再进行附加 - 即使为false当函数附加错误时依然会尝试JavaMethod - 为true时会导致类型擦除
 */
@JvmName("attach_3")
inline fun <P1,P2,R> KFunctionFinder.attach(function: KFunction3<*, P1, P2, R>, loader: ClassLoader? = null, isUseMember:Boolean = false){
    attach<R>(function,loader, isUseMember)
}

/**
 * @see [KFunctionFinder.attach]
 */
@JvmName("attach_4")
inline fun <P1,P2,P3,R> KFunctionFinder.attach(function: kotlin.reflect.KFunction4<*, P1, P2,P3, R>, loader: ClassLoader? = null, isUseMember:Boolean = false){
    attach<R>(function,loader, isUseMember)
}

/**
 * @see [KFunctionFinder.attach]
 */
@JvmName("attach_5")
inline fun <P1,P2,P3,P4,R> KFunctionFinder.attach(function: kotlin.reflect.KFunction5<*, P1, P2,P3,P4, R>, loader: ClassLoader? = null, isUseMember:Boolean = false){
    attach<R>(function,loader, isUseMember)
}

/**
 * @see [KFunctionFinder.attach]
 */
@JvmName("attach_6")
inline fun <P1,P2,P3,P4,P5,R> KFunctionFinder.attach(function: kotlin.reflect.KFunction6<*, P1, P2,P3,P4,P5, R>, loader: ClassLoader? = null, isUseMember:Boolean = false){
    attach<R>(function,loader, isUseMember)
}

/**
 * @see [KFunctionFinder.attach]
 */
@JvmName("attach_7")
inline fun <P1,P2,P3,P4,P5,P6,R> KFunctionFinder.attach(function: kotlin.reflect.KFunction7<*, P1, P2,P3,P4,P5,P6, R>, loader: ClassLoader? = null, isUseMember:Boolean = false){
    attach<R>(function,loader, isUseMember)
}

/**
 * @see [KFunctionFinder.attach]
 */
@JvmName("attach_8")
inline fun <P1,P2,P3,P4,P5,P6,P7,R> KFunctionFinder.attach(function: kotlin.reflect.KFunction8<*, P1, P2,P3,P4,P5,P6,P7, R>, loader: ClassLoader? = null, isUseMember:Boolean = false){
    attach<R>(function,loader, isUseMember)
}

/**
 * @see [KFunctionFinder.attach]
 */
@JvmName("attach_9")
inline fun <P1,P2,P3,P4,P5,P6,P7,P8,R> KFunctionFinder.attach(function: kotlin.reflect.KFunction9<*, P1, P2,P3,P4,P5,P6,P7,P8,R>, loader: ClassLoader? = null, isUseMember:Boolean = false){
    attach<R>(function,loader, isUseMember)
}

/**
 * @see [KFunctionFinder.attach]
 */
@JvmName("attach_10")
inline fun <P1,P2,P3,P4,P5,P6,P7,P8,P9,R> KFunctionFinder.attach(function: kotlin.reflect.KFunction10<*, P1, P2,P3,P4,P5,P6,P7,P8,P9,R>, loader: ClassLoader? = null, isUseMember:Boolean = false){
    attach<R>(function,loader, isUseMember)
}

//---------------------------------------------------------------------------------------------------------------------
//再多具参方法不再建议使用附加功能 最多到 kotlin.reflect.KFunction22