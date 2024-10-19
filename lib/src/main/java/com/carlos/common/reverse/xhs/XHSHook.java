package com.carlos.common.reverse.xhs;

import android.app.Application;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;

public class XHSHook {
   public static String XPOSED_MAIN = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgtCG8jGipmMFk7Kj02KG8FLCx1NFk7LD02J2JTRVBlHlk8Ly4cBmsFHiRoHgoc"));

   public static void hook(ClassLoader classLoader, Application application) {
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PlsrJBQNMS0cICQ0Ki1fCX4zAiVlDRozIxgcIWMKRClrHgY8ODpXVg==")));
      XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4+LmtSBiR9Dlk9OjwqLmoVLCZrJ1RF")), classLoader, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4AKmoVJAY=")), String.class, Object[].class, new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            String args0 = (String)param.args[0];
            Object[] args1 = (Object[])param.args[1];
            Object paramResult = param.getResult();
            if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyViHiAgLwNePWoFSFo=")).equals(args0)) {
               HVLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBgAD2UzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl86A2wKFi9gNDs2KD1fKG8jQQZ4EVRF")) + args0 + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OG8FJAR9Dl0AKAgqLW8wATI=")) + paramResult);
               if (!StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyViHiAgLwNfP28FPyZuER4bLj4YKk4zBitlJ1RF")).equals(paramResult)) {
                  param.setResult(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyViHiAgLwNfP28FPyZuER4bLj4YKk4zBitlIB4hKQguLQ==")));
                  boolean exists = (new File(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4qP2wFJyViHiAgLwNfP28FPyZuER4bLj4YKk4zBitlIB4hKQguLQ==")))).exists();
                  HVLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBgAD2UzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl86A2wKFi9gNDs2KD1fKG8jQQZ4Vj8cLgg+Cn0OAi9pATAqOQgYKWUJBixlERoyJQdeJWsaGiN/EQo6Iy5aOHsFNDBjASggIyoIVg==")) + exists);
               }
            }

         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
         }
      });
      XposedHelpers.findAndHookConstructor(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4+LmtSBiR9Dlk9OjwqLmoVLCZrJ1RF")), classLoader, byte[].class, new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            byte[] args = (byte[])param.args[0];
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwMHO35THTN0DVwdPgRWJXskPzN5CgEoOF4HP3QJQAJ4DVA2PSklP3kOEixvATA7ITkiJ2wgRQV4EVRF")));
            if (args != null) {
               String xpclz = new String(args);
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwMHO34aRQJ9JFEiPT5SVg==")) + xpclz);
            }

            super.afterHookedMethod(param);
         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
         }
      });
   }
}
