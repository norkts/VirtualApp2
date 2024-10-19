package com.kook.common.systemutil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppInfo {
   public static int getVersionCode(Context context) {
      try {
         PackageManager packageManager = context.getPackageManager();
         PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
         int versionCode = packInfo.versionCode;
         return versionCode;
      } catch (PackageManager.NameNotFoundException var4) {
         PackageManager.NameNotFoundException e = var4;
         e.printStackTrace();
         return 0;
      }
   }

   public static int getVersionCode(Context context, String apkFilePath) {
      PackageManager packageManager = context.getPackageManager();
      PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkFilePath, 0);
      return packageInfo != null ? packageInfo.versionCode : -1;
   }

   public static String getVersionName(Context context) {
      try {
         PackageManager packageManager = context.getPackageManager();
         PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
         String versionName = packInfo.versionName;
         return versionName;
      } catch (PackageManager.NameNotFoundException var4) {
         PackageManager.NameNotFoundException e = var4;
         e.printStackTrace();
         return "";
      }
   }
}
