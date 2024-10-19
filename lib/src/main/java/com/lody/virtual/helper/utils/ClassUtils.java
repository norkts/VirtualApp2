package com.lody.virtual.helper.utils;

public class ClassUtils {
   public static boolean isClassExist(String className) {
      try {
         Class.forName(className);
         return true;
      } catch (ClassNotFoundException var2) {
         return false;
      }
   }

   public static void fixArgs(Class<?>[] types, Object[] args) {
      for(int i = 0; i < types.length; ++i) {
         if (types[i] == Integer.TYPE && args[i] == null) {
            args[i] = 0;
         } else if (types[i] == Boolean.TYPE && args[i] == null) {
            args[i] = false;
         }
      }

   }
}
