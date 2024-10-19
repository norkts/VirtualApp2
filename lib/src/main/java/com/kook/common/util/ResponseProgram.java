package com.kook.common.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import org.jdeferred.android.AndroidDeferredManager;

public class ResponseProgram {
   private static final AndroidDeferredManager androidDeferredManager = new AndroidDeferredManager();
   private static final AndroidDeferredManager gDM = new AndroidDeferredManager();
   private static final Handler gUiHandler = new Handler(Looper.getMainLooper());

   public static AndroidDeferredManager defer() {
      return androidDeferredManager;
   }

   public static int dpToPx(Context context, int dp) {
      return (int)TypedValue.applyDimension(1, (float)dp, context.getResources().getDisplayMetrics());
   }

   public static void post(Runnable r) {
      gUiHandler.post(r);
   }

   public static Handler getHandler() {
      return gUiHandler;
   }

   public static void postDelayed(long delay, Runnable r) {
      gUiHandler.postDelayed(r, delay);
   }

   public static void sleep(long time) {
      try {
         Thread.sleep(time);
      } catch (InterruptedException var3) {
         InterruptedException e = var3;
         e.printStackTrace();
      }

   }
}
