package com.lody.virtual.os;

import android.os.Handler;
import android.os.HandlerThread;
import com.lody.virtual.StringFog;

public final class BackgroundThread extends HandlerThread {
   private static BackgroundThread sInstance;
   private static Handler sHandler;

   private BackgroundThread() {
      super("va.android.bg", 10);
   }

   private static void ensureThreadLocked() {
      if (sInstance == null) {
         sInstance = new BackgroundThread();
         sInstance.start();
         sHandler = new Handler(sInstance.getLooper());
      }

   }

   public static BackgroundThread get() {
      Class var0 = BackgroundThread.class;
      synchronized(BackgroundThread.class) {
         ensureThreadLocked();
         return sInstance;
      }
   }

   public static Handler getHandler() {
      Class var0 = BackgroundThread.class;
      synchronized(BackgroundThread.class) {
         ensureThreadLocked();
         return sHandler;
      }
   }
}
