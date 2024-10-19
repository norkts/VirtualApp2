package com.lody.virtual.helper.utils;

import android.os.Handler;

public abstract class SchedulerTask implements Runnable {
   private Handler mHandler;
   private long mDelay;
   private final Runnable mInnerRunnable = new Runnable() {
      public void run() {
         SchedulerTask.this.run();
         if (SchedulerTask.this.mDelay > 0L) {
            SchedulerTask.this.mHandler.postDelayed(this, SchedulerTask.this.mDelay);
         }

      }
   };

   public SchedulerTask(Handler handler, long delay) {
      this.mHandler = handler;
      this.mDelay = delay;
   }

   public void schedule() {
      this.mHandler.post(this.mInnerRunnable);
   }

   public void cancel() {
      this.mHandler.removeCallbacks(this.mInnerRunnable);
   }
}
