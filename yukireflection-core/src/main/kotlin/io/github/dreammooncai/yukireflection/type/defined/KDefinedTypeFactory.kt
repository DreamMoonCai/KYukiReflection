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
 * This file is created by fankes on 2022/4/3.
 * This file is modified by fankes on 2023/1/21.
 */
package io.github.dreammooncai.yukireflection.type.defined

import io.github.dreammooncai.yukireflection.bean.KGenericClass
import io.github.dreammooncai.yukireflection.bean.KVariousClass
import io.github.dreammooncai.yukireflection.factory.kclassOf
import io.github.dreammooncai.yukireflection.factory.toKClass
import io.github.dreammooncai.yukireflection.factory.toKClassOrNull
import io.github.dreammooncai.yukireflection.factory.type
import io.github.dreammooncai.yukireflection.finder.base.KBaseFinder
import kotlin.reflect.*

/**
 * 未定义类型实例
 *
 * 请使用 [UndefinedKotlin] 来调用它
 */
internal class UndefinedClass private constructor()

/**
 * 模糊类型实例
 *
 * 请使用 [VagueKotlin] 来调用它
 */
class VagueClass private constructor()

/**
 * 得到未定义类型
 * @return [KClass]<[UndefinedClass]>
 */
@Suppress("UNCHECKED_CAST")
internal val UndefinedKotlin get() = ("com.highcapable.yukireflection.type.defined.UndefinedType".toKClassOrNull() ?: kclassOf<UndefinedClass>()) as KClass<UndefinedClass>

/**
 * 得到模糊类型
 * @return [KClass]<[VagueClass]>
 */
@Suppress("UNCHECKED_CAST")
val VagueKotlin get() = ("com.highcapable.yukireflection.type.defined.VagueKotlin".toKClassOrNull() ?: kclassOf<VagueClass>()) as KClass<VagueClass>

/**
 * 获取模糊根的泛型对象
 *
 * ```kotlin
 *
 * class ABC{
 *     val temp:bbb<Int>
 * }
 *
 * val vague = ABC::class.property { type = VagueKotlin.generic(Int::class) }
 *
 * vague --> val temp:bbb<Int>
 *
 * ```
 *
 * @param params 泛型参数
 */
fun KClass<VagueClass>.generic(vararg params:Any): ArrayList<Any> {
    val pm = arrayListOf<Any>()
    params.forEach {
        when(it){
            is KClassifier -> pm.add(KTypeProjection(KVariance.INVARIANT, it.type))
            is KTypeProjection,is KVariance -> pm.add(it)
            is KGenericClass -> pm.add(KTypeProjection(KVariance.INVARIANT, it.type))
            is KType -> pm.add(KTypeProjection(KVariance.INVARIANT, it))
            is KParameter -> pm.add(KTypeProjection(KVariance.INVARIANT, it.type))
            is String -> pm.add(KTypeProjection(KVariance.INVARIANT, it.toKClass().type))
            is KVariousClass -> pm.add(KTypeProjection(KVariance.INVARIANT,it.get().type))
            else -> runCatching { KBaseFinder.checkArrayGenerics(it) }.getOrNull()?.let { pm.add(it) }
        }
    }
    return pm
}