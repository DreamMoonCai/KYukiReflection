# 更新日志

> 这里记录了 `KYukiReflection` 的版本更新历史。

::: danger

我们只会对最新的 API 版本进行维护，若你正在使用过时的 API 版本则代表你自愿放弃一切维护的可能性。

:::

### 1.0.2 | 2024.6.29 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- 针对真实的使用场景我们增加了诸如 `singletonInstance` 和 `companionSingletonInstance` 这样的方法以快速获取单例和对象类实例
- 进行 `bindProperty()` 方法委托绑定时如果被绑定的属性 `this` 与 `Class` 类型一致则不需要传入 `thisRef` ，将自动使用域内的`this`
- 当使用 `Kotlin` 的反射查找获得的 函数、属性 为对象类或单例类成员则不需要传入 `thisRef` 将自动使用相关示例
- 为元数据异常的 `Kotlin` 类增加签名查找方式，通过只读取 `Class` 的元数据来查找对应数据避免异常发生
- 为查找的结果实例增加 `original` 函数使得允许调用没有进行 `Hook` 的相关内容，尽管这只在有 `HookAPI` 时才生效
- 新增 `function` 查找结果实例或查找实例中可以直接获取 `getter`、`setter` 的 `property` 查找结果实例或查找实例
- 新增 `KFunction`、`KProperty` 通过 `instance` 方法转换为查找结果实例
- 优化 `Kotlin` 的反射查找和匹配适应程度
- 更多内容请细节优化在使用中体现

### 1.0.1 | 2024.06.18 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 首个版本提交至 Maven