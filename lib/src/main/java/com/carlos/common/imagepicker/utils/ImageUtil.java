package com.carlos.common.imagepicker.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build.VERSION;
import android.text.format.DateFormat;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class ImageUtil {
   public static String saveImage(Bitmap bitmap, String path) {
      String name = DateFormat.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KAcYJ2kbEg1iHgpAKRcAD28gAgM=")), Calendar.getInstance(Locale.CHINA)) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz06CGgzSFo="));
      FileOutputStream b = null;
      File file = new File(path);
      if (!file.exists()) {
         file.mkdirs();
      }

      String fileName = path + File.separator + name;

      try {
         b = new FileOutputStream(fileName);
         bitmap.compress(CompressFormat.JPEG, 75, b);
         String var18 = fileName;
         return var18;
      } catch (FileNotFoundException var16) {
         FileNotFoundException e = var16;
         e.printStackTrace();
      } finally {
         try {
            if (b != null) {
               b.flush();
               b.close();
            }
         } catch (IOException var15) {
            IOException e = var15;
            e.printStackTrace();
         }

      }

      return "";
   }

   public static Bitmap getBitmap(Context context, int vectorDrawableId) {
      Bitmap bitmap;
      if (VERSION.SDK_INT > 21) {
         Drawable vectorDrawable = context.getDrawable(vectorDrawableId);

         assert vectorDrawable != null;

         bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Config.ARGB_8888);
         Canvas canvas = new Canvas(bitmap);
         vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
         vectorDrawable.draw(canvas);
      } else {
         bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
      }

      return bitmap;
   }

   public static Bitmap zoomBitmap(Bitmap bm, int reqWidth, int reqHeight) {
      int width = bm.getWidth();
      int height = bm.getHeight();
      float scaleWidth = (float)reqWidth / (float)width;
      float scaleHeight = (float)reqHeight / (float)height;
      float scale = Math.min(scaleWidth, scaleHeight);
      Matrix matrix = new Matrix();
      matrix.postScale(scale, scale);
      Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
      return newbm;
   }

   public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
      int degree = 0;

      try {
         ExifInterface exifInterface = new ExifInterface(pathName);
         int result = exifInterface.getAttributeInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oy0MCWgVBgZ9AQozKi0YVg==")), 0);
         switch (result) {
            case 3:
               degree = 180;
               break;
            case 6:
               degree = 90;
               break;
            case 8:
               degree = 270;
         }
      } catch (IOException var8) {
         return null;
      }

      try {
         BitmapFactory.Options options = new BitmapFactory.Options();
         options.inJustDecodeBounds = true;
         BitmapFactory.decodeFile(pathName, options);
         options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
         options.inJustDecodeBounds = false;
         Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
         if (degree != 0) {
            Bitmap newBitmap = rotateImageView(bitmap, degree);
            bitmap.recycle();
            bitmap = null;
            return newBitmap;
         } else {
            return bitmap;
         }
      } catch (OutOfMemoryError var7) {
         Log.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQguMw==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Bxo/WEZaH1VYBS0WAxpYKEUWJRE=")));
         return null;
      }
   }

   public static Bitmap rotateImageView(Bitmap bitmap, int angle) {
      Matrix matrix = new Matrix();
      matrix.postRotate((float)angle);
      return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
   }

   private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
      int width = options.outWidth;
      int height = options.outHeight;
      int inSampleSize = 1;
      if (width > reqWidth && height > reqHeight) {
         int widthRatio = Math.round((float)width / (float)reqWidth);
         int heightRatio = Math.round((float)height / (float)reqHeight);
         inSampleSize = Math.max(widthRatio, heightRatio);
      }

      return inSampleSize;
   }
}
