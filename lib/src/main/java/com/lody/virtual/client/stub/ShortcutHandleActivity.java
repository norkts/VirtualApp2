package com.lody.virtual.client.stub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.AppLauncherCallback;
import com.lody.virtual.client.ipc.VActivityManager;
import java.net.URISyntaxException;

public class ShortcutHandleActivity extends Activity implements AppLauncherCallback {
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.finish();
      Intent intent = this.getIntent();
      if (intent != null) {
         int userId = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASg/IzxfMWk2NFo=")), 0);
         String splashUri = intent.getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9hJyQoLwgqMmMFSFo=")));
         String targetUri = intent.getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASwzJi5SVg==")));
         Intent splashIntent = null;
         Intent targetIntent = null;
         URISyntaxException e;
         if (splashUri != null) {
            try {
               splashIntent = Intent.parseUri(splashUri, 0);
            } catch (URISyntaxException var11) {
               e = var11;
               e.printStackTrace();
            }
         }

         if (targetUri != null) {
            try {
               targetIntent = Intent.parseUri(targetUri, 0);
            } catch (URISyntaxException var10) {
               e = var10;
               e.printStackTrace();
            }
         }

         if (targetIntent != null) {
            if (VERSION.SDK_INT >= 15) {
               targetIntent.setSelector((Intent)null);
            }

            if (splashIntent == null) {
               try {
                  VActivityManager.get().startActivity(targetIntent, userId);
               } catch (Throwable var9) {
                  var9.printStackTrace();
               }
            } else {
               splashIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmsIRVRmDB4T")), targetIntent);
               splashIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmUmNFo=")), userId);
               this.startActivity(splashIntent);
            }

         }
      }
   }

   public boolean checkVerify() {
      return true;
   }

   public String currentActivity() {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYP28FPCNlJx0bKhgXKn0KND9vATgiIz01KmgzJCVoVhpMJAgqIG4zBh9uDhowKT4YLGkVSFo="));
   }
}
