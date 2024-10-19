package com.lody.virtual.server.pm;

import android.util.ArrayMap;
import com.lody.virtual.server.pm.parser.PackageParserEx;
import com.lody.virtual.server.pm.parser.VPackage;

public class PackageCacheManager {
   static final ArrayMap<String, VPackage> PACKAGE_CACHE = new ArrayMap();

   public static int size() {
      synchronized(PACKAGE_CACHE) {
         return PACKAGE_CACHE.size();
      }
   }

   public static boolean contain(String packageName) {
      synchronized(PACKAGE_CACHE) {
         return PACKAGE_CACHE.containsKey(packageName);
      }
   }

   public static void put(VPackage pkg, PackageSetting ps) {
      synchronized(PACKAGE_CACHE) {
         VPackage existOne = (VPackage)PACKAGE_CACHE.remove(pkg.packageName);
         if (existOne != null) {
            VPackageManagerService.get().deletePackageLocked(existOne);
         }

         PackageParserEx.initApplicationInfoBase(ps, pkg);
         PACKAGE_CACHE.put(pkg.packageName, pkg);
         pkg.mExtras = ps;
         VPackageManagerService.get().analyzePackageLocked(pkg);
      }
   }

   public static VPackage get(String packageName) {
      synchronized(PACKAGE_CACHE) {
         return (VPackage)PACKAGE_CACHE.get(packageName);
      }
   }

   public static PackageSetting getSetting(String packageName) {
      synchronized(PACKAGE_CACHE) {
         VPackage p = (VPackage)PACKAGE_CACHE.get(packageName);
         return p != null ? (PackageSetting)p.mExtras : null;
      }
   }

   public static PackageSetting getSettingLocked(String packageName) {
      VPackage p = (VPackage)PACKAGE_CACHE.get(packageName);
      return p != null ? (PackageSetting)p.mExtras : null;
   }

   public static VPackage remove(String packageName) {
      synchronized(PACKAGE_CACHE) {
         VPackage pkg = (VPackage)PACKAGE_CACHE.remove(packageName);
         if (pkg != null) {
            VPackageManagerService.get().deletePackageLocked(pkg);
         }

         return pkg;
      }
   }
}
