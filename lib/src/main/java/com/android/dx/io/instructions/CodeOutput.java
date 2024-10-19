package com.android.dx.io.instructions;

public interface CodeOutput extends CodeCursor {
   void write(short var1);

   void write(short var1, short var2);

   void write(short var1, short var2, short var3);

   void write(short var1, short var2, short var3, short var4);

   void write(short var1, short var2, short var3, short var4, short var5);

   void writeInt(int var1);

   void writeLong(long var1);

   void write(byte[] var1);

   void write(short[] var1);

   void write(int[] var1);

   void write(long[] var1);
}
