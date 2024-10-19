package com.android.dx.rop.cst;

import com.android.dx.util.FixedSizeList;

public class CstArray extends Constant {
   private final List list;

   public CstArray(List list) {
      if (list == null) {
         throw new NullPointerException("list == null");
      } else {
         list.throwIfMutable();
         this.list = list;
      }
   }

   public boolean equals(Object other) {
      return !(other instanceof CstArray) ? false : this.list.equals(((CstArray)other).list);
   }

   public int hashCode() {
      return this.list.hashCode();
   }

   protected int compareTo0(Constant other) {
      return this.list.compareTo(((CstArray)other).list);
   }

   public String toString() {
      return this.list.toString("array{", ", ", "}");
   }

   public String typeName() {
      return "array";
   }

   public boolean isCategory2() {
      return false;
   }

   public String toHuman() {
      return this.list.toHuman("{", ", ", "}");
   }

   public List getList() {
      return this.list;
   }

   public static final class List extends FixedSizeList implements Comparable<List> {
      public List(int size) {
         super(size);
      }

      public int compareTo(List other) {
         int thisSize = this.size();
         int otherSize = other.size();
         int compareSize = thisSize < otherSize ? thisSize : otherSize;

         for(int i = 0; i < compareSize; ++i) {
            Constant thisItem = (Constant)this.get0(i);
            Constant otherItem = (Constant)other.get0(i);
            int compare = thisItem.compareTo(otherItem);
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

      public Constant get(int n) {
         return (Constant)this.get0(n);
      }

      public void set(int n, Constant a) {
         this.set0(n, a);
      }
   }
}
