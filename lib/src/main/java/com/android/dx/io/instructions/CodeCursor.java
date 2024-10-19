package com.android.dx.io.instructions;

public interface CodeCursor {
   int cursor();

   int baseAddressForCursor();

   void setBaseAddress(int var1, int var2);
}
