package com.carlos.common.persistent;

import android.os.Parcel;
import android.os.Parcelable;
import com.carlos.common.network.StringFog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VPersistent implements Parcelable {
   public static final int VERSION = 3;
   public static final String PRODUCT_ENV_SIT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC42CQ=="));
   public static final String PRODUCT_ENV_PROD = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBdfKmkVSFo="));
   public static String PRODUCT_ENV_KEY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBdfKmlTEiVsMgExKC1fBWwjGjA="));
   public int requestCount = 0;
   public long currentTimeMillis = 0L;
   public static final String upgradeEnforce = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdXImo3AiBoJSM8Ki42IG4jGlo="));
   public static final String upgradeVersion = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdXImo3AiBoIi8xLS0AOW8jMFo="));
   public static String fileName = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lz42L2kPOCNqJyBF"));
   public static final String fileMd5 = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lz42L2kPNCB8J1RF"));
   public static final String urlHost = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdfL2MnIClvDiRF"));
   public static final String uploadAppUrl = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdXL28nAiBmATMcLQU2JWozOFo="));
   public static final String downloadAppUrl = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxguMm83MClrJycBKRcEImMgGgNvEVRF"));
   public static final String uploadDevicesUrl = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwdXL28nAiBmAScxLi4uCWkKAhBlClE3"));
   public static final String downloadDevicesUrl = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxguMm83MClrJycBKggYJGwFAixqLiAgKRgcVg=="));
   public static final String requestTime = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4ADmUJEjVsMic9KBgYVg=="));
   public static final String heartbeatCount = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgAJGo0HiZoJzMiIT42JW8wBlo="));
   public final Map<String, String> buildAllConfig = new HashMap();
   public static final String adbHook = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KAgcJWRSPCllAVRF"));
   public static final String backupRecovery = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KD5bJmwkEjRnESM3KD1XD2owLFo="));
   public static final String dingtalk = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lxg2KWkkHiNqNx5F"));
   public static final String dingtalkPic = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lxg2KWkkHiNqNxFBLxgAVg=="));
   public static final String hookXposed = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguKmwqIDRqDjsxKghSVg=="));
   public static final String injectSo = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KggqLWkJQTBnAQ5F"));
   public static final String mockDevice = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQguJmwlHi9sEVw3KhhSVg=="));
   public static final String mockphone = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQguJmwkRSxqAQUx"));
   public static final String mockwifi = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQguJmwkGitoEV1F"));
   public static final String staticIp = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC0cJGVSJCV9JApF"));
   public static final String virtualbox = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz42D2VTEiNqNz87IwhSVg=="));
   public static final String virtuallocation = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz42D2VTEiNqNx07KT4IJmwFNCU="));
   public static final String channelLimit = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KC4MJG83OC9qNR09KBguJg=="));
   public static final String channelStatus = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KC4MJG83OC9qMjsiKRdfJWojSFo="));
   public static final Parcelable.Creator<VPersistent> CREATOR = new Parcelable.Creator<VPersistent>() {
      public VPersistent createFromParcel(Parcel source) {
         return new VPersistent(source);
      }

      public VPersistent[] newArray(int size) {
         return new VPersistent[size];
      }
   };

   public int describeContents() {
      long currentTimeMillis = System.currentTimeMillis();
      return 0;
   }

   public VPersistent() {
   }

   public String getBuildConfig(String key) {
      return (String)this.buildAllConfig.get(key);
   }

   public void setBuildConfig(String key, String value) {
      this.buildAllConfig.put(key, value);
   }

   public void readToParcel(Parcel in) {
      this.requestCount = in.readInt();
      this.currentTimeMillis = in.readLong();
      int buildAppConfigSize = in.readInt();

      for(int i = 0; i < buildAppConfigSize; ++i) {
         String key = in.readString();
         String value = in.readString();
         this.buildAllConfig.put(key, value);
      }

   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.requestCount);
      dest.writeLong(this.currentTimeMillis);
      dest.writeInt(this.buildAllConfig.size());
      Iterator var3 = this.buildAllConfig.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry<String, String> entry = (Map.Entry)var3.next();
         dest.writeString((String)entry.getKey());
         dest.writeString((String)entry.getValue());
      }

   }

   public VPersistent(Parcel in) {
      this.readToParcel(in);
   }
}
