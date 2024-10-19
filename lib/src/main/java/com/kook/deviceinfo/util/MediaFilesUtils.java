package com.kook.deviceinfo.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import androidx.core.app.ActivityCompat;
import com.kook.deviceinfo.DeviceApplication;
import java.io.File;

public class MediaFilesUtils {
   public static boolean isSDCardEnableByEnvironment() {
      return "mounted".equals(Environment.getExternalStorageState());
   }

   public static int getImagesInternal() {
      String[] projection = new String[]{"_data"};
      int count = getDataCount(Media.INTERNAL_CONTENT_URI, projection);
      return count;
   }

   public static int getImagesExternal() {
      String[] projection = new String[]{"_data"};
      int count = getDataCount(Media.EXTERNAL_CONTENT_URI, projection);
      return count;
   }

   public static int getAudioInternal() {
      String[] projection = new String[]{"_data"};
      int count = getDataCount(android.provider.MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection);
      return count;
   }

   public static int getAudioExternal() {
      String[] projection = new String[]{"_data"};
      int count = getDataCount(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection);
      return count;
   }

   public static int getVideoInternal() {
      String[] projection = new String[]{"_data"};
      int count = getDataCount(android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI, projection);
      return count;
   }

   public static int getVideoExternal() {
      String[] projection = new String[]{"_data"};
      int count = getDataCount(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection);
      return count;
   }

   public static int getDownloadFilesCount() {
      try {
         return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles().length;
      } catch (Exception var1) {
         return -1;
      }
   }

   public static int getDataCount(Uri uri, String[] projection) {
      if (ActivityCompat.checkSelfPermission(DeviceApplication.getApp(), "android.permission.READ_EXTERNAL_STORAGE") != 0) {
         return 0;
      } else {
         int count = 0;
         ContentResolver contentResolver = DeviceApplication.getApp().getContentResolver();
         Cursor cursor = contentResolver.query(uri, projection, (String)null, (String[])null, (String)null);
         if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
         }

         return count;
      }
   }

   private static String getAbsolutePath(File file) {
      return file == null ? "" : file.getAbsolutePath();
   }
}
