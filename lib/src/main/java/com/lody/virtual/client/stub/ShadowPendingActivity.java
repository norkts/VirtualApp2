package com.lody.virtual.client.stub;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.VLog;

public class ShadowPendingActivity extends Activity {
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.finish();
      Intent intent = this.getIntent();
      ComponentUtils.IntentSenderInfo info = null;

      try {
         info = ComponentUtils.parseIntentSenderInfo(intent, true);
      } catch (Throwable var5) {
         Throwable e = var5;
         e.printStackTrace();
      }

      if (info != null && info.userId != -1) {
         ActivityInfo activityInfo = VirtualCore.get().resolveActivityInfo(info.intent, info.userId);
         if (activityInfo == null) {
            VLog.e(ShadowPendingActivity.class.getSimpleName(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4+CWoFNCxLEQo1PxguPWoFNCRvNysrIxgcCmIKRT97MCBF")) + intent);
            return;
         }

         if (info.callerActivity != null && !this.isTaskRoot()) {
            info.intent.addFlags(33554432);
            VActivityManager.get().startActivity(info.intent, activityInfo, info.callerActivity, info.options, (String)null, -1, info.targetPkg, info.userId);
         } else {
            info.intent.addFlags(268435456);
            VActivityManager.get().startActivity(info.intent, activityInfo, (IBinder)null, info.options, (String)null, -1, info.targetPkg, info.userId);
         }
      }

   }
}
