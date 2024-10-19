package com.carlos.common.reverse.dingding;

import android.content.Intent;
import android.util.Log;
import com.carlos.common.reverse.ReflectionApplication;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import com.lody.virtual.helper.utils.VLog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DingTalk extends ReflectionApplication {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDWAFAiZiJQo7KhcEVg=="));

   public static void hook(ClassLoader classLoader) {
      if (REFLECTION_DTALK) {
         try {
            Class<?> ActionRequest = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjDiw7Lz0hDm8zLC1qHiwsKQc5KmEzLClqHhocLyoqHWggMAVqNxpAJAgmPG4FMCI=")), classLoader);
            Class<?> Plugin = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjDiw7Lz0hDm8zLC1qHiwsKQc5KmEzLClqHhocLyoqQGUaNDFlERpF")), classLoader);
            Class<?> Method = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4+LmtSBiR9Dlk9Oj4uPWkVOCtoJC8bJBguCmMaAi8=")), classLoader);
            Class<?> TheOneActivityBase = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjDiw7Lz0hDm8zLC1qHiwsKQc5KmEzLClqHhocLyoqO2wzAiJoHg05KBccLGQgAjFmJx4ZLAciCWwKAhR9ASg/")), classLoader);
            XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjDiw7Lz0hDm4jMCxsNwYaLgQcIGMKRSJqHiQbKghfO2wjNwRgAR45JC4MKGwaHh5uJB40Jgg2LGUaOC9mEQZF")), classLoader, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cU2gaPAlgNwo/Kj42Vg==")), Intent.class, new XC_MethodHook() {
               public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  super.afterHookedMethod(methodHookParam);
                  VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDWAFAiZiJQo7KhcEVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRgYCGgwMDdgHg4QLwgqPWYjAgZqDiQaKgcXJEsaJCBqHBodIz4AKm9TTVo=")) + methodHookParam.getResult());
               }

               public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  super.beforeHookedMethod(methodHookParam);
               }
            });
            XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjDiw7Lz0hDm8zLC1qHiwsKQc5KmEzLClqHhocLyoqDmUaNDFlER05JQdfM24FNAJuJyc5IQcqCWoFSFo=")), classLoader, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQwNDc=")), ActionRequest, new XC_MethodHook() {
               public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  super.afterHookedMethod(methodHookParam);
                  VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDWAFAiZiJQo7KhcEVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwkdM0YEGwtZXhNLAhoBHngVSFo=")) + methodHookParam.getResult());
                  VLog.printStackTrace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQwNDc=")));
                  methodHookParam.setResult((Object)null);
               }

               public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  super.beforeHookedMethod(methodHookParam);
               }
            });
            XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjDiw7Lz0hDm8zLC1qHiwsKQc5KmEzLClqHhocLyoqDmUaNDFlER05JQdfM24FNAJuJyc5IQcqCWoFSFo=")), classLoader, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGILFl5uJzA7")), ActionRequest, new XC_MethodHook() {
               public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  super.afterHookedMethod(methodHookParam);
                  VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDWAFAiZiJQo7KhcEVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwkdM0YEGwtZXhNLAhoBHngVSFo=")) + methodHookParam.getResult());
                  VLog.printStackTrace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGILFl5uJzA7")));
                  methodHookParam.setResult((Object)null);
               }

               public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  methodHookParam.setResult((Object)null);
                  super.beforeHookedMethod(methodHookParam);
               }
            });
            Class<?> apiPermissionInfo = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjDiw7Lz0hDm4gRS9vNyg5PC06J2EwQSxlJywiKQgpKmUFGjBoEQU5IwgiIH0aFiRvJzAcKi4YD2ohAiZiNB5F")), classLoader);
            Class<?> apiPermissionCheckResult = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjDiw7Lz0hDm4gRS9vNyg5PC5bJ2EwRSBsVx4qLD41KmwjNDVsHgowIBgfJWEFPD1iNwYbIwgYKW8zAiVgMig0KAcqCWIVGgNvAQI/")), classLoader);
            final Object[] enumConstants = apiPermissionCheckResult.getEnumConstants();
            XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjDiw7Lz0hDm4gRS9vNyg5PC06J2EwQSxlJywiKQgpKmwjNCZsJx42JANfBW4KJDVlJyQZJgcuLGUFNCZmHgY5Lwg2MW8FMExsNwYzLxYYL2EaTVo=")), classLoader, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBg+KWcFNARgDgYpIy0cDW8VSFo=")), apiPermissionInfo, String.class, String.class, new XC_MethodHook() {
               public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  super.afterHookedMethod(methodHookParam);
               }

               public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  methodHookParam.setResult(enumConstants[0]);
               }
            });
         } catch (Throwable var8) {
            Throwable throwable = var8;
            Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRgYCGg2MDdgHgE8KRdfDWwJTStuETAgKQcqI2AgRD15EVRF")) + throwable.toString());
            HVLog.printThrowable(throwable);
         }

      }
   }

   private static void hookInterface(ClassLoader classLoader, Object pthis) {
      try {
         Class<?> previewCallback = classLoader.loadClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k0LwguPmUFQQRrDRoALRgIJ2EwPy99ESguIxg2J28hLDNqAQI1OwcuIg==")));
         Object obj_proxy = Proxy.newProxyInstance(classLoader, new Class[]{previewCallback}, new InvocationHandler() {
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
               HVLog.i(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwQHDXpSHSNOClw3OgNWD38nTSNrDiwZLD4pJA==")) + method.getName());
               return null;
            }
         });
         HVLog.i(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwguLGUFGixLV1w3OgNWD38nPyN1DQEePF8HL04OQCh8ClAcOSolL3UJHQF6VgE8CANaJHwOTAN/IyM8MwQHDXsFSFo=")));
         XposedHelpers.callMethod(pthis, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LghSVg==")), obj_proxy);
      } catch (NoClassDefFoundError var4) {
         NoClassDefFoundError fe = var4;
         HVLog.i(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4tOA==")) + fe.getMessage());
      } catch (Exception var5) {
         Exception e = var5;
         HVLog.i(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQQ6Vg==")) + e.getMessage());
      }

   }
}
