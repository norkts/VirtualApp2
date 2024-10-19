package com.lody.virtual.helper.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class DrawableUtils {
   public static Bitmap drawableToBitMap(Drawable drawable) {
      if (drawable == null) {
         return null;
      } else if (drawable instanceof BitmapDrawable) {
         BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
         return bitmapDrawable.getBitmap();
      } else {
         Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Config.ARGB_8888 : Config.RGB_565);
         Canvas canvas = new Canvas(bitmap);
         drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
         drawable.draw(canvas);
         return bitmap;
      }
   }
}
