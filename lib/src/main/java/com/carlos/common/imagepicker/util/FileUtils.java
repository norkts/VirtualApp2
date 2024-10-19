package com.carlos.common.imagepicker.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Build.VERSION;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import com.carlos.libcommon.StringFog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Locale;

public class FileUtils {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JT4YDmgYNAZjDlEp"));

   private FileUtils() {
   }

   public static boolean isExternalStorageDocument(Uri uri) {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmkgFgZrDgobLRgED2YaAjVpDjwuORgcKWggNAFoERoZJy5SVg==")).equals(uri.getAuthority());
   }

   public static boolean isDownloadsDocument(Uri uri) {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmowRSVvNx4vLhcMD04wFipqJB4bKQhbIGwnBjBqNzAaJgc2JWoVMFo=")).equals(uri.getAuthority());
   }

   public static boolean isMediaDocument(Uri uri) {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmowRSVvNx4vLhcMD04wQSBuHhoqORgcKWggNAFoERoZJy5SVg==")).equals(uri.getAuthority());
   }

   public static boolean isGooglePhotosUri(Uri uri) {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4+DmEVNyllHgYeIz4uD3UzLANqJCw0Jj4MVg==")).equals(uri.getAuthority());
   }

   public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
      Cursor cursor = null;
      String column = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4qP2wFJFo="));
      String[] projection = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4qP2wFJFo="))};

      String var8;
      try {
         cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, (String)null);
         if (cursor == null || !cursor.moveToFirst()) {
            return null;
         }

         int column_index = cursor.getColumnIndexOrThrow(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4qP2wFJFo=")));
         var8 = cursor.getString(column_index);
      } catch (IllegalArgumentException var12) {
         IllegalArgumentException ex = var12;
         Log.i(TAG, String.format(Locale.getDefault(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFJAZ9DCg1KhgMD29TIyhhJywsKgg9JE4OOFN+ASwM")), ex.getMessage()));
         return null;
      } finally {
         if (cursor != null) {
            cursor.close();
         }

      }

      return var8;
   }

   @SuppressLint({"NewApi"})
   public static String getPath(Context context, Uri uri) {
      boolean isKitKat = VERSION.SDK_INT >= 19;
      if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
         String id;
         String[] split;
         String type;
         if (isExternalStorageDocument(uri)) {
            id = DocumentsContract.getDocumentId(uri);
            split = id.split(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD5SVg==")));
            type = split[0];
            if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhcMCWoVJARnAVRF")).equalsIgnoreCase(type)) {
               return Environment.getExternalStorageDirectory() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + split[1];
            }
         } else if (isDownloadsDocument(uri)) {
            id = DocumentsContract.getDocumentId(uri);
            if (!TextUtils.isEmpty(id)) {
               try {
                  Uri contentUri = ContentUris.withAppendedId(Uri.parse(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmVgU1Oi02DWUFMCRlJzgvKToADmYKMDdvDiwOLz4uMWUzHgNrESwc"))), Long.valueOf(id));
                  return getDataColumn(context, contentUri, (String)null, (String[])null);
               } catch (NumberFormatException var9) {
                  NumberFormatException e = var9;
                  Log.i(TAG, e.getMessage());
                  return null;
               }
            }
         } else if (isMediaDocument(uri)) {
            id = DocumentsContract.getDocumentId(uri);
            split = id.split(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD5SVg==")));
            type = split[0];
            Uri contentUri = null;
            if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgIP2gzNFo=")).equals(type)) {
               contentUri = Media.EXTERNAL_CONTENT_URI;
            } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YPGgVGlo=")).equals(type)) {
               contentUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuPGUVGlo=")).equals(type)) {
               contentUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            String selection = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4YPH5TGlo="));
            String[] selectionArgs = new String[]{split[1]};
            return getDataColumn(context, contentUri, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4YPH5TGlo=")), selectionArgs);
         }
      } else {
         if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmEVRF")).equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
               return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, (String)null, (String[])null);
         }

         if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YDmgVSFo=")).equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
         }
      }

      return null;
   }

   public static void copyFile(@NonNull String pathFrom, @NonNull String pathTo) throws IOException {
      if (!pathFrom.equalsIgnoreCase(pathTo)) {
         FileChannel outputChannel = null;
         FileChannel inputChannel = null;

         try {
            inputChannel = (new FileInputStream(new File(pathFrom))).getChannel();
            outputChannel = (new FileOutputStream(new File(pathTo))).getChannel();
            inputChannel.transferTo(0L, inputChannel.size(), outputChannel);
            inputChannel.close();
         } finally {
            if (inputChannel != null) {
               inputChannel.close();
            }

            if (outputChannel != null) {
               outputChannel.close();
            }

         }

      }
   }
}
