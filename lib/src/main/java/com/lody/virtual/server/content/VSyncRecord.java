package com.lody.virtual.server.content;

import android.accounts.Account;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.lody.virtual.StringFog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VSyncRecord {
   public int userId;
   public SyncRecordKey key;
   public int syncable = -1;
   public boolean isPeriodic = false;
   public Map<SyncExtras, PeriodicSyncConfig> configs = new HashMap();
   public List<SyncExtras> extras = new ArrayList();

   public VSyncRecord(int userId, Account account, String authority) {
      this.userId = userId;
      this.key = new SyncRecordKey(account, authority);
   }

   public static boolean equals(Bundle a, Bundle b, boolean sameSize) {
      if (a == b) {
         return true;
      } else if (sameSize && a.size() != b.size()) {
         return false;
      } else {
         if (a.size() <= b.size()) {
            Bundle smaller = a;
            a = b;
            b = smaller;
         }

         Iterator var5 = a.keySet().iterator();

         String key;
         do {
            do {
               if (!var5.hasNext()) {
                  return true;
               }

               key = (String)var5.next();
            } while(!sameSize && isIgnoredKey(key));

            if (!b.containsKey(key)) {
               return false;
            }
         } while(a.get(key).equals(b.get(key)));

         return false;
      }
   }

   private static boolean isIgnoredKey(String str) {
      return str.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfKGgVMC9mHjAw"))) || str.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgmCGowFitsJyg/LBg2MW8VEgM="))) || str.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgmCGowFitsJCw7Ly0EDWkVHlo="))) || str.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgAH2ojGgZsJyw/LBguIQ=="))) || str.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4AKmszNFo="))) || str.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc6DmozJCw="))) || str.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguDmgaMC9gJFkpJi1fLGkgRQRqASwg"))) || str.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgYKWszJARiHx4wKAdbPWUzLCVlNDBF"))) || str.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfKGgVLAZiDgpALAgmCG8FQSw="))) || str.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfKGgVLAZiDgpAKBdfI28VOCVoASxF"))) || str.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YCGs2GgJhNAY1Iz0cLmgjSFo="))) || str.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggEDmowPB9gDjAgKAguPWkzSFo="))) || str.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcCWwFAjdgHgYiKAhSVg==")));
   }

   static class PeriodicSyncConfig implements Parcelable {
      long syncRunTimeSecs;
      public static final Parcelable.Creator<PeriodicSyncConfig> CREATOR = new Parcelable.Creator<PeriodicSyncConfig>() {
         public PeriodicSyncConfig createFromParcel(Parcel source) {
            return new PeriodicSyncConfig(source);
         }

         public PeriodicSyncConfig[] newArray(int size) {
            return new PeriodicSyncConfig[size];
         }
      };

      public PeriodicSyncConfig(long syncRunTimeSecs) {
         this.syncRunTimeSecs = syncRunTimeSecs;
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel dest, int flags) {
         dest.writeLong(this.syncRunTimeSecs);
      }

      PeriodicSyncConfig(Parcel in) {
         this.syncRunTimeSecs = in.readLong();
      }
   }

   public static class SyncRecordKey implements Parcelable {
      Account account;
      String authority;
      public static final Parcelable.Creator<SyncRecordKey> CREATOR = new Parcelable.Creator<SyncRecordKey>() {
         public SyncRecordKey createFromParcel(Parcel source) {
            return new SyncRecordKey(source);
         }

         public SyncRecordKey[] newArray(int size) {
            return new SyncRecordKey[size];
         }
      };

      SyncRecordKey(Account account, String authority) {
         this.account = account;
         this.authority = authority;
      }

      SyncRecordKey(Parcel in) {
         this.account = (Account)in.readParcelable(Account.class.getClassLoader());
         this.authority = in.readString();
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel dest, int flags) {
         dest.writeParcelable(this.account, flags);
         dest.writeString(this.authority);
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            SyncRecordKey that = (SyncRecordKey)o;
            if (this.account != null) {
               if (this.account.equals(that.account)) {
                  return this.authority != null ? this.authority.equals(that.authority) : that.authority == null;
               }
            } else if (that.account == null) {
               return this.authority != null ? this.authority.equals(that.authority) : that.authority == null;
            }

            return false;
         } else {
            return false;
         }
      }
   }

   public static class SyncExtras implements Parcelable {
      Bundle extras;
      public static final Parcelable.Creator<SyncExtras> CREATOR = new Parcelable.Creator<SyncExtras>() {
         public SyncExtras createFromParcel(Parcel source) {
            return new SyncExtras(source);
         }

         public SyncExtras[] newArray(int size) {
            return new SyncExtras[size];
         }
      };

      public SyncExtras(Bundle extras) {
         this.extras = extras;
      }

      SyncExtras(Parcel in) {
         this.extras = in.readBundle(this.getClass().getClassLoader());
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel dest, int flags) {
         dest.writeBundle(this.extras);
      }

      public boolean equals(Object obj) {
         return VSyncRecord.equals(this.extras, ((SyncExtras)obj).extras, false);
      }
   }
}
