package com.android.dx.dex.file;

public abstract class IndexedItem extends Item {
   private int index = -1;

   public final boolean hasIndex() {
      return this.index >= 0;
   }

   public final int getIndex() {
      if (this.index < 0) {
         throw new RuntimeException("index not yet set");
      } else {
         return this.index;
      }
   }

   public final void setIndex(int index) {
      if (this.index != -1) {
         throw new RuntimeException("index already set");
      } else {
         this.index = index;
      }
   }

   public final String indexString() {
      return '[' + Integer.toHexString(this.index) + ']';
   }
}
