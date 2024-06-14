package com.dream.yukireflection.finder.signature.support

import com.dream.yukireflection.factory.*
import com.dream.yukireflection.utils.DexSignUtil
import java.lang.reflect.Field
import java.lang.reflect.Member
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.metadata.deserialization.NameResolver
import kotlin.reflect.jvm.internal.impl.metadata.jvm.JvmProtoBuf

/**
 * 属性签名处理支持组件
 *
 * @property nameResolver 名称解析器
 * @property proto 属性签名
 */
class KPropertySignatureSupport(private val declaringClass: KClass<*>? = null, private val loader: ClassLoader? = declaringClass?.classLoader, private val nameResolver: NameResolver, private val proto: JvmProtoBuf.JvmPropertySignature){

    /**
     * 字段签名处理支持组件
     *
     * @property proto 字段签名
     */
    inner class FieldSignatureSupport(private val proto: JvmProtoBuf.JvmFieldSignature){
        /**
         * 字段名
         */
        val name by lazy { nameResolver.getString(proto.name) }

        /**
         * 字段类型描述符 如:Ljava/lang/String;
         */
        val typeDescriptor by lazy { nameResolver.getString(proto.desc) }

        /**
         * 字段类型 [KClass] 使用创建此描述符对象的根源 [declaringClass] 的 [ClassLoader] 描述结果
         *
         * 如需指定 [ClassLoader] 请使用方法方式调用
         */
        val type by lazy { getType() }

        /**
         * 字段类型 [KClass] 使用创建此描述符对象的根源 [declaringClass] 的 [ClassLoader] 描述结果
         *
         * 如需指定 [ClassLoader] 请使用方法方式调用
         *
         * - 获取时不会触发异常
         */
        val typeOrNull by lazy { getTypeOrNull() }

        /**
         * 字段成员 [Field] 使用创建此描述符对象的根源类 [declaringClass] 进行字段查找
         *
         * 获取此字段以 [Field] 描述的结果
         */
        val member by lazy { getMember() }

        /**
         * 字段成员 [Field] 使用创建此描述符对象的根源类 [declaringClass] 进行字段查找
         *
         * 获取此字段以 [Field] 描述的结果
         *
         * - 获取时不会触发异常
         */
        val memberOrNull by lazy { getMemberOrNull() }

        /**
         * 字段类型 [KClass] 使用指定 [loader] 描述结果
         *
         * @param loader [ClassLoader] 字段类型 [type] 所在的 [ClassLoader]
         * @return [KClass]
         */
        fun getType(loader: ClassLoader? = null): KClass<*> = DexSignUtil.getTypeName(typeDescriptor).toKClassOrNull(loader ?: this@KPropertySignatureSupport.loader) ?: error("FieldSignatureSupport:typeDescriptor is null")

        /**
         * 字段类型 [KClass] 使用指定 [loader] 描述结果
         *
         * - 获取时不会触发异常
         *
         * @param loader [ClassLoader] 字段类型 [type] 所在的 [ClassLoader]
         * @return [KClass] or null
         */
        fun getTypeOrNull(loader: ClassLoader? = null): KClass<*>? = runCatching { getType(loader) }.getOrNull()

        /**
         * 字段成员 [Field] 使用指定 [declaringClass] 描述结果
         *
         * 获取此字段以 [Field] 描述的结果
         *
         * @param declaringClass [KClass] 字段所在的 [KClass]
         * @return [Field]
         */
        fun getMember(declaringClass: KClass<*>? = null): Field = (declaringClass ?: this@KPropertySignatureSupport.declaringClass)?.java?.getDeclaredField(name)?.also { it.isAccessible = true } ?: error("FieldSignatureSupport:member is null")

        /**
         * 字段成员 [Field] 使用指定 [declaringClass] 描述结果
         *
         * 获取此字段以 [Field] 描述的结果
         *
         * - 获取时不会触发异常
         *
         * @param declaringClass [KClass] 字段所在的 [KClass]
         * @return [Field] or null
         */
        fun getMemberOrNull(declaringClass: KClass<*>? = null): Field? = runCatching { getMember(declaringClass) }.getOrNull()

        /**
         * 字段名是否存在
         */
        val hasName by lazy { proto.hasName() }

        /**
         * 字段类型是否存在
         */
        val hasType by lazy { proto.hasDesc() }

        /**
         * 验证签名是否正确存在
         */
        val hasSignature by lazy { (declaringClass?.name?.let { it != DexSignUtil.getTypeName("L$name;") } ?: false) }

        override fun toString(): String {
            return if (!hasSignature)
                "['Signature object is Invalid.']"
            else "[name='$name', typeDescriptor='$typeDescriptor']"
        }
    }

    /**
     * 获取Getter函数签名处理支持组件
     */
    val getter by lazy { proto.getter?.let { KFunctionSignatureSupport(declaringClass,loader,nameResolver,it) } ?: error("PropertySignatureSupport:getter is null") }

    /**
     * 获取Getter函数签名处理支持组件
     */
    val getterOrNull by lazy { runCatching { getter }.getOrNull() }

    /**
     * 获取Setter函数签名处理支持组件
     */
    val setter by lazy { proto.setter?.let { KFunctionSignatureSupport(declaringClass,loader,nameResolver,it) } ?: error("PropertySignatureSupport:setter is null") }

    /**
     * 获取Setter函数签名处理支持组件
     */
    val setterOrNull by lazy { runCatching { setter }.getOrNull() }

    /**
     * 获取字段签名处理支持组件
     */
    val field by lazy { proto.field?.let { FieldSignatureSupport(it) } ?: error("PropertySignatureSupport:field is null") }

    /**
     * 获取字段签名处理支持组件
     */
    val fieldOrNull by lazy { runCatching { field }.getOrNull() }

    /**
     * 获取委托函数签名处理支持组件
     */
    val delegateFunction by lazy { proto.delegateMethod?.let { KFunctionSignatureSupport(declaringClass,loader,nameResolver,it) } ?: error("PropertySignatureSupport:delegateFunction is null") }

    /**
     * 获取委托函数签名处理支持组件
     */
    val delegateFunctionOrNull by lazy { runCatching { delegateFunction }.getOrNull() }

    /**
     * 获取合成函数签名处理支持组件
     */
    val syntheticFunction by lazy { proto.syntheticMethod?.let { KFunctionSignatureSupport(declaringClass,loader,nameResolver,it) } ?: error("PropertySignatureSupport:syntheticFunction is null") }

    /**
     * 获取合成函数签名处理支持组件
     */
    val syntheticFunctionOrNull by lazy { runCatching { syntheticFunction }.getOrNull() }

    /**
     * 获取此属性可获取的成员 [Member]
     *
     * [field]、[getter]、[setter]
     */
    val member: Member by lazy { getMember() }

    /**
     * 字段成员 [Field] 使用创建此描述符对象的根源类 [declaringClass] 进行字段查找
     *
     * 获取此字段以 [Field] 描述的结果
     *
     * - 获取时不会触发异常
     */
    val memberOrNull by lazy { getMemberOrNull() }

    /**
     * 获取此属性可获取的成员 [Member]
     *
     * [field]、[getter]、[setter] 依次尝试
     *
     * @param declaringClass [KClass] - 属性所在的 [KClass]
     * @param loader [ClassLoader] - 属性类型所使用的 [ClassLoader]
     * @return [Member]
     */
    fun getMember(declaringClass: KClass<*>? = null,loader: ClassLoader? = null):Member = fieldOrNull?.getMemberOrNull(declaringClass) ?: getterOrNull?.getMemberOrNull(declaringClass,loader) ?: setterOrNull?.getMemberOrNull(declaringClass,loader) ?: error("PropertySignatureSupport:member is null")

    /**
     * 获取此属性可获取的成员 [Member]
     *
     * [field]、[getter]、[setter] 依次尝试
     *
     * - 获取时不会触发异常
     *
     * @param declaringClass [KClass] - 属性所在的 [KClass]
     * @param loader [ClassLoader] - 属性类型所使用的 [ClassLoader]
     * @return [Member] or null
     */
    fun getMemberOrNull(declaringClass: KClass<*>? = null,loader: ClassLoader? = null):Member? = runCatching { getMember(declaringClass,loader) }.getOrNull()

    /**
     * 是否存在Getter函数签名
     */
    val hasGetter by lazy { proto.hasGetter() }

    /**
     * 是否存在Setter函数签名
     */
    val hasSetter by lazy { proto.hasSetter() }

    /**
     * 是否存在字段签名
     */
    val hasField by lazy { proto.hasField() }

    /**
     * 是否存在委托函数签名
     */
    val hasDelegateFunction by lazy { proto.hasDelegateMethod() }

    /**
     * 是否存在合成函数签名
     */
    val hasSyntheticFunction by lazy { proto.hasSyntheticMethod() }

    /**
     * 验证签名是否正确存在
     */
    val hasSignature by lazy { !(getterOrNull?.hasSignature == false && setterOrNull?.hasSignature == false && fieldOrNull?.hasSignature == false && delegateFunctionOrNull?.hasSignature == false && syntheticFunctionOrNull?.hasSignature == false) }

    override fun toString(): String {
        if (!hasSignature)
           return "['Signature object is Invalid.']"

        val signatureStr = mutableListOf<String>().also {
            if (getterOrNull?.hasSignature == true) it += "getter=$getterOrNull"
            if (setterOrNull?.hasSignature == true) it += "setter=$setterOrNull"
            if (fieldOrNull?.hasSignature == true) it += "field=$fieldOrNull"
            if (delegateFunctionOrNull?.hasSignature == true) it += "delegateFunction=$delegateFunctionOrNull"
            if (syntheticFunctionOrNull?.hasSignature == true) it += "syntheticFunction=$syntheticFunctionOrNull"
        }
        return "[" + signatureStr.joinToString(", ") + "]"
    }

}