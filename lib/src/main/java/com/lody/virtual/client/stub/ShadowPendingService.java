package com.lody.virtual.client.stub;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.utils.ComponentUtils;

public class ShadowPendingService extends Service {
   public IBinder onBind(Intent intent) {
      return null;
   }

   public int onStartCommand(Intent intent, int flags, int startId) {
      this.stopSelf();
      ComponentUtils.IntentSenderInfo info = null;

      try {
         info = ComponentUtils.parseIntentSenderInfo(intent, false);
      } catch (Throwable var6) {
         Throwable e = var6;
         e.printStackTrace();
      }

      if (info != null && info.userId != -1) {
         VActivityManager.get().startService(this, info.intent, info.userId);
      }

      return 2;
   }
}
