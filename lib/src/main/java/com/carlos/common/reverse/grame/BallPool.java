package com.carlos.common.reverse.grame;

import android.app.Application;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class BallPool {
   public static void hook(ClassLoader classLoader, Application application) {
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PlsrJBQNMS0cICQ0Ki1fCX4zAiVlDRoeIxgcI30gTSxlVx4uKi4ILG8VFjNqAQIdJi0YO3lTPFo=")));
      XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYPGwgRStoNzg6Ll8cJWEwPDZvHl0yIz42JWwnBgVqJCw0Jz1fKGxTAl9vAQ4yLQcMVg==")), classLoader, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+CGIFGi0=")), Integer.TYPE, new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            param.setResult(true);
         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
         }
      });
      XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojEi9gNAY5KhccKn8VGi9rJ1k/LS4+KGAVOCpsJFwdJy42IWoaMBBrEQI7LxcYJmwYODNlNzAhLAcqJw==")), classLoader, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+CGIFGi0=")), Integer.TYPE, new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            param.setResult(true);
         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
         }
      });
   }
}
