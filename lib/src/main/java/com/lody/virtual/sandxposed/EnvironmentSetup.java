package com.lody.virtual.sandxposed;

import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.swift.sandhook.xposedcompat.utils.FileUtils;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;
import java.util.Arrays;

public class EnvironmentSetup {
   public static void init(Context context, String packageName, String processName) {
      initSystemProp(context);
      initForWeChat(context, processName);
   }

   private static void initSystemProp(Context context) {
      System.setProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT1fKA==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OghSVg==")));
      System.setProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT1fKGYwNANiASxAKBccKA==")), (new File(context.getApplicationInfo().dataDir)).getParent());
      System.setProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+CGgKODBhEVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OghSVg==")));
   }

   private static void initForWeChat(Context context, String processName) {
      if (TextUtils.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm8jPFo=")), processName)) {
         File dataDir = new File(context.getApplicationInfo().dataDir);
         File tinker = new File(dataDir, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgYCGUzNAQ=")));
         File tinker_temp = new File(dataDir, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgYCGUzNARsJwo/KggmVg==")));
         File tinker_server = new File(dataDir, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgYCGUzNARsJyg/Iz4+PWoVSFo=")));

         try {
            FileUtils.delete(tinker);
            FileUtils.delete(tinker_temp);
            FileUtils.delete(tinker_server);
         } catch (Exception var7) {
         }

         final int mainProcessId = Process.myPid();
         XposedHelpers.findAndHookMethod(Process.class, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC4YDmoIIARgJCg/Iy4qVg==")), Integer.TYPE, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
               int pid = (Integer)param.args[0];
               if (pid == mainProcessId) {
                  StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                  if (stackTrace != null) {
                     StackTraceElement[] var4 = stackTrace;
                     int var5 = stackTrace.length;

                     for(int var6 = 0; var6 < var5; ++var6) {
                        StackTraceElement stackTraceElement = var4[var6];
                        if (stackTraceElement.getClassName().contains(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm8jPyZoDjw7")))) {
                           XposedBridge.log(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgfOGojGgZLESgvKQcqMWkzBSZ1MxpF")) + Arrays.toString(stackTrace));
                           param.setResult((Object)null);
                           break;
                        }
                     }

                  }
               }
            }
         });
      }
   }
}
