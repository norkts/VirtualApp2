package com.lody.virtual.client.stub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.lody.virtual.StringFog;

public class BridgeActivity extends Activity {
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.finish();
      Intent intent = this.getIntent();
      if (intent != null) {
         Intent targetIntent = (Intent)intent.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9jDlkgKAcYLmMFSFo=")));
         Bundle targetOptions = (Bundle)intent.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh99NzA2KBdbPWMFSFo=")));
         if (targetIntent != null && targetOptions != null) {
            this.startActivity(targetIntent, targetOptions);
         }
      }

   }
}
