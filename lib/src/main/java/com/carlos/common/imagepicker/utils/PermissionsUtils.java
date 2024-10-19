package com.carlos.common.imagepicker.utils;

import android.app.Activity;
import android.os.Build.VERSION;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.carlos.libcommon.StringFog;

public class PermissionsUtils {
   public static boolean checkReadStoragePermission(Activity activity) {
      if (VERSION.SDK_INT < 16) {
         return true;
      } else {
         int readStoragePermissionState = ContextCompat.checkSelfPermission(activity, "android.permission.READ_EXTERNAL_STORAGE");
         boolean readStoragePermissionGranted = readStoragePermissionState == 0;
         if (!readStoragePermissionGranted) {
            ActivityCompat.requestPermissions(activity, PermissionsConstant.PERMISSIONS_EXTERNAL_READ, 2);
         }

         return readStoragePermissionGranted;
      }
   }

   @RequiresApi(
      api = 23
   )
   public static boolean checkWriteStoragePermission(Activity activity) {
      int writeStoragePermissionState = ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
      boolean writeStoragePermissionGranted = writeStoragePermissionState == 0;
      if (!writeStoragePermissionGranted) {
         activity.requestPermissions(PermissionsConstant.PERMISSIONS_EXTERNAL_WRITE, 3);
      }

      return writeStoragePermissionGranted;
   }

   @RequiresApi(
      api = 23
   )
   public static boolean checkCameraPermission(Activity activity) {
      int cameraPermissionState = ContextCompat.checkSelfPermission(activity, "android.permission.CAMERA");
      boolean cameraPermissionGranted = cameraPermissionState == 0;
      if (!cameraPermissionGranted) {
         activity.requestPermissions(PermissionsConstant.PERMISSIONS_CAMERA, 1);
      }

      return cameraPermissionGranted;
   }
}
