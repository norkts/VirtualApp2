package com.carlos.science.utils;

import android.content.Context;

public class DensityUtil {
   public static int dip2px(Context context, float dpValue) {
      float scale = getScale(context);
      return (int)(dpValue * scale + 0.5F);
   }

   public static int px2dip(Context context, float pxValue) {
      float scale = getScale(context);
      return (int)(pxValue / scale + 0.5F);
   }

   public static int px2sp(Context context, float pxValue) {
      float fontScale = getScale(context);
      return (int)(pxValue / fontScale + 0.5F);
   }

   public static int sp2px(Context context, float spValue) {
      float fontScale = getScale(context);
      return (int)(spValue * fontScale + 0.5F);
   }

   private static float getScale(Context context) {
      float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
      return findScale(fontScale);
   }

   private static float findScale(float scale) {
      if (scale <= 1.0F) {
         scale = 1.0F;
      } else if ((double)scale <= 1.5) {
         scale = 1.5F;
      } else if (scale <= 2.0F) {
         scale = 2.0F;
      } else if (scale <= 3.0F) {
         scale = 3.0F;
      }

      return scale;
   }
}
