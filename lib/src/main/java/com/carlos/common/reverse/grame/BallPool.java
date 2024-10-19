package com.carlos.common.reverse.grame;

import android.app.Application;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class BallPool {
   public static void hook(ClassLoader classLoader, Application application) {
      HVLog.d(" 开始 hook com.miniclip.eightballpool  ");
      XposedHelpers.findAndHookMethod("com.google.firebase.crashlytics.internal.Logger", classLoader, "canLog", Integer.TYPE, new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            param.setResult(true);
         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
         }
      });
      XposedHelpers.findAndHookMethod("com.miniclip.eightballpool.EightBallPoolActivity", classLoader, "canLog", Integer.TYPE, new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            param.setResult(true);
         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
         }
      });
   }
}
