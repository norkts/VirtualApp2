package com.lody.virtual;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.swift.sandhook.HookLog;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookConfig;
import com.swift.sandhook.xposedcompat.XposedCompat;
import com.swift.sandhook.xposedcompat.utils.DexMakerUtils;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;
import java.lang.reflect.Member;

public class PineXposed {
   private static XC_MethodHook PendingIntentFlagsHook = new XC_MethodHook() {
      protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
         int flags = (Integer)param.args[3];
         if ((flags & 67108864) == 0 && (33554432 & flags) == 0) {
            param.args[3] = 67108864 | flags;
         }

      }
   };

   public static void init() {
      SandHookConfig.DEBUG = false;
      HookLog.DEBUG = SandHookConfig.DEBUG;
      SandHookConfig.compiler = SandHookConfig.SDK_INT < 26;
      SandHookConfig.delayHook = false;
      SandHook.setHookModeCallBack(new SandHook.HookModeCallBack() {
         public int hookMode(Member originMethod) {
            return VERSION.SDK_INT >= 30 ? 2 : 0;
         }
      });
      SandHook.disableVMInline();
      XposedCompat.cacheDir = new File(VirtualCore.get().getContext().getCacheDir(), "sandhook_cache_general");
   }

   public static void fixPendingIntentFlags(Context context) {
      if (BuildCompat.isS()) {
         XposedHelpers.findAndHookMethod(PendingIntent.class, "getActivityAsUser", Context.class, Integer.TYPE, Intent.class, Integer.TYPE, Bundle.class, UserHandle.class, PendingIntentFlagsHook);
         XposedHelpers.findAndHookMethod(PendingIntent.class, "getActivitiesAsUser", Context.class, Integer.TYPE, Intent[].class, Integer.TYPE, Bundle.class, UserHandle.class, PendingIntentFlagsHook);
         XposedHelpers.findAndHookMethod(PendingIntent.class, "getBroadcastAsUser", Context.class, Integer.TYPE, Intent.class, Integer.TYPE, UserHandle.class, PendingIntentFlagsHook);
         XposedHelpers.findAndHookMethod(PendingIntent.class, "getForegroundService", Context.class, Integer.TYPE, Intent.class, Integer.TYPE, PendingIntentFlagsHook);
      }

   }

   public static void initForXposed(Context context, String processName) {
      XposedCompat.cacheDir = new File(context.getCacheDir(), DexMakerUtils.MD5(processName));
      fixPendingIntentFlags(context);
      if (BuildCompat.isS()) {
         try {
            XposedHelpers.findAndHookMethod("android.content.AttributionSource", context.getClassLoader(), "checkCallingUid", new XC_MethodReplacement() {
               protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                  return true;
               }
            });
         } catch (Throwable var5) {
         }
      }

      try {
         XposedBridge.hookAllMethods(MediaRecorder.class, "native_setup", new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
               MethodParameterUtils.replaceFirstAppPkg(param.args);
            }
         });
      } catch (Throwable var4) {
         Throwable throwable = var4;
         VLog.e("VA", throwable);
      }

      try {
         XposedHelpers.findAndHookMethod("android.view.WindowManagerGlobal", context.getClassLoader(), "addView", View.class, ViewGroup.LayoutParams.class, Display.class, Window.class, Integer.TYPE, new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
               param.args[4] = VUserHandle.realUserId();
            }
         });
      } catch (Throwable var3) {
      }

   }
}
