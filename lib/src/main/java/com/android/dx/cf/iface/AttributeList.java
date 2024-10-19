package com.android.dx.cf.iface;

public interface AttributeList {
   boolean isMutable();

   int size();

   Attribute get(int var1);

   int byteLength();

   Attribute findFirst(String var1);

   Attribute findNext(Attribute var1);
}
