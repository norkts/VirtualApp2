package com.android.dx.util;

import com.android.dex.util.ByteOutput;

public interface Output extends ByteOutput {
   int getCursor();

   void assertCursor(int var1);

   void writeByte(int var1);

   void writeShort(int var1);

   void writeInt(int var1);

   void writeLong(long var1);

   int writeUleb128(int var1);

   int writeSleb128(int var1);

   void write(ByteArray var1);

   void write(byte[] var1, int var2, int var3);

   void write(byte[] var1);

   void writeZeroes(int var1);

   void alignTo(int var1);
}
