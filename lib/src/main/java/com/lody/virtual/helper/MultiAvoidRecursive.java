package com.lody.virtual.helper;

import android.util.SparseBooleanArray;

public class MultiAvoidRecursive {
   private SparseBooleanArray mCallings;

   public MultiAvoidRecursive(int initialCapacity) {
      this.mCallings = new SparseBooleanArray(initialCapacity);
   }

   public MultiAvoidRecursive() {
      this(7);
   }

   public boolean beginCall(int id) {
      synchronized(this.mCallings) {
         if (this.mCallings.get(id)) {
            return false;
         } else {
            this.mCallings.put(id, true);
            return true;
         }
      }
   }

   public void finishCall(int id) {
      synchronized(this.mCallings) {
         this.mCallings.put(id, false);
      }
   }
}
