package com.lody.virtual.client.stub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.lody.virtual.helper.utils.ComponentUtils;

public class ShadowPendingReceiver extends BroadcastReceiver {
   public void onReceive(Context context, Intent intent) {
      ComponentUtils.IntentSenderInfo info = null;

      try {
         info = ComponentUtils.parseIntentSenderInfo(intent, false);
      } catch (Throwable var5) {
         Throwable e = var5;
         e.printStackTrace();
      }

      if (info != null && info.userId != -1) {
         Intent redirectIntent = ComponentUtils.proxyBroadcastIntent(info.intent, info.userId);
         context.sendBroadcast(redirectIntent);
      }

   }
}
