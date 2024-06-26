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
 * This file is created by fankes on 2022/9/5.
 * This file is modified by fankes on 2023/1/25.
 */
@file:Suppress("PropertyName")

package io.github.dreammooncai.yukireflection.finder.classes.data

import io.github.dreammooncai.yukireflection.factory.enclosingClass
import io.github.dreammooncai.yukireflection.factory.name
import io.github.dreammooncai.yukireflection.factory.simpleNameOrJvm
import io.github.dreammooncai.yukireflection.finder.base.KBaseFinder
import io.github.dreammooncai.yukireflection.finder.base.data.KBaseRulesData
import io.github.dreammooncai.yukireflection.finder.base.rules.KModifierRules
import io.github.dreammooncai.yukireflection.finder.callable.data.KCallableRulesData
import io.github.dreammooncai.yukireflection.finder.callable.data.KConstructorRulesData
import io.github.dreammooncai.yukireflection.finder.callable.data.KFunctionRulesData
import io.github.dreammooncai.yukireflection.finder.callable.data.KPropertyRulesData
import io.github.dreammooncai.yukireflection.type.factory.KNameConditions
import kotlin.reflect.KFunction
import kotlin.reflect.KCallable
import kotlin.reflect.KProperty
import kotlin.reflect.KClass

/**
 * [KClass] 规则查找数据类
 * @param fromPackages 指定包名范围名称数组
 * @param fullName 完整名称
 * @param simpleName 简单名称
 * @param singleName 独立名称
 * @param fullNameConditions 完整名称规则
 * @param simpleNameConditions 简单名称规则
 * @param singleNameConditions 独立名称规则
 * @param isAnonymousClass 匿名类
 * @param isNoExtendsClass 无继承的父类
 * @param isNoImplementsClass 无继承的实现的接口类
 * @param extendsClass 继承的父类名称数组
 * @param annotationClass 注解类名称数组
 * @param implementsClass 实现的接口类名称数组
 * @param enclosingClass 包含的封闭类 (主类) 名称数组
 * @param callableRules [KCallable] 查找条件数据数组
 * @param propertyRules [KProperty] 查找条件数据数组
 * @param functionRules [KFunction] 查找条件数据数组
 * @param constroctorRules Constructor[KFunction] 查找条件数据数组
 */
internal class KClassRulesData internal constructor(
    var fromPackages: MutableList<PackageRulesData> = mutableListOf(),
    var fullName: NameRulesData? = null,
    var simpleName: NameRulesData? = null,
    var singleName: NameRulesData? = null,
    var fullNameConditions: KNameConditions? = null,
    var simpleNameConditions: KNameConditions? = null,
    var singleNameConditions: KNameConditions? = null,
    var isAnonymousClass: Boolean? = null,
    var isNoExtendsClass: Boolean? = null,
    var isNoImplementsClass: Boolean? = null,
    var extendsClass: MutableList<String> = mutableListOf(),
    var annotationClass: MutableList<String> = mutableListOf(),
    var implementsClass: MutableList<String> = mutableListOf(),
    var enclosingClass: MutableList<String> = mutableListOf(),
    var callableRules: MutableList<KCallableRulesData> = mutableListOf(),
    var propertyRules: MutableList<KPropertyRulesData> = mutableListOf(),
    var functionRules: MutableList<KFunctionRulesData> = mutableListOf(),
    var constroctorRules: MutableList<KConstructorRulesData> = mutableListOf()
) : KBaseRulesData() {

    /**
     * 创建类名匹配条件查找数据类
     * @param name 包名
     * @return [NameRulesData]
     */
    internal fun createNameRulesData(name: String) = NameRulesData(name)

    /**
     * 创建包名范围名称过滤匹配条件查找数据类
     * @param name 包名
     * @return [PackageRulesData]
     */
    internal fun createPackageRulesData(name: String) = PackageRulesData(name)

    /**
     * 获取 [KClass.simpleNameOrJvm] 与 [KClass.name] 的独立名称
     * @param instance 当前 [KClass] 实例
     * @return [String]
     */
    internal fun classSingleName(instance: KClass<*>) = instance.simpleNameOrJvm.takeIf { it.isNotBlank() == true }
        ?: instance.enclosingClass?.let { it.simpleNameOrJvm + instance.name.replace(it.name, newValue = "") } ?: ""

    /**
     * 类名匹配条件查找数据类
     * @param name 包名
     * @param isOptional 是否可选 - 默认否
     */
    inner class NameRulesData internal constructor(var name: String, var isOptional: Boolean = false) {

        /** [Class.getName] */
        internal val TYPE_NAME = 0

        /** [Class.getSimpleName] */
        internal val TYPE_SIMPLE_NAME = 1

        /** [Class.getSimpleName] or [Class.getName] */
        internal val TYPE_SINGLE_NAME = 2

        /**
         * 匹配当前 [KClass] 实例
         * @param instance 当前 [KClass] 实例
         * @param type 判断类型
         * @return [Boolean]
         */
        internal fun equals(instance: KClass<*>, type: Int) = when (type) {
            TYPE_NAME -> instance.name == name
            TYPE_SIMPLE_NAME -> instance.simpleNameOrJvm == name
            TYPE_SINGLE_NAME -> classSingleName(instance) == name
            else -> false
        }

        override fun toString() = "$name optional($isOptional)"
    }

    /**
     * 包名范围名称过滤匹配条件查找数据类
     * @param name 包名
     * @param isAbsolute 是否绝对匹配 - 默认否
     */
    inner class PackageRulesData internal constructor(var name: String, var isAbsolute: Boolean = false) {
        override fun toString() = "$name absolute($isAbsolute)"
    }

    override val templates
        get() = arrayOf(
            fromPackages.takeIf { it.isNotEmpty() }?.let { "from:$it" } ?: "",
            fullName?.let { "fullName:[$it]" } ?: "",
            simpleName?.let { "simpleName:[$it]" } ?: "",
            singleName?.let { "singleName:[$it]" } ?: "",
            fullNameConditions?.let { "fullNameConditions:[existed]" } ?: "",
            simpleNameConditions?.let { "simpleNameConditions:[existed]" } ?: "",
            singleNameConditions?.let { "singleNameConditions:[existed]" } ?: "",
            modifiers?.let { "modifiers:${KModifierRules.templates(uniqueValue)}" } ?: "",
            isAnonymousClass?.let { "isAnonymousClass:[$it]" } ?: "",
            isNoExtendsClass?.let { "isNoExtendsClass:[$it]" } ?: "",
            isNoImplementsClass?.let { "isNoImplementsClass:[$it]" } ?: "",
            annotationClass.takeIf { it.isNotEmpty() }?.let { "annotationClass:$it" } ?: "",
            extendsClass.takeIf { it.isNotEmpty() }?.let { "extendsClass:$it" } ?: "",
            implementsClass.takeIf { it.isNotEmpty() }?.let { "implementsClass:$it" } ?: "",
            enclosingClass.takeIf { it.isNotEmpty() }?.let { "enclosingClass:$it" } ?: "",
            callableRules.takeIf { it.isNotEmpty() }?.let { "callableRules:[${it.size} existed]" } ?: "",
            propertyRules.takeIf { it.isNotEmpty() }?.let { "propertyRules:[${it.size} existed]" } ?: "",
            functionRules.takeIf { it.isNotEmpty() }?.let { "functionRules:[${it.size} existed]" } ?: "",
            constroctorRules.takeIf { it.isNotEmpty() }?.let { "constroctorRules:[${it.size} existed]" } ?: ""
        )

    override val objectName get() = KBaseFinder.TAG_CLASS

    override val isInitialize
        get() = super.isInitialize || fromPackages.isNotEmpty() || fullName != null || simpleName != null || singleName != null ||
            fullNameConditions != null || simpleNameConditions != null || singleNameConditions != null || isAnonymousClass != null ||
          isNoExtendsClass != null || isNoImplementsClass != null || annotationClass.isNotEmpty() || extendsClass.isNotEmpty() || enclosingClass.isNotEmpty() ||
            callableRules.isNotEmpty() || propertyRules.isNotEmpty() || functionRules.isNotEmpty() || constroctorRules.isNotEmpty()

    override fun toString() = "[$fromPackages][$fullName][$simpleName][$singleName][$fullNameConditions][$simpleNameConditions]" +
      "[$singleNameConditions][$modifiers][$isAnonymousClass][$isNoExtendsClass][$isNoImplementsClass][$annotationClass][$extendsClass][$implementsClass]" +
        "[$enclosingClass][$callableRules][$propertyRules][$functionRules][$constroctorRules]" + super.toString()
}