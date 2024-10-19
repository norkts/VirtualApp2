package com.lody.virtual.client.core;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.os.Build.VERSION;
import com.kook.librelease.R.string;
import com.lody.virtual.PineXposed;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.NativeEngine;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.env.Constants;
import com.lody.virtual.client.env.HostPackageManager;
import com.lody.virtual.client.env.LocalPackageCache;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.hook.delegate.TaskDescriptionDelegate;
import com.lody.virtual.client.ipc.LocalProxyUtils;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.BitmapUtils;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.remote.VAppInstallerParams;
import com.lody.virtual.remote.VAppInstallerResult;
import com.lody.virtual.server.BinderProvider;
import com.lody.virtual.server.extension.VExtPackageAccessor;
import com.lody.virtual.server.interfaces.IAppManager;
import com.lody.virtual.server.interfaces.IPackageObserver;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mirror.android.app.ActivityThread;

public final class VirtualCore {
   public static final int GET_HIDDEN_APP = 1;
   private static final String TAG = VirtualCore.class.getSimpleName();
   @SuppressLint({"StaticFieldLeak"})
   private static VirtualCore gCore = new VirtualCore();
   private final int myUid = Process.myUid();
   private int remoteUid = -1;
   private HostPackageManager hostPackageManager;
   private String hostPkgName;
   private Object mainThread;
   private Context context;
   private String mainProcessName;
   private String processName;
   private ProcessType processType;
   private boolean isMainPackage;
   private IAppManager mService;
   private boolean isStartUp;
   private PackageInfo mHostPkgInfo;
   private ConditionVariable mInitLock;
   private AppCallback mAppCallback;
   private TaskDescriptionDelegate mTaskDescriptionDelegate;
   private SettingConfig mConfig;
   private AppRequestListener mAppRequestListener;
   private LaunchCallBack launchDelegate;
   private Handler mHandlerASyc;
   private final BroadcastReceiver mDownloadCompleteReceiver = new BroadcastReceiver() {
      public void onReceive(Context context, Intent intent) {
         VLog.w(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRgALWojHiV9DgoNLwcYOWkFGgQ=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uOWgVAj5iCiQwKi46Dm8zNDdrVjwqLD4IDmAaLD9uDjMpKBdfKWsVLDNvNC8tDRhSVg==")) + intent);
         if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42FhRiIh5AIQZbGmEhLF5iHDwRLAYMBg==")).equals(intent.getAction())) {
            VActivityManager.get().handleDownloadCompleteIntent(intent);
         }

      }
   };
   boolean scanned = false;

   private VirtualCore() {
      HandlerThread handlerThread = new HandlerThread(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwZfP2ojMCRiASwROy4cPw==")));
      handlerThread.start();
      this.mHandlerASyc = new Handler(handlerThread.getLooper());
   }

   public Handler getHandlerASyc() {
      return this.mHandlerASyc;
   }

   public static SettingConfig getConfig() {
      return get().mConfig;
   }

   public static void setConfig(SettingConfig config) {
      get().mConfig = config;
   }

   public static VirtualCore get() {
      return gCore;
   }

   public static PackageManager getPM() {
      return get().getPackageManager();
   }

   public static Object mainThread() {
      return get().mainThread;
   }

   public ConditionVariable getInitLock() {
      return this.mInitLock;
   }

   public int myUid() {
      return this.myUid;
   }

   public int remoteUid() {
      return this.remoteUid;
   }

   public int myUserId() {
      return VUserHandle.getUserId(this.myUid);
   }

   public AppCallback getAppCallback() {
      return this.mAppCallback == null ? AppCallback.EMPTY : this.mAppCallback;
   }

   public void setAppCallback(AppCallback callback) {
      this.mAppCallback = callback;
   }

   public void setCrashHandler(CrashHandler handler) {
      VClient.get().setCrashHandler(handler);
   }

   public TaskDescriptionDelegate getTaskDescriptionDelegate() {
      return this.mTaskDescriptionDelegate;
   }

   public void setTaskDescriptionDelegate(TaskDescriptionDelegate mTaskDescriptionDelegate) {
      this.mTaskDescriptionDelegate = mTaskDescriptionDelegate;
   }

   public LaunchCallBack getLaunchDelegate() {
      return this.launchDelegate;
   }

   public void setLaunchDelegate(LaunchCallBack launchDelegate) {
      this.launchDelegate = launchDelegate;
   }

   public int[] getGids() {
      return this.mHostPkgInfo.gids;
   }

   public ApplicationInfo getHostApplicationInfo() {
      return this.mHostPkgInfo.applicationInfo;
   }

   public Context getContext() {
      return this.context;
   }

   public PackageManager getPackageManager() {
      return this.context.getPackageManager();
   }

   public boolean isSystemApp() {
      ApplicationInfo applicationInfo = this.getContext().getApplicationInfo();
      return (applicationInfo.flags & 1) != 0 || (applicationInfo.flags & 128) != 0;
   }

   public String getHostPkg() {
      return this.hostPkgName;
   }

   public int getTargetSdkVersion() {
      return this.context.getApplicationInfo().targetSdkVersion;
   }

   public HostPackageManager getHostPackageManager() {
      return this.hostPackageManager;
   }

   public boolean checkSelfPermission(String permission, boolean isExt) {
      if (isExt) {
         return 0 == this.hostPackageManager.checkPermission(permission, StubManifest.EXT_PACKAGE_NAME);
      } else {
         return 0 == this.hostPackageManager.checkPermission(permission, StubManifest.PACKAGE_NAME);
      }
   }

   public void waitStartup() {
      if (Looper.myLooper() != Looper.getMainLooper()) {
         if (this.mInitLock != null) {
            this.mInitLock.block();
         }

      }
   }

   public int getUidForSharedUser(String sharedUserName) {
      try {
         return this.getService().getUidForSharedUser(sharedUserName);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public VAppInstallerResult installPackage(Uri uri, VAppInstallerParams params) {
      try {
         return this.getService().installPackage(uri, params);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (VAppInstallerResult)VirtualRuntime.crash(e);
      }
   }

   public void startup(Application application, Context context, SettingConfig config) throws Throwable {
      if (!this.isStartUp) {
         if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IT4YKmwKNDdgHCg1Iz0LDmoKBjdsNCwwKQReI0saQTBlJzMpKAhbKGUVNDB7AR45DRdbKG8KDTZlNwobLQg+PHojSFo=")));
         }

         if (!context.getPackageName().equals(config.getMainPackageName()) && !context.getPackageName().equals(config.getExtPackageName())) {
            throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz4uCWwFRSthMCQgKRcLOm8jQS9lMzw7LRg2LX0KJCB5Hh4eLF9XCmoVNzRoHlkZJAdfOm8KRQJ+NFEoLi5bP2gzNyRLEQY1LAMmL2kjGiN4HiwcPQhfO2YwLyNpJFkdLxg2IW8KFj9oDTwZJRc1L2ojNAFvEQE3Khg+OWUzJC1iCiQ2LwdXPX83TStuHjwgLT0qJ2JTOFo=")) + config.getMainPackageName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgAKnsFSFo=")) + config.getExtPackageName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186OmwaMyhiJB4gPxhSVg==")) + context.getPackageName());
         }

         this.mInitLock = new ConditionVariable();
         this.mConfig = config;
         String packageName = config.getMainPackageName();
         String ext_packageName = config.getExtPackageName();
         Constants.ACTION_SHORTCUT = packageName + Constants.ACTION_SHORTCUT;
         Constants.ACTION_BADGER_CHANGE = packageName + Constants.ACTION_BADGER_CHANGE;
         StubManifest.STUB_CP_AUTHORITY = packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz0iCW8gMAV9DlFAIy42LW4YNFo="));
         StubManifest.PROXY_CP_AUTHORITY = packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz06KmowOC9iHjAqJi4mKG8KFj8="));
         File externalFilesDir = context.getExternalFilesDir(config.getVirtualSdcardAndroidDataName());
         if (!externalFilesDir.exists()) {
            externalFilesDir.mkdirs();
         }

         if (ext_packageName == null) {
            ext_packageName = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OzwAH2AYRVE="));
         }

         StubManifest.PACKAGE_NAME = packageName;
         StubManifest.EXT_PACKAGE_NAME = ext_packageName;
         StubManifest.EXT_STUB_CP_AUTHORITY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcMmMFMD9qDiQbJQcYCm8FFhNoHlkZKi5SVg=="));
         StubManifest.EXT_PROXY_CP_AUTHORITY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcDmEwAjFvDjAuLBUuDmwzGixpHAY0IRgMVg=="));
         this.context = context;
         this.isMainPackage = context.getPackageName().equals(StubManifest.PACKAGE_NAME);
         NativeEngine.bypassHiddenAPIEnforcementPolicyIfNeeded();
         this.hostPackageManager = HostPackageManager.init();
         this.mHostPkgInfo = this.hostPackageManager.getPackageInfo(packageName, 256L);
         this.detectProcessType();
         if (this.isVAppProcess()) {
            this.mainThread = ActivityThread.currentActivityThread.call();
            if (this.mainThread != null && BuildCompat.isT()) {
               VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGoFAil9AQozKi0YEW8FMAZrDlk/PQNXVg==")) + application);
               ActivityThread.mInitialApplication.set(this.mainThread, application);
            }

            LocalPackageCache.init();
         }

         ApplicationInfo info;
         if (this.isExtPackage()) {
            try {
               info = this.getHostPackageManager().getApplicationInfo(packageName, 0L);
               if (info != null) {
                  this.remoteUid = info.uid;
               }
            } catch (PackageManager.NameNotFoundException var11) {
            }
         } else {
            try {
               info = this.getHostPackageManager().getApplicationInfo(ext_packageName, 0L);
               if (info != null) {
                  this.remoteUid = info.uid;
               }
            } catch (PackageManager.NameNotFoundException var10) {
            }
         }

         if (this.isVAppProcess() || this.isExtHelperProcess()) {
            ServiceManagerNative.linkToDeath(new IBinder.DeathRecipient() {
               public void binderDied() {
                  VLog.e(VirtualCore.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKmwjNARLETg7IykmPmkjQSx1VjwiIxgEKEsVODVsJCwuLAcXPngVSFo=")) + VirtualCore.this.processType.name());
                  Process.killProcess(Process.myPid());
               }
            });
         }

         if (this.isServerProcess() || this.isExtHelperProcess()) {
            VLog.w(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRgALWojHiV9DgoNLwcYOWkFGgQ=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OxgYKWwFNCZjDlk9PxU2DWUFMCRlJzgvJBg+Kn0KJCBlMCAqKAccI2UjATR7AR45DRgiOWwgMDFqDhEtPhhSVg==")) + this.processType);
            IntentFilter filter = new IntentFilter(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42FhRiIh5AIQZbGmEhLF5iHDwRLAYMBg==")));

            try {
               context.registerReceiver(this.mDownloadCompleteReceiver, filter);
            } catch (Throwable var9) {
                var9.printStackTrace();
            }
         }

         InvocationStubManager invocationStubManager = InvocationStubManager.getInstance();
         invocationStubManager.init();
         invocationStubManager.injectAll();
         this.isStartUp = true;
         this.mInitLock.open();
      }

   }

   public void waitForEngine() {
      ServiceManagerNative.ensureServerStarted();
   }

   public boolean isEngineLaunched() {
      if (this.isExtPackage()) {
         return true;
      } else {
         if (!BinderProvider.scanApps) {
            this.scanApps();
         }

         ActivityManager am = (ActivityManager)this.context.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZF")));
         String engineProcessName = this.getEngineProcessName();
         Iterator var3 = am.getRunningAppProcesses().iterator();

         ActivityManager.RunningAppProcessInfo info;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            info = (ActivityManager.RunningAppProcessInfo)var3.next();
         } while(!info.processName.endsWith(engineProcessName));

         return true;
      }
   }

   public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcessesEx() {
      ActivityManager am = (ActivityManager)this.context.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZF")));
      List<ActivityManager.RunningAppProcessInfo> list = new ArrayList(am.getRunningAppProcesses());
      if (!get().isSharedUserId()) {
         List<ActivityManager.RunningAppProcessInfo> list64 = VExtPackageAccessor.getRunningAppProcesses();
         list.addAll(list64);
      }

      return list;
   }

   public ActivityManager.RunningAppProcessInfo getProccessInfo(String processName, int uid) {
      List<ActivityManager.RunningAppProcessInfo> runningAppProcessesEx = this.getRunningAppProcessesEx();
      Iterator var4 = runningAppProcessesEx.iterator();

      ActivityManager.RunningAppProcessInfo info;
      do {
         if (!var4.hasNext()) {
            return null;
         }

         info = (ActivityManager.RunningAppProcessInfo)var4.next();
      } while(!info.processName.equals(processName) || info.uid != uid);

      return info;
   }

   public List<ActivityManager.RunningTaskInfo> getRunningTasksEx(int maxNum) {
      ActivityManager am = (ActivityManager)this.context.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZF")));
      List<ActivityManager.RunningTaskInfo> list = new ArrayList(am.getRunningTasks(maxNum));
      if (!get().isSharedUserId()) {
         List<ActivityManager.RunningTaskInfo> list64 = VExtPackageAccessor.getRunningTasks(maxNum);
         list.addAll(list64);
      }

      return list;
   }

   public List<ActivityManager.RecentTaskInfo> getRecentTasksEx(int maxNum, int flags) {
      ActivityManager am = (ActivityManager)this.context.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZF")));
      List<ActivityManager.RecentTaskInfo> list = new ArrayList(am.getRecentTasks(maxNum, flags));
      if (!get().isSharedUserId()) {
         List<ActivityManager.RecentTaskInfo> list64 = VExtPackageAccessor.getRecentTasks(maxNum, flags);
         list.addAll(list64);
      }

      return list;
   }

   public String getEngineProcessName() {
      return this.context.getString(string.server_process_name);
   }

   public void initialize(VirtualInitializer initializer) {
      if (initializer == null) {
         throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcCWwFAjdgHgYiKAgtOnsnTQBnDwJT")));
      } else {
         switch (this.processType) {
            case Main:
               initializer.onMainProcess();
               break;
            case VAppClient:
               if (VERSION.SDK_INT >= 21) {
                  PineXposed.init();
               }

               initializer.onVirtualProcess();
               break;
            case Server:
               initializer.onServerProcess();
               break;
            case CHILD:
               initializer.onChildProcess();
         }

      }
   }

   private static String getProcessName(Context context) {
      int pid = Process.myPid();
      String processName = null;
      ActivityManager am = (ActivityManager)context.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZF")));
      Iterator var4 = am.getRunningAppProcesses().iterator();

      while(var4.hasNext()) {
         ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)var4.next();
         if (info.pid == pid) {
            processName = info.processName;
            break;
         }
      }

      if (processName == null) {
         throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhcMD2szNANhIlk7KgcLOnsnTSZvAQId")));
      } else {
         return processName;
      }
   }

   private void detectProcessType() {
      this.hostPkgName = this.context.getApplicationInfo().packageName;
      this.mainProcessName = this.context.getApplicationInfo().processName;
      this.processName = getProcessName(this.context);
      if (this.processName.equals(this.mainProcessName)) {
         this.processType = VirtualCore.ProcessType.Main;
      } else if (this.processName.endsWith(Constants.SERVER_PROCESS_NAME)) {
         this.processType = VirtualCore.ProcessType.Server;
      } else if (this.processName.endsWith(Constants.HELPER_PROCESS_NAME)) {
         this.processType = VirtualCore.ProcessType.Helper;
      } else if (VActivityManager.get().isAppProcess(this.processName)) {
         this.processType = VirtualCore.ProcessType.VAppClient;
      } else {
         this.processType = VirtualCore.ProcessType.CHILD;
      }

   }

   private IAppManager getService() {
      if (!IInterfaceUtils.isAlive(this.mService)) {
         synchronized(this) {
            Object remote = this.getStubInterface();
            this.mService = (IAppManager)LocalProxyUtils.genProxy(IAppManager.class, remote);
         }
      }

      return this.mService;
   }

   private Object getStubInterface() {
      return IAppManager.Stub.asInterface(ServiceManagerNative.getService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KA=="))));
   }

   public boolean isVAppProcess() {
      return VirtualCore.ProcessType.VAppClient == this.processType;
   }

   public boolean isExtHelperProcess() {
      return VirtualCore.ProcessType.Helper == this.processType;
   }

   public boolean isMainProcess() {
      return VirtualCore.ProcessType.Main == this.processType;
   }

   public boolean isMainPackage() {
      return this.isMainPackage;
   }

   public boolean isExtPackage() {
      return !this.isMainPackage;
   }

   public boolean isChildProcess() {
      return VirtualCore.ProcessType.CHILD == this.processType;
   }

   public boolean isServerProcess() {
      return VirtualCore.ProcessType.Server == this.processType;
   }

   public String getProcessName() {
      return this.processName;
   }

   public String getMainProcessName() {
      return this.mainProcessName;
   }

   public boolean isAppRunning(String packageName, int userId, boolean foreground) {
      return VActivityManager.get().isAppRunning(packageName, userId, foreground);
   }

   public boolean isAppInstalled(String pkg) {
      try {
         return this.getService().isAppInstalled(pkg);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public boolean isPackageLaunchable(String packageName) {
      InstalledAppInfo info = this.getInstalledAppInfo(packageName, 0);
      return info != null && this.getLaunchIntent(packageName, info.getInstalledUsers()[0]) != null;
   }

   public Intent getLaunchIntent(String packageName, int userId) {
      VPackageManager pm = VPackageManager.get();
      Intent intentToResolve = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42QQ5nDB5F")));
      intentToResolve.addCategory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoJzg/LhgmKWEzBSlnDB5KIQhSVg==")));
      intentToResolve.setPackage(packageName);
      List<ResolveInfo> ris = pm.queryIntentActivities(intentToResolve, intentToResolve.resolveType(this.context), 0, userId);
      if (ris == null || ris.size() <= 0) {
         intentToResolve.removeCategory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoJzg/LhgmKWEzBSlnDB5KIQhSVg==")));
         intentToResolve.addCategory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoJzg/LhgmKWEzBSlkHCQUIRYYBmMIFlo=")));
         intentToResolve.setPackage(packageName);
         ris = pm.queryIntentActivities(intentToResolve, intentToResolve.resolveType(this.context), 0, userId);
      }

      if (ris != null && ris.size() > 0) {
         Intent intent = new Intent(intentToResolve);
         intent.setFlags(268435456);
         intent.setClassName(((ResolveInfo)ris.get(0)).activityInfo.packageName, ((ResolveInfo)ris.get(0)).activityInfo.name);
         return intent;
      } else {
         return null;
      }
   }

   public boolean createShortcut(int userId, String packageName, OnEmitShortcutListener listener) {
      return this.createShortcut(userId, packageName, (Intent)null, listener);
   }

   public boolean createShortcut(int userId, String packageName, Intent splash, OnEmitShortcutListener listener) {
      InstalledAppInfo setting = this.getInstalledAppInfo(packageName, 0);
      if (setting == null) {
         return false;
      } else {
         ApplicationInfo appInfo = setting.getApplicationInfo(userId);
         PackageManager pm = this.context.getPackageManager();

         String name;
         Bitmap icon;
         try {
            CharSequence sequence = appInfo.loadLabel(pm);
            name = sequence.toString();
            icon = BitmapUtils.drawableToBitmap(appInfo.loadIcon(pm));
         } catch (Throwable var17) {
            return false;
         }

         if (listener != null) {
            String newName = listener.getName(name);
            if (newName != null) {
               name = newName;
            }

            Bitmap newIcon = listener.getIcon(icon);
            if (newIcon != null) {
               icon = newIcon;
            }
         }

         Intent targetIntent = this.getLaunchIntent(packageName, userId);
         if (targetIntent == null) {
            return false;
         } else {
            Intent shortcutIntent = this.wrapperShortcutIntent(targetIntent, splash, packageName, userId);
            if (VERSION.SDK_INT >= 26) {
               ShortcutInfo likeShortcut = (new ShortcutInfo.Builder(this.getContext(), packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JhhSVg==")) + userId)).setLongLabel(name).setShortLabel(name).setIcon(Icon.createWithBitmap(icon)).setIntent(shortcutIntent).build();
               ShortcutManager shortcutManager = (ShortcutManager)this.getContext().getSystemService(ShortcutManager.class);
               if (shortcutManager != null) {
                  try {
                     shortcutManager.requestPinShortcut(likeShortcut, PendingIntent.getActivity(this.getContext(), packageName.hashCode() + userId, shortcutIntent, 134217728).getIntentSender());
                  } catch (Throwable var16) {
                     return false;
                  }
               }
            } else {
               Intent addIntent = new Intent();
               addIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmEgBiplNzAsIy0bKmILBgpgHxpO")), shortcutIntent);
               addIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmEgBiplNzAsIy0bKn0xJEhgEVRF")), name);
               addIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmEgBiplNzAsIy0bKmILLF5iJ1RF")), BitmapUtils.warrperIcon(icon, 256, 256));
               addIntent.setAction(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1Dm8zQQVlNzAZLhcLKn0KND9vDlkdORY2DGQmMB1iDwIALyscGH0xLB19IhpF")));

               try {
                  this.context.sendBroadcast(addIntent);
               } catch (Throwable var15) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public boolean removeShortcut(int userId, String packageName, Intent splash, OnEmitShortcutListener listener) {
      InstalledAppInfo setting = this.getInstalledAppInfo(packageName, 0);
      if (setting == null) {
         return false;
      } else {
         ApplicationInfo appInfo = setting.getApplicationInfo(userId);
         PackageManager pm = this.context.getPackageManager();

         String name;
         try {
            CharSequence sequence = appInfo.loadLabel(pm);
            name = sequence.toString();
         } catch (Throwable var12) {
            return false;
         }

         if (listener != null) {
            String newName = listener.getName(name);
            if (newName != null) {
               name = newName;
            }
         }

         Intent targetIntent = this.getLaunchIntent(packageName, userId);
         if (targetIntent == null) {
            return false;
         } else {
            Intent shortcutIntent = this.wrapperShortcutIntent(targetIntent, splash, packageName, userId);
            if (VERSION.SDK_INT < 26) {
               Intent addIntent = new Intent();
               addIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmEgBiplNzAsIy0bKmILBgpgHxpO")), shortcutIntent);
               addIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmEgBiplNzAsIy0bKn0xJEhgEVRF")), name);
               addIntent.setAction(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1Dm8zQQVlNzAZLhcLKn0KND9vDlkdORUADGILBg9kDzgRLhYYHGcYRQ59NR5PIRhSVg==")));
               this.context.sendBroadcast(addIntent);
            }

            return true;
         }
      }
   }

   public Intent wrapperShortcutIntent(Intent intent, Intent splash, String packageName, int userId) {
      Intent shortcutIntent = new Intent();
      shortcutIntent.addCategory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoJzg/LhgmKWEzBSlmHApKICsAAmcVSFo=")));
      shortcutIntent.setAction(Constants.ACTION_SHORTCUT);
      shortcutIntent.setPackage(this.getHostPkg());
      if (splash != null) {
         shortcutIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9hJyQoLwgqMmMFSFo=")), splash.toUri(0));
      }

      shortcutIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9hHg49Ji5SVg==")), packageName);
      shortcutIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASwzJi5SVg==")), intent.toUri(0));
      shortcutIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASg/IzxfMWk2NFo=")), userId);
      return shortcutIntent;
   }

   public InstalledAppInfo getInstalledAppInfo(String pkg, int flags) {
      try {
         return this.getService().getInstalledAppInfo(pkg, flags);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (InstalledAppInfo)VirtualRuntime.crash(e);
      }
   }

   public int getInstalledAppCount() {
      try {
         return this.getService().getInstalledAppCount();
      } catch (RemoteException var2) {
         RemoteException e = var2;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public boolean isStartup() {
      return this.isStartUp;
   }

   public boolean uninstallPackageAsUser(String pkgName, int userId) {
      try {
         return this.getService().uninstallPackageAsUser(pkgName, userId);
      } catch (RemoteException var4) {
         return false;
      }
   }

   public boolean uninstallPackage(String pkgName) {
      try {
         return this.getService().uninstallPackage(pkgName);
      } catch (RemoteException var3) {
         return false;
      }
   }

   public Resources getResources(String pkg) throws Resources.NotFoundException {
      InstalledAppInfo installedAppInfo = this.getInstalledAppInfo(pkg, 0);
      if (installedAppInfo != null) {
         AssetManager assets = (AssetManager)mirror.android.content.res.AssetManager.ctor.newInstance();
         mirror.android.content.res.AssetManager.addAssetPath.call(assets, installedAppInfo.getApkPath());
         Resources hostRes = this.context.getResources();
         return new Resources(assets, hostRes.getDisplayMetrics(), hostRes.getConfiguration());
      } else {
         throw new Resources.NotFoundException(pkg);
      }
   }

   public synchronized ActivityInfo resolveActivityInfo(Intent intent, int userId) {
      if (SpecialComponentList.shouldBlockIntent(intent)) {
         return null;
      } else {
         ActivityInfo activityInfo = null;
         if (intent.getComponent() == null) {
            ResolveInfo resolveInfo = VPackageManager.get().resolveIntent(intent, intent.getType(), 0, userId);
            if (resolveInfo != null && resolveInfo.activityInfo != null) {
               activityInfo = resolveInfo.activityInfo;
               intent.setClassName(activityInfo.packageName, activityInfo.name);
            }
         } else {
            activityInfo = this.resolveActivityInfo(intent.getComponent(), userId);
         }

         return activityInfo;
      }
   }

   public ActivityInfo resolveActivityInfo(ComponentName componentName, int userId) {
      return VPackageManager.get().getActivityInfo(componentName, 0, userId);
   }

   public ServiceInfo resolveServiceInfo(Intent intent, int userId) {
      if (SpecialComponentList.shouldBlockIntent(intent)) {
         return null;
      } else {
         ServiceInfo serviceInfo = null;
         ResolveInfo resolveInfo = VPackageManager.get().resolveService(intent, intent.getType(), 0, userId);
         if (resolveInfo != null) {
            serviceInfo = resolveInfo.serviceInfo;
         }

         return serviceInfo;
      }
   }

   public void killApp(String pkg, int userId) {
      VActivityManager.get().killAppByPkg(pkg, userId);
   }

   public void killAllApps() {
      VActivityManager.get().killAllApps();
   }

   public List<InstalledAppInfo> getInstalledApps(int flags) {
      try {
         return this.getService().getInstalledApps(flags);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public List<InstalledAppInfo> getInstalledAppsAsUser(int userId, int flags) {
      try {
         return this.getService().getInstalledAppsAsUser(userId, flags);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public void scanApps() {
      if (!this.scanned) {
         try {
            this.getService().scanApps();
            this.scanned = true;
         } catch (RemoteException var2) {
         }

      }
   }

   public AppRequestListener getAppRequestListener() {
      return this.mAppRequestListener;
   }

   public void setAppRequestListener(AppRequestListener listener) {
      this.mAppRequestListener = listener;
   }

   public boolean isPackageLaunched(int userId, String packageName) {
      try {
         return this.getService().isPackageLaunched(userId, packageName);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public void setPackageHidden(int userId, String packageName, boolean hidden) {
      try {
         this.getService().setPackageHidden(userId, packageName, hidden);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
      }

   }

   public boolean installPackageAsUser(int userId, String packageName) {
      try {
         return this.getService().installPackageAsUser(userId, packageName);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public boolean isAppInstalledAsUser(int userId, String packageName) {
      try {
         return this.getService().isAppInstalledAsUser(userId, packageName);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public int[] getPackageInstalledUsers(String packageName) {
      try {
         return this.getService().getPackageInstalledUsers(packageName);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (int[])VirtualRuntime.crash(e);
      }
   }

   public List<String> getInstalledSplitNames(String packageName) {
      try {
         return this.getService().getInstalledSplitNames(packageName);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public void registerObserver(IPackageObserver observer) {
      try {
         this.getService().registerObserver(observer);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         VirtualRuntime.crash(e);
      }

   }

   public void unregisterObserver(IPackageObserver observer) {
      try {
         this.getService().unregisterObserver(observer);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         VirtualRuntime.crash(e);
      }

   }

   public boolean isOutsideInstalled(String packageName) {
      if (packageName == null) {
         return false;
      } else {
         try {
            this.hostPackageManager.getApplicationInfo(packageName, 0L);
            return true;
         } catch (PackageManager.NameNotFoundException var3) {
            return false;
         }
      }
   }

   public boolean isExtPackageInstalled() {
      return this.isOutsideInstalled(StubManifest.EXT_PACKAGE_NAME);
   }

   public boolean isRunInExtProcess(String packageName) {
      try {
         return this.getService().isRunInExtProcess(packageName);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public boolean cleanPackageData(String pkg, int userId) {
      try {
         return this.getService().cleanPackageData(pkg, userId);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public int getAppPid(String pkg, int userId, String proccessName) {
      return VActivityManager.get().getAppPid(pkg, userId, proccessName);
   }

   public boolean isSharedUserId() {
      return this.myUid() == this.remoteUid();
   }

   public abstract static class VirtualInitializer {
      public void onMainProcess() {
      }

      public void onVirtualProcess() {
      }

      public void onServerProcess() {
      }

      public void onChildProcess() {
      }
   }

   public interface OnEmitShortcutListener {
      Bitmap getIcon(Bitmap var1);

      String getName(String var1);
   }

   public interface AppRequestListener {
      void onRequestInstall(String var1);

      void onRequestUninstall(String var1);
   }

   private static enum ProcessType {
      Server,
      VAppClient,
      Main,
      Helper,
      CHILD;
   }

   public abstract static class PackageObserver extends IPackageObserver.Stub {
   }
}
