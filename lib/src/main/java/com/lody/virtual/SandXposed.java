package com.lody.virtual;

import android.content.Context;
import android.os.Build.VERSION;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.BuildCompat;
import com.swift.sandhook.HookLog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookConfig;
import com.swift.sandhook.xposedcompat.XposedCompat;
import com.swift.sandhook.xposedcompat.utils.DexMakerUtils;
import java.io.File;
import java.lang.reflect.Member;
import mirror.dalvik.system.VMRuntime;

public class SandXposed {
   public static void init() {
      try {
         SandHookConfig.DEBUG = VMRuntime.isJavaDebuggable == null ? false : (Boolean)VMRuntime.isJavaDebuggable.call(VMRuntime.getRuntime.call());
      } catch (Throwable var1) {
         Throwable e = var1;
         e.printStackTrace();
      }

      HookLog.DEBUG = false;
      if (BuildCompat.isS()) {
         SandHookConfig.SDK_INT = 31;
      } else if (BuildCompat.isR()) {
         SandHookConfig.SDK_INT = 30;
      } else {
         SandHookConfig.SDK_INT = VERSION.SDK_INT;
      }

      SandHookConfig.compiler = SandHookConfig.SDK_INT < 26;
      SandHookConfig.delayHook = false;
      SandHook.setHookModeCallBack(new SandHook.HookModeCallBack() {
         public int hookMode(Member member) {
            return VERSION.SDK_INT >= 30 ? 2 : 0;
         }
      });
      SandHook.disableVMInline();
      XposedCompat.cacheDir = new File(VirtualCore.get().getContext().getCacheDir(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+CGgFRSVgJA5ALy0iP2wzGh9rJygbLhcMO2AVSFo=")));
   }

   public static void initForXposed(Context context, String processName) {
      XposedCompat.cacheDir = new File(context.getCacheDir(), DexMakerUtils.MD5(processName));
   }
}
