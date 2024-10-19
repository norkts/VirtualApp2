package com.lody.virtual.client.env;

import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.VLog;
import java.util.HashMap;
import java.util.Map;
import mirror.android.ddm.DdmHandleAppNameJBMR1;
import mirror.android.os.Process;
import mirror.dalvik.system.VMRuntime;

public class VirtualRuntime {
   private static final Handler sUIHandler = new Handler(Looper.getMainLooper());
   private static String sInitialPackageName;
   private static String sProcessName;
   private static final Map<String, String> ABI_TO_INSTRUCTION_SET_MAP = new HashMap(16);

   public static Handler getUIHandler() {
      return sUIHandler;
   }

   public static String getProcessName() {
      return sProcessName;
   }

   public static String getInitialPackageName() {
      return sInitialPackageName;
   }

   public static String getInstructionSet(String abi) {
      String instructionSet = (String)ABI_TO_INSTRUCTION_SET_MAP.get(abi);
      if (instructionSet == null) {
         throw new IllegalArgumentException("Unsupported ABI: " + abi);
      } else {
         return instructionSet;
      }
   }

   public static String getCurrentInstructionSet() {
      return (String)VMRuntime.getCurrentInstructionSet.call();
   }

   public static void setupRuntime(String processName, ApplicationInfo appInfo) {
      if (sProcessName == null) {
         sInitialPackageName = appInfo.packageName;
         sProcessName = processName;
         Process.setArgV0.call(processName);
         DdmHandleAppNameJBMR1.setAppName.call(processName, 0);
      }
   }

   public static <T> T crash(Throwable e) throws RuntimeException {
      e.printStackTrace();
      throw new RuntimeException("transact remote server failed", e);
   }

   public static boolean is64bit() {
      return VERSION.SDK_INT >= 23 ? android.os.Process.is64Bit() : (Boolean)VMRuntime.is64Bit.call(VMRuntime.getRuntime.call());
   }

   public static String adjustLibName(String libName) {
      return VirtualCore.get().isMainPackage() ? libName : libName + "_ext";
   }

   public static void exit() {
      VLog.d(VirtualRuntime.class.getSimpleName(), "Exit process : %s (%s).", getProcessName(), VirtualCore.get().getProcessName());
      android.os.Process.killProcess(android.os.Process.myPid());
   }

   public static boolean isArt() {
      return System.getProperty("java.vm.version").startsWith("2");
   }

   static {
      ABI_TO_INSTRUCTION_SET_MAP.put("armeabi", "arm");
      ABI_TO_INSTRUCTION_SET_MAP.put("armeabi-v7a", "arm");
      ABI_TO_INSTRUCTION_SET_MAP.put("mips", "mips");
      ABI_TO_INSTRUCTION_SET_MAP.put("mips64", "mips64");
      ABI_TO_INSTRUCTION_SET_MAP.put("arm64-v8a", "arm64");
   }
}
