package com.carlos.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.carlos.libcommon.StringFog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PathUtils {
   public static String getRealPathFromURI(Context context, Uri contentUri) {
      String result = null;
      String[] proj = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4qP2wFJFo="))};
      Cursor cursor = context.getContentResolver().query(contentUri, proj, (String)null, (String[])null, (String)null);
      if (cursor != null) {
         if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4qP2wFJFo=")));
            result = cursor.getString(column_index);
         }

         cursor.close();
      }

      return result;
   }

   private File createFileFromInputStream(Context context, InputStream inputStream) throws IOException {
      File tempFile = File.createTempFile(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRguDW8LOC9gHjBF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz0qDW8FSFo=")), context.getCacheDir());
      OutputStream outputStream = new FileOutputStream(tempFile);
      byte[] buffer = new byte[1024];

      int length;
      while((length = inputStream.read(buffer)) != -1) {
         ((OutputStream)outputStream).write(buffer, 0, length);
      }

      ((OutputStream)outputStream).close();
      inputStream.close();
      return tempFile;
   }

   @SuppressLint({"Range"})
   private String getFileName(Context context, Uri uri) {
      String result = null;
      if (uri.getScheme().equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmEVRF")))) {
         Cursor cursor = context.getContentResolver().query(uri, (String[])null, (String)null, (String[])null, (String)null);

         try {
            if (cursor != null && cursor.moveToFirst()) {
               result = cursor.getString(cursor.getColumnIndex(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4qCW8wICR9AQZAKj0iD2kjSFo="))));
            }
         } finally {
            cursor.close();
         }
      }

      if (result == null) {
         result = uri.getPath();
         int cut = result.lastIndexOf(47);
         if (cut != -1) {
            result = result.substring(cut + 1);
         }
      }

      return result;
   }
}
