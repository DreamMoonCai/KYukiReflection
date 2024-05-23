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
@file:Suppress("unused", "KDocUnresolvedReference", "DEPRECATION")

package com.dream.yukireflection.type.android

import android.app.* // ktlint-disable no-wildcard-imports
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetProviderInfo
import android.content.*
import android.content.Intent.ShortcutIconResource
import android.content.pm.*
import android.content.pm.LauncherApps.ShortcutQuery
import android.content.res.*
import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.*
import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.*
import android.provider.Settings
import android.service.notification.StatusBarNotification
import android.util.*
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.dream.yukireflection.factory.kclassOf
import com.dream.yukireflection.factory.toKClass
import com.dream.yukireflection.factory.toKClassOrNull
import kotlin.reflect.KClass

/**
 * 获得 [android.R] 类型
 * @return [KClass]<[android.R]>
 */
val AndroidRKClass get() = kclassOf<android.R>()

/**
 * 获得 [Context] 类型
 * @return [KClass]<[Context]>
 */
val ContextKClass get() = kclassOf<Context>()

/**
 * 获得 [ContextImpl] 类型
 * @return [KClass]
 */
val ContextImplKClass get() = "android.app.ContextImpl".toKClass()

/**
 * 获得 [ContextWrapper] 类型
 * @return [KClass]<[ContextWrapper]>
 */
val ContextWrapperKClass get() = kclassOf<ContextWrapper>()

/**
 * 获得 [Application] 类型
 * @return [KClass]<[Application]>
 */
val ApplicationKClass get() = kclassOf<Application>()

/**
 * 获得 [ApplicationInfo] 类型
 * @return [KClass]<[ApplicationInfo]>
 */
val ApplicationInfoKClass get() = kclassOf<ApplicationInfo>()

/**
 * 获得 [Instrumentation] 类型
 * @return [KClass]<[Instrumentation]>
 */
val InstrumentationKClass get() = kclassOf<Instrumentation>()

/**
 * 获得 [PackageInfo] 类型
 * @return [KClass]<[PackageInfo]>
 */
val PackageInfoKClass get() = kclassOf<PackageInfo>()

/**
 * 获得 [ApplicationPackageManager] 类型
 * @return [KClass]
 */
val ApplicationPackageManagerKClass get() = "android.app.ApplicationPackageManager".toKClass()

/**
 * 获得 [ActivityThread] 类型
 * @return [KClass]
 */
val ActivityThreadKClass get() = "android.app.ActivityThread".toKClass()

/**
 * 获得 [ActivityManager] 类型
 * @return [KClass]<[ActivityManager]>
 */
val ActivityManagerKClass get() = kclassOf<ActivityManager>()

/**
 * 获得 [IActivityManager] 类型
 * @return [KClass]
 */
val IActivityManagerKClass get() = "android.app.IActivityManager".toKClass()

/**
 * 获得 [ActivityManagerNative] 类型
 * @return [KClass]
 */
val ActivityManagerNativeKClass get() = "android.app.ActivityManagerNative".toKClass()

/**
 * 获得 [IActivityTaskManager] 类型
 *
 * - 在 Android O (26) 及以上系统加入
 * @return [KClass] or null
 */
val IActivityTaskManagerKClass get() = "android.app.IActivityTaskManager".toKClassOrNull()

/**
 * 获得 [ActivityTaskManager] 类型
 *
 * - 在 Android O (26) 及以上系统加入
 * @return [KClass] or null
 */
val ActivityTaskManagerKClass get() = "android.app.ActivityTaskManager".toKClassOrNull()

/**
 * 获得 [IPackageManager] 类型
 * @return [KClass]
 */
val IPackageManagerKClass get() = "android.content.pm.IPackageManager".toKClass()

/**
 * 获得 [ClientTransaction] 类型
 * @return [KClass]
 */
val ClientTransactionKClass get() = "android.app.servertransaction.ClientTransaction".toKClass()

/**
 * 获得 [LoadedApk] 类型
 * @return [KClass]
 */
val LoadedApkKClass get() = "android.app.LoadedApk".toKClass()

/**
 * 获得 [Singleton] 类型
 * @return [KClass]
 */
val SingletonKClass get() = "android.util.Singleton".toKClass()

/**
 * 获得 [Activity] 类型
 * @return [KClass]<[Activity]>
 */
val ActivityKClass get() = kclassOf<Activity>()

/**
 * 获得 [Looper] 类型
 * @return [KClass]<[Looper]>
 */
val LooperKClass get() = kclassOf<Looper>()

/**
 * 获得 [Fragment] 类型 - Support
 * @return [KClass]
 */
val FragmentClass_AndroidSupport get() = "android.support.v4.app.Fragment".toKClass()

/**
 * 获得 [Fragment] 类型 - AndroidX
 * @return [KClass]
 */
val FragmentClass_AndroidX get() = "androidx.fragment.app.Fragment".toKClass()

/**
 * 获得 [FragmentActivity] 类型 - Support
 * @return [KClass]
 */
val FragmentActivityClass_AndroidSupport get() = "android.support.v4.app.FragmentActivity".toKClass()

/**
 * 获得 [FragmentActivity] 类型 - AndroidX
 * @return [KClass]
 */
val FragmentActivityClass_AndroidX get() = "androidx.fragment.app.FragmentActivity".toKClass()

/**
 * 获得 [DocumentFile] 类型 - AndroidX
 * @return [KClass]
 */
val DocumentFileKClass get() = "androidx.documentfile.provider.DocumentFile".toKClass()

/**
 * 获得 [Service] 类型
 * @return [KClass]<[Service]>
 */
val ServiceKClass get() = kclassOf<Service>()

/**
 * 获得 [Binder] 类型
 * @return [KClass]<[Binder]>
 */
val BinderKClass get() = kclassOf<Binder>()

/**
 * 获得 [IBinder] 类型
 * @return [KClass]<[IBinder]>
 */
val IBinderKClass get() = kclassOf<IBinder>()

/**
 * 获得 [BroadcastReceiver] 类型
 * @return [KClass]<[BroadcastReceiver]>
 */
val BroadcastReceiverKClass get() = kclassOf<BroadcastReceiver>()

/**
 * 获得 [Bundle] 类型
 * @return [KClass]<[Bundle]>
 */
val BundleKClass get() = kclassOf<Bundle>()

/**
 * 获得 [BaseBundle] 类型
 * @return [KClass]<[BaseBundle]>
 */
val BaseBundleKClass get() = kclassOf<BaseBundle>()

/**
 * 获得 [Resources] 类型
 * @return [KClass]<[Resources]>
 */
val ResourcesKClass get() = kclassOf<Resources>()

/**
 * 获得 [Configuration] 类型
 * @return [KClass]<[Configuration]>
 */
val ConfigurationKClass get() = kclassOf<Configuration>()

/**
 * 获得 [ConfigurationInfo] 类型
 * @return [KClass]<[ConfigurationInfo]>
 */
val ConfigurationInfoKClass get() = kclassOf<ConfigurationInfo>()

/**
 * 获得 [ContentResolver] 类型
 * @return [KClass]<[ContentResolver]>
 */
val ContentResolverKClass get() = kclassOf<ContentResolver>()

/**
 * 获得 [ContentProvider] 类型
 * @return [KClass]<[ContentProvider]>
 */
val ContentProviderKClass get() = kclassOf<ContentProvider>()

/**
 * 获得 [Settings] 类型
 * @return [KClass]<[Settings]>
 */
val SettingsKClass get() = kclassOf<Settings>()

/**
 * 获得 [Settings.System] 类型
 * @return [KClass]<[Settings.System]>
 */
val Settings_SystemKClass get() = kclassOf<Settings.System>()

/**
 * 获得 [Settings.Secure] 类型
 * @return [KClass]<[Settings.Secure]>
 */
val Settings_SecureKClass get() = kclassOf<Settings.Secure>()

/**
 * 获得 [TypedArray] 类型
 * @return [KClass]<[TypedArray]>
 */
val TypedArrayKClass get() = kclassOf<TypedArray>()

/**
 * 获得 [TypedValue] 类型
 * @return [KClass]<[TypedValue]>
 */
val TypedValueKClass get() = kclassOf<TypedValue>()

/**
 * 获得 [SparseArray] 类型
 * @return [KClass]<[SparseArray]>
 */
val SparseArrayKClass get() = kclassOf<SparseArray<*>>()

/**
 * 获得 [SparseIntArray] 类型
 * @return [KClass]<[SparseIntArray]>
 */
val SparseIntArrayKClass get() = kclassOf<SparseIntArray>()

/**
 * 获得 [SparseBooleanArray] 类型
 * @return [KClass]<[SparseBooleanArray]>
 */
val SparseBooleanArrayKClass get() = kclassOf<SparseBooleanArray>()

/**
 * 获得 [SparseLongArray] 类型
 * @return [KClass]<[SparseLongArray]>
 */
val SparseLongArrayKClass get() = kclassOf<SparseLongArray>()

/**
 * 获得 [LongSparseArray] 类型
 * @return [KClass]<[LongSparseArray]>
 */
val LongSparseArrayKClass get() = kclassOf<LongSparseArray<*>>()

/**
 * 获得 [ArrayMap] 类型
 * @return [KClass]<[ArrayMap]>
 */
val ArrayMapKClass get() = kclassOf<ArrayMap<*, *>>()

/**
 * 获得 [ArraySet] 类型
 *
 * - 在 Android M (23) 及以上系统加入
 * @return [KClass]<[ArraySet]> or null
 */
val ArraySetKClass get() = if (Build.VERSION.SDK_INT >= 23) kclassOf<ArraySet<*>>() else null

/**
 * 获得 [Handler] 类型
 * @return [KClass]<[Handler]>
 */
val HandlerKClass get() = kclassOf<Handler>()

/**
 * 获得 [Handler.Callback] 类型
 * @return [KClass]<[Handler.Callback]>
 */
val Handler_CallbackKClass get() = kclassOf<Handler.Callback>()

/**
 * 获得 [Message] 类型
 * @return [KClass]<[Message]>
 */
val MessageKClass get() = kclassOf<Message>()

/**
 * 获得 [MessageQueue] 类型
 * @return [KClass]<[MessageQueue]>
 */
val MessageQueueKClass get() = kclassOf<MessageQueue>()

/**
 * 获得 [Messenger] 类型
 * @return [KClass]<[Messenger]>
 */
val MessengerKClass get() = kclassOf<Messenger>()

/**
 * 获得 [AsyncTask] 类型
 * @return [KClass]<[AsyncTask]>
 */
val AsyncTaskKClass get() = kclassOf<AsyncTask<*, *, *>>()

/**
 * 获得 [SimpleDateFormat] 类型
 *
 * - 在 Android N (24) 及以上系统加入
 * @return [KClass]<[SimpleDateFormat]> or null
 */
val SimpleDateFormatClass_Android get() = if (Build.VERSION.SDK_INT >= 24) kclassOf<SimpleDateFormat>() else null

/**
 * 获得 [Base64] 类型
 * @return [KClass]<[Base64]>
 */
val Base64Class_Android get() = kclassOf<Base64>()

/**
 * 获得 [Window] 类型
 * @return [KClass]<[Window]>
 */
val WindowKClass get() = kclassOf<Window>()

/**
 * 获得 [WindowMetrics] 类型
 *
 * - 在 Android R (30) 及以上系统加入
 * @return [KClass]<[WindowMetrics]> or null
 */
val WindowMetricsKClass get() = if (Build.VERSION.SDK_INT >= 30) kclassOf<WindowMetrics>() else null

/**
 * 获得 [WindowInsets] 类型
 * @return [KClass]<[WindowInsets]>
 */
val WindowInsetsKClass get() = kclassOf<WindowInsets>()

/**
 * 获得 [WindowInsets.Type] 类型
 *
 * - 在 Android R (30) 及以上系统加入
 * @return [KClass]<[WindowInsets.Type]> or null
 */
val WindowInsets_TypeKClass get() = if (Build.VERSION.SDK_INT >= 30) kclassOf<WindowInsets.Type>() else null

/**
 * 获得 [WindowManager] 类型
 * @return [KClass]<[WindowManager]>
 */
val WindowManagerKClass get() = kclassOf<WindowManager>()

/**
 * 获得 [WindowManager.LayoutParams] 类型
 * @return [KClass]<[WindowManager.LayoutParams]>
 */
val WindowManager_LayoutParamsKClass get() = kclassOf<WindowManager.LayoutParams>()

/**
 * 获得 [ViewManager] 类型
 * @return [KClass]<[ViewManager]>
 */
val ViewManagerKClass get() = kclassOf<ViewManager>()

/**
 * 获得 [Parcel] 类型
 * @return [KClass]<[Parcel]>
 */
val ParcelKClass get() = kclassOf<Parcel>()

/**
 * 获得 [Parcelable] 类型
 * @return [KClass]<[Parcelable]>
 */
val ParcelableKClass get() = kclassOf<Parcelable>()

/**
 * 获得 [Parcelable.Creator] 类型
 * @return [KClass]<[Parcelable.Creator]>
 */
val Parcelable_CreatorKClass get() = kclassOf<Parcelable.Creator<*>>()

/**
 * 获得 [Dialog] 类型
 * @return [KClass]<[Dialog]>
 */
val DialogKClass get() = kclassOf<Dialog>()

/**
 * 获得 [AlertDialog] 类型
 * @return [KClass]<[AlertDialog]>
 */
val AlertDialogKClass get() = kclassOf<AlertDialog>()

/**
 * 获得 [DisplayMetrics] 类型
 * @return [KClass]<[DisplayMetrics]>
 */
val DisplayMetricsKClass get() = kclassOf<DisplayMetrics>()

/**
 * 获得 [Display] 类型
 * @return [KClass]<[Display]>
 */
val DisplayKClass get() = kclassOf<Display>()

/**
 * 获得 [Toast] 类型
 * @return [KClass]<[Toast]>
 */
val ToastKClass get() = kclassOf<Toast>()

/**
 * 获得 [Intent] 类型
 * @return [KClass]<[Intent]>
 */
val IntentKClass get() = kclassOf<Intent>()

/**
 * 获得 [ComponentInfo] 类型
 * @return [KClass]<[ComponentInfo]>
 */
val ComponentInfoKClass get() = kclassOf<ComponentInfo>()

/**
 * 获得 [ComponentName] 类型
 * @return [KClass]<[ComponentName]>
 */
val ComponentNameKClass get() = kclassOf<ComponentName>()

/**
 * 获得 [PendingIntent] 类型
 * @return [KClass]<[PendingIntent]>
 */
val PendingIntentKClass get() = kclassOf<PendingIntent>()

/**
 * 获得 [ColorStateList] 类型
 * @return [KClass]<[ColorStateList]>
 */
val ColorStateListKClass get() = kclassOf<ColorStateList>()

/**
 * 获得 [ContentValues] 类型
 * @return [KClass]<[ContentValues]>
 */
val ContentValuesKClass get() = kclassOf<ContentValues>()

/**
 * 获得 [SharedPreferences] 类型
 * @return [KClass]<[SharedPreferences]>
 */
val SharedPreferencesKClass get() = kclassOf<SharedPreferences>()

/**
 * 获得 [MediaPlayer] 类型
 * @return [KClass]<[MediaPlayer]>
 */
val MediaPlayerKClass get() = kclassOf<MediaPlayer>()

/**
 * 获得 [ProgressDialog] 类型
 * @return [KClass]<[ProgressDialog]>
 */
val ProgressDialogKClass get() = kclassOf<ProgressDialog>()

/**
 * 获得 [Log] 类型
 * @return [KClass]<[Log]>
 */
val LogKClass get() = kclassOf<Log>()

/**
 * 获得 [Build] 类型
 * @return [KClass]<[Build]>
 */
val BuildKClass get() = kclassOf<Build>()

/**
 * 获得 [Xml] 类型
 * @return [KClass]<[Xml]>
 */
val XmlKClass get() = kclassOf<Xml>()

/**
 * 获得 [ContrastColorUtil] 类型
 * @return [KClass]
 */
val ContrastColorUtilKClass get() = "com.android.internal.util.ContrastColorUtil".toKClass()

/**
 * 获得 [StatusBarNotification] 类型
 * @return [KClass]<[StatusBarNotification]>
 */
val StatusBarNotificationKClass get() = kclassOf<StatusBarNotification>()

/**
 * 获得 [Notification] 类型
 * @return [KClass]<[Notification]>
 */
val NotificationKClass get() = kclassOf<Notification>()

/**
 * 获得 [Notification.Builder] 类型
 * @return [KClass]<[Notification.Builder]>
 */
val Notification_BuilderKClass get() = kclassOf<Notification.Builder>()

/**
 * 获得 [Notification.Action] 类型
 * @return [KClass]<[Notification.Action]>
 */
val Notification_ActionKClass get() = kclassOf<Notification.Action>()

/**
 * 获得 [DialogInterface] 类型
 * @return [KClass]<[DialogInterface]>
 */
val DialogInterfaceKClass get() = kclassOf<DialogInterface>()

/**
 * 获得 [DialogInterface.OnClickListener] 类型
 * @return [KClass]<[DialogInterface.OnClickListener]>
 */
val DialogInterface_OnClickListenerKClass get() = kclassOf<DialogInterface.OnClickListener>()

/**
 * 获得 [DialogInterface.OnCancelListener] 类型
 * @return [KClass]<[DialogInterface.OnCancelListener]>
 */
val DialogInterface_OnCancelListenerKClass get() = kclassOf<DialogInterface.OnCancelListener>()

/**
 * 获得 [DialogInterface.OnDismissListener] 类型
 * @return [KClass]<[DialogInterface.OnDismissListener]>
 */
val DialogInterface_OnDismissListenerKClass get() = kclassOf<DialogInterface.OnDismissListener>()

/**
 * 获得 [Environment] 类型
 * @return [KClass]<[Environment]>
 */
val EnvironmentKClass get() = kclassOf<Environment>()

/**
 * 获得 [Process] 类型
 * @return [KClass]<[Process]>
 */
val ProcessKClass get() = kclassOf<Process>()

/**
 * 获得 [Vibrator] 类型
 * @return [KClass]<[Vibrator]>
 */
val VibratorKClass get() = kclassOf<Vibrator>()

/**
 * 获得 [VibrationEffect] 类型
 *
 * - 在 Android O (26) 及以上系统加入
 * @return [KClass]<[VibrationEffect]> or null
 */
val VibrationEffectKClass get() = if (Build.VERSION.SDK_INT >= 26) kclassOf<VibrationEffect>() else null

/**
 * 获得 [VibrationAttributes] 类型
 *
 * - 在 Android R (30) 及以上系统加入
 * @return [KClass]<[VibrationAttributes]> or null
 */
val VibrationAttributesKClass get() = if (Build.VERSION.SDK_INT >= 30) kclassOf<VibrationAttributes>() else null

/**
 * 获得 [SystemClock] 类型
 * @return [KClass]<[SystemClock]>
 */
val SystemClockKClass get() = kclassOf<SystemClock>()

/**
 * 获得 [PowerManager] 类型
 * @return [KClass]<[PowerManager]>
 */
val PowerManagerKClass get() = kclassOf<PowerManager>()

/**
 * 获得 [PowerManager.WakeLock] 类型
 * @return [KClass]<[PowerManager.WakeLock]>
 */
val PowerManager_WakeLockKClass get() = kclassOf<PowerManager.WakeLock>()

/**
 * 获得 [UserHandle] 类型
 * @return [KClass]<[UserHandle]>
 */
val UserHandleKClass get() = kclassOf<UserHandle>()

/**
 * 获得 [ShortcutInfo] 类型
 *
 * - 在 Android N_MR1 (25) 及以上系统加入
 * @return [KClass]<[ShortcutInfo]> or null
 */
val ShortcutInfoKClass get() = if (Build.VERSION.SDK_INT >= 25) kclassOf<ShortcutInfo>() else null

/**
 * 获得 [ShortcutManager] 类型
 *
 * - 在 Android R (30) 及以上系统加入
 * @return [KClass]<[ShortcutManager]> or null
 */
val ShortcutManagerKClass get() = if (Build.VERSION.SDK_INT >= 30) kclassOf<ShortcutManager>() else null

/**
 * 获得 [ShortcutQuery] 类型
 *
 * - 在 Android N_MR1 (25) 及以上系统加入
 * @return [KClass]<[ShortcutQuery]> or null
 */
val ShortcutQueryKClass get() = if (Build.VERSION.SDK_INT >= 25) kclassOf<ShortcutQuery>() else null

/**
 * 获得 [KeyboardShortcutInfo] 类型
 * @return [KClass]<[KeyboardShortcutInfo]>
 */
val KeyboardShortcutInfoKClass get() = kclassOf<KeyboardShortcutInfo>()

/**
 * 获得 [KeyboardShortcutGroup] 类型
 * @return [KClass]<[KeyboardShortcutGroup]>
 */
val KeyboardShortcutGroupKClass get() = kclassOf<KeyboardShortcutGroup>()

/**
 * 获得 [ShortcutIconResource] 类型
 * @return [KClass]<[ShortcutIconResource]>
 */
val ShortcutIconResourceKClass get() = kclassOf<ShortcutIconResource>()

/**
 * 获得 [AssetManager] 类型
 * @return [KClass]<[AssetManager]>
 */
val AssetManagerKClass get() = kclassOf<AssetManager>()

/**
 * 获得 [AppWidgetManager] 类型
 * @return [KClass]<[AppWidgetManager]>
 */
val AppWidgetManagerKClass get() = kclassOf<AppWidgetManager>()

/**
 * 获得 [AppWidgetProvider] 类型
 * @return [KClass]<[AppWidgetProvider]>
 */
val AppWidgetProviderKClass get() = kclassOf<AppWidgetProvider>()

/**
 * 获得 [AppWidgetProviderInfo] 类型
 * @return [KClass]<[AppWidgetProviderInfo]>
 */
val AppWidgetProviderInfoKClass get() = kclassOf<AppWidgetProviderInfo>()

/**
 * 获得 [AppWidgetHost] 类型
 * @return [KClass]<[AppWidgetHost]>
 */
val AppWidgetHostKClass get() = kclassOf<AppWidgetHost>()

/**
 * 获得 [ActivityInfo] 类型
 * @return [KClass]<[ActivityInfo]>
 */
val ActivityInfoKClass get() = kclassOf<ActivityInfo>()

/**
 * 获得 [ResolveInfo] 类型
 * @return [KClass]<[ResolveInfo]>
 */
val ResolveInfoKClass get() = kclassOf<ResolveInfo>()

/**
 * 获得 [Property] 类型
 * @return [KClass]<[Property]>
 */
val PropertyKClass get() = kclassOf<Property<*, *>>()

/**
 * 获得 [IntProperty] 类型
 * @return [KClass]<[IntProperty]>
 */
val IntPropertyKClass get() = kclassOf<IntProperty<*>>()

/**
 * 获得 [FloatProperty] 类型
 * @return [KClass]<[FloatProperty]>
 */
val FloatPropertyKClass get() = kclassOf<FloatProperty<*>>()

/**
 * 获得 [SQLiteDatabase] 类型
 * @return [KClass]<[SQLiteDatabase]>
 */
val SQLiteDatabaseKClass get() = kclassOf<SQLiteDatabase>()

/**
 * 获得 [StrictMode] 类型
 * @return [KClass]<[StrictMode]>
 */
val StrictModeKClass get() = kclassOf<StrictMode>()

/**
 * 获得 [AccessibilityManager] 类型
 * @return [KClass]<[AccessibilityManager]>
 */
val AccessibilityManagerKClass get() = kclassOf<AccessibilityManager>()

/**
 * 获得 [AccessibilityEvent] 类型
 * @return [KClass]<[AccessibilityEvent]>
 */
val AccessibilityEventKClass get() = kclassOf<AccessibilityEvent>()

/**
 * 获得 [AccessibilityNodeInfo] 类型
 * @return [KClass]<[AccessibilityNodeInfo]>
 */
val AccessibilityNodeInfoKClass get() = kclassOf<AccessibilityNodeInfo>()

/**
 * 获得 [IInterface] 类型
 * @return [KClass]<[IInterface]>
 */
val IInterfaceKClass get() = kclassOf<IInterface>()