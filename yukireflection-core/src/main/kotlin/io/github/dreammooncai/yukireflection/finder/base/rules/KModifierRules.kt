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
 * This file is created by fankes on 2022/3/27.
 * This file is modified by fankes on 2022/9/14.
 * This file is modified by fankes on 2023/1/21.
 */
@file:Suppress("unused")

package io.github.dreammooncai.yukireflection.finder.base.rules

import io.github.dreammooncai.yukireflection.factory.*
import java.lang.reflect.Member
import java.lang.reflect.Modifier
import kotlin.reflect.*

/**
 * 这是一个 [KClass]、[KCallable] 描述符条件实现类
 *
 * 可对 R8 混淆后的 [KClass]、[KCallable] 进行更加详细的定位
 * @param instance 当前实例对象
 */
class KModifierRules private constructor(private val instance: Any) {

    internal companion object {

        /** 当前实例数组 */
        private val instances = mutableMapOf<Long, KModifierRules>()

        /**
         * 获取模板字符串数组
         * @param value 唯一标识值
         * @return [MutableList]<[String]>
         */
        internal fun templates(value: Long) = instances[value]?.templates ?: mutableListOf()

        /**
         * 创建实例
         * @param instance 实例对象
         * @param value 唯一标识值 - 默认 0
         * @return [KModifierRules]
         */
        internal fun with(instance: Any, value: Long = 0) = KModifierRules(instance).apply { instances[value] = this }
    }

    /** 当前模板字符串数组 */
    private val templates = mutableListOf<String>()

    /**
     * [KClass]、[KCallable] 类型是否包含 public
     *
     * 如下所示 ↓
     *
     * public class/void/int/String...
     *
     * ^^^
     * @return [Boolean]
     */
    val isPublic get() = Modifier.isPublic(modifiers).also { templates.add("<isPublic> ($it)") }

    /**
     * [KClass]、[KCallable] 类型是否包含 private
     *
     * 如下所示 ↓
     *
     * private class/void/int/String...
     *
     * ^^^
     * @return [Boolean]
     */
    val isPrivate get() = Modifier.isPrivate(modifiers).also { templates.add("<isPrivate> ($it)") }

    /**
     * [KClass]、[KCallable] 类型是否包含 protected
     *
     * 如下所示 ↓
     *
     * protected class/void/int/String...
     *
     * ^^^
     * @return [Boolean]
     */
    val isProtected get() = Modifier.isProtected(modifiers).also { templates.add("<isProtected> ($it)") }

    /**
     * [KClass]、[KCallable] 类型是否包含 static
     *
     * 对于任意的静态 [KClass]、[KCallable] 可添加此描述进行确定
     *
     * 如下所示 ↓
     *
     * static class/void/int/String...
     *
     * ^^^
     *
     * - 注意 Kotlin → Jvm 后的 object 类中的方法并不是静态的
     * @return [Boolean]
     */
    val isStatic get() = Modifier.isStatic(modifiers).also { templates.add("<isStatic> ($it)") }

    /**
     * [KClass]、[KCallable] 类型是否包含 final
     *
     * 如下所示 ↓
     *
     * final class/void/int/String...
     *
     * ^^^
     *
     * - 注意 Kotlin → Jvm 后没有 open 标识的 [KClass]、[KCallable] 和没有任何关联的 [KClass]、[KCallable] 都将为 final
     * @return [Boolean]
     */
    val isFinal get() = Modifier.isFinal(modifiers).also { templates.add("<isFinal> ($it)") }

    /**
     * [KClass]、[KCallable] 类型是否包含 synchronized
     *
     * 如下所示 ↓
     *
     * synchronized class/void/int/String...
     *
     * ^^^
     * @return [Boolean]
     */
    val isSynchronized get() = Modifier.isSynchronized(modifiers).also { templates.add("<isSynchronized> ($it)") }

    /**
     * [KProperty] 类型是否包含 volatile
     *
     * 如下所示 ↓
     *
     * volatile int/String...
     *
     * ^^^
     * @return [Boolean]
     */
    val isVolatile get() = Modifier.isVolatile(modifiers).also { templates.add("<isVolatile> ($it)") }

    /**
     * [KProperty] 类型是否包含 transient
     *
     * 如下所示 ↓
     *
     * transient int/String...
     *
     * ^^^
     * @return [Boolean]
     */
    val isTransient get() = Modifier.isTransient(modifiers).also { templates.add("<isTransient> ($it)") }

    /**
     * [KFunction] 类型是否包含 native
     *
     * 对于任意 JNI 对接的 [KFunction] 可添加此描述进行确定
     *
     * 如下所示 ↓
     *
     * native void/int/String...
     *
     * ^^^
     * @return [Boolean]
     */
    val isNative get() = Modifier.isNative(modifiers).also { templates.add("<isNative> ($it)") }

    /**
     * [KClass] 类型是否包含 interface
     *
     * 如下所示 ↓
     *
     * interface ...
     *
     * ^^^
     * @return [Boolean]
     */
    val isInterface get() = Modifier.isInterface(modifiers).also { templates.add("<isInterface> ($it)") }

    /**
     * [KClass]、[KCallable] 类型是否包含 abstract
     *
     * 对于任意的抽象 [KClass]、[KCallable] 可添加此描述进行确定
     *
     * 如下所示 ↓
     *
     * abstract class/void/int/String...
     *
     * ^^^
     * @return [Boolean]
     */
    val isAbstract get() = Modifier.isAbstract(modifiers).also { templates.add("<isAbstract> ($it)") }

    /**
     * [KClass]、[KCallable] 类型是否包含 strictfp
     *
     * 如下所示 ↓
     *
     * strictfp class/void/int/String...
     *
     * ^^^
     * @return [Boolean]
     */
    val isStrict get() = Modifier.isStrict(modifiers).also { templates.add("<isStrict> ($it)") }


    /**
     * [KClass]、[KCallable] 所属类是否是 Kotlin 类
     *
     * @return [Boolean]
     */
    val isKotlin get() = (declaringClass?.isKotlin ?: false).also { templates.add("<isKotlin> ($it)") }

    /**
     * [KClass]、[KCallable] 所属类是否是 Kotlin 的 companion object
     *
     * 如下所示 ↓
     *
     * companion object{ val a = 1 } -> ::a isCompanion true
     *
     * ^^^
     * @return [Boolean]
     */
    val isCompanion get() = (declaringClass?.kotlin?.isCompanion ?: false).also { templates.add("<isCompanion> ($it)") }

    /**
     * [KClass]、[KCallable] 所属类是否是 Kotlin Data 类
     *
     * 如下所示 ↓
     *
     * data class A(val a = 1) -> ::a isData true
     *
     * ^^^
     * @return [Boolean]
     */
    val isData get() = (declaringClass?.kotlin?.isData ?: false).also { templates.add("<isData> ($it)") }

    /**
     * [KClass]、[KCallable] 所属类是否是 Kotlin 的 inner class
     *
     * 如下所示 ↓
     *
     * inner class A{ val a = 1 } -> ::a isInner true
     *
     * ^^^
     * @return [Boolean]
     */
    val isInner get() = (declaringClass?.kotlin?.isInner ?: false).also { templates.add("<isInner> ($it)") }

    /**
     * [KClass]、[KCallable] 所属类是否是 Kotlin 的 sealed class
     *
     * 如下所示 ↓
     *
     * sealed class A{ val a = 1 } -> ::a isSealed true
     *
     * ^^^
     * @return [Boolean]
     */
    val isSealed get() = (declaringClass?.kotlin?.isSealed ?: false).also { templates.add("<isSealed> ($it)") }

    /**
     * [KClass]、[KCallable] 所属类是否是 Kotlin 的 fun class
     *
     * 如下所示 ↓
     *
     * fun class A{ val a = 1 } -> ::a isFun true
     *
     * ^^^
     * @return [Boolean]
     */
    val isFun get() = (declaringClass?.kotlin?.isFun ?: false).also { templates.add("<isFun> ($it)") }

    /**
     * [KClass]、[KCallable] 所属类是否是 Kotlin 的 value class
     *
     * 如下所示 ↓
     *
     * value class A{ val a = 1 } -> ::a isValue true
     *
     * ^^^
     * @return [Boolean]
     */
    val isValue get() = (declaringClass?.kotlin?.isValue ?: false).also { templates.add("<isValue> ($it)") }

    /**
     * [KClass]、[KCallable] 所属类是否是 Kotlin 的 anonymous class
     *
     * 如下所示 ↓
     *
     * val a = object:{val b = 1} ::b isAnonymous true
     *
     * ^^^
     * @return [Boolean]
     */
    val isAnonymous get() = (declaringClass?.isAnonymousClass ?: false).also { templates.add("<isAnonymous> ($it)") }

    /**
     * [KProperty] 是否是 Kotlin 的 const 属性
     *
     * 如下所示 ↓
     *
     * const val a = 1
     *
     * ^^^
     * @return [Boolean]
     */
    val isConst get() = (asKProperty?.isConst ?: false).also { templates.add("<isConst> ($it)") }

    /**
     * [KProperty] 是否是 Kotlin 的 lateinit 属性
     *
     * 如下所示 ↓
     *
     * lateinit var a = 1
     *
     * ^^^
     * @return [Boolean]
     */
    val isLateinit get() = (asKProperty?.isLateinit ?: false).also { templates.add("<isLateinit> ($it)") }

    /**
     * [KProperty] 是否是 Kotlin 的 var 属性
     *
     * 如下所示 ↓
     *
     * var a = 1
     *
     * ^^^
     * @return [Boolean]
     */
    val isVar get() = (asKProperty?.isVar ?: false).also { templates.add("<isVar> ($it)") }

    /**
     * [KProperty] 是否是 Kotlin 的 val 属性
     *
     * 如下所示 ↓
     *
     * val a = 1
     *
     * ^^^
     * @return [Boolean]
     */
    val isVal get() = (asKProperty?.isVal ?: false).also { templates.add("<isVar> ($it)") }

    /**
     * [KCallable] 是否是 Kotlin 的 suspend 属性
     *
     * 如下所示 ↓
     *
     * suspend fun a() = 1
     *
     * ^^^
     * @return [Boolean]
     */
    val isSuspend get() = (asKCallable?.isSuspend ?: false).also { templates.add("<isSuspend> ($it)") }

    /**
     * [KFunction] 是否是 Kotlin 的 external 属性
     *
     * 如下所示 ↓
     *
     * external fun a()
     *
     * ^^^
     * @return [Boolean]
     */
    val isExternal get() = (asKFunction?.isExternal ?: false).also { templates.add("<isExternal> ($it)") }

    /**
     * [KCallable] 是否是 Kotlin 的 extensionRef 属性
     *
     * 如下所示 ↓
     *
     * fun A.a()
     *
     * ^^^
     * @return [Boolean]
     */
    val isExtension get() = (asKCallable?.isExtension ?: false).also { templates.add("<isExtension> ($it)") }

    /**
     * [KClass]、[KCallable] 是否是 Kotlin 的 顶级 属性
     *
     * 如下所示 ↓
     *
     * file A.kt
     *
     * fun a() -> ::a isTop true
     *
     * class A
     *
     * ^^^
     * @return [Boolean]
     */
    val isTop get() = (asKCallable?.declaringClass?.isTop ?: (instance as? KClass<*>)?.isTop ?: false).also { templates.add("<isTop> ($it)") }

    /**
     * [KFunction] 是否是 Kotlin 的 infix 属性
     *
     * 如下所示 ↓
     *
     * infix fun a(b:Int) = 1
     *
     * ^^^
     * @return [Boolean]
     */
    val isInfix get() = (asKFunction?.isInfix ?: false).also { templates.add("<isInfix> ($it)") }

    /**
     * [KFunction] 是否是 Kotlin 的 operator 属性
     *
     * 如下所示 ↓
     *
     * operator fun a(b:Int) = 1
     *
     * ^^^
     * @return [Boolean]
     */
    val isOperator get() = (asKFunction?.isOperator ?: false).also { templates.add("<isOperator> ($it)") }

    /**
     * [KFunction] 是否是 Kotlin 的 inline 属性
     *
     * 如下所示 ↓
     *
     * inline fun a(b:Int) = 1
     *
     * ^^^
     * @return [Boolean]
     */
    val isInline get() = (asKFunction?.isInline ?: false).also { templates.add("<isInline> ($it)") }

    /**
     * [KClass]、[KCallable] 是否是 Kotlin 的 open 属性
     *
     * 如下所示 ↓
     *
     * open class A{ val a = 1 }
     * open fun a(){}
     * open val a = 1
     *
     * ^^^
     * @return [Boolean]
     */
    val isOpen get() = when (instance) {
        is KCallable<*> -> instance.isOpen
        is KClass<*> -> instance.isOpen
        is Class<*> -> instance.kotlin.isOpen
        is Member -> instance.kotlin.isOpen
        else -> false
    }.also { templates.add("<isOpen> ($it)") }

    /**
     * 获取当前对象的类型描述符
     * @return [Int]
     */
    private val modifiers
        get() = when (instance) {
            is KCallable<*> -> instance.modifiers ?: 0
            is KClass<*> -> instance.java.modifiers
            is Class<*> -> instance.modifiers
            is Member -> instance.modifiers
            else -> 0
        }

    /**
     * 获取当前对象的声明类
     */
    private val declaringClass
        get() = when(instance){
            is KCallable<*> -> instance.declaringClass?.java
            is KClass<*> -> instance.java
            is Class<*> -> instance
            is Member -> instance.declaringClass
            else -> null
        }

    /**
     * 获取当前对象的 [KCallable]
     */
    private val asKCallable
        get() = instance as? KCallable<*>

    /**
     * 获取当前对象的 [KFunction]
     */
    private val asKFunction
        get() = instance as? KFunction<*>

    /**
     * 获取当前对象的 [KProperty]
     */
    private val asKProperty
        get() = instance as? KProperty<*>

    override fun toString() = "KModifierRules [$instance]"
}