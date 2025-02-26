package io.github.dreammooncai.yukireflection.factory;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KVariance;
import kotlin.reflect.jvm.internal.KClassifierImpl;
import kotlin.reflect.jvm.internal.impl.descriptors.ClassifierDescriptor;

public class KTypeParameterJavaImpl implements KTypeParameter, KClassifierImpl {
    TypeVariable<?> typeVariable;

    public KTypeParameterJavaImpl(TypeVariable<?> typeVariable) {
        this.typeVariable = typeVariable;
    }

    @Override
    public boolean isReified() {
        return false;
    }

    @NotNull
    @Override
    public String getName() {
        return typeVariable.getName();
    }

    @NotNull
    @Override
    public List<KType> getUpperBounds() {
        ArrayList<KType> kTypes = new ArrayList<>();
        for (java.lang.reflect.Type type : typeVariable.getBounds()) {
            kTypes.add(KJvmFactoryKt.getKotlinType(type));
        }
        return kTypes;
    }

    @NotNull
    @Override
    public KVariance getVariance() {
        return KVariance.INVARIANT;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        KTypeParameter that = (KTypeParameter) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getUpperBounds(), that.getUpperBounds());
    }

    @Override
    public int hashCode() {
        return super.hashCode() + 31;
    }

    @NotNull
    @Override
    public ClassifierDescriptor getDescriptor() {
        return KJvmFactoryKt.getDescriptor(typeVariable);
    }
}
