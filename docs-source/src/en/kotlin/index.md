---
home: true
title: KYukiReflection-Home
heroImage: /images/logo.png
actions:
  - text: Get Started
    link: /en/kotlin/guide/home
    type: primary
  - text: Changelog
    link: /en/kotlin/about/changelog
    type: secondary
features:
  - title: Intuitive
    details: KYukiReflection comes from YukiReflection and follows most of the original content in terms of design logic, making the KYukiReflection experience consistent with the YukiReflection experience.
  - title: Generic Refinement
    details: For YukiReflection's relatively weak support for generics, this set of APIs perfectly adapts to more possible situations and refines the generic experience.
  - title: Multiple Conditions
    details: The support for Kotlin is not meant to be discussed on paper but to be expanded in an all-round way. It supports the modifier conditions of all keywords, supports various generics, various customized parameters, and provides more detailed support for retrieval of nullable characteristics and variance characteristics. conditional experience. 
footer: Apache-2.0 License | Copyright (C) 2019-2023 HighCapable
---

### Bring it on! Let reflection become poetic and picturesque

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