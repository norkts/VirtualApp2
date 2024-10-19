package com.carlos.science.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class MotionVelocityUtil {
   private VelocityTracker mVelocityTracker;
   private int mMaxVelocity;
   private int mMinVelocity;

   public MotionVelocityUtil(Context context) {
      this.mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
      this.mMinVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
   }

   public int getMinVelocity() {
      return this.mMinVelocity < 1000 ? 1000 : this.mMinVelocity;
   }

   public void acquireVelocityTracker(MotionEvent event) {
      if (null == this.mVelocityTracker) {
         this.mVelocityTracker = VelocityTracker.obtain();
      }

      this.mVelocityTracker.addMovement(event);
   }

   public void computeCurrentVelocity() {
      this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaxVelocity);
   }

   public float getXVelocity() {
      return this.mVelocityTracker.getXVelocity();
   }

   public float getYVelocity() {
      return this.mVelocityTracker.getYVelocity();
   }

   public void releaseVelocityTracker() {
      if (null != this.mVelocityTracker) {
         this.mVelocityTracker.clear();
         this.mVelocityTracker.recycle();
         this.mVelocityTracker = null;
      }

   }
}
