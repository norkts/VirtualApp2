package com.android.dx.rop.type;

public interface TypeList {
   boolean isMutable();

   int size();

   Type getType(int var1);

   int getWordCount();

   TypeList withAddedType(Type var1);
}
