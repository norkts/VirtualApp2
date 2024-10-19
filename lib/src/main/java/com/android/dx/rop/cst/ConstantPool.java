package com.android.dx.rop.cst;

public interface ConstantPool {
   int size();

   Constant get(int var1);

   Constant get0Ok(int var1);

   Constant getOrNull(int var1);

   Constant[] getEntries();
}
