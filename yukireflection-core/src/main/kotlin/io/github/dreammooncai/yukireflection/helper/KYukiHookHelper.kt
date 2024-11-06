@file:Suppress("INVISIBLE_REFERENCE","INVISIBLE_MEMBER")
package io.github.dreammooncai.yukireflection.helper

import io.github.dreammooncai.yukireflection.factory.function
import io.github.dreammooncai.yukireflection.type.kotlin.YukiHookHelperKClass
import java.lang.reflect.Member
import com.highcapable.yukihookapi.hook.core.api.helper.YukiHookHelper

object KYukiHookHelper {
    /**
     * 获取当前 [Member] 是否被 Hook
     *
     * - 此方法仅在 Hook Api 下有效
     * @param member 实例
     * @return [Boolean]
     */
    fun isMemberHooked(member: Member?): Boolean {
        if(YukiHookHelperKClass == null){
            return false
        }
        return YukiHookHelper.isMemberHooked(member)
    }

    /**
     * 执行原始 [Member]
     *
     * 未进行 Hook 的 [Member]
     *
     * - 此方法仅在 Hook Api 下有效
     * @param member 实例
     * @param args 参数实例
     * @return [Any] or null
     * @throws IllegalStateException 如果 [Member] 参数个数不正确
     */
    fun invokeOriginalMember(member: Member?, instance: Any?, args: Array<out Any?>?): Any? {
        if(YukiHookHelperKClass == null){
            return null
        }
        return YukiHookHelper.invokeOriginalMember(member,instance,args)
    }

}