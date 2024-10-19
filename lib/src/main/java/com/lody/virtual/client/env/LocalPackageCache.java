package com.lody.virtual.client.env;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.ComponentUtils;
import java.util.HashMap;
import java.util.Map;

public class LocalPackageCache {
   private static final Map<String, Boolean> sSystemPackages = new HashMap();

   public static void init() {
      HostPackageManager pm = VirtualCore.get().getHostPackageManager();
      String[] pkgs = pm.getPackagesForUid(1000);
      if (pkgs != null) {
         String[] var2 = pkgs;
         int var3 = pkgs.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String pkg = var2[var4];
            sSystemPackages.put(pkg, !VirtualCore.get().isAppInstalled(pkg));
         }
      }

   }

   public static boolean isSystemPackage(String pkg) {
      synchronized(sSystemPackages) {
         Boolean isSystemPkg = (Boolean)sSystemPackages.get(pkg);
         if (isSystemPkg == null) {
            try {
               if (VirtualCore.get().isAppInstalled(pkg)) {
                  isSystemPkg = false;
               } else {
                  ApplicationInfo info = VirtualCore.get().getHostPackageManager().getApplicationInfo(pkg, 0L);
                  isSystemPkg = ComponentUtils.isSystemApp(info);
               }
            } catch (PackageManager.NameNotFoundException var5) {
               isSystemPkg = false;
            }

            sSystemPackages.put(pkg, isSystemPkg);
         }

         return isSystemPkg;
      }
   }

   public static boolean isOutsideVisiblePackage(String pkg) {
      if (pkg == null) {
         return false;
      } else {
         SettingConfig config = VirtualCore.getConfig();
         return pkg.equals(config.getMainPackageName()) || pkg.equals(config.getExtPackageName()) || config.isOutsidePackage(pkg) || isSystemPackage(pkg);
      }
   }
}
