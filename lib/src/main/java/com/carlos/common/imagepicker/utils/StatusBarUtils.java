package com.carlos.common.imagepicker.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.annotation.ColorInt;
import androidx.appcompat.widget.Toolbar;
import com.carlos.libcommon.StringFog;
import java.lang.reflect.Field;

public class StatusBarUtils {
   private static final int DEFAULT_STATUS_BAR_ALPHA = 0;

   public static void setColor(Activity activity, @ColorInt int color) {
      setBarColor(activity, color);
   }

   public static void setBarColor(Activity activity, @ColorInt int color) {
      if (VERSION.SDK_INT >= 19) {
         Window win = activity.getWindow();
         View decorView = win.getDecorView();
         win.addFlags(67108864);
         if (VERSION.SDK_INT >= 21) {
            win.clearFlags(67108864);
            int option = 1280;
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | option);
            win.addFlags(Integer.MIN_VALUE);
            win.setStatusBarColor(color);
         }
      }

   }

   public static void setTransparent(Activity activity) {
      if (VERSION.SDK_INT >= 19) {
         setColor(activity, 0);
      }
   }

   public static void fixToolbar(Toolbar toolbar, Activity activity) {
      if (VERSION.SDK_INT >= 19) {
         int statusHeight = getStatusBarHeight(activity);
         ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)toolbar.getLayoutParams();
         layoutParams.setMargins(0, statusHeight, 0, 0);
      }

   }

   public static void fixTitlebar(ViewGroup titlebar, Activity activity) {
      if (VERSION.SDK_INT >= 19) {
         int statusHeight = getStatusBarHeight(activity);
         ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)titlebar.getLayoutParams();
         layoutParams.setMargins(0, statusHeight, 0, 0);
      }

   }

   @SuppressLint({"PrivateApi"})
   public static int getStatusBarHeight(Context context) {
      int statusBarHeight = 0;

      try {
         Class<?> c = Class.forName("com.android.internal.R$dimen");
         Object obj = c.newInstance();
         Field field = c.getField("status_bar_height");
         int x = Integer.parseInt(field.get(obj).toString());
         statusBarHeight = context.getResources().getDimensionPixelSize(x);
      } catch (Exception var7) {
         Exception e1 = var7;
         e1.printStackTrace();
      }

      return statusBarHeight;
   }

   private static int calculateStatusColor(@ColorInt int color, int alpha) {
      float a = 1.0F - (float)alpha / 255.0F;
      int red = color >> 16 & 255;
      int green = color >> 8 & 255;
      int blue = color & 255;
      red = (int)((double)((float)red * a) + 0.5);
      green = (int)((double)((float)green * a) + 0.5);
      blue = (int)((double)((float)blue * a) + 0.5);
      return -16777216 | red << 16 | green << 8 | blue;
   }
}
