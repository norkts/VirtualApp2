package com.carlos.science.runner;

import android.view.View;

public abstract class OnceRunnable implements Runnable {
   private boolean mScheduled;

   public final void run() {
      this.onRun();
      this.mScheduled = false;
   }

   public abstract void onRun();

   public void postSelf(View carrier) {
      this.postDelaySelf(carrier, 0);
   }

   public void postDelaySelf(View carrier, int delay) {
      if (!this.mScheduled) {
         carrier.postDelayed(this, (long)delay);
         this.mScheduled = true;
      }

   }

   public boolean isRunning() {
      return this.mScheduled;
   }

   public void removeSelf(View carrier) {
      this.mScheduled = false;
      carrier.removeCallbacks(this);
   }
}
