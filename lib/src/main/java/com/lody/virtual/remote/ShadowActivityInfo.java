package com.lody.virtual.remote;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.compat.BundleCompat;

public class ShadowActivityInfo {
   public Intent intent;
   public ActivityInfo info;
   public int userId;
   public IBinder virtualToken;

   public ShadowActivityInfo(Intent intent, ActivityInfo info, int userId, IBinder virtualToken) {
      this.intent = intent;
      this.info = info;
      this.userId = userId;
      this.virtualToken = virtualToken;
   }

   public ShadowActivityInfo(Intent stub) {
      try {
         this.intent = (Intent)stub.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9jDlkgKAcYLmMFSFo=")));
         this.info = (ActivityInfo)stub.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9jDlk+KixfVg==")));
         this.userId = stub.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASg/IzxfMWk2NFo=")), -1);
         this.virtualToken = BundleCompat.getBinder(stub, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mHh4xKAcYHQ==")));
      } catch (Throwable var3) {
         Throwable e = var3;
         e.printStackTrace();
      }

   }

   public void saveToIntent(Intent stub) {
      stub.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9jDlkgKAcYLmMFSFo=")), this.intent);
      stub.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9jDlk+KixfVg==")), this.info);
      stub.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASg/IzxfMWk2NFo=")), this.userId);
      BundleCompat.putBinder(stub, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mHh4xKAcYHQ==")), this.virtualToken);
   }
}
