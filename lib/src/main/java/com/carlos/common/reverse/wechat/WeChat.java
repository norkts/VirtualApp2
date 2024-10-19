package com.carlos.common.reverse.wechat;

import android.app.Application;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class WeChat {
   public static void hook(ClassLoader classLoader, Application application) {
      HVLog.d(" 开始 hook com.tencent.mm  ");
      Class<?> logClz = XposedHelpers.findClass("com.tencent.mm.sdk.platformtools.Log", classLoader);
      Class<?> logImpClz = XposedHelpers.findClass("com.tencent.mm.sdk.platformtools.Log$LogImp", classLoader);
      XposedHelpers.setStaticIntField(logClz, "level", 0);
      Object getLogLevel = XposedHelpers.callStaticMethod(logClz, "getLogLevel");
      XposedHelpers.findAndHookMethod("com.tencent.mm.sdk.platformtools.Log", classLoader, "getLogLevel", new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            param.setResult(0);
            HVLog.d("  hook com.tencent.mm.sdk.platformtools.Log.getLogLevel  getResult:" + param.getResult());
         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
         }
      });
      HVLog.d("  hook com.tencent.mm  getLogLevel " + getLogLevel);
   }
}
