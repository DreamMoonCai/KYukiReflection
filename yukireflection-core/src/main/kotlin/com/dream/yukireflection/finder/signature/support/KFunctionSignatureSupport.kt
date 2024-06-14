@file:Suppress("UnusedImport", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE","MISSING_DEPENDENCY_SUPERCLASS")
package com.dream.yukireflection.finder.signature.support

import com.dream.yukireflection.factory.*
import com.dream.yukireflection.finder.signature.data.KSignatureData
import com.dream.yukireflection.utils.DexSignUtil
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf
import kotlin.reflect.jvm.internal.impl.metadata.deserialization.NameResolver
import kotlin.reflect.jvm.internal.impl.metadata.jvm.JvmProtoBuf

/**
 * 方法签名处理支持组件
 *
 * @property proto 方法签名
 */
class KFunctionSignatureSupport internal constructor(private val declaringClass: KClass<*>? = null, private val loader: ClassLoader? = declaringClass?.classLoader, private val proto: KSignatureData){
    constructor(declaringClass: KClass<*>? = null, loader: ClassLoader? = declaringClass?.classLoader, nameResolver: NameResolver, proto: JvmProtoBuf.JvmMethodSignature):this(declaringClass,loader,
        KSignatureData(nameResolver.getString(proto.name),nameResolver.getString(proto.desc))
    )
    constructor(declaringClass: KClass<*>? = null, loader: ClassLoader? = declaringClass?.classLoader, nameResolver: NameResolver, proto: ProtoBuf.Function):
      this(declaringClass,loader, KSignatureData(nameResolver.getString(proto.name),when {proto.valueParameterList.isNotEmpty() -> {
          val paramTypes = proto.valueParameterList.map {
              "L${nameResolver.getString(it.type.className)};"
          }
          "(${paramTypes.joinToString("")})"
      }
          else -> "()"
      } + "L${nameResolver.getString(proto.returnType.className)};"))
    /**
     * 方法名
     */
    val name by lazy { proto.name }

    /**
     * 方法签名 如:(Ljava/lang/String;)V
     *
     * 此签名不包括方法名
     */
    val descriptor by lazy { proto.descriptor }

    /**
     * 方法参数类型描述符 如:Ljava/lang/String;IZLjava/lang/String;
     */
    val paramTypesDescriptors by lazy { descriptor.substring(1,descriptor.indexOf(")")) }

    /**
     * 方法返回类型描述符 如:Ljava/lang/String;
     */
    val returnTypeDescriptor by lazy { descriptor.substring(descriptor.indexOf(")") + 1) }

    /**
     * 方法参数类型 [KClass] 使用创建此描述符对象的根源 [declaringClass] 的 [ClassLoader] 描述结果
     *
     * 如需指定 [ClassLoader] 请使用方法方式调用
     */
    val paramTypes by lazy { getParamTypes() }

    /**
     * 方法参数类型 [KClass] 使用创建此描述符对象的根源 [declaringClass] 的 [ClassLoader] 描述结果
     *
     * - 获取时不会触发异常
     *
     * 如需指定 [ClassLoader] 请使用方法方式调用
     */
    val paramTypesOrNull by lazy { getParamTypesOrNull() }

    /**
     * 方法返回类型 [KClass] 使用创建此描述符对象的根源 [declaringClass] 的 [ClassLoader] 描述结果
     *
     * 如需指定 [ClassLoader] 请使用方法方式调用
     */
    val returnType by lazy { getReturnType() }

    /**
     * 方法返回类型 [KClass] 使用创建此描述符对象的根源 [declaringClass] 的 [ClassLoader] 描述结果
     *
     * 如需指定 [ClassLoader] 请使用方法方式调用
     *
     * - 获取时不会触发异常
     */
    val returnTypeOrNull by lazy { getReturnTypeOrNull() }

    /**
     * 方法成员 [Method] 使用创建此描述符对象的根源类 [declaringClass] 进行方法查找
     *
     * 获取此方法以 [Method] 描述的结果
     */
    val member by lazy { getMember() }

    /**
     * 方法成员 [Method] 使用创建此描述符对象的根源类 [declaringClass] 进行方法查找
     *
     * 获取此方法以 [Method] 描述的结果
     *
     * - 获取时不会触发异常
     */
    val memberOrNull by lazy { getMemberOrNull() }

    /**
     * 方法参数类型 [KClass] 使用创建此描述符对象的根源 [declaringClass] 的 [ClassLoader] 描述结果
     *
     * @param loader [ClassLoader] 参数类型 [paramTypes] 所在的 [ClassLoader]
     * @return [List]<[KClass]>
     */
    fun getParamTypes(loader: ClassLoader? = null):List<KClass<*>> = DexSignUtil.getParamTypeNames(paramTypesDescriptors).map { it.toKClassOrNull(loader ?: this@KFunctionSignatureSupport.loader) ?: error("FunctionSignatureSupport:paramDescriptors is null") }

    /**
     * 方法参数类型 [KClass] 使用创建此描述符对象的根源 [declaringClass] 的 [ClassLoader] 描述结果
     *
     * - 获取时不会触发异常
     *
     * @param loader [ClassLoader] 参数类型 [paramTypes] 所在的 [ClassLoader]
     * @return [List]<[KClass]> or null
     */
    fun getParamTypesOrNull(loader: ClassLoader? = null):List<KClass<*>>? = runCatching { getParamTypes(loader) }.getOrNull()

    /**
     * 方法返回类型 [KClass] 使用创建此描述符对象的根源 [declaringClass] 的 [ClassLoader] 描述结果
     *
     * @param loader [ClassLoader] 返回类型 [returnType] 所在的 [ClassLoader]
     * @return [KClass]
     */
    fun getReturnType(loader: ClassLoader? = null): KClass<*> = DexSignUtil.getTypeName(returnTypeDescriptor).toKClassOrNull(loader ?: this@KFunctionSignatureSupport.loader) ?: error("FunctionSignatureSupport:returnTypeDescriptor is null")

    /**
     * 方法返回类型 [KClass] 使用创建此描述符对象的根源 [declaringClass] 的 [ClassLoader] 描述结果
     *
     * - 获取时不会触发异常
     *
     * @param loader [ClassLoader] 返回类型 [returnType] 所在的 [ClassLoader]
     * @return [KClass] or null
     */
    fun getReturnTypeOrNull(loader: ClassLoader? = null): KClass<*>? = runCatching { getReturnType(loader) }.getOrNull()

    /**
     * 方法成员 [Method] 使用指定 [declaringClass] 描述结果
     *
     * @param declaringClass [KClass] 方法所在的 [KClass]
     * @param loader [ClassLoader] 方法参数 [paramTypes] 所在的 [ClassLoader]
     * @return [Method]
     */
    fun getMember(declaringClass: KClass<*>? = null, loader: ClassLoader? = null): Method = (declaringClass ?: this.declaringClass)?.java?.getDeclaredMethod(name,*getParamTypes(loader ?: this@KFunctionSignatureSupport.loader).map { it.java }.toTypedArray())?.also { it.isAccessible = true } ?: error("MethodSignatureSupport:member is null")

    /**
     * 方法成员 [Method] 使用指定 [declaringClass] 描述结果
     *
     * - 获取时不会触发异常
     *
     * @param declaringClass [KClass] 方法所在的 [KClass]
     * @param loader [ClassLoader] 方法参数 [paramTypes] 所在的 [ClassLoader]
     * @return [Method] or null
     */
    fun getMemberOrNull(declaringClass: KClass<*>? = null, loader: ClassLoader? = null): Method? = runCatching { getMember(declaringClass,loader) }.getOrNull()

    /**
     * 验证签名是否正确存在
     */
    val hasSignature by lazy { (declaringClass?.name?.let { it != DexSignUtil.getTypeName("L$name;") } ?: false) && descriptor.startsWith("(") }

    override fun toString(): String {
        return if (!hasSignature)
            "['Signature object is Invalid.']"
        else
            "[name='$name', desc='$descriptor']"
    }
}