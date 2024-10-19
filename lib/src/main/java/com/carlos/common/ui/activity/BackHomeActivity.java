package com.carlos.common.ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.carlos.libcommon.StringFog;
import java.util.Iterator;

public class BackHomeActivity extends Activity {
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      ActivityManager am = (ActivityManager)this.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZF")));
      boolean existTask = false;
      Class homeActivityClz = null;

      try {
         homeActivityClz = Class.forName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLDdhNFE1IykYMm8FPCt1NVkcLBguHX0jFixqNBo9Li5SVg==")));
      } catch (ClassNotFoundException var8) {
         ClassNotFoundException e = var8;
         e.printStackTrace();
      }

      ComponentName homeActivity = new ComponentName(this, homeActivityClz);
      Iterator var6 = am.getRunningTasks(Integer.MAX_VALUE).iterator();

      while(var6.hasNext()) {
         ActivityManager.RunningTaskInfo info = (ActivityManager.RunningTaskInfo)var6.next();
         if (info.baseActivity != null && info.baseActivity.equals(homeActivity)) {
            am.moveTaskToFront(info.id, 0);
            existTask = true;
            break;
         }

         if (info.topActivity != null && info.topActivity.equals(homeActivity)) {
            am.moveTaskToFront(info.id, 0);
            existTask = true;
            break;
         }
      }

      if (!existTask) {
         Intent intent = new Intent(this, homeActivityClz);
         intent.addFlags(268435456);
         intent.addFlags(131072);
         this.startActivity(intent);
      }

      this.finish();
   }
}
