package com.carlos.science.client.hyxd;

import android.os.Build.VERSION;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import mirror.dalvik.system.VMRuntime;

public class HYXDNative {
   private static final String TAG = "HYXDNative";
   private static final String LIB_NAME = "hyxd";
   private static final String LIB_NAME_64 = "hyxd_64";

   public static boolean is64bit() {
      if (VERSION.SDK_INT < 21) {
         return false;
      } else {
         if (VERSION.SDK_INT >= 23) {
         }

         return (Boolean)VMRuntime.is64Bit.call(VMRuntime.getRuntime.call());
      }
   }

   public static native void init(int var0, String var1);

   public static native void searchWrite(String var0, String var1, boolean var2);

   public static native void memoryTest();

   static {
      try {
         if (is64bit()) {
            System.loadLibrary("hyxd_64");
         } else {
            System.loadLibrary("hyxd");
         }
      } catch (Throwable var1) {
         Throwable e = var1;
         HVLog.printThrowable(e);
      }

   }
}
