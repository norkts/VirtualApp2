package com.lody.virtual.server.content;

import android.accounts.Account;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import com.lody.virtual.StringFog;
import java.util.Iterator;

public class SyncOperation implements Comparable {
   public static final int REASON_BACKGROUND_DATA_SETTINGS_CHANGED = -1;
   public static final int REASON_ACCOUNTS_UPDATED = -2;
   public static final int REASON_SERVICE_CHANGED = -3;
   public static final int REASON_PERIODIC = -4;
   public static final int REASON_IS_SYNCABLE = -5;
   public static final int REASON_SYNC_AUTO = -6;
   public static final int REASON_MASTER_SYNC_AUTO = -7;
   public static final int REASON_USER_START = -8;
   private static String[] REASON_NAMES = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRg+LGsYLCtmEQozKj06L2YFFjdlNyAgLghSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWowNCZmEShKIxc2OWUzGiw=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKmwjAiliDCg0LwcYM2kjBlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhguKmUVGixjDihF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAc2A2kVBil9DiwoKAhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JgcuLGo2LD9gNChF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Owg+KWwFNARpJwY2LysiLWUzNFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQc2M28mLAZ9ASwg"))};
   public final Account account;
   public final String authority;
   public final ComponentName service;
   public final int userId;
   public final int reason;
   public int syncSource;
   public final boolean allowParallelSyncs;
   public Bundle extras;
   public final String key;
   public boolean expedited;
   public SyncStorageEngine.PendingOperation pendingOperation;
   public long latestRunTime;
   public Long backoff;
   public long delayUntil;
   public long effectiveRunTime;
   public long flexTime;

   public SyncOperation(Account account, int userId, int reason, int source, String authority, Bundle extras, long runTimeFromNow, long flexTime, long backoff, long delayUntil, boolean allowParallelSyncs) {
      this.service = null;
      this.account = account;
      this.authority = authority;
      this.userId = userId;
      this.reason = reason;
      this.syncSource = source;
      this.allowParallelSyncs = allowParallelSyncs;
      this.extras = new Bundle(extras);
      this.cleanBundle(this.extras);
      this.delayUntil = delayUntil;
      this.backoff = backoff;
      long now = SystemClock.elapsedRealtime();
      if (runTimeFromNow >= 0L && !this.isExpedited()) {
         this.expedited = false;
         this.latestRunTime = now + runTimeFromNow;
         this.flexTime = flexTime;
      } else {
         this.expedited = true;
         this.latestRunTime = now;
         this.flexTime = 0L;
      }

      this.updateEffectiveRunTime();
      this.key = this.toKey();
   }

   private void cleanBundle(Bundle bundle) {
      this.removeFalseExtra(bundle, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc6DmozJCw=")));
      this.removeFalseExtra(bundle, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4AKmszNFo=")));
      this.removeFalseExtra(bundle, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgmCGowFitsJyg/LBg2MW8VEgM=")));
      this.removeFalseExtra(bundle, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgmCGowFitsJCw7Ly0EDWkVHlo=")));
      this.removeFalseExtra(bundle, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgAH2ojGgZsJyw/LBguIQ==")));
      this.removeFalseExtra(bundle, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgYKWszJARiHx4wKAdbPWUzLCVlNDBF")));
      this.removeFalseExtra(bundle, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfKGgVMC9mHjAw")));
      this.removeFalseExtra(bundle, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguDmgaMC9gJFkpJi1fLGkgRQRqASwg")));
      this.removeFalseExtra(bundle, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggEDmowPB9gDjAgKAguPWkzSFo=")));
      bundle.remove(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfKGgVLAZiDgpALAgmCG8FQSw=")));
      bundle.remove(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfKGgVLAZiDgpAKBdfI28VOCVoASxF")));
   }

   private void removeFalseExtra(Bundle bundle, String extraName) {
      if (!bundle.getBoolean(extraName, false)) {
         bundle.remove(extraName);
      }

   }

   SyncOperation(SyncOperation other) {
      this.service = other.service;
      this.account = other.account;
      this.authority = other.authority;
      this.userId = other.userId;
      this.reason = other.reason;
      this.syncSource = other.syncSource;
      this.extras = new Bundle(other.extras);
      this.expedited = other.expedited;
      this.latestRunTime = SystemClock.elapsedRealtime();
      this.flexTime = 0L;
      this.backoff = other.backoff;
      this.allowParallelSyncs = other.allowParallelSyncs;
      this.updateEffectiveRunTime();
      this.key = this.toKey();
   }

   public String toString() {
      return this.dump((PackageManager)null, true);
   }

   public String dump(PackageManager pm, boolean useOneLine) {
      StringBuilder sb = (new StringBuilder()).append(this.account.name).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhcuVg=="))).append(this.userId).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl9fVg=="))).append(this.account.type).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PAhSVg=="))).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186Vg=="))).append(this.authority).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186Vg=="))).append(SyncStorageEngine.SOURCES[this.syncSource]).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186DmsaMCthJwoALAcYAGwjPCt4EVRF"))).append(this.latestRunTime);
      if (this.expedited) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186WGEIIBVqHAZLIAU2Vg==")));
      }

      sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186KmgVJANgJFgiPxhSVg==")));
      sb.append(reasonToString(pm, this.reason));
      if (!useOneLine && !this.extras.keySet().isEmpty()) {
         sb.append("\n    ");
         extrasToStringBuilder(this.extras, sb);
      }

      return sb.toString();
   }

   public static String reasonToString(PackageManager pm, int reason) {
      if (reason >= 0) {
         if (pm != null) {
            String[] packages = pm.getPackagesForUid(reason);
            if (packages != null && packages.length == 1) {
               return packages[0];
            } else {
               String name = pm.getNameForUid(reason);
               return name != null ? name : String.valueOf(reason);
            }
         } else {
            return String.valueOf(reason);
         }
      } else {
         int index = -reason - 1;
         return index >= REASON_NAMES.length ? String.valueOf(reason) : REASON_NAMES[index];
      }
   }

   public boolean isMeteredDisallowed() {
      return this.extras.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggEDmowPB9gDjAgKAguPWkzSFo=")), false);
   }

   public boolean isInitialization() {
      return this.extras.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcCWwFAjdgHgYiKAhSVg==")), false);
   }

   public boolean isExpedited() {
      return this.extras.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfKGgVMC9mHjAw")), false) || this.expedited;
   }

   public boolean ignoreBackoff() {
      return this.extras.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgmCGowFitsJCw7Ly0EDWkVHlo=")), false);
   }

   private String toKey() {
      StringBuilder sb = new StringBuilder();
      if (this.service == null) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUFGgRjAQoZPTkmVg=="))).append(this.authority);
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg+OWszGgVgNw08LS0YOW8jBTM=")) + this.account.name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186I28zNAR0AVRF")) + this.userId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186LGkaICt0AVRF")) + this.account.type + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LwhSVg==")));
      } else {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uKmwjAiliCiQhIxciP2wFQS1rCg5F"))).append(this.service.getPackageName()).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhcuKWgaETM="))).append(this.userId).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186OWoFJANhI11F"))).append(this.service.getClassName()).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LwhSVg==")));
      }

      sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhguIGwKFjdhIwU8")));
      extrasToStringBuilder(this.extras, sb);
      return sb.toString();
   }

   public static void extrasToStringBuilder(Bundle bundle, StringBuilder sb) {
      sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg==")));
      Iterator var2 = bundle.keySet().iterator();

      while(var2.hasNext()) {
         String key = (String)var2.next();
         sb.append(key).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwhSVg=="))).append(bundle.get(key)).append(" ");
      }

      sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg==")));
   }

   public void updateEffectiveRunTime() {
      this.effectiveRunTime = this.ignoreBackoff() ? this.latestRunTime : Math.max(Math.max(this.latestRunTime, this.delayUntil), this.backoff);
   }

   public int compareTo(Object o) {
      SyncOperation other = (SyncOperation)o;
      if (this.expedited != other.expedited) {
         return this.expedited ? -1 : 1;
      } else {
         long thisIntervalStart = Math.max(this.effectiveRunTime - this.flexTime, 0L);
         long otherIntervalStart = Math.max(other.effectiveRunTime - other.flexTime, 0L);
         if (thisIntervalStart < otherIntervalStart) {
            return -1;
         } else {
            return otherIntervalStart < thisIntervalStart ? 1 : 0;
         }
      }
   }
}
