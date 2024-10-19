package com.carlos.common.reverse.grame;

import android.util.Log;
import com.carlos.common.reverse.ReflectionApplication;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import com.lody.virtual.helper.utils.VLog;
import de.robv.android.xposed.XposedHelpers;

public class Grame extends ReflectionApplication {
   private static final String TAG = "HV-DingTalk";

   public static void hook(ClassLoader classLoader) {
      if (REFLECTION_DTALK) {
         try {
            Class<?> Sentry = XposedHelpers.findClass("io.sentry.Sentry", classLoader);
            Class<?> SentryOptions = XposedHelpers.findClass("io.sentry.SentryOptions", classLoader);
            Class<?> SentryAndroid = XposedHelpers.findClass("io.sentry.android.core.SentryAndroid", classLoader);
            Class<?> AndroidLogger = XposedHelpers.findClass("io.sentry.android.core.AndroidLogger", classLoader);
            VLog.d(TAG, "=================================hook end===============================");
         } catch (Throwable var5) {
            Throwable throwable = var5;
            Log.e(TAG, "=================================hook===============================" + throwable.toString());
            HVLog.printThrowable(throwable);
         }

      }
   }
}
