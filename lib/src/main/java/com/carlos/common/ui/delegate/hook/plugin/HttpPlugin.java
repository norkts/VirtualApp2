package com.carlos.common.ui.delegate.hook.plugin;

import android.app.Application;
import android.view.View;
import com.carlos.common.ui.delegate.hook.utils.ClassUtil;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.net.URL;

public class HttpPlugin {
   private static final String TAG = "QQBrowserHookHttp";
   ClassLoader mClassLoader;
   String mVersionName;
   boolean isHooking = false;

   public void hook(String packageName, String processName, Application application) {
      this.mClassLoader = application.getClassLoader();
      if (!this.isHooking) {
         this.isHooking = true;
         this.hookHttp();
      }
   }

   private void hookHttp() {
      HVLog.d(TAG, "QQBrowser开始HookHttp");

      Exception e;
      Class RequestClass;
      try {
         RequestClass = XposedHelpers.findClass("com.tencent.mtt.browser.window.UrlParams", this.mClassLoader);
         XposedBridge.hookAllConstructors(RequestClass, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
               Object object = param.thisObject;
               ClassUtil.printFieldsInClassAndObject("UrlParams", object.getClass(), object);
            }
         });
      } catch (Exception var8) {
         e = var8;
         e.printStackTrace();
      }

      try {
         RequestClass = XposedHelpers.findClass("java.net.HttpURLConnection", this.mClassLoader);
         XposedBridge.hookAllConstructors(RequestClass, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
               if (param.args.length == 1 && param.args[0].getClass() == URL.class) {
                  URL url = (URL)param.args[0];
                  HVLog.d("QQBrowserHookHttp", "HttpURLConnection: " + param.args[0] + "");
                  if (url.toString().contains("113.96")) {
                     StringBuilder TraceString = new StringBuilder("");
                     TraceString.append("<<<<------------------------------>>>>>  \n").append("\n <<<<------------------------------>>>>>").append("\n");
                     HVLog.e("QQBrowserHookHttp", "堆栈信息：" + TraceString.toString());
                  }

               }
            }
         });
      } catch (Exception var7) {
         e = var7;
         e.printStackTrace();
      }

      try {
         RequestClass = XposedHelpers.findClass("com.tencent.common.http.MttRequestBase", this.mClassLoader);
         XposedBridge.hookAllMethods(RequestClass, "addHeaders", new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
               Object thisObject = param.thisObject;
            }
         });
      } catch (Exception var6) {
         e = var6;
         e.printStackTrace();
      }

      try {
         RequestClass = XposedHelpers.findClass("com.tencent.common.http.MttRequestBase", this.mClassLoader);
         XposedBridge.hookAllMethods(RequestClass, "addHeader", new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
               Object thisObject = param.thisObject;
            }
         });
      } catch (Exception var5) {
         e = var5;
         e.printStackTrace();
      }

      try {
         RequestClass = XposedHelpers.findClass("com.squareup.okhttp.Request", this.mClassLoader);
         XposedHelpers.findAndHookMethod("com.squareup.okhttp.OkHttpClient", this.mClassLoader, "newCall", RequestClass, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
               Object param0 = param.args[0];
               ClassUtil.printFieldsInClassAndObject("OkHttpClient.newCall-param0", param0.getClass(), param0);
            }
         });
      } catch (Exception var4) {
         e = var4;
         e.printStackTrace();
      }

      try {
         XposedHelpers.findAndHookMethod("com.tencent.mtt.WindowComponentExtensionImp", this.mClassLoader, "j", new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
               HVLog.d("QQBrowserHookHttp", "WindowComponentExtensionImp.j() 执行了");
            }
         });
      } catch (Exception var3) {
         e = var3;
         e.printStackTrace();
      }

      try {
         XposedHelpers.findAndHookMethod("com.tencent.mtt.browser.bra.toolbar.h", this.mClassLoader, "onClick", View.class, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
               HVLog.d("QQBrowserHookHttp", "toolbar.h.onClick() 执行了");
            }
         });
      } catch (Exception var2) {
         e = var2;
         e.printStackTrace();
      }

   }
}
