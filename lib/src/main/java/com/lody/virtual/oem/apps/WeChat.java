package com.lody.virtual.oem.apps;

import android.app.Application;
import com.lody.virtual.StringFog;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class WeChat {
   private static final String PKG_MM = "com.tencent.mm";
   private static HashSet<String> sBinderMapClsNameSet;

   private static void cleanMap(String clsName, Application application) {
      try {
         Field[] var2 = Class.forName(clsName, true, application.getClassLoader()).getDeclaredFields();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            field.setAccessible(true);
            if (HashMap.class.isAssignableFrom(field.getType())) {
               HashMap<String, Object> map = (HashMap)field.get((Object)null);
               if (map.get("package") != null && map.get("device_identifiers") != null && map.get("iphonesubinfo") != null) {
                  map.clear();
                  return;
               }
            }
         }
      } catch (Throwable var7) {
      }

   }

   public static void disableBinderHook(String pkg, Application application) {
      if (PKG_MM.equals(pkg)) {
         Iterator<String> it = sBinderMapClsNameSet.iterator();

         while(it.hasNext()) {
            cleanMap((String)it.next(), application);
         }
      }

   }

   static {
      HashSet<String> hashSet = new HashSet();
      sBinderMapClsNameSet = hashSet;
      hashSet.add("com.tencent.mm.sensitive.l");
      sBinderMapClsNameSet.add("com.tencent.mm.sensitive.m");
   }
}
