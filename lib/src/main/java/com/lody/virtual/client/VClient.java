package com.lody.virtual.client;

import java.lang.ThreadGroup;;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Instrumentation;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Binder;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.StrictMode;
import android.os.Build.VERSION;
import android.security.net.config.ApplicationConfig;
import android.util.Log;
import android.view.autofill.AutofillManager;
import androidx.annotation.RequiresApi;
import com.lody.virtual.PineXposed;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.CrashHandler;
import com.lody.virtual.client.core.InvocationStubManager;
import com.lody.virtual.client.core.LaunchCallBack;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.fixer.ContextFixer;
import com.lody.virtual.client.hook.instruments.InstrumentationVirtualApp;
import com.lody.virtual.client.hook.providers.ProviderHook;
import com.lody.virtual.client.hook.providers.SettingsProviderHook;
import com.lody.virtual.client.hook.proxies.am.HCallbackStub;
import com.lody.virtual.client.hook.proxies.view.AutoFillManagerStub;
import com.lody.virtual.client.hook.secondary.ProxyServiceFactory;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.ipc.VDeviceManager;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.client.ipc.VirtualStorageManager;
import com.lody.virtual.client.service.VServiceRuntime;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.ComposeClassLoader;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.compat.StorageManagerCompat;
import com.lody.virtual.helper.compat.StrictModeCompat;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.oem.EmuiHelper;
import com.lody.virtual.oem.apps.WeChat;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.receiver.StaticReceiverSystem;
import com.lody.virtual.remote.ClientConfig;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.remote.VDeviceConfig;
import com.lody.virtual.server.extension.VExtPackageAccessor;
import com.lody.virtual.server.secondary.FakeIdentityBinder;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mirror.android.app.ActivityManagerNative;
import mirror.android.app.ActivityThread;
import mirror.android.app.ActivityThreadNMR1;
import mirror.android.app.ActivityThreadQ;
import mirror.android.app.ContextImpl;
import mirror.android.app.ContextImplKitkat;
import mirror.android.app.IActivityManager;
import mirror.android.app.LoadedApk;
import mirror.android.app.LoadedApkKitkat;
import mirror.android.content.ContentProviderHolderOreo;
import mirror.android.content.res.CompatibilityInfo;
import mirror.android.providers.Settings;
import mirror.android.renderscript.RenderScriptCacheDir;
import mirror.android.view.DisplayAdjustments;
import mirror.android.view.HardwareRenderer;
import mirror.android.view.RenderScript;
import mirror.android.view.ThreadedRenderer;
import mirror.com.android.internal.content.ReferrerIntent;
import mirror.dalvik.system.VMRuntime;
import mirror.java.lang.ThreadGroupN;

public final class VClient extends IVClient.Stub {
   private static final int NEW_INTENT = 11;
   private static final int RECEIVER = 12;
   private static final int FINISH_ACTIVITY = 13;
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")) + VClient.class.getSimpleName();
   @SuppressLint({"StaticFieldLeak"})
   private static final VClient gClient = new VClient();
   private final H mH = new H();
   private Instrumentation mInstrumentation;
   private ClientConfig clientConfig;
   private int corePid;
   private AppBindData mBoundApplication;
   private Application mInitialApplication;
   private CrashHandler crashHandler;
   private InstalledAppInfo mAppInfo;
   private final Map<String, Application> mAllApplications = new HashMap(1);
   private Set<String> mExportedVApiPkgs = new HashSet();
   private static boolean CheckJunitClazz = false;

   public int getCorePid() {
      return this.corePid;
   }

   public void setCorePid(int corePid) {
      this.corePid = corePid;
   }

   private VClient() {
   }

   public synchronized void addExportedVApiPkg(String pkg) {
      this.mExportedVApiPkgs.add(pkg);
   }

   public InstalledAppInfo getAppInfo() {
      return this.mAppInfo;
   }

   public static VClient get() {
      return gClient;
   }

   public boolean isDynamicApp() {
      InstalledAppInfo appInfo = this.getAppInfo();
      return appInfo != null && appInfo.dynamic;
   }

   public VDeviceConfig getDeviceConfig() {
      return VDeviceManager.get().getDeviceConfig(VUserHandle.getUserId(this.getVUid()));
   }

   public Application getCurrentApplication() {
      return this.mInitialApplication;
   }

   public String getCurrentPackage() {
      return this.mBoundApplication != null ? this.mBoundApplication.appInfo.packageName : VPackageManager.get().getNameForUid(this.getVUid());
   }

   public ApplicationInfo getCurrentApplicationInfo() {
      return this.mBoundApplication != null ? this.mBoundApplication.appInfo : null;
   }

   public CrashHandler getCrashHandler() {
      return this.crashHandler;
   }

   public void setCrashHandler(CrashHandler crashHandler) {
      this.crashHandler = crashHandler;
   }

   public int getVUid() {
      return this.clientConfig == null ? 0 : this.clientConfig.vuid;
   }

   public int getVUserHandle() {
      return this.clientConfig == null ? 0 : VUserHandle.getUserId(this.clientConfig.vuid);
   }

   public int getVpid() {
      return this.clientConfig == null ? 0 : this.clientConfig.vpid;
   }

   public int getBaseVUid() {
      return this.clientConfig == null ? 0 : VUserHandle.getAppId(this.clientConfig.vuid);
   }

   public ClassLoader getClassLoader(ApplicationInfo appInfo) {
      Context context = this.createPackageContext(appInfo.packageName);
      return context.getClassLoader();
   }

   private void sendMessage(int what, Object obj) {
      Message msg = Message.obtain();
      msg.what = what;
      msg.obj = obj;
      this.mH.sendMessage(msg);
   }

   public IBinder getAppThread() {
      return (IBinder)ActivityThread.getApplicationThread.call(VirtualCore.mainThread());
   }

   public IBinder getToken() {
      return this.clientConfig == null ? null : this.clientConfig.token;
   }

   public ClientConfig getClientConfig() {
      return this.clientConfig;
   }

   public boolean isAppRunning() {
      return this.mInitialApplication != null;
   }

   public boolean isProcessBound() {
      return this.clientConfig != null;
   }

   public void initProcess(ClientConfig clientConfig) {
      if (this.clientConfig != null) {
         throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uMmgVLAZLHgY2KQg1OmowRSVoJyg6KTo6Vg==")) + clientConfig.vpid + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl5WOA==")) + clientConfig.processName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186LGUFAgNLESQqKi0qPWoKDShqDjMrPyo6Vg==")) + this.clientConfig.processName);
      } else {
         this.clientConfig = clientConfig;
      }
   }

   private void handleNewIntent(NewIntentData data) {
      ComponentUtils.unpackFillIn(data.intent, get().getClassLoader());
      Intent intent = VERSION.SDK_INT >= 22 ? (Intent)ReferrerIntent.ctor.newInstance(data.intent, data.creator) : data.intent;
      if (ActivityThread.performNewIntents != null) {
         ActivityThread.performNewIntents.call(VirtualCore.mainThread(), data.token, Collections.singletonList(intent));
      } else if (ActivityThreadNMR1.performNewIntents != null) {
         ActivityThreadNMR1.performNewIntents.call(VirtualCore.mainThread(), data.token, Collections.singletonList(intent), true);
      } else if (BuildCompat.isS()) {
         Object obj = ((Map)ActivityThread.mActivities.get(VirtualCore.mainThread())).get(data.token);
         if (obj != null) {
            ActivityThread.handleNewIntent(obj, Collections.singletonList(intent));
         }
      } else {
         ActivityThreadQ.handleNewIntent.call(VirtualCore.mainThread(), data.token, Collections.singletonList(intent));
      }

   }

   public void bindApplication(final String packageName, final String processName) {
      synchronized(this.mAllApplications) {
         if (this.mAllApplications.containsKey(packageName)) {
            return;
         }
      }

      if (this.clientConfig == null) {
         throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcKmgVLCVhNAo/KF4mKmoVNClrDjA6Pyo6Vg==")) + processName);
      } else {
         if (Looper.myLooper() != Looper.getMainLooper()) {
            final ConditionVariable cond = new ConditionVariable();
            VirtualRuntime.getUIHandler().post(new Runnable() {
               public void run() {
                  VClient.this.bindApplicationMainThread(packageName, processName, cond);
                  cond.open();
               }
            });
            cond.block();
         } else {
            this.bindApplicationMainThread(packageName, processName, (ConditionVariable)null);
         }

      }
   }

   @RequiresApi(
      api = 26
   )
   private void bindApplicationMainThread(String packageName, String processName, ConditionVariable cond) {
      synchronized(this.mAllApplications) {
         if (this.mAllApplications.containsKey(packageName)) {
            return;
         }
      }

      VirtualCore.getConfig();
      Log.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT49O35SIClgJFk+KQc5OnozSFo=")), SettingConfig.isUseNativeEngine2(packageName) + "");
      if (VirtualCore.get().isExtHelperProcess()) {
         VExtPackageAccessor.syncPackages();
      }

      boolean isInitialApp = this.mInitialApplication == null;
      Binder.clearCallingIdentity();
      if (processName == null) {
         processName = packageName;
      }

      try {
         this.setupUncaughtHandler();
      } catch (Throwable var23) {
         Throwable e = var23;
         e.printStackTrace();
      }

      int userId = VUserHandle.getUserId(this.getVUid());

      try {
         this.fixInstalledProviders();
         SettingsProviderHook.passSettingsProviderPermissionCheck(packageName);
      } catch (Throwable var22) {
         Throwable e = var22;
         e.printStackTrace();
      }

      VDeviceConfig deviceConfig = this.getDeviceConfig();
      VDeviceManager.get().applyBuildProp(deviceConfig);
      ActivityThread.mInitialApplication.set(VirtualCore.mainThread(), null);
      AppBindData data = new AppBindData();
      InstalledAppInfo info = VirtualCore.get().getInstalledAppInfo(packageName, 0);
      if (info == null) {
         (new Exception(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KHsFBiVmVyQ/LRccL2UzSFo=")))).printStackTrace();
         Process.killProcess(0);
         System.exit(0);
      }

      if (isInitialApp) {
         this.mAppInfo = info;
      }

      data.appInfo = VPackageManager.get().getApplicationInfo(packageName, 0, userId);
      data.processName = processName;
      data.providers = VPackageManager.get().queryContentProviders(processName, this.getVUid(), 128);
      Iterator<ProviderInfo> iterator = data.providers.iterator();

      while(iterator.hasNext()) {
         ProviderInfo providerInfo = (ProviderInfo)iterator.next();
         if (!providerInfo.enabled) {
            iterator.remove();
         }
      }

      boolean isExt = VirtualCore.get().isExtPackage();
      VLog.i(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jj4YCGgFAiZiICQ7IxgmCGwjAjdvER4cLCo5J2EkOyt+AS8pJgQAIGEJAlo=")), data.appInfo.packageName, data.processName, Process.myPid());
      if (isInitialApp) {
         VirtualCore.getConfig();
         this.mBoundApplication = data;
         VirtualRuntime.setupRuntime(data.processName, data.appInfo);
         this.mInstrumentation = (Instrumentation)ActivityThread.mInstrumentation.get(VirtualCore.mainThread());
         int targetSdkVersion = data.appInfo.targetSdkVersion;
         if (targetSdkVersion < 9) {
            StrictMode.ThreadPolicy newPolicy = (new StrictMode.ThreadPolicy.Builder(StrictMode.getThreadPolicy())).permitNetwork().build();
            StrictMode.setThreadPolicy(newPolicy);
         }

         if (VERSION.SDK_INT >= 24 && VirtualCore.get().getTargetSdkVersion() >= 24 && targetSdkVersion < 24) {
            StrictModeCompat.disableDeathOnFileUriExposure();
         }

         if (targetSdkVersion < 21) {
            mirror.android.os.Message.updateCheckRecycle.call(targetSdkVersion);
         }

         AlarmManager alarmManager = (AlarmManager)VirtualCore.get().getContext().getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggEP28jElo=")));
         if (mirror.android.app.AlarmManager.mTargetSdkVersion != null) {
            try {
               mirror.android.app.AlarmManager.mTargetSdkVersion.set(alarmManager, targetSdkVersion);
            } catch (Exception var21) {
               Exception e = var21;
               e.printStackTrace();
            }
         }

         if (isExt) {
            System.setProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4+LmtSBi9gIFkgKggmPmwgRVo=")), (new File(VEnvironment.getDataUserPackageDirectoryExt(userId, info.packageName), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+OWUFNFo=")))).getAbsolutePath());
         } else {
            System.setProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4+LmtSBi9gIFkgKggmPmwgRVo=")), (new File(VEnvironment.getDataUserPackageDirectory(userId, info.packageName), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+OWUFNFo=")))).getAbsolutePath());
         }

         VLog.i(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+I2ojLCBqDlk9KQcYPQ==")));
         NativeEngine.launchEngine(packageName);
         if (VirtualCore.getConfig().isEnableIORedirect()) {
            this.mountVirtualFS(info, isExt);
         }
      }

      VirtualCore.getConfig();
      Object mainThread = VirtualCore.mainThread();
      this.initDataStorage(isExt, userId, packageName);
      Context context = this.createPackageContext(data.appInfo.packageName);
      if (isInitialApp) {
         VLog.i(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMBZiARoLLD0MKGoVLCxrAVRF")));
         NativeEngine.startDexOverride();
         StaticReceiverSystem.get().attach(packageName, VirtualCore.get().getContext(), data.appInfo, userId);
         File codeCacheDir;
         if (VERSION.SDK_INT >= 23) {
            codeCacheDir = context.getCodeCacheDir();
         } else {
            codeCacheDir = context.getCacheDir();
         }

         if (VERSION.SDK_INT < 24) {
            if (HardwareRenderer.setupDiskCache != null) {
               HardwareRenderer.setupDiskCache.call(codeCacheDir);
            }
         } else if (ThreadedRenderer.setupDiskCache != null) {
            ThreadedRenderer.setupDiskCache.call(codeCacheDir);
         }

         if (VERSION.SDK_INT >= 23) {
            if (RenderScriptCacheDir.setupDiskCache != null) {
               RenderScriptCacheDir.setupDiskCache.call(codeCacheDir);
            }
         } else if (RenderScript.setupDiskCache != null) {
            RenderScript.setupDiskCache.call(codeCacheDir);
         }

         this.mBoundApplication.info = ContextImpl.mPackageInfo.get(context);
         Object boundApp = ActivityThread.mBoundApplication.get(mainThread);
         ActivityThread.AppBindData.appInfo.set(boundApp, data.appInfo);
         ActivityThread.AppBindData.processName.set(boundApp, data.processName);
         ActivityThread.AppBindData.instrumentationName.set(boundApp, new ComponentName(data.appInfo.packageName, Instrumentation.class.getName()));
         ActivityThread.AppBindData.info.set(boundApp, data.info);
         ActivityThread.AppBindData.providers.set(boundApp, data.providers);
         if (LoadedApk.mSecurityViolation != null) {
            LoadedApk.mSecurityViolation.set(this.mBoundApplication.info, false);
         }

         VMRuntime.setTargetSdkVersion.call(VMRuntime.getRuntime.call(), data.appInfo.targetSdkVersion);
         Configuration configuration = context.getResources().getConfiguration();
         Object compatInfo = null;
         if (CompatibilityInfo.ctor != null) {
            compatInfo = CompatibilityInfo.ctor.newInstance(data.appInfo, configuration.screenLayout, configuration.smallestScreenWidthDp, false);
         }

         if (CompatibilityInfo.ctorLG != null) {
            compatInfo = CompatibilityInfo.ctorLG.newInstance(data.appInfo, configuration.screenLayout, configuration.smallestScreenWidthDp, false, 0);
         }

         if (compatInfo != null) {
            if (VERSION.SDK_INT < 24) {
               DisplayAdjustments.setCompatibilityInfo.call(ContextImplKitkat.mDisplayAdjustments.get(context), compatInfo);
            }

            DisplayAdjustments.setCompatibilityInfo.call(LoadedApkKitkat.mDisplayAdjustments.get(this.mBoundApplication.info), compatInfo);
         }

         if (VERSION.SDK_INT >= 30) {
            ApplicationConfig.setDefaultInstance((ApplicationConfig)null);
         }

         VLog.i(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxpcHUZaOQtYEEZJKRdfDWwNGzcUPzkLHC5SVg==")));
         PineXposed.initForXposed(context, processName);
         this.fixSystem();
         VirtualCore.get().getAppCallback().beforeStartApplication(packageName, processName, context);
         if (this.mExportedVApiPkgs.contains(packageName) && LoadedApk.mClassLoader != null) {
            LoadedApk.mClassLoader.set(data.info, new ComposeClassLoader(VClient.class.getClassLoader(), (ClassLoader)LoadedApk.getClassLoader.call(data.info)));
         }
      }

      if (CheckJunitClazz && BuildCompat.isR() && data.appInfo.targetSdkVersion < 30) {
         ClassLoader cl = (ClassLoader)LoadedApk.getClassLoader.call(data.info);
         if (VERSION.SDK_INT >= 30) {
            Reflect.on((Object)cl).set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+KmgVBgY=")), new ClassLoader() {
               protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                  return name.startsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD0uCGUaMFo="))) ? VClient.class.getClassLoader().loadClass(name) : super.loadClass(name, resolve);
               }
            });
         }
      }

      Application app;
      try {
         if (VirtualCore.getConfig().resumeInstrumentationInMakeApplication(packageName) && this.mInstrumentation instanceof InstrumentationVirtualApp) {
            InstrumentationVirtualApp vaIns = (InstrumentationVirtualApp)this.mInstrumentation;
            this.mInstrumentation = vaIns.getBaseInstrumentation();
            ActivityThread.mInstrumentation.set(VirtualCore.mainThread(), this.mInstrumentation);
            app = (Application)LoadedApk.makeApplication.call(data.info, false, vaIns);
            this.mInstrumentation = vaIns;
            ActivityThread.mInstrumentation.set(VirtualCore.mainThread(), this.mInstrumentation);
         } else {
            app = (Application)LoadedApk.makeApplication.call(data.info, false, null);
         }
      } catch (Throwable var25) {
         Throwable e = var25;
         throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcP2sjHitLEQo1PxdXOWwFGhFsHjwdIxg2O2YaGipsN1RF")), e);
      }

      ContextFixer.fixContext(app, data.appInfo.packageName);
      WeChat.disableBinderHook(packageName, app);
      if (isInitialApp) {
         this.mInitialApplication = app;
         ActivityThread.mInitialApplication.set(mainThread, app);
      }

      VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC4AD2UzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2XGojAgZjDiAoJwgmKmczSFo=")) + isInitialApp + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFEglgNAYgKQciCGYgTQJlER4qLRcqI2AgRD0=")) + this.mInitialApplication);
      boolean isTargetGame = packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogPCtgDiAwKAMYD2wgRSM="))) || packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojQTdjJCA1KC0iD2kgDSZlJywaLC5SVg=="))) || packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojQTdjJCA1KC0iD2kgDSZoDgoqIwguMX0FMFo=")));
      Object boundApp;
      List providers;
      if (isTargetGame) {
         boundApp = ActivityThread.mBoundApplication.get(mainThread);
         providers = (List)ActivityThread.AppBindData.providers.get(boundApp);
         if (providers != null && !providers.isEmpty()) {
            this.installContentProviders(app, providers);
         }
      }

      if (LoadedApk.mApplication != null) {
         boundApp = ContextImpl.mPackageInfo.get(context);
         if (boundApp != null) {
            LoadedApk.mApplication.set(boundApp, app);
         }
      }

      if (VERSION.SDK_INT >= 24 && StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm8jPzJsNygqLD0iJ2EzGlo=")).equals(processName)) {
         this.fixWeChatRecovery(this.mInitialApplication);
      }

      Throwable th;
      if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmUVGiZrER4bLj5SVg==")).equals(packageName)) {
         try {
            context.getSharedPreferences(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4uCGgFAiZiJR4sIz0MPGkgRStlNzAgKT5SVg==")), 0).edit().putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOD9sJzAsKBciLmkgAlo=")), false).putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOD9sJzAsKBciLmkgAh9oJwYeKQgEJ2YaGipsN1RF")), false).apply();
            context.getSharedPreferences(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YCG8zQT8=")), 0).edit().putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGo2GgVhHgo7LBcMHWkjMDdoNwIgLghSVg==")), false).apply();
         } catch (Throwable var20) {
            th = var20;
            th.printStackTrace();
         }
      }

      synchronized(this.mAllApplications) {
         this.mAllApplications.put(packageName, app);
      }

      if (!isTargetGame) {
         boundApp = ActivityThread.mBoundApplication.get(mainThread);
         providers = (List)ActivityThread.AppBindData.providers.get(boundApp);
         if (providers != null && !providers.isEmpty()) {
            VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhcMD2wjAixiASwpPT5SVg==")) + providers.size() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFJAJhVgJF")) + app);
            this.installContentProviders(app, providers);
         }
      }

      if (isInitialApp) {
         VirtualCore.get().getAppCallback().beforeApplicationCreate(packageName, processName, app);

         try {
            XposedHelpers.findAndHookMethod(Binder.class, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzJCRgHgY2KCwMMWkzSFo=")), new XC_MethodReplacement() {
               protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                  int ret = (Integer)XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
                  return NativeEngine.onGetCallingUid(ret);
               }
            });
         } catch (Throwable var18) {
            th = var18;
            VLog.i(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzJCRgHgY2KCwMMWk3TStsNAocKSlXVg==")) + th.getMessage());
            th.printStackTrace();
         }
      }

      if (cond != null) {
         cond.open();
      }

      try {
         this.mInstrumentation.callApplicationOnCreate(this.mInitialApplication);
         InvocationStubManager.getInstance().checkEnv(HCallbackStub.class);
         if (isInitialApp) {
            Application createdApp = (Application)ActivityThread.mInitialApplication.get(mainThread);
            if (createdApp != null) {
               this.mInitialApplication = createdApp;
            }
         }
      } catch (Exception var24) {
         Exception e = var24;
         if (!this.mInstrumentation.onException(app, e)) {
            throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcP2sjHitLEQo1PxcqKGkjQQZrDTwsKQc6KGMKNCRqHhoeKV9XVg==")) + data.appInfo.name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ODo6Vg==")) + e.toString(), e);
         }
      }

      if (!packageName.contains(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojRQV9ATg/KQMYMmUFLCw=")))) {
         LaunchCallBack launchDelegate = VirtualCore.get().getLaunchDelegate();
         VLog.i(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oxg+I2ojLCBlJCAoKhUuOW4FJFo=")));
         if (launchDelegate != null) {
            launchDelegate.onLaunch(packageName, VirtualCore.get().getHostPkg(), VirtualCore.get().getContext(), app);
         }
      }

      if (isInitialApp) {
         VirtualCore.get().getAppCallback().afterApplicationCreate(packageName, processName, app);
      }

      VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwQHDXpSHSNOClw3OgNWD38nPyN1DQEePF8HL04OQCNpJFkcORgiKWsaDQRsJx4bIBg2KGxTAiNuJywzKBc6D28zNCxONSg7Kj02BGozNANrAS8bIxgcLmIKND9jESAeLAgAIH0FGjBsEQI0DV5aJHwOTAN/IyM8MwQHDXpSHSNOCl1F")));
      VActivityManager.get().appDoneExecuting(info.packageName);
   }

   private void initDataStorage(boolean isExt, int userId, String pkg) {
      if (isExt) {
         FileUtils.ensureDirCreate(VEnvironment.getDataUserPackageDirectoryExt(userId, pkg));
         FileUtils.ensureDirCreate(VEnvironment.getDeDataUserPackageDirectoryExt(userId, pkg));
      } else {
         FileUtils.ensureDirCreate(VEnvironment.getDataUserPackageDirectory(userId, pkg));
         FileUtils.ensureDirCreate(VEnvironment.getDeDataUserPackageDirectory(userId, pkg));
      }

   }

   private void fixWeChatRecovery(Application app) {
      try {
         Field field = app.getClassLoader().loadClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41DmoVGillJCQgKS0XKmkwLCZsJzguLBc2Vg=="))).getField(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNDBmEVRF")));
         field.setAccessible(true);
         if (field.get((Object)null) != null) {
            return;
         }

         field.set((Object)null, app.getBaseContext());
      } catch (Throwable var3) {
         Throwable e = var3;
         e.printStackTrace();
      }

   }

   @SuppressLint({"NewApi"})
   private void fixSystem() {
      if (BuildCompat.isS()) {
         try {
            Reflect.on(Canvas.class).call(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMzGiNhHiAgKQcuMW8zLAZuDCQgKS02I2AgRVo=")), 26);
         } catch (Exception var2) {
         }
      }

      if (BuildCompat.isQ() && BuildCompat.isEMUI()) {
         XposedBridge.hookAllMethods(AutofillManager.class, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOD9uNAY/LCsMDmUzGgRrASxF")), new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
               AutoFillManagerStub.disableAutoFill(param.thisObject);
            }
         });
      }

      EmuiHelper.disableCache();
   }

   private void setupUncaughtHandler() {
      ThreadGroup root;
      for(root = Thread.currentThread().getThreadGroup(); root.getParent() != null; root = root.getParent()) {
      }

      ThreadGroup newRoot = new RootThreadGroup(root);
      if (VERSION.SDK_INT < 24) {
         List<ThreadGroup> groups = (List)mirror.java.lang.ThreadGroup.groups.get(root);
         synchronized(groups) {
            List<ThreadGroup> newGroups = new ArrayList(groups);
            newGroups.remove(newRoot);
            mirror.java.lang.ThreadGroup.groups.set(newRoot, newGroups);
            groups.clear();
            groups.add(newRoot);
            mirror.java.lang.ThreadGroup.groups.set(root, groups);
            Iterator var6 = newGroups.iterator();

            while(var6.hasNext()) {
               ThreadGroup group = (ThreadGroup)var6.next();
               if (group != newRoot) {
                  mirror.java.lang.ThreadGroup.parent.set(group, newRoot);
               }
            }
         }
      } else {
         ThreadGroup[] groups = (ThreadGroup[])ThreadGroupN.groups.get(root);
         synchronized(groups) {
            ThreadGroup[] newGroups = (ThreadGroup[])(groups).clone();
            ThreadGroupN.groups.set(newRoot, newGroups);
            ThreadGroupN.groups.set(root, new ThreadGroup[]{newRoot});
            ThreadGroup[] var15 = newGroups;
            int var16 = newGroups.length;

            for(int var8 = 0; var8 < var16; ++var8) {
               Object group = var15[var8];
               if (group != null && group != newRoot) {
                  ThreadGroupN.parent.set(group, newRoot);
               }
            }

            ThreadGroupN.ngroups.set(root, 1);
         }
      }

   }

   @SuppressLint({"SdCardPath"})
   private void mountVirtualFS(InstalledAppInfo info, boolean isExt) {
      String packageName = info.packageName;
      int userId = VUserHandle.myUserId();
      String dataDir;
      String de_dataDir;
      String libPath;
      if (isExt) {
         dataDir = VEnvironment.getDataUserPackageDirectoryExt(userId, packageName).getPath();
         de_dataDir = VEnvironment.getDeDataUserPackageDirectoryExtRoot(userId).getPath();
         libPath = VEnvironment.getDataAppLibDirectoryExt(packageName).getAbsolutePath();
      } else {
         dataDir = VEnvironment.getDataUserPackageDirectory(userId, packageName).getPath();
         de_dataDir = VEnvironment.getDeDataUserPackageDirectoryRoot(userId).getPath();
         libPath = VEnvironment.getDataAppLibDirectory(packageName).getAbsolutePath();
      }

      VDeviceConfig deviceConfig = this.getDeviceConfig();
      if (deviceConfig.enable) {
         File wifiMacAddressFile = this.getDeviceConfig().getWifiFile(userId, isExt);
         if (wifiMacAddressFile != null && wifiMacAddressFile.exists()) {
            String wifiMacAddressPath = wifiMacAddressFile.getPath();
            NativeEngine.redirectFile(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My02J283GilgHiApIylfDmkgASVvJwIsLCk5KX0KFi9lNAo8LAhSVg==")), wifiMacAddressPath);
            NativeEngine.redirectFile(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My02J283GilgHiApIylfDmkgASVrDiwZOQQAO2IaFjVuASw8")), wifiMacAddressPath);
            NativeEngine.redirectFile(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My02J283GilgHiApIylfDmkgASVvJx4tI18AO2IaFjVuASw8")), wifiMacAddressPath);
         }
      }

      this.forbidHost();
      String cache = (new File(dataDir, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+OWUFNFo=")))).getAbsolutePath();
      NativeEngine.redirectDirectory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My0qDW8JGlo=")), cache);
      NativeEngine.redirectDirectory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyViHiAgLwNfVg==")) + packageName, dataDir);
      int realUserId = VUserHandle.realUserId();
      NativeEngine.redirectDirectory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyVmASg/IzlfVg==")) + realUserId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + packageName, dataDir);
      if (VERSION.SDK_INT >= 24) {
         NativeEngine.redirectDirectory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyVmASg/IzxfPmknNFo=")) + realUserId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")), de_dataDir);
      }

      NativeEngine.whitelist(libPath);
      if (info.dynamic) {
         NativeEngine.whitelist(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyVmASg/IzlfVg==")) + realUserId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4ECWsnGlo=")));
      } else {
         NativeEngine.redirectDirectory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyViHiAgLwNfVg==")) + packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4ECWsnGlo=")), libPath);
         NativeEngine.redirectDirectory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyVmASg/IzlfVg==")) + realUserId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4ECWsnGlo=")), libPath);
      }

      File userLibDir = VEnvironment.getUserAppLibDirectory(userId, packageName);
      NativeEngine.redirectDirectory(userLibDir.getPath(), libPath);
      VirtualStorageManager vsManager = VirtualStorageManager.get();
      String vsPath = vsManager.getVirtualStorage(info.packageName, userId);
      boolean enable = vsManager.isVirtualStorageEnable(info.packageName, userId);
      if (enable && vsPath != null) {
         File vsDirectory = new File(vsPath);
         if (vsDirectory.exists() || vsDirectory.mkdirs()) {
            HashSet<String> mountPoints = this.getMountPoints();
            Iterator var17 = mountPoints.iterator();

            while(var17.hasNext()) {
               String mountPoint = (String)var17.next();
               NativeEngine.redirectDirectory(mountPoint, vsPath);
            }
         }
      } else {
         this.redirectSdcard(info);
      }

      if (!info.dynamic && (new File(info.getApkPath(isExt))).exists()) {
         NativeEngine.redirectFile(VEnvironment.getPackageFileStub(packageName), info.getApkPath(isExt));
         if (VERSION.SDK_INT == 27) {
            DexOverride override = new DexOverride(VEnvironment.getPackageFileStub(packageName), info.getApkPath(isExt), (String)null, (String)null);
            NativeEngine.addDexOverride(override);
         }
      }

      if (VirtualCore.getConfig().isEnableIORedirect()) {
         if (VirtualCore.getConfig().isDisableTinker(packageName)) {
            NativeEngine.forbid(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyViHiAgLwNfVg==")) + packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My0qCWojQSthMB5F")), false);
            NativeEngine.forbid(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyViHiAgLwNfVg==")) + packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My0qCWojQSthNR4pKAguLGkgRCU=")), false);
            NativeEngine.forbid(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyViHiAgLwNfVg==")) + packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My0qCWojQSthNR4gKAdXKn8FSFo=")), false);
            NativeEngine.forbid(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyVmASg/IzlfVg==")) + realUserId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My0qCWojQSthMB5F")), false);
            NativeEngine.forbid(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyVmASg/IzlfVg==")) + realUserId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My0qCWojQSthNR4pKAguLGkgRCU=")), false);
            NativeEngine.forbid(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyVmASg/IzlfVg==")) + realUserId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My0qCWojQSthNR4gKAdXKn8FSFo=")), false);
         }

         NativeEngine.enableIORedirect(info);
      }

   }

   private void forbidHost() {
      ActivityManager am = (ActivityManager)VirtualCore.get().getContext().getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZF")));
      Iterator var2 = am.getRunningAppProcesses().iterator();

      while(true) {
         ActivityManager.RunningAppProcessInfo info;
         do {
            do {
               do {
                  do {
                     if (!var2.hasNext()) {
                        return;
                     }

                     info = (ActivityManager.RunningAppProcessInfo)var2.next();
                  } while(info.pid == Process.myPid());
               } while(info.uid != VirtualCore.get().myUid());
            } while(VActivityManager.get().isAppPid(info.pid));
         } while(!info.processName.startsWith(StubManifest.PACKAGE_NAME) && (StubManifest.EXT_PACKAGE_NAME == null || !info.processName.startsWith(StubManifest.EXT_PACKAGE_NAME)));

         NativeEngine.forbid(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My06KmozLyU=")) + info.pid + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4IP28KLFo=")), false);
         NativeEngine.forbid(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My06KmozLyU=")) + info.pid + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My42DWgFHi9gNDBF")), false);
      }
   }

   @SuppressLint({"SdCardPath"})
   private HashSet<String> getMountPoints() {
      HashSet<String> mountPoints = new HashSet(3);
      mountPoints.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4ICGwJGgNiHig7Iz01DQ==")));
      mountPoints.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My02PGszJARiVx5F")));
      mountPoints.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My02LGowFjdiJDM1KAdXLW8zQQZrAS8c")) + VUserHandle.realUserId() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
      mountPoints.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qD28jJC1iCh4/KggMCG4gBitrVgZF")) + VUserHandle.realUserId() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
      String[] points = StorageManagerCompat.getAllPoints(VirtualCore.get().getContext());
      if (points != null) {
         String[] var3 = points;
         int var4 = points.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String point = var3[var5];
            if (point.endsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")))) {
               mountPoints.add(point);
            } else {
               mountPoints.add(point + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
            }
         }
      }

      return mountPoints;
   }

   private Context createPackageContext(String packageName) {
      try {
         Context hostContext = VirtualCore.get().getContext();
         Context packageContext = hostContext.createPackageContext(packageName, 3);
         PackageManager packageManager = packageContext.getPackageManager();
         VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhZfD28wMBNgJFkgKAgALn40IFo=")) + hostContext + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OGszGiZmHjAaLBccD2ozOzI=")) + packageContext + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsKIDd9JA47KC0MQG4jPCt+N1RF")) + packageName);
         return packageContext;
      } catch (PackageManager.NameNotFoundException var5) {
         PackageManager.NameNotFoundException e = var5;
         e.printStackTrace();
         VirtualRuntime.crash(e);
         throw new RuntimeException();
      }
   }

   private void installContentProviders(Context app, List<ProviderInfo> providers) {
      long origId = Binder.clearCallingIdentity();
      Object mainThread = VirtualCore.mainThread();

      try {
         Iterator var6 = providers.iterator();

         while(var6.hasNext()) {
            ProviderInfo cpi = (ProviderInfo)var6.next();

            try {
               ActivityThread.installProvider(mainThread, app, cpi, (Object)null);
            } catch (Throwable var12) {
               Throwable e = var12;
               e.printStackTrace();
            }
         }
      } finally {
         Binder.restoreCallingIdentity(origId);
      }

   }

   public IBinder acquireProviderClient(ProviderInfo info) {
      this.bindApplication(info.packageName, info.processName);
      IInterface provider = null;
      String[] authorities = info.authority.split(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OC5SVg==")));
      String authority = authorities.length == 0 ? info.authority : authorities[0];
      ContentResolver resolver = VirtualCore.get().getContext().getContentResolver();
      ContentProviderClient client = null;

      try {
         client = resolver.acquireUnstableContentProviderClient(authority);
      } catch (Throwable var8) {
         Throwable e = var8;
         e.printStackTrace();
      }

      if (client != null) {
         provider = (IInterface)mirror.android.content.ContentProviderClient.mContentProvider.get(client);
         client.release();
      }

      VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2L2wVAgRiDyQqKi4+MWkzGgRgJwIaLhgcCksVSFo=")) + info + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhcMM28wNCRmVgU8")) + provider + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phc6KmozLCthJysiPxhSVg==")) + VirtualRuntime.getProcessName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFJAVmHho1Iz0cLmgkIFo=")) + authority);
      return provider != null ? provider.asBinder() : null;
   }

   private void fixInstalledProviders() {
      this.clearSettingProvider();
      Map<Object, Object> clientMap = (Map)ActivityThread.mProviderMap.get(VirtualCore.mainThread());
      Iterator var2 = clientMap.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry<Object, Object> e = (Map.Entry)var2.next();
         Object clientRecord = e.getValue();
         IInterface provider;
         Object holder;
         ProviderInfo info;
         if (BuildCompat.isOreo()) {
            provider = (IInterface)ActivityThread.ProviderClientRecordJB.mProvider.get(clientRecord);
            holder = ActivityThread.ProviderClientRecordJB.mHolder.get(clientRecord);
            if (holder != null) {
               info = (ProviderInfo)ContentProviderHolderOreo.info.get(holder);
               if (!info.authority.startsWith(StubManifest.STUB_CP_AUTHORITY)) {
                  provider = ProviderHook.createProxy(true, info.authority, provider);
                  ActivityThread.ProviderClientRecordJB.mProvider.set(clientRecord, provider);
                  ContentProviderHolderOreo.provider.set(holder, provider);
               }
            }
         } else {
            provider = (IInterface)ActivityThread.ProviderClientRecordJB.mProvider.get(clientRecord);
            holder = ActivityThread.ProviderClientRecordJB.mHolder.get(clientRecord);
            if (holder != null) {
               info = (ProviderInfo)IActivityManager.ContentProviderHolder.info.get(holder);
               if (!info.authority.startsWith(StubManifest.STUB_CP_AUTHORITY)) {
                  provider = ProviderHook.createProxy(true, info.authority, provider);
                  ActivityThread.ProviderClientRecordJB.mProvider.set(clientRecord, provider);
                  IActivityManager.ContentProviderHolder.provider.set(holder, provider);
               }
            }
         }
      }

   }

   private void clearSettingProvider() {
      Object cache = Settings.System.sNameValueCache.get();
      if (cache != null) {
         clearContentProvider(cache);
      }

      cache = Settings.Secure.sNameValueCache.get();
      if (cache != null) {
         clearContentProvider(cache);
      }

      if (Settings.Global.TYPE != null) {
         cache = Settings.Global.sNameValueCache.get();
         if (cache != null) {
            clearContentProvider(cache);
         }
      }

   }

   private static void clearContentProvider(Object cache) {
      if (BuildCompat.isOreo()) {
         Object holder = Settings.NameValueCacheOreo.mProviderHolder.get(cache);
         if (holder != null) {
            Settings.ContentProviderHolder.mContentProvider.set(holder, null);
         }
      } else {
         Settings.NameValueCache.mContentProvider.set(cache, (Object)null);
      }

   }

   public void finishActivity(IBinder token) {
      this.sendMessage(13, token);
   }

   public void scheduleNewIntent(String creator, IBinder token, Intent intent) {
      NewIntentData data = new NewIntentData();
      data.creator = creator;
      data.token = token;
      data.intent = intent;
      this.sendMessage(11, data);
   }

   public void scheduleReceiver(String processName, ComponentName component, Intent intent, BroadcastReceiver.PendingResult pendingResult) {
      ReceiverData receiverData = new ReceiverData();
      receiverData.pendingResult = pendingResult;
      receiverData.intent = intent;
      receiverData.component = component;
      receiverData.processName = processName;
      receiverData.stacktrace = new Exception();
      this.sendMessage(12, receiverData);
   }

   private void handleReceiver(ReceiverData data) {
      BroadcastReceiver.PendingResult result = data.pendingResult;

      try {
         Context context = this.mInitialApplication.getBaseContext();
         Context receiverContext = (Context)ContextImpl.getReceiverRestrictedContext.call(context);
         ContextFixer.fixContext(receiverContext, data.component.getPackageName());
         String className = data.component.getClassName();
         ClassLoader classLoader = (ClassLoader)LoadedApk.getClassLoader.call(this.mBoundApplication.info);
         BroadcastReceiver receiver = (BroadcastReceiver)classLoader.loadClass(className).newInstance();
         mirror.android.content.BroadcastReceiver.setPendingResult.call(receiver, result);
         data.intent.setExtrasClassLoader(context.getClassLoader());
         ComponentUtils.unpackFillIn(data.intent, classLoader);
         if (data.intent.getComponent() == null) {
            data.intent.setComponent(data.component);
         }

         FakeIdentityBinder.setSystemIdentity();
         receiver.onReceive(receiverContext, data.intent);
         if (mirror.android.content.BroadcastReceiver.getPendingResult.call(receiver) != null) {
            IBinder token = (IBinder)mirror.android.content.BroadcastReceiver.PendingResult.mToken.get(result);
            if (!VActivityManager.get().broadcastFinish(token)) {
               result.finish();
            }
         }

      } catch (Exception var9) {
         Exception e = var9;
         data.stacktrace.printStackTrace();
         throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcP2sjHitLEQo1PxgqLm4gRQZ4HgogLT4uI2YwLDV5EVRF")) + data.component + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ODo6Vg==")) + e.toString(), e);
      }
   }

   public ClassLoader getClassLoader() {
      return (ClassLoader)LoadedApk.getClassLoader.call(this.mBoundApplication.info);
   }

   public Service createService(ServiceInfo info, IBinder token) {
      this.bindApplication(info.packageName, info.processName);
      ClassLoader classLoader = (ClassLoader)LoadedApk.getClassLoader.call(this.mBoundApplication.info);

      Service service;
      Exception e;
      try {
         service = (Service)classLoader.loadClass(info.name).newInstance();
      } catch (Exception var7) {
         e = var7;
         throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcP2sjHitLEQo1PxccDmoKBjdlNCwaLRcqJ0sVNCBlNzgiKAgfJA==")) + info.name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ODo6Vg==")) + e.toString(), e);
      }

      try {
         Context context = VirtualCore.get().getContext().createPackageContext(info.packageName, 3);
         ContextImpl.setOuterContext.call(context, service);
         mirror.android.app.Service.attach.call(service, context, VirtualCore.mainThread(), info.name, token, this.mInitialApplication, ActivityManagerNative.getDefault.call());
         ContextFixer.fixContext(context, info.packageName);
         service.onCreate();
         return service;
      } catch (Exception var6) {
         e = var6;
         throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcP2sjHitLEQo1PxcqKGkjQQZrDTw6LhcMMmMKNCB5EVRF")) + info.name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ODo6Vg==")) + e.toString(), e);
      }
   }

   public IBinder createProxyService(ComponentName component, IBinder binder) {
      return ProxyServiceFactory.getProxyService(this.getCurrentApplication(), component, binder);
   }

   public String getDebugInfo() {
      return VirtualRuntime.getProcessName();
   }

   public boolean finishReceiver(IBinder token) {
      return StaticReceiverSystem.get().broadcastFinish(token);
   }

   public List<ActivityManager.RunningServiceInfo> getServices() {
      return VServiceRuntime.getInstance().getServices();
   }

   private void redirectSdcardAndroidData(InstalledAppInfo info) {
      SettingConfig config = VirtualCore.getConfig();
      HashSet<String> mountPoints = this.getMountPoints();
      String[] dirs = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Myw+CGgKFiVjDg01KBciLm4nNFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Myw+CGgKFiVjDg01KgcMPmwjQCU=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Myw+CGgKFiVjDg01Ki0uOH8FSFo="))};
      File replace = VirtualCore.get().getContext().getExternalFilesDir(config.getVirtualSdcardAndroidDataName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + VUserHandle.myUserId() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
      if (info.packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojBitnHh42Oj0AMWU0RVo="))) && StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+DW8wNCZiJ1RF")).equals(Build.BRAND) && VERSION.SDK_INT == 29) {
         VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Bz8nL0YWWgBLHig1KgMYDmkgFiVlMxoZIxcpCEtXLQUcUz1MXgAdEh8VJARoDgo6JQcLL3UJPzZHAg8TBhojREZaA1dYEzkBAhk3IEcXPQJAECUbBVcJWU5XAwQUFh9LXjYdDhQ7D1YGUi05EzUNEQISLRYaPwtNWDYVOhoNPTUVPAswXx87QBknTVo=")) + info.packageName);
      } else if (info.packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojBitnHh42Oj0MKA=="))) && StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+DW8wNCZiJ1RF")).equals(Build.BRAND) && VERSION.SDK_INT == 29) {
         VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Bz8nL0YWWgBLHig1KgMYDmkgFiVlMxogKSo4IhhaACEYSVsuUgwMO2UzMCZqNx4zDV8lP3lXFy4ZUgsIRAArCBorMUxXFgsTWjU3QB87F1IePxMpXAQCLBktBCwbNl8uWQoCIxQtHD8fNkI0EUQkLhQEFD0CFS4wWwldPFQBACsGSV8+XwkbOg==")) + info.packageName);
      } else {
         if (!replace.exists() && !replace.mkdirs()) {
            VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4+CWoFNCxLEQo1PxcqKGkjQQZrDTwvIxcLPksVSFo=")) + replace);
         }

         Iterator var6 = mountPoints.iterator();

         while(var6.hasNext()) {
            String mountPoint = (String)var6.next();
            String[] var8 = dirs;
            int var9 = dirs.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               String dir = var8[var10];
               File origin = new File(mountPoint + dir);
               File target = new File(replace.getPath() + dir);
               if (!target.exists() && !target.mkdirs()) {
                  VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4+CWoFNCxLEQo1PxcqKGkjQQZrDTwvIxcLPksVSFo=")) + target);
               }

               NativeEngine.redirectDirectory(origin.getPath(), replace.getPath() + dir);
            }
         }

      }
   }

   private void redirectSdcard(InstalledAppInfo info) {
      SettingConfig config = VirtualCore.getConfig();
      this.redirectSdcardAndroidData(info);
      if (BuildCompat.isR() && VirtualCore.get().getTargetSdkVersion() >= 30) {
         int userId = VUserHandle.myUserId();
         ApplicationInfo applicationInfo = info.getApplicationInfo(userId);
         if (applicationInfo == null) {
            return;
         }

         int targetSdkVersion = applicationInfo.targetSdkVersion;
         if (targetSdkVersion < 30) {
            HashSet<String> mountPoints = this.getMountPoints();
            File replace = VirtualCore.get().getContext().getExternalFilesDir(config.getVirtualSdcardAndroidDataName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + VUserHandle.myUserId() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
            if (VirtualCore.get().isSharedUserId()) {
               replace = new File(replace.toString().replace(StubManifest.EXT_PACKAGE_NAME, StubManifest.PACKAGE_NAME));
            }

            if (!replace.exists() && !replace.mkdirs()) {
               VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4+CWoFNCxLEQo1PxcqKGkjQQZrDTwvIxcLPksVSFo=")) + replace);
            }

            Iterator var8 = mountPoints.iterator();

            String mountPoint;
            while(var8.hasNext()) {
               mountPoint = (String)var8.next();
               File origin = new File(mountPoint + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
               NativeEngine.redirectDirectory(origin.getPath(), replace.getPath());
            }

            var8 = mountPoints.iterator();

            while(var8.hasNext()) {
               mountPoint = (String)var8.next();

               try {
                  String[] standardDirectories = (String[])Reflect.on(Environment.class).field(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IisqEWIhMBFpMgpAIBUcAmEhAlF9IgpPJhU2Vg=="))).get();
                  String[] var11 = standardDirectories;
                  int var12 = standardDirectories.length;

                  for(int var13 = 0; var13 < var12; ++var13) {
                     String directory = var11[var13];
                     String standardPath = NativeEngine.pathCat(mountPoint, directory);
                     NativeEngine.whitelist(standardPath);
                  }
               } catch (Exception var16) {
                  var16.printStackTrace();
               }
            }
         }
      }

   }

   @SuppressLint({"HandlerLeak"})
   private class H extends Handler {
      private H() {
         super(Looper.getMainLooper());
      }

      public void handleMessage(Message msg) {
         switch (msg.what) {
            case 11:
               VClient.this.handleNewIntent((NewIntentData)msg.obj);
               break;
            case 12:
               VClient.this.handleReceiver((ReceiverData)msg.obj);
               break;
            case 13:
               VActivityManager.get().finishActivity((IBinder)msg.obj);
         }

      }

      // $FF: synthetic method
      H(Object x1) {
         this();
      }
   }

   private static final class ReceiverData {
      BroadcastReceiver.PendingResult pendingResult;
      Intent intent;
      ComponentName component;
      String processName;
      Throwable stacktrace;

      private ReceiverData() {
      }

      // $FF: synthetic method
      ReceiverData(Object x0) {
         this();
      }
   }

   private static final class AppBindData {
      String processName;
      ApplicationInfo appInfo;
      List<ProviderInfo> providers;
      Object info;

      private AppBindData() {
      }

      // $FF: synthetic method
      AppBindData(Object x0) {
         this();
      }
   }

   private static final class NewIntentData {
      String creator;
      IBinder token;
      Intent intent;

      private NewIntentData() {
      }

      // $FF: synthetic method
      NewIntentData(Object x0) {
         this();
      }
   }

   private static class RootThreadGroup extends ThreadGroup {
      RootThreadGroup(ThreadGroup parent) {
         super(parent, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITw+Vg==")));
      }

      public void uncaughtException(Thread t, Throwable e) {
         CrashHandler handler = VClient.gClient.crashHandler;
         if (handler != null) {
            handler.handleUncaughtException(t, e);
         } else {
            VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcOWsaNC1jEQpF")), e);
         }

      }
   }
}
