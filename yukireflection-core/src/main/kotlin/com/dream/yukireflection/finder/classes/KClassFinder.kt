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
 * This file is created by fankes on 2022/9/4.
 * This file is modified by fankes on 2023/1/25.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package com.dream.yukireflection.finder.classes

import com.dream.yukireflection.finder.base.KClassBaseFinder
import com.dream.yukireflection.finder.classes.data.KClassRulesData
import com.dream.yukireflection.finder.classes.rules.KCallableRules
import com.dream.yukireflection.finder.classes.rules.KConstructorRules
import com.dream.yukireflection.finder.classes.rules.KFunctionRules
import com.dream.yukireflection.finder.classes.rules.KPropertyRules
import com.dream.yukireflection.finder.classes.rules.base.KBaseRules
import com.dream.yukireflection.finder.tools.KReflectionTool
import com.dream.yukireflection.type.factory.KModifierConditions
import com.dream.yukireflection.type.factory.KNameConditions
import com.dream.yukireflection.utils.factory.runBlocking
import kotlin.reflect.KClass
import kotlin.reflect.KCallable
import kotlin.reflect.KProperty
import kotlin.reflect.KFunction

/**
 * [KClass] 查找类
 *
 * @param classSet 当前需要查找的 List<[KClass]> 数组
 */
class KClassFinder internal constructor(classSet: Collection<KClass<*>>? = null) : KClassBaseFinder(classSet) {

    override var rulesData = KClassRulesData()

    /**
     * 设置 [KClass] 完整名称
     *
     * 只会查找匹配到的 [Class.getName]
     *
     * 例如 com.demo.Test 需要填写 com.demo.Test
     * @return [String]
     */
    var fullName
        get() = rulesData.fullName?.name ?: ""
        set(value) {
            rulesData.fullName = rulesData.createNameRulesData(value)
        }

    /**
     * 设置 [KClass] 简单名称
     *
     * 只会查找匹配到的 [Class.getSimpleName]
     *
     * 例如 com.demo.Test 只需要填写 Test
     *
     * 对于匿名类例如 com.demo.Test$InnerTest 会为空 - 此时你可以使用 [singleName]
     * @return [String]
     */
    var simpleName
        get() = rulesData.simpleName?.name ?: ""
        set(value) {
            rulesData.simpleName = rulesData.createNameRulesData(value)
        }

    /**
     * 设置 [KClass] 独立名称
     *
     * 设置后将首先使用 [Class.getSimpleName] - 若为空则会使用 [Class.getName] 进行处理
     *
     * 例如 com.demo.Test 只需要填写 Test
     *
     * 对于匿名类例如 com.demo.Test$InnerTest 只需要填写 Test$InnerTest
     * @return [String]
     */
    var singleName
        get() = rulesData.singleName?.name ?: ""
        set(value) {
            rulesData.singleName = rulesData.createNameRulesData(value)
        }

    /**
     * 设置在指定包名范围查找当前 [KClass]
     *
     * 设置后仅会在当前 [name] 开头匹配的包名路径下进行查找 - 可提升查找速度
     *
     * 例如 ↓
     *
     * com.demo.test
     *
     * com.demo.test.demo
     *
     * - 建议设置此参数指定查找范围 - 否则 [KClass] 过多时将会非常慢
     * @param name 指定包名
     * @return [FromPackageRules] 可设置 [FromPackageRules.absolute] 标识包名绝对匹配
     */
    fun from(vararg name: String) = FromPackageRules(mutableListOf<KClassRulesData.PackageRulesData>().also {
        name.takeIf { e -> e.isNotEmpty() }?.forEach { e -> it.add(rulesData.createPackageRulesData(e)) }
        if (it.isNotEmpty()) rulesData.fromPackages.addAll(it)
    })

    /**
     * 设置 [KClass] 标识符筛选条件
     *
     * - 可不设置筛选条件
     * @param conditions 条件方法体
     */
    fun modifiers(conditions: KModifierConditions) {
        rulesData.modifiers = conditions
    }

    /**
     * 设置 [KClass] 完整名称
     *
     * 只会查找匹配到的 [Class.getName]
     *
     * 例如 com.demo.Test 需要填写 com.demo.Test
     * @param value 名称
     * @return [ClassNameRules] 可设置 [ClassNameRules.optional] 标识类名可选
     */
    fun fullName(value: String) = rulesData.createNameRulesData(value).let {
        rulesData.fullName = it
        ClassNameRules(it)
    }

    /**
     * 设置 [KClass] 简单名称
     *
     * 只会查找匹配到的 [Class.getSimpleName]
     *
     * 例如 com.demo.Test 只需要填写 Test
     *
     * 对于匿名类例如 com.demo.Test$InnerTest 会为空 - 此时你可以使用 [singleName]
     * @param value 名称
     * @return [ClassNameRules] 可设置 [ClassNameRules.optional] 标识类名可选
     */
    fun simpleName(value: String) = rulesData.createNameRulesData(value).let {
        rulesData.simpleName = it
        ClassNameRules(it)
    }

    /**
     * 设置 [KClass] 独立名称
     *
     * 设置后将首先使用 [Class.getSimpleName] - 若为空则会使用 [Class.getName] 进行处理
     *
     * 例如 com.demo.Test 只需要填写 Test
     *
     * 对于匿名类例如 com.demo.Test$InnerTest 只需要填写 Test$InnerTest
     * @param value 名称
     * @return [ClassNameRules] 可设置 [ClassNameRules.optional] 标识类名可选
     */
    fun singleName(value: String) = rulesData.createNameRulesData(value).let {
        rulesData.singleName = it
        ClassNameRules(it)
    }

    /**
     * 设置 [KClass] 完整名称条件
     *
     * 只会查找匹配到的 [Class.getName]
     * @param conditions 条件方法体
     */
    fun fullName(conditions: KNameConditions) {
        rulesData.fullNameConditions = conditions
    }

    /**
     * 设置 [KClass] 简单名称条件
     *
     * 只会查找匹配到的 [Class.getSimpleName]
     * @param conditions 条件方法体
     */
    fun simpleName(conditions: KNameConditions) {
        rulesData.simpleNameConditions = conditions
    }

    /**
     * 设置 [KClass] 独立名称条件
     *
     * 设置后将首先使用 [Class.getSimpleName] - 若为空则会使用 [Class.getName] 进行处理
     * @param conditions 条件方法体
     */
    fun singleName(conditions: KNameConditions) {
        rulesData.singleNameConditions = conditions
    }

    /** 设置 [KClass] 继承的父类 */
    inline fun <reified T> extends() {
        rulesData.extendsClass.add(T::class.java.name)
    }

    /**
     * 设置 [KClass] 继承的父类
     *
     * 会同时查找 [name] 中所有匹配的父类
     * @param name [KClass] 完整名称
     */
    fun extends(vararg name: String) {
        rulesData.extendsClass.addAll(name.toList())
    }


    /** 设置 [Class] 修饰的注解 */
    inline fun <reified T> annotations() {
        rulesData.annotationClass.add(T::class.java.name)
    }

    /**
     * 设置 [Class] 修饰的注解
     *
     * 会同时查找 [name] 中所有匹配的注解类
     * @param name [Class] 完整名称
     */
    fun annotations(vararg name: String) {
        rulesData.annotationClass.addAll(name.toList())
    }


    /** 设置 [KClass] 实现的接口类 */
    inline fun <reified T> implements() {
        rulesData.implementsClass.add(T::class.java.name)
    }

    /**
     * 设置 [KClass] 实现的接口类
     *
     * 会同时查找 [name] 中所有匹配的接口类
     * @param name [KClass] 完整名称
     */
    fun implements(vararg name: String) {
        rulesData.implementsClass.addAll(name.toList())
    }

    /**
     * 标识 [KClass] 为匿名类
     *
     * 例如 com.demo.Test$1 或 com.demo.Test$InnerTest
     *
     * 标识后你可以使用 [enclosing] 来进一步指定匿名类的 (封闭类) 主类
     */
    fun anonymous() {
        rulesData.isAnonymousClass = true
    }

    /**
     * 设置 [KClass] 没有任何继承
     *
     * 此时 [KClass] 只应该继承于 [Any]
     *
     * - 设置此条件后 [extends] 将失效
     */
    fun noExtends() {
        rulesData.isNoExtendsClass = true
    }

    /**
     * 设置 [KClass] 没有任何接口
     *
     * - 设置此条件后 [implements] 将失效
     */
    fun noImplements() {
        rulesData.isNoImplementsClass = true
    }

    /**
     * 设置 [KClass] 没有任何继承与接口
     *
     * 此时 [KClass] 只应该继承于 [Any]
     *
     * - 设置此条件后 [extends] 与 [implements] 将失效
     */
    fun noSuper() {
        noExtends()
        noImplements()
    }

    /** 设置 [KClass] 匿名类的 (封闭类) 主类 */
    inline fun <reified T> enclosing() {
        rulesData.enclosingClass.add(T::class.java.name)
    }

    /**
     * 设置 [KClass] 匿名类的 (封闭类) 主类
     *
     * 会同时查找 [name] 中所有匹配的 (封闭类) 主类
     * @param name [KClass] 完整名称
     */
    fun enclosing(vararg name: String) {
        rulesData.enclosingClass.addAll(name.toList())
    }

    /**
     * 包名范围名称过滤匹配条件实现类
     * @param packages 包名数组
     */
    inner class FromPackageRules internal constructor(private val packages: MutableList<KClassRulesData.PackageRulesData>) {

        /**
         * 设置包名绝对匹配
         *
         * 例如有如下包名 ↓
         *
         * com.demo.test.a
         *
         * com.demo.test.a.b
         *
         * com.demo.test.active
         *
         * 若包名条件为 "com.demo.test.a" 则绝对匹配仅能匹配到第一个
         *
         * 相反地 - 不设置以上示例会全部匹配
         */
        fun absolute() = packages.takeIf { it.isNotEmpty() }?.forEach { it.isAbsolute = true }
    }

    /**
     * 类名匹配条件实现类
     * @param name 类名匹配实例
     */
    inner class ClassNameRules internal constructor(private val name: KClassRulesData.NameRulesData) {

        /**
         * 设置类名可选
         *
         * 例如有如下类名 ↓
         *
         * com.demo.Test (fullName) / Test (simpleName)
         *
         * defpackage.a (fullName) / a (simpleName)
         *
         * 这两个类名都是同一个类 - 但是在有些版本中被混淆有些版本没有
         *
         * 此时可设置类名为 "com.demo.Test" (fullName) / "Test" (simpleName)
         *
         * 这样就可在完全匹配类名情况下使用类名而忽略其它查找条件 - 否则忽略此条件继续使用其它查找条件
         */
        fun optional() {
            name.isOptional = true
        }
    }

    /**
     * 设置 [KClass] 满足的 [KCallable] 条件
     * @param initiate 条件方法体
     * @return [KCallableRules]
     */
    inline fun callable(initiate: KCallableRules.() -> Unit = {}) = KBaseRules.createCallableRules(this).apply(initiate).build()

    /**
     * 设置 [KClass] 满足的 [KProperty] 条件
     * @param initiate 条件方法体
     * @return [KCallableRules]
     */
    inline fun property(initiate: KPropertyRules.() -> Unit = {}) = KBaseRules.createPropertyRules(this).apply(initiate).build()

    /**
     * 设置 [KClass] 满足的 [KFunction] 条件
     * @param initiate 条件方法体
     * @return [KCallableRules]
     */
    inline fun function(initiate: KFunctionRules.() -> Unit = {}) = KBaseRules.createFunctionRules(this).apply(initiate).build()

    /**
     * 设置 [KClass] 满足的 Constructor[KFunction] 条件
     * @param initiate 查找方法体
     * @return [KCallableRules]
     */
    inline fun constructor(initiate: KConstructorRules.() -> Unit = {}) = KBaseRules.createConstructorRules(this).apply(initiate).build()

    /**
     * 得到 [KClass] 或一组 [KClass]
     * @return [MutableList]<[KClass]>
     * @throws NoClassDefFoundError 如果找不到 [KClass]
     */
    private val result get() = KReflectionTool.findClasses(classSet, rulesData)

    /**
     * 设置实例
     * @param classes 当前找到的 [KClass] 数组
     */
    private fun setInstance(classes: MutableList<KClass<*>>) {
        classInstances.clear()
        classes.takeIf { it.isNotEmpty() }?.forEach { classInstances.add(it) }
    }

    override fun build() = runCatching {
        if (classSet != null) {
            /** 开始任务 */
            fun startProcess() {
                runBlocking {
                    setInstance(result)
                }.result { ms -> classInstances.takeIf { it.isNotEmpty() }?.forEach { debugMsg(msg = "Find Class [$it] takes ${ms}ms") } }
            }
            Result().also {
                startProcess()
            }
        } else Result(isNotFound = true, Throwable(LOADERSET_IS_NULL))
    }.getOrElse { e -> Result(isNotFound = true, e)}

    /**
     * [KClass] 查找结果实现类
     * @param isNotFound 是否没有找到 [KClass] - 默认否
     * @param throwable 错误信息
     */
    inner class Result internal constructor(
        internal var isNotFound: Boolean = false,
        internal var throwable: Throwable? = null
    ) : BaseResult {

        /** 异常结果重新回调方法体 */
        internal var noClassDefFoundErrorCallback: (() -> Unit)? = null

        /**
         * 创建监听结果事件方法体
         * @param initiate 方法体
         * @return [Result] 可继续向下监听
         */
        inline fun result(initiate: Result.() -> Unit) = apply(initiate)

        /**
         * 得到 [KClass] 本身
         *
         * - 若有多个 [KClass] 结果只会返回第一个
         *
         * - 在查找条件找不到任何结果的时候将返回 null
         *
         * @return [KClass] or null
         */
        fun get() = all().takeIf { it.isNotEmpty() }?.first()

        /**
         * 得到 [KClass] 本身数组
         *
         * - 返回全部查找条件匹配的多个 [KClass] 实例
         *
         * - 在查找条件找不到任何结果的时候将返回空的 [MutableList]
         *
         * @return [MutableList]<[KClass]>
         */
        fun all() = classInstances

        /**
         * 得到 [KClass] 本身数组 (依次遍历)
         *
         * - 回调全部查找条件匹配的多个 [KClass] 实例
         *
         * - 在查找条件找不到任何结果的时候将不会执行
         *
         * @param result 回调每个结果
         * @return [Result] 可继续向下监听
         */
        fun all(result: (KClass<*>) -> Unit): Result {
            all().takeIf { it.isNotEmpty() }?.forEach(result)
            return this
        }

        /**
         * 监听找不到 [KClass] 时
         * @param result 回调错误
         * @return [Result] 可继续向下监听
         */
        fun onNoClassDefFoundError(result: (Throwable) -> Unit): Result {
            noClassDefFoundErrorCallback = { if (isNotFound) result(throwable ?: Throwable("Initialization Error")) }
            noClassDefFoundErrorCallback?.invoke()
            return this
        }

        /**
         * 忽略异常并停止打印任何错误日志
         *
         * - 此时若要监听异常结果 - 你需要手动实现 [onNoClassDefFoundError] 方法
         * @return [Result] 可继续向下监听
         */
        fun ignored(): Result {
            isIgnoreErrorLogs = true
            return this
        }
    }
}