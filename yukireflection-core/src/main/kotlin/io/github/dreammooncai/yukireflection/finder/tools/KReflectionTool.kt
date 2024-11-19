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
@file:Suppress("KotlinConstantConditions", "KDocUnresolvedReference", "MISSING_DEPENDENCY_SUPERCLASS")

package io.github.dreammooncai.yukireflection.finder.tools

import io.github.dreammooncai.yukireflection.KYukiReflection
import io.github.dreammooncai.yukireflection.bean.KGenericClass
import io.github.dreammooncai.yukireflection.factory.*
import io.github.dreammooncai.yukireflection.finder.base.data.KBaseRulesData
import io.github.dreammooncai.yukireflection.finder.classes.data.KClassRulesData
import io.github.dreammooncai.yukireflection.finder.callable.data.KCallableRulesData
import io.github.dreammooncai.yukireflection.finder.callable.data.KConstructorRulesData
import io.github.dreammooncai.yukireflection.finder.callable.data.KFunctionRulesData
import io.github.dreammooncai.yukireflection.finder.callable.data.KPropertyRulesData
import io.github.dreammooncai.yukireflection.type.defined.UndefinedKotlin
import io.github.dreammooncai.yukireflection.type.defined.VagueKotlin
import io.github.dreammooncai.yukireflection.factory.returnClass
import io.github.dreammooncai.yukireflection.finder.base.KBaseFinder
import io.github.dreammooncai.yukireflection.finder.base.rules.KModifierRules
import io.github.dreammooncai.yukireflection.finder.base.rules.KObjectRules
import io.github.dreammooncai.yukireflection.finder.signature.support.KFunctionSignatureSupport
import io.github.dreammooncai.yukireflection.finder.signature.support.KPropertySignatureSupport
import io.github.dreammooncai.yukireflection.log.KYLog
import io.github.dreammooncai.yukireflection.type.kotlin.*
import io.github.dreammooncai.yukireflection.utils.DexSignUtil
import io.github.dreammooncai.yukireflection.utils.factory.*
import io.github.dreammooncai.yukireflection.utils.factory.conditions
import io.github.dreammooncai.yukireflection.utils.factory.findLastIndex
import io.github.dreammooncai.yukireflection.utils.factory.let
import io.github.dreammooncai.yukireflection.utils.factory.takeIf
import kotlin.math.abs
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.*
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf
import kotlin.reflect.jvm.internal.impl.metadata.jvm.JvmProtoBuf
import kotlin.reflect.jvm.internal.impl.name.FqNameUnsafe

/**
 * 这是一个对 [KClass]、[KCallable] 查找的工具实现类
 */
internal object KReflectionTool {

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
    internal fun hasClassByName(name: String, loader: ClassLoader?) = runCatching {
        KReflectionTool.findClassByName(
            name,
            loader
        ); true
    }.getOrNull() ?: false

    /**
     * 使用字符串类名获取 [KClass]
     * @param name [KClass] 完整名称
     * @param loader [KClass] 所在的 [ClassLoader]
     * @param initialize 是否初始化 [KClass] 的静态方法块 - 默认否
     * @return [KClass]
     * @throws NoClassDefFoundError 如果找不到 [KClass] 或设置了错误的 [ClassLoader]
     */
    internal fun findClassByName(name: String, loader: ClassLoader?, initialize: Boolean = false): KClass<*> {
        val jvmName = JavaToKotlinClassMap.INSTANCE.mapKotlinToJava(FqNameUnsafe(name))?.asSingleFqName()?.asString() ?: name
        val uniqueCode = "[$jvmName][$loader]"

        /**
         * 获取 [Class.forName] 的 [Class] 对象
         * @param jvmName [Class] 完整名称
         * @param initialize 是否初始化 [Class] 的静态方法块
         * @param loader [Class] 所在的 [ClassLoader] - 默认为 [currentClassLoader]
         * @return [Class]
         */
        fun classForName(jvmName: String, initialize: Boolean, loader: ClassLoader? = KReflectionTool.currentClassLoader) =
            Class.forName(jvmName, initialize, loader)

        /**
         * 使用默认方式和 [ClassLoader] 装载 [Class]
         * @return [Class] or null
         */
        fun loadWithDefaultClassLoader() = if (initialize.not()) loader?.loadClass(jvmName) else classForName(jvmName, initialize, loader)
        return KReflectionTool.MemoryCache.classData[uniqueCode] ?: runCatching {
            if (jvmName.endsWith("[]")) {
                val clazz = KReflectionTool.findClassByName(
                    jvmName.substring(
                        0,
                        jvmName.length - 2
                    ), loader, initialize
                )
                return ArrayClass(clazz).also { KReflectionTool.MemoryCache.classData[uniqueCode] = it }
            }
            if (jvmName.startsWith("[")) {
                return KReflectionTool.findClassByName(
                    DexSignUtil.getTypeName(
                        jvmName
                    ), loader, initialize
                )
            }
            val baseType = when (jvmName) {
                "boolean" -> Int::class.javaPrimitiveType
                "byte" -> Byte::class.javaPrimitiveType
                "char" -> Char::class.javaPrimitiveType
                "short" -> Short::class.javaPrimitiveType
                "int" -> Int::class.javaPrimitiveType
                "long" -> Long::class.javaPrimitiveType
                "float" -> Float::class.javaPrimitiveType
                "double" -> Double::class.javaPrimitiveType
                "void" -> UnitKClass.java
                else -> null
            }
            if (baseType != null) return baseType.kotlin.also { KReflectionTool.MemoryCache.classData[uniqueCode] = it }
            (loadWithDefaultClassLoader() ?: classForName(jvmName, initialize)).kotlin.also { KReflectionTool.MemoryCache.classData[uniqueCode] = it }
        }.getOrNull() ?: throw KReflectionTool.createException(
            loader ?: KReflectionTool.currentClassLoader,
            name = KBaseFinder.TAG_CLASS,
            "name:[$jvmName]"
        )
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
                classes += classSet?.first() ?: throw KReflectionTool.createException(
                    classSet,
                    objectName,
                    *templates
                )
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
                    extendsClass.takeIf { it.isNotEmpty() }?.also { and(instance.hasExtends && it.contains(instance.superclass.name)) }
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
                            KYLog.warn(msg = "$objectName type[$i] mistake, it will be ignored in current conditions")
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
                                    value.type?.takeIf { value.exists(it) }?.also { and(it == property.returnClass) }
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
                        when {
                            rule.name.isGetter -> instance.existGetterFunctions
                            rule.name.isSetter -> instance.existSetterFunctions
                            else -> instance.existFunctions
                        }?.apply {
                            var numberOfFound = 0
                            if (rule.isInitialize) forEach { function ->
                                rule.conditions {
                                    value.name.takeIf { it.isNotBlank() }?.also { and(it == function.name) }
                                    value.returnType?.takeIf { value.exists(it) }?.also {
                                        and(
                                            typeEq(
                                                it,
                                                function.returnType
                                            )
                                        )
                                    }
                                    value.returnTypeConditions
                                        ?.also { function.also { r -> runCatching { and(it(r.returnType(), r.returnType)) } } }
                                    value.paramCount.takeIf { it >= 0 }?.also { and(function.valueParameters.size == it) }
                                    value.paramCountRange.takeIf { it.isEmpty().not() }?.also { and(function.valueParameters.size in it) }
                                    value.paramCountConditions
                                        ?.also { function.valueParameters.size.also { s -> runCatching { and(it(s.cast(), s)) } } }
                                    value.paramTypes?.takeIf { value.exists(*it) }?.also {
                                        and(
                                            paramTypesEq(
                                                it,
                                                function.valueParameters.toTypedArray()
                                            )
                                        )
                                    }
                                    value.paramTypesConditions
                                        ?.also { function.also { t -> runCatching { and(it(t.paramTypes(), t.valueParameters)) } } }
                                    value.paramNames?.takeIf { value.exists(*it) }?.also {
                                        and(
                                            paramNamesEq(
                                                it,
                                                function.valueParameters.map { it.name }.toTypedArray()
                                            )
                                        )
                                    }
                                    value.paramNamesConditions
                                        ?.also { function.also { t -> runCatching { and(it(t.paramNames(), t.valueParameters.map { it.name })) } } }
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
                                    value.paramTypes?.takeIf { value.exists(*it) }?.also {
                                        and(
                                            paramTypesEq(
                                                it,
                                                constructor.valueParameters.toTypedArray()
                                            )
                                        )
                                    }
                                    value.paramTypesConditions
                                        ?.also { constructor.also { t -> runCatching { and(it(t.paramTypes(), t.valueParameters)) } } }
                                    value.paramNames?.takeIf { value.exists(*it) }?.also {
                                        and(
                                            paramNamesEq(
                                                it,
                                                constructor.valueParameters.map { it.name }.toTypedArray()
                                            )
                                        )
                                    }
                                    value.paramNamesConditions
                                        ?.also {
                                            constructor.also { t ->
                                                runCatching {
                                                    and(
                                                        it(
                                                            t.paramNames(),
                                                            t.valueParameters.map { it.name })
                                                    )
                                                }
                                            }
                                        }
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
        }.takeIf { it.isNotEmpty() } ?: throw KReflectionTool.createException(
            classSet,
            objectName,
            *templates
        )
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
                val iLType = type?.let(matchIndex) { e ->
                    declares.findLastIndex {
                        typeEq(
                            e,
                            it.returnType
                        )
                    }
                } ?: -1
                val iLName = name.takeIf(matchIndex) { it.isNotBlank() }?.let { e -> declares.findLastIndex { e == it.name } } ?: -1
                val iLModify = modifiers?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.cast()) } } } ?: -1
                val iLNameCds = nameConditions
                    ?.let(matchIndex) { e -> declares.findLastIndex { it.name.let { n -> runOrFalse { e(n.cast(), n) } } } } ?: -1
                val iLTypeCds = typeConditions?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.type(), it.returnType) } } } ?: -1
                declares.forEachIndexed { index, instance ->
                    conditions {
                        type?.also {
                            and((typeEq(
                                it,
                                instance.returnType
                            )).let { hold ->
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
     * 查找任意 [KPropertySignatureSupport] 或一组 [KPropertySignatureSupport]
     * @param classSet [KPropertySignatureSupport] 所在类
     * @param rulesData 规则查找数据
     * @param loader [ClassLoader] 方法参数 [KPropertySignatureSupport.member] 所在的 [ClassLoader]
     * @return [MutableList]<[KPropertySignatureSupport]>
     * @throws IllegalStateException 如果未设置任何条件或 [KPropertyRulesData.type] 目标类不存在
     * @throws NoSuchFieldError 如果找不到 [KPropertySignatureSupport]
     */
    internal fun findPropertySignatures(classSet: KClass<*>?, rulesData: KPropertyRulesData, loader: ClassLoader? = null) =
        rulesData.createResult { hasCondition ->
            if (type == UndefinedKotlin) error("Property match type class is not found")
            if (classSet == null) return@createResult mutableListOf()
            val nameResolver = classSet.memberScope?.deserializationContext?.nameResolver ?: return@createResult mutableListOf()
            val protos = (classSet.memberScope?.impl ?: return@createResult mutableListOf()).propertyProtos
            val protoBufs by lazy { mutableListOf<ProtoBuf.Property>().also { buf -> protos.values.forEach { buf += it } }.asSequence() }
            if (hasCondition.not()) {
                return@createResult protoBufs.map {
                    KPropertySignatureSupport(
                        classSet,
                        loader,
                        nameResolver,
                        it.getExtension(JvmProtoBuf.propertySignature)
                    )
                }.toMutableList()
            }
            mutableListOf<KPropertySignatureSupport>().also { property ->
                val supportMap: MutableMap<ProtoBuf.Property, KPropertySignatureSupport> = mutableMapOf()
                fun ProtoBuf.Property.support(): KPropertySignatureSupport = supportMap.getOrPut(this) {
                    KPropertySignatureSupport(
                        classSet,
                        loader,
                        nameResolver,
                        this.getExtension(JvmProtoBuf.propertySignature)
                    )
                }

                fun ProtoBuf.Property.name(): String {
                    protos.forEach { (t, u) ->
                        if (u.contains(this)) {
                            return t.asString()
                        }
                    }
                    return nameResolver.getString(name)
                }

                fun ProtoBuf.Property.type() = support().getReturnTypeOrNull(classSet, loader)
                fun ProtoBuf.Property.members() = arrayListOf(
                    support().fieldOrNull?.getMemberOrNull(classSet),
                    support().getterOrNull?.getMemberOrNull(classSet),
                    support().setterOrNull?.getMemberOrNull(classSet)
                ).filterNotNull()
                protoBufs.also { declares ->
                    var iType = -1
                    var iName = -1
                    var iModify = -1
                    var iNameCds = -1
                    var iTypeCds = -1
                    val iLType = type?.let(matchIndex) { e ->
                        declares.findLastIndex {
                            typeEq(
                                e,
                                it.type()
                            )
                        }
                    } ?: -1
                    val iLName = name.takeIf(matchIndex) { it.isNotBlank() }?.let { e -> declares.findLastIndex { e == it.name() } } ?: -1
                    val iLModify = modifiers?.let(matchIndex) { e ->
                        declares.findLastIndex {
                            runOrFalse {
                                val members = it.members()
                                if (members.isEmpty()) return@runOrFalse true
                                members.any { e(KModifierRules.with(it)) }
                            }
                        }
                    } ?: -1
                    val iLNameCds = nameConditions
                        ?.let(matchIndex) { e -> declares.findLastIndex { it.name().let { n -> runOrFalse { e(n.cast(), n) } } } } ?: -1
                    val iLTypeCds = typeConditions?.let(matchIndex) { e ->
                        declares.findLastIndex {
                            runOrFalse {
                                val type = it.type() ?: return@runOrFalse true
                                e(KObjectRules.with(type), type)
                            }
                        }
                    } ?: -1
                    declares.forEachIndexed { index, instance ->
                        conditions {
                            type?.also {
                                and((typeEq(
                                    it,
                                    instance.type()
                                )).let { hold ->
                                    if (hold) iType++
                                    hold && matchIndex.compare(iType, iLType)
                                })
                            }
                            name.takeIf { it.isNotBlank() }?.also {
                                and((it == instance.name()).let { hold ->
                                    if (hold) iName++
                                    hold && matchIndex.compare(iName, iLName)
                                })
                            }
                            modifiers?.also {
                                and(runOrFalse {
                                    val members = instance.members()
                                    if (members.isEmpty()) return@runOrFalse true
                                    members.any { it(KModifierRules.with(instance)) }
                                }.let { hold ->
                                    if (hold) iModify++
                                    hold && matchIndex.compare(iModify, iLModify)
                                })
                            }
                            nameConditions?.also {
                                and(instance.name().let { n -> runOrFalse { it(n.cast(), n) } }.let { hold ->
                                    if (hold) iNameCds++
                                    hold && matchIndex.compare(iNameCds, iLNameCds)
                                })
                            }
                            typeConditions?.also {
                                and(instance.let { t ->
                                    runOrFalse {
                                        val type = t.type() ?: return@runOrFalse true
                                        it(KObjectRules.with(type), type)
                                    }
                                }.let { hold ->
                                    if (hold) iTypeCds++
                                    hold && matchIndex.compare(iTypeCds, iLTypeCds)
                                })
                            }
                            orderIndex.compare(index, declares.lastIndex()) { and(it) }
                        }.finally { property.add(instance.support()) }
                    }
                }
            }.takeIf { it.isNotEmpty() } ?: findSuperOrThrow(classSet)
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
            ?.forEachIndexed { p, it -> if (it == UndefinedKotlin) error("Function match paramType[$p] class is not found") }
        mutableListOf<KFunction<*>>().also { functions ->
            when {
                rulesData.name.isGetter -> classSet.existGetterFunctions
                rulesData.name.isSetter -> classSet.existSetterFunctions
                else -> classSet.existFunctions
            }?.also { declares ->
                var iReturnType = -1
                var iReturnTypeCds = -1
                var iParamTypes = -1
                var iParamTypesCds = -1
                var iParamNames = -1
                var iParamNamesCds = -1
                var iParamCount = -1
                var iParamCountRange = -1
                var iParamCountCds = -1
                var iName = -1
                var iModify = -1
                var iNameCds = -1
                val iLReturnType = returnType?.let(matchIndex) { e ->
                    declares.findLastIndex {
                        typeEq(
                            e,
                            it.returnType
                        )
                    }
                } ?: -1
                val iLReturnTypeCds = returnTypeConditions
                    ?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.returnType(), it.returnType) } } } ?: -1
                val iLParamCount = paramCount.takeIf(matchIndex) { it >= 0 }
                    ?.let { e -> declares.findLastIndex { e == it.valueParameters.size } } ?: -1
                val iLParamCountRange = paramCountRange.takeIf(matchIndex) { it.isEmpty().not() }
                    ?.let { e -> declares.findLastIndex { it.valueParameters.size in e } } ?: -1
                val iLParamCountCds = paramCountConditions?.let(matchIndex) { e ->
                    declares.findLastIndex { it.valueParameters.size.let { s -> runOrFalse { e(s.cast(), s) } } }
                } ?: -1
                val iLParamTypes = paramTypes?.let(matchIndex) { e ->
                    declares.findLastIndex {
                        paramTypesEq(
                            e,
                            it.valueParameters.toTypedArray()
                        )
                    }
                } ?: -1
                val iLParamTypesCds = paramTypesConditions
                    ?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.paramTypes(), it.valueParameters) } } } ?: -1
                val iLParamNames = paramNames?.let(matchIndex) { e ->
                    declares.findLastIndex {
                        paramNamesEq(
                            e,
                            it.valueParameters.map { it.name }.toTypedArray()
                        )
                    }
                } ?: -1
                val iLParamNamesCds = paramNamesConditions
                    ?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.paramNames(), it.valueParameters.map { it.name }) } } } ?: -1
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
                            and((typeEq(
                                it,
                                instance.returnType
                            )).let { hold ->
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
                            and(
                                paramTypesEq(
                                    it,
                                    instance.valueParameters.toTypedArray()
                                ).let { hold ->
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
                        paramNames?.also {
                            and(
                                paramNamesEq(
                                    it,
                                    instance.valueParameters.map { it.name }.toTypedArray()
                                ).let { hold ->
                                    if (hold) iParamNames++
                                    hold && matchIndex.compare(iParamNames, iLParamNames)
                                })
                        }
                        paramNamesConditions?.also {
                            and(instance.let { t -> runOrFalse { it(t.paramNames(), t.valueParameters.map { it.name }) } }.let { hold ->
                                if (hold) iParamNamesCds++
                                hold && matchIndex.compare(iParamNamesCds, iLParamNamesCds)
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
     * 查找任意 [KFunctionSignatureSupport] 或一组 [KFunctionSignatureSupport]
     * @param classSet [KFunctionSignatureSupport] 所在类
     * @param rulesData 规则查找数据
     * @param loader [ClassLoader] 方法参数 [KFunctionSignatureSupport.paramClass] 所在的 [ClassLoader]
     * @return [MutableList]<[KFunctionSignatureSupport]>
     * @throws IllegalStateException 如果未设置任何条件或 [KFunctionRulesData.paramTypes] 以及 [KFunctionRulesData.returnType] 目标类不存在
     * @throws NoSuchMethodError 如果找不到 [KFunctionSignatureSupport]
     */
    internal fun findFunctionSignatures(classSet: KClass<*>?, rulesData: KFunctionRulesData, loader: ClassLoader? = null) =
        rulesData.createResult { hasCondition ->
            if (returnType == UndefinedKotlin) error("Function match returnType class is not found")
            if (classSet == null) return@createResult mutableListOf()
            paramTypes?.takeIf { it.isNotEmpty() }
                ?.forEachIndexed { p, it -> if (it == UndefinedKotlin) error("Function match paramType[$p] class is not found") }
            val nameResolver = classSet.memberScope?.deserializationContext?.nameResolver ?: return@createResult mutableListOf()
            val protos = (classSet.memberScope?.impl ?: return@createResult mutableListOf()).functionProtos
            if (rulesData.name.isGetter || rulesData.name.isSetter){
                val data = KPropertyRulesData()
                data.name = rulesData.name.getterOrSetterJvmName
                data.nameConditions = rulesData.nameConditions
                data.isFindInSuper = rulesData.isFindInSuper
                data.matchCount = rulesData.matchCount
                data.matchCountRange = rulesData.matchCountRange
                data.matchCountConditions = rulesData.matchCountConditions
                data.modifiers = rulesData.modifiers
                data.orderIndex = rulesData.orderIndex
                data.matchIndex = rulesData.matchIndex
                data.type = rulesData.returnType ?: rulesData.paramTypes?.firstOrNull()
                data.typeConditions = rulesData.returnTypeConditions
                return@createResult if (rulesData.name.isSetter)
                    findPropertySignatures(classSet, data,loader).mapNotNull { it.setterOrNull }.toMutableList()
                else
                    findPropertySignatures(classSet, data,loader).mapNotNull { it.getterOrNull }.toMutableList()
            }
            val protoBufs by lazy { mutableListOf<ProtoBuf.Function>().also { buf -> protos.values.forEach { buf += it } }.asSequence() }
            if (hasCondition.not()) {
                return@createResult protoBufs.map { KFunctionSignatureSupport(classSet, loader, nameResolver, it) }.toMutableList()
            }
            mutableListOf<KFunctionSignatureSupport>().also { functions ->
                val supportMap: MutableMap<ProtoBuf.Function, KFunctionSignatureSupport> = mutableMapOf()
                fun ProtoBuf.Function.support(): KFunctionSignatureSupport =
                    supportMap.getOrPut(this) { KFunctionSignatureSupport(classSet, loader, nameResolver, this) }

                fun ProtoBuf.Function.name(): String {
                    protos.forEach { (t, u) ->
                        if (u.contains(this)) {
                            return t.asString()
                        }
                    }
                    return nameResolver.getString(name)
                }
                protoBufs.also { declares ->
                    var iReturnType = -1
                    var iReturnTypeCds = -1
                    var iParamTypes = -1
                    var iParamTypesCds = -1
                    var iParamNames = -1
                    var iParamNamesCds = -1
                    var iParamCount = -1
                    var iParamCountRange = -1
                    var iParamCountCds = -1
                    var iName = -1
                    var iModify = -1
                    var iNameCds = -1
                    val iLReturnType = returnType?.let(matchIndex) { e ->
                        declares.findLastIndex {
                            typeEq(
                                e,
                                it.support().getReturnTypeOrNull(classSet, loader)
                            )
                        }
                    } ?: -1
                    val iLReturnTypeCds = returnTypeConditions
                        ?.let(matchIndex) { e ->
                            declares.findLastIndex {
                                runOrFalse {
                                    val returnType = it.support().getReturnTypeOrNull(classSet, loader) ?: return@runOrFalse true
                                    e(KObjectRules.with(returnType), returnType)
                                }
                            }
                        } ?: -1
                    val iLParamCount = paramCount.takeIf(matchIndex) { it >= 0 }
                        ?.let { e -> declares.findLastIndex { e == it.valueParameterCount } } ?: -1
                    val iLParamCountRange = paramCountRange.takeIf(matchIndex) { it.isEmpty().not() }
                        ?.let { e -> declares.findLastIndex { it.valueParameterCount in e } } ?: -1
                    val iLParamCountCds = paramCountConditions?.let(matchIndex) { e ->
                        declares.findLastIndex { it.valueParameterCount.let { s -> runOrFalse { e(s.cast(), s) } } }
                    } ?: -1
                    val iLParamTypes = paramTypes?.let(matchIndex) { e ->
                        declares.findLastIndex {
                            paramTypesEq(
                                e,
                                it.support().getParamTypesOrNull(classSet, loader)?.toTypedArray()
                            )
                        }
                    } ?: -1
                    val iLParamTypesCds = paramTypesConditions
                        ?.let(matchIndex) { e ->
                            declares.findLastIndex {
                                runOrFalse {
                                    val paramTypes = it.support().getParamTypesOrNull(classSet, loader) ?: return@runOrFalse true
                                    e(KObjectRules.with(paramTypes), it.valueParameterList.mapIndexed { index, valueParameter ->
                                        val param = paramTypes[index]
                                        object : KParameter {
                                            override val annotations: List<Annotation>
                                                get() = param.annotations
                                            override val index: Int
                                                get() = index
                                            override val isOptional: Boolean
                                                get() = throw NotImplementedError("Parameter type condition customization in Signature Conditions is not supported!!!")
                                            override val isVararg: Boolean
                                                get() = valueParameter.hasVarargElementType()
                                            override val kind: KParameter.Kind
                                                get() = throw NotImplementedError("Parameter type condition customization in Signature Conditions is not supported!!!")
                                            override val name: String
                                                get() = nameResolver.getString(valueParameter.name)
                                            override val type: KType
                                                get() = param
                                        }
                                    })
                                }
                            }
                        } ?: -1
                    val iLParamNames = paramNames?.let(matchIndex) { e ->
                        declares.findLastIndex {
                            paramNamesEq(
                                e,
                                it.valueParameterList.map { nameResolver.getString(it.name) }.toTypedArray()
                            )
                        }
                    } ?: -1
                    val iLParamNamesCds = paramNamesConditions
                        ?.let(matchIndex) { e ->
                            declares.findLastIndex {
                                runOrFalse {
                                    e(
                                        KObjectRules.with(it),
                                        it.valueParameterList.map { nameResolver.getString(it.name) })
                                }
                            }
                        } ?: -1
                    val iLName = name.takeIf(matchIndex) { it.isNotBlank() }?.let { e -> declares.findLastIndex { e == it.name() } } ?: -1
                    val iLModify = modifiers?.let(matchIndex) { e ->
                        declares.findLastIndex {
                            runOrFalse {
                                val member = it.support().getMemberOrNull(classSet, loader) ?: return@runOrFalse true
                                e(KModifierRules.with(member))
                            }
                        }
                    } ?: -1
                    val iLNameCds = nameConditions
                        ?.let(matchIndex) { e -> declares.findLastIndex { it.name().let { n -> runOrFalse { e(n.cast(), n) } } } } ?: -1
                    declares.forEachIndexed { index, instance ->
                        conditions {
                            name.takeIf { it.isNotBlank() }?.also {
                                and((it == instance.name()).let { hold ->
                                    if (hold) iName++
                                    hold && matchIndex.compare(iName, iLName)
                                })
                            }
                            returnType?.also {
                                and((typeEq(
                                    it,
                                    instance.support().getReturnTypeOrNull(classSet, loader)
                                )).let { hold ->
                                    if (hold) iReturnType++
                                    hold && matchIndex.compare(iReturnType, iLReturnType)
                                })
                            }
                            returnTypeConditions?.also {
                                and(instance.let { r ->
                                    runOrFalse {
                                        val returnType = r.support().getReturnTypeOrNull(classSet, loader) ?: return@runOrFalse true
                                        it(KObjectRules.with(returnType), returnType)
                                    }
                                }.let { hold ->
                                    if (hold) iReturnTypeCds++
                                    hold && matchIndex.compare(iReturnTypeCds, iLReturnTypeCds)
                                })
                            }
                            paramCount.takeIf { it >= 0 }?.also {
                                and((instance.valueParameterCount == it).let { hold ->
                                    if (hold) iParamCount++
                                    hold && matchIndex.compare(iParamCount, iLParamCount)
                                })
                            }
                            paramCountRange.takeIf { it.isEmpty().not() }?.also {
                                and((instance.valueParameterCount in it).let { hold ->
                                    if (hold) iParamCountRange++
                                    hold && matchIndex.compare(iParamCountRange, iLParamCountRange)
                                })
                            }
                            paramCountConditions?.also {
                                and(instance.valueParameterCount.let { s -> runOrFalse { it(s.cast(), s) } }.let { hold ->
                                    if (hold) iParamCountCds++
                                    hold && matchIndex.compare(iParamCountCds, iLParamCountCds)
                                })
                            }
                            paramTypes?.also {
                                and(
                                    paramTypesEq(
                                        it,
                                        instance.support().getParamTypesOrNull(classSet, loader)?.toTypedArray()
                                    ).let { hold ->
                                        if (hold) iParamTypes++
                                        hold && matchIndex.compare(iParamTypes, iLParamTypes)
                                    })
                            }
                            paramTypesConditions?.also {
                                and(instance.let { t ->
                                    runOrFalse {
                                        val paramTypes = t.support().getParamTypesOrNull(classSet, loader) ?: return@runOrFalse true
                                        it(KObjectRules.with(paramTypes), t.valueParameterList.mapIndexed { index, valueParameter ->
                                            val param = paramTypes[index]
                                            object : KParameter {
                                                override val annotations: List<Annotation>
                                                    get() = param.annotations
                                                override val index: Int
                                                    get() = index
                                                override val isOptional: Boolean
                                                    get() = throw NotImplementedError("Parameter type condition customization in Signature Conditions is not supported!!!")
                                                override val isVararg: Boolean
                                                    get() = valueParameter.hasVarargElementType()
                                                override val kind: KParameter.Kind
                                                    get() = throw NotImplementedError("Parameter type condition customization in Signature Conditions is not supported!!!")
                                                override val name: String
                                                    get() = nameResolver.getString(valueParameter.name)
                                                override val type: KType
                                                    get() = param
                                            }
                                        })
                                    }
                                }.let { hold ->
                                    if (hold) iParamTypesCds++
                                    hold && matchIndex.compare(iParamTypesCds, iLParamTypesCds)
                                })
                            }
                            paramNames?.also {
                                and(
                                    paramNamesEq(
                                        it,
                                        instance.valueParameterList.map { nameResolver.getString(it.name) }
                                            .toTypedArray()
                                    ).let { hold ->
                                        if (hold) iParamNames++
                                        hold && matchIndex.compare(iParamNames, iLParamNames)
                                    })
                            }
                            paramNamesConditions?.also {
                                and(instance.let { t ->
                                    runOrFalse {
                                        it(
                                            KObjectRules.with(t),
                                            t.valueParameterList.map { nameResolver.getString(it.name) })
                                    }
                                }.let { hold ->
                                    if (hold) iParamNamesCds++
                                    hold && matchIndex.compare(iParamNamesCds, iLParamNamesCds)
                                })
                            }
                            modifiers?.also {
                                and(runOrFalse {
                                    val member = instance.support().getMemberOrNull(classSet, loader) ?: return@runOrFalse true
                                    it(KModifierRules.with(member))
                                }.let { hold ->
                                    if (hold) iModify++
                                    hold && matchIndex.compare(iModify, iLModify)
                                })
                            }
                            nameConditions?.also {
                                and(instance.name().let { n -> runOrFalse { it(n.cast(), n) } }.let { hold ->
                                    if (hold) iNameCds++
                                    hold && matchIndex.compare(iNameCds, iLNameCds)
                                })
                            }
                            orderIndex.compare(index, declares.lastIndex()) { and(it) }
                        }.finally { functions.add(instance.support()) }
                    }
                }
            }.takeIf { it.isNotEmpty() } ?: findSuperOrThrow(classSet)
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
                var iParamNames = -1
                var iParamNamesCds = -1
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
                val iLParamTypes = paramTypes?.let(matchIndex) { e ->
                    declares.findLastIndex {
                        paramTypesEq(
                            e,
                            it.valueParameters.toTypedArray()
                        )
                    }
                } ?: -1
                val iLParamTypesCds = paramTypesConditions
                    ?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.paramTypes(), it.valueParameters) } } } ?: -1
                val iLParamNames = paramNames?.let(matchIndex) { e ->
                    declares.findLastIndex {
                        paramNamesEq(
                            e,
                            it.valueParameters.map { it.name }.toTypedArray()
                        )
                    }
                } ?: -1
                val iLParamNamesCds = paramNamesConditions
                    ?.let(matchIndex) { e -> declares.findLastIndex { runOrFalse { e(it.paramNames(), it.valueParameters.map { it.name }) } } } ?: -1
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
                            and(
                                paramTypesEq(
                                    it,
                                    instance.valueParameters.toTypedArray()
                                ).let { hold ->
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
                        paramNames?.also {
                            and(
                                paramNamesEq(
                                    it,
                                    instance.valueParameters.map { it.name }.toTypedArray()
                                ).let { hold ->
                                    if (hold) iParamNames++
                                    hold && matchIndex.compare(iParamNames, iLParamNames)
                                })
                        }
                        paramNamesConditions?.also {
                            and(instance.let { t -> runOrFalse { it(t.paramNames(), t.valueParameters.map { it.name }) } }.let { hold ->
                                if (hold) iParamNamesCds++
                                hold && matchIndex.compare(iParamNamesCds, iLParamNamesCds)
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
            KBaseFinder.TAG_CLASS -> NoClassDefFoundErrorKClass.space()
            KBaseFinder.TAG_PROPERTY -> NoSuchFieldErrorKClass.space()
            KBaseFinder.TAG_FUNCTION, KBaseFinder.TAG_CONSTRUCTOR -> NoSuchMethodErrorKClass.space()
            else -> error("Invalid Exception type")
        }
        var splicing = ""
        content.forEach { if (it.isNotBlank()) splicing += "$space$it\n" }
        val template = "Can't find this $name in [$instanceSet]:\n${splicing}Generated by ${TAG}"
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
                    yieldAll(
                        ((if (existTop) top.kotlin.java.declaredFields else arrayOf()) + java.declaredFields).asSequence().mapNotNull { it.kotlin })
                    yieldAll(
                        ((if (existTop) top.kotlin.java.declaredMethods else arrayOf()) + java.declaredMethods).asSequence().mapNotNull { it.kotlin })
                    yieldAll(java.declaredConstructors.asSequence().mapNotNull { it.kotlin })
                } else {
                    yieldAll(((if (existTop) top.declaredTopPropertys else arrayListOf()) + declaredPropertys).asSequence())
                    yieldAll(((if (existTop) top.declaredTopFunctions else arrayListOf()) + declaredFunctions).toList())
                    yieldAll(constructors.toList())
                }
            }
        }.onFailure {
            KYLog.warn(msg = "Failed to get the declared KCallables in [$this] because got an exception", e = it)
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
            KYLog.warn(msg = "Failed to get the declared Propertys in [$this] because got an exception", e = it)
        }.getOrNull()

    /**
     * 判断当前 [String] 是否为 Getter 函数的函数名
     * @return [Boolean]
     */
    private val String.isGetter get() = startsWith("<get-") && endsWith(">")

    /**
     * 获取当前 [KClass] 中存在的 Getter 函数的 [KFunction] 函数名
     * @return [String] or null
     */
    private val String.getterOrSetterJvmName get() = substring(5, length - 1)

    /**
     * 判断当前 [String] 是否为 Setter 函数的函数名
     * @return [Boolean]
     */
    private val String.isSetter get() = startsWith("<set-") && endsWith(">")
    /**
     * 获取当前 [KClass] 中存在的 [KFunction] 数组
     * @return [Sequence]<[KFunction]> or null
     */
    private val KClass<*>.existFunctions
        get() = runCatching {
            if (KYukiReflection.Configs.isUseJvmObtainCallables)
                ((if (existTop) top.kotlin.java.declaredMethods else arrayOf()) + java.declaredMethods).asSequence().mapNotNull { it.kotlin } else
                ((if (existTop) top.declaredTopFunctions else arrayListOf()) + declaredFunctions).asSequence()
        }.onFailure {
            KYLog.warn(msg = "Failed to get the declared Functions in [$this] because got an exception", e = it)
        }.getOrNull()

    /**
     * 获取当前 [KClass] 中存在的 Getter [KFunction] 数组
     * @return [Sequence]<Getter [KFunction]> or null
     */
    private val KClass<*>.existGetterFunctions
        get() = runCatching { existPropertys?.map { it.getter } }.getOrNull()

    /**
     * 获取当前 [KClass] 中存在的 Setter [KFunction] 数组
     * @return [Sequence]<Getter [KFunction]> or null
     */
    private val KClass<*>.existSetterFunctions
        get() = runCatching { existPropertys?.mapNotNull { it.toMutableOrNull?.setter } }.getOrNull()

    /**
     * 获取当前 [KClass] 中存在的 Constructor [KFunction] 数组
     * @return [Sequence]<Constructor [KFunction]> or null
     */
    private val KClass<*>.existConstructors
        get() = runCatching {
            if (KYukiReflection.Configs.isUseJvmObtainCallables) java.declaredConstructors.asSequence()
                .mapNotNull { it.kotlin } else constructors.asSequence()
        }.onFailure {
            KYLog.warn(msg = "Failed to get the declared Constructors in [$this] because got an exception", e = it)
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
                }.onFailure { KYLog.warn(msg = "Failed to access [$callable] because got an exception", e = it) }
            }
        }

    /**
     * 判断两个类型是否在一定参数上相等
     *
     *    如: compare:KVariance.INVARIANT -> original = KType(variance:KVariance.INVARIANT)  true
     *       compare:KClass<Int> -> original = KType(kotlin:KClass<Int>)  true
     *
     * 类型支持 [Class]/[KClassifier]/[KClass]/[KTypeParameter] or [KTypeProjection]/array([KTypeProjection]) or [KVariance]/array([KVariance]) or [KType] or [KParameter] or [KParameter.Kind] or [KGenericClass]
     *
     * @param compare 类型
     * @param original 类型
     * @return [Boolean] 是否相等
     */
    private fun typeEq(compare: Any?, original: Any?): Boolean {
        when (compare) {
            original, VagueKotlin, VagueKotlin.java -> return true
        }
        when (original) {
            VagueKotlin, VagueKotlin.java -> return true
        }
        when (compare) {
            Void.TYPE, Void::class, Void::class.java, UnitKClass, UnitKClass.java -> {
                when (original) {
                    Void.TYPE, Void::class, Void::class.java, UnitKClass, UnitKClass.java -> return true
                }
            }
        }
        if (compare == null || original == null) return false
        return when (compare) {
            is Class<*> -> when (original) {
                is KType -> compare.kotlin == original.classifier.kotlin
                is KParameter -> compare.kotlin == original.type.classifier.kotlin
                is KClassifier -> compare.kotlin == original.kotlin
                else -> false
            }

            is KClassifier -> when (original) {
                is KType -> compare.kotlin == original.classifier.kotlin
                is KParameter -> compare.kotlin == original.type.classifier.kotlin
                else -> false
            }

            is KTypeProjection -> when (original) {
                is KVariance -> compare.variance == original
                is KType -> compare.variance == original.arguments.first().variance && compare.type?.generic() == original.arguments.first().type?.generic()
                is KParameter -> compare.variance == original.type.arguments.first().variance && compare.type?.generic() == original.type.arguments.first().type?.generic()
                is Class<*> -> compare.type?.kotlin == original.kotlin
                is KClassifier -> compare.type?.kotlin == original
                else -> false
            }

            is KVariance -> when (original) {
                is KTypeProjection -> compare == original.variance
                is KType -> compare == original.arguments.first().variance
                is KParameter -> compare == original.type.arguments.first().variance
                else -> false
            }

            is KType -> when (original) {
                is KType -> original.generic() == compare.generic()
                is KClassifier -> compare.generic() == original
                is KParameter -> compare.generic().equals(original.type)
                else -> false
            }

            is KParameter -> when (original) {
                is KClassifier -> compare.type.classifier.kotlin == original.kotlin
                is KType -> compare.type.generic() == original
                is KParameter.Kind -> compare.kind == original
                else -> false
            }

            is KParameter.Kind -> when (original) {
                is KParameter -> compare == original.kind
                else -> false
            }

            is KGenericClass -> when (original) {
                is KType -> compare == original.generic()
                is KClassifier -> compare.type.classifier.kotlin == original.kotlin
                is KParameter -> compare == original
                else -> false
            }

            else -> {
                var eq = true
                val result = runCatching {
                    KBaseFinder.checkArrayGenerics(compare)
                }.getOrNull() ?: return false
                when (original) {
                    is KType -> {
                        if (original.arguments.size != result.size) return false
                        result.forEachIndexed { index, t ->
                            when (t) {
                                is KTypeProjection -> {
                                    if (!eq) return false
                                    eq = t.type?.classifier.kotlin == VagueKotlin && t.variance == original.arguments[index].variance
                                    if (!eq) {
                                        eq =
                                            (t.variance == original.arguments[index].variance) && (t.type?.generic() == original.arguments[index].type?.generic())
                                    }
                                }

                                is KVariance -> {
                                    if (!eq) return false
                                    eq = t == original.arguments.first().variance
                                }
                            }
                        }
                    }

                    is KParameter -> {
                        if (original.type.arguments.size != result.size) return false
                        result.forEachIndexed { index, t ->
                            when (t) {
                                is KTypeProjection -> {
                                    if (!eq) return false
                                    eq = t.type?.classifier.kotlin == VagueKotlin && t.variance == original.type.arguments[index].variance
                                    if (!eq) eq =
                                        (t.variance == original.type.arguments[index].variance) && (t.type?.generic() == original.type.arguments[index].type?.generic())
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
                if (compare.all { it == VagueKotlin }) error("The number of VagueKotlin must be at least less than the count of paramTypes")
                for (i in compare.indices) return typeEq(
                    compare[i],
                    original[i]
                )
                true
            }
        }
    }

    /**
     * 对比两者参数名是否一致 包含忽略对象如 [VagueKotlin]
     *
     * @param compare 用于比较的数组
     * @param original 方法、构造方法原始数组
     * @return [Boolean] 是否相等
     */
    private fun paramNamesEq(compare: Array<out String?>?, original: Array<out String?>?): Boolean {
        return when {
            (compare == null && original == null) || (compare?.isEmpty() == true && original?.isEmpty() == true) -> true
            (compare == null && original != null) || (compare != null && original == null) || (compare?.size != original?.size) -> false
            else -> {
                if (compare == null || original == null) return false
                for (i in compare.indices) {
                    if (compare[i] != original[i] &&
                        compare[i] != VagueKotlin.name &&
                        original[i] != VagueKotlin.name &&
                        compare[i] != "" &&
                        original[i] != "" &&
                        compare[i] != "null" &&
                        original[i] != "null"
                    ) return false
                }
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
        result(
            when (this) {
                is KPropertyRulesData -> isInitialize
                is KFunctionRulesData -> isInitialize
                is KConstructorRulesData -> isInitialize
                else -> false
            }
        )

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
                KReflectionTool.findPropertys(
                    classSet.superclass,
                    rulesData = this
                ) as T
            else throwNotFoundError(classSet)

        is KFunctionRulesData ->
            if (isFindInSuper && classSet.hasExtends)
                KReflectionTool.findFunctions(
                    classSet.superclass,
                    rulesData = this
                ) as T
            else throwNotFoundError(classSet)

        is KConstructorRulesData ->
            if (isFindInSuper && classSet.hasExtends)
                KReflectionTool.findConstructors(
                    classSet.superclass,
                    rulesData = this
                ) as T
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
        is KPropertyRulesData -> throw KReflectionTool.createException(
            instanceSet,
            objectName,
            *templates
        )

        is KFunctionRulesData -> throw KReflectionTool.createException(
            instanceSet,
            objectName,
            *templates
        )

        is KConstructorRulesData -> throw KReflectionTool.createException(
            instanceSet,
            objectName,
            *templates
        )

        else -> error("Type [$this] not allowed")
    }
}