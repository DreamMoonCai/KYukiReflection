@file:Suppress("unused", "KDocUnresolvedReference", "FunctionName")
package com.dream.yukireflection.type.kotlin

import com.dream.yukireflection.factory.toKClassOrNull
import kotlin.reflect.KClass

/**
 * 获得 [com.highcapable.yukihookapi.hook.core.api.helper.YukiHookHelper] 类型
 * @return [KClass]<[com.highcapable.yukihookapi.hook.core.api.helper.YukiHookHelper]>
 */
val YukiHookHelperKClass get() = "com.highcapable.yukihookapi.hook.core.api.helper.YukiHookHelper".toKClassOrNull()