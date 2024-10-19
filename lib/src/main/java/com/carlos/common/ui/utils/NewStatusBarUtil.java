package com.carlos.common.ui.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build.VERSION;
import android.view.Window;
import android.view.WindowManager;
import com.carlos.libcommon.StringFog;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NewStatusBarUtil {
   @TargetApi(19)
   public static void transparencyBar(Activity activity) {
      Window window;
      if (VERSION.SDK_INT >= 21) {
         window = activity.getWindow();
         window.clearFlags(201326592);
         window.getDecorView().setSystemUiVisibility(1792);
         window.addFlags(Integer.MIN_VALUE);
         window.setStatusBarColor(0);
         window.setNavigationBarColor(0);
      } else if (VERSION.SDK_INT >= 19) {
         window = activity.getWindow();
         window.setFlags(67108864, 67108864);
      }

   }

   public static void setStatusBarColor(Activity activity, int colorId) {
      if (VERSION.SDK_INT >= 21) {
         Window window = activity.getWindow();
         window.setStatusBarColor(activity.getResources().getColor(colorId));
      } else if (VERSION.SDK_INT >= 19) {
         transparencyBar(activity);
         SystemBarTintManager tintManager = new SystemBarTintManager(activity);
         tintManager.setStatusBarTintEnabled(true);
         tintManager.setStatusBarTintResource(colorId);
      }

   }

   public static int StatusBarLightMode(Activity activity) {
      int result = 0;
      if (VERSION.SDK_INT >= 19) {
         if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
            result = 1;
         } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
            result = 2;
         }
      }

      return result;
   }

   public static void StatusBarLightMode(Activity activity, int type) {
      if (type == 1) {
         MIUISetStatusBarLightMode(activity.getWindow(), true);
      } else if (type == 2) {
         FlymeSetStatusBarLightMode(activity.getWindow(), true);
      }

   }

   public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
      boolean result = false;
      if (window != null) {
         Class clazz = window.getClass();

         try {
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
            if (dark) {
               extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {
               extraFlagField.invoke(window, 0, darkModeFlag);
            }

            result = true;
         } catch (Exception var8) {
         }
      }

      return result;
   }

   public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
      boolean result = false;
      if (window != null) {
         try {
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt((Object)null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
               value |= bit;
            } else {
               value &= ~bit;
            }

            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            result = true;
         } catch (Exception var8) {
         }
      }

      return result;
   }
}
