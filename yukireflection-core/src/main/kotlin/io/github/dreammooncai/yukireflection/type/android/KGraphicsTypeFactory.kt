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
 * This file is created by fankes on 2022/2/13.
 * This file is modified by fankes on 2023/1/21.
 */
@file:Suppress("unused", "KDocUnresolvedReference")

package io.github.dreammooncai.yukireflection.type.android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.NinePatch
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.Icon
import android.os.Build
import android.text.Editable
import android.text.GetChars
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Size
import android.util.SizeF
import io.github.dreammooncai.yukireflection.factory.klassOf
import kotlin.reflect.KClass

/**
 * 获得 [Typeface] 类型
 * @return [KClass]<[Typeface]>
 */
val TypefaceKClass get() = klassOf<Typeface>()

/**
 * 获得 [Bitmap] 类型
 * @return [KClass]<[Bitmap]>
 */
val BitmapKClass get() = klassOf<Bitmap>()

/**
 * 获得 [Icon] 类型
 *
 * - 在 Android M (23) 及以上系统加入
 * @return [KClass]<[Icon]> or null
 */
val IconKClass get() = if (Build.VERSION.SDK_INT >= 23) klassOf<Icon>() else null

/**
 * 获得 [Outline] 类型
 * @return [KClass]<[Outline]>
 */
val OutlineKClass get() = klassOf<Outline>()

/**
 * 获得 [Drawable] 类型
 * @return [KClass]<[Drawable]>
 */
val DrawableKClass get() = klassOf<Drawable>()

/**
 * 获得 [GradientDrawable] 类型
 * @return [KClass]<[GradientDrawable]>
 */
val GradientDrawableKClass get() = klassOf<GradientDrawable>()

/**
 * 获得 [ColorDrawable] 类型
 * @return [KClass]<[ColorDrawable]>
 */
val ColorDrawableKClass get() = klassOf<ColorDrawable>()

/**
 * 获得 [BitmapDrawable] 类型
 * @return [KClass]<[BitmapDrawable]>
 */
val BitmapDrawableKClass get() = klassOf<BitmapDrawable>()

/**
 * 获得 [Size] 类型
 * @return [KClass]<[Size]>
 */
val SizeKClass get() = klassOf<Size>()

/**
 * 获得 [SizeF] 类型
 * @return [KClass]<[SizeF]>
 */
val SizeFKClass get() = klassOf<SizeF>()

/**
 * 获得 [Rect] 类型
 * @return [KClass]<[Rect]>
 */
val RectKClass get() = klassOf<Rect>()

/**
 * 获得 [RectF] 类型
 * @return [KClass]<[RectF]>
 */
val RectFKClass get() = klassOf<RectF>()

/**
 * 获得 [NinePatch] 类型
 * @return [KClass]<[NinePatch]>
 */
val NinePatchKClass get() = klassOf<NinePatch>()

/**
 * 获得 [Paint] 类型
 * @return [KClass]<[Paint]>
 */
val PaintKClass get() = klassOf<Paint>()

/**
 * 获得 [TextPaint] 类型
 * @return [KClass]<[TextPaint]>
 */
val TextPaintKClass get() = klassOf<TextPaint>()

/**
 * 获得 [Canvas] 类型
 * @return [KClass]<[Canvas]>
 */
val CanvasKClass get() = klassOf<Canvas>()

/**
 * 获得 [Point] 类型
 * @return [KClass]<[Point]>
 */
val PointKClass get() = klassOf<Point>()

/**
 * 获得 [PointF] 类型
 * @return [KClass]<[PointF]>
 */
val PointFKClass get() = klassOf<PointF>()

/**
 * 获得 [Matrix] 类型
 * @return [KClass]<[Matrix]>
 */
val MatrixKClass get() = klassOf<Matrix>()

/**
 * 获得 [ColorMatrix] 类型
 * @return [KClass]<[ColorMatrix]>
 */
val ColorMatrixKClass get() = klassOf<ColorMatrix>()

/**
 * 获得 [ColorMatrixColorFilter] 类型
 * @return [KClass]<[ColorMatrixColorFilter]>
 */
val ColorMatrixColorFilterKClass get() = klassOf<ColorMatrixColorFilter>()

/**
 * 获得 [TextUtils] 类型
 * @return [KClass]<[TextUtils]>
 */
val TextUtilsKClass get() = klassOf<TextUtils>()

/**
 * 获得 [Editable] 类型
 * @return [KClass]<[Editable]>
 */
val EditableKClass get() = klassOf<Editable>()

/**
 * 获得 [TextWatcher] 类型
 * @return [KClass]<[TextWatcher]>
 */
val TextWatcherKClass get() = klassOf<TextWatcher>()

/**
 * 获得 [Editable.Factory] 类型
 * @return [KClass]<[Editable.Factory]>
 */
val Editable_FactoryKClass get() = klassOf<Editable.Factory>()

/**
 * 获得 [GetChars] 类型
 * @return [KClass]<[GetChars]>
 */
val GetCharsKClass get() = klassOf<GetChars>()

/**
 * 获得 [Spannable] 类型
 * @return [KClass]<[Spannable]>
 */
val SpannableKClass get() = klassOf<Spannable>()

/**
 * 获得 [SpannableStringBuilder] 类型
 * @return [KClass]<[SpannableStringBuilder]>
 */
val SpannableStringBuilderKClass get() = klassOf<SpannableStringBuilder>()

/**
 * 获得 [BitmapFactory] 类型
 * @return [KClass]<[BitmapFactory]>
 */
val BitmapFactoryKClass get() = klassOf<BitmapFactory>()

/**
 * 获得 [BitmapFactory.Options] 类型
 * @return [KClass]<[BitmapFactory.Options]>
 */
val BitmapFactory_OptionsKClass get() = klassOf<BitmapFactory.Options>()