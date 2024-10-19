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
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojGgJhHhE2LAcYL2kgBgZlESgvLhciJ2AzFlo="));
      }

      public String getPackageKey() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+MWgVJC1iDFk7KgcMVg=="));
      }

      public String getClassNameKey() {
         return null;
      }

      public String getCountKey() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWsjNAQ="));
      }
   }

   static class NewHtcHomeBadger2 extends BroadcastBadger1 {
      public String getAction() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojRQZ9IFkoLwgMDm4FFitsMxosLT0qI2AgRCliDyBIICscGWEmLFFiMgpOIyw2WA=="));
      }

      public String getPackageKey() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iDlk7KgcMVg=="));
      }

      public String getClassNameKey() {
         return null;
      }

      public String getCountKey() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4AI2ogMFo="));
      }
   }

   static class AospHomeBadger extends BroadcastBadger1 {
      public String getAction() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42MA5mHDxJJQYYA2cLBgpmMihTLBUmWGYFSFo="));
      }

      public String getPackageKey() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+PGgzNB99JB4vKj42HWozQSlqJzguLhUAKn0KQSA="));
      }

      public String getClassNameKey() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+PGgzNB99JB4vKj42HW4FODdsJDAMLC4+L2IFSFo="));
      }

      public String getCountKey() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+PGgzNB99JB4vKj42Vg=="));
      }
   }

   static class AdwHomeBadger extends BroadcastBadger1 {
      public String getAction() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy0MPXojJCxmIFkoLwgMDm4FFitsMxoqLD0uKmYaLDV8NSxJIRYcVg=="));
      }

      public String getPackageKey() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhYcEWIbNFo="));
      }

      public String getClassNameKey() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JiwcEWIbNFo="));
      }

      public String getCountKey() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JiwABWImMFo="));
      }
   }

   static class LGHomeBadger extends BroadcastBadger1 {
      public String getAction() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42MA5mHDxJJQYYA2cLBgpmMihTLBUmWGYFSFo="));
      }

      public String getPackageKey() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+PGgzNB99JB4vKj42HWozQSlqJzguLhUAKn0KQSA="));
      }

      public String getClassNameKey() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+PGgzNB99JB4vKj42HW4FODdsJDAMLC4+L2IFSFo="));
      }

      public String getCountKey() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+PGgzNB99JB4vKj42Vg=="));
      }
   }
}
