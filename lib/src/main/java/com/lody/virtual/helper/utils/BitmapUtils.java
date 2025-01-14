package com.lody.virtual.helper.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapUtils {
   public static Bitmap drawableToBitmap(Drawable drawable) {
      if (drawable instanceof BitmapDrawable) {
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

   public static Bitmap warrperIcon(Bitmap bitmap, int newWidth, int newHeight) {
      int width = bitmap.getWidth();
      int height = bitmap.getHeight();
      if (width >= newWidth && height >= newHeight) {
         float scaleWidth = (float)newWidth / (float)width;
         float scaleHeight = (float)newHeight / (float)height;
         Matrix matrix = new Matrix();
         matrix.postScale(scaleWidth, scaleHeight);
         return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
      } else {
         return bitmap;
      }
   }
}
