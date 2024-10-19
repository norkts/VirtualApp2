package com.swift.sandhook.xposedcompat.utils;

import android.app.Application;
import java.lang.reflect.Method;

public class ApplicationUtils {
   private static Class classActivityThread;
   private static Method currentApplicationMethod;
   static Application application;

   public static Application currentApplication() {
      if (application != null) {
         return application;
      } else {
         if (currentApplicationMethod == null) {
            try {
               classActivityThread = Class.forName("android.app.ActivityThread");
               currentApplicationMethod = classActivityThread.getDeclaredMethod("currentApplication");
            } catch (Exception var2) {
               Exception e = var2;
               e.printStackTrace();
            }
         }

         if (currentApplicationMethod == null) {
            return null;
         } else {
            try {
               application = (Application)currentApplicationMethod.invoke((Object)null);
            } catch (Exception var1) {
            }

            return application;
         }
      }
   }
}
