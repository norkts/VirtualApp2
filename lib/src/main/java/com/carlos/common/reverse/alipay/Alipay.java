package com.carlos.common.reverse.alipay;

import android.content.Context;
import com.carlos.common.reverse.ReflectionApplication;
import com.carlos.libcommon.StringFog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class Alipay extends ReflectionApplication {
   public static void hook(ClassLoader classLoader) {
      if (REFLECTION_ALIPAY) {
         try {
            XposedHelpers.findAndHookMethod(XposedHelpers.findClass("com.alipay.apmobilesecuritysdk.scanattack.common.ScanAttack", classLoader), "getAD104", Context.class, Integer.TYPE, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, String.class, new XC_MethodHook() {
               protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                  param.setResult((Object)null);
               }
            });
         } catch (Exception var2) {
         }

      }
   }
}
