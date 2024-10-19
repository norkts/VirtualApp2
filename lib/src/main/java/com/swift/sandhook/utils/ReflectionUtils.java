package com.swift.sandhook.utils;

import android.util.Log;
import java.lang.reflect.Method;

public class ReflectionUtils {
   public static Method forNameMethod;
   public static Method getMethodMethod;
   static Class vmRuntimeClass;
   static Method addWhiteListMethod;
   static Object vmRuntime;

   public static boolean passApiCheck() {
      try {
         addReflectionWhiteList("Landroid/", "Lcom/android/", "Ljava/lang/", "Ldalvik/system/", "Llibcore/io/");
         return true;
      } catch (Throwable var1) {
         Throwable throwable = var1;
         throwable.printStackTrace();
         return false;
      }
   }

   public static void addReflectionWhiteList(String... memberSigs) throws Throwable {
      addWhiteListMethod.invoke(vmRuntime, memberSigs);
   }

   static {
      try {
         getMethodMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
         forNameMethod = Class.class.getDeclaredMethod("forName", String.class);
         vmRuntimeClass = (Class)forNameMethod.invoke((Object)null, "dalvik.system.VMRuntime");
         addWhiteListMethod = (Method)getMethodMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
         Method getVMRuntimeMethod = (Method)getMethodMethod.invoke(vmRuntimeClass, "getRuntime", null);
         vmRuntime = getVMRuntimeMethod.invoke((Object)null);
      } catch (Exception var1) {
         Exception e = var1;
         Log.e("ReflectionUtils", "error get methods", e);
      }

   }
}
