package com.carlos.common.reverse.grame;

import android.util.Log;
import com.carlos.common.reverse.ReflectionApplication;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import com.lody.virtual.helper.utils.VLog;
import de.robv.android.xposed.XposedHelpers;

public class Grame extends ReflectionApplication {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDWAFAiZiJQo7KhcEVg=="));

   public static void hook(ClassLoader classLoader) {
      if (REFLECTION_DTALK) {
         try {
            Class<?> Sentry = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgfCG8zNCZmESwZOjwqPW8aBgRuAVRF")), classLoader);
            Class<?> SentryOptions = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgfCG8zNCZmESwZOjwqPW8aBgRuDwY7KggYKWAzNFo=")), classLoader);
            Class<?> SentryAndroid = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgfCG8zNCZmESwZOj0iDmkwRSVqAS8bLT4ACGIORRBuDh49LBc2HWUzMCZqNx4z")), classLoader);
            Class<?> AndroidLogger = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgfCG8zNCZmESwZOj0iDmkwRSVqAS8bLT4ACGIORQ5sNDA7KQg2IH0VGjFoNygb")), classLoader);
            VLog.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwMHO35THTN0DVwdPgRWJXskPzN5CgEoOF4HP3QJQAJ4DVA2PSklP3kOHTdlAQY6JSkiLGwwLyl7ICMsPwMHO35THTN0DVwdPgRWJXskPzN5CgEoOF4HP3QJQAJ4DVA2")));
         } catch (Throwable var5) {
            Throwable throwable = var5;
            Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwMHO35THTN0DVwdPgRWJXskPzN5CgEoOF4HP3QJQAJ4DVA2PSklP3kOHTdlAQY6JSpaDngJTCl7ICMsPwMHO35THTN0DVwdPgRWJXskPzN5CgEoOF4HP3QJQVo=")) + throwable.toString());
            HVLog.printThrowable(throwable);
         }

      }
   }
}
