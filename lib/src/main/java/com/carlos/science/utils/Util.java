package com.carlos.science.utils;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.View;
import com.carlos.libcommon.StringFog;

public class Util {
   public static void setBackground(View view, Drawable drawable) {
      if (VERSION.SDK_INT >= 16) {
         view.setBackground(drawable);
      } else {
         view.setBackgroundDrawable(drawable);
      }

   }

   public static boolean isOnePlus() {
      return getManufacturer().contains(StringFog.decrypt("HAsXBgkbLA=="));
   }

   public static String getManufacturer() {
      String manufacturer = Build.MANUFACTURER;
      manufacturer = manufacturer.toLowerCase();
      return manufacturer;
   }
}
