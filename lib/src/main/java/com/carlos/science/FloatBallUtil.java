package com.carlos.science;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.WindowManager;
import com.carlos.libcommon.StringFog;

public class FloatBallUtil {
   public static boolean inSingleActivity;

   public static WindowManager.LayoutParams getLayoutParams(Context context) {
      return getLayoutParams(context, false);
   }

   public static WindowManager.LayoutParams getLayoutParams(Context context, boolean listenBackEvent) {
      WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
      layoutParams.flags = 262696;
      if (listenBackEvent) {
         layoutParams.flags &= -9;
      }

      if (context != null && context instanceof Activity) {
         layoutParams.type = 2;
      } else {
         int sdkInt = VERSION.SDK_INT;
         if (sdkInt < 19) {
            layoutParams.type = 2002;
         } else if (sdkInt < 25) {
            if (StringFog.decrypt("KwwTGQgH").equalsIgnoreCase(Build.MANUFACTURER)) {
               layoutParams.type = 2002;
            } else {
               layoutParams.type = 2005;
            }
         } else if (sdkInt < 26) {
            layoutParams.type = 2002;
         } else {
            layoutParams.type = 2038;
         }
      }

      layoutParams.format = 1;
      layoutParams.gravity = 51;
      layoutParams.width = -2;
      layoutParams.height = -2;
      return layoutParams;
   }

   public static WindowManager.LayoutParams getStatusBarLayoutParams(Context context) {
      WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
      layoutParams.width = 0;
      layoutParams.height = 0;
      layoutParams.flags = 40;
      layoutParams.gravity = 51;
      if (context != null && context instanceof Activity) {
         layoutParams.type = 2;
      } else {
         int sdkInt = VERSION.SDK_INT;
         if (sdkInt < 19) {
            layoutParams.type = 2002;
         } else if (sdkInt < 25) {
            layoutParams.type = 2005;
         } else if (sdkInt < 26) {
            layoutParams.type = 2002;
         } else {
            layoutParams.type = 2038;
         }
      }

      return layoutParams;
   }
}
