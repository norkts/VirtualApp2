package com.lody.virtual.server;

import android.app.ActivityManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.Build.VERSION;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.stub.KeepAliveService;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.helper.compat.NotificationChannelCompat;
import com.lody.virtual.server.accounts.VAccountManagerService;
import com.lody.virtual.server.am.VActivityManagerService;
import com.lody.virtual.server.content.VContentService;
import com.lody.virtual.server.device.VDeviceManagerService;
import com.lody.virtual.server.fs.FileTransfer;
import com.lody.virtual.server.interfaces.IServiceFetcher;
import com.lody.virtual.server.job.VJobSchedulerService;
import com.lody.virtual.server.location.VirtualLocationService;
import com.lody.virtual.server.notification.VNotificationManagerService;
import com.lody.virtual.server.pm.VAppManagerService;
import com.lody.virtual.server.pm.VPackageManagerService;
import com.lody.virtual.server.pm.VUserManagerService;
import com.lody.virtual.server.vs.VirtualStorageService;
import java.util.Iterator;

public final class BinderProvider extends ContentProvider {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jj4YCGgFNARpESw1LD0cPmkgRVo="));
   private final ServiceFetcher mServiceFetcher = new ServiceFetcher();
   private static boolean sInitialized = false;
   public static boolean scanApps = true;

   public boolean onCreate() {
      return this.init();
   }

   private boolean init() {
      if (sInitialized) {
         return false;
      } else {
         Context context = this.getContext();
         if (context != null) {
            if (VERSION.SDK_INT >= 26) {
               NotificationChannelCompat.checkOrCreateChannel(context, NotificationChannelCompat.DAEMON_ID, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRg+M2oVGiY=")));
               NotificationChannelCompat.checkOrCreateChannel(context, NotificationChannelCompat.DEFAULT_ID, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguPmsaNCRmEVRF")));
            }

            try {
               context.startService(new Intent(context, KeepAliveService.class));
            } catch (Exception var3) {
               Exception e = var3;
               e.printStackTrace();
            }
         }

         if (!VirtualCore.get().isStartup()) {
            return false;
         } else {
            this.addService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YDmhSEgZhNCA2Iy0+PWoVSFo=")), FileTransfer.get());
            VPackageManagerService.systemReady();
            this.addService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iAVRF")), VPackageManagerService.get());
            this.addService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZF")), VActivityManagerService.get());
            this.addService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jSFo=")), VUserManagerService.get());
            VAppManagerService.systemReady();
            this.addService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KA==")), VAppManagerService.get());
            if (VERSION.SDK_INT >= 21) {
               this.addService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4AOg==")), VJobSchedulerService.get());
            }

            VNotificationManagerService.systemReady(context);
            this.addService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOC99JCAgKQdfDg==")), VNotificationManagerService.get());
            VContentService.systemReady();
            this.addService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEVRF")), VAccountManagerService.get());
            this.addService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmEVRF")), VContentService.get());
            this.addService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT02Vg==")), VirtualStorageService.get());
            this.addService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguLmUVLCs=")), VDeviceManagerService.get());
            this.addService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV10oKi0qVg==")), VirtualLocationService.get());
            this.killAllProcess();
            sInitialized = true;
            if (scanApps) {
               VAppManagerService.get().scanApps();
            }

            VAccountManagerService.systemReady();
            return true;
         }
      }
   }

   private void addService(String name, IBinder service) {
      ServiceCache.addService(name, service);
   }

   public Bundle call(String method, String arg, Bundle extras) {
      if (!sInitialized) {
         this.init();
      }

      if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JhhSVg==")).equals(method)) {
         Bundle bundle = new Bundle();
         BundleCompat.putBinder((Bundle)bundle, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh99NAY2KBcMKGMFSFo=")), this.mServiceFetcher);
         return bundle;
      } else {
         return null;
      }
   }

   public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
      return null;
   }

   public String getType(Uri uri) {
      return null;
   }

   public Uri insert(Uri uri, ContentValues values) {
      return null;
   }

   public int delete(Uri uri, String selection, String[] selectionArgs) {
      return 0;
   }

   public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
      return 0;
   }

   private void killAllProcess() {
      try {
         int uid = this.getContext().getPackageManager().getApplicationInfo(this.getContext().getPackageName(), 0).uid;
         String str = this.getContext().getPackageName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD06Vg=="));
         Iterator var3 = ((ActivityManager)this.getContext().getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZF")))).getRunningAppProcesses().iterator();

         while(var3.hasNext()) {
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = (ActivityManager.RunningAppProcessInfo)var3.next();
            if (runningAppProcessInfo.uid == uid && runningAppProcessInfo.processName.startsWith(str)) {
               Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggiLGgaEShhESw1LD0cPmkgRChsJCwsKS0pKGMgGjdsVyMpLD1fKWgjNCVvMFFF")) + runningAppProcessInfo.processName);
               Process.killProcess(runningAppProcessInfo.pid);
            }
         }
      } catch (Throwable var5) {
         Throwable th = var5;
         th.printStackTrace();
      }

   }

   private static class ServiceFetcher extends IServiceFetcher.Stub {
      private ServiceFetcher() {
      }

      public IBinder getService(String name) throws RemoteException {
         return name != null ? ServiceCache.getService(name) : null;
      }

      public void addService(String name, IBinder service) throws RemoteException {
         if (name != null && service != null) {
            ServiceCache.addService(name, service);
         }

      }

      public void removeService(String name) throws RemoteException {
         if (name != null) {
            ServiceCache.removeService(name);
         }

      }

      // $FF: synthetic method
      ServiceFetcher(Object x0) {
         this();
      }
   }
}
