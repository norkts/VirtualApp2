package com.android.dx.io.instructions;

public final class ShortArrayCodeOutput extends BaseCodeCursor implements CodeOutput {
   private final short[] array;

   public ShortArrayCodeOutput(int maxSize) {
      if (maxSize < 0) {
         throw new IllegalArgumentException("maxSize < 0");
      } else {
         this.array = new short[maxSize];
      }
   }

   public short[] getArray() {
      int cursor = this.cursor();
      if (cursor == this.array.length) {
         return this.array;
      } else {
         short[] result = new short[cursor];
         System.arraycopy(this.array, 0, result, 0, cursor);
         return result;
      }
   }

   public void write(short codeUnit) {
      this.array[this.cursor()] = codeUnit;
      this.advance(1);
   }

   public void write(short u0, short u1) {
      this.write(u0);
      this.write(u1);
   }

   public void write(short u0, short u1, short u2) {
      this.write(u0);
      this.write(u1);
      this.write(u2);
   }

   public void write(short u0, short u1, short u2, short u3) {
      this.write(u0);
      this.write(u1);
      this.write(u2);
      this.write(u3);
   }

   public void write(short u0, short u1, short u2, short u3, short u4) {
      this.write(u0);
      this.write(u1);
      this.write(u2);
      this.write(u3);
      this.write(u4);
   }

   public void writeInt(int value) {
      this.write((short)value);
      this.write((short)(value >> 16));
   }

   public void writeLong(long value) {
      this.write((short)((int)value));
      this.write((short)((int)(value >> 16)));
      this.write((short)((int)(value >> 32)));
      this.write((short)((int)(value >> 48)));
   }

   public void write(byte[] data) {
      int value = 0;
      boolean even = true;
      byte[] var4 = data;
      int var5 = data.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         byte b = var4[var6];
         if (even) {
            value = b & 255;
            even = false;
         } else {
            value |= b << 8;
            this.write((short)value);
            even = true;
         }
      }

      if (!even) {
         this.write((short)value);
      }

   }

   public void write(short[] data) {
      short[] var2 = data;
      int var3 = data.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         short unit = var2[var4];
         this.write(unit);
      }

   }

   public void write(int[] data) {
      int[] var2 = data;
      int var3 = data.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int i = var2[var4];
         this.writeInt(i);
      }

   }

   public void write(long[] data) {
      long[] var2 = data;
      int var3 = data.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         long l = var2[var4];
         this.writeLong(l);
      }

   }
}
