package com.swift.sandhook;

import android.util.Log;

public class HookLog {
   public static final String TAG = "SandHook";
   public static boolean DEBUG;

   public static int v(String s) {
      return Log.v("SandHook", s);
   }

   public static int i(String s) {
      return Log.i("SandHook", s);
   }

   public static int d(String s) {
      return Log.d("SandHook", s);
   }

   public static int w(String s) {
      return Log.w("SandHook", s);
   }

   public static int e(String s) {
      return Log.e("SandHook", s);
   }

   public static int e(String s, Throwable t) {
      return Log.e("SandHook", s, t);
   }

   static {
      DEBUG = SandHookConfig.DEBUG;
   }
}
