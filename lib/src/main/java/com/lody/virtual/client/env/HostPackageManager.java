package com.lody.virtual.client.env;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;
import android.os.Build.VERSION;
import androidx.annotation.RequiresApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.os.VUserHandle;
import mirror.android.app.ActivityThread;

public abstract class HostPackageManager {
   private static HostPackageManager sInstance;

   public static HostPackageManager init() {
      sInstance = new HostPackageManagerImpl();
      return sInstance;
   }

   public static HostPackageManager get() {
      return sInstance;
   }

   public abstract PackageInfo getPackageInfo(String var1, long var2) throws PackageManager.NameNotFoundException;

   public abstract ApplicationInfo getApplicationInfo(String var1, long var2) throws PackageManager.NameNotFoundException;

   public abstract ResolveInfo resolveActivity(Intent var1, long var2);

   public abstract ProviderInfo resolveContentProvider(String var1, long var2);

   public abstract String[] getPackagesForUid(int var1);

   public abstract int checkPermission(String var1, String var2);

   private static class HostPackageManagerImpl extends HostPackageManager {
      private IPackageManager mService;
      private static Boolean sIsAndroid13Beta = null;

      public HostPackageManagerImpl() {
         this.mService = (IPackageManager)ActivityThread.getPackageManager.call();
      }

      public static boolean isAndroid13AndUp() {
         if (sIsAndroid13Beta == null) {
            if (VERSION.SDK_INT >= 31) {
               sIsAndroid13Beta = VERSION.SDK_INT >= 33;
               if (!sIsAndroid13Beta) {
                  try {
                     IPackageManager.class.getMethod("getPackageInfo", String.class, Long.TYPE, Integer.TYPE);
                     sIsAndroid13Beta = true;
                  } catch (NoSuchMethodException var1) {
                     sIsAndroid13Beta = false;
                  }
               }
            } else {
               sIsAndroid13Beta = false;
            }
         }

         return sIsAndroid13Beta;
      }

      @RequiresApi(
         api = 33
      )
      public PackageInfo getPackageInfo(String packageName, long flags) throws PackageManager.NameNotFoundException {
         PackageInfo info;
         try {
            if (BuildCompat.isTiramisu()) {
               info = this.mService.getPackageInfo(packageName, flags, VUserHandle.realUserId());
            } else {
               info = this.mService.getPackageInfo(packageName, (int)flags, VUserHandle.realUserId());
            }
         } catch (RemoteException var6) {
            RemoteException e = var6;
            throw new RuntimeException(e);
         }

         if (info == null) {
            throw new PackageManager.NameNotFoundException(packageName);
         } else {
            return info;
         }
      }

      public ApplicationInfo getApplicationInfo(String packageName, long flags) throws PackageManager.NameNotFoundException {
         ApplicationInfo info;
         try {
            if (BuildCompat.isTiramisu()) {
               info = this.mService.getApplicationInfo(packageName, flags, VUserHandle.realUserId());
            } else {
               info = this.mService.getApplicationInfo(packageName, (int)flags, VUserHandle.realUserId());
            }
         } catch (RemoteException var6) {
            RemoteException e = var6;
            throw new RuntimeException(e);
         }

         if (info == null) {
            throw new PackageManager.NameNotFoundException(packageName);
         } else {
            return info;
         }
      }

      public ResolveInfo resolveActivity(Intent intent, long flags) {
         try {
            return BuildCompat.isTiramisu() ? this.mService.resolveIntent(intent, (String)null, flags, VUserHandle.realUserId()) : this.mService.resolveIntent(intent, (String)null, (int)flags, VUserHandle.realUserId());
         } catch (RemoteException var5) {
            RemoteException e = var5;
            throw new RuntimeException(e);
         }
      }

      public ProviderInfo resolveContentProvider(String authority, long flags) {
         try {
            return BuildCompat.isTiramisu() ? this.mService.resolveContentProvider(authority, flags, VUserHandle.realUserId()) : this.mService.resolveContentProvider(authority, (int)flags, VUserHandle.realUserId());
         } catch (RemoteException var5) {
            RemoteException e = var5;
            throw new RuntimeException(e);
         }
      }

      public String[] getPackagesForUid(int uid) {
         try {
            return this.mService.getPackagesForUid(uid);
         } catch (RemoteException var3) {
            RemoteException e = var3;
            throw new RuntimeException(e);
         }
      }

      public int checkPermission(String permName, String pkgName) {
         try {
            return this.mService.checkPermission(permName, pkgName, VUserHandle.realUserId());
         } catch (RemoteException var4) {
            RemoteException e = var4;
            throw new RuntimeException(e);
         }
      }
   }
}
