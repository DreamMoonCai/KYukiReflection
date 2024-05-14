package com.dream.yukireflection.factory;

import kotlin.jvm.internal.*;
import kotlin.reflect.KClass;
import kotlin.reflect.KDeclarationContainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static kotlin.jvm.internal.CallableReference.NO_RECEIVER;

public class KReference {
    public enum Flags {
        NO_FLAGS(0),
        TOP_LEVEL(1),
        SYNTHETIC(2),
        PARAMETER_VARARG(0),
        PARAMETER_UNIT(2),
        PARAMETER_SUSPEND(4);

        private final int value;

        Flags(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static PropertyReference0 propertyStatic(KDeclarationContainer owner, String name, String signature) {
        return propertyStatic(NO_RECEIVER, ((ClassBasedDeclarationContainer)owner).getJClass(), name, signature, owner instanceof KClass ? Flags.NO_FLAGS : Flags.TOP_LEVEL);
    }

    public static PropertyReference0 propertyStatic(Class<?> owner, String name, String signature) {
        return propertyStatic(NO_RECEIVER, owner, name, signature, Flags.NO_FLAGS);
    }

    public static PropertyReference0 propertyStatic(Class<?> owner, String name, String signature, Flags flags) {
        return propertyStatic(NO_RECEIVER, owner, name, signature, flags);
    }

    public static PropertyReference0 propertyStatic(Object receiver, Class<?> owner, String name, String signature) {
        return propertyStatic(receiver, owner, name, signature, Flags.NO_FLAGS);
    }

    public static PropertyReference0 propertyStatic(Object receiver, Class<?> owner, String name, String signature, Flags flags) {
        return new PropertyReference0Impl(receiver, owner, name, signature, flags.value);
    }

    public static PropertyReference0 propertyStatic(Field field) {
        return propertyStatic(NO_RECEIVER,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field),field.isSynthetic() ? Flags.SYNTHETIC : Flags.NO_FLAGS);
    }

    public static PropertyReference0 propertyStatic(Field field,Object receiver) {
        return propertyStatic(receiver,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field),field.isSynthetic() ? Flags.SYNTHETIC : Flags.NO_FLAGS);
    }

    public static PropertyReference0 propertyStatic(Field field, Flags flags) {
        return propertyStatic(NO_RECEIVER,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field), flags);
    }

    public static PropertyReference0 propertyStatic(Field field, Flags flags, Object receiver) {
        return propertyStatic(receiver,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field), flags);
    }


    public static MutablePropertyReference0 mutablePropertyStatic(KDeclarationContainer owner, String name, String signature) {
        return mutablePropertyStatic(NO_RECEIVER, ((ClassBasedDeclarationContainer)owner).getJClass(), name, signature, owner instanceof KClass ? Flags.NO_FLAGS : Flags.TOP_LEVEL);
    }

    public static MutablePropertyReference0 mutablePropertyStatic(Class<?> owner, String name, String signature) {
        return mutablePropertyStatic(NO_RECEIVER, owner, name, signature, Flags.NO_FLAGS);
    }

    public static MutablePropertyReference0 mutablePropertyStatic(Class<?> owner, String name, String signature, Flags flags) {
        return mutablePropertyStatic(NO_RECEIVER, owner, name, signature, flags);
    }

    public static MutablePropertyReference0 mutablePropertyStatic(Object receiver, Class<?> owner, String name, String signature) {
        return mutablePropertyStatic(receiver, owner, name, signature, Flags.NO_FLAGS);
    }

    public static MutablePropertyReference0 mutablePropertyStatic(Object receiver, Class<?> owner, String name, String signature, Flags flags) {
        return new MutablePropertyReference0Impl(receiver, owner, name, signature, flags.value);
    }

    public static MutablePropertyReference0 mutablePropertyStatic(Field field) {
        return mutablePropertyStatic(NO_RECEIVER,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field),field.isSynthetic() ? Flags.SYNTHETIC : Flags.NO_FLAGS);
    }

    public static MutablePropertyReference0 mutablePropertyStatic(Field field,Object receiver) {
        return mutablePropertyStatic(receiver,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field),field.isSynthetic() ? Flags.SYNTHETIC : Flags.NO_FLAGS);
    }

    public static MutablePropertyReference0 mutablePropertyStatic(Field field, Flags flags) {
        return mutablePropertyStatic(NO_RECEIVER,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field), flags);
    }

    public static MutablePropertyReference0 mutablePropertyStatic(Field field, Flags flags, Object receiver) {
        return mutablePropertyStatic(receiver,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field), flags);
    }


    public static PropertyReference1 property(KDeclarationContainer owner, String name, String signature) {
        return property(NO_RECEIVER, ((ClassBasedDeclarationContainer)owner).getJClass(), name, signature, owner instanceof KClass ? Flags.NO_FLAGS : Flags.TOP_LEVEL);
    }

    public static PropertyReference1 property(Class<?> owner, String name, String signature) {
        return property(NO_RECEIVER, owner, name, signature, Flags.NO_FLAGS);
    }

    public static PropertyReference1 property(Class<?> owner, String name, String signature, Flags flags) {
        return property(NO_RECEIVER, owner, name, signature, flags);
    }

    public static PropertyReference1 property(Object receiver, Class<?> owner, String name, String signature) {
        return property(receiver, owner, name, signature, Flags.NO_FLAGS);
    }

    public static PropertyReference1 property(Object receiver, Class<?> owner, String name, String signature, Flags flags) {
        return new PropertyReference1Impl(receiver, owner, name, signature, flags.value);
    }

    public static PropertyReference1 property(Field field) {
        return property(NO_RECEIVER,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field),field.isSynthetic() ? Flags.SYNTHETIC : Flags.NO_FLAGS);
    }

    public static PropertyReference1 property(Field field,Object receiver) {
        return property(receiver,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field),field.isSynthetic() ? Flags.SYNTHETIC : Flags.NO_FLAGS);
    }

    public static PropertyReference1 property(Field field, Flags flags) {
        return property(NO_RECEIVER,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field), flags);
    }

    public static PropertyReference1 property(Field field, Flags flags, Object receiver) {
        return property(receiver,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field), flags);
    }


    public static MutablePropertyReference1 mutableProperty(KDeclarationContainer owner, String name, String signature) {
        return mutableProperty(NO_RECEIVER, ((ClassBasedDeclarationContainer)owner).getJClass(), name, signature, owner instanceof KClass ? Flags.NO_FLAGS : Flags.TOP_LEVEL);
    }

    public static MutablePropertyReference1 mutableProperty(Class<?> owner, String name, String signature) {
        return mutableProperty(NO_RECEIVER, owner, name, signature, Flags.NO_FLAGS);
    }

    public static MutablePropertyReference1 mutableProperty(Class<?> owner, String name, String signature, Flags flags) {
        return mutableProperty(NO_RECEIVER, owner, name, signature, flags);
    }

    public static MutablePropertyReference1 mutableProperty(Object receiver, Class<?> owner, String name, String signature) {
        return mutableProperty(receiver, owner, name, signature, Flags.NO_FLAGS);
    }

    public static MutablePropertyReference1 mutableProperty(Object receiver, Class<?> owner, String name, String signature, Flags flags) {
        return new MutablePropertyReference1Impl(receiver, owner, name, signature, flags.value);
    }

    public static MutablePropertyReference1 mutableProperty(Field field) {
        return mutableProperty(NO_RECEIVER,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field),field.isSynthetic() ? Flags.SYNTHETIC : Flags.NO_FLAGS);
    }

    public static MutablePropertyReference1 mutableProperty(Field field,Object receiver) {
        return mutableProperty(receiver,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field),field.isSynthetic() ? Flags.SYNTHETIC : Flags.NO_FLAGS);
    }

    public static MutablePropertyReference1 mutableProperty(Field field, Flags flags) {
        return mutableProperty(NO_RECEIVER,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field), flags);
    }

    public static MutablePropertyReference1 mutableProperty(Field field, Flags flags, Object receiver) {
        return mutableProperty(receiver,field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field), flags);
    }


    public static PropertyReference2 propertyDelegate(KDeclarationContainer owner, String name, String signature) {
        return propertyDelegate(((ClassBasedDeclarationContainer)owner).getJClass(), name, signature, owner instanceof KClass ? Flags.NO_FLAGS : Flags.TOP_LEVEL);
    }

    public static PropertyReference2 propertyDelegate(Class<?> owner, String name, String signature) {
        return propertyDelegate(owner, name, signature, Flags.NO_FLAGS);
    }

    public static PropertyReference2 propertyDelegate(Class<?> owner, String name, String signature, Flags flags) {
        return new PropertyReference2Impl(owner, name, signature, flags.value);
    }

    public static PropertyReference2 propertyDelegate(Field field) {
        return propertyDelegate(field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field),field.isSynthetic() ? Flags.SYNTHETIC : Flags.NO_FLAGS);
    }

    public static PropertyReference2 propertyDelegate(Field field, Flags flags) {
        return propertyDelegate(field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field), flags);
    }


    public static MutablePropertyReference2 mutablePropertyDelegate(KDeclarationContainer owner, String name, String signature) {
        return mutablePropertyDelegate(((ClassBasedDeclarationContainer)owner).getJClass(), name, signature, owner instanceof KClass ? Flags.NO_FLAGS : Flags.TOP_LEVEL);
    }

    public static MutablePropertyReference2 mutablePropertyDelegate(Class<?> owner, String name, String signature) {
        return mutablePropertyDelegate(owner, name, signature, Flags.NO_FLAGS);
    }

    public static MutablePropertyReference2 mutablePropertyDelegate(Class<?> owner, String name, String signature, Flags flags) {
        return new MutablePropertyReference2Impl(owner, name, signature, flags.value);
    }

    public static MutablePropertyReference2 mutablePropertyDelegate(Field field) {
        return mutablePropertyDelegate(field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field),field.isSynthetic() ? Flags.SYNTHETIC : Flags.NO_FLAGS);
    }

    public static MutablePropertyReference2 mutablePropertyDelegate(Field field, Flags flags) {
        return mutablePropertyDelegate(field.getDeclaringClass(), field.getName(), KotlinFactoryKt.getKotlinSimpleSignature(field), flags);
    }

    public static FunctionReference function(int arity, KDeclarationContainer owner, String name, String signature) {
        return function(arity, NO_RECEIVER, ((ClassBasedDeclarationContainer)owner).getJClass(), name, signature, owner instanceof KClass ? Flags.NO_FLAGS : Flags.TOP_LEVEL);
    }

    public static FunctionReference function(int arity, Class<?> owner, String name, String signature, Flags flags) {
        return function(arity, NO_RECEIVER, owner, name, signature, flags);
    }

    public static FunctionReference function(int arity, Object receiver, Class<?> owner, String name, String signature, Flags flags) {
        return new FunctionReferenceImpl(arity, receiver, owner, name, signature, flags.value);
    }

    public static FunctionReference function(Method method){
        return function(method,Flags.NO_FLAGS);
    }

    public static FunctionReference function(Method method,Flags flags){
        return function(method,flags,NO_RECEIVER);
    }

    public static FunctionReference function(Method method,Flags flags, Object receiver){
        int arity = method.getParameterTypes().length;
        if (!Modifier.isStatic(arity) || !Reflection.createKotlinClass(method.getDeclaringClass()).isCompanion()) {
            arity++;
        }
        return function(arity,receiver,method.getDeclaringClass(),method.getName(), KotlinFactoryKt.getKotlinSimpleSignature(method),flags);
    }

    public static FunctionReference function(Constructor<?> constructor){
        return function(constructor,Flags.NO_FLAGS);
    }

    public static FunctionReference function(Constructor<?> constructor,Flags flags){
        return function(constructor,flags,NO_RECEIVER);
    }

    public static FunctionReference function(Constructor<?> constructor,Flags flags, Object receiver){
        int arity = constructor.getParameterTypes().length;
        return function(arity,receiver,constructor.getDeclaringClass(),constructor.getName(), KotlinFactoryKt.getKotlinSimpleSignature(constructor),flags);
    }
}
