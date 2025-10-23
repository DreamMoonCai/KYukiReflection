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

package io.github.dreammooncai.yukireflection.type.android

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
import io.github.dreammooncai.yukireflection.factory.klassOf
import io.github.dreammooncai.yukireflection.factory.toKClass
import io.github.dreammooncai.yukireflection.factory.toKClassOrNull
import kotlin.reflect.KClass

/**
 * 获得 [android.R] 类型
 * @return [KClass]<[android.R]>
 */
val AndroidRKClass get() = klassOf<android.R>()

/**
 * 获得 [Context] 类型
 * @return [KClass]<[Context]>
 */
val ContextKClass get() = klassOf<Context>()

/**
 * 获得 [ContextImpl] 类型
 * @return [KClass]
 */
val ContextImplKClass get() = "android.app.ContextImpl".toKClass()

/**
 * 获得 [ContextWrapper] 类型
 * @return [KClass]<[ContextWrapper]>
 */
val ContextWrapperKClass get() = klassOf<ContextWrapper>()

/**
 * 获得 [Application] 类型
 * @return [KClass]<[Application]>
 */
val ApplicationKClass get() = klassOf<Application>()

/**
 * 获得 [ApplicationInfo] 类型
 * @return [KClass]<[ApplicationInfo]>
 */
val ApplicationInfoKClass get() = klassOf<ApplicationInfo>()

/**
 * 获得 [Instrumentation] 类型
 * @return [KClass]<[Instrumentation]>
 */
val InstrumentationKClass get() = klassOf<Instrumentation>()

/**
 * 获得 [PackageInfo] 类型
 * @return [KClass]<[PackageInfo]>
 */
val PackageInfoKClass get() = klassOf<PackageInfo>()

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
val ActivityManagerKClass get() = klassOf<ActivityManager>()

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
val ActivityKClass get() = klassOf<Activity>()

/**
 * 获得 [Looper] 类型
 * @return [KClass]<[Looper]>
 */
val LooperKClass get() = klassOf<Looper>()

/**
 * 获得 [Fragment] 类型 - Support
 * @return [KClass]
 */
val FragmentKClass_AndroidSupport get() = "android.support.v4.app.Fragment".toKClass()

/**
 * 获得 [Fragment] 类型 - AndroidX
 * @return [KClass]
 */
val FragmentKClass_AndroidX get() = "androidx.fragment.app.Fragment".toKClass()

/**
 * 获得 [FragmentActivity] 类型 - Support
 * @return [KClass]
 */
val FragmentActivityKClass_AndroidSupport get() = "android.support.v4.app.FragmentActivity".toKClass()

/**
 * 获得 [FragmentActivity] 类型 - AndroidX
 * @return [KClass]
 */
val FragmentActivityKClass_AndroidX get() = "androidx.fragment.app.FragmentActivity".toKClass()

/**
 * 获得 [DocumentFile] 类型 - AndroidX
 * @return [KClass]
 */
val DocumentFileKClass get() = "androidx.documentfile.provider.DocumentFile".toKClass()

/**
 * 获得 [Service] 类型
 * @return [KClass]<[Service]>
 */
val ServiceKClass get() = klassOf<Service>()

/**
 * 获得 [Binder] 类型
 * @return [KClass]<[Binder]>
 */
val BinderKClass get() = klassOf<Binder>()

/**
 * 获得 [IBinder] 类型
 * @return [KClass]<[IBinder]>
 */
val IBinderKClass get() = klassOf<IBinder>()

/**
 * 获得 [BroadcastReceiver] 类型
 * @return [KClass]<[BroadcastReceiver]>
 */
val BroadcastReceiverKClass get() = klassOf<BroadcastReceiver>()

/**
 * 获得 [Bundle] 类型
 * @return [KClass]<[Bundle]>
 */
val BundleKClass get() = klassOf<Bundle>()

/**
 * 获得 [BaseBundle] 类型
 * @return [KClass]<[BaseBundle]>
 */
val BaseBundleKClass get() = klassOf<BaseBundle>()

/**
 * 获得 [Resources] 类型
 * @return [KClass]<[Resources]>
 */
val ResourcesKClass get() = klassOf<Resources>()

/**
 * 获得 [Configuration] 类型
 * @return [KClass]<[Configuration]>
 */
val ConfigurationKClass get() = klassOf<Configuration>()

/**
 * 获得 [ConfigurationInfo] 类型
 * @return [KClass]<[ConfigurationInfo]>
 */
val ConfigurationInfoKClass get() = klassOf<ConfigurationInfo>()

/**
 * 获得 [ContentResolver] 类型
 * @return [KClass]<[ContentResolver]>
 */
val ContentResolverKClass get() = klassOf<ContentResolver>()

/**
 * 获得 [ContentProvider] 类型
 * @return [KClass]<[ContentProvider]>
 */
val ContentProviderKClass get() = klassOf<ContentProvider>()

/**
 * 获得 [Settings] 类型
 * @return [KClass]<[Settings]>
 */
val SettingsKClass get() = klassOf<Settings>()

/**
 * 获得 [Settings.System] 类型
 * @return [KClass]<[Settings.System]>
 */
val Settings_SystemKClass get() = klassOf<Settings.System>()

/**
 * 获得 [Settings.Secure] 类型
 * @return [KClass]<[Settings.Secure]>
 */
val Settings_SecureKClass get() = klassOf<Settings.Secure>()

/**
 * 获得 [TypedArray] 类型
 * @return [KClass]<[TypedArray]>
 */
val TypedArrayKClass get() = klassOf<TypedArray>()

/**
 * 获得 [TypedValue] 类型
 * @return [KClass]<[TypedValue]>
 */
val TypedValueKClass get() = klassOf<TypedValue>()

/**
 * 获得 [SparseArray] 类型
 * @return [KClass]<[SparseArray]>
 */
val SparseArrayKClass get() = klassOf<SparseArray<*>>()

/**
 * 获得 [SparseIntArray] 类型
 * @return [KClass]<[SparseIntArray]>
 */
val SparseIntArrayKClass get() = klassOf<SparseIntArray>()

/**
 * 获得 [SparseBooleanArray] 类型
 * @return [KClass]<[SparseBooleanArray]>
 */
val SparseBooleanArrayKClass get() = klassOf<SparseBooleanArray>()

/**
 * 获得 [SparseLongArray] 类型
 * @return [KClass]<[SparseLongArray]>
 */
val SparseLongArrayKClass get() = klassOf<SparseLongArray>()

/**
 * 获得 [LongSparseArray] 类型
 * @return [KClass]<[LongSparseArray]>
 */
val LongSparseArrayKClass get() = klassOf<LongSparseArray<*>>()

/**
 * 获得 [ArrayMap] 类型
 * @return [KClass]<[ArrayMap]>
 */
val ArrayMapKClass get() = klassOf<ArrayMap<*, *>>()

/**
 * 获得 [ArraySet] 类型
 *
 * - 在 Android M (23) 及以上系统加入
 * @return [KClass]<[ArraySet]> or null
 */
val ArraySetKClass get() = if (Build.VERSION.SDK_INT >= 23) klassOf<ArraySet<*>>() else null

/**
 * 获得 [Handler] 类型
 * @return [KClass]<[Handler]>
 */
val HandlerKClass get() = klassOf<Handler>()

/**
 * 获得 [Handler.Callback] 类型
 * @return [KClass]<[Handler.Callback]>
 */
val Handler_CallbackKClass get() = klassOf<Handler.Callback>()

/**
 * 获得 [Message] 类型
 * @return [KClass]<[Message]>
 */
val MessageKClass get() = klassOf<Message>()

/**
 * 获得 [MessageQueue] 类型
 * @return [KClass]<[MessageQueue]>
 */
val MessageQueueKClass get() = klassOf<MessageQueue>()

/**
 * 获得 [Messenger] 类型
 * @return [KClass]<[Messenger]>
 */
val MessengerKClass get() = klassOf<Messenger>()

/**
 * 获得 [AsyncTask] 类型
 * @return [KClass]<[AsyncTask]>
 */
val AsyncTaskKClass get() = klassOf<AsyncTask<*, *, *>>()

/**
 * 获得 [SimpleDateFormat] 类型
 *
 * - 在 Android N (24) 及以上系统加入
 * @return [KClass]<[SimpleDateFormat]> or null
 */
val SimpleDateFormatClass_Android get() = if (Build.VERSION.SDK_INT >= 24) klassOf<SimpleDateFormat>() else null

/**
 * 获得 [Base64] 类型
 * @return [KClass]<[Base64]>
 */
val Base64Class_Android get() = klassOf<Base64>()

/**
 * 获得 [Window] 类型
 * @return [KClass]<[Window]>
 */
val WindowKClass get() = klassOf<Window>()

/**
 * 获得 [WindowMetrics] 类型
 *
 * - 在 Android R (30) 及以上系统加入
 * @return [KClass]<[WindowMetrics]> or null
 */
val WindowMetricsKClass get() = if (Build.VERSION.SDK_INT >= 30) klassOf<WindowMetrics>() else null

/**
 * 获得 [WindowInsets] 类型
 * @return [KClass]<[WindowInsets]>
 */
val WindowInsetsKClass get() = klassOf<WindowInsets>()

/**
 * 获得 [WindowInsets.Type] 类型
 *
 * - 在 Android R (30) 及以上系统加入
 * @return [KClass]<[WindowInsets.Type]> or null
 */
val WindowInsets_TypeKClass get() = if (Build.VERSION.SDK_INT >= 30) klassOf<WindowInsets.Type>() else null

/**
 * 获得 [WindowManager] 类型
 * @return [KClass]<[WindowManager]>
 */
val WindowManagerKClass get() = klassOf<WindowManager>()

/**
 * 获得 [WindowManager.LayoutParams] 类型
 * @return [KClass]<[WindowManager.LayoutParams]>
 */
val WindowManager_LayoutParamsKClass get() = klassOf<WindowManager.LayoutParams>()

/**
 * 获得 [ViewManager] 类型
 * @return [KClass]<[ViewManager]>
 */
val ViewManagerKClass get() = klassOf<ViewManager>()

/**
 * 获得 [Parcel] 类型
 * @return [KClass]<[Parcel]>
 */
val ParcelKClass get() = klassOf<Parcel>()

/**
 * 获得 [Parcelable] 类型
 * @return [KClass]<[Parcelable]>
 */
val ParcelableKClass get() = klassOf<Parcelable>()

/**
 * 获得 [Parcelable.Creator] 类型
 * @return [KClass]<[Parcelable.Creator]>
 */
val Parcelable_CreatorKClass get() = klassOf<Parcelable.Creator<*>>()

/**
 * 获得 [Dialog] 类型
 * @return [KClass]<[Dialog]>
 */
val DialogKClass get() = klassOf<Dialog>()

/**
 * 获得 [AlertDialog] 类型
 * @return [KClass]<[AlertDialog]>
 */
val AlertDialogKClass get() = klassOf<AlertDialog>()

/**
 * 获得 [DisplayMetrics] 类型
 * @return [KClass]<[DisplayMetrics]>
 */
val DisplayMetricsKClass get() = klassOf<DisplayMetrics>()

/**
 * 获得 [Display] 类型
 * @return [KClass]<[Display]>
 */
val DisplayKClass get() = klassOf<Display>()

/**
 * 获得 [Toast] 类型
 * @return [KClass]<[Toast]>
 */
val ToastKClass get() = klassOf<Toast>()

/**
 * 获得 [Intent] 类型
 * @return [KClass]<[Intent]>
 */
val IntentKClass get() = klassOf<Intent>()

/**
 * 获得 [ComponentInfo] 类型
 * @return [KClass]<[ComponentInfo]>
 */
val ComponentInfoKClass get() = klassOf<ComponentInfo>()

/**
 * 获得 [ComponentName] 类型
 * @return [KClass]<[ComponentName]>
 */
val ComponentNameKClass get() = klassOf<ComponentName>()

/**
 * 获得 [PendingIntent] 类型
 * @return [KClass]<[PendingIntent]>
 */
val PendingIntentKClass get() = klassOf<PendingIntent>()

/**
 * 获得 [ColorStateList] 类型
 * @return [KClass]<[ColorStateList]>
 */
val ColorStateListKClass get() = klassOf<ColorStateList>()

/**
 * 获得 [ContentValues] 类型
 * @return [KClass]<[ContentValues]>
 */
val ContentValuesKClass get() = klassOf<ContentValues>()

/**
 * 获得 [SharedPreferences] 类型
 * @return [KClass]<[SharedPreferences]>
 */
val SharedPreferencesKClass get() = klassOf<SharedPreferences>()

/**
 * 获得 [MediaPlayer] 类型
 * @return [KClass]<[MediaPlayer]>
 */
val MediaPlayerKClass get() = klassOf<MediaPlayer>()

/**
 * 获得 [ProgressDialog] 类型
 * @return [KClass]<[ProgressDialog]>
 */
val ProgressDialogKClass get() = klassOf<ProgressDialog>()

/**
 * 获得 [Log] 类型
 * @return [KClass]<[Log]>
 */
val LogKClass get() = klassOf<Log>()

/**
 * 获得 [Build] 类型
 * @return [KClass]<[Build]>
 */
val BuildKClass get() = klassOf<Build>()

/**
 * 获得 [Xml] 类型
 * @return [KClass]<[Xml]>
 */
val XmlKClass get() = klassOf<Xml>()

/**
 * 获得 [ContrastColorUtil] 类型
 * @return [KClass]
 */
val ContrastColorUtilKClass get() = "com.android.internal.util.ContrastColorUtil".toKClass()

/**
 * 获得 [StatusBarNotification] 类型
 * @return [KClass]<[StatusBarNotification]>
 */
val StatusBarNotificationKClass get() = klassOf<StatusBarNotification>()

/**
 * 获得 [Notification] 类型
 * @return [KClass]<[Notification]>
 */
val NotificationKClass get() = klassOf<Notification>()

/**
 * 获得 [Notification.Builder] 类型
 * @return [KClass]<[Notification.Builder]>
 */
val Notification_BuilderKClass get() = klassOf<Notification.Builder>()

/**
 * 获得 [Notification.Action] 类型
 * @return [KClass]<[Notification.Action]>
 */
val Notification_ActionKClass get() = klassOf<Notification.Action>()

/**
 * 获得 [DialogInterface] 类型
 * @return [KClass]<[DialogInterface]>
 */
val DialogInterfaceKClass get() = klassOf<DialogInterface>()

/**
 * 获得 [DialogInterface.OnClickListener] 类型
 * @return [KClass]<[DialogInterface.OnClickListener]>
 */
val DialogInterface_OnClickListenerKClass get() = klassOf<DialogInterface.OnClickListener>()

/**
 * 获得 [DialogInterface.OnCancelListener] 类型
 * @return [KClass]<[DialogInterface.OnCancelListener]>
 */
val DialogInterface_OnCancelListenerKClass get() = klassOf<DialogInterface.OnCancelListener>()

/**
 * 获得 [DialogInterface.OnDismissListener] 类型
 * @return [KClass]<[DialogInterface.OnDismissListener]>
 */
val DialogInterface_OnDismissListenerKClass get() = klassOf<DialogInterface.OnDismissListener>()

/**
 * 获得 [Environment] 类型
 * @return [KClass]<[Environment]>
 */
val EnvironmentKClass get() = klassOf<Environment>()

/**
 * 获得 [Process] 类型
 * @return [KClass]<[Process]>
 */
val ProcessKClass get() = klassOf<Process>()

/**
 * 获得 [Vibrator] 类型
 * @return [KClass]<[Vibrator]>
 */
val VibratorKClass get() = klassOf<Vibrator>()

/**
 * 获得 [VibrationEffect] 类型
 *
 * - 在 Android O (26) 及以上系统加入
 * @return [KClass]<[VibrationEffect]> or null
 */
val VibrationEffectKClass get() = if (Build.VERSION.SDK_INT >= 26) klassOf<VibrationEffect>() else null

/**
 * 获得 [VibrationAttributes] 类型
 *
 * - 在 Android R (30) 及以上系统加入
 * @return [KClass]<[VibrationAttributes]> or null
 */
val VibrationAttributesKClass get() = if (Build.VERSION.SDK_INT >= 30) klassOf<VibrationAttributes>() else null

/**
 * 获得 [SystemClock] 类型
 * @return [KClass]<[SystemClock]>
 */
val SystemClockKClass get() = klassOf<SystemClock>()

/**
 * 获得 [PowerManager] 类型
 * @return [KClass]<[PowerManager]>
 */
val PowerManagerKClass get() = klassOf<PowerManager>()

/**
 * 获得 [PowerManager.WakeLock] 类型
 * @return [KClass]<[PowerManager.WakeLock]>
 */
val PowerManager_WakeLockKClass get() = klassOf<PowerManager.WakeLock>()

/**
 * 获得 [UserHandle] 类型
 * @return [KClass]<[UserHandle]>
 */
val UserHandleKClass get() = klassOf<UserHandle>()

/**
 * 获得 [ShortcutInfo] 类型
 *
 * - 在 Android N_MR1 (25) 及以上系统加入
 * @return [KClass]<[ShortcutInfo]> or null
 */
val ShortcutInfoKClass get() = if (Build.VERSION.SDK_INT >= 25) klassOf<ShortcutInfo>() else null

/**
 * 获得 [ShortcutManager] 类型
 *
 * - 在 Android R (30) 及以上系统加入
 * @return [KClass]<[ShortcutManager]> or null
 */
val ShortcutManagerKClass get() = if (Build.VERSION.SDK_INT >= 30) klassOf<ShortcutManager>() else null

/**
 * 获得 [ShortcutQuery] 类型
 *
 * - 在 Android N_MR1 (25) 及以上系统加入
 * @return [KClass]<[ShortcutQuery]> or null
 */
val ShortcutQueryKClass get() = if (Build.VERSION.SDK_INT >= 25) klassOf<ShortcutQuery>() else null

/**
 * 获得 [KeyboardShortcutInfo] 类型
 * @return [KClass]<[KeyboardShortcutInfo]>
 */
val KeyboardShortcutInfoKClass get() = klassOf<KeyboardShortcutInfo>()

/**
 * 获得 [KeyboardShortcutGroup] 类型
 * @return [KClass]<[KeyboardShortcutGroup]>
 */
val KeyboardShortcutGroupKClass get() = klassOf<KeyboardShortcutGroup>()

/**
 * 获得 [ShortcutIconResource] 类型
 * @return [KClass]<[ShortcutIconResource]>
 */
val ShortcutIconResourceKClass get() = klassOf<ShortcutIconResource>()

/**
 * 获得 [AssetManager] 类型
 * @return [KClass]<[AssetManager]>
 */
val AssetManagerKClass get() = klassOf<AssetManager>()

/**
 * 获得 [AppWidgetManager] 类型
 * @return [KClass]<[AppWidgetManager]>
 */
val AppWidgetManagerKClass get() = klassOf<AppWidgetManager>()

/**
 * 获得 [AppWidgetProvider] 类型
 * @return [KClass]<[AppWidgetProvider]>
 */
val AppWidgetProviderKClass get() = klassOf<AppWidgetProvider>()

/**
 * 获得 [AppWidgetProviderInfo] 类型
 * @return [KClass]<[AppWidgetProviderInfo]>
 */
val AppWidgetProviderInfoKClass get() = klassOf<AppWidgetProviderInfo>()

/**
 * 获得 [AppWidgetHost] 类型
 * @return [KClass]<[AppWidgetHost]>
 */
val AppWidgetHostKClass get() = klassOf<AppWidgetHost>()

/**
 * 获得 [ActivityInfo] 类型
 * @return [KClass]<[ActivityInfo]>
 */
val ActivityInfoKClass get() = klassOf<ActivityInfo>()

/**
 * 获得 [ResolveInfo] 类型
 * @return [KClass]<[ResolveInfo]>
 */
val ResolveInfoKClass get() = klassOf<ResolveInfo>()

/**
 * 获得 [Property] 类型
 * @return [KClass]<[Property]>
 */
val PropertyKClass get() = klassOf<Property<*, *>>()

/**
 * 获得 [IntProperty] 类型
 * @return [KClass]<[IntProperty]>
 */
val IntPropertyKClass get() = klassOf<IntProperty<*>>()

/**
 * 获得 [FloatProperty] 类型
 * @return [KClass]<[FloatProperty]>
 */
val FloatPropertyKClass get() = klassOf<FloatProperty<*>>()

/**
 * 获得 [SQLiteDatabase] 类型
 * @return [KClass]<[SQLiteDatabase]>
 */
val SQLiteDatabaseKClass get() = klassOf<SQLiteDatabase>()

/**
 * 获得 [StrictMode] 类型
 * @return [KClass]<[StrictMode]>
 */
val StrictModeKClass get() = klassOf<StrictMode>()

/**
 * 获得 [AccessibilityManager] 类型
 * @return [KClass]<[AccessibilityManager]>
 */
val AccessibilityManagerKClass get() = klassOf<AccessibilityManager>()

/**
 * 获得 [AccessibilityEvent] 类型
 * @return [KClass]<[AccessibilityEvent]>
 */
val AccessibilityEventKClass get() = klassOf<AccessibilityEvent>()

/**
 * 获得 [AccessibilityNodeInfo] 类型
 * @return [KClass]<[AccessibilityNodeInfo]>
 */
val AccessibilityNodeInfoKClass get() = klassOf<AccessibilityNodeInfo>()

/**
 * 获得 [IInterface] 类型
 * @return [KClass]<[IInterface]>
 */
val IInterfaceKClass get() = klassOf<IInterface>()