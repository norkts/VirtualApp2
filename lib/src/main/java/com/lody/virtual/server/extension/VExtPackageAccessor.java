package com.lody.virtual.server.extension;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import com.lody.virtual.IExtHelperInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.ProviderCall;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.IPCHelper;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.helper.utils.VLog;
import java.util.Collections;
import java.util.List;

public class VExtPackageAccessor {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITwuIGwIIDd9JA47KC0ME24FAitsJDAcKS5SVg=="));
   private static IPCHelper<IExtHelperInterface> sHelper = new IPCHelper<IExtHelperInterface>() {
      public IExtHelperInterface getInterface() {
         Context context = VirtualCore.get().getContext();

         for(int i = 0; i < 3; ++i) {
            Bundle response = (new ProviderCall.Builder(context, VExtPackageAccessor.getAuthority())).methodName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGojNClmEVRF"))).callSafely();
            if (response != null) {
               IBinder binder = BundleCompat.getBinder(response, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh99NAY2KBcMKGMFSFo=")));
               return IExtHelperInterface.Stub.asInterface(binder);
            }

            VExtPackageAccessor.tryPullUpExtProcess();
            SystemClock.sleep(200L);
         }

         return null;
      }
   };

   private static Intent getLaunchIntentForPackage(PackageManager pm, String packageName) {
      Intent intentToResolve = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42TQ5iDB4CIjsuBmMLHg5gHApF")));
      intentToResolve.setPackage(packageName);
      List<ResolveInfo> ris = pm.queryIntentActivities(intentToResolve, 0);
      if (ris.size() <= 0) {
         return null;
      } else {
         Intent intent = new Intent(intentToResolve);
         intent.setFlags(268435456);
         intent.setFlags(65536);
         intent.setClassName(((ResolveInfo)ris.get(0)).activityInfo.packageName, ((ResolveInfo)ris.get(0)).activityInfo.name);
         return intent;
      }
   }

   private static void tryPullUpExtProcess() {
      Context context = VirtualCore.get().getContext();
      Intent intent = getLaunchIntentForPackage(context.getPackageManager(), StubManifest.EXT_PACKAGE_NAME);
      if (intent != null) {
         intent.addFlags(65536);
         intent.addFlags(268435456);
         context.startActivity(intent);
      }

   }

   public static boolean hasExtPackageBootPermission() {
      if (!VirtualCore.get().isExtPackageInstalled()) {
         return false;
      } else if (callHelper()) {
         return true;
      } else {
         tryPullUpExtProcess();

         for(int i = 0; i < 5; ++i) {
            if (callHelper()) {
               return true;
            }

            SystemClock.sleep(200L);
         }

         return false;
      }
   }

   public static boolean callHelper() {
      try {
         (new ProviderCall.Builder(VirtualCore.get().getContext(), getAuthority())).methodName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JhhSVg=="))).retry(0).call();
      } catch (IllegalAccessException var2) {
         VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+DmoLRStgESQ/IzkmLGwgRQZvATgdPC02J2EzICxpJA0dLy0MCmEjRT9qDjw0JzkiLWkKBgRpJx05MzocVg==")));
         return false;
      }

      try {
         (new ProviderCall.Builder(VirtualCore.get().getContext(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYD2UjOAZqATg7KQQcMmMFMD9qDiQbJQcYCm8FFhNoHlkZKioiVg==")))).methodName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JhhSVg=="))).retry(0).call();
         return true;
      } catch (IllegalAccessException var1) {
         IllegalAccessException e = var1;
         Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+DmoLRStgESQ/IzkmLGwgRQZvATgdID02CmYKMF9uAQY9JQNWJGszJAVqASgzCDleJXszSFo=")) + e);
         return false;
      }
   }

   private static String getAuthority() {
      return VirtualCore.getConfig().getExtPackageHelperAuthority();
   }

   public static void syncPackages() {
      if (VirtualCore.get().isExtPackageInstalled()) {
         sHelper.callVoid(new IPCHelper.CallableVoid<IExtHelperInterface>() {
            public void call(IExtHelperInterface service) throws RemoteException {
               service.syncPackages();
            }
         });
      }
   }

   public static void cleanPackageData(final int[] userIds, final String packageName) {
      if (VirtualCore.get().isExtPackageInstalled()) {
         sHelper.callVoid(new IPCHelper.CallableVoid<IExtHelperInterface>() {
            public void call(IExtHelperInterface service) throws RemoteException {
               service.cleanPackageData(userIds, packageName);
            }
         });
      }
   }

   public static void forceStop(final int[] pids) {
      sHelper.callVoid(new IPCHelper.CallableVoid<IExtHelperInterface>() {
         public void call(IExtHelperInterface service) throws RemoteException {
            service.forceStop(pids);
         }
      });
   }

   public static List<ActivityManager.RunningTaskInfo> getRunningTasks(final int maxNum) {
      if (!VirtualCore.get().isExtPackageInstalled()) {
         return Collections.emptyList();
      } else {
         List<ActivityManager.RunningTaskInfo> res = (List)sHelper.call(new IPCHelper.Callable<IExtHelperInterface, List<ActivityManager.RunningTaskInfo>>() {
            public List<ActivityManager.RunningTaskInfo> call(IExtHelperInterface service) throws RemoteException {
               return service.getRunningTasks(maxNum);
            }
         });
         if (res == null) {
            res = Collections.emptyList();
         }

         return res;
      }
   }

   public static List<ActivityManager.RecentTaskInfo> getRecentTasks(final int maxNum, final int flags) {
      if (!VirtualCore.get().isExtPackageInstalled()) {
         return Collections.emptyList();
      } else {
         List<ActivityManager.RecentTaskInfo> res = (List)sHelper.call(new IPCHelper.Callable<IExtHelperInterface, List<ActivityManager.RecentTaskInfo>>() {
            public List<ActivityManager.RecentTaskInfo> call(IExtHelperInterface service) throws RemoteException {
               return service.getRecentTasks(maxNum, flags);
            }
         });
         if (res == null) {
            res = Collections.emptyList();
         }

         return res;
      }
   }

   public static List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() {
      if (!VirtualCore.get().isExtPackageInstalled()) {
         return Collections.emptyList();
      } else {
         List<ActivityManager.RunningAppProcessInfo> res = (List)sHelper.call(new IPCHelper.Callable<IExtHelperInterface, List<ActivityManager.RunningAppProcessInfo>>() {
            public List<ActivityManager.RunningAppProcessInfo> call(IExtHelperInterface service) throws RemoteException {
               return service.getRunningAppProcesses();
            }
         });
         if (res == null) {
            res = Collections.emptyList();
         }

         return res;
      }
   }

   public static boolean isExternalStorageManager() {
      return !VirtualCore.get().isExtPackageInstalled() ? true : sHelper.callBoolean(new IPCHelper.Callable<IExtHelperInterface, Boolean>() {
         public Boolean call(IExtHelperInterface service) throws RemoteException {
            return service.isExternalStorageManager();
         }
      });
   }

   /** @deprecated */
   @Deprecated
   public static void startActivity(final Intent intent, final Bundle options) {
      if (!VirtualCore.get().isExtPackageInstalled()) {
         sHelper.callVoid(new IPCHelper.CallableVoid<IExtHelperInterface>() {
            public void call(IExtHelperInterface service) throws RemoteException {
               service.startActivity(intent, options);
            }
         });
      }

   }
}
