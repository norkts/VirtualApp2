package com.android.dx.util;

import java.util.NoSuchElementException;

public class ListIntSet implements IntSet {
   final IntList ints = new IntList();

   public ListIntSet() {
      this.ints.sort();
   }

   public void add(int value) {
      int index = this.ints.binarysearch(value);
      if (index < 0) {
         this.ints.insert(-(index + 1), value);
      }

   }

   public void remove(int value) {
      int index = this.ints.indexOf(value);
      if (index >= 0) {
         this.ints.removeIndex(index);
      }

   }

   public boolean has(int value) {
      return this.ints.indexOf(value) >= 0;
   }

   public void merge(IntSet other) {
      int i;
      if (other instanceof ListIntSet) {
         ListIntSet o = (ListIntSet)other;
         i = this.ints.size();
         int szOther = o.ints.size();
         int i = 0;
         int j = 0;

         while(j < szOther && i < i) {
            while(j < szOther && o.ints.get(j) < this.ints.get(i)) {
               this.add(o.ints.get(j++));
            }

            if (j == szOther) {
               break;
            }

            while(i < i && o.ints.get(j) >= this.ints.get(i)) {
               ++i;
            }
         }

         while(j < szOther) {
            this.add(o.ints.get(j++));
         }

         this.ints.sort();
      } else if (other instanceof BitIntSet) {
         BitIntSet o = (BitIntSet)other;

         for(i = 0; i >= 0; i = Bits.findFirst(o.bits, i + 1)) {
            this.ints.add(i);
         }

         this.ints.sort();
      } else {
         IntIterator iter = other.iterator();

         while(iter.hasNext()) {
            this.add(iter.next());
         }
      }

   }

   public int elements() {
      return this.ints.size();
   }

   public IntIterator iterator() {
      return new IntIterator() {
         private int idx = 0;

         public boolean hasNext() {
            return this.idx < ListIntSet.this.ints.size();
         }

         public int next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return ListIntSet.this.ints.get(this.idx++);
            }
         }
      };
   }

   public String toString() {
      return this.ints.toString();
   }
}
