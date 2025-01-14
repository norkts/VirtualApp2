package com.lody.virtual.client.badger;

import android.content.ComponentName;
import android.content.Intent;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.BadgerInfo;

public abstract class BroadcastBadger2 implements IBadger {
   public abstract String getAction();

   public abstract String getComponentKey();

   public abstract String getCountKey();

   public BadgerInfo handleBadger(Intent intent) {
      BadgerInfo info = new BadgerInfo();
      String componentName = intent.getStringExtra(this.getComponentKey());
      ComponentName component = ComponentName.unflattenFromString(componentName);
      if (component != null) {
         info.packageName = component.getPackageName();
         info.className = component.getClassName();
         info.badgerCount = intent.getIntExtra(this.getCountKey(), 0);
         return info;
      } else {
         return null;
      }
   }

   static class NewHtcHomeBadger1 extends BroadcastBadger2 {
      public String getAction() {
         return "com.htc.launcher.action.SET_NOTIFICATION";
      }

      public String getComponentKey() {
         return "com.htc.launcher.extra.COMPONENT";
      }

      public String getCountKey() {
         return "com.htc.launcher.extra.COUNT";
      }
   }
}
