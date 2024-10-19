package com.lody.virtual.client.fixer;

import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.text.TextUtils;

public class ComponentFixer {
   public static String fixComponentClassName(String pkgName, String className) {
      if (className != null) {
         return className.charAt(0) == '.' ? pkgName + className : className;
      } else {
         return null;
      }
   }

   public static void fixComponentInfo(ComponentInfo info) {
      if (info != null) {
         if (TextUtils.isEmpty(info.processName)) {
            info.processName = info.packageName;
         }

         info.name = fixComponentClassName(info.packageName, info.name);
         if (info.processName == null) {
            info.processName = info.applicationInfo.processName;
         }
      }

   }

   public static void fixOutsideComponentInfo(ComponentInfo info) {
      if (info != null) {
         fixOutsideApplicationInfo(info.applicationInfo);
      }

   }

   public static void fixOutsideApplicationInfo(ApplicationInfo info) {
      if (info != null) {
         info.uid = 9000;
      }

   }
}
