package com.android.dx.util;

import java.util.NoSuchElementException;

public class BitIntSet implements IntSet {
   int[] bits;

   public BitIntSet(int max) {
      this.bits = Bits.makeBitSet(max);
   }

   public void add(int value) {
      this.ensureCapacity(value);
      Bits.set(this.bits, value, true);
   }

   private void ensureCapacity(int value) {
      if (value >= Bits.getMax(this.bits)) {
         int[] newBits = Bits.makeBitSet(Math.max(value + 1, 2 * Bits.getMax(this.bits)));
         System.arraycopy(this.bits, 0, newBits, 0, this.bits.length);
         this.bits = newBits;
      }

   }

   public void remove(int value) {
      if (value < Bits.getMax(this.bits)) {
         Bits.set(this.bits, value, false);
      }

   }

   public boolean has(int value) {
      return value < Bits.getMax(this.bits) && Bits.get(this.bits, value);
   }

   public void merge(IntSet other) {
      if (other instanceof BitIntSet) {
         BitIntSet o = (BitIntSet)other;
         this.ensureCapacity(Bits.getMax(o.bits) + 1);
         Bits.or(this.bits, o.bits);
      } else if (other instanceof ListIntSet) {
         ListIntSet o = (ListIntSet)other;
         int sz = o.ints.size();
         if (sz > 0) {
            this.ensureCapacity(o.ints.get(sz - 1));
         }

         for(int i = 0; i < o.ints.size(); ++i) {
            Bits.set(this.bits, o.ints.get(i), true);
         }
      } else {
         IntIterator iter = other.iterator();

         while(iter.hasNext()) {
            this.add(iter.next());
         }
      }

   }

   public int elements() {
      return Bits.bitCount(this.bits);
   }

   public IntIterator iterator() {
      return new IntIterator() {
         private int idx;

         {
            this.idx = Bits.findFirst(BitIntSet.this.bits, 0);
         }

         public boolean hasNext() {
            return this.idx >= 0;
         }

         public int next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               int ret = this.idx;
               this.idx = Bits.findFirst(BitIntSet.this.bits, this.idx + 1);
               return ret;
            }
         }
      };
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append('{');
      boolean first = true;

      for(int i = Bits.findFirst(this.bits, 0); i >= 0; i = Bits.findFirst(this.bits, i + 1)) {
         if (!first) {
            sb.append(", ");
         }

         first = false;
         sb.append(i);
      }

      sb.append('}');
      return sb.toString();
   }
}
