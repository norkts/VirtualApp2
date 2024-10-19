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
         this.intent = (Intent)stub.getParcelableExtra("_VA_|_intent_");
         this.info = (ActivityInfo)stub.getParcelableExtra("_VA_|_info_");
         this.userId = stub.getIntExtra("_VA_|_user_id_", -1);
         this.virtualToken = BundleCompat.getBinder(stub, "_VA_|_token_");
      } catch (Throwable var3) {
         Throwable e = var3;
         e.printStackTrace();
      }

   }

   public void saveToIntent(Intent stub) {
      stub.putExtra("_VA_|_intent_", this.intent);
      stub.putExtra("_VA_|_info_", this.info);
      stub.putExtra("_VA_|_user_id_", this.userId);
      BundleCompat.putBinder(stub, "_VA_|_token_", this.virtualToken);
   }
}
