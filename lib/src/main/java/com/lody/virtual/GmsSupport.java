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
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")) + GmsSupport.class.getSimpleName();
   private static final HashSet<String> GOOGLE_APP = new HashSet();
   private static final HashSet<String> GOOGLE_SERVICE = new HashSet();
   public static final String GMS_PKG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mL2EjSFo="));
   public static final String GSF_PKG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mD2IzSFo="));
   public static final String VENDING_PKG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmUVGiZrER4bLj5SVg=="));
   public static final String GAMES_PKG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC06KH0FBSluJCQcLy0YVg=="));
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
      VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl84Mx8tDCACNgg+QBwkMRoXNyhrNx4dLhc1JHczSFo=")) + files.length + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFPCNhIgozIzoIVg==")) + gmsDir.getAbsolutePath());
      if (files != null) {
         File[] var4 = files;
         int var5 = files.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File file = var4[var6];
            if (file.getName().endsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4+KGUzSFo=")))) {
               String apkPath = file.getPath();
               VLog.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6MWcFJAZjVgJF")) + apkPath);
               VAppInstallerParams params = new VAppInstallerParams(2);
               VAppInstallerResult result = core.installPackage(Uri.fromFile(file), params);
               if (result.status == 0) {
                  VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcKWwFJCRgVyQ9KggpOmozJC14HjAwLT42J2EjNz0=")) + apkPath);
               } else {
                  VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcKWwFJCRgVyQ9KggpOmozJC14ESQsIxgDPg==")) + apkPath + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MxguKm8jGgRLVgU8")) + result.status);
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
         VAppInstallerResult result = core.installPackage(Uri.parse(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iDQI5Ki1WDmkFNCVrJwIgPC4+KmIVMCpvDjMdLwcYIg=="))), params);
         VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcKWwFJCRgVyQ9Iy09OmoVGgNvAQI/Py5SVg==")) + result.status);
         result = core.installPackage(Uri.parse(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iDQI5Ki1WDmkFNCVrJwIgPC4+KmIVMCpvDjMdLwgmDw=="))), params);
         VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcKWwFJCRgVyQ9KggpOmoVGgNvAQI/Py5SVg==")) + result.status);
         result = core.installPackage(Uri.parse(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iDQI5Ki1WDm4jMCxsNwYaLgQcMmIKRS9vDh4g"))), params);
         VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcKWwFJCRgVyQuKAcYPmwjMC14HgogKT0uKGZSHlo=")) + result.status);
         core.installPackage(Uri.parse(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iDQI5Ki1WDmkFNCVrJwIgPC4+KmIVMCpvDjMdLwgmVg=="))), params);
         core.installPackage(Uri.parse(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iDQI5Ki1WDmkFNCVrJwIgPC4+KmIVMCpvDjMdLi4uCW8aNDZoEVRF"))), params);
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
      VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OGAxEl5sJSQXICoIVg==")) + VirtualCore.get().isAppInstalled(GMS_PKG));
      VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OGA2LAhsJSQXICoIVg==")) + VirtualCore.get().isAppInstalled(GSF_PKG));
      VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OGQhNABqHAYMICxfDGQLHTI=")) + VirtualCore.get().isAppInstalled(VENDING_PKG));
      VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OGAxJA1qDyhAOxUEWHgVSFo=")) + VirtualCore.get().isAppInstalled(GAMES_PKG));
      return VirtualCore.get().isAppInstalled(GMS_PKG) && VirtualCore.get().isAppInstalled(GSF_PKG) && VirtualCore.get().isAppInstalled(VENDING_PKG) && VirtualCore.get().isAppInstalled(GAMES_PKG);
   }

   static {
      GOOGLE_APP.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmUVGiZrER4bLj5SVg==")));
      GOOGLE_APP.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC06KH0FBSluJCQcLy0YVg==")));
      GOOGLE_APP.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC0mJ30FMCRpNF0uORhbDmwVSFo=")));
      GOOGLE_APP.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC0mJ30FMCRpNF0uORhbDmxSBjVqJ1RF")));
      GOOGLE_SERVICE.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mL2EjSFo=")));
      GOOGLE_SERVICE.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mD2IzSFo=")));
      GOOGLE_SERVICE.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mD2I0RTdsJDwiKRhSVg==")));
      GOOGLE_SERVICE.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4MO30gEjBlETA7KC4qD2wVGiZsAVRF")));
      GOOGLE_SERVICE.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4MO30gEjBlEVRF")));
      GOOGLE_SERVICE.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC42KWAwICxuJwo5Lz5bCmsKFlo=")));
      GOOGLE_SERVICE.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC02M2AwNCRuHiQ5Iz4ACGwnBjVqNxoZOwcuM2UjSFo=")));
      GOOGLE_SERVICE.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4iJ2IKFiVpDiwa")));
      GOOGLE_SERVICE.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4AKmIFFixsDgoiKRg2CmoFJAJlHlE0Jz5SVg==")));
      GOOGLE_SERVICE.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC06O2EzFiluASg8Ly0cCWwVSFo=")));
      GOOGLE_SERVICE.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC02J2YVLDNqJBozKC1fIA==")));
      GOOGLE_SERVICE.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC02M2AwNCRuHiQ5Iz4ACGwnBjVrEQI0Jj0MKGUzSFo=")));
   }
}
