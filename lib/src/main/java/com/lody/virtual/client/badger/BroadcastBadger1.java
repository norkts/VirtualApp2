package com.lody.virtual.client.badger;

import android.content.Intent;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.BadgerInfo;

public abstract class BroadcastBadger1 implements IBadger {
   public abstract String getAction();

   public abstract String getPackageKey();

   public abstract String getClassNameKey();

   public abstract String getCountKey();

   public BadgerInfo handleBadger(Intent intent) {
      BadgerInfo info = new BadgerInfo();
      info.packageName = intent.getStringExtra(this.getPackageKey());
      if (this.getClassNameKey() != null) {
         info.className = intent.getStringExtra(this.getClassNameKey());
      }

      info.badgerCount = intent.getIntExtra(this.getCountKey(), 0);
      return info;
   }

   static class OPPOHomeBader extends BroadcastBadger1 {
      public String getAction() {
         return "com.oppo.unsettledevent";
      }

      public String getPackageKey() {
         return "pakeageName";
      }

      public String getClassNameKey() {
         return null;
      }

      public String getCountKey() {
         return "number";
      }
   }

   static class NewHtcHomeBadger2 extends BroadcastBadger1 {
      public String getAction() {
         return "com.htc.launcher.action.UPDATE_SHORTCUT";
      }

      public String getPackageKey() {
         return "packagename";
      }

      public String getClassNameKey() {
         return null;
      }

      public String getCountKey() {
         return "count";
      }
   }

   static class AospHomeBadger extends BroadcastBadger1 {
      public String getAction() {
         return "android.intent.action.BADGE_COUNT_UPDATE";
      }

      public String getPackageKey() {
         return "badge_count_package_name";
      }

      public String getClassNameKey() {
         return "badge_count_class_name";
      }

      public String getCountKey() {
         return "badge_count";
      }
   }

   static class AdwHomeBadger extends BroadcastBadger1 {
      public String getAction() {
         return "org.adw.launcher.counter.SEND";
      }

      public String getPackageKey() {
         return "PNAME";
      }

      public String getClassNameKey() {
         return "CNAME";
      }

      public String getCountKey() {
         return "COUNT";
      }
   }

   static class LGHomeBadger extends BroadcastBadger1 {
      public String getAction() {
         return "android.intent.action.BADGE_COUNT_UPDATE";
      }

      public String getPackageKey() {
         return "badge_count_package_name";
      }

      public String getClassNameKey() {
         return "badge_count_class_name";
      }

      public String getCountKey() {
         return "badge_count";
      }
   }
}
