# Changelog

> The version update history of `KYukiReflection` is recorded here.

::: danger

We will only maintain the latest API version, if you are using an outdate API version, you voluntarily renounce any possibility of maintenance.

:::

::: warning

To avoid translation time consumption, Changelog will use **Google Translation** from **Chinese** to **English**, please refer to the original text for actual reference.

Time zone of version release date: **UTC+8**

:::

### 1.0.2 | 2024.6.29 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- For real-world use cases, we've added methods such as `singletonInstance` and `companionSingletonInstance` to quickly fetch singleton and object class instances
- When using the `bindProperty()` method to delegate binding, if the bound property `this` is the same as the `Class` type, you don't need to pass `thisRef`, and the `this` in the domain will be automatically used
- When using `Kotlin` reflection lookup to get a function, property that is an object class or a singleton class member, you don't need to pass in `thisRef` and the relevant example will be used automatically
- Added a signature lookup method for the `Kotlin` class with metadata exceptions, and only read the metadata of the `Class` to find the corresponding data to avoid exceptions
- Adding an `original` function to the result instance of the lookup allows calls to be made about things that don't have a `Hook`, although this only works if there's a `HookAPI`
- Add the `function` lookup result instance or the `property` lookup result instance or lookup instance of `getter` or `setter` can be directly obtained in the lookup instance
- Added `KFunction` and `KProperty` to convert them into search result instances by using the `instance` method
- Optimized the degree of reflection finding and matching adaptation of `Kotlin`
- For more information, please optimize the details in use

### 1.0.0 | 2023.01.26 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- The first version is submitted to Maven