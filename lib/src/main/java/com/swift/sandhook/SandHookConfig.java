package com.swift.sandhook;

import android.os.Build.VERSION;

public class SandHookConfig {
   public static volatile int SDK_INT = getSdkInt();
   public static volatile boolean DEBUG = true;
   public static volatile boolean compiler;
   public static volatile ClassLoader initClassLoader;
   public static volatile int curUser;
   public static volatile boolean delayHook;

   public static int getSdkInt() {
      try {
         if (VERSION.PREVIEW_SDK_INT > 0) {
            return VERSION.SDK_INT + 1;
         }
      } catch (Throwable var1) {
      }

      return VERSION.SDK_INT;
   }

   static {
      compiler = SDK_INT < 29;
      curUser = 0;
      delayHook = true;
   }
}
