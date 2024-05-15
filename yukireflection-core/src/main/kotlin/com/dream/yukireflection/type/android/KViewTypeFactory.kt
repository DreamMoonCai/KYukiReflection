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
@file:Suppress("unused")

package com.dream.yukireflection.type.android

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.appwidget.AppWidgetHostView
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.Surface
import android.view.SurfaceView
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.ViewPropertyAnimator
import android.view.ViewStructure
import android.view.ViewStub
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.RemoteViews
import android.widget.RemoteViews.RemoteView
import android.widget.TextClock
import android.widget.TextView
import android.widget.VideoView
import android.widget.ViewAnimator
import com.highcapable.yukireflection.type.android.*
import kotlin.reflect.KClass

/**
 * 获得 [View] 类型
 * @return [KClass]<[View]>
 */
val ViewKClass get() = ViewClass.kotlin

/**
 * 获得 [Surface] 类型
 * @return [KClass]<[Surface]>
 */
val SurfaceKClass get() = SurfaceClass.kotlin

/**
 * 获得 [SurfaceView] 类型
 * @return [KClass]<[SurfaceView]>
 */
val SurfaceViewKClass get() = SurfaceViewClass.kotlin

/**
 * 获得 [TextureView] 类型
 * @return [KClass]<[TextureView]>
 */
val TextureViewKClass get() = TextureViewClass.kotlin

/**
 * 获得 [WebView] 类型
 * @return [KClass]<[WebView]>
 */
val WebViewKClass get() = WebViewClass.kotlin

/**
 * 获得 [WebViewClient] 类型
 * @return [KClass]<[WebViewClient]>
 */
val WebViewClientKClass get() = WebViewClientClass.kotlin

/**
 * 获得 [ViewStructure] 类型
 * @return [KClass]<[ViewStructure]>
 */
val ViewStructureKClass get() = ViewStructureClass.kotlin

/**
 * 获得 [ViewGroup] 类型
 * @return [KClass]<[ViewGroup]>
 */
val ViewGroupKClass get() = ViewGroupClass.kotlin

/**
 * 获得 [ViewParent] 类型
 * @return [KClass]<[ViewParent]>
 */
val ViewParentKClass get() = ViewParentClass.kotlin

/**
 * 获得 [AppWidgetHostView] 类型
 * @return [KClass]<[AppWidgetHostView]>
 */
val AppWidgetHostViewKClass get() = AppWidgetHostViewClass.kotlin

/**
 * 获得 [RemoteViews] 类型
 * @return [KClass]<[RemoteViews]>
 */
val RemoteViewsKClass get() = RemoteViewsClass.kotlin

/**
 * 获得 [RemoteView] 类型
 * @return [KClass]<[RemoteView]>
 */
val RemoteViewKClass get() = RemoteViewClass.kotlin

/**
 * 获得 [TextView] 类型
 * @return [KClass]<[TextView]>
 */
val TextViewKClass get() = TextViewClass.kotlin

/**
 * 获得 [ImageView] 类型
 * @return [KClass]<[ImageView]>
 */
val ImageViewKClass get() = ImageViewClass.kotlin

/**
 * 获得 [ImageButton] 类型
 * @return [KClass]<[ImageButton]>
 */
val ImageButtonKClass get() = ImageButtonClass.kotlin

/**
 * 获得 [EditText] 类型
 * @return [KClass]<[EditText]>
 */
val EditTextKClass get() = EditTextClass.kotlin

/**
 * 获得 [Button] 类型
 * @return [KClass]<[Button]>
 */
val ButtonKClass get() = ButtonClass.kotlin

/**
 * 获得 [CheckBox] 类型
 * @return [KClass]<[CheckBox]>
 */
val CheckBoxKClass get() = CheckBoxClass.kotlin

/**
 * 获得 [CompoundButton] 类型
 * @return [KClass]<[CompoundButton]>
 */
val CompoundButtonKClass get() = CompoundButtonClass.kotlin

/**
 * 获得 [VideoView] 类型
 * @return [KClass]<[VideoView]>
 */
val VideoViewKClass get() = VideoViewClass.kotlin

/**
 * 获得 [ListView] 类型
 * @return [KClass]<[ListView]>
 */
val ListViewKClass get() = ListViewClass.kotlin

/**
 * 获得 [LayoutInflater] 类型
 * @return [KClass]<[LayoutInflater]>
 */
val LayoutInflaterKClass get() = LayoutInflaterClass.kotlin

/**
 * 获得 [LayoutInflater.Filter] 类型
 * @return [KClass]<[LayoutInflater.Filter]>
 */
val LayoutInflater_FilterKClass get() = LayoutInflater_FilterClass.kotlin

/**
 * 获得 [LayoutInflater.Factory] 类型
 * @return [KClass]<[LayoutInflater.Factory]>
 */
val LayoutInflater_FactoryKClass get() = LayoutInflater_FactoryClass.kotlin

/**
 * 获得 [LayoutInflater.Factory2] 类型
 * @return [KClass]<[LayoutInflater.Factory2]>
 */
val LayoutInflater_Factory2KClass get() = LayoutInflater_Factory2Class.kotlin

/**
 * 获得 [ListAdapter] 类型
 * @return [KClass]<[ListAdapter]>
 */
val ListAdapterKClass get() = ListAdapterClass.kotlin

/**
 * 获得 [ArrayAdapter] 类型
 * @return [KClass]<[ArrayAdapter]>
 */
val ArrayAdapterKClass get() = ArrayAdapterClass.kotlin

/**
 * 获得 [BaseAdapter] 类型
 * @return [KClass]<[BaseAdapter]>
 */
val BaseAdapterKClass get() = BaseAdapterClass.kotlin

/**
 * 获得 [RelativeLayout] 类型
 * @return [KClass]<[RelativeLayout]>
 */
val RelativeLayoutKClass get() = RelativeLayoutClass.kotlin

/**
 * 获得 [FrameLayout] 类型
 * @return [KClass]<[FrameLayout]>
 */
val FrameLayoutKClass get() = FrameLayoutClass.kotlin

/**
 * 获得 [LinearLayout] 类型
 * @return [KClass]<[LinearLayout]>
 */
val LinearLayoutKClass get() = LinearLayoutClass.kotlin

/**
 * 获得 [ViewGroup.LayoutParams] 类型
 * @return [KClass]<[ViewGroup.LayoutParams]>
 */
val ViewGroup_LayoutParamsKClass get() = ViewGroup_LayoutParamsClass.kotlin

/**
 * 获得 [RelativeLayout.LayoutParams] 类型
 * @return [KClass]<[RelativeLayout.LayoutParams]>
 */
val RelativeLayout_LayoutParamsKClass get() = RelativeLayout_LayoutParamsClass.kotlin

/**
 * 获得 [LinearLayout.LayoutParams] 类型
 * @return [KClass]<[LinearLayout.LayoutParams]>
 */
val LinearLayout_LayoutParamsKClass get() = LinearLayout_LayoutParamsClass.kotlin

/**
 * 获得 [FrameLayout.LayoutParams] 类型
 * @return [KClass]<[FrameLayout.LayoutParams]>
 */
val FrameLayout_LayoutParamsKClass get() = FrameLayout_LayoutParamsClass.kotlin

/**
 * 获得 [TextClock] 类型
 * @return [KClass]<[TextClock]>
 */
val TextClockKClass get() = TextClockClass.kotlin

/**
 * 获得 [MotionEvent] 类型
 * @return [KClass]<[MotionEvent]>
 */
val MotionEventKClass get() = MotionEventClass.kotlin

/**
 * 获得 [View.OnClickListener] 类型
 * @return [KClass]<[View.OnClickListener]>
 */
val View_OnClickListenerKClass get() = View_OnClickListenerClass.kotlin

/**
 * 获得 [View.OnLongClickListener] 类型
 * @return [KClass]<[View.OnLongClickListener]>
 */
val View_OnLongClickListenerKClass get() = View_OnLongClickListenerClass.kotlin

/**
 * 获得 [View.OnTouchListener] 类型
 * @return [KClass]<[View.OnTouchListener]>
 */
val View_OnTouchListenerKClass get() = View_OnTouchListenerClass.kotlin

/**
 * 获得 [AutoCompleteTextView] 类型
 * @return [KClass]<[AutoCompleteTextView]>
 */
val AutoCompleteTextViewKClass get() = AutoCompleteTextViewClass.kotlin

/**
 * 获得 [ViewStub] 类型
 * @return [KClass]<[ViewStub]>
 */
val ViewStubKClass get() = ViewStubClass.kotlin

/**
 * 获得 [ViewStub.OnInflateListener] 类型
 * @return [KClass]<[ViewStub.OnInflateListener]>
 */
val ViewStub_OnInflateListenerKClass get() = ViewStub_OnInflateListenerClass.kotlin

/**
 * 获得 [GestureDetector] 类型
 * @return [KClass]<[GestureDetector]>
 */
val GestureDetectorKClass get() = GestureDetectorClass.kotlin

/**
 * 获得 [GestureDetector.SimpleOnGestureListener] 类型
 * @return [KClass]<[GestureDetector.SimpleOnGestureListener]>
 */
val GestureDetector_SimpleOnGestureListenerKClass get() = GestureDetector_SimpleOnGestureListenerClass.kotlin

/**
 * 获得 [ProgressBar] 类型
 * @return [KClass]<[ProgressBar]>
 */
val ProgressBarKClass get() = ProgressBarClass.kotlin

/**
 * 获得 [AttributeSet] 类型
 * @return [KClass]<[AttributeSet]>
 */
val AttributeSetKClass get() = AttributeSetClass.kotlin

/**
 * 获得 [Animation] 类型
 * @return [KClass]<[Animation]>
 */
val AnimationKClass get() = AnimationClass.kotlin

/**
 * 获得 [Animation.AnimationListener] 类型
 * @return [KClass]<[Animation.AnimationListener]>
 */
val Animation_AnimationListenerKClass get() = Animation_AnimationListenerClass.kotlin

/**
 * 获得 [TranslateAnimation] 类型
 * @return [KClass]<[TranslateAnimation]>
 */
val TranslateAnimationKClass get() = TranslateAnimationClass.kotlin

/**
 * 获得 [AlphaAnimation] 类型
 * @return [KClass]<[AlphaAnimation]>
 */
val AlphaAnimationKClass get() = AlphaAnimationClass.kotlin

/**
 * 获得 [Animator] 类型
 * @return [KClass]<[Animator]>
 */
val AnimatorKClass get() = AnimatorClass.kotlin

/**
 * 获得 [Animator.AnimatorListener] 类型
 * @return [KClass]<[Animator.AnimatorListener]>
 */
val Animator_AnimatorListenerKClass get() = Animator_AnimatorListenerClass.kotlin

/**
 * 获得 [ObjectAnimator] 类型
 * @return [KClass]<[ObjectAnimator]>
 */
val ObjectAnimatorKClass get() = ObjectAnimatorClass.kotlin

/**
 * 获得 [ValueAnimator] 类型
 * @return [KClass]<[ValueAnimator]>
 */
val ValueAnimatorKClass get() = ValueAnimatorClass.kotlin

/**
 * 获得 [ValueAnimator.AnimatorUpdateListener] 类型
 * @return [KClass]<[ValueAnimator.AnimatorUpdateListener]>
 */
val ValueAnimator_AnimatorUpdateListenerKClass get() = ValueAnimator_AnimatorUpdateListenerClass.kotlin

/**
 * 获得 [ViewAnimator] 类型
 * @return [KClass]<[ViewAnimator]>
 */
val ViewAnimatorKClass get() = ViewAnimatorClass.kotlin

/**
 * 获得 [AnimatorSet] 类型
 * @return [KClass]<[AnimatorSet]>
 */
val AnimatorSetKClass get() = AnimatorSetClass.kotlin

/**
 * 获得 [AnimatorSet.Builder] 类型
 * @return [KClass]<[AnimatorSet.Builder]>
 */
val AnimatorSet_BuilderKClass get() = AnimatorSet_BuilderClass.kotlin

/**
 * 获得 [PropertyValuesHolder] 类型
 * @return [KClass]<[PropertyValuesHolder]>
 */
val PropertyValuesHolderKClass get() = PropertyValuesHolderClass.kotlin

/**
 * 获得 [ViewPropertyAnimator] 类型
 * @return [KClass]<[ViewPropertyAnimator]>
 */
val ViewPropertyAnimatorKClass get() = ViewPropertyAnimatorClass.kotlin

/**
 * 获得 [View.MeasureSpec] 类型
 * @return [KClass]<[View.MeasureSpec]>
 */
val View_MeasureSpecKClass get() = View_MeasureSpecClass.kotlin