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
import com.highcapable.yukireflection.type.android.*

/**
 * 获得 [android.R] 类型
 * @return [KClass]<[android.R]>
 */
val AndroidRKClass get() = AndroidRClass.kotlin

/**
 * 获得 [Context] 类型
 * @return [KClass]<[Context]>
 */
val ContextKClass get() = ContextClass.kotlin

/**
 * 获得 [ContextImpl] 类型
 * @return [KClass]
 */
val ContextImplKClass get() = ContextImplClass.kotlin

/**
 * 获得 [ContextWrapper] 类型
 * @return [KClass]<[ContextWrapper]>
 */
val ContextWrapperKClass get() = ContextWrapperClass.kotlin

/**
 * 获得 [Application] 类型
 * @return [KClass]<[Application]>
 */
val ApplicationKClass get() = ApplicationClass.kotlin

/**
 * 获得 [ApplicationInfo] 类型
 * @return [KClass]<[ApplicationInfo]>
 */
val ApplicationInfoKClass get() = ApplicationInfoClass.kotlin

/**
 * 获得 [Instrumentation] 类型
 * @return [KClass]<[Instrumentation]>
 */
val InstrumentationKClass get() = InstrumentationClass.kotlin

/**
 * 获得 [PackageInfo] 类型
 * @return [KClass]<[PackageInfo]>
 */
val PackageInfoKClass get() = PackageInfoClass.kotlin

/**
 * 获得 [ApplicationPackageManager] 类型
 * @return [KClass]
 */
val ApplicationPackageManagerKClass get() = ApplicationPackageManagerClass.kotlin

/**
 * 获得 [ActivityThread] 类型
 * @return [KClass]
 */
val ActivityThreadKClass get() = ActivityThreadClass.kotlin

/**
 * 获得 [ActivityManager] 类型
 * @return [KClass]<[ActivityManager]>
 */
val ActivityManagerKClass get() = ActivityManagerClass.kotlin

/**
 * 获得 [IActivityManager] 类型
 * @return [KClass]
 */
val IActivityManagerKClass get() = IActivityManagerClass.kotlin

/**
 * 获得 [ActivityManagerNative] 类型
 * @return [KClass]
 */
val ActivityManagerNativeKClass get() = ActivityManagerNativeClass.kotlin

/**
 * 获得 [IActivityTaskManager] 类型
 *
 * - 在 Android O (26) 及以上系统加入
 * @return [KClass] or null
 */
val IActivityTaskManagerKClass get() = IActivityTaskManagerClass?.kotlin

/**
 * 获得 [ActivityTaskManager] 类型
 *
 * - 在 Android O (26) 及以上系统加入
 * @return [KClass] or null
 */
val ActivityTaskManagerKClass get() = ActivityTaskManagerClass?.kotlin

/**
 * 获得 [IPackageManager] 类型
 * @return [KClass]
 */
val IPackageManagerKClass get() = IPackageManagerClass.kotlin

/**
 * 获得 [ClientTransaction] 类型
 * @return [KClass]
 */
val ClientTransactionKClass get() = ClientTransactionClass.kotlin

/**
 * 获得 [LoadedApk] 类型
 * @return [KClass]
 */
val LoadedApkKClass get() = LoadedApkClass.kotlin

/**
 * 获得 [Singleton] 类型
 * @return [KClass]
 */
val SingletonKClass get() = SingletonClass.kotlin

/**
 * 获得 [Activity] 类型
 * @return [KClass]<[Activity]>
 */
val ActivityKClass get() = ActivityClass.kotlin

/**
 * 获得 [Looper] 类型
 * @return [KClass]<[Looper]>
 */
val LooperKClass get() = LooperClass.kotlin

/**
 * 获得 [Fragment] 类型 - Support
 * @return [KClass]
 */
val FragmentKClass_AndroidSupport get() = FragmentClass_AndroidSupport.kotlin

/**
 * 获得 [Fragment] 类型 - AndroidX
 * @return [KClass]
 */
val FragmentKClass_AndroidX get() = FragmentClass_AndroidX.kotlin

/**
 * 获得 [FragmentActivity] 类型 - Support
 * @return [KClass]
 */
val FragmentActivityKClass_AndroidSupport get() = FragmentActivityClass_AndroidSupport.kotlin

/**
 * 获得 [FragmentActivity] 类型 - AndroidX
 * @return [KClass]
 */
val FragmentActivityKClass_AndroidX get() = FragmentActivityClass_AndroidX.kotlin

/**
 * 获得 [DocumentFile] 类型 - AndroidX
 * @return [KClass]
 */
val DocumentFileKClass get() = DocumentFileClass.kotlin

/**
 * 获得 [Service] 类型
 * @return [KClass]<[Service]>
 */
val ServiceKClass get() = ServiceClass.kotlin

/**
 * 获得 [Binder] 类型
 * @return [KClass]<[Binder]>
 */
val BinderKClass get() = BinderClass.kotlin

/**
 * 获得 [IBinder] 类型
 * @return [KClass]<[IBinder]>
 */
val IBinderKClass get() = IBinderClass.kotlin

/**
 * 获得 [BroadcastReceiver] 类型
 * @return [KClass]<[BroadcastReceiver]>
 */
val BroadcastReceiverKClass get() = BroadcastReceiverClass.kotlin

/**
 * 获得 [Bundle] 类型
 * @return [KClass]<[Bundle]>
 */
val BundleKClass get() = BundleClass.kotlin

/**
 * 获得 [BaseBundle] 类型
 * @return [KClass]<[BaseBundle]>
 */
val BaseBundleKClass get() = BaseBundleClass.kotlin

/**
 * 获得 [Resources] 类型
 * @return [KClass]<[Resources]>
 */
val ResourcesKClass get() = ResourcesClass.kotlin

/**
 * 获得 [Configuration] 类型
 * @return [KClass]<[Configuration]>
 */
val ConfigurationKClass get() = ConfigurationClass.kotlin

/**
 * 获得 [ConfigurationInfo] 类型
 * @return [KClass]<[ConfigurationInfo]>
 */
val ConfigurationInfoKClass get() = ConfigurationInfoClass.kotlin

/**
 * 获得 [ContentResolver] 类型
 * @return [KClass]<[ContentResolver]>
 */
val ContentResolverKClass get() = ContentResolverClass.kotlin

/**
 * 获得 [ContentProvider] 类型
 * @return [KClass]<[ContentProvider]>
 */
val ContentProviderKClass get() = ContentProviderClass.kotlin

/**
 * 获得 [Settings] 类型
 * @return [KClass]<[Settings]>
 */
val SettingsKClass get() = SettingsClass.kotlin

/**
 * 获得 [Settings.System] 类型
 * @return [KClass]<[Settings.System]>
 */
val Settings_SystemKClass get() = Settings_SystemClass.kotlin

/**
 * 获得 [Settings.Secure] 类型
 * @return [KClass]<[Settings.Secure]>
 */
val Settings_SecureKClass get() = Settings_SecureClass.kotlin

/**
 * 获得 [TypedArray] 类型
 * @return [KClass]<[TypedArray]>
 */
val TypedArrayKClass get() = TypedArrayClass.kotlin

/**
 * 获得 [TypedValue] 类型
 * @return [KClass]<[TypedValue]>
 */
val TypedValueKClass get() = TypedValueClass.kotlin

/**
 * 获得 [SparseArray] 类型
 * @return [KClass]<[SparseArray]>
 */
val SparseArrayKClass get() = SparseArrayClass.kotlin

/**
 * 获得 [SparseIntArray] 类型
 * @return [KClass]<[SparseIntArray]>
 */
val SparseIntArrayKClass get() = SparseIntArrayClass.kotlin

/**
 * 获得 [SparseBooleanArray] 类型
 * @return [KClass]<[SparseBooleanArray]>
 */
val SparseBooleanArrayKClass get() = SparseBooleanArrayClass.kotlin

/**
 * 获得 [SparseLongArray] 类型
 * @return [KClass]<[SparseLongArray]>
 */
val SparseLongArrayKClass get() = SparseLongArrayClass.kotlin

/**
 * 获得 [LongSparseArray] 类型
 * @return [KClass]<[LongSparseArray]>
 */
val LongSparseArrayKClass get() = LongSparseArrayClass.kotlin

/**
 * 获得 [ArrayMap] 类型
 * @return [KClass]<[ArrayMap]>
 */
val ArrayMapKClass get() = ArrayMapClass.kotlin

/**
 * 获得 [ArraySet] 类型
 *
 * - 在 Android M (23) 及以上系统加入
 * @return [KClass]<[ArraySet]> or null
 */
val ArraySetKClass get() = ArraySetClass?.kotlin

/**
 * 获得 [Handler] 类型
 * @return [KClass]<[Handler]>
 */
val HandlerKClass get() = HandlerClass.kotlin

/**
 * 获得 [Handler.Callback] 类型
 * @return [KClass]<[Handler.Callback]>
 */
val Handler_CallbackKClass get() = Handler_CallbackClass.kotlin

/**
 * 获得 [Message] 类型
 * @return [KClass]<[Message]>
 */
val MessageKClass get() = MessageClass.kotlin

/**
 * 获得 [MessageQueue] 类型
 * @return [KClass]<[MessageQueue]>
 */
val MessageQueueKClass get() = MessageQueueClass.kotlin

/**
 * 获得 [Messenger] 类型
 * @return [KClass]<[Messenger]>
 */
val MessengerKClass get() = MessengerClass.kotlin

/**
 * 获得 [AsyncTask] 类型
 * @return [KClass]<[AsyncTask]>
 */
val AsyncTaskKClass get() = AsyncTaskClass.kotlin

/**
 * 获得 [SimpleDateFormat] 类型
 *
 * - 在 Android N (24) 及以上系统加入
 * @return [KClass]<[SimpleDateFormat]> or null
 */
val SimpleDateFormatKClass_Android get() = SimpleDateFormatClass_Android?.kotlin

/**
 * 获得 [Base64] 类型
 * @return [KClass]<[Base64]>
 */
val Base64KClass_Android get() = Base64Class_Android.kotlin

/**
 * 获得 [Window] 类型
 * @return [KClass]<[Window]>
 */
val WindowKClass get() = WindowClass.kotlin

/**
 * 获得 [WindowMetrics] 类型
 *
 * - 在 Android R (30) 及以上系统加入
 * @return [KClass]<[WindowMetrics]> or null
 */
val WindowMetricsKClass get() = WindowMetricsClass?.kotlin

/**
 * 获得 [WindowInsets] 类型
 * @return [KClass]<[WindowInsets]>
 */
val WindowInsetsKClass get() = WindowInsetsClass.kotlin

/**
 * 获得 [WindowInsets.Type] 类型
 *
 * - 在 Android R (30) 及以上系统加入
 * @return [KClass]<[WindowInsets.Type]> or null
 */
val WindowInsets_TypeKClass get() = WindowInsets_TypeClass?.kotlin

/**
 * 获得 [WindowManager] 类型
 * @return [KClass]<[WindowManager]>
 */
val WindowManagerKClass get() = WindowManagerClass.kotlin

/**
 * 获得 [WindowManager.LayoutParams] 类型
 * @return [KClass]<[WindowManager.LayoutParams]>
 */
val WindowManager_LayoutParamsKClass get() = WindowManager_LayoutParamsClass.kotlin

/**
 * 获得 [ViewManager] 类型
 * @return [KClass]<[ViewManager]>
 */
val ViewManagerKClass get() = ViewManagerClass.kotlin

/**
 * 获得 [Parcel] 类型
 * @return [KClass]<[Parcel]>
 */
val ParcelKClass get() = ParcelClass.kotlin

/**
 * 获得 [Parcelable] 类型
 * @return [KClass]<[Parcelable]>
 */
val ParcelableKClass get() = ParcelableClass.kotlin

/**
 * 获得 [Parcelable.Creator] 类型
 * @return [KClass]<[Parcelable.Creator]>
 */
val Parcelable_CreatorKClass get() = Parcelable_CreatorClass.kotlin

/**
 * 获得 [Dialog] 类型
 * @return [KClass]<[Dialog]>
 */
val DialogKClass get() = DialogClass.kotlin

/**
 * 获得 [AlertDialog] 类型
 * @return [KClass]<[AlertDialog]>
 */
val AlertDialogKClass get() = AlertDialogClass.kotlin

/**
 * 获得 [DisplayMetrics] 类型
 * @return [KClass]<[DisplayMetrics]>
 */
val DisplayMetricsKClass get() = DisplayMetricsClass.kotlin

/**
 * 获得 [Display] 类型
 * @return [KClass]<[Display]>
 */
val DisplayKClass get() = DisplayClass.kotlin

/**
 * 获得 [Toast] 类型
 * @return [KClass]<[Toast]>
 */
val ToastKClass get() = ToastClass.kotlin

/**
 * 获得 [Intent] 类型
 * @return [KClass]<[Intent]>
 */
val IntentKClass get() = IntentClass.kotlin

/**
 * 获得 [ComponentInfo] 类型
 * @return [KClass]<[ComponentInfo]>
 */
val ComponentInfoKClass get() = ComponentInfoClass.kotlin

/**
 * 获得 [ComponentName] 类型
 * @return [KClass]<[ComponentName]>
 */
val ComponentNameKClass get() = ComponentNameClass.kotlin

/**
 * 获得 [PendingIntent] 类型
 * @return [KClass]<[PendingIntent]>
 */
val PendingIntentKClass get() = PendingIntentClass.kotlin

/**
 * 获得 [ColorStateList] 类型
 * @return [KClass]<[ColorStateList]>
 */
val ColorStateListKClass get() = ColorStateListClass.kotlin

/**
 * 获得 [ContentValues] 类型
 * @return [KClass]<[ContentValues]>
 */
val ContentValuesKClass get() = ContentValuesClass.kotlin

/**
 * 获得 [SharedPreferences] 类型
 * @return [KClass]<[SharedPreferences]>
 */
val SharedPreferencesKClass get() = SharedPreferencesClass.kotlin

/**
 * 获得 [MediaPlayer] 类型
 * @return [KClass]<[MediaPlayer]>
 */
val MediaPlayerKClass get() = MediaPlayerClass.kotlin

/**
 * 获得 [ProgressDialog] 类型
 * @return [KClass]<[ProgressDialog]>
 */
val ProgressDialogKClass get() = ProgressDialogClass.kotlin

/**
 * 获得 [Log] 类型
 * @return [KClass]<[Log]>
 */
val LogKClass get() = LogClass.kotlin

/**
 * 获得 [Build] 类型
 * @return [KClass]<[Build]>
 */
val BuildKClass get() = BuildClass.kotlin

/**
 * 获得 [Xml] 类型
 * @return [KClass]<[Xml]>
 */
val XmlKClass get() = XmlClass.kotlin

/**
 * 获得 [ContrastColorUtil] 类型
 * @return [KClass]
 */
val ContrastColorUtilKClass get() = ContrastColorUtilClass.kotlin

/**
 * 获得 [StatusBarNotification] 类型
 * @return [KClass]<[StatusBarNotification]>
 */
val StatusBarNotificationKClass get() = StatusBarNotificationClass.kotlin

/**
 * 获得 [Notification] 类型
 * @return [KClass]<[Notification]>
 */
val NotificationKClass get() = NotificationClass.kotlin

/**
 * 获得 [Notification.Builder] 类型
 * @return [KClass]<[Notification.Builder]>
 */
val Notification_BuilderKClass get() = Notification_BuilderClass.kotlin

/**
 * 获得 [Notification.Action] 类型
 * @return [KClass]<[Notification.Action]>
 */
val Notification_ActionKClass get() = Notification_ActionClass.kotlin

/**
 * 获得 [DialogInterface] 类型
 * @return [KClass]<[DialogInterface]>
 */
val DialogInterfaceKClass get() = DialogInterfaceClass.kotlin

/**
 * 获得 [DialogInterface.OnClickListener] 类型
 * @return [KClass]<[DialogInterface.OnClickListener]>
 */
val DialogInterface_OnClickListenerKClass get() = DialogInterface_OnClickListenerClass.kotlin

/**
 * 获得 [DialogInterface.OnCancelListener] 类型
 * @return [KClass]<[DialogInterface.OnCancelListener]>
 */
val DialogInterface_OnCancelListenerKClass get() = DialogInterface_OnCancelListenerClass.kotlin

/**
 * 获得 [DialogInterface.OnDismissListener] 类型
 * @return [KClass]<[DialogInterface.OnDismissListener]>
 */
val DialogInterface_OnDismissListenerKClass get() = DialogInterface_OnDismissListenerClass.kotlin

/**
 * 获得 [Environment] 类型
 * @return [KClass]<[Environment]>
 */
val EnvironmentKClass get() = EnvironmentClass.kotlin

/**
 * 获得 [Process] 类型
 * @return [KClass]<[Process]>
 */
val ProcessKClass get() = ProcessClass.kotlin

/**
 * 获得 [Vibrator] 类型
 * @return [KClass]<[Vibrator]>
 */
val VibratorKClass get() = VibratorClass.kotlin

/**
 * 获得 [VibrationEffect] 类型
 *
 * - 在 Android O (26) 及以上系统加入
 * @return [KClass]<[VibrationEffect]> or null
 */
val VibrationEffectKClass get() = VibrationEffectClass?.kotlin

/**
 * 获得 [VibrationAttributes] 类型
 *
 * - 在 Android R (30) 及以上系统加入
 * @return [KClass]<[VibrationAttributes]> or null
 */
val VibrationAttributesKClass get() = VibrationAttributesClass?.kotlin

/**
 * 获得 [SystemClock] 类型
 * @return [KClass]<[SystemClock]>
 */
val SystemClockKClass get() = SystemClockClass.kotlin

/**
 * 获得 [PowerManager] 类型
 * @return [KClass]<[PowerManager]>
 */
val PowerManagerKClass get() = PowerManagerClass.kotlin

/**
 * 获得 [PowerManager.WakeLock] 类型
 * @return [KClass]<[PowerManager.WakeLock]>
 */
val PowerManager_WakeLockKClass get() = PowerManager_WakeLockClass.kotlin

/**
 * 获得 [UserHandle] 类型
 * @return [KClass]<[UserHandle]>
 */
val UserHandleKClass get() = UserHandleClass.kotlin

/**
 * 获得 [ShortcutInfo] 类型
 *
 * - 在 Android N_MR1 (25) 及以上系统加入
 * @return [KClass]<[ShortcutInfo]> or null
 */
val ShortcutInfoKClass get() = ShortcutInfoClass?.kotlin

/**
 * 获得 [ShortcutManager] 类型
 *
 * - 在 Android R (30) 及以上系统加入
 * @return [KClass]<[ShortcutManager]> or null
 */
val ShortcutManagerKClass get() = ShortcutManagerClass?.kotlin

/**
 * 获得 [ShortcutQuery] 类型
 *
 * - 在 Android N_MR1 (25) 及以上系统加入
 * @return [KClass]<[ShortcutQuery]> or null
 */
val ShortcutQueryKClass get() = ShortcutQueryClass?.kotlin

/**
 * 获得 [KeyboardShortcutInfo] 类型
 * @return [KClass]<[KeyboardShortcutInfo]>
 */
val KeyboardShortcutInfoKClass get() = KeyboardShortcutInfoClass.kotlin

/**
 * 获得 [KeyboardShortcutGroup] 类型
 * @return [KClass]<[KeyboardShortcutGroup]>
 */
val KeyboardShortcutGroupKClass get() = KeyboardShortcutGroupClass.kotlin

/**
 * 获得 [ShortcutIconResource] 类型
 * @return [KClass]<[ShortcutIconResource]>
 */
val ShortcutIconResourceKClass get() = ShortcutIconResourceClass.kotlin

/**
 * 获得 [AssetManager] 类型
 * @return [KClass]<[AssetManager]>
 */
val AssetManagerKClass get() = AssetManagerClass.kotlin

/**
 * 获得 [AppWidgetManager] 类型
 * @return [KClass]<[AppWidgetManager]>
 */
val AppWidgetManagerKClass get() = AppWidgetManagerClass.kotlin

/**
 * 获得 [AppWidgetProvider] 类型
 * @return [KClass]<[AppWidgetProvider]>
 */
val AppWidgetProviderKClass get() = AppWidgetProviderClass.kotlin

/**
 * 获得 [AppWidgetProviderInfo] 类型
 * @return [KClass]<[AppWidgetProviderInfo]>
 */
val AppWidgetProviderInfoKClass get() = AppWidgetProviderInfoClass.kotlin

/**
 * 获得 [AppWidgetHost] 类型
 * @return [KClass]<[AppWidgetHost]>
 */
val AppWidgetHostKClass get() = AppWidgetHostClass.kotlin

/**
 * 获得 [ActivityInfo] 类型
 * @return [KClass]<[ActivityInfo]>
 */
val ActivityInfoKClass get() = ActivityInfoClass.kotlin

/**
 * 获得 [ResolveInfo] 类型
 * @return [KClass]<[ResolveInfo]>
 */
val ResolveInfoKClass get() = ResolveInfoClass.kotlin

/**
 * 获得 [Property] 类型
 * @return [KClass]<[Property]>
 */
val PropertyKClass get() = PropertyClass.kotlin

/**
 * 获得 [IntProperty] 类型
 * @return [KClass]<[IntProperty]>
 */
val IntPropertyKClass get() = IntPropertyClass.kotlin

/**
 * 获得 [FloatProperty] 类型
 * @return [KClass]<[FloatProperty]>
 */
val FloatPropertyKClass get() = FloatPropertyClass.kotlin

/**
 * 获得 [SQLiteDatabase] 类型
 * @return [KClass]<[SQLiteDatabase]>
 */
val SQLiteDatabaseKClass get() = SQLiteDatabaseClass.kotlin

/**
 * 获得 [StrictMode] 类型
 * @return [KClass]<[StrictMode]>
 */
val StrictModeKClass get() = StrictModeClass.kotlin

/**
 * 获得 [AccessibilityManager] 类型
 * @return [KClass]<[AccessibilityManager]>
 */
val AccessibilityManagerKClass get() = AccessibilityManagerClass.kotlin

/**
 * 获得 [AccessibilityEvent] 类型
 * @return [KClass]<[AccessibilityEvent]>
 */
val AccessibilityEventKClass get() = AccessibilityEventClass.kotlin

/**
 * 获得 [AccessibilityNodeInfo] 类型
 * @return [KClass]<[AccessibilityNodeInfo]>
 */
val AccessibilityNodeInfoKClass get() = AccessibilityNodeInfoClass.kotlin

/**
 * 获得 [IInterface] 类型
 * @return [KClass]<[IInterface]>
 */
val IInterfaceKClass get() = IInterfaceClass.kotlin