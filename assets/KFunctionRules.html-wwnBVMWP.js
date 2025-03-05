import{_ as a,o as s,c as o,a as n}from"./app-8qBaMd0v.js";const e={},p=n(`<h1 id="kfunctionrules-class" tabindex="-1"><a class="header-anchor" href="#kfunctionrules-class" aria-hidden="true">#</a> KFunctionRules <span class="symbol">- class</span></h1><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">class</span><span style="color:#ADBAC7;"> </span><span style="color:#F69D50;">KFunctionRules</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">internal</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">constructor</span><span style="color:#ADBAC7;">(</span><span style="color:#F47067;">private</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> rulesData: </span><span style="color:#F69D50;">KFunctionRulesData</span><span style="color:#ADBAC7;">) : </span><span style="color:#F69D50;">KBaseRules</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p><code>KFunction</code> 查找条件实现类。</p></blockquote><h2 id="name-field" tabindex="-1"><a class="header-anchor" href="#name-field" aria-hidden="true">#</a> name <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">var</span><span style="color:#ADBAC7;"> name: </span><span style="color:#F69D50;">String</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KFunction</code> 名称。</p></blockquote><h2 id="paramcount-field" tabindex="-1"><a class="header-anchor" href="#paramcount-field" aria-hidden="true">#</a> paramCount <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">var</span><span style="color:#ADBAC7;"> paramCount: </span><span style="color:#F69D50;">Int</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KFunction</code> 参数个数。</p></blockquote><p>你可以不使用 <code>param</code> 指定参数类型而是仅使用此变量指定参数个数。</p><p>若参数个数小于零则忽略并使用 <code>param</code>。</p><h2 id="returntype-field" tabindex="-1"><a class="header-anchor" href="#returntype-field" aria-hidden="true">#</a> returnType <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">var</span><span style="color:#ADBAC7;"> returnType: </span><span style="color:#F69D50;">Any</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KFunction</code> 返回值。</p></blockquote><p>可不填写返回值。</p><h2 id="modifiers-method" tabindex="-1"><a class="header-anchor" href="#modifiers-method" aria-hidden="true">#</a> modifiers <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">modifiers</span><span style="color:#ADBAC7;">(conditions: </span><span style="color:#F69D50;">KModifierConditions</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KFunction</code> 标识符筛选条件。</p></blockquote><p>可不设置筛选条件。</p><h2 id="emptyparam-method" tabindex="-1"><a class="header-anchor" href="#emptyparam-method" aria-hidden="true">#</a> emptyParam <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">emptyParam</span><span style="color:#ADBAC7;">()</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KFunction</code> 空参数、无参数。</p></blockquote><h2 id="param-method" tabindex="-1"><a class="header-anchor" href="#param-method" aria-hidden="true">#</a> param <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">param</span><span style="color:#ADBAC7;">(</span><span style="color:#F47067;">vararg</span><span style="color:#ADBAC7;"> paramType: </span><span style="color:#F69D50;">Any</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KFunction</code> 参数。</p></blockquote><p>如果同时使用了 <code>paramCount</code> 则 <code>paramType</code> 的数量必须与 <code>paramCount</code> 完全匹配。</p><p>如果 <code>KFunction</code> 中存在一些无意义又很长的类型，你可以使用 <code>VagueKotlin</code> 来替代它。</p><div class="custom-container danger"><p class="custom-container-title">特别注意</p><p>无参 <strong>Method</strong> 请使用 <strong>emptyParam</strong> 设置查找条件。</p><p>有参 <strong>Method</strong> 必须使用此方法设定参数或使用 <strong>paramCount</strong> 指定个数。</p></div><h2 id="param-method-1" tabindex="-1"><a class="header-anchor" href="#param-method-1" aria-hidden="true">#</a> param <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">param</span><span style="color:#ADBAC7;">(conditions: </span><span style="color:#F69D50;">KParameterConditions</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KFunction</code> 参数条件。</p></blockquote><div class="custom-container danger"><p class="custom-container-title">特别注意</p><p>无参 <strong>Method</strong> 请使用 <strong>emptyParam</strong> 设置查找条件。</p><p>有参 <strong>Method</strong> 必须使用此方法设定参数或使用 <strong>paramCount</strong> 指定个数。</p></div><h2 id="paramname-method" tabindex="-1"><a class="header-anchor" href="#paramname-method" aria-hidden="true">#</a> paramName <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">paramName</span><span style="color:#ADBAC7;">(</span><span style="color:#F47067;">vararg</span><span style="color:#ADBAC7;"> paramName: </span><span style="color:#F69D50;">String</span><span style="color:#ADBAC7;">): </span><span style="color:#F69D50;">Unit</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>Constructor KFunction</code> 参数名称。</p></blockquote><p>如果 <code>Constructor KFunction</code> 中存在一些不太清楚的参数名称，你可以使用 <a href="../../../type/defined/KDefinedTypeFactory#vaguekotlin-field">VagueKotlin</a>.name 或者 空字符串 或者 &quot;null&quot; 来替代它。</p><h2 id="paramname-method-1" tabindex="-1"><a class="header-anchor" href="#paramname-method-1" aria-hidden="true">#</a> paramName <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">paramName</span><span style="color:#ADBAC7;">(conditions: </span><span style="color:#F69D50;">KNamesConditions</span><span style="color:#ADBAC7;">): </span><span style="color:#F69D50;">Unit</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>Constructor KFunction</code> 参数名称条件。</p></blockquote><h2 id="name-method" tabindex="-1"><a class="header-anchor" href="#name-method" aria-hidden="true">#</a> name <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">name</span><span style="color:#ADBAC7;">(conditions: </span><span style="color:#F69D50;">KNameConditions</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KFunction</code> 名称条件。</p></blockquote><h2 id="paramcount-method" tabindex="-1"><a class="header-anchor" href="#paramcount-method" aria-hidden="true">#</a> paramCount <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">paramCount</span><span style="color:#ADBAC7;">(numRange: </span><span style="color:#F69D50;">IntRange</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KFunction</code> 参数个数范围。</p></blockquote><p>你可以不使用 <code>param</code> 指定参数类型而是仅使用此方法指定参数个数范围。</p><h2 id="paramcount-method-1" tabindex="-1"><a class="header-anchor" href="#paramcount-method-1" aria-hidden="true">#</a> paramCount <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">paramCount</span><span style="color:#ADBAC7;">(conditions: </span><span style="color:#F69D50;">KCountConditions</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KFunction</code> 参数个数条件。</p></blockquote><p>你可以不使用 <code>param</code> 指定参数类型而是仅使用此方法指定参数个数条件。</p><h2 id="returntype-method" tabindex="-1"><a class="header-anchor" href="#returntype-method" aria-hidden="true">#</a> returnType <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">returnType</span><span style="color:#ADBAC7;">(conditions: </span><span style="color:#F69D50;">KTypeConditions</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KFunction</code> 返回值条件。</p></blockquote><p>可不填写返回值。</p>`,96),t=[p];function c(l,r){return s(),o("div",null,t)}const i=a(e,[["render",c],["__file","KFunctionRules.html.vue"]]);export{i as default};
