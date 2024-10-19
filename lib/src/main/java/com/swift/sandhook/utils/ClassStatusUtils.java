package com.swift.sandhook.utils;

import com.swift.sandhook.SandHookConfig;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassStatusUtils {
   static Field fieldStatusOfClass;

   public static int getClassStatus(Class clazz, boolean isUnsigned) {
      if (clazz == null) {
         return 0;
      } else {
         int status = 0;

         try {
            status = fieldStatusOfClass.getInt(clazz);
         } catch (Throwable var4) {
         }

         if (isUnsigned) {
            status = (int)(toUnsignedLong(status) >> 28);
         }

         return status;
      }
   }

   public static long toUnsignedLong(int x) {
      return (long)x & 4294967295L;
   }

   public static boolean isInitialized(Class clazz) {
      if (fieldStatusOfClass == null) {
         return true;
      } else if (SandHookConfig.SDK_INT >= 28) {
         return getClassStatus(clazz, true) >= 14;
      } else if (SandHookConfig.SDK_INT == 27) {
         return getClassStatus(clazz, false) == 11;
      } else {
         return getClassStatus(clazz, false) == 10;
      }
   }

   public static boolean isStaticAndNoInited(Member hookMethod) {
      if (!(hookMethod instanceof Method)) {
         return false;
      } else {
         Class declaringClass = hookMethod.getDeclaringClass();
         return Modifier.isStatic(hookMethod.getModifiers()) && !isInitialized(declaringClass);
      }
   }

   static {
      try {
         fieldStatusOfClass = Class.class.getDeclaredField("status");
         fieldStatusOfClass.setAccessible(true);
      } catch (NoSuchFieldException var1) {
      }

   }
}
