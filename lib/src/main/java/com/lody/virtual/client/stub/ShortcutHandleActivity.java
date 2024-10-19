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
         int userId = intent.getIntExtra("_VA_|_user_id_", 0);
         String splashUri = intent.getStringExtra("_VA_|_splash_");
         String targetUri = intent.getStringExtra("_VA_|_uri_");
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
               splashIntent.putExtra("android.intent.extra.INTENT", targetIntent);
               splashIntent.putExtra("android.intent.extra.CC", userId);
               this.startActivity(splashIntent);
            }

         }
      }
   }

   public boolean checkVerify() {
      return true;
   }

   public String currentActivity() {
      return "com.carlos.common.ui.activity.base.VerifyActivity";
   }
}
