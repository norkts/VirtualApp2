package com.swift.sandhook.xposedcompat.utils;

import android.util.Log;
import com.swift.sandhook.HookLog;
import java.lang.reflect.Member;

public class DexLog {
   public static final String TAG = "SandXposed";
   public static boolean DEBUG;

   public static int v(String s) {
      return Log.v("SandXposed", s);
   }

   public static int i(String s) {
      return Log.i("SandXposed", s);
   }

   public static int d(String s) {
      return Log.d("SandXposed", s);
   }

   public static void printMethodHookIn(Member member) {
      if (DEBUG && member != null) {
         Log.d("SandXposed", "method <" + member.toString() + "> hook in");
      }

   }

   public static void printCallOriginError(Member member) {
      if (member != null) {
         Log.e("SandXposed", "method <" + member.toString() + "> call origin error!");
      }

   }

   public static int w(String s) {
      return Log.w("SandXposed", s);
   }

   public static int e(String s) {
      return Log.e("SandXposed", s);
   }

   public static int e(String s, Throwable t) {
      return Log.e("SandXposed", s, t);
   }

   static {
      DEBUG = HookLog.DEBUG;
   }
}
