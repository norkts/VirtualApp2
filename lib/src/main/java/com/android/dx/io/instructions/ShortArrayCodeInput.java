package com.android.dx.io.instructions;

import java.io.EOFException;

public final class ShortArrayCodeInput extends BaseCodeCursor implements CodeInput {
   private final short[] array;

   public ShortArrayCodeInput(short[] array) {
      if (array == null) {
         throw new NullPointerException("array == null");
      } else {
         this.array = array;
      }
   }

   public boolean hasMore() {
      return this.cursor() < this.array.length;
   }

   public int read() throws EOFException {
      try {
         int value = this.array[this.cursor()];
         this.advance(1);
         return value & '\uffff';
      } catch (ArrayIndexOutOfBoundsException var2) {
         throw new EOFException();
      }
   }

   public int readInt() throws EOFException {
      int short0 = this.read();
      int short1 = this.read();
      return short0 | short1 << 16;
   }

   public long readLong() throws EOFException {
      long short0 = (long)this.read();
      long short1 = (long)this.read();
      long short2 = (long)this.read();
      long short3 = (long)this.read();
      return short0 | short1 << 16 | short2 << 32 | short3 << 48;
   }
}
