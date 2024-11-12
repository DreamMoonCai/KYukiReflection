package io.github.dreammooncai.yukireflection.finder.signature.support

import io.github.dreammooncai.yukireflection.factory.*
import io.github.dreammooncai.yukireflection.utils.DexSignUtil
import java.lang.reflect.Field
import java.lang.reflect.Member
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.jvm.internal.impl.metadata.deserialization.NameResolver
import kotlin.reflect.jvm.internal.impl.metadata.jvm.JvmProtoBuf

/**
 * 属性签名处理支持组件
 *
 * @property declaringClass 属性声明类
 * @property loader [ClassLoader] - 属性类型所使用的 [ClassLoader]
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
         * 字段类型 [KType] 通过 [getMember] 获取泛型类型转 [KType]
         *
         * 获取此字段以 [KType] 描述的结果
         *
         * 没有找到泛型类型时为 [getReturnClass]
         *
         * 如需指定 [declaringClass] 请使用方法方式调用
         */
        val returnType by lazy { getReturnType() }

        /**
         * 字段类型 [KType] 通过 [getMember] 获取泛型类型转 [KType]
         *
         * 获取此字段以 [KType] 描述的结果
         *
         * 没有找到泛型类型时为 [getReturnClass]
         *
         * 如需指定 [declaringClass] 请使用方法方式调用
         *
         * - 获取时不会触发异常
         */
        val returnTypeOrNull by lazy { getReturnTypeOrNull() }

        /**
         * 字段类型 [KClass] 使用创建此描述符对象的根源 [declaringClass] 的 [ClassLoader] 描述结果
         *
         * 如需指定 [ClassLoader] 请使用方法方式调用
         */
        val returnClass by lazy { getReturnClass() }

        /**
         * 字段类型 [KClass] 使用创建此描述符对象的根源 [declaringClass] 的 [ClassLoader] 描述结果
         *
         * 如需指定 [ClassLoader] 请使用方法方式调用
         *
         * - 获取时不会触发异常
         */
        val returnClassOrNull by lazy { getReturnClassOrNull() }

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
         * 字段类型 [KType] 通过 [getMember] 获取泛型类型转 [KType]
         *
         * 没有找到泛型类型时为 [getReturnClass]
         *
         * 获取此字段以 [KType] 描述的结果
         * @param declaringClass [KClass] 字段所在的 [KClass]
         * @return [KType] or null
         */
        fun getReturnType(declaringClass: KClass<*>? = null): KType = runCatching { getMember(declaringClass).genericType.kotlinType }.getOrNull() ?: getReturnClass(loader).type

        /**
         * 字段类型 [KType] 通过 [getMember] 获取泛型类型转 [KType]
         *
         * 获取此字段以 [KType] 描述的结果
         *
         * 没有找到泛型类型时为 [getReturnClass]
         *
         * - 获取时不会触发异常
         *
         * @param declaringClass [KClass] 字段所在的 [KClass]
         * @return [KType] or null
         */
        fun getReturnTypeOrNull(declaringClass: KClass<*>? = null): KType? = runCatching { getReturnType(declaringClass) }.getOrNull()

        /**
         * 字段类型 [KClass] 使用指定 [loader] 描述结果
         *
         * @param loader [ClassLoader] 字段类型 [type] 所在的 [ClassLoader]
         * @return [KClass]
         */
        fun getReturnClass(loader: ClassLoader? = null): KClass<*> = DexSignUtil.getTypeName(typeDescriptor).toKClassOrNull(loader ?: this@KPropertySignatureSupport.loader) ?: error("Descriptor: $typeDescriptor, cannot be converted to [KClass].")

        /**
         * 字段类型 [KClass] 使用指定 [loader] 描述结果
         *
         * - 获取时不会触发异常
         *
         * @param loader [ClassLoader] 字段类型 [type] 所在的 [ClassLoader]
         * @return [KClass] or null
         */
        fun getReturnClassOrNull(loader: ClassLoader? = null): KClass<*>? = runCatching { getReturnClass(loader) }.getOrNull()

        /**
         * 字段成员 [Field] 使用指定 [declaringClass] 描述结果
         *
         * 获取此字段以 [Field] 描述的结果
         *
         * @param declaringClass [KClass] 字段所在的 [KClass]
         * @return [Field]
         */
        fun getMember(declaringClass: KClass<*>? = null): Field = (declaringClass ?: this@KPropertySignatureSupport.declaringClass)?.java?.getDeclaredField(name)?.also { it.isAccessible = true } ?: error("Descriptors cannot be converted to members, please check $declaringClass or $name.")

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
    val getter by lazy { proto.getter?.let { KFunctionSignatureSupport(declaringClass,loader,nameResolver,it) } ?: error("There doesn't seem to be a getter method for this property.") }

    /**
     * 获取Getter函数签名处理支持组件
     */
    val getterOrNull by lazy { runCatching { getter }.getOrNull().takeIf { hasGetter } }

    /**
     * 获取Setter函数签名处理支持组件
     */
    val setter by lazy { proto.setter?.let { KFunctionSignatureSupport(declaringClass,loader,nameResolver,it) } ?: error("There doesn't seem to be a setter method for this property.") }

    /**
     * 获取Setter函数签名处理支持组件
     */
    val setterOrNull by lazy { runCatching { setter }.getOrNull().takeIf { hasSetter } }

    /**
     * 获取字段签名处理支持组件
     */
    val field by lazy { proto.field?.let { FieldSignatureSupport(it) } ?: error("This property doesn't seem to have a field.") }

    /**
     * 获取字段签名处理支持组件
     */
    val fieldOrNull by lazy { runCatching { field }.getOrNull().takeIf { hasField } }

    /**
     * 获取委托函数签名处理支持组件
     */
    val delegateFunction by lazy { proto.delegateMethod?.let { KFunctionSignatureSupport(declaringClass,loader,nameResolver,it) } ?: error("This property doesn't seem to have a delegate function.") }

    /**
     * 获取委托函数签名处理支持组件
     */
    val delegateFunctionOrNull by lazy { runCatching { delegateFunction }.getOrNull().takeIf { hasDelegateFunction } }

    /**
     * 获取合成函数签名处理支持组件
     */
    val syntheticFunction by lazy { proto.syntheticMethod?.let { KFunctionSignatureSupport(declaringClass,loader,nameResolver,it) } ?: error("This property doesn't seem to have a synthetic function.") }

    /**
     * 获取合成函数签名处理支持组件
     */
    val syntheticFunctionOrNull by lazy { runCatching { syntheticFunction }.getOrNull().takeIf { hasSyntheticFunction } }

    /**
     * 获取此属性可获取的泛型返回类型 [KType]
     *
     * [field]、[getter]、[setter]
     *
     * 没有找到泛型类型时为 [getReturnClass]
     */
    val returnType by lazy { getReturnType() }

    /**
     * 获取此属性可获取的泛型返回类型 [KType]
     *
     * [field]、[getter]、[setter]
     *
     * 没有找到泛型类型时为 [getReturnClass]
     *
     * - 获取时不会触发异常
     */
    val returnTypeOrNull by lazy { getReturnTypeOrNull() }

    /**
     * 获取此属性可获取的返回类型 [KClass]
     *
     * [field]、[getter]、[setter]
     */
    val returnClass by lazy { getReturnClass() }

    /**
     * 获取此属性可获取的返回类型 [KClass]
     *
     * [field]、[getter]、[setter]
     *
     * - 获取时不会触发异常
     */
    val returnClassOrNull by lazy { getReturnClassOrNull() }

    /**
     * 获取此属性可获取的成员 [Member]
     *
     * [field]、[getter]、[setter]
     */
    val member: Member by lazy { getMember() }

    /**
     * 获取此属性可获取的成员 [Member]
     *
     * [field]、[getter]、[setter]
     *
     * - 获取时不会触发异常
     */
    val memberOrNull by lazy { getMemberOrNull() }

    /**
     * 获取此属性可获取的泛型返回类型 [KType]
     *
     * [field]、[getter]、[setter] 依次尝试
     *
     * 没有找到泛型类型时为 [getReturnClass]
     *
     * @param declaringClass [KClass] - 属性所在的 [KClass]
     * @param loader [ClassLoader] - 属性类型所使用的 [ClassLoader]
     * @return [KType]
     */
    fun getReturnType(declaringClass: KClass<*>? = null,loader: ClassLoader? = null) = fieldOrNull?.getReturnTypeOrNull(declaringClass) ?: getterOrNull?.getReturnType(declaringClass,loader) ?: setterOrNull?.getParamTypesOrNull(declaringClass,loader)?.first() ?: error("This property does not get the return type, please check $hasSignature.")

    /**
     * 获取此属性可获取的泛型返回类型 [KType]
     *
     * [field]、[getter]、[setter] 依次尝试
     *
     * 没有找到泛型类型时为 [getReturnClass]
     *
     * - 获取时不会触发异常
     *
     * @param declaringClass [KClass] - 属性所在的 [KClass]
     * @param loader [ClassLoader] - 属性类型所使用的 [ClassLoader]
     * @return [KType] or null
     */
    fun getReturnTypeOrNull(declaringClass: KClass<*>? = null,loader: ClassLoader? = null) = runCatching { getReturnType(declaringClass,loader) }.getOrNull()

    /**
     * 获取此属性可获取的返回类型 [KClass]
     *
     * [field]、[getter]、[setter] 依次尝试
     *
     * @param loader [ClassLoader] - 属性类型所使用的 [ClassLoader]
     * @return [KClass]
     */
    fun getReturnClass(loader: ClassLoader? = null) = fieldOrNull?.getReturnClassOrNull(loader) ?: getterOrNull?.getReturnClassOrNull(loader) ?: setterOrNull?.getParamClassOrNull(loader)?.first() ?: error("This property does not get the return type, please check $hasSignature.")

    /**
     * 获取此属性可获取的返回类型 [KClass]
     *
     * [field]、[getter]、[setter] 依次尝试
     *
     * - 获取时不会触发异常
     *
     * @param loader [ClassLoader] - 属性类型所使用的 [ClassLoader]
     * @return [KClass] or null
     */
    fun getReturnClassOrNull(loader: ClassLoader? = null) = runCatching { getReturnClass(loader) }.getOrNull()

    /**
     * 获取此属性可获取的成员 [Member]
     *
     * [field]、[getter]、[setter] 依次尝试
     *
     * @param declaringClass [KClass] - 属性所在的 [KClass]
     * @param loader [ClassLoader] - 属性类型所使用的 [ClassLoader]
     * @return [Member]
     */
    fun getMember(declaringClass: KClass<*>? = null,loader: ClassLoader? = null):Member = fieldOrNull?.getMemberOrNull(declaringClass) ?: getterOrNull?.getMemberOrNull(declaringClass,loader) ?: setterOrNull?.getMemberOrNull(declaringClass,loader) ?: error("This property cannot be converted to a member, please check $hasSignature.")

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