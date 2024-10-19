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
            XposedHelpers.findAndHookMethod(XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjASQ7LQMYOWozPCVoNx4dLhc2J30jLDVvATAyLAgcLXUwLDVrERooIBgMKGkgGQJuASg8IwgACHomLCl9DlkRLBg2OW4FJFo=")), classLoader), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMbMwFPVgpF")), Context.class, Integer.TYPE, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, String.class, new XC_MethodHook() {
               protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                  param.setResult((Object)null);
               }
            });
         } catch (Exception var2) {
         }

      }
   }
}
