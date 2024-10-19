package com.carlos.science.floatball;

import android.graphics.drawable.Drawable;

public class FloatBallCfg {
   public Drawable mIcon;
   public int mSize;
   public Gravity mGravity;
   public int mOffsetY;
   public boolean mHideHalfLater;

   public FloatBallCfg(int size, Drawable icon) {
      this(size, icon, FloatBallCfg.Gravity.LEFT_TOP, 0);
   }

   public FloatBallCfg(int size, Drawable icon, Gravity gravity) {
      this(size, icon, gravity, 0);
   }

   public FloatBallCfg(int size, Drawable icon, Gravity gravity, int offsetY) {
      this.mOffsetY = 0;
      this.mHideHalfLater = true;
      this.mSize = size;
      this.mIcon = icon;
      this.mGravity = gravity;
      this.mOffsetY = offsetY;
   }

   public FloatBallCfg(int size, Drawable icon, Gravity gravity, boolean hideHalfLater) {
      this.mOffsetY = 0;
      this.mHideHalfLater = true;
      this.mSize = size;
      this.mIcon = icon;
      this.mGravity = gravity;
      this.mHideHalfLater = hideHalfLater;
   }

   public FloatBallCfg(int size, Drawable icon, Gravity gravity, int offsetY, boolean hideHalfLater) {
      this.mOffsetY = 0;
      this.mHideHalfLater = true;
      this.mSize = size;
      this.mIcon = icon;
      this.mGravity = gravity;
      this.mOffsetY = offsetY;
      this.mHideHalfLater = hideHalfLater;
   }

   public void setGravity(Gravity gravity) {
      this.mGravity = gravity;
   }

   public void setHideHalfLater(boolean hideHalfLater) {
      this.mHideHalfLater = hideHalfLater;
   }

   public static enum Gravity {
      LEFT_TOP(51),
      LEFT_CENTER(19),
      LEFT_BOTTOM(83),
      RIGHT_TOP(53),
      RIGHT_CENTER(21),
      RIGHT_BOTTOM(85);

      int mValue;

      private Gravity(int gravity) {
         this.mValue = gravity;
      }

      public int getGravity() {
         return this.mValue;
      }
   }
}
