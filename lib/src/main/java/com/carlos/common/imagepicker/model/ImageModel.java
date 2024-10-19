package com.carlos.common.imagepicker.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import com.carlos.common.imagepicker.entity.Folder;
import com.carlos.common.imagepicker.entity.Image;
import com.carlos.common.imagepicker.utils.StringUtils;
import com.carlos.libcommon.StringFog;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageModel {
   public static void loadImageForSDCard(final Context context, final DataCallback callback) {
      (new Thread(new Runnable() {
         public void run() {
            Uri mImageUri = Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = context.getContentResolver();
            Cursor mCursor = mContentResolver.query(mImageUri, new String[]{"_data", "_display_name", "date_added", "_id"}, (String)null, (String[])null, "date_added");
            ArrayList<Image> images = new ArrayList();
            if (mCursor != null) {
               while(true) {
                  if (!mCursor.moveToNext()) {
                     mCursor.close();
                     break;
                  }

                  String path = mCursor.getString(mCursor.getColumnIndex("_data"));
                  String name = mCursor.getString(mCursor.getColumnIndex("_display_name"));
                  long time = mCursor.getLong(mCursor.getColumnIndex("date_added"));
                  if (!".downloading".equals(ImageModel.getExtensionName(path))) {
                     images.add(new Image(path, time, name));
                  }
               }
            }

            Collections.reverse(images);
            callback.onSuccess(ImageModel.splitFolder(images));
         }
      })).start();
   }

   private static ArrayList<Folder> splitFolder(ArrayList<Image> images) {
      ArrayList<Folder> folders = new ArrayList();
      folders.add(new Folder("全部图片", images));
      if (images != null && !images.isEmpty()) {
         int size = images.size();

         for(int i = 0; i < size; ++i) {
            String path = ((Image)images.get(i)).getPath();
            String name = getFolderName(path);
            if (StringUtils.isNotEmptyString(name)) {
               Folder folder = getFolder(name, folders);
               folder.addImage((Image)images.get(i));
            }
         }
      }

      return folders;
   }

   private static String getExtensionName(String filename) {
      if (filename != null && filename.length() > 0) {
         int dot = filename.lastIndexOf(46);
         if (dot > -1 && dot < filename.length() - 1) {
            return filename.substring(dot + 1);
         }
      }

      return "";
   }

   private static String getFolderName(String path) {
      if (StringUtils.isNotEmptyString(path)) {
         String[] strings = path.split(File.separator);
         if (strings.length >= 2) {
            return strings[strings.length - 2];
         }
      }

      return "";
   }

   private static Folder getFolder(String name, List<Folder> folders) {
      if (!folders.isEmpty()) {
         int size = folders.size();

         for(int i = 0; i < size; ++i) {
            Folder folder = (Folder)folders.get(i);
            if (name.equals(folder.getName())) {
               return folder;
            }
         }
      }

      Folder newFolder = new Folder(name);
      folders.add(newFolder);
      return newFolder;
   }

   public interface DataCallback {
      void onSuccess(ArrayList<Folder> var1);
   }
}
