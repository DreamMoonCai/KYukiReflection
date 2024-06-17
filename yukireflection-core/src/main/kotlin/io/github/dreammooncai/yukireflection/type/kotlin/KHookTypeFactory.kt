@file:Suppress("unused", "KDocUnresolvedReference", "FunctionName")
package io.github.dreammooncai.yukireflection.type.kotlin

import io.github.dreammooncai.yukireflection.factory.toKClassOrNull
import kotlin.reflect.KClass

/**
 * 获得 [com.highcapable.yukihookapi.hook.core.api.helper.YukiHookHelper] 类型
 * @return [KClass]<[com.highcapable.yukihookapi.hook.core.api.helper.YukiHookHelper]>
 */
val YukiHookHelperKClass get() = "com.highcapable.yukihookapi.hook.core.api.helper.YukiHookHelper".toKClassOrNull()