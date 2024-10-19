package com.lody.virtual.sandxposed;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.OSUtils;
import com.lody.virtual.remote.InstalledAppInfo;
import com.swift.sandhook.HookLog;
import com.swift.sandhook.PendingHookHandler;
import com.swift.sandhook.SandHookConfig;
import com.swift.sandhook.utils.ReflectionUtils;
import com.swift.sandhook.xposedcompat.XposedCompat;
import com.swift.sandhook.xposedcompat.utils.DexMakerUtils;
import de.robv.android.xposed.XposedBridge;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import mirror.dalvik.system.VMRuntime;

public class SandXposed {
   public static void init() {
      Log.e("SandHook", "Pending Hook Mode!");
      if (VERSION.SDK_INT >= 28) {
         ReflectionUtils.passApiCheck();
      }

      SandHookConfig.DEBUG = VMRuntime.isJavaDebuggable == null ? false : (Boolean)VMRuntime.isJavaDebuggable.call(VMRuntime.getRuntime.call());
      HookLog.DEBUG = SandHookConfig.DEBUG;
      SandHookConfig.SDK_INT = OSUtils.getInstance().isAndroidQ() ? 29 : VERSION.SDK_INT;
      SandHookConfig.compiler = SandHookConfig.SDK_INT < 26;
      if (PendingHookHandler.canWork()) {
         Log.e("SandHook", "Pending Hook Mode!");
      }

   }

   public static void injectXposedModule(Context context, String packageName, String processName) {
      Log.d("HV-", "===============injectXposedModule 0====================");
      Log.d("HV-", "===============injectXposedModule  1====================");
      List<InstalledAppInfo> appInfos = VirtualCore.get().getInstalledApps(1342177280);
      ClassLoader classLoader = context.getClassLoader();
      Log.e("XPOSED", "start appInfos:" + appInfos.size());
      Iterator var5 = appInfos.iterator();

      while(var5.hasNext()) {
         InstalledAppInfo module = (InstalledAppInfo)var5.next();
         if (TextUtils.equals(packageName, module.packageName)) {
            Log.d("injectXposedModule", "injectSelf : " + processName);
         }

         Log.e("XPOSED", "load:" + module.packageName + "-" + module.libPath);
         XposedCompat.loadModule(module.getApkPath(), module.getOatFile().getParent(), module.libPath, XposedBridge.class.getClassLoader());
      }

      XposedCompat.context = context;
      XposedCompat.packageName = packageName;
      XposedCompat.processName = processName;
      XposedCompat.cacheDir = new File(context.getCacheDir(), DexMakerUtils.MD5(processName));
      XposedCompat.classLoader = XposedCompat.getSandHookXposedClassLoader(classLoader, XposedBridge.class.getClassLoader());
      XposedCompat.isFirstApplication = true;
      SandHookHelper.initHookPolicy();
      EnvironmentSetup.init(context, packageName, processName);

      try {
         XposedCompat.callXposedModuleInit();
      } catch (Throwable var7) {
         Throwable throwable = var7;
         throwable.printStackTrace();
         Log.e("XPOSED", "err:" + throwable.getStackTrace().toString());
      }

   }
}
