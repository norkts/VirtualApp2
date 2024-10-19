package com.carlos.common.reverse.epicgames;

import android.app.Application;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class EpicGames {
   public static void hook(ClassLoader classLoader, Application application) {
      HVLog.d(" 开始 Logger  ======================================================      2  ");
      final Class<?> Logger = XposedHelpers.findClass("com.epicgames.ue4.Logger", classLoader);
      XposedHelpers.callStaticMethod(Logger, "SuppressLogs");
      XposedHelpers.findAndHookMethod("com.epicgames.ue4.Logger", classLoader, "debug", String.class, new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            HVLog.d("epicgames 游戏 debug log:" + param.args[0]);
         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            boolean bAllowLogging = XposedHelpers.getStaticBooleanField(Logger, "bAllowLogging");
            boolean bAllowExceptionLogging = XposedHelpers.getStaticBooleanField(Logger, "bAllowExceptionLogging");
            HVLog.d("epicgames 游戏 debug log:" + param.args[0] + "    bAllowLogging:" + bAllowLogging + "    bAllowExceptionLogging:" + bAllowExceptionLogging);
            XposedHelpers.setStaticBooleanField(Logger, "bAllowLogging", true);
            XposedHelpers.setStaticBooleanField(Logger, "bAllowExceptionLogging", true);
         }
      });
      XposedHelpers.findAndHookMethod("com.epicgames.ue4.Logger", classLoader, "error", String.class, new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            HVLog.d("epicgames 游戏 error log:" + param.args[0] + "    result:" + param.getResult());
         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            HVLog.d("epicgames 游戏error log:" + param.args[0]);
         }
      });
      XposedHelpers.findAndHookMethod("com.epicgames.ue4.Logger", classLoader, "verbose", String.class, new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            HVLog.d("epicgames 游戏 verbose log:" + param.args[0]);
         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            boolean bAllowLogging = XposedHelpers.getStaticBooleanField(Logger, "bAllowLogging");
            boolean bAllowExceptionLogging = XposedHelpers.getStaticBooleanField(Logger, "bAllowExceptionLogging");
            HVLog.d("epicgames 游戏 verbose log:" + param.args[0] + "    bAllowLogging:" + bAllowLogging + "    bAllowExceptionLogging:" + bAllowExceptionLogging);
            XposedHelpers.setStaticBooleanField(Logger, "bAllowLogging", true);
            XposedHelpers.setStaticBooleanField(Logger, "bAllowExceptionLogging", true);
         }
      });
      XposedHelpers.findAndHookMethod("com.epicgames.ue4.Logger", classLoader, "warn", String.class, new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            HVLog.d("epicgames 游戏 warn log:" + param.args[0]);
         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            HVLog.d("epicgames 游戏 warn log:" + param.args[0]);
         }
      });
      XposedHelpers.findAndHookMethod("com.epicgames.ue4.GameActivity", classLoader, "AndroidThunkJava_GetMetaDataInt", String.class, new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            HVLog.d("epicgames 游戏 AndroidThunkJava_GetMetaDataInt log:" + param.args[0] + "    result:" + param.getResult());
         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            HVLog.d("epicgames 游戏 AndroidThunkJava_GetMetaDataInt log:" + param.args[0] + "    result:" + param.getResult());
         }
      });
      XposedHelpers.findAndHookMethod("com.epicgames.ue4.GameActivity", classLoader, "AndroidThunkJava_ForceQuit", new XC_MethodHook() {
         protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            HVLog.d("epicgames 游戏 AndroidThunkJava_ForceQuit ");
            HVLog.printInfo();
         }

         protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            HVLog.d("epicgames 游戏 AndroidThunkJava_ForceQuit log:");
         }
      });
   }
}
