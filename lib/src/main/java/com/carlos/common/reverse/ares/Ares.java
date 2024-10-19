package com.carlos.common.reverse.ares;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.carlos.common.reverse.hooker.DialogFragmentHooker;
import com.carlos.common.reverse.hooker.DialogHooker;
import com.carlos.common.reverse.hooker.GoogleServiceHooker;
import com.carlos.common.reverse.hooker.ThreadHooker;
import com.carlos.libcommon.StringFog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookConfig;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ares {
   public static void hook(ClassLoader classLoader, Application application, String packageName, String processName) {
      if (!StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mL2EjSFo=")).equals(packageName) && !StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mD2IzSFo=")).equals(packageName) && !StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmUVGiZrER4bLj5SVg==")).equals(packageName)) {
         Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4uPmowFitlASQsKhccP24gBi9lJxoAKS4uO2YaLz0=")) + packageName);
         testDev(application, packageName);
         initHooker(application, packageName);
      }
   }

   private static void testDev(Application application, String packageName) {
   }

   private static void initDSF(Application application, String mstr) {
   }

   private static void initHooker(Application application, String packageName) {
      SandHookConfig.DEBUG = false;
      List<Class> listHook = new ArrayList();
      listHook.add(DialogFragmentHooker.class);
      listHook.add(DialogHooker.class);
      if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojQTdjJCA1KC0iD2kgDSZoDgogKT5SVg==")).equals(packageName)) {
         listHook.add(GoogleServiceHooker.class);
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHi99ASQsOj4AVg==")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("GRsKGh8QQgFEAl9PEwhSVg==")), new XC_MethodReplacement() {
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OF5eIHkORDB3Vh0aPV8fIg==")));
               Thread.dumpStack();
               return null;
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHi99ASQsOg5AL15aOlIeKlocGSJaJA==")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cE2sVBiliDlFF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NSwaLRgEKWImGilqHgo7LxhbJWsFSFo=")), new XC_MethodReplacement() {
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cE2sVBiliDlFF")));
               return null;
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHi99ASQsOg47L1kBMlNaBDk/GRs5JA==")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cE2oFAiljJ1RF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NSwaLRgEKWImGilqHgo7LxhbJWsFSFo=")), Integer.TYPE, new XC_MethodReplacement() {
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cE2sVBiliDlFF")));
               return null;
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4+LmtSBiR9Dlk9OjwuLW8aBi9lAShF")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfM2szSFo=")), String[].class, String[].class, File.class, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4+LmtSBiR9Dlk9OjwuLW8aBi9lAShF")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfM2szSFo=")), String.class, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4+LmtSBiR9Dlk9OjwuLW8aBi9lAShF")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfM2szSFo=")), String.class, String[].class, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4+LmtSBiR9Dlk9OjwuLW8aBi9lAShF")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfM2szSFo=")), String[].class, String[].class, File.class, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4+LmtSBiR9Dlk9OjwuLW8aBi9lAShF")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfM2szSFo=")), String[].class, String[].class, File.class, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ij0uCGwFAiNiCiQ/LRcMP340AShsNyg6KhgECnczSFo=")) + param.getResult());
               super.afterHookedMethod(param);
            }
         });
         listHook.add(ThreadHooker.class);
         listHook.add(GoogleServiceHooker.class);
      }

      if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojTSVnDigzLBgbDmkKBlo=")).equals(packageName) || StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogPCtgDiAwKAMYD2wgRSM=")).equals(packageName) || StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLCVgDSwvIy0ADW8zBi9lNyA6PC1XJ2AwAilvDicdKC4qIGwzGgVoDRoyJi0YLmwaEQJsDlg5Iz4AKmoVJCQ=")).equals(packageName)) {
         listHook.add(GoogleServiceHooker.class);
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojRS9mNDM2LwgMLmwwGQZ1NTgwKghfX0wbJCBpNTgiLy0IGmoFJAJqNyBF")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MM2saMCtuJDA6JD0cPWULBi9oAQIcLj5SVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojRS9mNDM2LAcbDmQzLD5rDCAgLSsiI2IFJABsHhouKRccVg==")), new XC_MethodReplacement() {
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MM2saMCtuJDA6JD0cPWULBi9oAQIcLjo6Vg==")));
               return null;
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojAiZ9JCM2Iy0MP2UgRS9vHhEbIQcMKWcVBSlvDBoiIi42BWoLAgVlEVRF")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAYYCX0bAi9rDgYzIQhSVg==")), Context.class, Object.class, new XC_MethodReplacement() {
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhcMD2kKDShrDgYJKQccXmwhLC9iDTxF")));
               return null;
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojAiZ9JCM2Iy0MP2UgRS9vHhEbJC4+CmMFICB8MiQ5LDwICWgKFjBnDgo0IwguOm8FMCJuJywZOz4+LGUaOCs=")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgYXGUVAgljDAYzIQhSVg==")), Context.class, new XC_MethodReplacement() {
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgc6KGAwNDdhNAoCIz0ME2oKAi9sJCwsLC0qDH0FFixqNA0pIi42BWoFAlBlHx4wLQMiVg==")));
               return null;
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojAiZ9JCM2Iy0MP2UgRS9vHhEbIQcMKWcVBSlhASA5JwcAO2wzMFdvJwY5IBUmP2UaQT1uAV0ZLAgACA==")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgYXGUbAi9jDAYJIQhSVg==")), Integer.TYPE, new XC_MethodReplacement() {
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgc6KGAwNDdhNAoUIz1fDmUxQQJsEQIaLT4+CmMKAil5HBoiIi42I2IFAlBlHxE3")));
               return null;
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojAiZ9JCM2Iy0MP2UgRS9vHhEbIQcMKWcVBSlhASA5JwcAO2wzMFdvJwY5IBUmP2UaQT1uAV0ZLAgACA==")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAYYXGUVAi9jDAYzIQhSVg==")), new XC_MethodReplacement() {
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgc6KGAwNDdhNAoUIz1fDmUxQQJsEQIaLT4+CmMKAil5HhpNIi42I2oFAlBlHxE3CT5SVg==")));
               return null;
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojAiZ9JCM2Iy0MP2UgRS9vHhEbIQcMKWcVBSlhASA5JwcAO2wzMFdvJwY5IBUmP2UaQT1uAV0ZLAgACA==")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAYYXGUbAglrDAYzKQhSVg==")), new XC_MethodReplacement() {
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgc6KGAwNDdhNAoUIz1fDmUxQQJsEQIaLT4+CmMKAil5HhpNIi42I2oFAlBlHxE3CS5SVg==")));
               return null;
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojAiZ9JCM2Iy0MP2UgRS9vHhEbIQcMKWcVBSlhASA5JwcAO2wzMFdvJwY5IBUmP2UaQT1uAV0ZLAgACA==")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAYYCX0bAgljDgYzKQhSVg==")), new XC_MethodReplacement() {
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgc6KGAwNDdhNAoUIz1fDmUxQQJsEQIaLT4+CmMKAil5HhpNKiw2BWIFAgVlERE3CQhSVg==")));
               return null;
            }
         });
         XposedHelpers.findAndHookMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojAiZ9JCM2Iy0MP2UgRS9vHhEbIQcMKWcVBSlhASA5JwcAO2wzMFdvJwY5IBUmP2UaQT1uAV0ZLAgACA==")), application.getClassLoader(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAYYCX0bAgljDgYzKQhSVg==")), String.class, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAYYCX0bAgljDgYzKQMlLX4zSFo=")) + param.args[0].toString());
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAYYCX0bAgljDgYzKQMlLX4zSFo=")) + param.getResult());
               super.afterHookedMethod(param);
            }
         });
      }

      try {
         SandHook.addHookClass(application.getApplicationContext().getClassLoader(), (Class[])listHook.toArray(new Class[0]));
      } catch (Exception var4) {
         Exception e = var4;
         Log.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IixeOGAYFl93N1RF")) + e.getMessage());
         e.printStackTrace();
      }

   }
}
