package com.lody.virtual.client.core;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualRuntime;

public class ANRWatchDog extends Thread {
   private static final int MESSAGE_WATCHDOG_TIME_TICK = 0;
   private static final int ANR_TIMEOUT = 5000;
   private static int lastTimeTick = -1;
   private static int timeTick = 0;
   private boolean makeCrash;
   @SuppressLint({"HandlerLeak"})
   private final Handler watchDogHandler;

   public ANRWatchDog(boolean makeCrash) {
      this.watchDogHandler = new Handler() {
         public void handleMessage(Message msg) {
            ANRWatchDog.timeTick++;
            ANRWatchDog.timeTick = ANRWatchDog.timeTick % Integer.MAX_VALUE;
         }
      };
      this.makeCrash = makeCrash;
   }

   public ANRWatchDog() {
      this(false);
   }

   public void run() {
      while(true) {
         this.watchDogHandler.sendEmptyMessage(0);

         try {
            Thread.sleep(5000L);
         } catch (InterruptedException var2) {
            InterruptedException e = var2;
            e.printStackTrace();
         }

         if (timeTick == lastTimeTick) {
            this.triggerANR();
         } else {
            lastTimeTick = timeTick;
         }
      }
   }

   private void triggerANR() {
      if (this.makeCrash) {
         throw new ANRException();
      } else {
         try {
            throw new ANRException();
         } catch (ANRException var2) {
            ANRException e = var2;
            e.printStackTrace();
         }
      }
   }

   public static class ANRException extends RuntimeException {
      public ANRException() {
         super("========= ANR =========" + getAnrDesc());
         Thread mainThread = Looper.getMainLooper().getThread();
         this.setStackTrace(mainThread.getStackTrace());
      }

      private static String getAnrDesc() {
         return VirtualCore.get().isVAppProcess() ? VirtualRuntime.getProcessName() : VirtualCore.get().getProcessName();
      }
   }
}
