package io.github.dreammooncai.yukireflection.factory

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.jvm.internal.*
import kotlin.reflect.KClass
import kotlin.reflect.KDeclarationContainer
import kotlin.reflect.KCallable

/**
 * 引用方式创建相关Kotlin对象
 *
 * 引用对象创建速度快懒加载只有使用时会耗时加载由此创建的[KCallable]可调用 [KCallable.ref]
 */
object KReference {
    enum class Flags(val value: Int) {
        NO_FLAGS(0),
        TOP_LEVEL(1),
        SYNTHETIC(2),
        PARAMETER_VARARG(0),
        PARAMETER_UNIT(2),
        PARAMETER_SUSPEND(4)
    }

    fun propertyStatic(owner: KDeclarationContainer, name: String?, signature: String?): PropertyReference0 {
        return propertyStatic(
            CallableReference.NO_RECEIVER,
            (owner as ClassBasedDeclarationContainer).jClass,
            name,
            signature,
            if (owner is KClass<*>) Flags.NO_FLAGS else Flags.TOP_LEVEL
        )
    }

    fun propertyStatic(owner: Class<*>?, name: String?, signature: String?): PropertyReference0 {
        return propertyStatic(CallableReference.NO_RECEIVER, owner, name, signature, Flags.NO_FLAGS)
    }

    fun propertyStatic(owner: Class<*>?, name: String?, signature: String?, flags: Flags): PropertyReference0 {
        return propertyStatic(CallableReference.NO_RECEIVER, owner, name, signature, flags)
    }

    @JvmOverloads
    fun propertyStatic(
        receiver: Any?,
        owner: Class<*>?,
        name: String?,
        signature: String?,
        flags: Flags = Flags.NO_FLAGS
    ): PropertyReference0 {
        return PropertyReference0Impl(receiver, owner, name, signature, flags.value)
    }

    fun propertyStatic(field: Field): PropertyReference0 {
        return propertyStatic(
            CallableReference.NO_RECEIVER,
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            if (field.isSynthetic) Flags.SYNTHETIC else Flags.NO_FLAGS
        )
    }

    fun propertyStatic(field: Field, receiver: Any?): PropertyReference0 {
        return propertyStatic(
            receiver,
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            if (field.isSynthetic) Flags.SYNTHETIC else Flags.NO_FLAGS
        )
    }

    fun propertyStatic(field: Field, flags: Flags): PropertyReference0 {
        return propertyStatic(
            CallableReference.NO_RECEIVER,
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            flags
        )
    }

    fun propertyStatic(field: Field, flags: Flags, receiver: Any?): PropertyReference0 {
        return propertyStatic(receiver, field.declaringClass, field.name, field.kotlinSimpleSignature, flags)
    }


    fun mutablePropertyStatic(
        owner: KDeclarationContainer,
        name: String?,
        signature: String?
    ): MutablePropertyReference0 {
        return mutablePropertyStatic(
            CallableReference.NO_RECEIVER,
            (owner as ClassBasedDeclarationContainer).jClass,
            name,
            signature,
            if (owner is KClass<*>) Flags.NO_FLAGS else Flags.TOP_LEVEL
        )
    }

    fun mutablePropertyStatic(owner: Class<*>?, name: String?, signature: String?): MutablePropertyReference0 {
        return mutablePropertyStatic(CallableReference.NO_RECEIVER, owner, name, signature, Flags.NO_FLAGS)
    }

    fun mutablePropertyStatic(
        owner: Class<*>?,
        name: String?,
        signature: String?,
        flags: Flags
    ): MutablePropertyReference0 {
        return mutablePropertyStatic(CallableReference.NO_RECEIVER, owner, name, signature, flags)
    }

    @JvmOverloads
    fun mutablePropertyStatic(
        receiver: Any?,
        owner: Class<*>?,
        name: String?,
        signature: String?,
        flags: Flags = Flags.NO_FLAGS
    ): MutablePropertyReference0 {
        return MutablePropertyReference0Impl(receiver, owner, name, signature, flags.value)
    }

    fun mutablePropertyStatic(field: Field): MutablePropertyReference0 {
        return mutablePropertyStatic(
            CallableReference.NO_RECEIVER,
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            if (field.isSynthetic) Flags.SYNTHETIC else Flags.NO_FLAGS
        )
    }

    fun mutablePropertyStatic(field: Field, receiver: Any?): MutablePropertyReference0 {
        return mutablePropertyStatic(
            receiver,
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            if (field.isSynthetic) Flags.SYNTHETIC else Flags.NO_FLAGS
        )
    }

    fun mutablePropertyStatic(field: Field, flags: Flags): MutablePropertyReference0 {
        return mutablePropertyStatic(
            CallableReference.NO_RECEIVER,
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            flags
        )
    }

    fun mutablePropertyStatic(field: Field, flags: Flags, receiver: Any?): MutablePropertyReference0 {
        return mutablePropertyStatic(receiver, field.declaringClass, field.name, field.kotlinSimpleSignature, flags)
    }


    fun property(owner: KDeclarationContainer, name: String?, signature: String?): PropertyReference1 {
        return property(
            CallableReference.NO_RECEIVER,
            (owner as ClassBasedDeclarationContainer).jClass,
            name,
            signature,
            if (owner is KClass<*>) Flags.NO_FLAGS else Flags.TOP_LEVEL
        )
    }

    fun property(owner: Class<*>?, name: String?, signature: String?): PropertyReference1 {
        return property(CallableReference.NO_RECEIVER, owner, name, signature, Flags.NO_FLAGS)
    }

    fun property(owner: Class<*>?, name: String?, signature: String?, flags: Flags): PropertyReference1 {
        return property(CallableReference.NO_RECEIVER, owner, name, signature, flags)
    }

    @JvmOverloads
    fun property(
        receiver: Any?,
        owner: Class<*>?,
        name: String?,
        signature: String?,
        flags: Flags = Flags.NO_FLAGS
    ): PropertyReference1 {
        return PropertyReference1Impl(receiver, owner, name, signature, flags.value)
    }

    fun property(field: Field): PropertyReference1 {
        return property(
            CallableReference.NO_RECEIVER,
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            if (field.isSynthetic) Flags.SYNTHETIC else Flags.NO_FLAGS
        )
    }

    fun property(field: Field, receiver: Any?): PropertyReference1 {
        return property(
            receiver,
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            if (field.isSynthetic) Flags.SYNTHETIC else Flags.NO_FLAGS
        )
    }

    fun property(field: Field, flags: Flags): PropertyReference1 {
        return property(
            CallableReference.NO_RECEIVER,
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            flags
        )
    }

    fun property(field: Field, flags: Flags, receiver: Any?): PropertyReference1 {
        return property(receiver, field.declaringClass, field.name, field.kotlinSimpleSignature, flags)
    }


    fun mutableProperty(owner: KDeclarationContainer, name: String?, signature: String?): MutablePropertyReference1 {
        return mutableProperty(
            CallableReference.NO_RECEIVER,
            (owner as ClassBasedDeclarationContainer).jClass,
            name,
            signature,
            if (owner is KClass<*>) Flags.NO_FLAGS else Flags.TOP_LEVEL
        )
    }

    fun mutableProperty(owner: Class<*>?, name: String?, signature: String?): MutablePropertyReference1 {
        return mutableProperty(CallableReference.NO_RECEIVER, owner, name, signature, Flags.NO_FLAGS)
    }

    fun mutableProperty(owner: Class<*>?, name: String?, signature: String?, flags: Flags): MutablePropertyReference1 {
        return mutableProperty(CallableReference.NO_RECEIVER, owner, name, signature, flags)
    }

    @JvmOverloads
    fun mutableProperty(
        receiver: Any?,
        owner: Class<*>?,
        name: String?,
        signature: String?,
        flags: Flags = Flags.NO_FLAGS
    ): MutablePropertyReference1 {
        return MutablePropertyReference1Impl(receiver, owner, name, signature, flags.value)
    }

    fun mutableProperty(field: Field): MutablePropertyReference1 {
        return mutableProperty(
            CallableReference.NO_RECEIVER,
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            if (field.isSynthetic) Flags.SYNTHETIC else Flags.NO_FLAGS
        )
    }

    fun mutableProperty(field: Field, receiver: Any?): MutablePropertyReference1 {
        return mutableProperty(
            receiver,
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            if (field.isSynthetic) Flags.SYNTHETIC else Flags.NO_FLAGS
        )
    }

    fun mutableProperty(field: Field, flags: Flags): MutablePropertyReference1 {
        return mutableProperty(
            CallableReference.NO_RECEIVER,
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            flags
        )
    }

    fun mutableProperty(field: Field, flags: Flags, receiver: Any?): MutablePropertyReference1 {
        return mutableProperty(receiver, field.declaringClass, field.name, field.kotlinSimpleSignature, flags)
    }


    fun propertyDelegate(owner: KDeclarationContainer, name: String?, signature: String?): PropertyReference2 {
        return propertyDelegate(
            (owner as ClassBasedDeclarationContainer).jClass,
            name,
            signature,
            if (owner is KClass<*>) Flags.NO_FLAGS else Flags.TOP_LEVEL
        )
    }

    @JvmOverloads
    fun propertyDelegate(
        owner: Class<*>?,
        name: String?,
        signature: String?,
        flags: Flags = Flags.NO_FLAGS
    ): PropertyReference2 {
        return PropertyReference2Impl(owner, name, signature, flags.value)
    }

    fun propertyDelegate(field: Field): PropertyReference2 {
        return propertyDelegate(
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            if (field.isSynthetic) Flags.SYNTHETIC else Flags.NO_FLAGS
        )
    }

    fun propertyDelegate(field: Field, flags: Flags): PropertyReference2 {
        return propertyDelegate(field.declaringClass, field.name, field.kotlinSimpleSignature, flags)
    }


    fun mutablePropertyDelegate(
        owner: KDeclarationContainer,
        name: String?,
        signature: String?
    ): MutablePropertyReference2 {
        return mutablePropertyDelegate(
            (owner as ClassBasedDeclarationContainer).jClass,
            name,
            signature,
            if (owner is KClass<*>) Flags.NO_FLAGS else Flags.TOP_LEVEL
        )
    }

    @JvmOverloads
    fun mutablePropertyDelegate(
        owner: Class<*>?,
        name: String?,
        signature: String?,
        flags: Flags = Flags.NO_FLAGS
    ): MutablePropertyReference2 {
        return MutablePropertyReference2Impl(owner, name, signature, flags.value)
    }

    fun mutablePropertyDelegate(field: Field): MutablePropertyReference2 {
        return mutablePropertyDelegate(
            field.declaringClass,
            field.name,
            field.kotlinSimpleSignature,
            if (field.isSynthetic) Flags.SYNTHETIC else Flags.NO_FLAGS
        )
    }

    fun mutablePropertyDelegate(field: Field, flags: Flags): MutablePropertyReference2 {
        return mutablePropertyDelegate(field.declaringClass, field.name, field.kotlinSimpleSignature, flags)
    }

    fun function(arity: Int, owner: KDeclarationContainer, name: String?, signature: String?): FunctionReference {
        return function(
            arity,
            CallableReference.NO_RECEIVER,
            (owner as ClassBasedDeclarationContainer).jClass,
            name,
            signature,
            if (owner is KClass<*>) Flags.NO_FLAGS else Flags.TOP_LEVEL
        )
    }

    fun function(arity: Int, owner: Class<*>?, name: String?, signature: String?, flags: Flags): FunctionReference {
        return function(arity, CallableReference.NO_RECEIVER, owner, name, signature, flags)
    }

    fun function(
        arity: Int,
        receiver: Any?,
        owner: Class<*>?,
        name: String?,
        signature: String?,
        flags: Flags
    ): FunctionReference {
        return FunctionReferenceImpl(arity, receiver, owner, name, signature, flags.value)
    }

    @JvmOverloads
    fun function(
        method: Method,
        flags: Flags = Flags.NO_FLAGS,
        receiver: Any? = CallableReference.NO_RECEIVER
    ): FunctionReference {
        var arity = method.parameterTypes.size
        if (!Modifier.isStatic(arity) || !Reflection.createKotlinClass(method.declaringClass).isCompanion) {
            arity++
        }
        return function(arity, receiver, method.declaringClass, method.name, method.kotlinSimpleSignature, flags)
    }

    @JvmOverloads
    fun function(
        constructor: Constructor<*>,
        flags: Flags = Flags.NO_FLAGS,
        receiver: Any? = CallableReference.NO_RECEIVER
    ): FunctionReference {
        val arity = constructor.parameterTypes.size
        return function(
            arity,
            receiver,
            constructor.declaringClass,
            constructor.name,
            constructor.kotlinSimpleSignature,
            flags
        )
    }
}
