import{_ as s,o as a,c as e,a as n}from"./app-8qBaMd0v.js";const o={},l=n(`<h1 id="kcurrentclass-class" tabindex="-1"><a class="header-anchor" href="#kcurrentclass-class" aria-hidden="true">#</a> KCurrentClass <span class="symbol">- class</span></h1><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">class</span><span style="color:#ADBAC7;"> </span><span style="color:#F69D50;">KCurrentClass</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">internal</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">constructor</span><span style="color:#ADBAC7;">(</span><span style="color:#F47067;">private</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> classSet: </span><span style="color:#F69D50;">KClass</span><span style="color:#ADBAC7;">&lt;*&gt;, </span><span style="color:#F47067;">internal</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> instance: </span><span style="color:#F69D50;">Any</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>当前实例的类操作对象。</p></blockquote><h2 id="name-field" tabindex="-1"><a class="header-anchor" href="#name-field" aria-hidden="true">#</a> name <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> name: </span><span style="color:#F69D50;">String</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获得当前 <code>classSet</code> 的 <code>KClass.name</code>。</p></blockquote><h2 id="simplename-field" tabindex="-1"><a class="header-anchor" href="#simplename-field" aria-hidden="true">#</a> simpleName <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> simpleName: </span><span style="color:#F69D50;">String</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获得当前 <code>classSet</code> 的 <code>KClass.simpleNameOrJvm</code>。</p></blockquote><h2 id="generic-method" tabindex="-1"><a class="header-anchor" href="#generic-method" aria-hidden="true">#</a> generic <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">generic</span><span style="color:#ADBAC7;">(): </span><span style="color:#F69D50;">KGenericClass</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获得当前实例中的泛型操作对象。</p></blockquote><h2 id="generic-method-1" tabindex="-1"><a class="header-anchor" href="#generic-method-1" aria-hidden="true">#</a> generic <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">inline</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">generic</span><span style="color:#ADBAC7;">(</span><span style="color:#F47067;">vararg</span><span style="color:#ADBAC7;"> params: </span><span style="color:#F69D50;">Any</span><span style="color:#ADBAC7;">,initiate: </span><span style="color:#F69D50;">KTypeBuildConditions</span><span style="color:#ADBAC7;">): </span><span style="color:#F69D50;">KGenericClass</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>使用指定的泛型类型，获得当前实例中的泛型操作对象。</p></blockquote><h2 id="genericsuper-method" tabindex="-1"><a class="header-anchor" href="#genericsuper-method" aria-hidden="true">#</a> genericSuper <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">genericSuper</span><span style="color:#ADBAC7;">(): </span><span style="color:#F69D50;">KGenericClass</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获得当前实例中的泛型父类操作对象。</p></blockquote><p>如果当前实例不存在泛型将返回 <code>null</code>。</p><h2 id="genericsuper-method-1" tabindex="-1"><a class="header-anchor" href="#genericsuper-method-1" aria-hidden="true">#</a> genericSuper <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">inline</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">genericSuper</span><span style="color:#ADBAC7;">(initiate: </span><span style="color:#F69D50;">KClassConditions</span><span style="color:#ADBAC7;">): </span><span style="color:#F69D50;">KGenericClass</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获得当前实例中的泛型父类并筛选出来。</p></blockquote><p>如果当前实例不存在泛型将返回 <code>null</code>。</p><h2 id="superclass-method" tabindex="-1"><a class="header-anchor" href="#superclass-method" aria-hidden="true">#</a> superClass <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">superClass</span><span style="color:#ADBAC7;">(): </span><span style="color:#F69D50;">SuperClass</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>调用父类实例。</p></blockquote><h2 id="property-method" tabindex="-1"><a class="header-anchor" href="#property-method" aria-hidden="true">#</a> property <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">inline</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">property</span><span style="color:#ADBAC7;">(initiate: </span><span style="color:#F69D50;">KPropertyConditions</span><span style="color:#ADBAC7;">): </span><span style="color:#F69D50;">KPropertyFinder</span><span style="color:#ADBAC7;">.Result.Instance</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>调用当前实例中的变量/属性。</p></blockquote><h2 id="function-method" tabindex="-1"><a class="header-anchor" href="#function-method" aria-hidden="true">#</a> function <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">inline</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">function</span><span style="color:#ADBAC7;">(initiate: </span><span style="color:#F69D50;">KFunctionConditions</span><span style="color:#ADBAC7;">): </span><span style="color:#F69D50;">KFunctionFinder</span><span style="color:#ADBAC7;">.Result.Instance</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>调用当前实例中的方法/函数。</p></blockquote><h2 id="superclass-class" tabindex="-1"><a class="header-anchor" href="#superclass-class" aria-hidden="true">#</a> SuperClass <span class="symbol">- class</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">inner</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">class</span><span style="color:#ADBAC7;"> </span><span style="color:#F69D50;">SuperClass</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">internal</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">constructor</span><span style="color:#ADBAC7;">(</span><span style="color:#F47067;">private</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> superClassSet: </span><span style="color:#F69D50;">KClass</span><span style="color:#ADBAC7;">&lt;*&gt;)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>当前类的父类实例的类操作对象。</p></blockquote><h3 id="name-field-1" tabindex="-1"><a class="header-anchor" href="#name-field-1" aria-hidden="true">#</a> name <span class="symbol">- field</span></h3><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> name: </span><span style="color:#F69D50;">String</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获得当前 <code>classSet</code> 中父类的 <code>KClass.name</code>。</p></blockquote><h3 id="simplename-field-1" tabindex="-1"><a class="header-anchor" href="#simplename-field-1" aria-hidden="true">#</a> simpleName <span class="symbol">- field</span></h3><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> simpleName: </span><span style="color:#F69D50;">String</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获得当前 <code>classSet</code> 中父类的 <code>KClass.simpleNameOrJvm</code>。</p></blockquote><h2 id="generic-method-2" tabindex="-1"><a class="header-anchor" href="#generic-method-2" aria-hidden="true">#</a> generic <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">generic</span><span style="color:#ADBAC7;">(): </span><span style="color:#F69D50;">KGenericClass</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获得当前实例中父类的泛型操作对象。</p></blockquote><h2 id="generic-method-3" tabindex="-1"><a class="header-anchor" href="#generic-method-3" aria-hidden="true">#</a> generic <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">inline</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">generic</span><span style="color:#ADBAC7;">(</span><span style="color:#F47067;">vararg</span><span style="color:#ADBAC7;"> params: </span><span style="color:#F69D50;">Any</span><span style="color:#ADBAC7;">,initiate: </span><span style="color:#F69D50;">KTypeBuildConditions</span><span style="color:#ADBAC7;">): </span><span style="color:#F69D50;">KGenericClass</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>使用指定的泛型类型，获得当前实例中父类的泛型操作对象。</p></blockquote><h2 id="genericsuper-method-2" tabindex="-1"><a class="header-anchor" href="#genericsuper-method-2" aria-hidden="true">#</a> genericSuper <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">genericSuper</span><span style="color:#ADBAC7;">(): </span><span style="color:#F69D50;">KGenericClass</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获得当前实例父类中的泛型父类操作对象。</p></blockquote><p>如果当前实例父类不存在泛型将返回 <code>null</code>。</p><h2 id="genericsuper-method-3" tabindex="-1"><a class="header-anchor" href="#genericsuper-method-3" aria-hidden="true">#</a> genericSuper <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">inline</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">genericSuper</span><span style="color:#ADBAC7;">(initiate: </span><span style="color:#F69D50;">KClassConditions</span><span style="color:#ADBAC7;">): </span><span style="color:#F69D50;">KGenericClass</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获得当前实例中父类的泛型父类并筛选出来。</p></blockquote><p>如果当前实例父类不存在泛型将返回 <code>null</code>。</p><h2 id="property-method-1" tabindex="-1"><a class="header-anchor" href="#property-method-1" aria-hidden="true">#</a> property <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">inline</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">property</span><span style="color:#ADBAC7;">(initiate: </span><span style="color:#F69D50;">KPropertyConditions</span><span style="color:#ADBAC7;">): </span><span style="color:#F69D50;">KPropertyFinder</span><span style="color:#ADBAC7;">.Result.Instance</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>调用当前实例中父类的变量/属性。</p></blockquote><h2 id="function-method-1" tabindex="-1"><a class="header-anchor" href="#function-method-1" aria-hidden="true">#</a> function <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">inline</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">function</span><span style="color:#ADBAC7;">(initiate: </span><span style="color:#F69D50;">KFunctionConditions</span><span style="color:#ADBAC7;">): </span><span style="color:#F69D50;">KFunctionFinder</span><span style="color:#ADBAC7;">.Result.Instance</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>调用当前实例中父类的方法/函数。</p></blockquote>`,118),p=[l];function c(t,r){return a(),e("div",null,p)}const i=s(o,[["render",c],["__file","KCurrentClass.html.vue"]]);export{i as default};
