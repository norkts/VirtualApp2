package com.lody.virtual;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.HostPackageManager;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.VAppInstallerParams;
import com.lody.virtual.remote.VAppInstallerResult;
import java.io.File;
import java.util.HashSet;

public class GmsSupport {
   private static final String TAG = "HV-" + GmsSupport.class.getSimpleName();
   private static final HashSet<String> GOOGLE_APP = new HashSet();
   private static final HashSet<String> GOOGLE_SERVICE = new HashSet();
   public static final String GMS_PKG = "com.google.android.gms";
   public static final String GSF_PKG = "com.google.android.gsf";
   public static final String VENDING_PKG = "com.android.vending";
   public static final String GAMES_PKG = "com.google.android.play.games";
   public static final HashSet<String> PERMISSION_FORCE_GRANT = new HashSet();

   public static boolean isGoogleFrameworkInstalled() {
      return VirtualCore.get().isAppInstalled(GMS_PKG);
   }

   public static boolean isGoogleService(String packageName) {
      return GOOGLE_SERVICE.contains(packageName);
   }

   public static boolean isGoogleAppOrService(String str) {
      return GOOGLE_APP.contains(str) || GOOGLE_SERVICE.contains(str);
   }

   public static boolean isOutsideGoogleFrameworkExist() {
      return VirtualCore.get().isOutsideInstalled(GMS_PKG) && VirtualCore.get().isOutsideInstalled(GSF_PKG);
   }

   private static void installPackages(File gmsDir, int userId) {
      VirtualCore core = VirtualCore.get();
      File[] files = gmsDir.listFiles();
      VLog.d("HV-", "  安装数量 files :" + files.length + "    gmsDir:" + gmsDir.getAbsolutePath());
      if (files != null) {
         File[] var4 = files;
         int var5 = files.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File file = var4[var6];
            if (file.getName().endsWith(".apk")) {
               String apkPath = file.getPath();
               VLog.d(TAG, "apkPath:" + apkPath);
               VAppInstallerParams params = new VAppInstallerParams(2);
               VAppInstallerResult result = core.installPackage(Uri.fromFile(file), params);
               if (result.status == 0) {
                  VLog.w(TAG, "install gms pkg success:" + apkPath);
               } else {
                  VLog.w(TAG, "install gms pkg fail:" + apkPath + ",error : " + result.status);
               }
            }
         }
      }

   }

   public static boolean isOutsideSupportGms() {
      HostPackageManager hostPM = VirtualCore.get().getHostPackageManager();
      ApplicationInfo gmsAppInfo = null;
      ApplicationInfo gsfAppInfo = null;

      PackageManager.NameNotFoundException e;
      try {
         gmsAppInfo = hostPM.getApplicationInfo(GMS_PKG, 0L);
      } catch (PackageManager.NameNotFoundException var5) {
         e = var5;
         e.printStackTrace();
      }

      if (gmsAppInfo == null) {
         return false;
      } else {
         try {
            gsfAppInfo = hostPM.getApplicationInfo(GSF_PKG, 0L);
         } catch (PackageManager.NameNotFoundException var4) {
            e = var4;
            e.printStackTrace();
         }

         return gsfAppInfo != null;
      }
   }

   public static void installGApps(File gmsDir, int userId) {
      installPackages(gmsDir, userId);
   }

   public static void installDynamicGms(int userId) {
      VirtualCore core = VirtualCore.get();
      if (userId == 0) {
         VAppInstallerParams params = new VAppInstallerParams(2);
         VAppInstallerResult result = core.installPackage(Uri.parse("package:com.google.android.gsf"), params);
         VLog.w(TAG, "install gsf result:" + result.status);
         result = core.installPackage(Uri.parse("package:com.google.android.gms"), params);
         VLog.w(TAG, "install gms result:" + result.status);
         result = core.installPackage(Uri.parse("package:com.android.vending"), params);
         VLog.w(TAG, "install vending result:" + result.status);
         core.installPackage(Uri.parse("package:com.google.android.gm"), params);
         core.installPackage(Uri.parse("package:com.google.android.youtube"), params);
      } else {
         core.installPackageAsUser(userId, GMS_PKG);
         core.installPackageAsUser(userId, GSF_PKG);
         core.installPackageAsUser(userId, VENDING_PKG);
      }

   }

   public static void remove(String packageName) {
      GOOGLE_SERVICE.remove(packageName);
      GOOGLE_APP.remove(packageName);
   }

   public static boolean isInstalledGoogleService() {
      VLog.d("HV-", "   GMS_PKG:" + VirtualCore.get().isAppInstalled(GMS_PKG));
      VLog.d("HV-", "   GSF_PKG:" + VirtualCore.get().isAppInstalled(GSF_PKG));
      VLog.d("HV-", "   VENDING_PKG:" + VirtualCore.get().isAppInstalled(VENDING_PKG));
      VLog.d("HV-", "   GAMES_PKG:" + VirtualCore.get().isAppInstalled(GAMES_PKG));
      return VirtualCore.get().isAppInstalled(GMS_PKG) && VirtualCore.get().isAppInstalled(GSF_PKG) && VirtualCore.get().isAppInstalled(VENDING_PKG) && VirtualCore.get().isAppInstalled(GAMES_PKG);
   }

   static {
      GOOGLE_APP.add("com.android.vending");
      GOOGLE_APP.add("com.google.android.play.games");
      GOOGLE_APP.add("com.google.android.wearable.app");
      GOOGLE_APP.add("com.google.android.wearable.app.cn");
      GOOGLE_SERVICE.add("com.google.android.gms");
      GOOGLE_SERVICE.add("com.google.android.gsf");
      GOOGLE_SERVICE.add("com.google.android.gsf.login");
      GOOGLE_SERVICE.add("com.google.android.backuptransport");
      GOOGLE_SERVICE.add("com.google.android.backup");
      GOOGLE_SERVICE.add("com.google.android.configupdater");
      GOOGLE_SERVICE.add("com.google.android.syncadapters.contacts");
      GOOGLE_SERVICE.add("com.google.android.feedback");
      GOOGLE_SERVICE.add("com.google.android.onetimeinitializer");
      GOOGLE_SERVICE.add("com.google.android.partnersetup");
      GOOGLE_SERVICE.add("com.google.android.setupwizard");
      GOOGLE_SERVICE.add("com.google.android.syncadapters.calendar");
   }
}
