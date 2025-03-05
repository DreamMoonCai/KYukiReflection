import{_ as s,o as e,c as a,a as o}from"./app-8qBaMd0v.js";const n={},p=o(`<h1 id="kpropertyrules-class" tabindex="-1"><a class="header-anchor" href="#kpropertyrules-class" aria-hidden="true">#</a> KPropertyRules <span class="symbol">- class</span></h1><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">class</span><span style="color:#ADBAC7;"> </span><span style="color:#F69D50;">KPropertyRules</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">internal</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">constructor</span><span style="color:#ADBAC7;">(</span><span style="color:#F47067;">private</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> rulesData: </span><span style="color:#F69D50;">KPropertyRulesData</span><span style="color:#ADBAC7;">) : </span><span style="color:#F69D50;">KBaseRules</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p><code>KProperty</code> 查找条件实现类。</p></blockquote><h2 id="name-field" tabindex="-1"><a class="header-anchor" href="#name-field" aria-hidden="true">#</a> name <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">var</span><span style="color:#ADBAC7;"> name: </span><span style="color:#F69D50;">String</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KProperty</code> 名称。</p></blockquote><h2 id="type-field" tabindex="-1"><a class="header-anchor" href="#type-field" aria-hidden="true">#</a> type <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">var</span><span style="color:#ADBAC7;"> type: </span><span style="color:#F69D50;">Any</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KProperty</code> 类型。</p></blockquote><p>可不填写类型。</p><h2 id="modifiers-method" tabindex="-1"><a class="header-anchor" href="#modifiers-method" aria-hidden="true">#</a> modifiers <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">modifiers</span><span style="color:#ADBAC7;">(conditions: </span><span style="color:#F69D50;">KModifierConditions</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KProperty</code> 标识符筛选条件。</p></blockquote><p>可不设置筛选条件。</p><h2 id="name-method" tabindex="-1"><a class="header-anchor" href="#name-method" aria-hidden="true">#</a> name <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">name</span><span style="color:#ADBAC7;">(conditions: </span><span style="color:#F69D50;">KNameConditions</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KProperty</code> 名称条件。</p></blockquote><h2 id="type-method" tabindex="-1"><a class="header-anchor" href="#type-method" aria-hidden="true">#</a> type <span class="symbol">- method</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">type</span><span style="color:#ADBAC7;">(conditions: </span><span style="color:#F69D50;">KTypeConditions</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置 <code>KProperty</code> 类型条件。</p></blockquote><p>可不填写类型。</p>`,39),l=[p];function t(c,r){return e(),a("div",null,l)}const i=s(n,[["render",t],["__file","KPropertyRules.html.vue"]]);export{i as default};
