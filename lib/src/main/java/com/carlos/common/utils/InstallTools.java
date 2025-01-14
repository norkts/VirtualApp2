package com.carlos.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build.VERSION;
import androidx.core.content.FileProvider;
import com.carlos.libcommon.StringFog;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InstallTools {
   public static String TAG = "VA-InstallTools";

   public static long getInstallTimeByApk(Context context) {
      try {
         PackageManager packageManager = context.getApplicationContext().getPackageManager();
         PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
         long firstInstallTime = packageInfo.firstInstallTime;
         long lastUpdateTime = packageInfo.lastUpdateTime;
         return packageInfo.firstInstallTime;
      } catch (PackageManager.NameNotFoundException var7) {
         PackageManager.NameNotFoundException e = var7;
         e.printStackTrace();
         return 0L;
      }
   }

   public static boolean isInstallAppByPackageName(Context context, String packageName) {
      PackageManager packageManager = context.getPackageManager();
      List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
      List<String> packageNames = new ArrayList();
      if (packageInfos != null) {
         for(int i = 0; i < packageInfos.size(); ++i) {
            String packName = ((PackageInfo)packageInfos.get(i)).packageName;
            packageNames.add(packName);
         }
      }

      return packageNames.contains(packageName);
   }

   public long timeToStamp(String timers) {
      Date d = new Date();
      long timeStemp = 0L;

      try {
         SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
         d = sf.parse(timers);
      } catch (ParseException var6) {
         ParseException e = var6;
         e.printStackTrace();
      }

      timeStemp = d.getTime();
      return timeStemp;
   }

   public static int checkAPKProcess(File packageFile) {
      return 0;
   }

   public static void install(Context context, File file) {
      try {
         Intent intent = new Intent("android.intent.action.VIEW");
         intent.addFlags(268435456);
         intent.addFlags(1);
         Uri uri;
         if (VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, context.getPackageName().concat(".provider"), file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
         } else {
            uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
         }

         context.startActivity(intent);
      } catch (Exception var4) {
      }

   }
}
