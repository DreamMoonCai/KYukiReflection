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

package com.dream.yukireflection.type.kotlin

import android.os.Build
import com.dream.yukireflection.factory.kclassOf
import com.dream.yukireflection.factory.toKClass
import com.highcapable.yukireflection.factory.classOf
import com.highcapable.yukireflection.factory.toClass
import com.highcapable.yukireflection.factory.toClassOrNull
import com.highcapable.yukireflection.type.java.*
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
import com.highcapable.yukireflection.type.java.ArrayClass as JavaArrayClass

/**
 * 获得任意类型的数组
 *
 * 它在 Java 中表示为：([type])[]
 * @param type 类型
 * @return [KClass]<[Array]>
 */
fun ArrayClass(type: KClass<*>) = JavaArrayClass(type.java).kotlin

/**
 * 获得 [Boolean] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "boolean"
 * @return [KClass]
 */
val BooleanKType get() = BooleanType.kotlin

/**
 * 获得 [Char] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "char"
 * @return [KClass]
 */
val CharKType get() = CharType.kotlin

/**
 * 获得 [Byte] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "byte"
 * @return [KClass]
 */
val ByteKType get() = ByteType.kotlin

/**
 * 获得 [Short] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "short"
 * @return [KClass]
 */
val ShortKType get() = ShortType.kotlin

/**
 * 获得 [Int] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "int"
 * @return [KClass]
 */
val IntKType get() = IntType.kotlin

/**
 * 获得 [Float] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "float"
 * @return [KClass]
 */
val FloatKType get() = FloatType.kotlin

/**
 * 获得 [Long] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "long"
 * @return [KClass]
 */
val LongKType get() = LongType.kotlin

/**
 * 获得 [Double] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) - 它在字节码中的关键字为 "double"
 * @return [KClass]
 */
val DoubleKType get() = DoubleType.kotlin

/**
 * 获得 [Any] 类型
 *
 * 它等价于 Java 中的 [java.lang.Object]
 * @return [KClass]<[Any]>
 */
val AnyKClass get() = AnyClass.kotlin

/**
 * 获得 [Boolean] 类型
 *
 * 它等价于 Java 中的 [java.lang.Boolean]
 * @return [KClass]<[Boolean]>
 */
val BooleanKClass get() = BooleanClass.kotlin

/**
 * 获得 [Char] 类型
 *
 * 它等价于 Java 中的 [java.lang.Character]
 * @return [KClass]<[Char]>
 */
val CharKClass get() = CharClass.kotlin

/**
 * 获得 [Byte] 类型
 *
 * 它等价于 Java 中的 [java.lang.Byte]
 * @return [KClass]<[Byte]>
 */
val ByteKClass get() = ByteClass.kotlin

/**
 * 获得 [Short] 类型
 *
 * 它等价于 Java 中的 [java.lang.Short]
 * @return [KClass]<[Short]>
 */
val ShortKClass get() = ShortClass.kotlin

/**
 * 获得 [Int] 类型
 *
 * 它等价于 Java 中的 [java.lang.Integer]
 * @return [KClass]<[Int]>
 */
val IntKClass get() = IntClass.kotlin

/**
 * 获得 [Float] 类型
 *
 * 它等价于 Java 中的 [java.lang.Float]
 * @return [KClass]<[Float]>
 */
val FloatKClass get() = FloatClass.kotlin

/**
 * 获得 [Long] 类型
 *
 * 它等价于 Java 中的 [java.lang.Long]
 * @return [KClass]<[Long]>
 */
val LongKClass get() = LongClass.kotlin

/**
 * 获得 [Double] 类型
 *
 * 它等价于 Java 中的 [java.lang.Double]
 * @return [KClass]<[Double]>
 */
val DoubleKClass get() = DoubleClass.kotlin

/**
 * 获得 [Number] 类型
 *
 * 它等价于 Java 中的 [java.lang.Number]
 * @return [KClass]<[Number]>
 */
val NumberKClass get() = NumberClass.kotlin

/**
 * 获得 [Unit] 类型
 *
 * 它等价于 Java 中的 [java.lang.Void]
 * @return [KClass]<[Void]>
 */
val UnitKClass get() = Unit::class.java.kotlin

/**
 * 获得 [String] 类型
 *
 * 它等价于 Java 中的 [java.lang.String]
 * @return [KClass]<[String]>
 */
val StringKClass get() = StringClass.kotlin

/**
 * 获得 [CharSequence] 类型
 *
 * 它等价于 Java 中的 [java.lang.CharSequence]
 * @return [KClass]<[CharSequence]>
 */
val CharSequenceKClass get() = CharSequenceClass.kotlin

/**
 * 获得 [Serializable] 类型
 * @return [KClass]<[Serializable]>
 */
val SerializableKClass get() = SerializableClass.kotlin

/**
 * 获得 [Array] 类型
 *
 * 它等价于 Java 中的 [java.lang.reflect.Array]
 * @return [KClass]<[JavaArray]>
 */
val ArrayKClass get() = JavaArrayClass.kotlin

/**
 * 获得 [Boolean] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "boolean[]"
 * @return [KClass]<[JavaArray]>
 */
val BooleanArrayKType get() = BooleanArrayType.kotlin

/**
 * 获得 [Char] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "char[]"
 * @return [KClass]<[JavaArray]>
 */
val CharArrayKType get() = CharArrayType.kotlin

/**
 * 获得 [Byte] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "byte[]"
 * @return [KClass]<[JavaArray]>
 */
val ByteArrayKType get() = ByteArrayType.kotlin

/**
 * 获得 [Short] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "short[]"
 * @return [KClass]<[JavaArray]>
 */
val ShortArrayKType get() = ShortArrayType.kotlin

/**
 * 获得 [Int] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "int[]"
 * @return [KClass]<[JavaArray]>
 */
val IntArrayKType get() = IntArrayType.kotlin

/**
 * 获得 [Float] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "float[]"
 * @return [KClass]<[JavaArray]>
 */
val FloatArrayKType get() = FloatArrayType.kotlin

/**
 * 获得 [Long] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "long[]"
 * @return [KClass]<[JavaArray]>
 */
val LongArrayKType get() = LongArrayType.kotlin

/**
 * 获得 [Double] - [Array] 类型
 *
 * 这是 Java 原始类型 (Primitive Type) 数组 - 它在字节码中的关键字为 "double[]"
 * @return [KClass]<[JavaArray]>
 */
val DoubleArrayKType get() = DoubleArrayType.kotlin

/**
 * 获得 [Any] - [Array] 类型
 *
 * 它在 Java 中表示为：Object[]
 * @return [KClass]<[JavaArray]>
 */
val AnyArrayKClass get() = AnyArrayClass.kotlin

/**
 * 获得 [Boolean] - [Array] 类型
 *
 * 它在 Java 中表示为：Boolean[]
 * @return [KClass]<[JavaArray]>
 */
val BooleanArrayKClass get() = BooleanArrayClass.kotlin

/**
 * 获得 [Char] - [Array] 类型
 *
 * 它在 Java 中表示为：Character[]
 * @return [KClass]<[JavaArray]>
 */
val CharArrayKClass get() = CharArrayClass.kotlin

/**
 * 获得 [Byte] - [Array] 类型
 *
 * 它在 Java 中表示为：Byte[]
 * @return [KClass]<[JavaArray]>
 */
val ByteArrayKClass get() = ByteArrayClass.kotlin

/**
 * 获得 [Short] - [Array] 类型
 *
 * 它在 Java 中表示为：Short[]
 * @return [KClass]<[JavaArray]>
 */
val ShortArrayKClass get() = ShortArrayClass.kotlin

/**
 * 获得 [Int] - [Array] 类型
 *
 * 它在 Java 中表示为：Integer[]
 * @return [KClass]<[JavaArray]>
 */
val IntArrayKClass get() = IntArrayClass.kotlin

/**
 * 获得 [Float] - [Array] 类型
 *
 * 它在 Java 中表示为：Float[]
 * @return [KClass]<[JavaArray]>
 */
val FloatArrayKClass get() = FloatArrayClass.kotlin

/**
 * 获得 [Long] - [Array] 类型
 *
 * 它在 Java 中表示为：Long[]
 * @return [KClass]<[JavaArray]>
 */
val LongArrayKClass get() = LongArrayClass.kotlin

/**
 * 获得 [Double] - [Array] 类型
 *
 * 它在 Java 中表示为：Double[]
 * @return [KClass]<[JavaArray]>
 */
val DoubleArrayKClass get() = DoubleArrayClass.kotlin

/**
 * 获得 [Number] - [Array] 类型
 *
 * 它在 Java 中表示为：Number[]
 * @return [KClass]<[JavaArray]>
 */
val NumberArrayKClass get() = NumberArrayClass.kotlin

/**
 * 获得 [String] - [Array] 类型
 *
 * 它在 Java 中表示为：String[]
 * @return [KClass]<[JavaArray]>
 */
val StringArrayKClass get() = StringArrayClass.kotlin

/**
 * 获得 [CharSequence] - [Array] 类型
 *
 * 它在 Java 中表示为：CharSequence[]
 * @return [KClass]<[JavaArray]>
 */
val CharSequenceArrayKClass get() = CharSequenceArrayClass.kotlin

/**
 * 获得 [Cloneable] 类型
 * @return [KClass]<[Cloneable]>
 */
val CloneableKClass get() = CloneableClass.kotlin

/**
 * 获得 [List] 类型
 * @return [KClass]<[List]>
 */
val ListKClass get() = ListClass.kotlin

/**
 * 获得 [ArrayList] 类型
 * @return [KClass]<[ArrayList]>
 */
val ArrayListKClass get() = ArrayListClass.kotlin

/**
 * 获得 [HashMap] 类型
 * @return [KClass]<[HashMap]>
 */
val HashMapKClass get() = HashMapClass.kotlin

/**
 * 获得 [HashSet] 类型
 * @return [KClass]<[HashSet]>
 */
val HashSetKClass get() = HashSetClass.kotlin

/**
 * 获得 [WeakHashMap] 类型
 * @return [KClass]<[WeakHashMap]>
 */
val WeakHashMapKClass get() = WeakHashMapClass.kotlin

/**
 * 获得 [WeakReference] 类型
 * @return [KClass]<[WeakReference]>
 */
val WeakReferenceKClass get() = WeakReferenceClass.kotlin

/**
 * 获得 [Enum] 类型
 * @return [KClass]<[Enum]>
 */
val EnumKClass get() = EnumClass.kotlin

/**
 * 获得 [Map] 类型
 * @return [KClass]<[Map]>
 */
val MapKClass get() = MapClass.kotlin

/**
 * 获得 [Map.Entry] 类型
 * @return [KClass]<[Map.Entry]>
 */
val Map_EntryKClass get() = Map_EntryClass.kotlin

/**
 * 获得 [Reference] 类型
 * @return [KClass]<[Reference]>
 */
val ReferenceKClass get() = ReferenceClass.kotlin

/**
 * 获得 [Vector] 类型
 * @return [KClass]<[Vector]>
 */
val VectorKClass get() = VectorClass.kotlin

/**
 * 获得 [File] 类型
 * @return [KClass]<[File]>
 */
val FileKClass get() = FileClass.kotlin

/**
 * 获得 [InputStream] 类型
 * @return [KClass]<[InputStream]>
 */
val InputStreamKClass get() = InputStreamClass.kotlin

/**
 * 获得 [OutputStream] 类型
 * @return [KClass]<[OutputStream]>
 */
val OutputStreamKClass get() = OutputStreamClass.kotlin

/**
 * 获得 [BufferedReader] 类型
 * @return [KClass]<[BufferedReader]>
 */
val BufferedReaderKClass get() = BufferedReaderClass.kotlin

/**
 * 获得 [Date] 类型
 * @return [KClass]<[Date]>
 */
val DateKClass get() = DateClass.kotlin

/**
 * 获得 [TimeZone] 类型
 * @return [KClass]<[TimeZone]>
 */
val TimeZoneKClass get() = TimeZoneClass.kotlin

/**
 * 获得 [SimpleDateFormat] 类型
 * @return [KClass]<[SimpleDateFormat]>
 */
val SimpleDateFormatKClass_Java get() = SimpleDateFormatClass_Java.kotlin

/**
 * 获得 [Timer] 类型
 * @return [KClass]<[Timer]>
 */
val TimerKClass get() = TimerClass.kotlin

/**
 * 获得 [TimerTask] 类型
 * @return [KClass]<[TimerTask]>
 */
val TimerTaskKClass get() = TimerTaskClass.kotlin

/**
 * 获得 [Thread] 类型
 * @return [KClass]<[Thread]>
 */
val ThreadKClass get() = ThreadClass.kotlin

/**
 * 获得 [Base64] 类型
 *
 * - 在 Android O (26) 及以上系统加入
 * @return [KClass]<[Base64]> or null
 */
val Base64KClass_Java get() = Base64Class_Java?.kotlin

/**
 * 获得 [Observer] 类型
 * @return [KClass]<[Observer]>
 */
val ObserverKClass get() = ObserverClass.kotlin

/**
 * 获得 [Set] 类型
 * @return [KClass]<[Set]>
 */
val SetKClass get() = SetClass.kotlin

/**
 * 获得 [JSONObject] 类型
 * @return [KClass]<[JSONObject]>
 */
val JSONObjectKClass get() = JSONObjectClass.kotlin

/**
 * 获得 [JSONArray] 类型
 * @return [KClass]<[JSONArray]>
 */
val JSONArrayKClass get() = JSONArrayClass.kotlin

/**
 * 获得 [StringBuilder] 类型
 * @return [KClass]<[StringBuilder]>
 */
val StringBuilderKClass get() = StringBuilderClass.kotlin

/**
 * 获得 [StringBuffer] 类型
 * @return [KClass]<[StringBuffer]>
 */
val StringBufferKClass get() = StringBufferClass.kotlin

/**
 * 获得 [ZipEntry] 类型
 * @return [KClass]<[ZipEntry]>
 */
val ZipEntryKClass get() = ZipEntryClass.kotlin

/**
 * 获得 [ZipFile] 类型
 * @return [KClass]<[ZipFile]>
 */
val ZipFileKClass get() = ZipFileClass.kotlin

/**
 * 获得 [ZipInputStream] 类型
 * @return [KClass]<[ZipInputStream]>
 */
val ZipInputStreamKClass get() = ZipInputStreamClass.kotlin

/**
 * 获得 [ZipOutputStream] 类型
 * @return [KClass]<[ZipOutputStream]>
 */
val ZipOutputStreamKClass get() = ZipOutputStreamClass.kotlin

/**
 * 获得 [HttpURLConnection] 类型
 * @return [KClass]<[HttpURLConnection]>
 */
val HttpURLConnectionKClass get() = HttpURLConnectionClass.kotlin

/**
 * 获得 [HttpCookie] 类型
 * @return [KClass]<[HttpCookie]>
 */
val HttpCookieKClass get() = HttpCookieClass.kotlin

/**
 * 获得 [HttpClient] 类型
 * @return [KClass] or null
 */
val HttpClientKClass get() = HttpClientClass?.kotlin

/**
 * 获得 [AtomicBoolean] 类型
 * @return [KClass]<[AtomicBoolean]>
 */
val AtomicBooleanKClass get() = AtomicBooleanClass.kotlin

/**
 * 获得 [Supplier] 类型
 * @return [KClass]<[Supplier]>
 */
val SupplierKClass get() = SupplierClass.kotlin

/**
 * 获得 [KClass] 类型
 * @return [KClass]<[KClass]>
 */
val JavaKClass get() = JavaClass.kotlin

/**
 * 获得 [ClassLoader] 类型
 * @return [KClass]<[ClassLoader]>
 */
val JavaKClassLoader get() = JavaClassLoader.kotlin

/**
 * 获得 [BaseDexClassLoader] 类型
 * @return [KClass]<[BaseDexClassLoader]>
 */
val DalvikBaseDexClassLoaderKotlin get() = DalvikBaseDexClassLoader.kotlin

/**
 * 获得 [DexClassLoader] 类型
 * @return [KClass]<[DexClassLoader]>
 */
val DalvikDexClassLoaderKotlin get() = DalvikDexClassLoader.kotlin

/**
 * 获得 [PathClassLoader] 类型
 * @return [KClass]<[PathClassLoader]>
 */
val DalvikPathClassLoaderKotlin get() = DalvikPathClassLoader.kotlin

/**
 * 获得 [InMemoryDexClassLoader] 类型
 * @return [KClass]<[InMemoryDexClassLoader]>
 */
val DalvikInMemoryDexClassLoaderKotlin get() = DalvikInMemoryDexClassLoader.kotlin

/**
 * 获得 [Method] 类型
 * @return [KClass]<[Method]>
 */
val JavaMethodKClass get() = JavaMethodClass.kotlin

/**
 * 获得 [Field] 类型
 * @return [KClass]<[Field]>
 */
val JavaFieldKClass get() = JavaFieldClass.kotlin

/**
 * 获得 [Constructor] 类型
 * @return [KClass]<[Constructor]>
 */
val JavaConstructorKClass get() = JavaConstructorClass.kotlin

/**
 * 获得 [Member] 类型
 * @return [KClass]<[Member]>
 */
val JavaMemberKClass get() = JavaMemberClass.kotlin

/**
 * 获得 [Annotation] 类型
 * @return [KClass]<[Annotation]>
 */
val JavaAnnotationKClass get() = JavaAnnotationClass.kotlin

/**
 * 获得 [java.util.function.Function] 类型
 * @return [KClass]<[JavaFunction]>
 */
val FunctionKClass get() = FunctionClass.kotlin

/**
 * 获得 [Optional] 类型
 * @return [KClass]<[Optional]>
 */
val OptionalKClass get() = OptionalClass.kotlin

/**
 * 获得 [OptionalInt] 类型
 * @return [KClass]<[OptionalInt]>
 */
val OptionalIntKClass get() = OptionalIntClass.kotlin

/**
 * 获得 [OptionalLong] 类型
 * @return [KClass]<[OptionalLong]>
 */
val OptionalLongKClass get() = OptionalLongClass.kotlin

/**
 * 获得 [OptionalDouble] 类型
 * @return [KClass]<[OptionalDouble]>
 */
val OptionalDoubleKClass get() = OptionalDoubleClass.kotlin

/**
 * 获得 [Objects] 类型
 * @return [KClass]<[Objects]>
 */
val ObjectsKClass get() = ObjectsClass.kotlin

/**
 * 获得 [Runtime] 类型
 * @return [KClass]<[Runtime]>
 */
val RuntimeKClass get() = RuntimeClass.kotlin

/**
 * 获得 [NullPointerException] 类型
 * @return [KClass]<[NullPointerException]>
 */
val NullPointerExceptionKClass get() = NullPointerExceptionClass.kotlin

/**
 * 获得 [NumberFormatException] 类型
 * @return [KClass]<[NumberFormatException]>
 */
val NumberFormatExceptionKClass get() = NumberFormatExceptionClass.kotlin

/**
 * 获得 [IllegalStateException] 类型
 * @return [KClass]<[IllegalStateException]>
 */
val IllegalStateExceptionKClass get() = IllegalStateExceptionClass.kotlin

/**
 * 获得 [RuntimeException] 类型
 * @return [KClass]<[RuntimeException]>
 */
val RuntimeExceptionKClass get() = RuntimeExceptionClass.kotlin

/**
 * 获得 [ClassNotFoundException] 类型
 * @return [KClass]<[ClassNotFoundException]>
 */
val ClassNotFoundExceptionKClass get() = ClassNotFoundExceptionClass.kotlin

/**
 * 获得 [NoClassDefFoundError] 类型
 * @return [KClass]<[NoClassDefFoundError]>
 */
val NoClassDefFoundErrorKClass get() = NoClassDefFoundErrorClass.kotlin

/**
 * 获得 [NoSuchMethodError] 类型
 * @return [KClass]<[NoSuchMethodError]>
 */
val NoSuchMethodErrorKClass get() = NoSuchMethodErrorClass.kotlin

/**
 * 获得 [NoSuchFieldError] 类型
 * @return [KClass]<[NoSuchFieldError]>
 */
val NoSuchFieldErrorKClass get() = NoSuchFieldErrorClass.kotlin

/**
 * 获得 [Error] 类型
 * @return [KClass]<[Error]>
 */
val ErrorKClass get() = ErrorClass.kotlin

/**
 * 获得 [Exception] 类型
 * @return [KClass]<[Exception]>
 */
val ExceptionKClass get() = ExceptionClass.kotlin

/**
 * 获得 [Throwable] 类型
 * @return [KClass]<[Throwable]>
 */
val ThrowableKClass get() = ThrowableClass.kotlin

/**
 * 获得 [KProperty] 类型
 * @return [KClass]<[KProperty]>
 */
val KPropertyKClass get() = kclassOf<KProperty<*>>()

/**
 * 获得 [KFunction] 类型
 * @return [KClass]<[KFunction]>
 */
val KFunctionKClass get() = kclassOf<KFunction<*>>()

/**
 * 获得 [KCallable] 类型
 * @return [KClass]<[KCallable]>
 */
val KCallableKClass get() = kclassOf<KCallable<*>>()

/**
 * 获得 [KClass] 类型
 * @return [KClass]<[KClass]>
 */
val KotlinKClass get() = kclassOf<KClass<*>>()

/**
 * 获得 [KType] 类型
 * @return [KClass]<[KType]>
 */
val KTypeKClass get() = kclassOf<KType>()

/**
 * 获得 [KClassifier] 类型
 * @return [KClass]<[KClassifier]>
 */
val KClassifierKClass get() = kclassOf<KClassifier>()

/**
 * 获得 [KTypeParameter] 类型
 * @return [KClass]<[KTypeParameter]>
 */
val KTypeParameterKClass get() = kclassOf<KTypeParameter>()

/**
 * 获得 [KParameter] 类型
 * @return [KClass]<[KParameter]>
 */
val KParameterKClass get() = kclassOf<KParameter>()

/**
 * 获得 [KTypeProjection] 类型
 * @return [KClass]<[KTypeProjection]>
 */
val KTypeProjectionKClass get() = kclassOf<KTypeProjection>()

/**
 * 获得 [KVariance] 类型
 * @return [KClass]<[KVariance]>
 */
val KVarianceKClass get() = kclassOf<KVariance>()

/**
 * 获得 [KMutableProperty] 类型
 * @return [KClass]<[KMutableProperty]>
 */
val KMutablePropertyKClass get() = kclassOf<KMutableProperty<*>>()

/**
 * 获得 [kotlin.reflect.jvm.internal.KCallableImpl] 类型
 * @return [KClass]<[kotlin.reflect.jvm.internal.KCallableImpl]>
 */
val KCallableImplKClass get() = "kotlin.reflect.jvm.internal.KCallableImpl".toKClass()

/**
 * 获得 [kotlin.reflect.jvm.internal.KPackageImpl] 类型
 * @return [KClass]<[kotlin.reflect.jvm.internal.KPackageImpl]>
 */
val KPackageImplKClass get() = "kotlin.reflect.jvm.internal.KPackageImpl".toKClass()