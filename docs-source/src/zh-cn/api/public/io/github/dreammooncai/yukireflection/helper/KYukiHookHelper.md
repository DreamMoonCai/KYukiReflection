---
pageClass: code-page
---

# KYukiHookHelper <span class="symbol">- object</span>

```kotlin:no-line-numbers
object KYukiHookHelper
```

**变更记录**

`v1.0.0` `新增`

**功能描述**

> 访问YukiHook部分增强功能。

## isMemberHooked <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun isMemberHooked(member: Member?): Boolean
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 获取当前 `Member` 是否被 Hook

::: danger

此方法仅在 Hook Api 下有效

:::

## invokeOriginalMember <span class="symbol">- method</span>

```kotlin:no-line-numbers
fun invokeOriginalMember(member: Member?, instance: Any?, args: Array<out Any?>?): Any?
```

**变更记录**

`v1.0.0` `添加`

**功能描述**

> 执行原始 `Member`
> 未进行 Hook 的 `Member`

::: danger

此方法仅在 Hook Api 下有效

:::