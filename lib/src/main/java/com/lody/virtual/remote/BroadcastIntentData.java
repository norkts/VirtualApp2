package com.lody.virtual.remote;

import android.content.Intent;
import com.lody.virtual.StringFog;

public class BroadcastIntentData {
   public int userId;
   public Intent intent;
   public String targetPackage;

   public BroadcastIntentData(int userId, Intent intent, String targetPackage) {
      this.userId = userId;
      this.intent = intent;
      this.targetPackage = targetPackage;
   }

   public BroadcastIntentData(Intent proxyIntent) {
      this.userId = proxyIntent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASg/IzxfMWk2NFo=")), -1);
      this.intent = (Intent)proxyIntent.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9jDlkgKAcYLmMFSFo=")));
      this.targetPackage = proxyIntent.getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mHiAqKC0MLmMKTSFrIgZF")));
   }

   public void saveIntent(Intent proxyIntent) {
      proxyIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASg/IzxfMWk2NFo=")), this.userId);
      proxyIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9jDlkgKAcYLmMFSFo=")), this.intent);
      proxyIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mHiAqKC0MLmMKTSFrIgZF")), this.targetPackage);
   }
}
