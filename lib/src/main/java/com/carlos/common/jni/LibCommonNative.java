package com.carlos.common.jni;

import com.carlos.libcommon.StringFog;

public class LibCommonNative {
   public static native boolean getSHA1(String var0);

   public static native void fileEncrypt(String var0, String var1, String var2);

   public static native void fileDecrypt(String var0, String var1, String var2);

   public static native void fileSplit(String var0, String var1, int var2);

   public static native void fileMerge(String var0, String var1, String var2, int var3);

   static {
      System.loadLibrary(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADWoVGiZOATw7")));
   }
}
