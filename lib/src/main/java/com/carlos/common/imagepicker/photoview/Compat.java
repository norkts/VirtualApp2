package com.carlos.common.imagepicker.photoview;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.view.View;

class Compat {
   private static final int SIXTY_FPS_INTERVAL = 16;

   public static void postOnAnimation(View view, Runnable runnable) {
      if (VERSION.SDK_INT >= 16) {
         postOnAnimationJellyBean(view, runnable);
      } else {
         view.postDelayed(runnable, 16L);
      }

   }

   @TargetApi(16)
   private static void postOnAnimationJellyBean(View view, Runnable runnable) {
      view.postOnAnimation(runnable);
   }
}
