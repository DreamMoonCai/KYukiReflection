import{_ as s,o as e,c as a,a as n}from"./app-8qBaMd0v.js";const o={},l=n(`<h1 id="basefinder-class" tabindex="-1"><a class="header-anchor" href="#basefinder-class" aria-hidden="true">#</a> BaseFinder <span class="symbol">- class</span></h1><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">abstract</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">class</span><span style="color:#ADBAC7;"> </span><span style="color:#F69D50;">BaseFinder</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>这是 <code>KClass</code> 与 <code>KCallable</code> 查找类功能的基本类实现。</p></blockquote><h2 id="basefinder-indextypecondition-class" tabindex="-1"><a class="header-anchor" href="#basefinder-indextypecondition-class" aria-hidden="true">#</a> BaseFinder.IndexTypeCondition <span class="symbol">- class</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">inner</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">class</span><span style="color:#ADBAC7;"> </span><span style="color:#F69D50;">IndexTypeCondition</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">internal</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">constructor</span><span style="color:#ADBAC7;">(</span><span style="color:#F47067;">private</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> type: </span><span style="color:#F69D50;">IndexConfigType</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>字节码下标筛选实现类。</p></blockquote><h3 id="index-method" tabindex="-1"><a class="header-anchor" href="#index-method" aria-hidden="true">#</a> index <span class="symbol">- method</span></h3><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">index</span><span style="color:#ADBAC7;">(num: </span><span style="color:#F69D50;">Int</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置下标。</p></blockquote><p>若 <code>index</code> 小于零则为倒序，此时可以使用 <code>IndexTypeConditionSort.reverse</code> 方法实现。</p><p>可使用 <code>IndexTypeConditionSort.first</code> 和 <code>IndexTypeConditionSort.last</code> 设置首位和末位筛选条件。</p><h3 id="index-method-1" tabindex="-1"><a class="header-anchor" href="#index-method-1" aria-hidden="true">#</a> index <span class="symbol">- method</span></h3><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">index</span><span style="color:#ADBAC7;">(): </span><span style="color:#F69D50;">IndexTypeConditionSort</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>得到下标。</p></blockquote><h3 id="indextypeconditionsort-class" tabindex="-1"><a class="header-anchor" href="#indextypeconditionsort-class" aria-hidden="true">#</a> IndexTypeConditionSort <span class="symbol">- class</span></h3><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">inner</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">class</span><span style="color:#ADBAC7;"> </span><span style="color:#F69D50;">IndexTypeConditionSort</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">internal</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">constructor</span><span style="color:#ADBAC7;">()</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>字节码下标排序实现类。</p></blockquote><h4 id="first-method" tabindex="-1"><a class="header-anchor" href="#first-method" aria-hidden="true">#</a> first <span class="symbol">- method</span></h4><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">first</span><span style="color:#ADBAC7;">()</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置满足条件的第一个。</p></blockquote><h4 id="last-method" tabindex="-1"><a class="header-anchor" href="#last-method" aria-hidden="true">#</a> last <span class="symbol">- method</span></h4><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">last</span><span style="color:#ADBAC7;">()</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置满足条件的最后一个。</p></blockquote><h4 id="reverse-method" tabindex="-1"><a class="header-anchor" href="#reverse-method" aria-hidden="true">#</a> reverse <span class="symbol">- method</span></h4><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">fun</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">reverse</span><span style="color:#ADBAC7;">(num: </span><span style="color:#F69D50;">Int</span><span style="color:#ADBAC7;">)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>设置倒序下标。</p></blockquote>`,50),p=[l];function t(c,d){return e(),a("div",null,p)}const i=s(o,[["render",t],["__file","KBaseFinder.html.vue"]]);export{i as default};
