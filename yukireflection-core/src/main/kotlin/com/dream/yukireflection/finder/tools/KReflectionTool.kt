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
 * This file is modified by fankes on 2023/1/21.
 */
@file:Suppress("KotlinConstantConditions", "KDocUnresolvedReference")

package com.dream.yukireflection.finder.tools

import com.dream.yukireflection.KYukiReflection
import com.dream.yukireflection.bean.KGenericClass
import com.dream.yukireflection.factory.*
import com.dream.yukireflection.finder.base.data.KBaseRulesData
import com.dream.yukireflection.finder.classes.data.KClassRulesData
import com.dream.yukireflection.finder.callable.data.KCallableRulesData
import com.dream.yukireflection.finder.callable.data.KConstructorRulesData
import com.dream.yukireflection.finder.callable.data.KFunctionRulesData
import com.dream.yukireflection.finder.callable.data.KPropertyRulesData
import com.dream.yukireflection.type.defined.UndefinedKotlin
import com.dream.yukireflection.type.defined.VagueKotlin
import com.dream.yukireflection.factory.kotlin
import com.dream.yukireflection.finder.base.KBaseFinder
import com.highcapable.yukireflection.finder.tools.ReflectionTool
import com.highcapable.yukireflection.log.YLog
import com.highcapable.yukireflection.type.defined.UndefinedType
import com.highcapable.yukireflection.type.java.NoClassDefFoundErrorClass
import com.highcapable.yukireflection.type.java.NoSuchFieldErrorClass
import com.highcapable.yukireflection.type.java.NoSuchMethodErrorClass
import com.highcapable.yukireflection.utils.factory.conditions
import com.highcapable.yukireflection.utils.factory.findLastIndex
import com.highcapable.yukireflection.utils.factory.lastIndex
import com.highcapable.yukireflection.utils.factory.let
import com.highcapable.yukireflection.utils.factory.runOrFalse
import com.highcapable.yukireflection.utils.factory.takeIf
import org.jetbrains.kotlin.builtins.jvm.JavaToKotlinClassMap
import org.jetbrains.kotlin.name.FqNameUnsafe
import kotlin.math.abs
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.*

/**
 * 这是一个对 [KClass]、[KCallable] 查找的工具实现类
 */
object KReflectionTool {

    /** 当前工具类的标签 */
    private const val TAG = "${KYukiReflection.TAG}#KReflectionTool"

    /**
     * 当前工具类的 [ClassLoader]
     * @return [ClassLoader]
     */
    private val currentClassLoader get() = kclassOf<KYukiReflection>().classLoader

    /**
     * 内存缓存实例实现
     */
    private object MemoryCache {

        /** 缓存的 [KClass] 对象数组 */
        val classData = mutableMapOf<String, KClass<*>?>()
    }

    /**
     * 使用字符串类名查找 [KClass] 是否存在
     * @param name [KClass] 完整名称
     * @param loader [KClass] 所在的 [ClassLoader]
     * @return [Boolean]
     */
    fun hasClassByName(name: String, loader: ClassLoader?) = runCatching { findClassByName(name, loader); true }.getOrNull() ?: false

    /**
     * 使用字符串类名获取 [KClass]
     * @param name [KClass] 完整名称
     * @param loader [KClass] 所在的 [ClassLoader]
     * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
     * @return [KClass]
     * @throws NoClassDefFoundError 如果找不到 [KClass] 或设置了错误的 [ClassLoader]
     */
    fun findClassByName(name: String, loader: ClassLoader?, initialize: Boolean = false): KClass<*> {
        val jvmName = JavaToKotlinClassMap.mapKotlinToJava(FqNameUnsafe(name))?.asSingleFqName()?.asString() ?: name
        val uniqueCode = "[$name][$loader]"

        return MemoryCache.classData[uniqueCode] ?: runCatching {
            ReflectionTool.findClassByName(jvmName, loader, initialize).kotlin.also { MemoryCache.classData[uniqueCode] = it }
        }.getOrNull() ?: throw createException(loader ?: currentClassLoader, name = KBaseFinder.TAG_CLASS, "name:[$jvmName]")
    }

    /**
     * 查找任意 [KClass] 或一组 [KClass]
     * @param loaderSet 类所在 [ClassLoader]
     * @param rulesData 规则查找数据
     * @return [MutableList]<[KClass]>
     * @throws IllegalStateException 如果 [classSet] 为 null 或未设置任何条件
     * @throws NoClassDefFoundError 如果找不到 [KClass]
     */
    internal fun findClasses(classSet: Collection<KClass<*>>?, rulesData: KClassRulesData) = rulesData.createResult {
        mutableListOf<KClass<*>>().also { classes ->
            if (!rulesData.isInitialize) {
                classes += classSet?.first() ?: throw createException(classSet, objectName, *templates)
                return@createResult classes
            }
            /**
             * 开始查找作业
             * @param instance 当前 [KClass] 实例
             */
            fun startProcess(instance: KClass<*>) {
                conditions {
                    fromPackages.takeIf { it.isNotEmpty() }?.also { and(true) }
                    fullName?.also { it.equals(instance, it.TYPE_NAME).also { e -> if (it.isOptional) opt(e) else and(e) } }
                    simpleName?.also { it.equals(instance, it.TYPE_SIMPLE_NAME).also { e -> if (it.isOptional) opt(e) else and(e) } }
                    singleName?.also { it.equals(instance, it.TYPE_SINGLE_NAME).also { e -> if (it.isOptional) opt(e) else and(e) } }
                    fullNameConditions?.also { (instance.name).also { n -> runCatching { and(it(n.cast(), n)) } } }
                    simpleNameConditions?.also { instance.simpleNameOrJvm.also { n -> runCatching { and(it(n.cast(), n)) } } }
                    singleNameConditions?.also { classSingleName(instance).also { n -> runCatching { and(it(n.cast(), n)) } } }
                    modifiers?.also { runCatching { and(it(instance.cast())) } }
                    annotationClass.takeIf { it.isNotEmpty() }
                        ?.also { and(instance.annotations.isNotEmpty() && instance.annotations.any { e -> it.contains(e.annotationClass.qualifiedName) }) }
                    extendsClass.takeIf { it.isNotEmpty() }?.also { and(instance.hasExtends && it.contains(instance.superclass?.name)) }
                    implementsClass.takeIf { it.isNotEmpty() }
                        ?.also { and(instance.interfaces.isNotEmpty() && instance.interfaces.any { e -> it.contains(e.name) }) }
                    enclosingClass.takeIf { it.isNotEmpty() }
                        ?.also { and(instance.enclosingClass != null && it.contains(instance.enclosingClass!!.name)) }
                    isAnonymousClass?.also { and(instance.isAnonymous && it) }
                    isNoExtendsClass?.also { and(instance.hasExtends.not() && it) }
                    isNoImplementsClass?.also { and(instance.interfaces.isEmpty() && it) }
                    /**
                     * 匹配 [KCallableRulesData]
                     * @param size [KCallable] 个数
                     * @param result 回调是否匹配
                     */
                    fun KCallableRulesData.matchCount(size: Int, result: (Boolean) -> Unit) {
                        takeIf { it.isInitializeOfMatch }?.also { rule ->
                            rule.conditions {
                                value.matchCount.takeIf { it >= 0 }?.also { and(it == size) }
                                value.matchCountRange.takeIf { it.isEmpty().not() }?.also { and(size in it) }
                                value.matchCountConditions?.also { runCatching { and(it(size.cast(), size)) } }
                            }.finally { result(true) }.without { result(false) }
                        } ?: result(true)
                    }

                    /**
                     * 检查类型中的 [KClass] 是否存在 - 即不存在 [UndefinedKotlin]
                     * @param type 类型
                     * @return [Boolean]
                     */
                    fun KCallableRulesData.exists(vararg type: Any?): Boolean {
                        if (type.isEmpty()) return true
                        for (i in type.indices) if (type[i] == UndefinedKotlin) {
                            YLog.warn(msg = "$objectName type[$i] mistake, it will be ignored in current conditions")
                            return false
                        }
                        return true
                    }
                    callableRules.takeIf { it.isNotEmpty() }?.forEach { rule ->
                        instance.existCallables?.apply {
                            var numberOfFound = 0
                            if (rule.isInitializeOfSuper) forEach { callable ->
                                rule.conditions {
                                    value.modifiers?.also { runCatching { and(it(callable.cast())) } }
                                }.finally { numberOfFound++ }
                            }.run { rule.matchCount(numberOfFound) { and(it && numberOfFound > 0) } }
                            else rule.matchCount(count()) { and(it) }
                        }
                    }
                    propertyRules.takeIf { it.isNotEmpty() }?.forEach { rule ->
                        instance.existPropertys?.apply {
                            var numberOfFound = 0
                            if (rule.isInitialize) forEach { property ->
                                rule.conditions {
                                    value.type?.takeIf { value.exists(it) }?.also { and(it == property.kotlin) }
                                    value.name.takeIf { it.isNotBlank() }?.also { and(it == property.name) }
                                    value.modifiers?.also { runCatching { and(it(property.cast())) } }
                                    value.nameConditions?.also { property.name.also { n -> runCatching { and(it(n.cast(), n)) } } }
                                    value.typeConditions?.also { property.also { t -> runCatching { and(it(t.type(), t.returnType)) } } }
                                }.finally { numberOfFound++ }
                            }.run { rule.matchCount(numberOfFound) { and(it && numberOfFound > 0) } }
                            else rule.matchCount(count()) { and(it) }
                        }
                    }
                    functionRules.takeIf { it.isNotEmpty() }?.forEach { rule ->
                        instance.existFunctions?.apply {
                            var numberOfFound = 0
                            if (rule.isInitialize) forEach { function ->
                                rule.conditions {
                                    value.name.takeIf { it.isNotBlank() }?.also { and(it == function.name) }
                                    value.returnType?.takeIf { value.exists(it) }?.also { and(typeEq(it,function.returnType)) }
                                    value.returnTypeConditions
                                        ?.also { function.also { r -> runCatching { and(it(r.returnType(), r.returnType)) } } }
                                    value.paramCount.takeIf { it >= 0 }?.also { and(function.valueParameters.size == it) }
                                    value.paramCountRange.takeIf { it.isEmpty().not() }?.also { and(function.valueParameters.size in it) }
                                    value.paramCountConditions
                                        ?.also { function.valueParameters.size.also { s -> runCatching { and(it(s.cast(), s)) } } }
                                    value.paramTypes?.takeIf { value.exists(*it) }?.also { and(paramTypesEq(it, function.valueParameters.toTypedArray())) }
                                    value.paramTypesConditions
                                        ?.also { function.also { t -> runCatching { and(it(t.paramTypes(), t.valueParameters)) } } }
                                    value.modifiers?.also { runCatching { and(it(function.cast())) } }
                                    value.nameConditions?.also { function.name.also { n -> runCatching { and(it(n.cast(), n)) } } }
                                }.finally { numberOfFound++ }
                            }.run { rule.matchCount(numberOfFound) { and(it && numberOfFound > 0) } }
                            else rule.matchCount(count()) { and(it) }
                        }
                    }
                    constroctorRules.takeIf { it.isNotEmpty() }?.forEach { rule ->
                        instance.existConstructors?.apply {
                            var numberOfFound = 0
                            if (rule.isInitialize) forEach { constructor ->
                                rule.conditions {
                                    value.paramCount.takeIf { it >= 0 }?.also { and(constructor.valueParameters.size == it) }
                                    value.paramCountRange.takeIf { it.isEmpty().not() }?.also { and(constructor.valueParameters.size in it) }
                                    value.paramCountConditions
                                        ?.also { constructor.valueParameters.size.also { s -> runCatching { and(it(s.cast(), s)) } } }
                                    value.paramTypes?.takeIf { value.exists(*it) }?.also { and(
                                        paramTypesEq(
                                            it,
                                            constructor.valueParameters.toTypedArray()
                                        )
                                    ) }
                                    value.paramTypesConditions
                                        ?.also { constructor.also { t -> runCatching { and(it(t.paramTypes(), t.valueParameters)) } } }
                                    value.modifiers?.also { runCatching { and(it(constructor.cast())) } }
                                }.finally { numberOfFound++ }
                            }.run { rule.matchCount(numberOfFound) { and(it && numberOfFound > 0) } }
                            else rule.matchCount(count()) { and(it) }
                        }
                    }
                }.finally { classes.add(instance) }
            }
            classSet?.forEach {
                startProcess(it)
            }
        }.takeIf { it.isNotEmpty() } ?: throw createException(classSet, objectName, *templates)
    }

    /**
     * 查找任意 [KProperty] 或一组 [KProperty]
     * @param classSet [KProperty] 所在类
     * @param rulesData 规则查找数据
     * @return [MutableList]<[KProperty]>
     * @throws IllegalStateException 如果未设置任何条件或 [KPropertyRulesData.type] 目标类不存在
     * @throws NoSuchFieldError 如果找不到 [KProperty]
     */
    internal fun findPropertys(classSet: KClass<*>?, rulesData: KPropertyRulesData) = rulesData.createResult { hasCondition ->
        if (type == UndefinedKotlin) error("Property match type class is not found")
        if (classSet == null) return@createResult mutableListOf()
        if (hasCondition.not()) return@createResult classSet.existPropertys?.toList()?.toAccessibleKCallables() ?: mutableListOf()
        mutableListOf<KProperty<*>>().also { property ->
            classSet.existPropertys?.also { declares ->
                var iType = -1
                var iName = -1
                var iModify = -1
                var iNameCds = -1
                var iTypeCds = -1
                val iLType = type?.let(matchIndex) { e -> declares.findLastIndex { typeEq(e,it.returnType) } } ?: -1
                val iLName = name.takeIf(matchIndex) { it.isNotBlank() }?.let { e -> declares.findLastIndex { e == it.name } } ?: -1
                val iLModify = modifiers?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.cast()) } } } ?: -1
                val iLNameCds = nameConditions
                    ?.let(matchIndex) { e -> declares.findLastIndex { it.name.let { n -> runOrFalse { e(n.cast(), n) } } } } ?: -1
                val iLTypeCds = typeConditions?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.type(), it.returnType) } } } ?: -1
                declares.forEachIndexed { index, instance ->
                    conditions {
                        type?.also {
                            and((typeEq(it,instance.returnType)).let { hold ->
                                if (hold) iType++
                                hold && matchIndex.compare(iType, iLType)
                            })
                        }
                        name.takeIf { it.isNotBlank() }?.also {
                            and((it == instance.name).let { hold ->
                                if (hold) iName++
                                hold && matchIndex.compare(iName, iLName)
                            })
                        }
                        modifiers?.also {
                            and(runOrFalse { it(instance.cast()) }.let { hold ->
                                if (hold) iModify++
                                hold && matchIndex.compare(iModify, iLModify)
                            })
                        }
                        nameConditions?.also {
                            and(instance.name.let { n -> runOrFalse { it(n.cast(), n) } }.let { hold ->
                                if (hold) iNameCds++
                                hold && matchIndex.compare(iNameCds, iLNameCds)
                            })
                        }
                        typeConditions?.also {
                            and(instance.let { t -> runOrFalse { it(t.type(), t.returnType) } }.let { hold ->
                                if (hold) iTypeCds++
                                hold && matchIndex.compare(iTypeCds, iLTypeCds)
                            })
                        }
                        orderIndex.compare(index, declares.lastIndex()) { and(it) }
                    }.finally { property.add(instance) }
                }
            }
        }.takeIf { it.isNotEmpty() }?.toAccessibleKCallables() ?: findSuperOrThrow(classSet)
    }

    /**
     * 查找任意 [Function] 或一组 [Function]
     * @param classSet [Function] 所在类
     * @param rulesData 规则查找数据
     * @return [MutableList]<[Function]>
     * @throws IllegalStateException 如果未设置任何条件或 [KFunctionRulesData.paramTypes] 以及 [KFunctionRulesData.returnType] 目标类不存在
     * @throws NoSuchMethodError 如果找不到 [Function]
     */
    internal fun findFunctions(classSet: KClass<*>?, rulesData: KFunctionRulesData) = rulesData.createResult { hasCondition ->
        if (returnType == UndefinedKotlin) error("Function match returnType class is not found")
        if (classSet == null) return@createResult mutableListOf()
        if (hasCondition.not()) return@createResult classSet.existFunctions?.toList()?.toAccessibleKCallables() ?: mutableListOf()
        paramTypes?.takeIf { it.isNotEmpty() }
            ?.forEachIndexed { p, it -> if (it == UndefinedType) error("Function match paramType[$p] class is not found") }
        mutableListOf<KFunction<*>>().also { functions ->
            classSet.existFunctions?.also { declares ->
                var iReturnType = -1
                var iReturnTypeCds = -1
                var iParamTypes = -1
                var iParamTypesCds = -1
                var iParamCount = -1
                var iParamCountRange = -1
                var iParamCountCds = -1
                var iName = -1
                var iModify = -1
                var iNameCds = -1
                val iLReturnType = returnType?.let(matchIndex) { e -> declares.findLastIndex { typeEq(e,it.returnType) } } ?: -1
                val iLReturnTypeCds = returnTypeConditions
                    ?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.returnType(), it.returnType) } } } ?: -1
                val iLParamCount = paramCount.takeIf(matchIndex) { it >= 0 }
                    ?.let { e -> declares.findLastIndex { e == it.valueParameters.size } } ?: -1
                val iLParamCountRange = paramCountRange.takeIf(matchIndex) { it.isEmpty().not() }
                    ?.let { e -> declares.findLastIndex { it.valueParameters.size in e } } ?: -1
                val iLParamCountCds = paramCountConditions?.let(matchIndex) { e ->
                    declares.findLastIndex { it.valueParameters.size.let { s -> runOrFalse { e(s.cast(), s) } } }
                } ?: -1
                val iLParamTypes = paramTypes?.let(matchIndex) { e -> declares.findLastIndex { paramTypesEq(e, it.valueParameters.toTypedArray()) } } ?: -1
                val iLParamTypesCds = paramTypesConditions
                    ?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.paramTypes(), it.valueParameters) } } } ?: -1
                val iLName = name.takeIf(matchIndex) { it.isNotBlank() }?.let { e -> declares.findLastIndex { e == it.name } } ?: -1
                val iLModify = modifiers?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.cast()) } } } ?: -1
                val iLNameCds = nameConditions
                    ?.let(matchIndex) { e -> declares.findLastIndex { it.name.let { n -> runOrFalse { e(n.cast(), n) } } } } ?: -1
                declares.forEachIndexed { index, instance ->
                    conditions {
                        name.takeIf { it.isNotBlank() }?.also {
                            and((it == instance.name).let { hold ->
                                if (hold) iName++
                                hold && matchIndex.compare(iName, iLName)
                            })
                        }
                        returnType?.also {
                            and((typeEq(it,instance.returnType)).let { hold ->
                                if (hold) iReturnType++
                                hold && matchIndex.compare(iReturnType, iLReturnType)
                            })
                        }
                        returnTypeConditions?.also {
                            and(instance.let { r -> runOrFalse { it(r.returnType(), r.returnType) } }.let { hold ->
                                if (hold) iReturnTypeCds++
                                hold && matchIndex.compare(iReturnTypeCds, iLReturnTypeCds)
                            })
                        }
                        paramCount.takeIf { it >= 0 }?.also {
                            and((instance.valueParameters.size == it).let { hold ->
                                if (hold) iParamCount++
                                hold && matchIndex.compare(iParamCount, iLParamCount)
                            })
                        }
                        paramCountRange.takeIf { it.isEmpty().not() }?.also {
                            and((instance.valueParameters.size in it).let { hold ->
                                if (hold) iParamCountRange++
                                hold && matchIndex.compare(iParamCountRange, iLParamCountRange)
                            })
                        }
                        paramCountConditions?.also {
                            and(instance.valueParameters.size.let { s -> runOrFalse { it(s.cast(), s) } }.let { hold ->
                                if (hold) iParamCountCds++
                                hold && matchIndex.compare(iParamCountCds, iLParamCountCds)
                            })
                        }
                        paramTypes?.also {
                            and(paramTypesEq(it, instance.valueParameters.toTypedArray()).let { hold ->
                                if (hold) iParamTypes++
                                hold && matchIndex.compare(iParamTypes, iLParamTypes)
                            })
                        }
                        paramTypesConditions?.also {
                            and(instance.let { t -> runOrFalse { it(t.paramTypes(), t.valueParameters) } }.let { hold ->
                                if (hold) iParamTypesCds++
                                hold && matchIndex.compare(iParamTypesCds, iLParamTypesCds)
                            })
                        }
                        modifiers?.also {
                            and(runOrFalse { it(instance.cast()) }.let { hold ->
                                if (hold) iModify++
                                hold && matchIndex.compare(iModify, iLModify)
                            })
                        }
                        nameConditions?.also {
                            and(instance.name.let { n -> runOrFalse { it(n.cast(), n) } }.let { hold ->
                                if (hold) iNameCds++
                                hold && matchIndex.compare(iNameCds, iLNameCds)
                            })
                        }
                        orderIndex.compare(index, declares.lastIndex()) { and(it) }
                    }.finally { functions.add(instance) }
                }
            }
        }.takeIf { it.isNotEmpty() }?.toAccessibleKCallables() ?: findSuperOrThrow(classSet)
    }

    /**
     * 查找任意 Constructor [KFunction] 或一组 Constructor [KFunction]
     * @param classSet Constructor [KFunction] 所在类
     * @param rulesData 规则查找数据
     * @return [MutableList]<Constructor [KFunction]>
     * @throws IllegalStateException 如果未设置任何条件或 [KConstructorRulesData.paramTypes] 目标类不存在
     * @throws NoSuchMethodError 如果找不到 Constructor [KFunction]
     */
    internal fun findConstructors(classSet: KClass<*>?, rulesData: KConstructorRulesData) = rulesData.createResult { hasCondition ->
        if (classSet == null) return@createResult mutableListOf()
        if (hasCondition.not()) return@createResult classSet.existConstructors?.toList()?.toAccessibleKCallables() ?: mutableListOf()
        paramTypes?.takeIf { it.isNotEmpty() }
            ?.forEachIndexed { p, it -> if (it == UndefinedKotlin) error("Constructor match paramType[$p] class is not found") }
        mutableListOf<KFunction<*>>().also { constructors ->
            classSet.existConstructors?.also { declares ->
                var iParamTypes = -1
                var iParamTypesCds = -1
                var iParamCount = -1
                var iParamCountRange = -1
                var iParamCountCds = -1
                var iModify = -1
                val iLParamCount = paramCount.takeIf(matchIndex) { it >= 0 }
                    ?.let { e -> declares.findLastIndex { e == it.valueParameters.size } } ?: -1
                val iLParamCountRange = paramCountRange.takeIf(matchIndex) { it.isEmpty().not() }
                    ?.let { e -> declares.findLastIndex { it.valueParameters.size in e } } ?: -1
                val iLParamCountCds = paramCountConditions?.let(matchIndex) { e ->
                    declares.findLastIndex { it.valueParameters.size.let { s -> runOrFalse { e(s.cast(), s) } } }
                } ?: -1
                val iLParamTypes = paramTypes?.let(matchIndex) { e -> declares.findLastIndex { paramTypesEq(e, it.valueParameters.toTypedArray()) } } ?: -1
                val iLParamTypesCds = paramTypesConditions
                    ?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.paramTypes(), it.valueParameters) } } } ?: -1
                val iLModify = modifiers?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.cast()) } } } ?: -1
                declares.forEachIndexed { index, instance ->
                    conditions {
                        paramCount.takeIf { it >= 0 }?.also {
                            and((instance.valueParameters.size == it).let { hold ->
                                if (hold) iParamCount++
                                hold && matchIndex.compare(iParamCount, iLParamCount)
                            })
                        }
                        paramCountRange.takeIf { it.isEmpty().not() }?.also {
                            and((instance.valueParameters.size in it).let { hold ->
                                if (hold) iParamCountRange++
                                hold && matchIndex.compare(iParamCountRange, iLParamCountRange)
                            })
                        }
                        paramCountConditions?.also {
                            and(instance.valueParameters.size.let { s -> runOrFalse { it(s.cast(), s) } }.let { hold ->
                                if (hold) iParamCountCds++
                                hold && matchIndex.compare(iParamCountCds, iLParamCountCds)
                            })
                        }
                        paramTypes?.also {
                            and(paramTypesEq(it, instance.valueParameters.toTypedArray()).let { hold ->
                                if (hold) iParamTypes++
                                hold && matchIndex.compare(iParamTypes, iLParamTypes)
                            })
                        }
                        paramTypesConditions?.also {
                            and(instance.let { t -> runOrFalse { it(t.paramTypes(), t.valueParameters) } }.let { hold ->
                                if (hold) iParamTypesCds++
                                hold && matchIndex.compare(iParamTypesCds, iLParamTypesCds)
                            })
                        }
                        modifiers?.also {
                            and(runOrFalse { it(instance.cast()) }.let { hold ->
                                if (hold) iModify++
                                hold && matchIndex.compare(iModify, iLModify)
                            })
                        }
                        orderIndex.compare(index, declares.lastIndex()) { and(it) }
                    }.finally { constructors.add(instance) }
                }
            }
        }.takeIf { it.isNotEmpty() }?.toAccessibleKCallables() ?: findSuperOrThrow(classSet)
    }

    /**
     * 创建一个异常
     * @param instanceSet 所在 [ClassLoader] or [KClass]
     * @param name 实例名称
     * @param content 异常内容
     * @return [Throwable]
     */
    private fun createException(instanceSet: Any?, name: String, vararg content: String): Throwable {
        /**
         * 获取 [Class.getName] 长度的空格数量并使用 "->" 拼接
         * @return [String]
         */
        fun KClass<*>.space(): String {
            var space = ""
            for (i in 0..jvmName.length) space += " "
            return "$space -> "
        }
        if (content.isEmpty()) return IllegalStateException("Exception content is null")
        val space = when (name) {
            KBaseFinder.TAG_CLASS -> NoClassDefFoundErrorClass.kotlin.space()
            KBaseFinder.TAG_PROPERTY -> NoSuchFieldErrorClass.kotlin.space()
            KBaseFinder.TAG_FUNCTION, KBaseFinder.TAG_CONSTRUCTOR -> NoSuchMethodErrorClass.kotlin.space()
            else -> error("Invalid Exception type")
        }
        var splicing = ""
        content.forEach { if (it.isNotBlank()) splicing += "$space$it\n" }
        val template = "Can't find this $name in [$instanceSet]:\n${splicing}Generated by $TAG"
        return when (name) {
            KBaseFinder.TAG_CLASS -> NoClassDefFoundError(template)
            KBaseFinder.TAG_PROPERTY -> NoSuchFieldError(template)
            KBaseFinder.TAG_FUNCTION, KBaseFinder.TAG_CONSTRUCTOR -> NoSuchMethodError(template)
            else -> error("Invalid Exception type")
        }
    }

    /**
     * 获取当前 [KClass] 中存在的 [KCallable] 数组
     * @return [Sequence]<[KCallable]> or null
     */
    private val KClass<*>.existCallables
        get() = runCatching {
            sequence {
                if (KYukiReflection.Configs.isUseJvmObtainCallables) {
                    yieldAll(((if (existTop) top.kotlin.java.declaredFields else arrayOf()) + java.declaredFields).asSequence().mapNotNull { it.kotlin })
                    yieldAll(((if (existTop) top.kotlin.java.declaredMethods else arrayOf()) + java.declaredMethods).asSequence().mapNotNull { it.kotlin })
                    yieldAll(java.declaredConstructors.asSequence().mapNotNull { it.kotlin })
                } else {
                    yieldAll(((if (existTop) top.declaredTopPropertys else arrayListOf()) + declaredPropertys).asSequence())
                    yieldAll(((if (existTop) top.declaredTopFunctions else arrayListOf()) + declaredFunctions).toList())
                    yieldAll(constructors.toList())
                }
            }
        }.onFailure {
            YLog.warn(msg = "Failed to get the declared KCallables in [$this] because got an exception", e = it)
        }.getOrNull()

    /**
     * 获取当前 [KClass] 中存在的 [KProperty] 数组
     * @return [Sequence]<[KProperty]> or null
     */
    private val KClass<*>.existPropertys
        get() = runCatching {
            if (KYukiReflection.Configs.isUseJvmObtainCallables)
                ((if (existTop) top.kotlin.java.declaredFields else arrayOf()) + java.declaredFields).asSequence().mapNotNull { it.kotlin }
            else ((if (existTop) top.declaredTopPropertys else arrayListOf()) + declaredPropertys).asSequence()
        }.onFailure {
            YLog.warn(msg = "Failed to get the declared Propertys in [$this] because got an exception", e = it)
        }.getOrNull()

    /**
     * 获取当前 [KClass] 中存在的 [KFunction] 数组
     * @return [Sequence]<[KFunction]> or null
     */
    private val KClass<*>.existFunctions
        get() = runCatching { if (KYukiReflection.Configs.isUseJvmObtainCallables)
            ((if (existTop) top.kotlin.java.declaredMethods else arrayOf()) + java.declaredMethods).asSequence().mapNotNull { it.kotlin } else
                ((if (existTop) top.declaredTopFunctions else arrayListOf()) + declaredFunctions).asSequence() }.onFailure {
            YLog.warn(msg = "Failed to get the declared Functions in [$this] because got an exception", e = it)
        }.getOrNull()

    /**
     * 获取当前 [KClass] 中存在的 Constructor [KFunction] 数组
     * @return [Sequence]<Constructor [KFunction]> or null
     */
    private val KClass<*>.existConstructors
        get() = runCatching { if (KYukiReflection.Configs.isUseJvmObtainCallables) java.declaredConstructors.asSequence().mapNotNull { it.kotlin } else constructors.asSequence() }.onFailure {
            YLog.warn(msg = "Failed to get the declared Constructors in [$this] because got an exception", e = it)
        }.getOrNull()

    /**
     * 批量允许访问内部方法
     * @return [MutableList]<[T]>
     */
    private inline fun <reified T : KCallable<*>> List<T>.toAccessibleKCallables() =
        mutableListOf<T>().also { list ->
            forEach { callable ->
                runCatching {
                    list.add(callable)
                    callable.isAccessible = true
                }.onFailure { YLog.warn(msg = "Failed to access [$callable] because got an exception", e = it) }
            }
        }

    /**
     * 判断两个类型是否在一定参数上相等
     *
     *    如: compare:KVariance.INVARIANT -> original = KType(variance:KVariance.INVARIANT)  true
     *       compare:KClass<Int> -> original = KType(kotlin:KClass<Int>)  true
     *
     * 类型支持 [KClassifier]/[KClass]/[KTypeParameter] or [KTypeProjection]/array([KTypeProjection]) or [KVariance]/array([KVariance]) or [KType] or [KParameter] or [KParameter.Kind] or [KGenericClass]
     *
     * @param compare 类型
     * @param original 类型
     * @return [Boolean] 是否相等
     */
    private fun typeEq(compare: Any?, original: Any?): Boolean {
        if (compare == original || compare == VagueKotlin || original == VagueKotlin)return true
        if (compare == null || original == null) return false
        return when(compare){
            is KClassifier -> when(original){
                is KType -> compare == original.classifier
                is KParameter -> compare == original.type.classifier
                else -> false
            }
            is KTypeProjection -> when(original){
                is KVariance -> compare.variance == original
                is KType -> compare.variance == original.arguments.first().variance && compare.type?.generic() == original.arguments.first().type?.generic()
                is KParameter -> compare.variance == original.type.arguments.first().variance && compare.type?.generic() == original.type.arguments.first().type?.generic()
                else -> false
            }
            is KVariance -> when(original){
                is KTypeProjection -> compare == original.variance
                is KType -> compare == original.arguments.first().variance
                is KParameter -> compare == original.type.arguments.first().variance
                else -> false
            }
            is KType -> when(original){
                is KType -> original.generic() == compare.generic()
                is KClassifier -> compare.generic() == original
                is KParameter -> compare.generic().equals(original.type)
                else -> false
            }
            is KParameter -> when(original){
                is KClassifier -> compare.type.classifier == original
                is KType -> compare.type.generic() == original
                is KParameter.Kind -> compare.kind == original
                else -> false
            }
            is KParameter.Kind -> when(original){
                is KParameter -> compare == original.kind
                else -> false
            }
            is KGenericClass -> when(original){
                is KType -> compare == original.generic()
                is KClassifier -> compare.type.classifier == original
                is KParameter -> compare == original
                else -> false
            }
            else -> {
                var eq = true
                val result = runCatching {
                    KBaseFinder.checkArrayGenerics(compare)
                }.getOrNull() ?: return false
                when(original){
                    is KType -> {
                        if (original.arguments.size != result.size)return false
                        result.forEachIndexed { index, t ->
                            when(t){
                                is KTypeProjection -> {
                                    if (!eq) return false
                                    eq = if (t.type?.classifier == VagueKotlin && t.variance == original.arguments[index].variance) true
                                    else (t.variance == original.arguments[index].variance) && (t.type?.generic() == original.arguments[index].type?.generic())
                                }
                                is KVariance -> {
                                    if (!eq) return false
                                    eq = t == original.arguments.first().variance
                                }
                            }
                        }
                    }
                    is KParameter -> {
                        if (original.type.arguments.size != result.size)return false
                        result.forEachIndexed { index, t ->
                            when(t){
                                is KTypeProjection -> {
                                    if (!eq) return false
                                    eq = if (t.type?.classifier == VagueKotlin && t.variance == original.type.arguments[index].variance) true
                                    else (t.variance == original.type.arguments[index].variance) && (t.type?.generic() == original.type.arguments[index].type?.generic())
                                }
                                is KVariance -> {
                                    if (!eq) return false
                                    eq = t == original.type.arguments.first().variance
                                }
                            }
                        }
                    }
                    else -> return false
                }
                eq
            }
        }
    }

    /**
     * 判断两个方法、构造方法类型数组是否相等
     *
     * 复制自 [KClass] 中的 [Class.arrayContentsEq]
     * @param compare 用于比较的数组
     * @param original 方法、构造方法原始数组
     * @return [Boolean] 是否相等
     * @throws IllegalStateException 如果 [VagueKotlin] 配置不正确
     */
    private fun paramTypesEq(compare: Array<out Any>?, original: Array<out Any>?): Boolean {
        return when {
            (compare == null && original == null) || (compare?.isEmpty() == true && original?.isEmpty() == true) -> true
            (compare == null && original != null) || (compare != null && original == null) || (compare?.size != original?.size) -> false
            else -> {
                if (compare == null || original == null) return false
                if (compare.all { it == VagueKotlin }) error("The number of VagueType must be at least less than the count of paramTypes")
                for (i in compare.indices) return typeEq(compare[i],original[i])
                true
            }
        }
    }

    /**
     * 创建查找结果方法体
     * @param result 回调方法体
     * @return [T]
     * @throws IllegalStateException 如果没有 [KBaseRulesData.isInitialize]
     */
    private inline fun <reified T, R : KBaseRulesData> R.createResult(result: R.(hasCondition: Boolean) -> T) =
        result(when (this) {
            is KPropertyRulesData -> isInitialize
            is KFunctionRulesData -> isInitialize
            is KConstructorRulesData -> isInitialize
            else -> false
        })

    /**
     * 比较位置下标的前后顺序
     * @param need 当前位置
     * @param last 最后位置
     * @return [Boolean] 返回是否成立
     */
    private fun Pair<Int, Boolean>?.compare(need: Int, last: Int) = this == null || ((first >= 0 && first == need && second) ||
      (first < 0 && abs(first) == (last - need) && second) || (last == need && second.not()))

    /**
     * 比较位置下标的前后顺序
     * @param need 当前位置
     * @param last 最后位置
     * @param result 回调是否成立
     */
    private fun Pair<Int, Boolean>?.compare(need: Int, last: Int, result: (Boolean) -> Unit) {
        if (this == null) return
        ((first >= 0 && first == need && second) ||
          (first < 0 && abs(first) == (last - need) && second) ||
          (last == need && second.not())).also(result)
    }

    /**
     * 在 [KClass.getSuperclass] 中查找或抛出异常
     * @param classSet 所在类
     * @return [T]
     * @throws NoSuchFieldError 继承于方法 [throwNotFoundError] 的异常
     * @throws NoSuchMethodError 继承于方法 [throwNotFoundError] 的异常
     * @throws IllegalStateException 如果 [R] 的类型错误
     */
    private inline fun <reified T, R : KCallableRulesData> R.findSuperOrThrow(classSet: KClass<*>): T = when (this) {
        is KPropertyRulesData ->
            if (isFindInSuper && classSet.hasExtends)
                findPropertys(classSet.superclass, rulesData = this) as T
            else throwNotFoundError(classSet)
        is KFunctionRulesData ->
            if (isFindInSuper && classSet.hasExtends)
                findFunctions(classSet.superclass, rulesData = this) as T
            else throwNotFoundError(classSet)
        is KConstructorRulesData ->
            if (isFindInSuper && classSet.hasExtends)
                findConstructors(classSet.superclass, rulesData = this) as T
            else throwNotFoundError(classSet)
        else -> error("Type [$this] not allowed")
    }

    /**
     * 抛出找不到 [KClass]、[KCallable] 的异常
     * @param instanceSet 所在 [ClassLoader] or [KClass]
     * @throws NoClassDefFoundError 如果找不到 [KClass]
     * @throws NoSuchFieldError 如果找不到 [KProperty]
     * @throws NoSuchMethodError 如果找不到 [KFunction] or Constructor [KFunction]
     * @throws IllegalStateException 如果 [KBaseRulesData] 的类型错误
     */
    private fun KCallableRulesData.throwNotFoundError(instanceSet: Any?): Nothing = when (this) {
        is KPropertyRulesData -> throw createException(instanceSet, objectName, *templates)
        is KFunctionRulesData -> throw createException(instanceSet, objectName, *templates)
        is KConstructorRulesData -> throw createException(instanceSet, objectName, *templates)
        else -> error("Type [$this] not allowed")
    }
}