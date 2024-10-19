package com.kook.deviceinfo.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kook.common.utils.HVLog;

public class AppInfoHelper {
   private static final String TAG = "AppInfoHelper";

   public static void printAppInfo(Context context) {
      try {
         PackageManager packageManager = context.getPackageManager();
         String packageName = context.getPackageName();
         ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 128);
         String appName = packageManager.getApplicationLabel(appInfo).toString();
         PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
         String appVersionName = packageInfo.versionName;
         int appVersionCode = packageInfo.versionCode;
         long installTime = packageInfo.firstInstallTime;
         boolean isSystem = (appInfo.flags & 1) != 0;
         String packageSign = getSignature(packageManager, packageName);
         packageName = packageInfo.packageName;
         int minSdkVersion = appInfo.targetSdkVersion;
         HVLog.d("App Name: " + appName);
         HVLog.d("App Version Name: " + appVersionName);
         HVLog.d("App Version Code: " + appVersionCode);
         HVLog.d("Installation Time: " + installTime);
         HVLog.d("Is System App: " + isSystem);
         HVLog.d("Package Name: " + packageName);
         HVLog.d("Minimum SDK Version: " + minSdkVersion);
         HVLog.d("Package Signature: " + packageSign);
      } catch (PackageManager.NameNotFoundException var13) {
         PackageManager.NameNotFoundException e = var13;
         HVLog.d("Error getting app info: " + e.getMessage());
      }

   }

   private static String getSignature(PackageManager packageManager, String packageName) {
      try {
         PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 64);
         if (packageInfo.signatures.length > 0) {
            JSONArray jsonArray = new JSONArray();
            Signature[] var4 = packageInfo.signatures;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Signature signature = var4[var6];
               jsonArray.add(signature.toCharsString());
            }

            return jsonArray.toJSONString();
         }
      } catch (PackageManager.NameNotFoundException var8) {
         PackageManager.NameNotFoundException e = var8;
         HVLog.d("Error getting package signature: " + e.getMessage());
      }

      return null;
   }

   public static void getAppInfo(Context context, String packageName) {
      try {
         PackageManager packageManager = context.getPackageManager();
         ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
         PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
         String appName = packageManager.getApplicationLabel(applicationInfo).toString();
         int appVersionCode = packageInfo.versionCode;
         String appVersionName = packageInfo.versionName;
         long installTime = packageInfo.firstInstallTime;
         boolean isSystem = (applicationInfo.flags & 1) != 0;
         int minSdkVersion = applicationInfo.targetSdkVersion;
         getSignature(context.getPackageManager(), packageName);
         int flag = applicationInfo.flags;
         long updateTime = packageInfo.lastUpdateTime;
         JSONObject jsonObject = new JSONObject();
         jsonObject.put("appName", appName);
         jsonObject.put("appVersionCode", appVersionCode);
         jsonObject.put("appVersionName", appVersionName);
         jsonObject.put("flag", flag);
         jsonObject.put("installTime", installTime);
         jsonObject.put("isSystem", isSystem);
         jsonObject.put("minSdkVersion", minSdkVersion);
         jsonObject.put("packageName", packageName);
         jsonObject.put("updateTime", updateTime);
         getSignature(packageManager, packageName);
         String var16 = jsonObject.toJSONString();
      } catch (PackageManager.NameNotFoundException var17) {
         PackageManager.NameNotFoundException e = var17;
         HVLog.d("Package not found: " + e.getMessage());
      }

   }

   public static String getJsonToAppInfo(Context context, String packageName) {
      try {
         PackageManager packageManager = context.getPackageManager();
         ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
         PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
         String appName = packageManager.getApplicationLabel(applicationInfo).toString();
         int appVersionCode = packageInfo.versionCode;
         String appVersionName = packageInfo.versionName;
         long installTime = packageInfo.firstInstallTime;
         boolean isSystem = (applicationInfo.flags & 1) != 0;
         int minSdkVersion = applicationInfo.targetSdkVersion;
         int flag = applicationInfo.flags;
         long updateTime = packageInfo.lastUpdateTime;
         JSONObject jsonObject = new JSONObject();
         jsonObject.put("appName", appName);
         jsonObject.put("appVersionCode", appVersionCode);
         jsonObject.put("appVersionName", appVersionName);
         jsonObject.put("flag", flag);
         jsonObject.put("installTime", installTime);
         jsonObject.put("isSystem", isSystem);
         jsonObject.put("minSdkVersion", minSdkVersion);
         jsonObject.put("packageName", packageName);
         jsonObject.put("updateTime", updateTime);
         String signature = getSignature(packageManager, packageName);
         jsonObject.put("signature", signature);
         String jsonString = jsonObject.toJSONString();
         return jsonString;
      } catch (PackageManager.NameNotFoundException var18) {
         PackageManager.NameNotFoundException e = var18;
         HVLog.d("Package not found: " + e.getMessage());
         return null;
      }
   }
}
