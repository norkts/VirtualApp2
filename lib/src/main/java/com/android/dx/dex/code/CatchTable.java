package com.android.dx.dex.code;

import com.android.dx.util.FixedSizeList;

public final class CatchTable extends FixedSizeList implements Comparable<CatchTable> {
   public static final CatchTable EMPTY = new CatchTable(0);

   public CatchTable(int size) {
      super(size);
   }

   public Entry get(int n) {
      return (Entry)this.get0(n);
   }

   public void set(int n, Entry entry) {
      this.set0(n, entry);
   }

   public int compareTo(CatchTable other) {
      if (this == other) {
         return 0;
      } else {
         int thisSize = this.size();
         int otherSize = other.size();
         int checkSize = Math.min(thisSize, otherSize);

         for(int i = 0; i < checkSize; ++i) {
            Entry thisEntry = this.get(i);
            Entry otherEntry = other.get(i);
            int compare = thisEntry.compareTo(otherEntry);
            if (compare != 0) {
               return compare;
            }
         }

         if (thisSize < otherSize) {
            return -1;
         } else if (thisSize > otherSize) {
            return 1;
         } else {
            return 0;
         }
      }
   }

   public static class Entry implements Comparable<Entry> {
      private final int start;
      private final int end;
      private final CatchHandlerList handlers;

      public Entry(int start, int end, CatchHandlerList handlers) {
         if (start < 0) {
            throw new IllegalArgumentException("start < 0");
         } else if (end <= start) {
            throw new IllegalArgumentException("end <= start");
         } else if (handlers.isMutable()) {
            throw new IllegalArgumentException("handlers.isMutable()");
         } else {
            this.start = start;
            this.end = end;
            this.handlers = handlers;
         }
      }

      public int hashCode() {
         int hash = this.start * 31 + this.end;
         hash = hash * 31 + this.handlers.hashCode();
         return hash;
      }

      public boolean equals(Object other) {
         if (other instanceof Entry) {
            return this.compareTo((Entry)other) == 0;
         } else {
            return false;
         }
      }

      public int compareTo(Entry other) {
         if (this.start < other.start) {
            return -1;
         } else if (this.start > other.start) {
            return 1;
         } else if (this.end < other.end) {
            return -1;
         } else {
            return this.end > other.end ? 1 : this.handlers.compareTo(other.handlers);
         }
      }

      public int getStart() {
         return this.start;
      }

      public int getEnd() {
         return this.end;
      }

      public CatchHandlerList getHandlers() {
         return this.handlers;
      }
   }
}
