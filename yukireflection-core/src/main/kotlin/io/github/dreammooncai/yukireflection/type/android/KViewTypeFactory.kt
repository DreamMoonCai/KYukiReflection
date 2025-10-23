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

package io.github.dreammooncai.yukireflection.type.android

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
import io.github.dreammooncai.yukireflection.factory.klassOf
import kotlin.reflect.KClass

/**
 * 获得 [View] 类型
 * @return [KClass]<[View]>
 */
val ViewKClass get() = klassOf<View>()

/**
 * 获得 [Surface] 类型
 * @return [KClass]<[Surface]>
 */
val SurfaceKClass get() = klassOf<Surface>()

/**
 * 获得 [SurfaceView] 类型
 * @return [KClass]<[SurfaceView]>
 */
val SurfaceViewKClass get() = klassOf<SurfaceView>()

/**
 * 获得 [TextureView] 类型
 * @return [KClass]<[TextureView]>
 */
val TextureViewKClass get() = klassOf<TextureView>()

/**
 * 获得 [WebView] 类型
 * @return [KClass]<[WebView]>
 */
val WebViewKClass get() = klassOf<WebView>()

/**
 * 获得 [WebViewClient] 类型
 * @return [KClass]<[WebViewClient]>
 */
val WebViewClientKClass get() = klassOf<WebViewClient>()

/**
 * 获得 [ViewStructure] 类型
 * @return [KClass]<[ViewStructure]>
 */
val ViewStructureKClass get() = klassOf<ViewStructure>()

/**
 * 获得 [ViewGroup] 类型
 * @return [KClass]<[ViewGroup]>
 */
val ViewGroupKClass get() = klassOf<ViewGroup>()

/**
 * 获得 [ViewParent] 类型
 * @return [KClass]<[ViewParent]>
 */
val ViewParentKClass get() = klassOf<ViewParent>()

/**
 * 获得 [AppWidgetHostView] 类型
 * @return [KClass]<[AppWidgetHostView]>
 */
val AppWidgetHostViewKClass get() = klassOf<AppWidgetHostView>()

/**
 * 获得 [RemoteViews] 类型
 * @return [KClass]<[RemoteViews]>
 */
val RemoteViewsKClass get() = klassOf<RemoteViews>()

/**
 * 获得 [RemoteView] 类型
 * @return [KClass]<[RemoteView]>
 */
val RemoteViewKClass get() = klassOf<RemoteView>()

/**
 * 获得 [TextView] 类型
 * @return [KClass]<[TextView]>
 */
val TextViewKClass get() = klassOf<TextView>()

/**
 * 获得 [ImageView] 类型
 * @return [KClass]<[ImageView]>
 */
val ImageViewKClass get() = klassOf<ImageView>()

/**
 * 获得 [ImageButton] 类型
 * @return [KClass]<[ImageButton]>
 */
val ImageButtonKClass get() = klassOf<ImageButton>()

/**
 * 获得 [EditText] 类型
 * @return [KClass]<[EditText]>
 */
val EditTextKClass get() = klassOf<EditText>()

/**
 * 获得 [Button] 类型
 * @return [KClass]<[Button]>
 */
val ButtonKClass get() = klassOf<Button>()

/**
 * 获得 [CheckBox] 类型
 * @return [KClass]<[CheckBox]>
 */
val CheckBoxKClass get() = klassOf<CheckBox>()

/**
 * 获得 [CompoundButton] 类型
 * @return [KClass]<[CompoundButton]>
 */
val CompoundButtonKClass get() = klassOf<CompoundButton>()

/**
 * 获得 [VideoView] 类型
 * @return [KClass]<[VideoView]>
 */
val VideoViewKClass get() = klassOf<VideoView>()

/**
 * 获得 [ListView] 类型
 * @return [KClass]<[ListView]>
 */
val ListViewKClass get() = klassOf<ListView>()

/**
 * 获得 [LayoutInflater] 类型
 * @return [KClass]<[LayoutInflater]>
 */
val LayoutInflaterKClass get() = klassOf<LayoutInflater>()

/**
 * 获得 [LayoutInflater.Filter] 类型
 * @return [KClass]<[LayoutInflater.Filter]>
 */
val LayoutInflater_FilterKClass get() = klassOf<LayoutInflater.Filter>()

/**
 * 获得 [LayoutInflater.Factory] 类型
 * @return [KClass]<[LayoutInflater.Factory]>
 */
val LayoutInflater_FactoryKClass get() = klassOf<LayoutInflater.Factory>()

/**
 * 获得 [LayoutInflater.Factory2] 类型
 * @return [KClass]<[LayoutInflater.Factory2]>
 */
val LayoutInflater_Factory2KClass get() = klassOf<LayoutInflater.Factory2>()

/**
 * 获得 [ListAdapter] 类型
 * @return [KClass]<[ListAdapter]>
 */
val ListAdapterKClass get() = klassOf<ListAdapter>()

/**
 * 获得 [ArrayAdapter] 类型
 * @return [KClass]<[ArrayAdapter]>
 */
val ArrayAdapterKClass get() = klassOf<ArrayAdapter<*>>()

/**
 * 获得 [BaseAdapter] 类型
 * @return [KClass]<[BaseAdapter]>
 */
val BaseAdapterKClass get() = klassOf<BaseAdapter>()

/**
 * 获得 [RelativeLayout] 类型
 * @return [KClass]<[RelativeLayout]>
 */
val RelativeLayoutKClass get() = klassOf<RelativeLayout>()

/**
 * 获得 [FrameLayout] 类型
 * @return [KClass]<[FrameLayout]>
 */
val FrameLayoutKClass get() = klassOf<FrameLayout>()

/**
 * 获得 [LinearLayout] 类型
 * @return [KClass]<[LinearLayout]>
 */
val LinearLayoutKClass get() = klassOf<LinearLayout>()

/**
 * 获得 [ViewGroup.LayoutParams] 类型
 * @return [KClass]<[ViewGroup.LayoutParams]>
 */
val ViewGroup_LayoutParamsKClass get() = klassOf<ViewGroup.LayoutParams>()

/**
 * 获得 [RelativeLayout.LayoutParams] 类型
 * @return [KClass]<[RelativeLayout.LayoutParams]>
 */
val RelativeLayout_LayoutParamsKClass get() = klassOf<RelativeLayout.LayoutParams>()

/**
 * 获得 [LinearLayout.LayoutParams] 类型
 * @return [KClass]<[LinearLayout.LayoutParams]>
 */
val LinearLayout_LayoutParamsKClass get() = klassOf<LinearLayout.LayoutParams>()

/**
 * 获得 [FrameLayout.LayoutParams] 类型
 * @return [KClass]<[FrameLayout.LayoutParams]>
 */
val FrameLayout_LayoutParamsKClass get() = klassOf<FrameLayout.LayoutParams>()

/**
 * 获得 [TextClock] 类型
 * @return [KClass]<[TextClock]>
 */
val TextClockKClass get() = klassOf<TextClock>()

/**
 * 获得 [MotionEvent] 类型
 * @return [KClass]<[MotionEvent]>
 */
val MotionEventKClass get() = klassOf<MotionEvent>()

/**
 * 获得 [View.OnClickListener] 类型
 * @return [KClass]<[View.OnClickListener]>
 */
val View_OnClickListenerKClass get() = klassOf<View.OnClickListener>()

/**
 * 获得 [View.OnLongClickListener] 类型
 * @return [KClass]<[View.OnLongClickListener]>
 */
val View_OnLongClickListenerKClass get() = klassOf<View.OnLongClickListener>()

/**
 * 获得 [View.OnTouchListener] 类型
 * @return [KClass]<[View.OnTouchListener]>
 */
val View_OnTouchListenerKClass get() = klassOf<View.OnTouchListener>()

/**
 * 获得 [AutoCompleteTextView] 类型
 * @return [KClass]<[AutoCompleteTextView]>
 */
val AutoCompleteTextViewKClass get() = klassOf<AutoCompleteTextView>()

/**
 * 获得 [ViewStub] 类型
 * @return [KClass]<[ViewStub]>
 */
val ViewStubKClass get() = klassOf<ViewStub>()

/**
 * 获得 [ViewStub.OnInflateListener] 类型
 * @return [KClass]<[ViewStub.OnInflateListener]>
 */
val ViewStub_OnInflateListenerKClass get() = klassOf<ViewStub.OnInflateListener>()

/**
 * 获得 [GestureDetector] 类型
 * @return [KClass]<[GestureDetector]>
 */
val GestureDetectorKClass get() = klassOf<GestureDetector>()

/**
 * 获得 [GestureDetector.SimpleOnGestureListener] 类型
 * @return [KClass]<[GestureDetector.SimpleOnGestureListener]>
 */
val GestureDetector_SimpleOnGestureListenerKClass get() = klassOf<GestureDetector.SimpleOnGestureListener>()

/**
 * 获得 [ProgressBar] 类型
 * @return [KClass]<[ProgressBar]>
 */
val ProgressBarKClass get() = klassOf<ProgressBar>()

/**
 * 获得 [AttributeSet] 类型
 * @return [KClass]<[AttributeSet]>
 */
val AttributeSetKClass get() = klassOf<AttributeSet>()

/**
 * 获得 [Animation] 类型
 * @return [KClass]<[Animation]>
 */
val AnimationKClass get() = klassOf<Animation>()

/**
 * 获得 [Animation.AnimationListener] 类型
 * @return [KClass]<[Animation.AnimationListener]>
 */
val Animation_AnimationListenerKClass get() = klassOf<Animation.AnimationListener>()

/**
 * 获得 [TranslateAnimation] 类型
 * @return [KClass]<[TranslateAnimation]>
 */
val TranslateAnimationKClass get() = klassOf<TranslateAnimation>()

/**
 * 获得 [AlphaAnimation] 类型
 * @return [KClass]<[AlphaAnimation]>
 */
val AlphaAnimationKClass get() = klassOf<AlphaAnimation>()

/**
 * 获得 [Animator] 类型
 * @return [KClass]<[Animator]>
 */
val AnimatorKClass get() = klassOf<Animator>()

/**
 * 获得 [Animator.AnimatorListener] 类型
 * @return [KClass]<[Animator.AnimatorListener]>
 */
val Animator_AnimatorListenerKClass get() = klassOf<Animator.AnimatorListener>()

/**
 * 获得 [ObjectAnimator] 类型
 * @return [KClass]<[ObjectAnimator]>
 */
val ObjectAnimatorKClass get() = klassOf<ObjectAnimator>()

/**
 * 获得 [ValueAnimator] 类型
 * @return [KClass]<[ValueAnimator]>
 */
val ValueAnimatorKClass get() = klassOf<ValueAnimator>()

/**
 * 获得 [ValueAnimator.AnimatorUpdateListener] 类型
 * @return [KClass]<[ValueAnimator.AnimatorUpdateListener]>
 */
val ValueAnimator_AnimatorUpdateListenerKClass get() = klassOf<ValueAnimator.AnimatorUpdateListener>()

/**
 * 获得 [ViewAnimator] 类型
 * @return [KClass]<[ViewAnimator]>
 */
val ViewAnimatorKClass get() = klassOf<ViewAnimator>()

/**
 * 获得 [AnimatorSet] 类型
 * @return [KClass]<[AnimatorSet]>
 */
val AnimatorSetKClass get() = klassOf<AnimatorSet>()

/**
 * 获得 [AnimatorSet.Builder] 类型
 * @return [KClass]<[AnimatorSet.Builder]>
 */
val AnimatorSet_BuilderKClass get() = klassOf<AnimatorSet.Builder>()

/**
 * 获得 [PropertyValuesHolder] 类型
 * @return [KClass]<[PropertyValuesHolder]>
 */
val PropertyValuesHolderKClass get() = klassOf<PropertyValuesHolder>()

/**
 * 获得 [ViewPropertyAnimator] 类型
 * @return [KClass]<[ViewPropertyAnimator]>
 */
val ViewPropertyAnimatorKClass get() = klassOf<ViewPropertyAnimator>()

/**
 * 获得 [View.MeasureSpec] 类型
 * @return [KClass]<[View.MeasureSpec]>
 */
val View_MeasureSpecKClass get() = klassOf<View.MeasureSpec>()