package com.carlos.common.ui.activity.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carlos.libcommon.StringFog;
import com.lody.virtual.helper.compat.PermissionCompat;

@TargetApi(23)
public class PermissionRequestActivity extends Activity {
   private static final int REQUEST_PERMISSION_CODE = 995;
   private static final String EXTRA_PERMISSIONS = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfLG8jJyZhHjAqKgccL2oFLCVlN1RF"));
   private static final String EXTRA_APP_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfLG8jJyZ9ASQsJi0YOW8jGlo="));
   private static final String EXTRA_USER_ID = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfLG8jJyZmASg/IzxfMWkzSFo="));
   private static final String EXTRA_PACKAGE_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfLG8jJyZhHiA5KS0iM2kmNCZoAQ4g"));
   private int userId;
   private String appName;
   private String packageName;

   public static void requestPermission(@NonNull Activity activity, @NonNull String[] permissions, @NonNull String appName, int userId, @NonNull String packageName, int requestCode) {
      Intent intent = new Intent(activity, PermissionRequestActivity.class);
      intent.putExtra(EXTRA_PERMISSIONS, permissions);
      intent.putExtra(EXTRA_APP_NAME, appName);
      intent.putExtra(EXTRA_PACKAGE_NAME, packageName);
      intent.putExtra(EXTRA_USER_ID, userId);
      activity.startActivityForResult(intent, requestCode);
      activity.overridePendingTransition(0, 0);
   }

   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Intent intent = this.getIntent();
      String[] permissions = intent.getStringArrayExtra(EXTRA_PERMISSIONS);
      this.appName = intent.getStringExtra(EXTRA_APP_NAME);
      this.packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
      this.userId = intent.getIntExtra(EXTRA_USER_ID, -1);
      this.requestPermissions(permissions, 995);
   }

   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      if (PermissionCompat.isRequestGranted(grantResults)) {
         Intent data = new Intent();
         data.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhbPQ==")), this.packageName);
         data.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28mGi9iEVRF")), this.userId);
         this.setResult(-1, data);
      } else {
         this.runOnUiThread(() -> {
            Toast.makeText(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Bxk3D0ZJWiA=")) + this.appName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxwnL0MWPSs=")), 0).show();
         });
      }

      this.finish();
   }
}
