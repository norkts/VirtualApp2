package com.carlos.common.imagepicker.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageCaptureManager {
   private static final String CAPTURED_PHOTO_PATH_KEY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwY2I28gFitgNwoCKRdfLm8ITTdvEVlF"));
   public static final String PHOTO_PATH = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhfD2wFGh9hHiAgKRhSVg=="));
   private String mCurrentPhotoPath;
   private Context mContext;

   public ImageCaptureManager(Context mContext) {
      this.mContext = mContext;
   }

   private File createImageFile() throws IOException {
      String timeStamp = (new SimpleDateFormat(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KAcYJ2kbEg1iHgpAIRUAD28gAgM=")), Locale.ENGLISH)).format(new Date());
      String imageFileName = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JDs6WGA2Glo=")) + timeStamp + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5XKGgzSFo="));
      File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
      if (!storageDir.exists() && !storageDir.mkdir()) {
         Log.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRY+Wg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRhfKmowPC9gNDs8IAguKG8KRQN1Mx0bPC5SVg==")));
         throw new IOException();
      } else {
         File image = new File(storageDir, imageFileName);
         this.mCurrentPhotoPath = image.getAbsolutePath();
         return image;
      }
   }

   public Intent dispatchTakePictureIntent() throws IOException {
      Intent takePictureIntent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k3KAc2MW4nMDdoJCwaLD4bKmsIQQ5mIgoOIAZbQGcYNABgEVRF")));
      if (takePictureIntent.resolveActivity(this.mContext.getPackageManager()) != null) {
         File file = this.createImageFile();
         Uri photoFile = null;
         if (VERSION.SDK_INT >= 24) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4qP2wFJFo=")), file.getAbsolutePath());
            Uri uri = this.mContext.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
            takePictureIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy0uLG8KNAY=")), uri);
         } else {
            photoFile = Uri.fromFile(file);
         }

         if (photoFile != null) {
            takePictureIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy0uLG8KNAY=")), photoFile);
         }

         takePictureIntent.putExtra(PHOTO_PATH, file.getAbsolutePath());
      }

      return takePictureIntent;
   }

   public void galleryAddPic() {
      Intent mediaScanIntent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42QQpmHBoAJQUYH2ALBl9gHAoALysuAmQxRVVkJSQK")));
      if (!TextUtils.isEmpty(this.mCurrentPhotoPath)) {
         File f = new File(this.mCurrentPhotoPath);
         Uri contentUri = Uri.fromFile(f);
         mediaScanIntent.setData(contentUri);
         this.mContext.sendBroadcast(mediaScanIntent);
      }
   }

   public String getCurrentPhotoPath() {
      return this.mCurrentPhotoPath;
   }

   public void onSaveInstanceState(Bundle savedInstanceState) {
      if (savedInstanceState != null && this.mCurrentPhotoPath != null) {
         savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, this.mCurrentPhotoPath);
      }

   }

   public void onRestoreInstanceState(Bundle savedInstanceState) {
      if (savedInstanceState != null && savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
         this.mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
      }

   }
}
