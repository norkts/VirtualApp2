package com.lody.virtual.server.am;

import android.app.ActivityManager;
import android.app.IStopUserCallback;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.IVClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.Constants;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.client.ipc.ProviderCall;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.compat.ActivityManagerCompat;
import com.lody.virtual.helper.compat.ApplicationThreadCompat;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.helper.compat.PermissionCompat;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.Singleton;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VBinder;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.AppTaskInfo;
import com.lody.virtual.remote.BadgerInfo;
import com.lody.virtual.remote.ClientConfig;
import com.lody.virtual.remote.IntentSenderData;
import com.lody.virtual.remote.VParceledListSlice;
import com.lody.virtual.server.extension.VExtPackageAccessor;
import com.lody.virtual.server.interfaces.IActivityManager;
import com.lody.virtual.server.pm.PackageCacheManager;
import com.lody.virtual.server.pm.PackageSetting;
import com.lody.virtual.server.pm.VAppManagerService;
import com.lody.virtual.server.pm.VPackageManagerService;
import com.lody.virtual.server.settings.VSettingsProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VActivityManagerService extends IActivityManager.Stub {
   private static final Singleton<VActivityManagerService> sService = new Singleton<VActivityManagerService>() {
      protected VActivityManagerService create() {
         return new VActivityManagerService();
      }
   };
   private static final String TAG = VActivityManagerService.class.getSimpleName();
   private final List<ProcessRecord> mPidsSelfLocked;
   private final ActivityStack mActivityStack;
   private final Map<IBinder, IntentSenderData> mIntentSenderMap;
   private final Map<String, Boolean> sIdeMap;
   private boolean mResult;
   private final Handler mHandler;

   private VActivityManagerService() {
      this.mPidsSelfLocked = new ArrayList();
      this.mActivityStack = new ActivityStack(this);
      this.mIntentSenderMap = new HashMap();
      this.sIdeMap = new HashMap();
      this.mHandler = new Handler();
      this.mHandler.postDelayed(new Runnable() {
         public void run() {
            synchronized(VActivityManagerService.this.mIntentSenderMap) {
               Iterator<IntentSenderData> it = VActivityManagerService.this.mIntentSenderMap.values().iterator();

               while(true) {
                  if (!it.hasNext()) {
                     break;
                  }

                  IntentSenderData data = (IntentSenderData)it.next();
                  PendingIntent pendingIntent = data.getPendingIntent();
                  if (pendingIntent == null || pendingIntent.getTargetPackage() == null) {
                     it.remove();
                  }
               }
            }

            VActivityManagerService.this.mHandler.postDelayed(this, 300000L);
         }
      }, 300000L);
   }

   public static VActivityManagerService get() {
      return (VActivityManagerService)sService.get();
   }

   public int startActivity(Intent intent, ActivityInfo info, IBinder resultTo, Bundle options, String resultWho, int requestCode, String callingPkg, int userId) {
      synchronized(this) {
         int var10000;
         try {
            var10000 = this.mActivityStack.startActivityLocked(userId, intent, info, resultTo, options, resultWho, requestCode);
         } catch (Throwable var12) {
            Throwable e = var12;
            throw new RuntimeException(e);
         }

         return var10000;
      }
   }

   public int startActivityFromHistory(Intent intent) {
      synchronized(this) {
         return this.mActivityStack.startActivityFromHistoryLocked(intent);
      }
   }

   public boolean finishActivityAffinity(int userId, IBinder token) {
      synchronized(this) {
         return this.mActivityStack.finishActivityAffinity(userId, token);
      }
   }

   public int startActivities(Intent[] intents, String[] resolvedTypes, IBinder token, Bundle options, String callingPkg, int userId) {
      synchronized(this) {
         ActivityInfo[] infos = new ActivityInfo[intents.length];

         for(int i = 0; i < intents.length; ++i) {
            ActivityInfo ai = VirtualCore.get().resolveActivityInfo(intents[i], userId);
            if (ai == null) {
               return ActivityManagerCompat.START_INTENT_NOT_RESOLVED;
            }

            infos[i] = ai;
         }

         return this.mActivityStack.startActivitiesLocked(userId, intents, infos, token, options);
      }
   }

   public int getSystemPid() {
      return Process.myPid();
   }

   public int getSystemUid() {
      return Process.myUid();
   }

   public int getCurrentUserId() {
      return VUserHandle.myUserId();
   }

   public void onActivityCreated(IBinder record, IBinder token, int taskId) {
      int pid = Binder.getCallingPid();
      ProcessRecord targetApp;
      synchronized(this.mPidsSelfLocked) {
         targetApp = this.findProcessLocked(pid);
      }

      if (targetApp != null) {
         this.mActivityStack.onActivityCreated(targetApp, token, taskId, (ActivityRecord)record);
      }

   }

   public void onActivityResumed(int userId, IBinder token) {
      this.mActivityStack.onActivityResumed(userId, token);
   }

   public boolean onActivityDestroyed(int userId, IBinder token) {
      ActivityRecord r = this.mActivityStack.onActivityDestroyed(userId, token);
      return r != null;
   }

   public void onActivityFinish(int userId, IBinder token) {
      this.mActivityStack.onActivityFinish(userId, token);
   }

   public AppTaskInfo getTaskInfo(int taskId) {
      return this.mActivityStack.getTaskInfo(taskId);
   }

   public String getPackageForToken(int userId, IBinder token) {
      return this.mActivityStack.getPackageForToken(userId, token);
   }

   public ComponentName getActivityClassForToken(int userId, IBinder token) {
      return this.mActivityStack.getActivityClassForToken(userId, token);
   }

   private void processDied(ProcessRecord record) {
      this.mActivityStack.processDied(record);
   }

   public IBinder acquireProviderClient(int userId, ProviderInfo info) {
      String processName = info.processName;
      ProcessRecord r;
      synchronized(this) {
         r = this.startProcessIfNeeded(processName, userId, info.packageName, -1);
      }

      if (r != null) {
         try {
            return r.client.acquireProviderClient(info);
         } catch (RemoteException var8) {
            RemoteException e = var8;
            e.printStackTrace();
         }
      }

      return null;
   }

   public boolean broadcastFinish(IBinder token) throws RemoteException {
      synchronized(this.mPidsSelfLocked) {
         Iterator var3 = this.mPidsSelfLocked.iterator();

         ProcessRecord r;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            r = (ProcessRecord)var3.next();
         } while(r.client == null || !r.client.finishReceiver(token));

         return true;
      }
   }

   public void addOrUpdateIntentSender(IntentSenderData sender, int userId) {
      if (sender != null && sender.token != null) {
         synchronized(this.mIntentSenderMap) {
            IntentSenderData data = (IntentSenderData)this.mIntentSenderMap.get(sender.token);
            if (data == null) {
               this.mIntentSenderMap.put(sender.token, sender);
            } else {
               data.update(sender);
            }

         }
      }
   }

   public void removeIntentSender(IBinder token) {
      if (token != null) {
         synchronized(this.mIntentSenderMap) {
            this.mIntentSenderMap.remove(token);
         }
      }

   }

   public IntentSenderData getIntentSender(IBinder token) {
      if (token != null) {
         synchronized(this.mIntentSenderMap) {
            return (IntentSenderData)this.mIntentSenderMap.get(token);
         }
      } else {
         return null;
      }
   }

   public ComponentName getCallingActivity(int userId, IBinder token) {
      return this.mActivityStack.getCallingActivity(userId, token);
   }

   public String getCallingPackage(int userId, IBinder token) {
      return this.mActivityStack.getCallingPackage(userId, token);
   }

   public VParceledListSlice<ActivityManager.RunningServiceInfo> getServices(String pkg, int maxNum, int flags, int userId) {
      List<ActivityManager.RunningServiceInfo> infoList = new ArrayList();
      synchronized(this.mPidsSelfLocked) {
         Iterator var7 = this.mPidsSelfLocked.iterator();

         while(var7.hasNext()) {
            ProcessRecord r = (ProcessRecord)var7.next();
            if (r.pkgList.contains(pkg) && r.client.asBinder().isBinderAlive()) {
               try {
                  ((List)infoList).addAll(r.client.getServices());
               } catch (RemoteException var11) {
                  RemoteException e = var11;
                  e.printStackTrace();
               }
            }
         }
      }

      if (((List)infoList).size() > maxNum) {
         infoList = ((List)infoList).subList(0, maxNum);
      }

      return new VParceledListSlice((List)infoList);
   }

   public void processRestarted(String packageName, String processName, int userId) {
      int callingPid = VBinder.getCallingPid();
      ProcessRecord app;
      synchronized(this.mPidsSelfLocked) {
         app = this.findProcessLocked(callingPid);
      }

      if (app == null) {
         String stubProcessName = this.getProcessName(callingPid);
         if (stubProcessName == null) {
            return;
         }

         int vpid = this.parseVPid(stubProcessName);
         if (vpid != -1) {
            this.startProcessIfNeeded(processName, userId, packageName, vpid);
         }
      }

   }

   private int parseVPid(String stubProcessName) {
      if (stubProcessName == null) {
         return -1;
      } else {
         String prefix;
         if (stubProcessName.startsWith(StubManifest.EXT_PACKAGE_NAME)) {
            prefix = StubManifest.EXT_PACKAGE_NAME + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD06Vg=="));
         } else {
            if (!stubProcessName.startsWith(StubManifest.PACKAGE_NAME)) {
               return -1;
            }

            prefix = VirtualCore.get().getHostPkg() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD06Vg=="));
         }

         if (stubProcessName.startsWith(prefix)) {
            try {
               return Integer.parseInt(stubProcessName.substring(prefix.length()));
            } catch (NumberFormatException var4) {
            }
         }

         return -1;
      }
   }

   private String getProcessName(int pid) {
      Iterator var2 = VirtualCore.get().getRunningAppProcessesEx().iterator();

      ActivityManager.RunningAppProcessInfo info;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         info = (ActivityManager.RunningAppProcessInfo)var2.next();
      } while(info.pid != pid);

      return info.processName;
   }

   private void onProcessDied(ProcessRecord record) {
      if (record != null) {
         synchronized(this.mPidsSelfLocked) {
            this.mPidsSelfLocked.remove(record);
         }

         this.processDied(record);
      }

   }

   public int getFreeStubCount() {
      return StubManifest.STUB_COUNT - this.mPidsSelfLocked.size();
   }

   public int checkPermission(boolean isExt, String permission, int pid, int uid, String packageName) {
      if (permission == null) {
         return -1;
      } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCw+H2UmAlVkNTAOISxbDGALPFRnJ1RF")).equals(permission)) {
         return 0;
      } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsMGWUmLBZiMgoOIBYuA2cYGh9iNQ5TLhU2WGYILFo=")).equals(permission)) {
         return 0;
      } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCwMHWUmElV9EVRF")).equals(permission)) {
         return 0;
      } else if (!StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCwYDG4YLB9hDCwTJQZbH2QxGg9nMgZPLys2E30jSFo=")).equals(permission) && !StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCwYDG4YLB9hDCwTJQZbH2QxGg9nMgZPLys2E30hRVV9JSQR")).equals(permission)) {
         if (uid == 0) {
            return 0;
         } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsMGWUIFl9mHAoVIiwYGWEhLF5iJSQWLC5SVg==")).equals(permission)) {
            return 0;
         } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsmU2sLFgpgIjBJOxY2H2MIGh9iNRoXLQU+Vg==")).equals(permission)) {
            return 0;
         } else {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCwAGGkmLB9iMgoOJwVfHX02MBNnJShMLisIBmAhPFZiHyAWIis2XGIxBl4=")).equals(permission) ? 0 : VPackageManagerService.get().checkUidPermission(isExt, permission, uid);
         }
      } else {
         return -1;
      }
   }

   public ClientConfig initProcess(String packageName, String processName, int userId) {
      ProcessRecord r = this.startProcessIfNeeded(processName, userId, packageName, -1);
      return r != null ? r.getClientConfig() : null;
   }

   public void appDoneExecuting(String packageName, int userId) {
      int pid = VBinder.getCallingPid();
      ProcessRecord r = this.findProcessLocked(pid);
      if (r != null) {
         r.pkgList.add(packageName);
      }

   }

   ProcessRecord startProcessIfNeeded(String processName, int userId, String packageName, int vpid) {
      this.runProcessGC();
      PackageSetting ps = PackageCacheManager.getSetting(packageName);
      boolean isExt = ps.isRunInExtProcess();
      if (isExt && !VirtualCore.get().isExtPackageInstalled()) {
         VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMExhNB45KAgqL2QjHgBrASgvLhgpJGIwPCxsHgotOD4cCWsJICBqMzw0IRgLL2UaODNsAV0yLQQ6CGowMyhjDlkpLBciCG83MyZ1NDwsLT5bO2IgLBNpDlEuPhhSVg==")) + packageName);
         return null;
      } else {
         ApplicationInfo info = VPackageManagerService.get().getApplicationInfo(packageName, 0, userId);
         if (ps != null && info != null) {
            if (!ps.isLaunched(userId)) {
               ps.setLaunched(userId, true);
               VAppManagerService.get().savePersistenceData();
            }

            int vuid = VUserHandle.getUid(userId, ps.appId);
            synchronized(this) {
               ProcessRecord app;
               if (vpid != -1) {
                  app = new ProcessRecord(info, processName, vuid, vpid, isExt);
                  if (this.initProcessLocked(app)) {
                     synchronized(this.mPidsSelfLocked) {
                        this.mPidsSelfLocked.add(app);
                     }

                     return app;
                  } else {
                     return null;
                  }
               } else {
                  synchronized(this.mPidsSelfLocked) {
                     app = this.findProcessLocked(processName, userId);
                  }

                  if (app != null) {
                     return app;
                  } else {
                     if (processName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mL2EkRTNuASg8Ki0YCmsFBiA=")))) {
                        Intent intent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42JBJ9JVlNIRY2XWILJEx9HFEKLBhSVg==")));
                        intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmYFNCBlNVkhKC4qIGUVNFo=")), userId);
                        VirtualCore.get().getContext().sendBroadcast(intent);
                     }

                     Set<Integer> blackList = new HashSet(3);
                     int retryCount = 3;

                     while(retryCount-- > 0) {
                        vpid = this.queryFreeStubProcess(isExt, blackList);
                        if (vpid == -1) {
                           this.killAllApps();
                           VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4fOGggFitiCiQuIxccPn83TQRvAR0rJjw1JGAwAjJ8MBEd")));
                           SystemClock.sleep(500L);
                        } else {
                           app = new ProcessRecord(info, processName, vuid, vpid, isExt);
                           if (this.initProcessLocked(app)) {
                              synchronized(this.mPidsSelfLocked) {
                                 this.mPidsSelfLocked.add(app);
                              }

                              return app;
                           }

                           blackList.add(vpid);
                        }
                     }

                     return null;
                  }
               }
            }
         } else {
            VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMExhNB45KAgqL2QjHgBrASgvLhgpJGIwPCxsHgotOD4cCWsJICBqMzwoJxghL2wwRSJ+NzA5Ki0qP2oFGSZOMFksLwcqCW4jEit9NzgeLl5XVg==")) + packageName);
            return null;
         }
      }
   }

   private void runProcessGC() {
      if (get().getFreeStubCount() < 10) {
         this.killAllApps();
      }

   }

   private void sendFirstLaunchBroadcast(PackageSetting ps, int userId) {
      Intent intent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xOA5hIgIAJwYAE2MxAgBnMiwALhUmWWQ2MFM=")), Uri.fromParts(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iAVRF")), ps.packageName, (String)null));
      intent.setPackage(ps.packageName);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49Km4IGgk=")), VUserHandle.getUid(ps.appId, userId));
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmYFNCBlNVkhKC4qIGUVNFo=")), userId);
      this.sendBroadcastAsUser(intent, new VUserHandle(userId));
   }

   public int getUidByPid(int pid) {
      synchronized(this.mPidsSelfLocked) {
         ProcessRecord r = this.findProcessLocked(pid);
         if (r != null) {
            return r.vuid;
         }
      }

      return pid == Process.myPid() ? 1000 : 9000;
   }

   private void startRequestPermissions(boolean isExt, String[] permissions, final ConditionVariable permissionLock) {
      PermissionCompat.startRequestPermissions(VirtualCore.get().getContext(), isExt, permissions, new PermissionCompat.CallBack() {
         public boolean onResult(int requestCode, String[] permissions, int[] grantResults) {
            try {
               VActivityManagerService.this.mResult = PermissionCompat.isRequestGranted(grantResults);
            } finally {
               permissionLock.open();
            }

            return VActivityManagerService.this.mResult;
         }
      });
   }

   private boolean initProcessLocked(final ProcessRecord app) {
      this.requestPermissionIfNeed(app);
      Bundle extras = new Bundle();
      extras.putParcelable(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh99JFEzKAcYLmMFAiVlNyQaLjsAVg==")), app.getClientConfig());
      extras.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh99JB4qKAZfKmwjBh8=")), Process.myPid());
      Bundle res = ProviderCall.callSafely(app.getProviderAuthority(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9jDlkzLBZfKmoVNClrDjA6ID5SVg==")), (String)null, extras, 0);
      if (res == null) {
         return false;
      } else {
         app.pid = res.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9hHgYwJi5SVg==")));
         final IBinder clientBinder = BundleCompat.getBinder(res, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh99JFEzKAcYLmMFSFo=")));
         IVClient client = IVClient.Stub.asInterface(clientBinder);
         if (client == null) {
            app.kill();
            return false;
         } else {
            RemoteException e;
            try {
               clientBinder.linkToDeath(new IBinder.DeathRecipient() {
                  public void binderDied() {
                     clientBinder.unlinkToDeath(this, 0);
                     VActivityManagerService.this.onProcessDied(app);
                  }
               }, 0);
            } catch (RemoteException var8) {
               e = var8;
               e.printStackTrace();
            }

            app.client = client;

            try {
               app.appThread = ApplicationThreadCompat.asInterface(client.getAppThread());
            } catch (RemoteException var7) {
               e = var7;
               e.printStackTrace();
            }

            VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMyhgNDAtPxgmKG8FAitsJDMrPyo6Vg==")) + app.processName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phc6CWgOTCg=")) + app.pid);
            return true;
         }
      }
   }

   private void requestPermissionIfNeed(ProcessRecord app) {
      if (PermissionCompat.isCheckPermissionRequired(app.info)) {
         String[] permissions = VPackageManagerService.get().getDangerousPermissions(app.info.packageName);
         if (!PermissionCompat.checkPermissions(permissions, app.isExt)) {
            ConditionVariable permissionLock = new ConditionVariable();
            this.startRequestPermissions(app.isExt, permissions, permissionLock);
            permissionLock.block();
         }
      }

   }

   public int queryFreeStubProcess(boolean isExt, Set<Integer> blackList) {
      synchronized(this.mPidsSelfLocked) {
         for(int vpid = 0; vpid < StubManifest.STUB_COUNT; ++vpid) {
            int N = this.mPidsSelfLocked.size();
            boolean skip = false;

            while(N-- > 0) {
               ProcessRecord r = (ProcessRecord)this.mPidsSelfLocked.get(N);
               if (blackList.contains(r.vpid)) {
                  skip = true;
                  break;
               }

               if (r.vpid == vpid && r.isExt == isExt) {
                  skip = true;
                  break;
               }
            }

            if (!skip) {
               return vpid;
            }
         }

         return -1;
      }
   }

   public boolean isAppProcess(String processName) {
      return this.parseVPid(processName) != -1;
   }

   public boolean isAppPid(int pid) {
      synchronized(this.mPidsSelfLocked) {
         return this.findProcessLocked(pid) != null;
      }
   }

   public String getAppProcessName(int pid) {
      synchronized(this.mPidsSelfLocked) {
         ProcessRecord r = this.findProcessLocked(pid);
         return r != null ? r.processName : null;
      }
   }

   public List<String> getProcessPkgList(int pid) {
      synchronized(this.mPidsSelfLocked) {
         ProcessRecord r = this.findProcessLocked(pid);
         if (r != null) {
            return new ArrayList(r.pkgList);
         }
      }

      return Collections.emptyList();
   }

   public void killAllApps() {
      synchronized(this.mPidsSelfLocked) {
         for(int i = 0; i < this.mPidsSelfLocked.size(); ++i) {
            ProcessRecord r = (ProcessRecord)this.mPidsSelfLocked.get(i);
            r.kill();
         }

      }
   }

   public void killAppByPkg(String pkg, int userId) {
      synchronized(this.mPidsSelfLocked) {
         Iterator var4 = this.mPidsSelfLocked.iterator();

         while(true) {
            ProcessRecord r;
            do {
               if (!var4.hasNext()) {
                  return;
               }

               r = (ProcessRecord)var4.next();
            } while(userId != -1 && r.userId != userId);

            if (r.pkgList.contains(pkg)) {
               r.kill();
            }
         }
      }
   }

   public boolean isAppRunning(String packageName, int userId, boolean foreground) {
      boolean running = false;
      synchronized(this.mPidsSelfLocked) {
         int N = this.mPidsSelfLocked.size();

         while(N-- > 0) {
            ProcessRecord r = (ProcessRecord)this.mPidsSelfLocked.get(N);
            if (r.userId == userId && r.info.packageName.equals(packageName) && (!foreground || r.info.processName.equals(packageName))) {
               try {
                  running = r.client.isAppRunning();
               } catch (Exception var10) {
                  Exception e = var10;
                  e.printStackTrace();
               }
               break;
            }
         }

         return running;
      }
   }

   public void killApplicationProcess(String processName, int vuid) {
      synchronized(this.mPidsSelfLocked) {
         Iterator var4 = this.mPidsSelfLocked.iterator();

         while(var4.hasNext()) {
            ProcessRecord r = (ProcessRecord)var4.next();
            if (r.vuid == vuid) {
               if (r.isExt) {
                  VExtPackageAccessor.forceStop(new int[]{r.pid});
               } else {
                  r.kill();
               }
            }
         }

      }
   }

   public void dump() {
   }

   public String getInitialPackage(int pid) {
      synchronized(this.mPidsSelfLocked) {
         ProcessRecord r = this.findProcessLocked(pid);
         return r != null ? r.info.packageName : null;
      }
   }

   public ProcessRecord findProcessLocked(int pid) {
      Iterator var2 = this.mPidsSelfLocked.iterator();

      ProcessRecord r;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         r = (ProcessRecord)var2.next();
      } while(r.pid != pid);

      return r;
   }

   public ProcessRecord findProcessLocked(String processName, int userId) {
      Iterator var3 = this.mPidsSelfLocked.iterator();

      ProcessRecord r;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         r = (ProcessRecord)var3.next();
      } while(!r.processName.equals(processName) || r.userId != userId);

      return r;
   }

   public int stopUser(int userHandle, IStopUserCallback.Stub stub) {
      synchronized(this.mPidsSelfLocked) {
         int N = this.mPidsSelfLocked.size();

         while(N-- > 0) {
            ProcessRecord r = (ProcessRecord)this.mPidsSelfLocked.get(N);
            if (r.userId == userHandle) {
               r.kill();
            }
         }
      }

      try {
         stub.userStopped(userHandle);
      } catch (RemoteException var7) {
         RemoteException e = var7;
         e.printStackTrace();
      }

      return 0;
   }

   public void sendOrderedBroadcastAsUser(Intent intent, VUserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
      Context context = VirtualCore.get().getContext();
      if (user != null) {
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASg/IzxfMWk2NFo=")), user.getIdentifier());
      }

      context.sendOrderedBroadcast(intent, (String)null, resultReceiver, scheduler, initialCode, initialData, initialExtras);
   }

   public void sendBroadcastAsUser(Intent intent, VUserHandle user) {
      SpecialComponentList.protectIntent(intent);
      Context context = VirtualCore.get().getContext();
      if (user != null) {
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASg/IzxfMWk2NFo=")), user.getIdentifier());
      }

      context.sendBroadcast(intent);
   }

   public void sendBroadcastAsUser(Intent intent, VUserHandle user, String permission) {
      SpecialComponentList.protectIntent(intent);
      Context context = VirtualCore.get().getContext();
      if (user != null) {
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASg/IzxfMWk2NFo=")), user.getIdentifier());
      }

      context.sendBroadcast(intent);
   }

   public void notifyBadgerChange(BadgerInfo info) {
      Intent intent = new Intent(Constants.ACTION_BADGER_CHANGE);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28hAiw=")), info.userId);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iDFk7KgcMVg==")), info.packageName);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+PGgzNARlJB4vKj42Vg==")), info.badgerCount);
      VirtualCore.get().getContext().sendBroadcast(intent);
   }

   public void setAppInactive(String packageName, boolean idle, int userId) {
      synchronized(this.sIdeMap) {
         this.sIdeMap.put(packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JhhSVg==")) + userId, idle);
      }
   }

   public boolean isAppInactive(String packageName, int userId) {
      synchronized(this.sIdeMap) {
         Boolean idle = (Boolean)this.sIdeMap.get(packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JhhSVg==")) + userId);
         return idle != null && !idle;
      }
   }

   public void handleDownloadCompleteIntent(Intent intent) {
      intent.setPackage((String)null);
      intent.setComponent((ComponentName)null);
      Intent send = ComponentUtils.proxyBroadcastIntent(intent, -1);
      VirtualCore.get().getContext().sendBroadcast(send);
   }

   public int getAppPid(String packageName, int userId, String proccessName) {
      synchronized(this.mPidsSelfLocked) {
         int N = this.mPidsSelfLocked.size();

         while(true) {
            if (N-- > 0) {
               ProcessRecord r = (ProcessRecord)this.mPidsSelfLocked.get(N);
               if (r.userId != userId || !r.info.packageName.equals(packageName)) {
                  continue;
               }

               try {
                  if (r.client.isAppRunning() && r.info.processName.equals(proccessName)) {
                     int var10000 = r.pid;
                     return var10000;
                  }
               } catch (Exception var9) {
                  Exception e = var9;
                  e.printStackTrace();
               }
            }

            return -1;
         }
      }
   }

   public final void setSettingsProvider(int userId, int tableIndex, String arg, String value) {
      VSettingsProvider.getInstance().setSettingsProvider(userId, tableIndex, arg, value);
   }

   public final String getSettingsProvider(int userId, int tableIndex, String arg) {
      return VSettingsProvider.getInstance().getSettingsProvider(userId, tableIndex, arg);
   }

   // $FF: synthetic method
   VActivityManagerService(Object x0) {
      this();
   }
}
