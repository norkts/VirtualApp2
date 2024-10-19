package com.lody.virtual.helper;

public class AvoidRecursive {
   private boolean mCalling = false;

   public boolean beginCall() {
      if (this.mCalling) {
         return false;
      } else {
         this.mCalling = true;
         return true;
      }
   }

   public void finishCall() {
      this.mCalling = false;
   }
}
