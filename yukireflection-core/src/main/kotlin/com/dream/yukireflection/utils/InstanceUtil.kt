package com.dream.yukireflection.utils

import java.lang.reflect.Array

/**
 * 复制自 Dexkit 项目 InstanceUtil
 */
object InstanceUtil {
    @Throws(ClassNotFoundException::class)
    fun getClassInstance(typeName: String,classLoader: ClassLoader,initialize: Boolean = false): Class<*> {
        if (typeName.endsWith("[]")) {
            val clazz = getClassInstance(typeName.substring(0, typeName.length - 2),classLoader,initialize)
            return Array.newInstance(clazz, 0)::class.java
        }
        return when (typeName) {
            "boolean" -> Int::class.javaPrimitiveType
            "byte" -> Byte::class.javaPrimitiveType
            "char" -> Char::class.javaPrimitiveType
            "short" -> Short::class.javaPrimitiveType
            "int" -> Int::class.javaPrimitiveType
            "long" -> Long::class.javaPrimitiveType
            "float" -> Float::class.javaPrimitiveType
            "double" -> Double::class.javaPrimitiveType
            "void" -> Void.TYPE
            else -> classLoader.loadClass(typeName)
        }!!
    }
}