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
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IgU+HG8jGj1hJDAqIRdfDWwLFgZvHjxF"));
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
      HVLog.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IgU+HG8jGj1hJDAqAhxYHEcvHwtiEQYcIzxfCmYVOFo=")));

      Exception e;
      Class RequestClass;
      try {
         RequestClass = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm8gBgZ1Nwo5LD0mD2IFMylqJBodLz4uMXU2NCZqDDwoJz0mJGUjSFo=")), this.mClassLoader);
         XposedBridge.hookAllConstructors(RequestClass, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
               Object object = param.thisObject;
               ClassUtil.printFieldsInClassAndObject(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQcMDmcFJAR9Dl0p")), object.getClass(), object);
            }
         });
      } catch (Exception var8) {
         e = var8;
         e.printStackTrace();
      }

      try {
         RequestClass = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4+LmtSBiZiAQ02IRg2Lmo2Gl99HzAcLC4cJ30jFixsJB5F")), this.mClassLoader);
         XposedBridge.hookAllConstructors(RequestClass, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
               if (param.args.length == 1 && param.args[0].getClass() == URL.class) {
                  URL url = (URL)param.args[0];
                  HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IgU+HG8jGj1hJDAqIRdfDWwLFgZvHjxF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBcqLG8INF9oHCg1Kj0YPW4KBi9lJx0xPQhSVg==")) + param.args[0] + "");
                  if (url.toString().contains(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OgM9KXokDT4=")))) {
                     StringBuilder TraceString = new StringBuilder("");
                     TraceString.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("P14DJH4JHSNOClw3OgNWD38nPyN1DQEePF8HL04OQCh8ClAcOSolL3UJHQF6Vx0pMjpeD3lTP1E="))).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Djo5JH4OGTROClw3OgNWD38nPyN1DQEePF8HL04OQCh8ClAcOSolL3UJHQF6VgE8MjpeD3g3Alo="))).append("\n");
                     HVLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IgU+HG8jGj1hJDAqIRdfDWwLFgZvHjxF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Bxw3W0YvLQpYXhM7Agk/DUUWJVc=")) + TraceString.toString());
                  }

               }
            }
         });
      } catch (Exception var7) {
         e = var7;
         e.printStackTrace();
      }

      try {
         RequestClass = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm4FNCNlAQYbPC5fCmYVOylkATA9JBgADW8FNCVsDwooJy02Vg==")), this.mClassLoader);
         XposedBridge.hookAllMethods(RequestClass, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPH0FNDdiHjAqIy5SVg==")), new XC_MethodHook() {
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
         RequestClass = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm4FNCNlAQYbPC5fCmYVOylkATA9JBgADW8FNCVsDwooJy02Vg==")), this.mClassLoader);
         XposedBridge.hookAllMethods(RequestClass, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPH0FNDdiHjAq")), new XC_MethodHook() {
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
         RequestClass = XposedHelpers.findClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogLAFmDiAqKAgMKn8VNCFqHiw/KQQcU2IFPDBuASw9")), this.mClassLoader);
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogLAFmDiAqKAgMKn8VNCFqHiw/KQQcA2MmBj9qESACKT42J2UwMFo=")), this.mClassLoader, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4uLWMzJCRgEVRF")), RequestClass, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
               Object param0 = param.args[0];
               ClassUtil.printFieldsInClassAndObject(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oy5bXWwKMAJlJFEzKAcYLn8VMCtvJTAsLAgDL2EaPDVpDlA5")), param0.getClass(), param0);
            }
         });
      } catch (Exception var4) {
         e = var4;
         e.printStackTrace();
      }

      try {
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm8gBgZ1MiAaLC4qKWYmNCpsASAeKRgAKm8bNCxsASg5Jy0AJmw2BgNqN1RF")), this.mClassLoader, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD5SVg==")), new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IgU+HG8jGj1hJDAqIRdfDWwLFgZvHjxF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IS4YCGgFGj1lJB43IxdfDmkjMAZjDlk/LhgcD2MKAilnDlE5ORg5LHoJIjICXiI/RBlEIxwEJFo=")));
            }
         });
      } catch (Exception var3) {
         e = var3;
         e.printStackTrace();
      }

      try {
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm8gBgZ1Nwo5LD0mD2IFMylpNygqORccKWUjHjZrHg05JRhSVg==")), this.mClassLoader, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cE2oFAiljJ1RF")), View.class, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IgU+HG8jGj1hJDAqIRdfDWwLFgZvHjxF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgAD2oFFjdhMFk0Oj1fDmYFOC9oJ1wZM184IhwWJitXEF8tW0QEVg==")));
            }
         });
      } catch (Exception var2) {
         e = var2;
         e.printStackTrace();
      }

   }
}
