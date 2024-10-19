package com.lody.virtual.client.ipc;

import android.app.Activity;
import android.app.IServiceConnection;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.text.TextUtils;
import com.carlos.common.persistent.StoragePersistenceServices;
import com.carlos.common.persistent.VPersistent;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.hook.secondary.ServiceConnectionProxy;
import com.lody.virtual.client.stub.IntentBuilder;
import com.lody.virtual.client.stub.WindowPreviewActivity;
import com.lody.virtual.helper.compat.ActivityManagerCompat;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.AppTaskInfo;
import com.lody.virtual.remote.BadgerInfo;
import com.lody.virtual.remote.ClientConfig;
import com.lody.virtual.remote.IntentSenderData;
import com.lody.virtual.remote.VParceledListSlice;
import com.lody.virtual.server.IBinderProxyService;
import com.lody.virtual.server.extension.VExtPackageAccessor;
import com.lody.virtual.server.interfaces.IActivityManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mirror.android.app.ActivityThread;
import mirror.android.content.ContentProviderNative;

public class VActivityManager {
   private static final VActivityManager sAM = new VActivityManager();
   private IActivityManager mService;
   private static final Map<ServiceConnection, ServiceConnectionDelegate> sServiceConnectionDelegates = new HashMap();
   private static final String FILE_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki5fP28jNCxpESw/KD0MKGkjMClrDjBF"));
   private static SharedPreferences mSharedPreferences;

   public IActivityManager getService() {
      if (!IInterfaceUtils.isAlive(this.mService)) {
         Class var1 = VActivityManager.class;
         synchronized(VActivityManager.class) {
            Object remote = this.getRemoteInterface();
            this.mService = (IActivityManager)LocalProxyUtils.genProxy(IActivityManager.class, remote);
         }
      }

      return this.mService;
   }

   private Object getRemoteInterface() {
      return IActivityManager.Stub.asInterface(ServiceManagerNative.getService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZF"))));
   }

   public static VActivityManager get() {
      return sAM;
   }

   public int startActivity(Intent intent, ActivityInfo info, IBinder resultTo, Bundle options, String resultWho, int requestCode, String callingPkg, int userId) {
      if (info == null) {
         info = VirtualCore.get().resolveActivityInfo(intent, userId);
         if (info == null) {
            return ActivityManagerCompat.START_INTENT_NOT_RESOLVED;
         }
      }

      try {
         return this.getService().startActivity(intent, info, resultTo, options, resultWho, requestCode, callingPkg, userId);
      } catch (RemoteException var10) {
         RemoteException e = var10;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public int startActivities(Intent[] intents, String[] resolvedTypes, IBinder token, Bundle options, String callingPkg, int userId) {
      try {
         return this.getService().startActivities(intents, resolvedTypes, token, options, callingPkg, userId);
      } catch (RemoteException var8) {
         RemoteException e = var8;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public int startActivityFromHistory(Intent intent) {
      try {
         return this.getService().startActivityFromHistory(intent);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public int startActivity(Intent intent, int userId) {
      if (userId < 0) {
         return ActivityManagerCompat.START_NOT_CURRENT_USER_ACTIVITY;
      } else {
         ActivityInfo info = VirtualCore.get().resolveActivityInfo(intent, userId);
         return info == null ? ActivityManagerCompat.START_INTENT_NOT_RESOLVED : this.startActivity(intent, info, (IBinder)null, (Bundle)null, (String)null, -1, (String)null, userId);
      }
   }

   public void appDoneExecuting(String packageName) {
      try {
         this.getService().appDoneExecuting(packageName, VUserHandle.myUserId());
      } catch (RemoteException var3) {
         RemoteException e = var3;
         VirtualRuntime.crash(e);
      }

   }

   public void onActivityCreate(IBinder record, IBinder token, int taskId) {
      try {
         this.getService().onActivityCreated(record, token, taskId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
      }

   }

   public void onActivityResumed(IBinder token) {
      try {
         this.getService().onActivityResumed(VUserHandle.myUserId(), token);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         e.printStackTrace();
      }

   }

   public boolean onActivityDestroy(IBinder token) {
      try {
         return this.getService().onActivityDestroyed(VUserHandle.myUserId(), token);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public AppTaskInfo getTaskInfo(int taskId) {
      try {
         return this.getService().getTaskInfo(taskId);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (AppTaskInfo)VirtualRuntime.crash(e);
      }
   }

   public ComponentName getCallingActivity(IBinder token) {
      try {
         return this.getService().getCallingActivity(VUserHandle.myUserId(), token);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (ComponentName)VirtualRuntime.crash(e);
      }
   }

   public String getCallingPackage(IBinder token) {
      try {
         return this.getService().getCallingPackage(VUserHandle.myUserId(), token);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (String)VirtualRuntime.crash(e);
      }
   }

   public String getPackageForToken(IBinder token) {
      try {
         return this.getService().getPackageForToken(VUserHandle.myUserId(), token);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (String)VirtualRuntime.crash(e);
      }
   }

   public ComponentName getActivityForToken(IBinder token) {
      try {
         return this.getService().getActivityClassForToken(VUserHandle.myUserId(), token);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (ComponentName)VirtualRuntime.crash(e);
      }
   }

   public void processRestarted(String packageName, String processName, int userId) {
      try {
         this.getService().processRestarted(packageName, processName, userId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
      }

   }

   public String getAppProcessName(int pid) {
      try {
         return this.getService().getAppProcessName(pid);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (String)VirtualRuntime.crash(e);
      }
   }

   public String getInitialPackage(int pid) {
      try {
         return this.getService().getInitialPackage(pid);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (String)VirtualRuntime.crash(e);
      }
   }

   public boolean isAppProcess(String processName) {
      try {
         return this.getService().isAppProcess(processName);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public void killAllApps() {
      try {
         this.getService().killAllApps();
      } catch (RemoteException var2) {
         RemoteException e = var2;
         e.printStackTrace();
      }

   }

   public void killApplicationProcess(String procName, int vuid) {
      try {
         this.getService().killApplicationProcess(procName, vuid);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         e.printStackTrace();
      }

   }

   public void killAppByPkg(String pkg, int userId) {
      try {
         this.getService().killAppByPkg(pkg, userId);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         e.printStackTrace();
      }

   }

   public List<String> getProcessPkgList(int pid) {
      try {
         return this.getService().getProcessPkgList(pid);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public boolean isAppPid(int pid) {
      try {
         return this.getService().isAppPid(pid);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public int getUidByPid(int pid) {
      try {
         return this.getService().getUidByPid(pid);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public int getSystemPid() {
      try {
         return this.getService().getSystemPid();
      } catch (RemoteException var2) {
         RemoteException e = var2;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public int getSystemUid() {
      try {
         return this.getService().getSystemUid();
      } catch (RemoteException var2) {
         RemoteException e = var2;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public void sendCancelActivityResult(IBinder resultTo, String resultWho, int requestCode) {
      this.sendActivityResult(resultTo, resultWho, requestCode, (Intent)null, 0);
   }

   public void sendActivityResult(IBinder resultTo, String resultWho, int requestCode, Intent data, int resultCode) {
      Activity activity = this.findActivityByToken(resultTo);
      if (activity != null) {
         Object mainThread = VirtualCore.mainThread();
         ActivityThread.sendActivityResult.call(mainThread, resultTo, resultWho, requestCode, data, resultCode);
      }

   }

   public IInterface acquireProviderClient(int userId, ProviderInfo info) throws RemoteException {
      IBinder binder = this.getService().acquireProviderClient(userId, info);
      return binder != null ? (IInterface)ContentProviderNative.asInterface.call(binder) : null;
   }

   public void addOrUpdateIntentSender(IntentSenderData sender) throws RemoteException {
      this.getService().addOrUpdateIntentSender(sender, VUserHandle.myUserId());
   }

   public void removeIntentSender(IBinder token) throws RemoteException {
      this.getService().removeIntentSender(token);
   }

   public IntentSenderData getIntentSender(IBinder token) {
      try {
         return this.getService().getIntentSender(token);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (IntentSenderData)VirtualRuntime.crash(e);
      }
   }

   public Activity findActivityByToken(IBinder token) {
      Object r = ((Map)ActivityThread.mActivities.get(VirtualCore.mainThread())).get(token);
      return r != null ? (Activity)ActivityThread.ActivityClientRecord.activity.get(r) : null;
   }

   public void finishActivity(IBinder token) {
      Activity activity = this.findActivityByToken(token);
      if (activity == null) {
         VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITw+OWwFAj5jAQoZIgciDm4jEitsN1RF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YCGUaLCBlDiggKQg+MWUwLyhrNzgaLAQ5PksaPCZqHho/Ki0cM3hTHTRqJCg7JhhSVg==")));
      } else {
         while(true) {
            Activity parent = (Activity)mirror.android.app.Activity.mParent.get(activity);
            if (parent == null) {
               int resultCode = mirror.android.app.Activity.mResultCode.get(activity);
               Intent resultData = (Intent)mirror.android.app.Activity.mResultData.get(activity);
               ActivityManagerCompat.finishActivity(token, resultCode, resultData);
               mirror.android.app.Activity.mFinished.set(activity, true);
               return;
            }

            activity = parent;
         }
      }
   }

   public boolean isAppRunning(String packageName, int userId, boolean foreground) {
      try {
         return this.getService().isAppRunning(packageName, userId, foreground);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public int getUid() {
      return VClient.get().getVUid();
   }

   public ClientConfig initProcess(String packageName, String processName, int userId) {
      try {
         return this.getService().initProcess(packageName, processName, userId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (ClientConfig)VirtualRuntime.crash(e);
      }
   }

   public void sendBroadcast(Intent intent, int userId) {
      Intent newIntent = ComponentUtils.proxyBroadcastIntent(intent, userId);
      if (newIntent != null) {
         VirtualCore.get().getContext().sendBroadcast(newIntent);
      }

   }

   public void notifyBadgerChange(BadgerInfo info) {
      try {
         this.getService().notifyBadgerChange(info);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         VirtualRuntime.crash(e);
      }

   }

   public void setAppInactive(String packageName, boolean idle, int userId) {
      try {
         this.getService().setAppInactive(packageName, idle, userId);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         VirtualRuntime.crash(e);
      }

   }

   public boolean isAppInactive(String packageName, int userId) {
      try {
         return this.getService().isAppInactive(packageName, userId);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public boolean launchApp(int userId, String packageName) {
      return this.launchApp(userId, packageName, true);
   }

   int getPersistentValueToInt(String key) {
      StoragePersistenceServices storagePersistenceServices = StoragePersistenceServices.get();
      VPersistent persistent = storagePersistenceServices.getVPersistent();
      Map<String, String> buildAllConfig = persistent.buildAllConfig;
      String value = (String)buildAllConfig.get(key);
      if (!TextUtils.isEmpty(value)) {
         try {
            return Integer.parseInt(value);
         } catch (Exception var7) {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public static int getInt(Context context, String keyname, int def) {
      SharedPreferences shared = getSharedPreferences(context);
      int v = shared.getInt(keyname, def);
      return v == def ? def : v;
   }

   private static SharedPreferences getSharedPreferences(Context context) {
      if (mSharedPreferences == null) {
         mSharedPreferences = context.getSharedPreferences(FILE_NAME, 4);
      }

      return mSharedPreferences;
   }

   public boolean launchApp(final int userId, String packageName, boolean preview) {
      int channelLimit = this.getPersistentValueToInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fP2ojBitgHFEzKgccLg==")));
      int channelStatus = this.getPersistentValueToInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fP2ojBitgHyggLwg2LWoFSFo=")));
      int channelLimitLocal = getInt(VirtualCore.get().getContext(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fP2ojBitgHFEzKgccLg==")), 0);
      if (channelLimit <= channelLimitLocal) {
         return false;
      } else if (channelStatus == 0) {
         return false;
      } else if (VirtualCore.get().isRunInExtProcess(packageName) && !VExtPackageAccessor.hasExtPackageBootPermission()) {
         return false;
      } else {
         Context context = VirtualCore.get().getContext();
         VPackageManager pm = VPackageManager.get();
         Intent intentToResolve = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42QQ5nDB5F")));
         intentToResolve.addCategory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoJzg/LhgmKWEzBSlnDB5KIQhSVg==")));
         intentToResolve.setPackage(packageName);
         List<ResolveInfo> ris = pm.queryIntentActivities(intentToResolve, intentToResolve.resolveType(context), 0, userId);
         if (ris == null || ris.size() <= 0) {
            intentToResolve.removeCategory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoJzg/LhgmKWEzBSlnDB5KIQhSVg==")));
            intentToResolve.addCategory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoJzg/LhgmKWEzBSlkHCQUIRYYBmMIFlo=")));
            intentToResolve.setPackage(packageName);
            ris = pm.queryIntentActivities(intentToResolve, intentToResolve.resolveType(context), 0, userId);
         }

         if (ris != null && ris.size() > 0) {
            ActivityInfo info = ((ResolveInfo)ris.get(0)).activityInfo;
            final Intent intent = new Intent(intentToResolve);
            intent.setFlags(268435456);
            intent.setClassName(info.packageName, info.name);
            if (preview && !get().isAppRunning(info.packageName, userId, true)) {
               intent.addFlags(65536);
               WindowPreviewActivity.previewActivity(userId, info);
               VirtualRuntime.getUIHandler().postDelayed(new Runnable() {
                  public void run() {
                     VActivityManager.get().startActivity(intent, userId);
                  }
               }, 400L);
            } else {
               get().startActivity(intent, userId);
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public void onFinishActivity(IBinder token) {
      try {
         this.getService().onActivityFinish(VUserHandle.myUserId(), token);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         VirtualRuntime.crash(e);
      }

   }

   public int checkPermission(String permission, int pid, int uid) {
      try {
         return this.getService().checkPermission(VirtualCore.get().isExtPackage(), permission, pid, uid, VClient.get().getCurrentPackage());
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public void handleDownloadCompleteIntent(Intent intent) {
      try {
         this.getService().handleDownloadCompleteIntent(intent);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         e.printStackTrace();
      }

   }

   public boolean finishActivityAffinity(int userId, IBinder token) {
      try {
         return this.getService().finishActivityAffinity(userId, token);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public ComponentName startService(Context context, Intent service, int userId) {
      if (VirtualCore.get().isServerProcess()) {
         service.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASg/IzxfMWk2NFo=")), userId);
         return context.startService(service);
      } else {
         ServiceInfo serviceInfo = VirtualCore.get().resolveServiceInfo(service, userId);
         if (serviceInfo != null) {
            ClientConfig clientConfig = get().initProcess(serviceInfo.packageName, serviceInfo.processName, userId);
            Intent intent = IntentBuilder.createStartProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, serviceInfo, service, userId);
            return context.startService(intent);
         } else {
            return null;
         }
      }
   }

   public ServiceConnectionDelegate getDelegate(ServiceConnection conn) {
      ServiceConnectionDelegate delegate = (ServiceConnectionDelegate)sServiceConnectionDelegates.get(conn);
      if (delegate == null) {
         delegate = new ServiceConnectionDelegate(conn);
         sServiceConnectionDelegates.put(conn, delegate);
      }

      return delegate;
   }

   public ServiceConnection removeDelegate(ServiceConnection conn) {
      Iterator<ServiceConnectionDelegate> it = sServiceConnectionDelegates.values().iterator();

      ServiceConnectionDelegate delegate;
      do {
         if (!it.hasNext()) {
            return conn;
         }

         delegate = (ServiceConnectionDelegate)it.next();
      } while(conn != delegate);

      it.remove();
      return delegate;
   }

   public boolean bindService(Context context, Intent service, ServiceConnection connection, int flags, int userId) {
      if (VirtualCore.get().isServerProcess()) {
         service.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASg/IzxfMWk2NFo=")), userId);
         return context.bindService(service, connection, flags);
      } else {
         ServiceConnection connection = this.getDelegate(connection);
         ServiceInfo serviceInfo = VirtualCore.get().resolveServiceInfo(service, userId);
         if (serviceInfo != null) {
            ClientConfig clientConfig = get().initProcess(serviceInfo.packageName, serviceInfo.processName, userId);
            IServiceConnection conn = ServiceConnectionProxy.getDispatcher(context, connection, flags);
            Intent intent = IntentBuilder.createBindProxyServiceIntent(clientConfig.vpid, clientConfig.isExt, serviceInfo, service, flags, userId, conn);
            return context.bindService(intent, connection, flags);
         } else {
            return false;
         }
      }
   }

   public void unbindService(Context context, ServiceConnection connection) {
      connection = this.removeDelegate(connection);
      context.unbindService(connection);
   }

   public VParceledListSlice getServices(String pkg, int maxNum, int flags) {
      try {
         return this.getService().getServices(pkg, maxNum, flags, VUserHandle.myUserId());
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (VParceledListSlice)VirtualRuntime.crash(e);
      }
   }

   public boolean broadcastFinish(IBinder token) {
      try {
         return this.getService().broadcastFinish(token);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public int getAppPid(String pkg, int userId, String proccessName) {
      try {
         return this.getService().getAppPid(pkg, userId, proccessName);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
         return -1;
      }
   }

   public final void setSettingsProvider(int tableIndex, String arg, String value) {
      try {
         this.getService().setSettingsProvider(VUserHandle.myUserId(), tableIndex, arg, value);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
      }

   }

   public final String getSettingsProvider(int tableIndex, String arg) {
      try {
         return this.getService().getSettingsProvider(VUserHandle.myUserId(), tableIndex, arg);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         e.printStackTrace();
         return "";
      }
   }

   private static class ServiceConnectionDelegate implements ServiceConnection {
      private ServiceConnection mBase;

      public ServiceConnectionDelegate(ServiceConnection base) {
         this.mBase = base;
      }

      public void onServiceConnected(ComponentName name, IBinder service) {
         IBinderProxyService proxy = IBinderProxyService.Stub.asInterface(service);
         if (proxy != null) {
            try {
               this.mBase.onServiceConnected(proxy.getComponent(), proxy.getService());
            } catch (RemoteException var5) {
               RemoteException e = var5;
               e.printStackTrace();
            }
         } else {
            this.mBase.onServiceConnected(name, service);
         }

      }

      public void onServiceDisconnected(ComponentName name) {
         this.mBase.onServiceDisconnected(name);
      }
   }
}
