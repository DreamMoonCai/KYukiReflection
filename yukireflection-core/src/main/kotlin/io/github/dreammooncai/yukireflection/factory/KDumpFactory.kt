@file:Suppress("UnusedImport", "NON_PUBLIC_CALL_FROM_PUBLIC_INLINE","MISSING_DEPENDENCY_SUPERCLASS", "NOTHING_TO_INLINE", "DuplicatedCode")

package io.github.dreammooncai.yukireflection.factory

import android.annotation.SuppressLint
import java.lang.reflect.Modifier.isPrivate
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.KVariance
import kotlin.reflect.full.contextParameters
import kotlin.reflect.full.extensionReceiverParameter
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.valueParameters
import kotlin.text.append

@OptIn(ExperimentalContextParameters::class)
inline fun KClass<*>.dump(): String = buildString {
    if (!isSupportReflection) {
        appendLine(runCatching { @SuppressLint("NewApi") java.toGenericString() }.getOrElse { java.toString() })

        if (allFunctionSignatures.isNotEmpty()) {
            append("functions:")
            allFunctionSignatures.forEach {
                appendLine("\t$it")
            }
        }

        if (allPropertySignatures.isNotEmpty()) {
            append("properties:")
            allPropertySignatures.forEach {
                appendLine("\t$it")
            }
        }

        append("========end class $name========>")
        return@buildString
    }
    appendLine("name:$name")

    if (annotations.isNotEmpty()) {
        append("annotations:")
        appendLine(annotations.joinToString(" ") { "@"+it.annotationClass.simpleNameOrJvm })
    }
    append("modifiers: ")
    if (isOpen) append("open ")

    if (isPrivate) append("private ")
    if (isProtected) append("protected ")
    if (isInternal) append("internal ")

    if (isSealed) append("sealed ")
    if (isData) append("data ")
    if (isValue) append("value ")
    if (isFun) append("fun ")

    if (descriptor.isExternal) append("external ")
    if (descriptor.isExpect) append("expect ")
    if (descriptor.isActual) append("actual ")

    if (isAbstract) append("abstract ")
    appendLine()

    if (generics.isNotEmpty()) {
        append("generics:")
        appendLine("<${generics.joinToString(",") {
            buildString {
                when (it.variance) {
                    KVariance.INVARIANT -> {
                    }
                    KVariance.IN -> append("in ")
                    KVariance.OUT -> append("out ")
                }

                append(it.name)
                if (it.upperBounds.isNotEmpty()) {
                    append("while ${it.upperBounds.joinToString(",")}")
                }
            }
        }}>")
    }

    if (declaredTopFunctions.isNotEmpty()) {
        appendLine("functions:")
        declaredTopFunctions.forEach {
            appendLine("\t$it")
        }
    }

    if (declaredTopPropertys.isNotEmpty()) {
        appendLine("propertys:")
        declaredTopPropertys.forEach {
            appendLine("\t$it")
        }
    }

    append("========end class $name========>")
}

@OptIn(ExperimentalContextParameters::class)
inline fun KProperty<*>.dump(): String = runCatching {
    buildString {
        val descriptor = descriptor
        append("name:$name")
        append("->")
        append("JvmName:${javaSignatureField?.name ?: "<anonymous>"}")
        append(",")
        append("JvmGetter:${javaSignatureGetter?.name ?: "<anonymous>"}")
        if (isVar) {
            append(",")
            append("JvmSetter:${javaSignatureSetter?.name ?: "<anonymous>"}")
        }
        appendLine()

        if (annotations.isNotEmpty()) {
            append("annotations:")
            appendLine(annotations.joinToString(" ") { "@"+it.annotationClass.simpleNameOrJvm })
        }
        append("modifiers: ")
        if (isOpen) append("open ")
        if (isOverride) append("override ")

        if (isPrivate) append("private ")
        if (isProtected) append("protected ")
        if (isInternal) append("internal ")

        if (isConst) append("const ")
        if (descriptor != null) {
            if (descriptor.isExternal) append("external ")
            if (descriptor.isExpect) append("expect ")
            if (descriptor.isActual) append("actual ")
            if (descriptor.isDelegated) append("delegated ")
        }
        if (isLateinit) append("lateinit ")
        if (isAbstract) append("abstract ")
        appendLine()

        if (generics.isNotEmpty()) {
            append("generics:")
            appendLine("<${generics.joinToString(",") {
                buildString {
                    when (it.variance) {
                        KVariance.INVARIANT -> {
                        }
                        KVariance.IN -> append("in ")
                        KVariance.OUT -> append("out ")
                    }

                    append(it.name)
                    if (it.upperBounds.isNotEmpty()) {
                        append("while ${it.upperBounds.joinToString(",")}")
                    }
                }
            }}>")
        }

        if (isExtension) {
            append("extension:")
            appendLine(extensionReceiverParameter!!.type)
        }

        instanceParameter?.let { parameter ->
            append("instance:")
            appendLine(parameter.type)
        }

        if (contextParameters.isNotEmpty()) {
            append("context:")
            appendLine(contextParameters.joinToString(" ") { "[${it.name}: ${it.type}]" })
        }

        if (valueParameters.isNotEmpty()) {
            appendLine("param:")
            appendLine("\t"+valueParameters.joinToString("\t\n") { "${it.index - valueParameters.first().index}.${it.name}: ${it.type}" })
        }

        append("type:")
        appendLine(returnType)
        append("========end property $name========>")
    }
}.getOrElse {
    buildString {
        appendLine(signature().give())
        append("========end property $name========>")
    }
}

@OptIn(ExperimentalContextParameters::class)
inline fun KFunction<*>.dump(): String = runCatching {
    buildString {
        val descriptor = descriptor
        append("name:$name")
        append("->")
        appendLine("JvmName:${javaSignatureMethod?.name ?: "<anonymous>"}")

        if (annotations.isNotEmpty()) {
            append("annotations:")
            appendLine(annotations.joinToString(" ") { "@"+it.annotationClass.simpleNameOrJvm })
        }
        append("modifiers: ")
        if (isOpen) append("open ")
        if (isOverride) append("override ")

        if (isPrivate) append("private ")
        if (isProtected) append("protected ")
        if (isInternal) append("internal ")

        if (isExternal) append("external ")
        if (descriptor != null) {
            if (descriptor.isExpect) append("expect ")
            if (descriptor.isActual) append("actual ")
        }
        if (isInline) append("inline ")
        if (isInfix) append("infix ")
        if (isOperator) append("operator ")
        if (isSuspend) append("suspend ")
        if (descriptor?.isTailrec == true) append("tailrec ")

        if (isAbstract) append("abstract ")
        appendLine()

        if (generics.isNotEmpty()) {
            append("generics:")
            appendLine("<${generics.joinToString(",") {
                buildString {
                    if (it.isReified) append("reified ")
                    when (it.variance) {
                        KVariance.INVARIANT -> {
                        }
                        KVariance.IN -> append("in ")
                        KVariance.OUT -> append("out ")
                    }

                    append(it.name)
                    if (it.upperBounds.isNotEmpty()) {
                        append("while ${it.upperBounds.joinToString(",")}")
                    }
                }
            }}>")
        }

        if (isExtension) {
            append("extension: ")
            appendLine(extensionReceiverParameter!!.type)
        }

        instanceParameter?.let { parameter ->
            append("instance:")
            appendLine(parameter.type)
        }

        if (contextParameters.isNotEmpty()) {
            append("context:")
            appendLine(contextParameters.joinToString(" ") { "[${it.name}: ${it.type}]" })
        }

        if (valueParameters.isNotEmpty()) {
            appendLine("param:")
            appendLine("\t"+valueParameters.joinToString("\t\n") { "${it.index - valueParameters.first().index}.${it.name}: ${it.type}" })
        }

        append("return:")
        appendLine(returnType)
        append("========end function $name========>")
    }
}.getOrElse {
    buildString {
        appendLine(signature().give())
        append("========end function $name========>")
    }
}