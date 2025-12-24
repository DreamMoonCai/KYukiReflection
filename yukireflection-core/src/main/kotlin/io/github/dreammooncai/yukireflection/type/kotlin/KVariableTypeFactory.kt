/*
 * YukiReflection - An efficient Reflection API for Java and Android built in Kotlin.
 * Copyright (C) 2019-2024 HighCapable
 * https://github.com/HighCapable/YukiReflection
 *
 * Apache License Version 2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file is created by fankes on 2022/2/2.
 * This file is modified by fankes on 2023/1/21.
 */
@file:Suppress("unused", "KDocUnresolvedReference", "DEPRECATION", "FunctionName", "UNCHECKED_CAST")

package io.github.dreammooncai.yukireflection.type.kotlin

import android.os.Build
import io.github.dreammooncai.yukireflection.factory.klassOf
import io.github.dreammooncai.yukireflection.factory.toKClass
import io.github.dreammooncai.yukireflection.factory.toKClassOrNull
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader
import dalvik.system.InMemoryDexClassLoader
import dalvik.system.PathClassLoader
import org.json.JSONArray
import org.json.JSONObject
import java.io.* // ktlint-disable no-wildcard-imports
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Member
import java.lang.reflect.Method
import java.net.HttpCookie
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Supplier
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.reflect.*
import java.lang.reflect.Array as JavaArray
import java.util.function.Function as JavaFunction

/**
 * 获得任意类型的数组
 *
 * 它在 Java 中表示为：([type])[]
 * @param type 类型
 * @return [KClass]<[Array]>
 */
fun ArrayClass(type: KClass<*>) = (JavaArray.newInstance(type.java, 0).javaClass as Class<JavaArray>).kotlin

/**
 * 获得 [Boolean] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "boolean"
 * @return [KClass]
 */
val BooleanKType get() = Boolean::class.javaPrimitiveType?.kotlin ?: "boolean".toKClass()

/**
 * 获得 [Char] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "char"
 * @return [KClass]
 */
val CharKType get() = Char::class.javaPrimitiveType?.kotlin ?: "char".toKClass()

/**
 * 获得 [Byte] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "byte"
 * @return [KClass]
 */
val ByteKType get() = Byte::class.javaPrimitiveType?.kotlin ?: "byte".toKClass()

/**
 * 获得 [Short] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "short"
 * @return [KClass]
 */
val ShortKType get() = Short::class.javaPrimitiveType?.kotlin ?: "short".toKClass()

/**
 * 获得 [Int] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "int"
 * @return [KClass]
 */
val IntKType get() = Int::class.javaPrimitiveType?.kotlin ?: "int".toKClass()

/**
 * 获得 [Float] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "float"
 * @return [KClass]
 */
val FloatKType get() = Float::class.javaPrimitiveType?.kotlin ?: "float".toKClass()

/**
 * 获得 [Long] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "long"
 * @return [KClass]
 */
val LongKType get() = Long::class.javaPrimitiveType?.kotlin ?: "long".toKClass()

/**
 * 获得 [Double] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "double"
 * @return [KClass]
 */
val DoubleKType get() = Double::class.javaPrimitiveType?.kotlin ?: "double".toKClass()

/**
 * 获得 [Any] 类型
 *
 * 它等价于 Java 中的 [java.lang.Object]
 * @return [KClass]<[Any]>
 */
val AnyKClass get() = klassOf<Any>()

/**
 * 获得 [Boolean] 类型
 *
 * 它等价于 Java 中的 [java.lang.Boolean]
 * @return [KClass]<[Boolean]>
 */
val BooleanKClass get() = klassOf<Boolean>()

/**
 * 获得 [Char] 类型
 *
 * 它等价于 Java 中的 [java.lang.Character]
 * @return [KClass]<[Char]>
 */
val CharKClass get() = klassOf<Char>()

/**
 * 获得 [Byte] 类型
 *
 * 它等价于 Java 中的 [java.lang.Byte]
 * @return [KClass]<[Byte]>
 */
val ByteKClass get() = klassOf<Byte>()

/**
 * 获得 [Short] 类型
 *
 * 它等价于 Java 中的 [java.lang.Short]
 * @return [KClass]<[Short]>
 */
val ShortKClass get() = klassOf<Short>()

/**
 * 获得 [Int] 类型
 *
 * 它等价于 Java 中的 [java.lang.Integer]
 * @return [KClass]<[Int]>
 */
val IntKClass get() = klassOf<Int>()

/**
 * 获得 [Float] 类型
 *
 * 它等价于 Java 中的 [java.lang.Float]
 * @return [KClass]<[Float]>
 */
val FloatKClass get() = klassOf<Float>()

/**
 * 获得 [Long] 类型
 *
 * 它等价于 Java 中的 [java.lang.Long]
 * @return [KClass]<[Long]>
 */
val LongKClass get() = klassOf<Long>()

/**
 * 获得 [Double] 类型
 *
 * 它等价于 Java 中的 [java.lang.Double]
 * @return [KClass]<[Double]>
 */
val DoubleKClass get() = klassOf<Double>()

/**
 * 获得 [Number] 类型
 *
 * 它等价于 Java 中的 [java.lang.Number]
 * @return [KClass]<[Number]>
 */
val NumberKClass get() = klassOf<Number>()

/**
 * 获得 [Unit] 类型
 *
 * 它等价于 Java 中的 [java.lang.Void]
 * @return [KClass]<[Unit]>
 */
val UnitKClass get() = klassOf<Unit>()

/**
 * 获得 [String] 类型
 *
 * 它等价于 Java 中的 [java.lang.String]
 * @return [KClass]<[String]>
 */
val StringKClass get() = klassOf<String>()

/**
 * 获得 [CharSequence] 类型
 *
 * 它等价于 Java 中的 [java.lang.CharSequence]
 * @return [KClass]<[CharSequence]>
 */
val CharSequenceKClass get() = klassOf<CharSequence>()

/**
 * 获得 [Serializable] 类型
 * @return [KClass]<[Serializable]>
 */
val SerializableKClass get() = klassOf<Serializable>()

/**
 * 获得 [Array] 类型
 *
 * 它等价于 Java 中的 [java.lang.reflect.Array]
 * @return [KClass]<[JavaArray]>
 */
val ArrayKClass get() = klassOf<JavaArray>()

/**
 * 获得 [Boolean] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "boolean[]"
 * @return [KClass]<[JavaArray]>
 */
val BooleanArrayKType get() = ArrayClass(BooleanKType)

/**
 * 获得 [Char] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "char[]"
 * @return [KClass]<[JavaArray]>
 */
val CharArrayKType get() = ArrayClass(CharKType)

/**
 * 获得 [Byte] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "byte[]"
 * @return [KClass]<[JavaArray]>
 */
val ByteArrayKType get() = ArrayClass(ByteKType)

/**
 * 获得 [Short] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "short[]"
 * @return [KClass]<[JavaArray]>
 */
val ShortArrayKType get() = ArrayClass(ShortKType)

/**
 * 获得 [Int] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "int[]"
 * @return [KClass]<[JavaArray]>
 */
val IntArrayKType get() = ArrayClass(IntKType)

/**
 * 获得 [Float] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "float[]"
 * @return [KClass]<[JavaArray]>
 */
val FloatArrayKType get() = ArrayClass(FloatKType)

/**
 * 获得 [Long] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "long[]"
 * @return [KClass]<[JavaArray]>
 */
val LongArrayKType get() = ArrayClass(LongKType)

/**
 * 获得 [Double] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "double[]"
 * @return [KClass]<[JavaArray]>
 */
val DoubleArrayKType get() = ArrayClass(DoubleKType)

/**
 * 获得 [Any] - [Array] 类型
 *
 * 它在 Java 中表示为：Object[]
 * @return [KClass]<[JavaArray]>
 */
val AnyArrayKClass get() = ArrayClass(AnyKClass)

/**
 * 获得 [Boolean] - [Array] 类型
 *
 * 它在 Java 中表示为：Boolean[]
 * @return [KClass]<[JavaArray]>
 */
val BooleanArrayKClass get() = ArrayClass(BooleanKClass)

/**
 * 获得 [Char] - [Array] 类型
 *
 * 它在 Java 中表示为：Character[]
 * @return [KClass]<[JavaArray]>
 */
val CharArrayKClass get() = ArrayClass(CharKClass)

/**
 * 获得 [Byte] - [Array] 类型
 *
 * 它在 Java 中表示为：Byte[]
 * @return [KClass]<[JavaArray]>
 */
val ByteArrayKClass get() = ArrayClass(ByteKClass)

/**
 * 获得 [Short] - [Array] 类型
 *
 * 它在 Java 中表示为：Short[]
 * @return [KClass]<[JavaArray]>
 */
val ShortArrayKClass get() = ArrayClass(ShortKClass)

/**
 * 获得 [Int] - [Array] 类型
 *
 * 它在 Java 中表示为：Integer[]
 * @return [KClass]<[JavaArray]>
 */
val IntArrayKClass get() = ArrayClass(IntKClass)

/**
 * 获得 [Float] - [Array] 类型
 *
 * 它在 Java 中表示为：Float[]
 * @return [KClass]<[JavaArray]>
 */
val FloatArrayKClass get() = ArrayClass(FloatKClass)

/**
 * 获得 [Long] - [Array] 类型
 *
 * 它在 Java 中表示为：Long[]
 * @return [KClass]<[JavaArray]>
 */
val LongArrayKClass get() = ArrayClass(LongKClass)

/**
 * 获得 [Double] - [Array] 类型
 *
 * 它在 Java 中表示为：Double[]
 * @return [KClass]<[JavaArray]>
 */
val DoubleArrayKClass get() = ArrayClass(DoubleKClass)

/**
 * 获得 [Number] - [Array] 类型
 *
 * 它在 Java 中表示为：Number[]
 * @return [KClass]<[JavaArray]>
 */
val NumberArrayKClass get() = ArrayClass(NumberKClass)

/**
 * 获得 [String] - [Array] 类型
 *
 * 它在 Java 中表示为：String[]
 * @return [KClass]<[JavaArray]>
 */
val StringArrayKClass get() = ArrayClass(StringKClass)

/**
 * 获得 [CharSequence] - [Array] 类型
 *
 * 它在 Java 中表示为：CharSequence[]
 * @return [KClass]<[JavaArray]>
 */
val CharSequenceArrayKClass get() = ArrayClass(CharSequenceKClass)

/**
 * 获得 [Cloneable] 类型
 * @return [KClass]<[Cloneable]>
 */
val CloneableKClass get() = klassOf<Cloneable>()

/**
 * 获得 [List] 类型
 * @return [KClass]<[List]>
 */
val ListKClass get() = klassOf<List<*>>()

/**
 * 获得 [ArrayList] 类型
 * @return [KClass]<[ArrayList]>
 */
val ArrayListKClass get() = klassOf<ArrayList<*>>()

/**
 * 获得 [HashMap] 类型
 * @return [KClass]<[HashMap]>
 */
val HashMapKClass get() = klassOf<HashMap<*, *>>()

/**
 * 获得 [HashSet] 类型
 * @return [KClass]<[HashSet]>
 */
val HashSetKClass get() = klassOf<HashSet<*>>()

/**
 * 获得 [WeakHashMap] 类型
 * @return [KClass]<[WeakHashMap]>
 */
val WeakHashMapKClass get() = klassOf<WeakHashMap<*, *>>()

/**
 * 获得 [WeakReference] 类型
 * @return [KClass]<[WeakReference]>
 */
val WeakReferenceKClass get() = klassOf<WeakReference<*>>()

/**
 * 获得 [Enum] 类型
 * @return [KClass]<[Enum]>
 */
val EnumKClass get() = klassOf<Enum<*>>()

/**
 * 获得 [Map] 类型
 * @return [KClass]<[Map]>
 */
val MapKClass get() = klassOf<Map<*, *>>()

/**
 * 获得 [Map.Entry] 类型
 * @return [KClass]<[Map.Entry]>
 */
val Map_EntryKClass get() = klassOf<Map.Entry<*, *>>()

/**
 * 获得 [Reference] 类型
 * @return [KClass]<[Reference]>
 */
val ReferenceKClass get() = klassOf<Reference<*>>()

/**
 * 获得 [Vector] 类型
 * @return [KClass]<[Vector]>
 */
val VectorKClass get() = klassOf<Vector<*>>()

/**
 * 获得 [File] 类型
 * @return [KClass]<[File]>
 */
val FileKClass get() = klassOf<File>()

/**
 * 获得 [InputStream] 类型
 * @return [KClass]<[InputStream]>
 */
val InputStreamKClass get() = klassOf<InputStream>()

/**
 * 获得 [OutputStream] 类型
 * @return [KClass]<[OutputStream]>
 */
val OutputStreamKClass get() = klassOf<OutputStream>()

/**
 * 获得 [BufferedReader] 类型
 * @return [KClass]<[BufferedReader]>
 */
val BufferedReaderKClass get() = klassOf<BufferedReader>()

/**
 * 获得 [Date] 类型
 * @return [KClass]<[Date]>
 */
val DateKClass get() = klassOf<Date>()

/**
 * 获得 [TimeZone] 类型
 * @return [KClass]<[TimeZone]>
 */
val TimeZoneKClass get() = klassOf<TimeZone>()

/**
 * 获得 [SimpleDateFormat] 类型
 * @return [KClass]<[SimpleDateFormat]>
 */
val SimpleDateFormatKClass_Java get() = klassOf<SimpleDateFormat>()

/**
 * 获得 [Timer] 类型
 * @return [KClass]<[Timer]>
 */
val TimerKClass get() = klassOf<Timer>()

/**
 * 获得 [TimerTask] 类型
 * @return [KClass]<[TimerTask]>
 */
val TimerTaskKClass get() = klassOf<TimerTask>()

/**
 * 获得 [Thread] 类型
 * @return [KClass]<[Thread]>
 */
val ThreadKClass get() = klassOf<Thread>()

/**
 * 获得 [Base64] 类型
 *
 * - 在 Android O (26) 及以上系统加入
 * @return [KClass]<[Base64]> or null
 */
val Base64KClass_Java get() = if (Build.VERSION.SDK_INT >= 26) klassOf<Base64>() else null

/**
 * 获得 [Observer] 类型
 * @return [KClass]<[Observer]>
 */
val ObserverKClass get() = klassOf<Observer>()

/**
 * 获得 [Set] 类型
 * @return [KClass]<[Set]>
 */
val SetKClass get() = klassOf<Set<*>>()

/**
 * 获得 [JSONObject] 类型
 * @return [KClass]<[JSONObject]>
 */
val JSONObjectKClass get() = klassOf<JSONObject>()

/**
 * 获得 [JSONArray] 类型
 * @return [KClass]<[JSONArray]>
 */
val JSONArrayKClass get() = klassOf<JSONArray>()

/**
 * 获得 [StringBuilder] 类型
 * @return [KClass]<[StringBuilder]>
 */
val StringBuilderKClass get() = klassOf<StringBuilder>()

/**
 * 获得 [StringBuffer] 类型
 * @return [KClass]<[StringBuffer]>
 */
val StringBufferKClass get() = klassOf<StringBuffer>()

/**
 * 获得 [ZipEntry] 类型
 * @return [KClass]<[ZipEntry]>
 */
val ZipEntryKClass get() = klassOf<ZipEntry>()

/**
 * 获得 [ZipFile] 类型
 * @return [KClass]<[ZipFile]>
 */
val ZipFileKClass get() = klassOf<ZipFile>()

/**
 * 获得 [ZipInputStream] 类型
 * @return [KClass]<[ZipInputStream]>
 */
val ZipInputStreamKClass get() = klassOf<ZipInputStream>()

/**
 * 获得 [ZipOutputStream] 类型
 * @return [KClass]<[ZipOutputStream]>
 */
val ZipOutputStreamKClass get() = klassOf<ZipOutputStream>()

/**
 * 获得 [HttpURLConnection] 类型
 * @return [KClass]<[HttpURLConnection]>
 */
val HttpURLConnectionKClass get() = klassOf<HttpURLConnection>()

/**
 * 获得 [HttpCookie] 类型
 * @return [KClass]<[HttpCookie]>
 */
val HttpCookieKClass get() = klassOf<HttpCookie>()

/**
 * 获得 [HttpClient] 类型
 * @return [KClass] or null
 */
val HttpClientKClass get() = "java.net.http.HttpClient".toKClassOrNull()

/**
 * 获得 [AtomicBoolean] 类型
 * @return [KClass]<[AtomicBoolean]>
 */
val AtomicBooleanKClass get() = klassOf<AtomicBoolean>()

/**
 * 获得 [Supplier] 类型
 * @return [KClass]<[Supplier]>
 */
val SupplierKClass get() = klassOf<Supplier<*>>()

/**
 * 获得 [KClass] 类型
 * @return [KClass]<[KClass]>
 */
val JavaKClass get() = klassOf<Class<*>>()

/**
 * 获得 [ClassLoader] 类型
 * @return [KClass]<[ClassLoader]>
 */
val JavaKClassLoader get() = klassOf<ClassLoader>()

/**
 * 获得 [BaseDexClassLoader] 类型
 * @return [KClass]<[BaseDexClassLoader]>
 */
val DalvikBaseDexClassLoaderKotlin get() = klassOf<BaseDexClassLoader>()

/**
 * 获得 [DexClassLoader] 类型
 * @return [KClass]<[DexClassLoader]>
 */
val DalvikDexClassLoaderKotlin get() = klassOf<DexClassLoader>()

/**
 * 获得 [PathClassLoader] 类型
 * @return [KClass]<[PathClassLoader]>
 */
val DalvikPathClassLoaderKotlin get() = klassOf<PathClassLoader>()

/**
 * 获得 [InMemoryDexClassLoader] 类型
 * @return [KClass]<[InMemoryDexClassLoader]>
 */
val DalvikInMemoryDexClassLoaderKotlin get() = klassOf<InMemoryDexClassLoader>()

/**
 * 获得 [Method] 类型
 * @return [KClass]<[Method]>
 */
val JavaMethodKClass get() = klassOf<Method>()

/**
 * 获得 [Field] 类型
 * @return [KClass]<[Field]>
 */
val JavaFieldKClass get() = klassOf<Field>()

/**
 * 获得 [Constructor] 类型
 * @return [KClass]<[Constructor]>
 */
val JavaConstructorKClass get() = klassOf<Constructor<*>>()

/**
 * 获得 [Member] 类型
 * @return [KClass]<[Member]>
 */
val JavaMemberKClass get() = klassOf<Member>()

/**
 * 获得 [Annotation] 类型
 * @return [KClass]<[Annotation]>
 */
val JavaAnnotationKClass get() = klassOf<Annotation>()

/**
 * 获得 [java.util.function.Function] 类型
 * @return [KClass]<[JavaFunction]>
 */
val FunctionKClass get() = klassOf<JavaFunction<*, *>>()

/**
 * 获得 [Optional] 类型
 * @return [KClass]<[Optional]>
 */
val OptionalKClass get() = klassOf<Optional<*>>()

/**
 * 获得 [OptionalInt] 类型
 * @return [KClass]<[OptionalInt]>
 */
val OptionalIntKClass get() = klassOf<OptionalInt>()

/**
 * 获得 [OptionalLong] 类型
 * @return [KClass]<[OptionalLong]>
 */
val OptionalLongKClass get() = klassOf<OptionalLong>()

/**
 * 获得 [OptionalDouble] 类型
 * @return [KClass]<[OptionalDouble]>
 */
val OptionalDoubleKClass get() = klassOf<OptionalDouble>()

/**
 * 获得 [Objects] 类型
 * @return [KClass]<[Objects]>
 */
val ObjectsKClass get() = klassOf<Objects>()

/**
 * 获得 [Runtime] 类型
 * @return [KClass]<[Runtime]>
 */
val RuntimeKClass get() = klassOf<Runtime>()

/**
 * 获得 [NullPointerException] 类型
 * @return [KClass]<[NullPointerException]>
 */
val NullPointerExceptionKClass get() = klassOf<NullPointerException>()

/**
 * 获得 [NumberFormatException] 类型
 * @return [KClass]<[NumberFormatException]>
 */
val NumberFormatExceptionKClass get() = klassOf<NumberFormatException>()

/**
 * 获得 [IllegalStateException] 类型
 * @return [KClass]<[IllegalStateException]>
 */
val IllegalStateExceptionKClass get() = klassOf<IllegalStateException>()

/**
 * 获得 [RuntimeException] 类型
 * @return [KClass]<[RuntimeException]>
 */
val RuntimeExceptionKClass get() = klassOf<RuntimeException>()

/**
 * 获得 [ClassNotFoundException] 类型
 * @return [KClass]<[ClassNotFoundException]>
 */
val ClassNotFoundExceptionKClass get() = klassOf<ClassNotFoundException>()

/**
 * 获得 [NoClassDefFoundError] 类型
 * @return [KClass]<[NoClassDefFoundError]>
 */
val NoClassDefFoundErrorKClass get() = klassOf<NoClassDefFoundError>()

/**
 * 获得 [NoSuchMethodError] 类型
 * @return [KClass]<[NoSuchMethodError]>
 */
val NoSuchMethodErrorKClass get() = klassOf<NoSuchMethodError>()

/**
 * 获得 [NoSuchFieldError] 类型
 * @return [KClass]<[NoSuchFieldError]>
 */
val NoSuchFieldErrorKClass get() = klassOf<NoSuchFieldError>()

/**
 * 获得 [Error] 类型
 * @return [KClass]<[Error]>
 */
val ErrorKClass get() = klassOf<Error>()

/**
 * 获得 [Exception] 类型
 * @return [KClass]<[Exception]>
 */
val ExceptionKClass get() = klassOf<Exception>()

/**
 * 获得 [Throwable] 类型
 * @return [KClass]<[Throwable]>
 */
val ThrowableKClass get() = klassOf<Throwable>()

/**
 * 获得 [KProperty] 类型
 * @return [KClass]<[KProperty]>
 */
val KPropertyKClass get() = klassOf<KProperty<*>>()

/**
 * 获得 [KFunction] 类型
 * @return [KClass]<[KFunction]>
 */
val KFunctionKClass get() = klassOf<KFunction<*>>()

/**
 * 获得 [KCallable] 类型
 * @return [KClass]<[KCallable]>
 */
val KCallableKClass get() = klassOf<KCallable<*>>()

/**
 * 获得 [KClass] 类型
 * @return [KClass]<[KClass]>
 */
val KotlinKClass get() = klassOf<KClass<*>>()

/**
 * 获得 [KType] 类型
 * @return [KClass]<[KType]>
 */
val KTypeKClass get() = klassOf<KType>()

/**
 * 获得 [KClassifier] 类型
 * @return [KClass]<[KClassifier]>
 */
val KClassifierKClass get() = klassOf<KClassifier>()

/**
 * 获得 [KTypeParameter] 类型
 * @return [KClass]<[KTypeParameter]>
 */
val KTypeParameterKClass get() = klassOf<KTypeParameter>()

/**
 * 获得 [KParameter] 类型
 * @return [KClass]<[KParameter]>
 */
val KParameterKClass get() = klassOf<KParameter>()

/**
 * 获得 [KTypeProjection] 类型
 * @return [KClass]<[KTypeProjection]>
 */
val KTypeProjectionKClass get() = klassOf<KTypeProjection>()

/**
 * 获得 [KVariance] 类型
 * @return [KClass]<[KVariance]>
 */
val KVarianceKClass get() = klassOf<KVariance>()

/**
 * 获得 [KMutableProperty] 类型
 * @return [KClass]<[KMutableProperty]>
 */
val KMutablePropertyKClass get() = klassOf<KMutableProperty<*>>()

/**
 * 获得 [kotlin.reflect.jvm.internal.KClassImpl] 类型
 * @return [KClass]<[kotlin.reflect.jvm.internal.KClassImpl]>
 */
val KClassImplKClass get() = "kotlin.reflect.jvm.internal.KClassImpl".toKClass()

/**
 * 获得 [kotlin.reflect.jvm.internal.impl.serialization.deserialization.descriptors.DeserializedMemberScope.OptimizedImplementation] 类型
 * @return [KClass]<[kotlin.reflect.jvm.internal.impl.serialization.deserialization.descriptors.DeserializedMemberScope.OptimizedImplementation]>
 */
val DeserializedMemberScope_OptimizedImplementationKClass get() = $$"kotlin.reflect.jvm.internal.impl.serialization.deserialization.descriptors.DeserializedMemberScope$OptimizedImplementation".toKClass()