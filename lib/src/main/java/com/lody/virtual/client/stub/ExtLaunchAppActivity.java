package com.lody.virtual.client.stub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.ipc.VActivityManager;

public class ExtLaunchAppActivity extends Activity {
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.finish();
      Intent intent = this.getIntent();
      if (intent != null) {
         int userid = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASg/Iz0cPmMFSFo=")), -1);
         String pkg = intent.getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9hHiA5KS0iM2kmNFo=")));
         if (userid >= 0 && pkg != null && !pkg.isEmpty()) {
            VActivityManager.get().launchApp(userid, pkg);
         }
      }

   }
}
