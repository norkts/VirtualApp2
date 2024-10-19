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
   private static final String TAG = "HV-DingTalk";

   public static void hook(ClassLoader classLoader) {
      if (REFLECTION_DTALK) {
         try {
            Class<?> ActionRequest = XposedHelpers.findClass("com.alibaba.lightapp.runtime.ActionRequest", classLoader);
            Class<?> Plugin = XposedHelpers.findClass("com.alibaba.lightapp.runtime.Plugin", classLoader);
            Class<?> Method = XposedHelpers.findClass("java.lang.reflect.Method", classLoader);
            Class<?> TheOneActivityBase = XposedHelpers.findClass("com.alibaba.lightapp.runtime.ariver.TheOneActivityBase", classLoader);
            XposedHelpers.findAndHookMethod("com.alibaba.android.dingtalkbase.DingtalkBaseActivity", classLoader, "onNewIntent", Intent.class, new XC_MethodHook() {
               public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  super.afterHookedMethod(methodHookParam);
                  VLog.d("HV-DingTalk", "DingtalkBaseActivity  getIntent:" + methodHookParam.getResult());
               }

               public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  super.beforeHookedMethod(methodHookParam);
               }
            });
            XposedHelpers.findAndHookMethod("com.alibaba.lightapp.runtime.plugin.internal.Util", classLoader, "getWua", ActionRequest, new XC_MethodHook() {
               public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  super.afterHookedMethod(methodHookParam);
                  VLog.d("HV-DingTalk", "查看返回:" + methodHookParam.getResult());
                  VLog.printStackTrace("getWua");
                  methodHookParam.setResult((Object)null);
               }

               public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  super.beforeHookedMethod(methodHookParam);
               }
            });
            XposedHelpers.findAndHookMethod("com.alibaba.lightapp.runtime.plugin.internal.Util", classLoader, "getLBSWua", ActionRequest, new XC_MethodHook() {
               public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  super.afterHookedMethod(methodHookParam);
                  VLog.d("HV-DingTalk", "查看返回:" + methodHookParam.getResult());
                  VLog.printStackTrace("getLBSWua");
                  methodHookParam.setResult((Object)null);
               }

               public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  methodHookParam.setResult((Object)null);
                  super.beforeHookedMethod(methodHookParam);
               }
            });
            Class<?> apiPermissionInfo = XposedHelpers.findClass("com.alibaba.ariver.permission.model.ApiPermissionInfo", classLoader);
            Class<?> apiPermissionCheckResult = XposedHelpers.findClass("com.alibaba.ariver.kernel.api.security.ApiPermissionCheckResult", classLoader);
            final Object[] enumConstants = apiPermissionCheckResult.getEnumConstants();
            XposedHelpers.findAndHookMethod("com.alibaba.ariver.permission.service.DefaultAuthenticationProxyImpl", classLoader, "hasPermission", apiPermissionInfo, String.class, String.class, new XC_MethodHook() {
               public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  super.afterHookedMethod(methodHookParam);
               }

               public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                  methodHookParam.setResult(enumConstants[0]);
               }
            });
         } catch (Throwable var8) {
            Throwable throwable = var8;
            Log.e(TAG, "DingTalk hook exception: " + throwable.toString());
            HVLog.printThrowable(throwable);
         }

      }
   }

   private static void hookInterface(ClassLoader classLoader, Object pthis) {
      try {
         Class<?> previewCallback = classLoader.loadClass("android.hardware.Camera$PreviewCallback");
         Object obj_proxy = Proxy.newProxyInstance(classLoader, new Class[]{previewCallback}, new InvocationHandler() {
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
               HVLog.i("------------- method " + method.getName());
               return null;
            }
         });
         HVLog.i("method ----------------------------------------- ");
         XposedHelpers.callMethod(pthis, "a", obj_proxy);
      } catch (NoClassDefFoundError var4) {
         NoClassDefFoundError fe = var4;
         HVLog.i("fe " + fe.getMessage());
      } catch (Exception var5) {
         Exception e = var5;
         HVLog.i("e " + e.getMessage());
      }

   }
}
