package com.android.dx.cf.iface;

import com.android.dx.util.ByteArray;

public interface ParseObserver {
   void changeIndent(int var1);

   void startParsingMember(ByteArray var1, int var2, String var3, String var4);

   void endParsingMember(ByteArray var1, int var2, String var3, String var4, Member var5);

   void parsed(ByteArray var1, int var2, int var3, String var4);
}
