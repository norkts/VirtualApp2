package com.carlos.common.imagepicker.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carlos.common.imagepicker.callback.BitmapLoadCallback;
import com.carlos.common.imagepicker.task.BitmapLoadTask;
import com.carlos.libcommon.StringFog;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class BitmapLoadUtils {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jj4YLGoVJAJoHh47KBYMLmwjOAM="));

   public static void decodeBitmapInBackground(@NonNull Context context, @NonNull Uri uri, @Nullable Uri outputUri, int requiredWidth, int requiredHeight, BitmapLoadCallback loadCallback) {
      (new BitmapLoadTask(context, uri, outputUri, requiredWidth, requiredHeight, loadCallback)).execute(new Void[0]);
   }

   public static Bitmap transformBitmap(@NonNull Bitmap bitmap, @NonNull Matrix transformMatrix) {
      try {
         Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), transformMatrix, true);
         if (!bitmap.sameAs(converted)) {
            bitmap = converted;
         }
      } catch (OutOfMemoryError var3) {
         OutOfMemoryError error = var3;
         Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcMP2ogLC5gJyw3Jz0cLm8jQQJ+MzxF")), error);
      }

      return bitmap;
   }

   public static int calculateInSampleSize(@NonNull BitmapFactory.Options options, int reqWidth, int reqHeight) {
      int height = options.outHeight;
      int width = options.outWidth;
      int inSampleSize = 1;
      if (height > reqHeight || width > reqWidth) {
         while(height / inSampleSize > reqHeight || width / inSampleSize > reqWidth) {
            inSampleSize *= 2;
         }
      }

      return inSampleSize;
   }

   public static int getExifOrientation(@NonNull Context context, @NonNull Uri imageUri) {
      int orientation = 0;

      try {
         InputStream stream = context.getContentResolver().openInputStream(imageUri);
         if (stream == null) {
            return orientation;
         }

         orientation = (new ImageHeaderParser(stream)).getOrientation();
         close(stream);
      } catch (IOException var4) {
         IOException e = var4;
         Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAaRS9iMh4qKQcMDmUzQQZqAQYbPyo6Vg==")) + imageUri.toString(), e);
      }

      return orientation;
   }

   public static int exifToDegrees(int exifOrientation) {
      short rotation;
      switch (exifOrientation) {
         case 3:
         case 4:
            rotation = 180;
            break;
         case 5:
         case 6:
            rotation = 90;
            break;
         case 7:
         case 8:
            rotation = 270;
            break;
         default:
            rotation = 0;
      }

      return rotation;
   }

   public static int exifToTranslation(int exifOrientation) {
      byte translation;
      switch (exifOrientation) {
         case 2:
         case 4:
         case 5:
         case 7:
            translation = -1;
            break;
         case 3:
         case 6:
         default:
            translation = 1;
      }

      return translation;
   }

   public static int calculateMaxBitmapSize(@NonNull Context context) {
      WindowManager wm = (WindowManager)context.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4YCGgFGj0=")));
      Point size = new Point();
      if (wm != null) {
         Display display = wm.getDefaultDisplay();
         display.getSize(size);
      }

      int width = size.x;
      int height = size.y;
      int maxBitmapSize = (int)Math.sqrt(Math.pow((double)width, 2.0) + Math.pow((double)height, 2.0));
      Canvas canvas = new Canvas();
      int maxCanvasSize = Math.min(canvas.getMaximumBitmapWidth(), canvas.getMaximumBitmapHeight());
      if (maxCanvasSize > 0) {
         maxBitmapSize = Math.min(maxBitmapSize, maxCanvasSize);
      }

      int maxTextureSize = EglUtils.getMaxTextureSize();
      if (maxTextureSize > 0) {
         maxBitmapSize = Math.min(maxBitmapSize, maxTextureSize);
      }

      Log.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+IGMjAgZgDiAsOy0cIGkkIyg=")) + maxBitmapSize);
      return maxBitmapSize;
   }

   public static void close(@Nullable Closeable c) {
      if (c != null && c instanceof Closeable) {
         try {
            c.close();
         } catch (IOException var2) {
         }
      }

   }
}
