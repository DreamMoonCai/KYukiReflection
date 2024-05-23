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
import com.dream.yukireflection.factory.kclassOf
import kotlin.reflect.KClass

/**
 * 获得 [View] 类型
 * @return [KClass]<[View]>
 */
val ViewKClass get() = kclassOf<View>()

/**
 * 获得 [Surface] 类型
 * @return [KClass]<[Surface]>
 */
val SurfaceKClass get() = kclassOf<Surface>()

/**
 * 获得 [SurfaceView] 类型
 * @return [KClass]<[SurfaceView]>
 */
val SurfaceViewKClass get() = kclassOf<SurfaceView>()

/**
 * 获得 [TextureView] 类型
 * @return [KClass]<[TextureView]>
 */
val TextureViewKClass get() = kclassOf<TextureView>()

/**
 * 获得 [WebView] 类型
 * @return [KClass]<[WebView]>
 */
val WebViewKClass get() = kclassOf<WebView>()

/**
 * 获得 [WebViewClient] 类型
 * @return [KClass]<[WebViewClient]>
 */
val WebViewClientKClass get() = kclassOf<WebViewClient>()

/**
 * 获得 [ViewStructure] 类型
 * @return [KClass]<[ViewStructure]>
 */
val ViewStructureKClass get() = kclassOf<ViewStructure>()

/**
 * 获得 [ViewGroup] 类型
 * @return [KClass]<[ViewGroup]>
 */
val ViewGroupKClass get() = kclassOf<ViewGroup>()

/**
 * 获得 [ViewParent] 类型
 * @return [KClass]<[ViewParent]>
 */
val ViewParentKClass get() = kclassOf<ViewParent>()

/**
 * 获得 [AppWidgetHostView] 类型
 * @return [KClass]<[AppWidgetHostView]>
 */
val AppWidgetHostViewKClass get() = kclassOf<AppWidgetHostView>()

/**
 * 获得 [RemoteViews] 类型
 * @return [KClass]<[RemoteViews]>
 */
val RemoteViewsKClass get() = kclassOf<RemoteViews>()

/**
 * 获得 [RemoteView] 类型
 * @return [KClass]<[RemoteView]>
 */
val RemoteViewKClass get() = kclassOf<RemoteView>()

/**
 * 获得 [TextView] 类型
 * @return [KClass]<[TextView]>
 */
val TextViewKClass get() = kclassOf<TextView>()

/**
 * 获得 [ImageView] 类型
 * @return [KClass]<[ImageView]>
 */
val ImageViewKClass get() = kclassOf<ImageView>()

/**
 * 获得 [ImageButton] 类型
 * @return [KClass]<[ImageButton]>
 */
val ImageButtonKClass get() = kclassOf<ImageButton>()

/**
 * 获得 [EditText] 类型
 * @return [KClass]<[EditText]>
 */
val EditTextKClass get() = kclassOf<EditText>()

/**
 * 获得 [Button] 类型
 * @return [KClass]<[Button]>
 */
val ButtonKClass get() = kclassOf<Button>()

/**
 * 获得 [CheckBox] 类型
 * @return [KClass]<[CheckBox]>
 */
val CheckBoxKClass get() = kclassOf<CheckBox>()

/**
 * 获得 [CompoundButton] 类型
 * @return [KClass]<[CompoundButton]>
 */
val CompoundButtonKClass get() = kclassOf<CompoundButton>()

/**
 * 获得 [VideoView] 类型
 * @return [KClass]<[VideoView]>
 */
val VideoViewKClass get() = kclassOf<VideoView>()

/**
 * 获得 [ListView] 类型
 * @return [KClass]<[ListView]>
 */
val ListViewKClass get() = kclassOf<ListView>()

/**
 * 获得 [LayoutInflater] 类型
 * @return [KClass]<[LayoutInflater]>
 */
val LayoutInflaterKClass get() = kclassOf<LayoutInflater>()

/**
 * 获得 [LayoutInflater.Filter] 类型
 * @return [KClass]<[LayoutInflater.Filter]>
 */
val LayoutInflater_FilterKClass get() = kclassOf<LayoutInflater.Filter>()

/**
 * 获得 [LayoutInflater.Factory] 类型
 * @return [KClass]<[LayoutInflater.Factory]>
 */
val LayoutInflater_FactoryKClass get() = kclassOf<LayoutInflater.Factory>()

/**
 * 获得 [LayoutInflater.Factory2] 类型
 * @return [KClass]<[LayoutInflater.Factory2]>
 */
val LayoutInflater_Factory2KClass get() = kclassOf<LayoutInflater.Factory2>()

/**
 * 获得 [ListAdapter] 类型
 * @return [KClass]<[ListAdapter]>
 */
val ListAdapterKClass get() = kclassOf<ListAdapter>()

/**
 * 获得 [ArrayAdapter] 类型
 * @return [KClass]<[ArrayAdapter]>
 */
val ArrayAdapterKClass get() = kclassOf<ArrayAdapter<*>>()

/**
 * 获得 [BaseAdapter] 类型
 * @return [KClass]<[BaseAdapter]>
 */
val BaseAdapterKClass get() = kclassOf<BaseAdapter>()

/**
 * 获得 [RelativeLayout] 类型
 * @return [KClass]<[RelativeLayout]>
 */
val RelativeLayoutKClass get() = kclassOf<RelativeLayout>()

/**
 * 获得 [FrameLayout] 类型
 * @return [KClass]<[FrameLayout]>
 */
val FrameLayoutKClass get() = kclassOf<FrameLayout>()

/**
 * 获得 [LinearLayout] 类型
 * @return [KClass]<[LinearLayout]>
 */
val LinearLayoutKClass get() = kclassOf<LinearLayout>()

/**
 * 获得 [ViewGroup.LayoutParams] 类型
 * @return [KClass]<[ViewGroup.LayoutParams]>
 */
val ViewGroup_LayoutParamsKClass get() = kclassOf<ViewGroup.LayoutParams>()

/**
 * 获得 [RelativeLayout.LayoutParams] 类型
 * @return [KClass]<[RelativeLayout.LayoutParams]>
 */
val RelativeLayout_LayoutParamsKClass get() = kclassOf<RelativeLayout.LayoutParams>()

/**
 * 获得 [LinearLayout.LayoutParams] 类型
 * @return [KClass]<[LinearLayout.LayoutParams]>
 */
val LinearLayout_LayoutParamsKClass get() = kclassOf<LinearLayout.LayoutParams>()

/**
 * 获得 [FrameLayout.LayoutParams] 类型
 * @return [KClass]<[FrameLayout.LayoutParams]>
 */
val FrameLayout_LayoutParamsKClass get() = kclassOf<FrameLayout.LayoutParams>()

/**
 * 获得 [TextClock] 类型
 * @return [KClass]<[TextClock]>
 */
val TextClockKClass get() = kclassOf<TextClock>()

/**
 * 获得 [MotionEvent] 类型
 * @return [KClass]<[MotionEvent]>
 */
val MotionEventKClass get() = kclassOf<MotionEvent>()

/**
 * 获得 [View.OnClickListener] 类型
 * @return [KClass]<[View.OnClickListener]>
 */
val View_OnClickListenerKClass get() = kclassOf<View.OnClickListener>()

/**
 * 获得 [View.OnLongClickListener] 类型
 * @return [KClass]<[View.OnLongClickListener]>
 */
val View_OnLongClickListenerKClass get() = kclassOf<View.OnLongClickListener>()

/**
 * 获得 [View.OnTouchListener] 类型
 * @return [KClass]<[View.OnTouchListener]>
 */
val View_OnTouchListenerKClass get() = kclassOf<View.OnTouchListener>()

/**
 * 获得 [AutoCompleteTextView] 类型
 * @return [KClass]<[AutoCompleteTextView]>
 */
val AutoCompleteTextViewKClass get() = kclassOf<AutoCompleteTextView>()

/**
 * 获得 [ViewStub] 类型
 * @return [KClass]<[ViewStub]>
 */
val ViewStubKClass get() = kclassOf<ViewStub>()

/**
 * 获得 [ViewStub.OnInflateListener] 类型
 * @return [KClass]<[ViewStub.OnInflateListener]>
 */
val ViewStub_OnInflateListenerKClass get() = kclassOf<ViewStub.OnInflateListener>()

/**
 * 获得 [GestureDetector] 类型
 * @return [KClass]<[GestureDetector]>
 */
val GestureDetectorKClass get() = kclassOf<GestureDetector>()

/**
 * 获得 [GestureDetector.SimpleOnGestureListener] 类型
 * @return [KClass]<[GestureDetector.SimpleOnGestureListener]>
 */
val GestureDetector_SimpleOnGestureListenerKClass get() = kclassOf<GestureDetector.SimpleOnGestureListener>()

/**
 * 获得 [ProgressBar] 类型
 * @return [KClass]<[ProgressBar]>
 */
val ProgressBarKClass get() = kclassOf<ProgressBar>()

/**
 * 获得 [AttributeSet] 类型
 * @return [KClass]<[AttributeSet]>
 */
val AttributeSetKClass get() = kclassOf<AttributeSet>()

/**
 * 获得 [Animation] 类型
 * @return [KClass]<[Animation]>
 */
val AnimationKClass get() = kclassOf<Animation>()

/**
 * 获得 [Animation.AnimationListener] 类型
 * @return [KClass]<[Animation.AnimationListener]>
 */
val Animation_AnimationListenerKClass get() = kclassOf<Animation.AnimationListener>()

/**
 * 获得 [TranslateAnimation] 类型
 * @return [KClass]<[TranslateAnimation]>
 */
val TranslateAnimationKClass get() = kclassOf<TranslateAnimation>()

/**
 * 获得 [AlphaAnimation] 类型
 * @return [KClass]<[AlphaAnimation]>
 */
val AlphaAnimationKClass get() = kclassOf<AlphaAnimation>()

/**
 * 获得 [Animator] 类型
 * @return [KClass]<[Animator]>
 */
val AnimatorKClass get() = kclassOf<Animator>()

/**
 * 获得 [Animator.AnimatorListener] 类型
 * @return [KClass]<[Animator.AnimatorListener]>
 */
val Animator_AnimatorListenerKClass get() = kclassOf<Animator.AnimatorListener>()

/**
 * 获得 [ObjectAnimator] 类型
 * @return [KClass]<[ObjectAnimator]>
 */
val ObjectAnimatorKClass get() = kclassOf<ObjectAnimator>()

/**
 * 获得 [ValueAnimator] 类型
 * @return [KClass]<[ValueAnimator]>
 */
val ValueAnimatorKClass get() = kclassOf<ValueAnimator>()

/**
 * 获得 [ValueAnimator.AnimatorUpdateListener] 类型
 * @return [KClass]<[ValueAnimator.AnimatorUpdateListener]>
 */
val ValueAnimator_AnimatorUpdateListenerKClass get() = kclassOf<ValueAnimator.AnimatorUpdateListener>()

/**
 * 获得 [ViewAnimator] 类型
 * @return [KClass]<[ViewAnimator]>
 */
val ViewAnimatorKClass get() = kclassOf<ViewAnimator>()

/**
 * 获得 [AnimatorSet] 类型
 * @return [KClass]<[AnimatorSet]>
 */
val AnimatorSetKClass get() = kclassOf<AnimatorSet>()

/**
 * 获得 [AnimatorSet.Builder] 类型
 * @return [KClass]<[AnimatorSet.Builder]>
 */
val AnimatorSet_BuilderKClass get() = kclassOf<AnimatorSet.Builder>()

/**
 * 获得 [PropertyValuesHolder] 类型
 * @return [KClass]<[PropertyValuesHolder]>
 */
val PropertyValuesHolderKClass get() = kclassOf<PropertyValuesHolder>()

/**
 * 获得 [ViewPropertyAnimator] 类型
 * @return [KClass]<[ViewPropertyAnimator]>
 */
val ViewPropertyAnimatorKClass get() = kclassOf<ViewPropertyAnimator>()

/**
 * 获得 [View.MeasureSpec] 类型
 * @return [KClass]<[View.MeasureSpec]>
 */
val View_MeasureSpecKClass get() = kclassOf<View.MeasureSpec>()