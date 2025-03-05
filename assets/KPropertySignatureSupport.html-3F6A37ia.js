import{_ as s,o as a,c as e,a as n}from"./app-8qBaMd0v.js";const o={},l=n(`<h1 id="kpropertysignaturesupport-class" tabindex="-1"><a class="header-anchor" href="#kpropertysignaturesupport-class" aria-hidden="true">#</a> KPropertySignatureSupport <span class="symbol">- class</span></h1><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">open</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">class</span><span style="color:#ADBAC7;"> </span><span style="color:#F69D50;">KPropertySignatureSupport</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">internal</span><span style="color:#ADBAC7;"> </span><span style="color:#DCBDFB;">constructor</span><span style="color:#ADBAC7;">(</span><span style="color:#F47067;">private</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> declaringClass: </span><span style="color:#F69D50;">KClass</span><span style="color:#ADBAC7;">&lt;*&gt;?, </span><span style="color:#F47067;">private</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> loader: </span><span style="color:#F69D50;">ClassLoader</span><span style="color:#ADBAC7;">?, </span><span style="color:#F47067;">private</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> nameResolver: </span><span style="color:#F69D50;">NameResolver</span><span style="color:#ADBAC7;">, </span><span style="color:#F47067;">private</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> proto: </span><span style="color:#F69D50;">JvmProtoBuf</span><span style="color:#ADBAC7;">.JvmPropertySignature)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>依据签名信息对 <code>KProperty</code> 获取相关信息的支持类</p></blockquote><h2 id="fieldsignaturesupport-class" tabindex="-1"><a class="header-anchor" href="#fieldsignaturesupport-class" aria-hidden="true">#</a> FieldSignatureSupport <span class="symbol">- class</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">inner</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">class</span><span style="color:#ADBAC7;"> </span><span style="color:#F69D50;">FieldSignatureSupport</span><span style="color:#ADBAC7;">(</span><span style="color:#F47067;">private</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> proto: </span><span style="color:#F69D50;">JvmProtoBuf</span><span style="color:#ADBAC7;">.JvmFieldSignature)</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>依据签名信息对 <code>Field</code> 获取相关信息的支持类</p></blockquote><h3 id="name-field" tabindex="-1"><a class="header-anchor" href="#name-field" aria-hidden="true">#</a> name <span class="symbol">- field</span></h3><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> name: </span><span style="color:#F69D50;">String</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获取 <code>Field</code> 字段名。</p></blockquote><h3 id="typedescriptor-field" tabindex="-1"><a class="header-anchor" href="#typedescriptor-field" aria-hidden="true">#</a> typeDescriptor <span class="symbol">- field</span></h3><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> typeDescriptor: </span><span style="color:#F69D50;">String</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获取 <code>Field</code> 字段类型签名。</p></blockquote><h3 id="returntype-field" tabindex="-1"><a class="header-anchor" href="#returntype-field" aria-hidden="true">#</a> returnType <span class="symbol">- field</span></h3><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> returnType: </span><span style="color:#F69D50;">KType</span></span>
<span class="line"></span></code></pre></div><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> returnTypeOrNull: </span><span style="color:#F69D50;">KType</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>依据返回类型签名，获取 <code>Field</code> 参数列表 <code>KType</code>。</p></blockquote><h3 id="member-field" tabindex="-1"><a class="header-anchor" href="#member-field" aria-hidden="true">#</a> member <span class="symbol">- field</span></h3><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> member: </span><span style="color:#F69D50;">Field</span></span>
<span class="line"></span></code></pre></div><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> memberOrNull: </span><span style="color:#F69D50;">Field</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>依据签名，获取 <code>Field</code> 的实例表述对象。</p></blockquote><h3 id="hasname-field" tabindex="-1"><a class="header-anchor" href="#hasname-field" aria-hidden="true">#</a> hasName <span class="symbol">- field</span></h3><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> hasName: </span><span style="color:#F69D50;">Boolean</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>检查字段名是否有效。</p></blockquote><h3 id="hastype-field" tabindex="-1"><a class="header-anchor" href="#hastype-field" aria-hidden="true">#</a> hasType <span class="symbol">- field</span></h3><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> hasType: </span><span style="color:#F69D50;">Boolean</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>检查字段类型是否有效。</p></blockquote><h3 id="hassignature-field" tabindex="-1"><a class="header-anchor" href="#hassignature-field" aria-hidden="true">#</a> hasSignature <span class="symbol">- field</span></h3><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> hasSignature: </span><span style="color:#F69D50;">Boolean</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>检查字段签名是否有效。</p></blockquote><h2 id="getter-field" tabindex="-1"><a class="header-anchor" href="#getter-field" aria-hidden="true">#</a> getter <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> getter: </span><span style="color:#F69D50;">KFunctionSignatureSupport</span></span>
<span class="line"></span></code></pre></div><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> getterOrNull: </span><span style="color:#F69D50;">KFunctionSignatureSupport</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获取Getter函数签名处理支持组件。</p></blockquote><h2 id="setter-field" tabindex="-1"><a class="header-anchor" href="#setter-field" aria-hidden="true">#</a> setter <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> setter: </span><span style="color:#F69D50;">KFunctionSignatureSupport</span></span>
<span class="line"></span></code></pre></div><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> setterOrNull: </span><span style="color:#F69D50;">KFunctionSignatureSupport</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获取Setter函数签名处理支持组件。</p></blockquote><h2 id="field-field" tabindex="-1"><a class="header-anchor" href="#field-field" aria-hidden="true">#</a> field <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> </span><span style="color:#F47067;">field</span><span style="color:#ADBAC7;">: </span><span style="color:#F69D50;">FieldSignatureSupport</span></span>
<span class="line"></span></code></pre></div><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> fieldOrNull: </span><span style="color:#F69D50;">FieldSignatureSupport</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获取字段签名处理支持组件。</p></blockquote><h2 id="delegatefunction-field" tabindex="-1"><a class="header-anchor" href="#delegatefunction-field" aria-hidden="true">#</a> delegateFunction <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> delegateFunction: </span><span style="color:#F69D50;">KFunctionSignatureSupport</span></span>
<span class="line"></span></code></pre></div><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> delegateFunctionOrNull: </span><span style="color:#F69D50;">KFunctionSignatureSupport</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获取Delegate函数签名处理支持组件。</p></blockquote><h2 id="syntheticfunction-field" tabindex="-1"><a class="header-anchor" href="#syntheticfunction-field" aria-hidden="true">#</a> syntheticFunction <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> syntheticFunction: </span><span style="color:#F69D50;">KFunctionSignatureSupport</span></span>
<span class="line"></span></code></pre></div><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> syntheticFunctionOrNull: </span><span style="color:#F69D50;">KFunctionSignatureSupport</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>获取Synthetic函数签名处理支持组件。</p></blockquote><h2 id="returntype-field-1" tabindex="-1"><a class="header-anchor" href="#returntype-field-1" aria-hidden="true">#</a> returnType <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> returnType: </span><span style="color:#F69D50;">KType</span></span>
<span class="line"></span></code></pre></div><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> returnTypeOrNull: </span><span style="color:#F69D50;">KType</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>依据返回类型签名，获取 <code>KProperty</code> 返回类型 <code>KType</code>。</p></blockquote><h2 id="returnclass-field" tabindex="-1"><a class="header-anchor" href="#returnclass-field" aria-hidden="true">#</a> returnClass <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> returnClass: </span><span style="color:#F69D50;">KClass</span><span style="color:#ADBAC7;">&lt;*&gt;</span></span>
<span class="line"></span></code></pre></div><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> returnClassOrNull: </span><span style="color:#F69D50;">KClass</span><span style="color:#ADBAC7;">&lt;*&gt;?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>依据返回类型签名，获取 <code>KProperty</code> 泛型擦除的返回类型 <code>KClass</code>。</p></blockquote><h2 id="member-field-1" tabindex="-1"><a class="header-anchor" href="#member-field-1" aria-hidden="true">#</a> member <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> member: </span><span style="color:#F69D50;">Member</span></span>
<span class="line"></span></code></pre></div><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> memberOrNull: </span><span style="color:#F69D50;">Member</span><span style="color:#ADBAC7;">?</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>依据签名，获取 <code>KProperty</code> 以 <code>Member</code> 的表述对象。</p></blockquote><h2 id="hasgetter-field" tabindex="-1"><a class="header-anchor" href="#hasgetter-field" aria-hidden="true">#</a> hasGetter <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> hasGetter: </span><span style="color:#F69D50;">Boolean</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>检查Getter是否有效。</p></blockquote><h2 id="hassetter-field" tabindex="-1"><a class="header-anchor" href="#hassetter-field" aria-hidden="true">#</a> hasSetter <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> hasSetter: </span><span style="color:#F69D50;">Boolean</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>检查Setter是否有效。</p></blockquote><h2 id="hasfield-field" tabindex="-1"><a class="header-anchor" href="#hasfield-field" aria-hidden="true">#</a> hasField <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> hasField: </span><span style="color:#F69D50;">Boolean</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>检查Field是否有效。</p></blockquote><h2 id="hasdelegatefunction-field" tabindex="-1"><a class="header-anchor" href="#hasdelegatefunction-field" aria-hidden="true">#</a> hasDelegateFunction <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> hasDelegateFunction: </span><span style="color:#F69D50;">Boolean</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>检查Delegate是否有效。</p></blockquote><h2 id="hassyntheticfunction-field" tabindex="-1"><a class="header-anchor" href="#hassyntheticfunction-field" aria-hidden="true">#</a> hasSyntheticFunction <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> hasSyntheticFunction: </span><span style="color:#F69D50;">Boolean</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>检查Synthetic是否有效。</p></blockquote><h2 id="hassignature-field-1" tabindex="-1"><a class="header-anchor" href="#hassignature-field-1" aria-hidden="true">#</a> hasSignature <span class="symbol">- field</span></h2><div class="language-kotlin" data-ext="kt"><pre class="shiki github-dark-dimmed" style="background-color:#22272e;" tabindex="0"><code><span class="line"><span style="color:#F47067;">val</span><span style="color:#ADBAC7;"> hasSignature: </span><span style="color:#F69D50;">Boolean</span></span>
<span class="line"></span></code></pre></div><p><strong>变更记录</strong></p><p><code>v1.0.0</code> <code>添加</code></p><p><strong>功能描述</strong></p><blockquote><p>检查签名是否有效。</p></blockquote>`,148),p=[l];function t(c,r){return a(),e("div",null,p)}const i=s(o,[["render",t],["__file","KPropertySignatureSupport.html.vue"]]);export{i as default};
