---
home: true
title: KYukiReflection-首页
heroImage: /images/logo.png
actions:
  - text: 快速上手
    link: /zh-cn/guide/home
    type: primary
  - text: 更新日志
    link: /zh-cn/about/changelog
    type: secondary
features:
  - title: 符合直觉
    details: KYukiReflection 来自 YukiReflection 在设计逻辑上遵循原来的大部分内容，使得 KYukiReflection 的体验与 YukiReflection 使用体验一致。
  - title: 泛型细化
    details: 针对于 YukiReflection 支持比较薄弱的泛型相关，此套API完美适配了更多可能情况细化泛型体验。
  - title: 条件多元
    details: 对于 Kotlin 的支持不旨在纸上谈兵而是全方位拓展，支持所有关键字的修饰符条件，支持各种泛型，各种定制化参数，对可空特性和方差特性条件检索支持等，提供更细致的条件体验。 
footer: Apache-2.0 License | Copyright (C) 2019-2023 HighCapable
---

### 来吧！让反射也变得诗情画意

```java
public class World {

    public static class A<T>{}

    private void sayHello(A<String> content) {
        System.out.println("Hello " + content + "!");
    }
}
```

```kotlin
val newWorld = World()
val newWorldA = World.A<String>()
kclassOf<World>().method {
    name = "sayHello"
    param(VagueKotlin.generic(StringKClass))
    returnType = UnitKClass
}.get(newWorld).call(newWorldA)
```