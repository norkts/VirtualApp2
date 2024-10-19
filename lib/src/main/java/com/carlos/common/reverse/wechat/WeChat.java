package com.carlos.common.reverse.wechat;

import android.app.Application;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class WeChat {
   public static void hook(ClassLoader classLoader, Application application) {
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PlsrJBQNMS0cICQ0Ki1fCX4zAiVlDRo/LhgcJWIKRT98NFEcODpXVg==")));
      Class<?> logClz = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm8jPyZsJywiPC06KH0FFiFsJygcIz4uKWUaLwRiAQYy")), classLoader);
      Class<?> logImpClz = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm8jPyZsJywiPC06KH0FFiFsJygcIz4uKWUaLwRiAQYyDhVXJm4mBgNqN1RF")), classLoader);
      XposedHelpers.setStaticIntField(logClz, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxguLmgVHlo=")), 0);
      Object getLogLevel = XposedHelpers.callStaticMethod(logClz, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIFGi1oHjAuKAdbVg==")));
      XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm8jPyZsJywiPC06KH0FFiFsJygcIz4uKWUaLwRiAQYy")), classLoader, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIFGi1oHjAuKAdbVg==")), new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            param.setResult(0);
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl86CmozGiFLHig1KgMYLmkjMClrARo/PC4IL04zNC9vIB45KT5bCmszGiZqHiw6Ji1XOnw2QQFpDSwyLQcqQGozPA5iATw/Kl4lOmkFGgZkNyg6KhgECnczSFo=")) + param.getResult());
         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
         }
      });
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl86CmozGiFLHig1KgMYLmkjMClrARo/PC4IL0tTOCJuATBAKQgIAmsKOD9qDTxF")) + getLogLevel);
   }
}
