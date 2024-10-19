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
   private static final String TAG = "BinderProvider";
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
               NotificationChannelCompat.checkOrCreateChannel(context, NotificationChannelCompat.DAEMON_ID, "daemon");
               NotificationChannelCompat.checkOrCreateChannel(context, NotificationChannelCompat.DEFAULT_ID, "default");
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
            this.addService("file-transfer", FileTransfer.get());
            VPackageManagerService.systemReady();
            this.addService("package", VPackageManagerService.get());
            this.addService("activity", VActivityManagerService.get());
            this.addService("user", VUserManagerService.get());
            VAppManagerService.systemReady();
            this.addService("app", VAppManagerService.get());
            if (VERSION.SDK_INT >= 21) {
               this.addService("job", VJobSchedulerService.get());
            }

            VNotificationManagerService.systemReady(context);
            this.addService("notification", VNotificationManagerService.get());
            VContentService.systemReady();
            this.addService("account", VAccountManagerService.get());
            this.addService("content", VContentService.get());
            this.addService("vs", VirtualStorageService.get());
            this.addService("device", VDeviceManagerService.get());
            this.addService("virtual-loc", VirtualLocationService.get());
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

      if ("@".equals(method)) {
         Bundle bundle = new Bundle();
         BundleCompat.putBinder((Bundle)bundle, "_VA_|_binder_", this.mServiceFetcher);
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
         String str = this.getContext().getPackageName() + ":p";
         Iterator var3 = ((ActivityManager)this.getContext().getSystemService("activity")).getRunningAppProcesses().iterator();

         while(var3.hasNext()) {
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = (ActivityManager.RunningAppProcessInfo)var3.next();
            if (runningAppProcessInfo.uid == uid && runningAppProcessInfo.processName.startsWith(str)) {
               Log.w(TAG, "after provider start,kill  process:" + runningAppProcessInfo.processName);
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
